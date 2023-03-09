package com.epam.esm.controllers;

import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.pojo.GiftCertificateSaveRequestPojo;
import com.epam.esm.pojo.GiftCertificateSearchRequestPojo;
import com.epam.esm.services.interfaces.GiftCertificateServiceInterface;
import com.epam.esm.util.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/certificates")
public class GiftCertificateController {
    private final GiftCertificateServiceInterface giftCertificateService;

    public GiftCertificateController(GiftCertificateServiceInterface giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    public ResponseEntity<List<GiftCertificateMainDto>> getGiftCertificates(@ModelAttribute GiftCertificateSearchRequestPojo req) {
        return ResponseEntity.ok(giftCertificateService.getByGiftCertificateSearchRequestPojo(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGiftCertificatesById(@PathVariable long id) {
        var cert = giftCertificateService.getById(id);
        return cert.isPresent() ? ResponseEntity.ok(cert.get()) :
                new ResponseEntity<>(new ResponseWrapper(HttpStatus.NOT_FOUND.value(),
                        String.format("Not Found! (id = %d)", id), 4042), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeGiftCertificateById(@PathVariable long id) {
        boolean success = giftCertificateService.deleteById(id);
        return success ? ResponseEntity.ok(new ResponseWrapper(HttpStatus.OK.value(),
                String.format("Removed successfully! (id = %d)", id), 2001)) :
                new ResponseEntity<>(new ResponseWrapper(HttpStatus.BAD_REQUEST.value(),
                        String.format("Deletion is not successful! (id = %d)", id), 4001),
                        HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GiftCertificateSaveRequestPojo giftCert) {
        boolean success;
        success = giftCertificateService.save(giftCert);
        if (success) {
            giftCert.setId(giftCertificateService.getByName(giftCert.getName()).get().getId());
        }
        return success ? new ResponseEntity<>(new ResponseWrapper(HttpStatus.CREATED.value(),
                String.format("Created successfully! (Name = %s, id = %d)", giftCert.getName(), giftCert.getId()),
                2011), HttpStatus.CREATED) :
                new ResponseEntity<>(new ResponseWrapper(HttpStatus.BAD_REQUEST.value(),
                        "Created is not successful!", 4002),
                        HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody GiftCertificateSaveRequestPojo giftCert) {
        boolean success = giftCert != null && (giftCert.getId() == 0 || id == giftCert.getId());
        if (success && giftCert.getId() == 0) giftCert.setId(id);
        success = success && giftCertificateService.update(giftCert);
        return success ? new ResponseEntity<>(new ResponseWrapper(HttpStatus.OK.value(),
                String.format("Updated successfully! (id = %d)", id), 2011), HttpStatus.OK)
                : new ResponseEntity<>(new ResponseWrapper(HttpStatus.BAD_REQUEST.value(),
                String.format("Updated is not successfully! (id = %d)", id), 4002),
                HttpStatus.BAD_REQUEST);
    }

}
