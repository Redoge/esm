package com.epam.esm.controllers;

import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.dto.GiftCertificateNestedDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.pojo.GiftCertificateSaveRequestPojo;
import com.epam.esm.services.GiftCertificateService;
import com.epam.esm.util.ResponseWrapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.epam.esm.pojo.GiftCertificateSearchRequestPojo;
import java.util.List;

@RestController
@RequestMapping("api/certificates")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping("")
    public ResponseEntity<List<GiftCertificateMainDto>> getGiftCertificates(@ModelAttribute GiftCertificateSearchRequestPojo req){
        return ResponseEntity.ok(giftCertificateService.getByGiftCertificateSearchRequestPojo(req));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getGiftCertificatesById(@PathVariable long id){
        var cert = giftCertificateService.getById(id);
        return cert.isPresent()? ResponseEntity.ok(cert.get()):
                new ResponseEntity<>(new ResponseWrapper(404, String.format("Not Found! (id = %d)", id),4042),
                HttpStatusCode.valueOf(404));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeGiftCertificateById(@PathVariable long id){
        boolean success = giftCertificateService.deleteById(id);
        return success?ResponseEntity.ok(new ResponseWrapper(200, String.format("Removed successfully! (id = %d)", id), 2001)) :
                new ResponseEntity<>(new ResponseWrapper(400, String.format("Deletion is not successful! (id = %d)", id), 4001),
                        HttpStatusCode.valueOf(400));
    }
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody GiftCertificateSaveRequestPojo giftCert){
        boolean success;
        success = giftCertificateService.save(giftCert);
        if(success) giftCert.setId(giftCertificateService.getByName(giftCert.getName()).get().getId());
        return success? new ResponseEntity<>(new ResponseWrapper(201, String.format("Created successfully! " +
                "(Name = %s, id = %d)", giftCert.getName(), giftCert.getId()), 2011),HttpStatusCode.valueOf(201))
                : new ResponseEntity<>(new ResponseWrapper(400, "Created is not successful!", 4002),
                HttpStatusCode.valueOf(400));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody GiftCertificateSaveRequestPojo giftCert){
        boolean success = giftCert!=null && (giftCert.getId()==0 || id==giftCert.getId());
        if(success && giftCert.getId()==0) giftCert.setId(id);
        success = success && giftCertificateService.update(giftCert, id);
        return success? new ResponseEntity<>(new ResponseWrapper(200, String.format("Updated successfully! " +
                "(id = %d)", id), 2011),HttpStatusCode.valueOf(200))
                : new ResponseEntity<>(new ResponseWrapper(400, String.format("Updated is not successfully! " +
                "(id = %d)", id), 4002),
                HttpStatusCode.valueOf(400));
    }

}
