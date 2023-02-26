package com.epam.esm.DAO;

import com.epam.esm.DAO.interfaces.GiftCertificateDaoInterface;
import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.interfaces.TagInterface;
import com.epam.esm.util.mappers.GiftCertificateRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GiftCertificateDao implements GiftCertificateDaoInterface {
    private static final String FIND_ALL_QUERY = "SELECT * FROM gift_certificate LEFT JOIN gift_certificate_tag ON " +
            "gift_certificate.gift_certificate_id = gift_certificate_tag.gift_certificate_id LEFT JOIN tag " +
            "ON gift_certificate_tag.tag_id = tag.tag_id;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM gift_certificate LEFT JOIN gift_certificate_tag ON " +
            "gift_certificate.gift_certificate_id = gift_certificate_tag.gift_certificate_id LEFT JOIN tag ON " +
            "gift_certificate_tag.tag_id = tag.tag_id WHERE gift_certificate.`gift_certificate_id` = ?;";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM gift_certificate LEFT JOIN gift_certificate_tag ON " +
            "gift_certificate.gift_certificate_id = gift_certificate_tag.gift_certificate_id LEFT JOIN tag ON " +
            "gift_certificate_tag.tag_id = tag.tag_id WHERE gift_certificate.`gift_certificate_name` = ?;";

    private static final String DELETE_BY_ID_QUERY_PART_1 = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ?;";
    private static final String DELETE_BY_ID_QUERY_PART_2 = "DELETE FROM gift_certificate WHERE gift_certificate_id = ?;";

    private static final String SAVE_QUERY = "INSERT INTO gift_certificate (gift_certificate_name, " +
            "gift_certificate_description, gift_certificate_price, gift_certificate_duration, " +
            "gift_certificate_create_date, gift_certificate_last_update_date) SELECT ?, ?, ?, ?, ?, ? " +
            "FROM dual WHERE NOT EXISTS (SELECT * FROM gift_certificate WHERE gift_certificate_name = ?)";

    private static final String FIND_BY_PART_NAME_OR_DESCRIPTION_AND_TAG_NAME =
            "SELECT *  FROM gift_certificate gc  LEFT JOIN gift_certificate_tag gct " +
                    "ON gc.gift_certificate_id = gct.gift_certificate_id LEFT JOIN tag t ON gct.tag_id = t.tag_id " +
                    "WHERE (gc.gift_certificate_name LIKE ? OR gc.gift_certificate_description LIKE ?)   " +
                    "AND t.tag_name = ? ORDER BY gc.gift_certificate_name ";

    private static final String FIND_BY_PART_NAME_OR_DESCRIPTION = "SELECT *  FROM gift_certificate gc  " +
            "LEFT JOIN gift_certificate_tag gct ON gc.gift_certificate_id = gct.gift_certificate_id LEFT JOIN tag t " +
            "ON gct.tag_id = t.tag_id WHERE (gc.gift_certificate_name LIKE ? OR gc.gift_certificate_description LIKE ?);";

    private static final String FIND_BY_TAG_NAME = "SELECT gc.* FROM gift_certificate gc " +
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
    public List<GiftCertificateMainDto> findAll() {
        return giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_ALL_QUERY));
    }
    @Override
    public Optional<GiftCertificateMainDto> findById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, giftCertificateRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    @Override
    public Optional<GiftCertificateMainDto> findByName(String name) {
        var certificates = giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_BY_NAME_QUERY, name));
        if (certificates.size() > 0) {
            return Optional.ofNullable(certificates.get(0));
        } else {
            return Optional.empty();
        }
    }
    @Override
    public Set<GiftCertificateMainDto> findByPartNameOrDescriptionAndTagName(String nameOrDescription, String tagName) {
        return Set.copyOf(giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_BY_PART_NAME_OR_DESCRIPTION_AND_TAG_NAME,
                "%" + nameOrDescription + "%", "%" + nameOrDescription + "%", tagName)));
    }
    @Override
    public Set<GiftCertificateMainDto> findByPartNameOrDescription(String nameOrDescription) {
        return Set.copyOf(giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_BY_PART_NAME_OR_DESCRIPTION,
                "%" + nameOrDescription + "%", "%" + nameOrDescription + "%")));
    }
    @Override
    public Set<GiftCertificateMainDto> findByTagName(String tagName) {
        return Set.copyOf(giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_BY_TAG_NAME, tagName)));
    }
    @Override
    public boolean update(GiftCertificate gc, long gcId) {
        GiftCertificateMainDto oldGc = findById(gcId).orElse(null);
        if (oldGc == null || gcId == 0) return false;
        boolean diffName = gc.getName() != null && !gc.getName().equals(oldGc.getName());
        boolean diffDescription = gc.getDescription() != null && !gc.getDescription().equals(oldGc.getDescription());
        boolean diffPrice = gc.getPrice() != null && !gc.getPrice().equals(oldGc.getPrice());
        boolean diffDuration = gc.getDuration() != 0 && !(gc.getDuration() == oldGc.getDuration());
        boolean needToUpdate = (diffName || diffDescription || diffPrice || diffDuration);
        if (needToUpdate) {
            jdbcTemplate.update(UPDATE_ONLY_GC_QUERY, diffName ? gc.getName() : oldGc.getName(),
                    diffDescription ? gc.getDescription() : oldGc.getDescription(),
                    diffPrice ? gc.getPrice() : oldGc.getPrice(),
                    diffDuration ? gc.getDuration() : oldGc.getDuration(),
                    LocalDateTime.now(),
                    gcId);
        }
        //If the tags have not received anything to do
        if (gc.getTags() == null) return needToUpdate;
        List<TagInterface> oldTags = oldGc.getTags();
        List<TagInterface> newTags = gc.getTags();
        List<TagInterface> tagsThatNeedToRemove;
        List<TagInterface> tagsThatNeedToAdd;
        if ((!oldTags.isEmpty() && oldTags.size() > 1) || (oldTags.size() == 1 && oldTags.get(0).getName() != null)) {
            if (oldTags.equals(newTags) && oldTags.get(0).getName() != null) return needToUpdate;
            tagsThatNeedToRemove = oldTags.stream().filter(e -> !newTags.contains(e.getName())).toList();
            tagsThatNeedToAdd = newTags.stream().filter(e -> !oldTags.contains(e.getName())).toList();
        } else {
            tagsThatNeedToRemove = List.of();
            tagsThatNeedToAdd = newTags;
        }
        removeOldTagFromCertificate(tagsThatNeedToRemove, gcId);
        addNewTagToCertificate(tagsThatNeedToAdd, gcId);
        return true;
    }
    @Override
    public boolean deleteById(long id) {
        boolean part1 = jdbcTemplate.update(DELETE_BY_ID_QUERY_PART_1, id) != 0;
        boolean part2 = jdbcTemplate.update(DELETE_BY_ID_QUERY_PART_2, id) != 0;
        return part1 || part2;
    }
    @Override
    public boolean save(GiftCertificate gc) {
        boolean success = jdbcTemplate.update(SAVE_QUERY, gc.getName(), gc.getDescription(), gc.getPrice(), gc.getDuration(),
                LocalDateTime.now(), LocalDateTime.now(), gc.getName()) == 1;
        Optional<GiftCertificateMainDto> createdGc = findByName(gc.getName());
        if (success && gc.getTags() != null && !gc.getTags().isEmpty() && createdGc.isPresent()) {
            addNewTagToCertificate(gc.getTags(), createdGc.get().getId());
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
}