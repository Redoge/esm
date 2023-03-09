package com.epam.esm.util.mappers;

import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.dto.GiftCertificateNestedDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.Tag;
import com.epam.esm.pojo.GiftCertificateSaveRequestPojo;
import com.epam.esm.util.formatters.TimeFormatter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GiftCertificateMapper {
    private final TimeFormatter timeFormatter;

    public GiftCertificateMapper(TimeFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
    }

    public GiftCertificate mapCertificateSaveRequestPojoToGiftCertificateTransfer(GiftCertificateSaveRequestPojo certificatePojo) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(certificatePojo.getName());
        giftCertificate.setDescription(certificatePojo.getDescription());
        giftCertificate.setDuration(certificatePojo.getDuration());
        giftCertificate.setPrice(certificatePojo.getPrice());
        if (CollectionUtils.isNotEmpty(certificatePojo.getTags())) {
            giftCertificate.setTags(certificatePojo.getTags()
                    .stream()
                    .map(Tag::new)
                    .collect(Collectors.toList()));
        }
        return giftCertificate;
    }

    public GiftCertificateMainDto mapGiftCertToMainDto(GiftCertificate gCert){
        return new GiftCertificateMainDto(gCert.getId(),
                gCert.getName(),
                gCert.getDescription(),
                gCert.getPrice(),
                gCert.getDuration(),
                timeFormatter.timeToIso8601(gCert.getCreateDate()),
                timeFormatter.timeToIso8601(gCert.getLastUpdateDate()),
                gCert.getTags());
    }

    public GiftCertificateNestedDto mapGiftCertToNestedDto(GiftCertificate giftCertificate) {
        var cert = new GiftCertificateNestedDto();
        cert.setId(giftCertificate.getId());
        cert.setName(giftCertificate.getName());
        cert.setDescription(giftCertificate.getDescription());
        cert.setDuration(giftCertificate.getDuration());
        cert.setPrice(giftCertificate.getPrice());
        cert.setCreateDate(timeFormatter.timeToIso8601(giftCertificate.getCreateDate()));
        cert.setLastUpdateDate(timeFormatter.timeToIso8601(giftCertificate.getLastUpdateDate()));
        return cert;
    }
}
