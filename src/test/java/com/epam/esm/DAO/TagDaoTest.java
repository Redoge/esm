//package com.epam.esm.DAO;
//
//import com.epam.esm.dto.TagMainDto;
//import com.epam.esm.util.formatters.TimeFormatter;
//import com.epam.esm.util.mappers.rowMappers.TagRowMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//@ExtendWith(MockitoExtension.class)
//class TagDaoTest {
//    private static  final String FIND_ALL_QUERY = "SELECT *  FROM tag LEFT JOIN gift_certificate_tag  on " +
//            "tag.tag_id = gift_certificate_tag.tag_id LEFT JOIN gift_certificate  on " +
//            "gift_certificate_tag.gift_certificate_id = gift_certificate.gift_certificate_id;";
//    private static final String FIND_BY_ID_QUERY = "SELECT *  FROM tag LEFT JOIN gift_certificate_tag  on tag.tag_id = " +
//            "gift_certificate_tag.tag_id LEFT JOIN gift_certificate  on gift_certificate_tag.gift_certificate_id = " +
//            "gift_certificate.gift_certificate_id WHERE tag.`tag_id` = ?;";
//    private static final String FIND_BY_NAME_QUERY = "SELECT *  FROM tag LEFT JOIN gift_certificate_tag  on tag.tag_id = " +
//            "gift_certificate_tag.tag_id LEFT JOIN gift_certificate  on gift_certificate_tag.gift_certificate_id = " +
//            "gift_certificate.gift_certificate_id WHERE tag.`tag_name` = ?;";
//
//    private static final String DELETE_BY_ID_QUERY_PART1 = "DELETE FROM gift_certificate_tag WHERE tag_id = ?;";
//    private static final String DELETE_BY_ID_QUERY_PART2 = "DELETE FROM tag WHERE tag_id = ?;";
//    private static final String SAVE_QUERY = "INSERT INTO `tag` (tag_name) SELECT ? WHERE NOT " +
//            "EXISTS (SELECT * FROM `tag` WHERE tag_name = ?)";
//    private static final String DELETE_FROM_CERT_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY = "DELETE FROM gift_certificate_tag " +
//            "WHERE tag_id = ? AND gift_certificate_id = ?;";
//    private static final String ADD_TO_CERT_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY = "INSERT INTO gift_certificate_tag " +
//            "(tag_id, gift_certificate_id) VALUE(?, ?);";
//    private JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
//    private TagRowMapper tagRowMapper = new TagRowMapper(new TimeFormatter());
//    private TagDao tagDao = new TagDao(jdbcTemplate, tagRowMapper);
//    private final List<Map<String, Object>> testList= getTestList();
//    @Test
//    void findAllEmptyTest() {
//        when(jdbcTemplate.queryForList(FIND_ALL_QUERY)).thenReturn(List.of());
//        assertEquals(List.of(), tagDao.findAll());
//    }
//    @Test
//    void findAllTest() {
//        when(jdbcTemplate.queryForList(FIND_ALL_QUERY)).thenReturn(testList);
//        assertEquals(2, tagDao.findAll().size());
//    }
//
//    @Test
//    void findByIdTest() {
//        var tmp = new TagMainDto(2L, "name2");
//        long id = 2L;
//        when(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,tagRowMapper, id)).thenReturn(tmp);
//        assertEquals(Optional.of(tmp), tagDao.findById(id));
//    }
//    @Test
//    void findByIdIncorrectTest() {
//        when(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,tagRowMapper, 2L)).thenThrow(new EmptyResultDataAccessException(1));
//        assertEquals(Optional.empty(), tagDao.findById(2L));
//    }
//
//    @Test
//    void findByNameTest() {
//        var tmp = new TagMainDto(2L, "name2");
//        String name = "name2";
//        when(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY,tagRowMapper, name)).thenReturn(tmp);
//        assertEquals(Optional.of(tmp), tagDao.findByName(name));
//    }
//
//    @Test
//    void findByNameIncorrectTest() {
//        String name = "name2";
//        when(jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY,tagRowMapper, name)).thenThrow(new EmptyResultDataAccessException(1));
//        assertEquals(Optional.empty(), tagDao.findByName(name));
//    }
//
//    @Test
//    void deleteByIdTest() {
//        long id = 2L;
//        when(jdbcTemplate.update(DELETE_BY_ID_QUERY_PART1, id)).thenReturn(1);
//        when(jdbcTemplate.update(DELETE_BY_ID_QUERY_PART2, id)).thenReturn(1);
//        assertTrue(tagDao.deleteById(id));
//    }
//    @Test
//    void deleteByIdIncorrectTest() {
//        long id = 2L;
//        when(jdbcTemplate.update(DELETE_BY_ID_QUERY_PART1, id)).thenReturn(0);
//        when(jdbcTemplate.update(DELETE_BY_ID_QUERY_PART2, id)).thenReturn(0);
//        assertFalse(tagDao.deleteById(id));
//    }
//
//    @Test
//    void saveTest() {
//        String tagName = "name1";
//        when(jdbcTemplate.update(SAVE_QUERY, tagName, tagName)).thenReturn(1);
//        assertTrue(tagDao.save(tagName));
//    }
//    @Test
//    void saveIncorrectTest() {
//        String tagName = "name1";
//        when(jdbcTemplate.update(SAVE_QUERY, tagName, tagName)).thenReturn(0);
//        assertFalse(tagDao.save(tagName));
//    }
//
//    @Test
//    void removeFromGiftCertificateByTagIdAndCertIdTest() {
//        long tagId = 1L;
//        long certId = 1L;
//        when(jdbcTemplate.update(DELETE_FROM_CERT_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY, tagId, certId)).thenReturn(1);
//        assertTrue(tagDao.removeFromGiftCertificateByTagIdAndCertId(tagId, certId));
//    }
//
//    @Test
//    void addTagToCertificateByTagIdAndCertIdTest() {
//        long tagId = 1L;
//        long certId = 1L;
//        when(jdbcTemplate.update(ADD_TO_CERT_BY_TAG_ID_AND_CERTIFICATE_ID_QUERY, tagId, certId)).thenReturn(1);
//        assertTrue(tagDao.addTagToCertificateByTagIdAndCertId(tagId, certId));
//    }
//
//
//    private List<Map<String, Object>> getTestList(){
//        return new ArrayList<>(List.of(
//                Map.of("gift_certificate_id", "1", "gift_certificate_name", "Changed1","gift_certificate_description", "desc1",
//                        "gift_certificate_price", "150.00","gift_certificate_duration", "12",
//                        "gift_certificate_create_date", "2022-10-05 00:00:00.0", "gift_certificate_last_update_date", "2023-02-22 18:18:02.0",
//                        "tag_id", "1", "tag_name", "name1"
//                ),
//                Map.of("gift_certificate_id", "2", "gift_certificate_name", "Changed2","gift_certificate_description", "desc2",
//                        "gift_certificate_price", "1500.00","gift_certificate_duration", "122",
//                        "gift_certificate_create_date", "2022-10-06 00:00:00.0", "gift_certificate_last_update_date", "2023-07-22 18:18:02.0",
//                        "tag_id", "2", "tag_name", "name2"
//                ),
//                Map.of("gift_certificate_id", "3", "gift_certificate_name", "Changed3","gift_certificate_description", "desc3",
//                        "gift_certificate_price", "1500.00","gift_certificate_duration", "122",
//                        "gift_certificate_create_date", "2022-10-06 00:00:00.0", "gift_certificate_last_update_date", "2023-07-22 18:18:02.0",
//                        "tag_id", "1", "tag_name", "name1"
//                )));
//    }
//}