package com.epam.esm.services;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.Tag;
import com.epam.esm.pojo.GiftCertificateSaveRequestPojo;
import com.epam.esm.pojo.GiftCertificateSearchRequestPojo;
import com.epam.esm.util.filters.GiftCertificateFilter;
import com.epam.esm.util.formatters.TimeFormatter;
import com.epam.esm.util.mappers.GiftCertificateMapper;
import com.epam.esm.util.sorters.GiftCertificateSorter;
import com.epam.esm.util.sorters.enums.SortingOrder;
import com.epam.esm.util.sorters.enums.SortingType;
import com.epam.esm.util.validators.GiftCertificateValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {
    private final GiftCertificateDao giftCertificateDao = mock(GiftCertificateDao.class);
    private final TimeFormatter timeFormatter = new TimeFormatter();
    private final GiftCertificateMapper giftCertificateMapper = new GiftCertificateMapper(timeFormatter);
    private final GiftCertificateValidator giftCertificateValidator = new GiftCertificateValidator();
    private final GiftCertificateFilter giftCertificateFilter = new GiftCertificateFilter();
    private final GiftCertificateSorter giftCertificateSorter = new GiftCertificateSorter();
    private final GiftCertificateService giftCertificateService = new GiftCertificateService(giftCertificateDao,
            giftCertificateMapper, giftCertificateValidator, giftCertificateFilter, giftCertificateSorter);
    private final List<GiftCertificate> certificates = createCertificatesForTest();
    private final List<GiftCertificateMainDto> certificatesDto = certificates
            .stream()
            .map(giftCertificateMapper::mapGiftCertToMainDto)
            .toList();
    private final long TEST_ID = 1L;
    private final String TEST_NAME = "name1";
    private final String TEST_NAME_WITH_TAG = "name4";
    private final String TEST_NAME_INCORRECT = "name969";
    private final String TEST_DESCRIPTION = "description3";
    @Test
    @DisplayName("getAll Test")
    void getAllTest() {
        when(giftCertificateDao.findAll()).thenReturn(certificates);

        var result = giftCertificateService.getAll();
        assertEquals(certificatesDto, result);
    }

    @Test
    @DisplayName("getAll Test - empty result")
    void getAllEmptyTest() {
        when(giftCertificateDao.findAll()).thenReturn(List.of());

        var result = giftCertificateService.getAll();
        assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("getById Test")
    void getByIdTest() {
        when(giftCertificateDao.findById(TEST_ID)).thenReturn(Optional.ofNullable(certificates.get(0)));

        var result = giftCertificateService.getById(TEST_ID).get();
        assertEquals(certificatesDto.get(0), result);
    }

    @Test
    @DisplayName("getById Test - empty result")
    void getByIdEmptyTest() {
        when(giftCertificateDao.findById(TEST_ID)).thenReturn(Optional.empty());

        var result = giftCertificateService.getById(TEST_ID);
        assertEquals(Optional.empty(), result);
    }

    @Test
    @DisplayName("getByName Test")
    void getByNameTest() {
        when(giftCertificateDao.findByName(TEST_NAME)).thenReturn(Optional.ofNullable(certificates.get(0)));

        var result = giftCertificateService.getByName(TEST_NAME).get();
        assertEquals(certificatesDto.get(0), result);
    }

    @Test
    @DisplayName("getByName Test - empty result")
    void getByNameEmptyTest() {
        when(giftCertificateDao.findByName(TEST_NAME_INCORRECT)).thenReturn(Optional.empty());

        var result = giftCertificateService.getByName(TEST_NAME_INCORRECT);
        assertEquals(Optional.empty(), result);
    }

    @Test
    @DisplayName("getByPartNameOrDescriptionAndTagName Test")
    void getByPartNameOrDescriptionAndTagNameTest() {
        when(giftCertificateDao.findByPartNameOrDescription(TEST_NAME_WITH_TAG)).
                thenReturn(List.of(certificates.get(3)));

        var result = giftCertificateService.getByPartNameOrDescriptionAndTagName(TEST_NAME_WITH_TAG, TEST_NAME);
        assertEquals(List.of(certificatesDto.get(3)), result);
    }

    @Test
    @DisplayName("getByPartNameOrDescriptionAndTagName Test - empty result")
    void getByPartNameOrDescriptionAndTagNameEmptyTest() {
        when(giftCertificateDao.findByPartNameOrDescription(TEST_NAME_WITH_TAG)).
                thenReturn(List.of(certificates.get(3)));

        var result = giftCertificateService.getByPartNameOrDescriptionAndTagName(TEST_NAME_WITH_TAG, TEST_NAME_INCORRECT);
        assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("findByPartNameOrDescription with Name Test")
    void getByPartNameOrDescriptionByNameTest() {
        when(giftCertificateDao.findByPartNameOrDescription(TEST_NAME_WITH_TAG)).
                thenReturn(List.of(certificates.get(3)));

        var result = giftCertificateService.getByPartNameOrDescription(TEST_NAME_WITH_TAG);
        assertEquals(List.of(certificatesDto.get(3)), result);
    }

    @Test
    @DisplayName("findByPartNameOrDescription with Description Test")
    void getByPartNameOrDescriptionByDescriptionTest() {
        when(giftCertificateDao.findByPartNameOrDescription(TEST_DESCRIPTION)).
                thenReturn(List.of(certificates.get(3)));

        var result = giftCertificateService.getByPartNameOrDescription(TEST_DESCRIPTION);
        assertEquals(List.of(certificatesDto.get(3)), result);
    }

    @Test
    @DisplayName("findByPartNameOrDescription Test - empty result")
    void getByPartNameOrDescriptionEmptyTest() {
        when(giftCertificateDao.findByPartNameOrDescription(TEST_DESCRIPTION)).
                thenReturn(List.of());

        var result = giftCertificateService.getByPartNameOrDescription(TEST_DESCRIPTION);
        assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Get by tag name")
    void getByTagNameTest() {
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);

        var result = giftCertificateService.getByTagName(TEST_NAME);
        assertEquals(List.of(certificatesDto.get(2),certificatesDto.get(3)), result);
    }

    @Test
    @DisplayName("Get by tag name - empty result")
    void getByTagNameEmptyTest() {
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);

        var result = giftCertificateService.getByTagName(TEST_NAME_INCORRECT);
        assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Save Test - CORRECT")
    void deleteByIdTest() {
        when(giftCertificateDao.deleteById(TEST_ID)).
                thenReturn(true);

        var result = giftCertificateService.deleteById(TEST_ID);
        assertTrue(result);
    }

    @Test
    @DisplayName("Save Test - FAILED")
    void saveFailedTest() {
        var result = giftCertificateService.save(new GiftCertificateSaveRequestPojo());
        assertFalse(result);
    }


    @Test
    @DisplayName("Get by empty GiftCertificateSearchRequestPojo")
    void getByGiftCertificateSearchRequestPojoEmptyTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();

        when(giftCertificateDao.findAll()).
                thenReturn(certificates);

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo);
        assertEquals(certificatesDto, result);
    }

    @Test
    @DisplayName("Get by GiftCertificateSearchRequestPojo with Name or Description Test")
    void getByGiftCertificateSearchRequestPojoByNameOrDescriptionTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setName(TEST_NAME);

        when(giftCertificateDao.findByPartNameOrDescription(TEST_NAME)).
                thenReturn(List.of(certificates.get(0)));

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo);
        assertEquals(List.of(certificatesDto.get(0)), result);

    }

    @Test
    @DisplayName("Get by GiftCertificateSearchRequestPojo with Tag name Test")
    void getByGiftCertificateSearchRequestPojoByTagTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setTagName(TEST_NAME);

        when(giftCertificateDao.findAll()).
                thenReturn(certificates);

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).size();
        assertEquals(2, result);
    }

    @Test
    @DisplayName("Get by GiftCertificateSearchRequestPojo Name and Tag name Test")
    void getByGiftCertificateSearchRequestPojoEmptyByNameOrDescriptionAndTagNameTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setTagName(TEST_NAME);
        pojo.setName(TEST_NAME_WITH_TAG);

        when(giftCertificateDao.findByPartNameOrDescription(TEST_NAME_WITH_TAG)).
                thenReturn(List.of(certificates.get(3)));

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo);
        assertEquals(List.of(certificatesDto.get(3)), result);
    }

    @Test
    @DisplayName("Get by GiftCertificateSearchRequestPojo with Description and Tag name Test")
    void getByGiftCertificateSearchRequestPojoEmptyByNameOrDescriptionAndTagByDescriptionTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setTagName(TEST_NAME);
        pojo.setDescription(TEST_DESCRIPTION);

        when(giftCertificateDao.findByPartNameOrDescription(TEST_DESCRIPTION)).
                thenReturn(new ArrayList<>(List.of(certificates.get(2))));

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo);
        assertEquals(new ArrayList<>(List.of(certificatesDto.get(2))), result);
    }

    @Test
    @DisplayName("Get by GiftCertificateSearchRequestPojo with sort by name ASC Test")
    void getByGiftCertificateSearchRequestPojoEmptySortedByNameAcsTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setSortingType(SortingType.NAME);
        pojo.setSortingOrder(SortingOrder.ASC);

        when(giftCertificateDao.findAll()).
                thenReturn(certificates);

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).get(0);
        assertEquals(certificatesDto.get(0), result);
    }

    @Test
    @DisplayName("Get by GiftCertificateSearchRequestPojo with sort by name DESC Test")
    void getByGiftCertificateSearchRequestPojoEmptySortedByNameDescTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setSortingType(SortingType.NAME);
        pojo.setSortingOrder(SortingOrder.DESC);

        when(giftCertificateDao.findAll()).
                thenReturn(certificates);

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).get(0);
        assertEquals(certificatesDto.get(4), result);
    }

    @Test
    @DisplayName("Get by GiftCertificateSearchRequestPojo with sort by time ASC Test")
    void getByGiftCertificateSearchRequestPojoEmptySortedByTimeAcsTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setSortingType(SortingType.TIME);
        pojo.setSortingOrder(SortingOrder.ASC);

        when(giftCertificateDao.findAll()).
                thenReturn(certificates);

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).get(0);
        assertEquals(certificatesDto.get(4), result);
    }

    @Test
    @DisplayName("Get by GiftCertificateSearchRequestPojo with sort by time DESC Test")
    void getByGiftCertificateSearchRequestPojoEmptySortedByTimeDescTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setSortingType(SortingType.TIME);
        pojo.setSortingOrder(SortingOrder.DESC);

        when(giftCertificateDao.findAll()).
                thenReturn(certificates);

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).get(0);
        assertEquals(certificatesDto.get(0), result);
    }



    private List<GiftCertificate> createCertificatesForTest() {
        List<GiftCertificate> result = new ArrayList<>();
        result.add(new GiftCertificate(1L, "Name1", "Description1",
                BigDecimal.valueOf(1000), 10, timeFormatter.stringToLocalDateTime("2020-12-03T10:15:30"),
                timeFormatter.stringToLocalDateTime("2023-12-03T10:15:30"), new ArrayList<>()));
        result.add(new GiftCertificate(2L, "Name2", "Description2",
                BigDecimal.valueOf(100), 2, timeFormatter.stringToLocalDateTime("2020-12-03T10:15:30"),
                timeFormatter.stringToLocalDateTime("2022-12-03T10:15:30"), new ArrayList<>()));
        result.add(new GiftCertificate(3L, "Name3", "Description3",
                BigDecimal.valueOf(100), 2, timeFormatter.stringToLocalDateTime("2020-12-03T10:15:30"),
                timeFormatter.stringToLocalDateTime("2021-12-03T10:15:30"), new ArrayList<>(List.of(
                new Tag(1L, "name1"))
        )));
        result.add(new GiftCertificate(4L, "Name4", "Description4",
                BigDecimal.valueOf(100), 2, timeFormatter.stringToLocalDateTime("2020-12-03T10:15:30"),
                timeFormatter.stringToLocalDateTime("2020-12-03T10:15:30"), new ArrayList<>(List.of(
                new Tag(1L, "name1"),
                new Tag(2L, "name2")

        ))));
        result.add(new GiftCertificate(5L, "Name5", "Description5",
                BigDecimal.valueOf(100), 2, timeFormatter.stringToLocalDateTime("2019-12-03T10:15:30"),
                timeFormatter.stringToLocalDateTime("2019-12-03T10:15:30"), new ArrayList<>(List.of(
                new Tag(2L, "name2")
        ))));
        return result;
    }
}