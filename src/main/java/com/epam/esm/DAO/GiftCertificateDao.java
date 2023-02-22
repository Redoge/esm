package com.epam.esm.DAO;

import com.epam.esm.DAO.interfaces.GiftCertificateDaoInterface;
import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.dto.TagNestedDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.util.mappers.GiftCertificateRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public class GiftCertificateDao implements GiftCertificateDaoInterface {
    private static  final String FIND_ALL_QUERY = "SELECT * FROM gift_certificate LEFT JOIN gift_certificate_tag ON " +
            "gift_certificate.gift_certificate_id = gift_certificate_tag.gift_certificate_id LEFT JOIN tag " +
            "ON gift_certificate_tag.tag_id = tag.tag_id;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM gift_certificate LEFT JOIN gift_certificate_tag ON " +
            "gift_certificate.gift_certificate_id = gift_certificate_tag.gift_certificate_id LEFT JOIN tag ON " +
            "gift_certificate_tag.tag_id = tag.tag_id WHERE gift_certificate_tag.`gift_certificate_id` = ?;";
    private static final String DELETE_BY_ID_QUERY = "DELETE `gift_certificate_tag`, `gift_certificate`" +
            " FROM `gift_certificate_tag` INNER JOIN `gift_certificate` " +
            "ON (`gift_certificate`.`gift_certificate_id` = `gift_certificate_tag`.`gift_certificate_id`)  " +
            "WHERE `gift_certificate`.`gift_certificate_id` = ?;";
    private static final String SAVE_QUERY = "INSERT INTO gift_certificate (gift_certificate_name, " +
            "gift_certificate_description, gift_certificate_price, gift_certificate_duration, " +
            "gift_certificate_create_date, gift_certificate_last_update_date) VALUES (?, ?, ?, ?, ?, ?);";

    private static final String FIND_BY_PART_NAME_OR_DESCRIPTION_AND_TAG_NAME =
            "SELECT gc.*  FROM gift_certificate gc  INNER JOIN gift_certificate_tag gct " +
                    "ON gc.gift_certificate_id = gct.gift_certificate_id INNER JOIN tag t ON gct.tag_id = t.tag_id " +
                    "WHERE (gc.gift_certificate_name LIKE ? OR gc.gift_certificate_description LIKE ?)   " +
                    "AND t.tag_name = ? ORDER BY gc.gift_certificate_name ";

    private static final String FIND_BY_PART_NAME_OR_DESCRIPTION = "SELECT * FROM " +
            "gift_certificate WHERE gift_certificate_name LIKE ? OR gift_certificate_description " +
            "LIKE ? ;";

    private static final String FIND_BY_TAG_NAME= "SELECT gc.* FROM gift_certificate gc " +
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
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,giftCertificateRowMapper, id));
    }

    @Override
    public Set<GiftCertificateMainDto> findByPartNameOrDescriptionAndTagName(String nameOrDescription, String tagName) {
        return Set.copyOf(giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_BY_PART_NAME_OR_DESCRIPTION_AND_TAG_NAME,
                 "%"+nameOrDescription+"%", "%"+nameOrDescription+"%", tagName)));
    }

    @Override
    public Set<GiftCertificateMainDto> findByPartNameOrDescription(String nameOrDescription) {
        return Set.copyOf(giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_BY_PART_NAME_OR_DESCRIPTION,
                "%"+nameOrDescription+"%", "%"+nameOrDescription+"%")));
    }

    @Override
    public Set<GiftCertificateMainDto> findByTagName(String tagName) {
        return Set.copyOf(giftCertificateRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_BY_TAG_NAME, tagName)));
    }

    @Override
    @Transactional
    public boolean update(GiftCertificateMainDto gc) {
        long gcId = gc.getId();
        GiftCertificateMainDto oldGc = findById(gcId).orElse(null);
        if(oldGc == null || gcId==0) return false;
        boolean checkUpdate;


        boolean diffName = gc.getName()!=null && !gc.getName().equals(oldGc.getName());
        boolean diffDescription = gc.getDescription()!= null && !gc.getDescription().equals(oldGc.getDescription());
        boolean diffPrice = gc.getPrice()!=null && !gc.getPrice().equals(oldGc.getPrice());
        boolean diffDuration = gc.getDuration()!=0 && !(gc.getDuration()==oldGc.getDuration());

        checkUpdate = (diffName || diffDescription || diffPrice || diffDuration);

        if(checkUpdate){
            jdbcTemplate.update(UPDATE_ONLY_GC_QUERY, diffName?gc.getName():oldGc.getName(),
                    diffDescription?gc.getDescription():oldGc.getDescription(),
                    diffPrice?gc.getPrice():oldGc.getPrice(),
                    diffDuration?gc.getDuration():oldGc.getDuration(),
                    LocalDateTime.now(),
                    gcId);
        }

        List<TagNestedDto> tags = oldGc.getTags();
        List<TagNestedDto> newTags = gc.getTags();

        List<TagNestedDto> tagsThatNeedToRemove = tags.stream().filter(element -> !newTags.contains(element)).toList();
        List<TagNestedDto> tagsThatNeedToAdd = newTags.stream().filter(element -> !tags.contains(element)).toList();

        if(tags.stream().map(TagNestedDto::getId).toList().equals(newTags.stream().map(TagNestedDto::getId).toList())){
            return checkUpdate;
        }
        //removing tags
        for(var tag : tagsThatNeedToRemove){
            tagDao.removeFromGiftCertificateByTagIdAndCertId(tag.getId(), gcId);
        }

        //adding new tags
        for(var tag : tagsThatNeedToAdd){
            var tmpTag = tagDao.findByName(tag.getName());
            if(tmpTag.isPresent()){
                tagDao.addTagToCertificateByTagIdAndCertId(tmpTag.get().getId(), gcId);
            }else{
                tagDao.save(tag.getName());
                tmpTag = tagDao.findByName(tag.getName());
                tagDao.addTagToCertificateByTagIdAndCertId(tmpTag.get().getId(), gcId);
            }
        }
        return true;
    }

    @Override
    public boolean deleteById(long id) {
        return jdbcTemplate.update(DELETE_BY_ID_QUERY, id)!=0;
    }
    @Override
    public boolean save(GiftCertificate gc) {
        return jdbcTemplate.update(SAVE_QUERY, gc.getName(), gc.getDescription(), gc.getPrice(), gc.getDuration(),
                gc.getCreateDate(), gc.getLastUpdateDate())==1;
    }
}
