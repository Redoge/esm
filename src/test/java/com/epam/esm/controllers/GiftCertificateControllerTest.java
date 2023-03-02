package com.epam.esm.controllers;

import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.models.Tag;
import com.epam.esm.pojo.GiftCertificateSaveRequestPojo;
import com.epam.esm.pojo.GiftCertificateSearchRequestPojo;
import com.epam.esm.services.GiftCertificateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateControllerTest {
    @Mock
    private  GiftCertificateService giftCertificateService;
    @InjectMocks
    private GiftCertificateController giftCertificateController;
    private final List<GiftCertificateMainDto>  certificates = createCertificatesForTest();


    @Test
    void getGiftCertificatesTest() {
        var pojo = new GiftCertificateSearchRequestPojo();
        when(giftCertificateService.getByGiftCertificateSearchRequestPojo(pojo)).thenReturn(certificates);
        ResponseEntity<List<GiftCertificateMainDto>> result = giftCertificateController.getGiftCertificates(pojo);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
    }

    @Test
    void getGiftCertificatesByIdTest() {
        when(giftCertificateService.getById(1L)).thenReturn(Optional.ofNullable(certificates.get(0)));
        ResponseEntity<?> result = giftCertificateController.getGiftCertificatesById(1L);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
    }
    @Test
    void getGiftCertificatesByIdIncorrectTest() {
        when(giftCertificateService.getById(1L)).thenReturn(Optional.empty());
        ResponseEntity<?> result = giftCertificateController.getGiftCertificatesById(1L);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(404));
    }
    @Test
    void removeGiftCertificateByIdTest() {
        when(giftCertificateService.deleteById(1L)).thenReturn(true);
        ResponseEntity<?> result = giftCertificateController.removeGiftCertificateById(1L);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
    }
    @Test
    void removeGiftCertificateByIdIncorrectTest() {
        when(giftCertificateService.deleteById(1L)).thenReturn(false);
        ResponseEntity<?> result = giftCertificateController.removeGiftCertificateById(1L);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(400));
    }

    @Test
    void createTest() {
        var giftCert = new GiftCertificateSaveRequestPojo();
        giftCert.setName("name1");
        var tmp = new GiftCertificateMainDto();
        tmp.setName("name1");
        when(giftCertificateService.save(giftCert)).thenReturn(true);
        when(giftCertificateService.getByName("name1")).thenReturn(Optional.of(tmp));
        ResponseEntity<?> result = giftCertificateController.create(giftCert);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(201));
    }
    @Test
    void createIncorrectTest() {
        var giftCert = new GiftCertificateSaveRequestPojo();
        when(giftCertificateService.save(giftCert)).thenReturn(false);
        ResponseEntity<?> result = giftCertificateController.create(giftCert);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(400));
    }
    @Test
    void updateTest() {
        var giftCert = new GiftCertificateSaveRequestPojo();
        when(giftCertificateService.update(giftCert, 1L)).thenReturn(true);
        ResponseEntity<?> result = giftCertificateController.update( 1L, giftCert);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
    }
    @Test
    void updateIncorrectTest() {
        var giftCert = new GiftCertificateSaveRequestPojo();
        when(giftCertificateService.update(giftCert, 1L)).thenReturn(false);
        ResponseEntity<?> result = giftCertificateController.update( 1L, giftCert);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(400));
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
}