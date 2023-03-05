package com.epam.esm.DAO;

import com.epam.esm.DAO.interfaces.GiftCertificateDaoInterface;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.interfaces.TagInterface;
import com.epam.esm.util.mappers.rowMappers.GiftCertificateRowMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private final TagDao tagDao;

    public GiftCertificateDao(JdbcTemplate jdbcTemplate, GiftCertificateRowMapper giftCertificateRowMapper, TagDao tagDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.giftCertificateRowMapper = giftCertificateRowMapper;
        this.tagDao = tagDao;
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
    public Set<GiftCertificate> findByPartNameOrDescriptionAndTagName(String nameOrDescription, String tagName) {
        return Set.copyOf(giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_BY_PART_NAME_OR_DESCRIPTION_AND_TAG_NAME,
                "%" + nameOrDescription + "%", "%" + nameOrDescription + "%", tagName)));
    }

    @Override
    public Set<GiftCertificate> findByPartNameOrDescription(String nameOrDescription) {
        return Set.copyOf(giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_BY_PART_NAME_OR_DESCRIPTION,
                "%" + nameOrDescription + "%", "%" + nameOrDescription + "%")));
    }

    @Override
    public Set<GiftCertificate> findByTagName(String tagName) {
        return Set.copyOf(giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_BY_TAG_NAME, tagName)));
    }

    @Override
    public boolean update(GiftCertificate gCert, long gcId) {
        GiftCertificate oldGc = findById(gcId).orElse(null);
        //if id <= 0 -> bad request
        if (oldGc == null || gcId <= 0) return false;
        boolean correct = updateOnlyNewGiftCertificate(oldGc, gCert, gcId);
        if (!correct) return false;
        //If the tags have not received (is null) anything to do
        if(gCert.getTags()==null) return true;
        List<TagInterface> oldTags = oldGc.getTags();
        List<TagInterface> newTags = gCert.getTags();
        if (oldTags.equals(newTags) && StringUtils.isNotEmpty(oldTags.get(0).getName()))
            return false;
        removeOldTagFromCertificate(getTagsThatNeedToRemove(oldTags, newTags), gcId);
        addNewTagToCertificate(getTagsThatNeedToAdd(oldTags, newTags), gcId);
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
        Optional<GiftCertificate> createdGc = findByName(gCert.getName());
        if (success && isNotEmpty(gCert.getTags()) && createdGc.isPresent()) {
            addNewTagToCertificate(gCert.getTags(), createdGc.get().getId());
        }
        return success;
    }

    private void addNewTagToCertificate(List<TagInterface> tags, long certId) {
        for (var tag : tags) {
            var tmpTag = tagDao.findByName(tag.getName());
            if (tmpTag.isPresent()) {
                tagDao.addTagToCertificateByTagIdAndCertId(tmpTag.get().getId(), certId);
            } else {
                tagDao.save(tag.getName());
                tmpTag = tagDao.findByName(tag.getName());
                tagDao.addTagToCertificateByTagIdAndCertId(tmpTag.get().getId(), certId);
            }
        }
    }

    private void removeOldTagFromCertificate(List<TagInterface> tags, long certId) {
        for (var tag : tags) {
            tagDao.removeFromGiftCertificateByTagIdAndCertId(tagDao.findByName(tag.getName()).get().getId(), certId);
        }
    }

    private List<TagInterface> getTagsThatNeedToRemove(List<TagInterface> oldTags, List<TagInterface> newTags) {
        if (isNotEmpty(oldTags) || (oldTags.size() == 1 && oldTags.get(0).getName() != null)) {
            return oldTags.stream()
                    .filter(tag -> !newTags.contains(tag.getName()))
                    .toList();
        }
        return List.of();
    }

    private List<TagInterface> getTagsThatNeedToAdd(List<TagInterface> oldTags, List<TagInterface> newTags) {
        if (isNotEmpty(oldTags) || (oldTags.size() == 1 && oldTags.get(0).getName() != null)) {
            return newTags.stream()
                    .filter(tag -> !oldTags.contains(tag.getName()))
                    .toList();
        }
        return newTags;
    }
    private boolean updateOnlyNewGiftCertificate(GiftCertificate oldGc, GiftCertificate newGc, long id){
        boolean diffName = newGc.getName() != null && !newGc.getName().equals(oldGc.getName());
        boolean diffDescription = newGc.getDescription() != null && !newGc.getDescription().equals(oldGc.getDescription());
        boolean diffPrice = newGc.getPrice() != null && !newGc.getPrice().equals(oldGc.getPrice());
        boolean diffDuration = newGc.getDuration() != 0 && !(newGc.getDuration() == oldGc.getDuration());
        boolean needToUpdate = (diffName || diffDescription || diffPrice || diffDuration);
        if (needToUpdate) {
            try{
                jdbcTemplate.update(UPDATE_ONLY_GC_QUERY, diffName ? newGc.getName() : oldGc.getName(),
                        diffDescription ? newGc.getDescription() : oldGc.getDescription(),
                        diffPrice ? newGc.getPrice() : oldGc.getPrice(),
                        diffDuration ? newGc.getDuration() : oldGc.getDuration(),
                        LocalDateTime.now(),
                        id);
            }catch (DataAccessException e){
                return false;
            }
        }
        return true;
    }
}