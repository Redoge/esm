package com.epam.esm.services;

import com.epam.esm.DAO.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.Tag;
import com.epam.esm.pojo.GiftCertificateSaveRequestPojo;
import com.epam.esm.pojo.GiftCertificateSearchRequestPojo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {
    @Mock
    private GiftCertificateDao giftCertificateDao;

    @InjectMocks
    private GiftCertificateService giftCertificateService;

    private final List<GiftCertificateMainDto>  certificates = createCertificatesForTest();
    @Test
    void getAllTest() {
        when(giftCertificateDao.findAll()).thenReturn(certificates);
        assertEquals(giftCertificateService.getAll(), certificates);
    }
    @Test
    void getAllEmptyTest() {
        when(giftCertificateDao.findAll()).thenReturn(List.of());
        assertEquals(giftCertificateService.getAll(), List.of());
    }

    @Test
    void getByIdTest() {
        when(giftCertificateDao.findById(1L)).thenReturn(Optional.ofNullable(certificates.get(0)));
        assertEquals(giftCertificateService.getById(1L).get(), certificates.get(0));
    }
    @Test
    void getByIdEmptyTest() {
        when(giftCertificateDao.findById(99L)).thenReturn(Optional.empty());
        assertEquals(giftCertificateService.getById(99L), Optional.empty());
    }

    @Test
    void getByNameTest() {
        when(giftCertificateDao.findByName("Name2")).thenReturn(Optional.ofNullable(certificates.get(1)));
        assertEquals(giftCertificateService.getByName("Name2").get(), certificates.get(1));
    }
    @Test
    void getByNameEmptyTest() {
        when(giftCertificateDao.findByName("Name5")).thenReturn(Optional.empty());
        assertEquals(giftCertificateService.getByName("Name5"), Optional.empty());
    }

    @Test
    void getByPartNameOrDescriptionAndTagNameTest() {
        when(giftCertificateDao.findByPartNameOrDescription("Name4")).
                thenReturn(Set.of(certificates.get(3)));
        assertEquals(giftCertificateService.getByPartNameOrDescriptionAndTagName("Name4","name1"),
                Set.of(certificates.get(3)));
    }
    @Test
    void getByPartNameOrDescriptionAndTagNameEmptyTest() {
        when(giftCertificateDao.findByPartNameOrDescription("Name4")).
                thenReturn(Set.of(certificates.get(3)));
        assertEquals(giftCertificateService.getByPartNameOrDescriptionAndTagName("Name4","name0"),
                Set.of());
    }

    @Test
    void getByPartNameOrDescriptionByNameTest() {
        when(giftCertificateDao.findByPartNameOrDescription("Name4")).
                thenReturn(Set.of(certificates.get(3)));
        assertEquals(giftCertificateService.getByPartNameOrDescription("Name4"),
                Set.of(certificates.get(3)));
    }
    @Test
    void getByPartNameOrDescriptionByDescriptionTest() {
        when(giftCertificateDao.findByPartNameOrDescription("Description4")).
                thenReturn(Set.of(certificates.get(3)));
        assertEquals(giftCertificateService.getByPartNameOrDescription("Description4"),
                Set.of(certificates.get(3)));
    }
    @Test
    void getByPartNameOrDescriptionEmptyTest() {
        when(giftCertificateDao.findByPartNameOrDescription("Description99")).
                thenReturn(Set.of());
        assertEquals(giftCertificateService.getByPartNameOrDescription("Description99"),
                Set.of());
    }

    @Test
    void getByTagNameTest() {
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);
        assertEquals(giftCertificateService.getByTagName("name1"),
                Set.of(certificates.get(2), certificates.get(3)));
    }
    @Test
    void getByTagNameEmptyTest() {
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);
        assertEquals(giftCertificateService.getByTagName("name989"),
                Set.of());
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
        assertEquals(giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo),
                certificates);
    }
    @Test
    void getByGiftCertificateSearchRequestPojoByNameOrDescriptionTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setName("name1");
        when(giftCertificateService.getByPartNameOrDescription("name1")).
                thenReturn(Set.of(certificates.get(0)));
        assertEquals(giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo),
                List.of(certificates.get(0)));

    }
    @Test
    void getByGiftCertificateSearchRequestPojoByTagTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setTagName("name1");
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);
        assertEquals(giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).size(),
                2);
    }
    @Test
    void getByGiftCertificateSearchRequestPojoEmptyByNameOrDescriptionAndTagByNameTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setTagName("name1");
        pojo.setName("name4");

        when(giftCertificateService.getByPartNameOrDescriptionAndTagName("name4", "name1")).
                thenReturn(Set.of(certificates.get(3)));
        assertEquals(giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo),
                List.of(certificates.get(3)));
    }
    @Test
    void getByGiftCertificateSearchRequestPojoEmptyByNameOrDescriptionAndTagByDescriptionTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setTagName("name1");
        pojo.setDescription("Description3");

        when(giftCertificateService.getByPartNameOrDescriptionAndTagName("Description3", "name1")).
                thenReturn(Set.of(certificates.get(2)));
        assertEquals(giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo),
                List.of(certificates.get(2)));
    }

    @Test
    void getByGiftCertificateSearchRequestPojoEmptySortedByNameAcsTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setSortByName("ASC");
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);
        assertEquals(giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).get(0), certificates.get(0));
    }
    @Test
    void getByGiftCertificateSearchRequestPojoEmptySortedByNameDescTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setSortByName("DESC");
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);
        assertEquals(certificates.get(4), giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).get(0));
    }

    @Test
    void getByGiftCertificateSearchRequestPojoEmptySortedByTimeAcsTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setSortByTime("ASC");
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);
        assertEquals(certificates.get(4), giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).get(0));
    }
    @Test
    void getByGiftCertificateSearchRequestPojoEmptySortedByTimeDescTest() {
        GiftCertificateSearchRequestPojo pojo = new GiftCertificateSearchRequestPojo();
        pojo.setSortByTime("DESC");
        when(giftCertificateDao.findAll()).
                thenReturn(certificates);
        assertEquals(certificates.get(0), giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo).get(0));
    }



    private List<GiftCertificateMainDto> createCertificatesForTest(){
        List<GiftCertificateMainDto> result = new ArrayList<>();
        result.add(new GiftCertificateMainDto(1L, "Name1", "Description1",
                BigDecimal.valueOf(1000), 10, "2020-12-03T10:15:30", "2023-12-03T10:15:30",List.of()));
        result.add(new GiftCertificateMainDto(2L, "Name2", "Description2",
                BigDecimal.valueOf(100), 2, "2020-12-03T10:15:30", "2022-12-03T10:15:30",List.of()));
        result.add(new GiftCertificateMainDto(3L, "Name3", "Description3",
                BigDecimal.valueOf(100), 2, "2020-12-03T10:15:30", "2021-12-03T10:15:30",List.of(
                        new Tag(1L, "name1")
        )));
        result.add(new GiftCertificateMainDto(4L, "Name4", "Description4",
                BigDecimal.valueOf(100), 2, "2020-12-03T10:15:30", "2020-12-03T10:15:30",List.of(
                new Tag(1L, "name1"),
                new Tag(2L,"name2")

        )));
        result.add(new GiftCertificateMainDto(5L, "Name5", "Description5",
                BigDecimal.valueOf(100), 2, "2019-12-03T10:15:30", "2019-12-03T10:15:30",List.of(
                new Tag(2L,"name2")
        )));
        return result;
    }
    private GiftCertificate giftCertificateSaveRequestPojoToGiftCertificateTransfer(GiftCertificateSaveRequestPojo giftCertificatePojo) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(giftCertificatePojo.getName());
        giftCertificate.setDescription(giftCertificatePojo.getDescription());
        giftCertificate.setDuration(giftCertificatePojo.getDuration());
        giftCertificate.setPrice(giftCertificatePojo.getPrice());
        if(giftCertificatePojo.getTags()!=null) {
            giftCertificate.setTags(
                    giftCertificatePojo.getTags().stream().map(o -> {
                        Tag tag = new Tag();
                        tag.setName(o);
                        return tag;
                    }).collect(Collectors.toList()));
        }
        return giftCertificate;
    }
}