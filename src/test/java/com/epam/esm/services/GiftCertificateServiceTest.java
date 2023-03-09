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
import com.epam.esm.util.validators.GiftCertificateValidator;
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

    @Test
    void getAllTest() {
        when(giftCertificateDao.findAll()).thenReturn(certificates);
        assertEquals(giftCertificateService.getAll(), certificatesDto);
    }

    @Test
    void getAllEmptyTest() {
        when(giftCertificateDao.findAll()).thenReturn(List.of());
        assertEquals(List.of(), giftCertificateService.getAll());
    }

    @Test
    void getByIdTest() {
        when(giftCertificateDao.findById(1L)).thenReturn(Optional.ofNullable(certificates.get(0)));
        assertEquals(certificatesDto.get(0), giftCertificateService.getById(1L).get());
    }

    @Test
    void getByIdEmptyTest() {
        when(giftCertificateDao.findById(99L)).thenReturn(Optional.empty());
        assertEquals(giftCertificateService.getById(99L), Optional.empty());
    }

    @Test
    void getByNameTest() {
        when(giftCertificateDao.findByName("Name2")).thenReturn(Optional.ofNullable(certificates.get(1)));
        assertEquals(certificatesDto.get(1), giftCertificateService.getByName("Name2").get());
    }

    @Test
    void getByNameEmptyTest() {
        when(giftCertificateDao.findByName("Name5")).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), giftCertificateService.getByName("Name5"));
    }

    @Test
    void getByPartNameOrDescriptionAndTagNameTest() {
        when(giftCertificateDao.findByPartNameOrDescription("Name4")).
                thenReturn(List.of(certificates.get(3)));
        assertEquals(List.of(certificatesDto.get(3)),
                giftCertificateService.getByPartNameOrDescriptionAndTagName("Name4", "name1"));
    }

    @Test
    void getByPartNameOrDescriptionAndTagNameEmptyTest() {
        when(giftCertificateDao.findByPartNameOrDescription("Name4")).
                thenReturn(List.of(certificates.get(3)));
        assertEquals(List.of(), giftCertificateService.getByPartNameOrDescriptionAndTagName("Name4", "name0"));
    }

    @Test
    void getByPartNameOrDescriptionByNameTest() {
        when(giftCertificateDao.findByPartNameOrDescription("Name4")).
                thenReturn(List.of(certificates.get(3)));
        assertEquals(List.of(certificatesDto.get(3)), giftCertificateService.getByPartNameOrDescription("Name4"));
    }

    @Test
    void getByPartNameOrDescriptionByDescriptionTest() {
        when(giftCertificateDao.findByPartNameOrDescription("Description4")).
                thenReturn(List.of(certificates.get(3)));
        assertEquals(List.of(certificatesDto.get(3)), giftCertificateService.getByPartNameOrDescription("Description4"));
    }

    @Test
    void getByPartNameOrDescriptionEmptyTest() {
        when(giftCertificateDao.findByPartNameOrDescription("Description99")).
                thenReturn(List.of());
        assertEquals(List.of(), giftCertificateService.getByPartNameOrDescription("Description99"));
    }

    @Test
    void getByTagNameTest() {
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);
        assertEquals(List.of(certificatesDto.get(2), certificatesDto.get(3)), giftCertificateService.getByTagName("name1"));
    }

    @Test
    void getByTagNameEmptyTest() {
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);
        assertEquals(List.of(), giftCertificateService.getByTagName("name989"));
    }

    @Test
    void deleteByIdTest() {
        when(giftCertificateDao.deleteById(1L)).
                thenReturn(true);
        assertTrue(giftCertificateService.deleteById(1L));
    }

    @Test
    void saveFailedTest() {
        assertFalse(giftCertificateService.save(new GiftCertificateSaveRequestPojo()));
    }


    @Test
    void getByGiftCertificateSearchRequestPojoEmptyTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo);
        assertEquals(certificatesDto, result);
    }

    @Test
    void getByGiftCertificateSearchRequestPojoByNameOrDescriptionTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setName("name1");
        when(giftCertificateDao.findByPartNameOrDescription("name1")).
                thenReturn(List.of(certificates.get(0)));

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo);
        assertEquals(List.of(certificatesDto.get(0)), result);

    }

    @Test
    void getByGiftCertificateSearchRequestPojoByTagTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setTagName("name1");
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).size();
        assertEquals(2, result);
    }

    @Test
    void getByGiftCertificateSearchRequestPojoEmptyByNameOrDescriptionAndTagByNameTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setTagName("name1");
        pojo.setName("name4");

        when(giftCertificateDao.findByPartNameOrDescription("name4")).
                thenReturn(List.of(certificates.get(3)));

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo);
        assertEquals(List.of(certificatesDto.get(3)), result);
    }

    @Test
    void getByGiftCertificateSearchRequestPojoEmptyByNameOrDescriptionAndTagByDescriptionTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setTagName("name1");
        pojo.setDescription("Description3");

        when(giftCertificateDao.findByPartNameOrDescription("Description3")).
                thenReturn(new ArrayList<>(List.of(certificates.get(2))));

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo);
        assertEquals(new ArrayList<>(List.of(certificatesDto.get(2))), result);
    }

    @Test
    void getByGiftCertificateSearchRequestPojoEmptySortedByNameAcsTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setSortByName("ASC");
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).get(0);
        assertEquals(certificatesDto.get(0), result);
    }

    @Test
    void getByGiftCertificateSearchRequestPojoEmptySortedByNameDescTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setSortByName("DESC");
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).get(0);
        assertEquals(certificatesDto.get(4), result);
    }

    @Test
    void getByGiftCertificateSearchRequestPojoEmptySortedByTimeAcsTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setSortByTime("ASC");
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);

        var result = giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).get(0);
        assertEquals(certificatesDto.get(4), result);
    }

    @Test
    void getByGiftCertificateSearchRequestPojoEmptySortedByTimeDescTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setSortByTime("DESC");
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