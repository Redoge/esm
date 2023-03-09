package com.epam.esm.util.sorters;

import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.pojo.GiftCertificateSearchRequestPojo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component
public class GiftCertificateSorter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";
    public List<GiftCertificateMainDto> sortMainDtoByName(List<GiftCertificateMainDto> giftCertsMainDtoList, String notation){
        if (notation.equalsIgnoreCase(ASC)) {
            giftCertsMainDtoList.sort(Comparator.comparing(GiftCertificateMainDto::getName));
        } else if (notation.equalsIgnoreCase(DESC)) {
            giftCertsMainDtoList.sort(Comparator.comparing(GiftCertificateMainDto::getName).reversed());
        }
        return giftCertsMainDtoList;
    }
    public List<GiftCertificateMainDto> sortMainDtoByTime(List<GiftCertificateMainDto> giftCertsMainDtoList, String notation){
        if (notation.equalsIgnoreCase(ASC)) {
            giftCertsMainDtoList.sort(Comparator.comparing(x -> LocalDateTime.parse(x.getLastUpdateDate(), formatter)));
        } else if (notation.equalsIgnoreCase(DESC)) {
            giftCertsMainDtoList.sort(Comparator.comparing(x -> LocalDateTime.parse(x.getLastUpdateDate(), formatter)));
            Collections.reverse(giftCertsMainDtoList);
        }
        return giftCertsMainDtoList;
    }
    public List<GiftCertificateMainDto> sortedGiftCertificateMainDtoBySearchReq(List<GiftCertificateMainDto> gCerts, GiftCertificateSearchRequestPojo certsSearchReqPojo) {
        gCerts = new ArrayList<>(gCerts);
        if (isNotEmpty(certsSearchReqPojo.getSortByName())) {
            gCerts = sortMainDtoByName(gCerts, certsSearchReqPojo.getSortByName());
        } else if (isNotEmpty(certsSearchReqPojo.getSortByTime())) {
            gCerts = sortMainDtoByTime(gCerts, certsSearchReqPojo.getSortByTime());
        }
        return gCerts;
    }
}
