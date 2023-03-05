package com.epam.esm.util.validators;

import com.epam.esm.pojo.GiftCertificateSaveRequestPojo;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component
public class GiftCertificateValidator {
    public boolean isValidCertificateRequestPojo(GiftCertificateSaveRequestPojo giftCertificate) {
        return (isNotEmpty(giftCertificate.getName()))
                && (isNotEmpty(giftCertificate.getDescription())
                && (giftCertificate.getPrice() != null && giftCertificate.getPrice().intValue() >= 0)
                && (giftCertificate.getDuration() >= 0));
    }
}
