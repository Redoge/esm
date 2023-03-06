package com.epam.esm.util.filters;

import com.epam.esm.models.GiftCertificate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class GiftCertificateFilter {
    public List<GiftCertificate> filterGiftCertificateByTagName(List<GiftCertificate> certs, String tagName) {
        certs.forEach(s-> System.out.println(s.getClass()));
        if (CollectionUtils.isEmpty(certs)) {
            return List.of();
        }
        return certs.stream()
                .filter(cert -> cert.getTags().
                        stream().
                        anyMatch(tag -> tag.getName()
                                .equalsIgnoreCase(tagName)))
                .toList();
    }
}
