package com.epam.esm.util.filters;

import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.models.GiftCertificate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
@Component
public class GiftCertificateFilter {
    public Set<GiftCertificate> filterGiftCertificateByTagName(Set<GiftCertificate> certs, String tagName) {
        if (CollectionUtils.isEmpty(certs)) {
            return Set.of();
        }
        return certs.stream()
                .filter(cert -> cert.getTags().
                        stream().
                        anyMatch(tag -> tag.getName()
                                .equalsIgnoreCase(tagName)))
                .collect(Collectors.toSet());
    }
}
