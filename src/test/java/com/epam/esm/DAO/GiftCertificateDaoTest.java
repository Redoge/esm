//package com.epam.esm.DAO;
//
//import com.epam.esm.dto.GiftCertificateMainDto;
//import com.epam.esm.dto.TagMainDto;
//import com.epam.esm.models.GiftCertificate;
//import com.epam.esm.models.Tag;
//import com.epam.esm.util.formatters.TimeFormatter;
//import com.epam.esm.util.mappers.rowMappers.GiftCertificateRowMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class GiftCertificateDaoTest {
//    private static final String FIND_ALL_QUERY = "SELECT * FROM gift_certificate LEFT JOIN gift_certificate_tag ON " +
//            "gift_certificate.gift_certificate_id = gift_certificate_tag.gift_certificate_id LEFT JOIN tag " +
//            "ON gift_certificate_tag.tag_id = tag.tag_id;";
//    private static final String FIND_BY_ID_QUERY = "SELECT * FROM gift_certificate LEFT JOIN gift_certificate_tag ON " +
//            "gift_certificate.gift_certificate_id = gift_certificate_tag.gift_certificate_id LEFT JOIN tag ON " +
//            "gift_certificate_tag.tag_id = tag.tag_id WHERE gift_certificate.`gift_certificate_id` = ?;";
//    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM gift_certificate LEFT JOIN gift_certificate_tag ON " +
//            "gift_certificate.gift_certificate_id = gift_certificate_tag.gift_certificate_id LEFT JOIN tag ON " +
//            "gift_certificate_tag.tag_id = tag.tag_id WHERE gift_certificate.`gift_certificate_name` = ?;";
//
//    private static final String DELETE_BY_ID_QUERY_PART_1 = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ?;";
//    private static final String DELETE_BY_ID_QUERY_PART_2 = "DELETE FROM gift_certificate WHERE gift_certificate_id = ?;";
//
//    private static final String SAVE_QUERY = "INSERT INTO gift_certificate (gift_certificate_name, " +
//            "gift_certificate_description, gift_certificate_price, gift_certificate_duration, " +
//            "gift_certificate_create_date, gift_certificate_last_update_date) SELECT ?, ?, ?, ?, ?, ? " +
//            "FROM dual WHERE NOT EXISTS (SELECT * FROM gift_certificate WHERE gift_certificate_name = ?)";
//
//    private static final String FIND_BY_PART_NAME_OR_DESCRIPTION_AND_TAG_NAME =
//            "SELECT *  FROM gift_certificate gc  LEFT JOIN gift_certificate_tag gct " +
//                    "ON gc.gift_certificate_id = gct.gift_certificate_id LEFT JOIN tag t ON gct.tag_id = t.tag_id " +
//                    "WHERE (gc.gift_certificate_name LIKE ? OR gc.gift_certificate_description LIKE ?)   " +
//                    "AND t.tag_name = ? ORDER BY gc.gift_certificate_name ";
//
//    private static final String FIND_BY_PART_NAME_OR_DESCRIPTION = "SELECT *  FROM gift_certificate gc  " +
//            "LEFT JOIN gift_certificate_tag gct ON gc.gift_certificate_id = gct.gift_certificate_id LEFT JOIN tag t " +
//            "ON gct.tag_id = t.tag_id WHERE (gc.gift_certificate_name LIKE ? OR gc.gift_certificate_description LIKE ?);";
//
//    private static final String FIND_BY_TAG_NAME = "SELECT * FROM gift_certificate gc " +
//            "INNER JOIN gift_certificate_tag gct ON gc.gift_certificate_id = gct.gift_certificate_id " +
//            "INNER JOIN tag t ON gct.tag_id = t.tag_id WHERE t.tag_name = ? ;";
//
//    private static final String UPDATE_ONLY_GC_QUERY = "UPDATE gift_certificate SET  gift_certificate_name = ?, " +
//            "gift_certificate_description = ?, gift_certificate_price = ? ,gift_certificate_duration = ?, " +
//            "gift_certificate_last_update_date = ? WHERE gift_certificate_id = ?;";
//    private final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
//    private final GiftCertificateRowMapper giftCertificateRowMapper = new GiftCertificateRowMapper(new TimeFormatter());
//    private final TagDao tagDao = mock(TagDao.class);
//    private final GiftCertificateDao dao = new GiftCertificateDao(jdbcTemplate, giftCertificateRowMapper, tagDao);
//    private final List<Map<String, Object>> testList= getTestList();
//    private final GiftCertificateMainDto testGiftCertificateMainDto = getGiftCertificateMainDtoForTest();
//    private final GiftCertificate testGiftCertificate = getGiftCertificateForTest();
//    @Test
//    void findAllEmptyTestTest() {
//        when(jdbcTemplate.queryForList(FIND_ALL_QUERY)).thenReturn(List.of());
//        assertEquals(List.of(), dao.findAll());
//    }
//    @Test
//    void findAllTestTest() {
//        when(jdbcTemplate.queryForList(FIND_ALL_QUERY)).thenReturn(testList);
//        assertEquals(2, dao.findAll().size());
//    }
//
//    @Test
//    void findByIdTest() {
//        long id = 1L;
//        when(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,giftCertificateRowMapper, id)).thenReturn(testGiftCertificateMainDto);
//        assertEquals(Optional.of(testGiftCertificateMainDto), dao.findById(id));
//    }
//    @Test
//    void findByIncorrectIdTest() {
//        long id = 1L;
//        when(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,giftCertificateRowMapper, id)).thenThrow(new EmptyResultDataAccessException(1));
//        assertEquals(Optional.empty(), dao.findById(id));
//    }
//
//    @Test
//    void findByNameTest() {
//        String name = "name1";
//        when(jdbcTemplate.queryForList(FIND_BY_NAME_QUERY, name)).thenReturn(new LinkedList<>(List.of(testList.get(1), testList.get(2))));
//        assertTrue( dao.findByName(name).isPresent());
//    }
//    @Test
//    void findByIncorrectNameTest() {
//        String name = "name1";
//        when(jdbcTemplate.queryForList(FIND_BY_NAME_QUERY, name)).thenReturn(new LinkedList<>());
//        assertFalse(dao.findByName(name).isPresent());
//    }
//
//    @Test
//    void findByPartNameOrDescriptionAndTagNameTest() {
//        String nameOrDescription = "Changed2";
//        String tagName = "name1";
//        when(jdbcTemplate.queryForList(FIND_BY_PART_NAME_OR_DESCRIPTION_AND_TAG_NAME,
//                "%" + nameOrDescription + "%", "%" + nameOrDescription + "%", tagName)).thenReturn(new LinkedList<>(List.of(testList.get(1), testList.get(2))));
//        assertEquals(1, dao.findByPartNameOrDescriptionAndTagName(nameOrDescription, tagName).size());
//    }
//
//    @Test
//    void findByPartNameOrDescriptionTest() {
//        String nameOrDescription = "name1";
//        when(jdbcTemplate.queryForList(FIND_BY_PART_NAME_OR_DESCRIPTION,
//                "%" + nameOrDescription + "%", "%" + nameOrDescription + "%")).thenReturn(new LinkedList<>(List.of(testList.get(1), testList.get(2))));
//        assertEquals(1, dao.findByPartNameOrDescription(nameOrDescription).size());
//    }
//
//    @Test
//    void findByTagNameTest() {
//        String tagName = "name1";
//        when(jdbcTemplate.queryForList(FIND_BY_TAG_NAME, tagName)).thenReturn(new LinkedList<>(List.of(testList.get(0), testList.get(2))));
//        assertEquals(2, dao.findByTagName(tagName).size());
//    }
//
//    @Test
//    void updateTest() {
//        when(jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,giftCertificateRowMapper,1L)).thenReturn(testGiftCertificateMainDto);
//
//        when(jdbcTemplate.update(eq(UPDATE_ONLY_GC_QUERY),
//                eq(testGiftCertificate.getName()),
//                eq(testGiftCertificate.getDescription()),
//                eq(testGiftCertificate.getPrice()),
//                eq(testGiftCertificate.getDuration()),
//                any(LocalDateTime.class),
//                eq(1L))).thenReturn(1);
//
//        when(jdbcTemplate.queryForList(eq(FIND_BY_NAME_QUERY), eq("Changed2")))
//                .thenReturn(new LinkedList<>(List.of(testList.get(1), testList.get(2))));
//
//        when(tagDao.findByName("name1"))
//                .thenReturn(Optional.of(new TagMainDto(1L, "name1")));
//        when(tagDao.findByName("name2"))
//                .thenReturn(Optional.of(new TagMainDto(2L, "name2")));
//
//        assertTrue(dao.update(testGiftCertificate, 1L));
//    }
//    @Test
//    void saveTest() {
//        when(jdbcTemplate.update(eq(SAVE_QUERY),
//                eq(testGiftCertificate.getName()),
//                eq(testGiftCertificate.getDescription()),
//                eq(testGiftCertificate.getPrice()),
//                eq(testGiftCertificate.getDuration()),
//                any(LocalDateTime.class),
//                any(LocalDateTime.class),
//                eq(testGiftCertificate.getName())))
//                .thenReturn(1);
//
//        when(jdbcTemplate.queryForList(eq(FIND_BY_NAME_QUERY), eq("Changed2")))
//                .thenReturn(new LinkedList<>(List.of(testList.get(1), testList.get(2))));
//
//        when(tagDao.findByName(anyString()))
//                .thenReturn(Optional.of(new TagMainDto(1L, "name1")));
//
//        assertTrue(dao.save(testGiftCertificate));
//    }
//    @Test
//    void deleteByIdTest() {
//        long id = 2L;
//        when(jdbcTemplate.update(DELETE_BY_ID_QUERY_PART_1, id)).thenReturn(1);
//        when(jdbcTemplate.update(DELETE_BY_ID_QUERY_PART_2, id)).thenReturn(1);
//        assertTrue(dao.deleteById(id));
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
//                Map.of("gift_certificate_id", "2", "gift_certificate_name", "Changed2","gift_certificate_description", "desc2",
//                        "gift_certificate_price", "1500.00","gift_certificate_duration", "122",
//                        "gift_certificate_create_date", "2022-10-06 00:00:00.0", "gift_certificate_last_update_date", "2023-07-22 18:18:02.0",
//                        "tag_id", "1", "tag_name", "name1"
//                )));
//    }
//
//    private GiftCertificateMainDto getGiftCertificateMainDtoForTest(){
//        return new GiftCertificateMainDto(1L, "Changed2", "description1", BigDecimal.valueOf(100),
//                10, "2023-02-28T15:43:42.1","2023-02-28T15:43:42.1", List.of(new Tag(1L,"name1")));
//    }
//    private GiftCertificate getGiftCertificateForTest(){
//        var gc =  new GiftCertificate();
//        gc.setName("Changed2");
//        gc.setDescription("description");
//        gc.setDuration(10);
//        gc.setPrice(BigDecimal.valueOf(1000));
//        gc.setLastUpdateDate(LocalDateTime.now());
//        gc.setCreateDate(LocalDateTime.now());
//        gc.setTags(List.of(new Tag(1L,"name1"), new Tag(2L,"name2")));
//        return gc;
//    }
//}