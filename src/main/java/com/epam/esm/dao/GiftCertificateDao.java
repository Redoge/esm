package com.epam.esm.dao;

import com.epam.esm.dao.interfaces.GiftCertificateDaoInterface;
import com.epam.esm.dao.interfaces.ManyToManyRelation.GiftCertificateTagInterface;
import com.epam.esm.dao.interfaces.TagDaoInterface;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.interfaces.TagInterface;
import com.epam.esm.util.filters.TagFilter;
import com.epam.esm.util.mappers.rowMappers.GiftCertificateRowMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Repository
public class GiftCertificateDao implements GiftCertificateDaoInterface {
    private static final String FIND_ALL_QUERY = "SELECT * FROM gift_certificate " +
            "LEFT JOIN gift_certificate_tag " +
            "ON gift_certificate.gift_certificate_id = gift_certificate_tag.gift_certificate_id " +
            "LEFT JOIN tag ON gift_certificate_tag.tag_id = tag.tag_id;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM gift_certificate " +
            "LEFT JOIN gift_certificate_tag ON gift_certificate.gift_certificate_id = gift_certificate_tag.gift_certificate_id " +
            "LEFT JOIN tag ON gift_certificate_tag.tag_id = tag.tag_id WHERE gift_certificate.`gift_certificate_id` = ?;";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM gift_certificate " +
            "LEFT JOIN gift_certificate_tag ON gift_certificate.gift_certificate_id = gift_certificate_tag.gift_certificate_id " +
            "LEFT JOIN tag ON gift_certificate_tag.tag_id = tag.tag_id WHERE gift_certificate.`gift_certificate_name` = ?;";

    private static final String DELETE_BY_ID_QUERY = "DELETE FROM gift_certificate WHERE gift_certificate_id = ?;";

    private static final String SAVE_QUERY = "INSERT INTO gift_certificate (gift_certificate_name, " +
            "gift_certificate_description, gift_certificate_price, gift_certificate_duration, " +
            "gift_certificate_create_date, gift_certificate_last_update_date) " +
            "SELECT ?, ?, ?, ?, ?, ? FROM dual WHERE NOT EXISTS " +
            "(SELECT * FROM gift_certificate WHERE gift_certificate_name = ?)";

    private static final String FIND_BY_PART_NAME_OR_DESCRIPTION_AND_TAG_NAME =
            "SELECT *  FROM gift_certificate gc  LEFT JOIN gift_certificate_tag gct " +
                    "ON gc.gift_certificate_id = gct.gift_certificate_id LEFT JOIN tag t ON gct.tag_id = t.tag_id " +
                    "WHERE (gc.gift_certificate_name LIKE ? OR gc.gift_certificate_description LIKE ?)   " +
                    "AND t.tag_name = ? ORDER BY gc.gift_certificate_name ";

    private static final String FIND_BY_PART_NAME_OR_DESCRIPTION = "SELECT *  FROM gift_certificate gc  " +
            "LEFT JOIN gift_certificate_tag gct ON gc.gift_certificate_id = gct.gift_certificate_id LEFT JOIN tag t " +
            "ON gct.tag_id = t.tag_id WHERE (gc.gift_certificate_name LIKE ? OR gc.gift_certificate_description LIKE ?);";

    private static final String FIND_BY_TAG_NAME = "SELECT * FROM gift_certificate gc " +
            "INNER JOIN gift_certificate_tag gct ON gc.gift_certificate_id = gct.gift_certificate_id " +
            "INNER JOIN tag t ON gct.tag_id = t.tag_id WHERE t.tag_name = ? ;";

    private static final String UPDATE_ONLY_GC_QUERY = "UPDATE gift_certificate SET  gift_certificate_name = ?, " +
            "gift_certificate_description = ?, gift_certificate_price = ? ,gift_certificate_duration = ?, " +
            "gift_certificate_last_update_date = ? WHERE gift_certificate_id = ?;";
    private final JdbcTemplate jdbcTemplate;
    private final GiftCertificateRowMapper giftCertificateRowMapper;
    private final TagDaoInterface tagDao;
    private final TagFilter tagFilter;
    private final GiftCertificateTagInterface giftCertificateTagDao;
    private final String SQL_WILDCARDS = "%";

    public GiftCertificateDao(JdbcTemplate jdbcTemplate, GiftCertificateRowMapper giftCertificateRowMapper, TagDaoInterface tagDao, TagFilter tagFilter, GiftCertificateTagInterface giftCertificateTagDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.giftCertificateRowMapper = giftCertificateRowMapper;
        this.tagDao = tagDao;
        this.tagFilter = tagFilter;
        this.giftCertificateTagDao = giftCertificateTagDao;
    }

    @Override
    public List<GiftCertificate> findAll() {
        return giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_ALL_QUERY));
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, giftCertificateRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, giftCertificateRowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<GiftCertificate> findByPartNameOrDescriptionAndTagName(String nameOrDescription, String tagName) {
        return giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_BY_PART_NAME_OR_DESCRIPTION_AND_TAG_NAME,
                SQL_WILDCARDS + nameOrDescription + SQL_WILDCARDS,
                SQL_WILDCARDS + nameOrDescription + SQL_WILDCARDS, tagName));
    }

    @Override
    public List<GiftCertificate> findByPartNameOrDescription(String nameOrDescription) {
        return giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_BY_PART_NAME_OR_DESCRIPTION,
                SQL_WILDCARDS + nameOrDescription + SQL_WILDCARDS,
                SQL_WILDCARDS + nameOrDescription + SQL_WILDCARDS));
    }

    @Override
    public List<GiftCertificate> findByTagName(String tagName) {
        return giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_BY_TAG_NAME, tagName));
    }

    @Override
    public boolean update(GiftCertificate gCert) {
        var gCertId = gCert.getId();
        GiftCertificate oldGCert = findById(gCertId).orElse(null);
        //if id <= 0 -> bad request
        if (oldGCert == null || gCertId <= 0) return false;
        boolean correct = updateOnlyNewFieldsGiftCertificate(oldGCert, gCert, gCertId);
        if (!correct) return false;
        //If the tags have not received (is null) anything to do
        if(gCert.getTags()==null) return true;
        List<TagInterface> oldTags = oldGCert.getTags();
        List<TagInterface> newTags = gCert.getTags();
        if (oldTags.equals(newTags) && StringUtils.isNotEmpty(oldTags.get(0).getName()))
            return false;
        removeOldTagFromCertificate(tagFilter.filterTagsThatNeedToRemoveFromCerts(oldTags, newTags), gCertId);
        addNewTagToCertificate(tagFilter.filterTagsThatNeedToAddToCerts(oldTags, newTags), gCertId);
        return true;
    }


    @Override
    public boolean deleteById(long id) {
        return jdbcTemplate.update(DELETE_BY_ID_QUERY, id) != 0;
    }

    @Override
    public boolean save(GiftCertificate gCert) {
        boolean success = jdbcTemplate.update(SAVE_QUERY, gCert.getName(), gCert.getDescription(), gCert.getPrice(),
                gCert.getDuration(), LocalDateTime.now(), LocalDateTime.now(), gCert.getName()) == 1;
        Optional<GiftCertificate> createdGCert = findByName(gCert.getName());
        if (success && isNotEmpty(gCert.getTags()) && createdGCert.isPresent()) {
            addNewTagToCertificate(gCert.getTags(), createdGCert.get().getId());
        }
        return success;
    }

    private void addNewTagToCertificate(List<TagInterface> tags, long certId) {
        var allTags = tagDao.findAll();
        for (var tag : tags) {
            var findedTag = allTags
                    .stream()
                    .filter(existTag->existTag.getName()
                            .equals(tag.getName()))
                    .findAny();
            if (findedTag.isPresent()) {
                giftCertificateTagDao.addTagToCertificateByTagIdAndCertId(findedTag.get().getId(), certId);
            } else {
                tagDao.saveByName(tag.getName());
                findedTag = tagDao.findByName(tag.getName());
                giftCertificateTagDao.addTagToCertificateByTagIdAndCertId(findedTag.get().getId(), certId);
            }
        }
    }
    private void removeOldTagFromCertificate(List<TagInterface> tags, long certId) {
        for (var tag : tags) {
            giftCertificateTagDao.removeFromGiftCertificateByTagIdAndCertId(tagDao.findByName(tag.getName()).get().getId(), certId);
        }
    }
    private boolean updateOnlyNewFieldsGiftCertificate(GiftCertificate oldGCert, GiftCertificate newGCert, long id){
        boolean diffName = newGCert.getName() != null && !newGCert.getName().equals(oldGCert.getName());
        boolean diffDescription = newGCert.getDescription() != null && !newGCert.getDescription().equals(oldGCert.getDescription());
        boolean diffPrice = newGCert.getPrice() != null && !newGCert.getPrice().equals(oldGCert.getPrice());
        boolean diffDuration = newGCert.getDuration() != 0 && (newGCert.getDuration() != oldGCert.getDuration());
        boolean needToUpdate = (diffName || diffDescription || diffPrice || diffDuration);
        if (needToUpdate) {
            try{
                jdbcTemplate.update(UPDATE_ONLY_GC_QUERY, diffName ? newGCert.getName() : oldGCert.getName(),
                        diffDescription ? newGCert.getDescription() : oldGCert.getDescription(),
                        diffPrice ? newGCert.getPrice() : oldGCert.getPrice(),
                        diffDuration ? newGCert.getDuration() : oldGCert.getDuration(),
                        LocalDateTime.now(),
                        id);
            }catch (DataAccessException e){
                return false;
            }
        }
        return true;
    }
}