package com.epam.esm.DAO;

import com.epam.esm.DAO.interfaces.TagDaoInterface;
import com.epam.esm.dto.TagMainDto;
import com.epam.esm.util.mappers.TagRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
public class TagDao implements TagDaoInterface {
    private static  final String FIND_ALL_QUERY = "SELECT *  FROM tag LEFT JOIN gift_certificate_tag  on " +
            "tag.tag_id = gift_certificate_tag.tag_id LEFT JOIN gift_certificate  on " +
            "gift_certificate_tag.gift_certificate_id = gift_certificate.gift_certificate_id;";
    private static final String FIND_BY_ID_QUERY = "SELECT *  FROM tag LEFT JOIN gift_certificate_tag  on tag.tag_id = " +
            "gift_certificate_tag.tag_id LEFT JOIN gift_certificate  on gift_certificate_tag.gift_certificate_id = " +
            "gift_certificate.gift_certificate_id WHERE tag.`tag_id` = ?;";
        private static final String FIND_BY_NAME_QUERY = "SELECT *  FROM tag LEFT JOIN gift_certificate_tag  on tag.tag_id = " +
            "gift_certificate_tag.tag_id LEFT JOIN gift_certificate  on gift_certificate_tag.gift_certificate_id = " +
            "gift_certificate.gift_certificate_id WHERE tag.`tag_name` = ?;";

    private static final String DELETE_BY_ID_QUERY_PART1 = "DELETE FROM gift_certificate_tag WHERE tag_id = ?;";
    private static final String DELETE_BY_ID_QUERY_PART2 = "DELETE FROM tag WHERE tag_id = ?;";
    private static final String SAVE_QUERY = "INSERT INTO `tag` (tag_name) SELECT ? WHERE NOT " +
            "EXISTS (SELECT * FROM `tag` WHERE tag_name = ?)";
    private static final String DELETE_FROM_CERT_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY = "DELETE FROM gift_certificate_tag " +
            "WHERE tag_id = ? AND gift_certificate_id = ?;";
    private static final String ADD_TO_CERT_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY = "INSERT INTO gift_certificate_tag " +
            "(tag_id, gift_certificate_id) VALUE(?, ?);";
    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper tagRowMapper;

    public TagDao(JdbcTemplate jdbcTemplate, TagRowMapper tagRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = tagRowMapper;
    }
    @Override
    public List<TagMainDto> findAll() {
        return tagRowMapper.mapRowToList(jdbcTemplate.queryForList(FIND_ALL_QUERY));
    }
    @Override
    public Optional<TagMainDto> findById(long id) {
        try{
           return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,tagRowMapper, id));
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<TagMainDto> findByName(String name) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY,tagRowMapper, name));
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }
//    @Transactional
    @Override //TODO: @Transactional
    public boolean deleteById(long id) {
        boolean part1 = jdbcTemplate.update(DELETE_BY_ID_QUERY_PART1, id)!=0;
        boolean part2 = jdbcTemplate.update(DELETE_BY_ID_QUERY_PART2, id)!=0;
        return part1 || part2;
    }
    @Override
    public boolean save(String tagName) {
        return jdbcTemplate.update(SAVE_QUERY, tagName, tagName)==1;
    }
    @Override
    public boolean removeFromGiftCertificateByTagIdAndCertId(long tagId, long certId) {
        return jdbcTemplate.update(DELETE_FROM_CERT_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY, tagId, certId)!=0;
    }
    @Override
    public boolean addTagToCertificateByTagIdAndCertId( long tagId, long certId) {
        return jdbcTemplate.update(ADD_TO_CERT_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY, tagId, certId)!=0;
    }


}