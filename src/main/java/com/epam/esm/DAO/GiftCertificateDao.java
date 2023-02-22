package com.epam.esm.DAO;

import com.epam.esm.DAO.interfaces.GiftCertificateDaoInterface;
import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.util.mappers.GiftCertificateRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
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
    private final JdbcTemplate jdbcTemplate;
    private final GiftCertificateRowMapper giftCertificateRowMapper;

    public GiftCertificateDao(JdbcTemplate jdbcTemplate, GiftCertificateRowMapper giftCertificateRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.giftCertificateRowMapper = giftCertificateRowMapper;
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
    public boolean update(GiftCertificate gc) {

        GiftCertificateMainDto oldGc = findById(gc.getId()).orElse(null);
        if(oldGc == null) return false;

        StringBuilder sb = new StringBuilder("UPDATE gift_certificate SET")
            .append(gc.getName()!=null && !gc.getName().equals(oldGc.getName())?" gift_certificate_name = ?,":"")
            .append(gc.getDescription()!= null && !gc.getDescription().equals(oldGc.getDescription())?
                    "gift_certificate_description = ?":"")
            .append(gc.getPrice()!=null && !gc.getPrice().equals(oldGc.getPrice())?"gift_certificate_price = ?":"")
            .append(gc.getDuration()!=0 && !(gc.getDuration()==oldGc.getDuration())?"gift_certificate_duration = ?":"");

        return false;//gift_certificate_last_update_date = ?
    }
//     gift_certificate_name = ?, gift_certificate_description = ?, gift_certificate_price = ?,
//     gift_certificate_duration = ?,
//     WHERE gift_certificate_id = ?"

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
