package com.epam.esm.util.sorters;

import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.pojo.GiftCertificateSearchRequestPojo;
import com.epam.esm.util.sorters.enums.SortingOrder;
import com.epam.esm.util.sorters.enums.SortingType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
@Component
public class GiftCertificateSorter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public List<GiftCertificateMainDto> sortMainDtoByName(List<GiftCertificateMainDto> giftCertsMainDtoList, SortingOrder notation){
        if (notation.equals(SortingOrder.ASC)) {
            giftCertsMainDtoList.sort(Comparator.comparing(GiftCertificateMainDto::getName));
        } else if (notation.equals(SortingOrder.DESC)) {
            giftCertsMainDtoList.sort(Comparator.comparing(GiftCertificateMainDto::getName).reversed());
        }
        return giftCertsMainDtoList;
    }
    public List<GiftCertificateMainDto> sortMainDtoByTime(List<GiftCertificateMainDto> giftCertsMainDtoList, SortingOrder notation){
        if (notation.equals(SortingOrder.ASC)) {
            giftCertsMainDtoList.sort(Comparator.comparing(x -> LocalDateTime.parse(x.getLastUpdateDate(), formatter)));
        } else if (notation.equals(SortingOrder.DESC)) {
            giftCertsMainDtoList.sort(Comparator.comparing(x -> LocalDateTime.parse(x.getLastUpdateDate(), formatter)));
            Collections.reverse(giftCertsMainDtoList);
        }
        return giftCertsMainDtoList;
    }
    public List<GiftCertificateMainDto> sortedGiftCertificateMainDtoBySearchReq(List<GiftCertificateMainDto> gCerts, GiftCertificateSearchRequestPojo certsSearchReqPojo) {
        gCerts = new ArrayList<>(gCerts);
        if (certsSearchReqPojo.getSortingType() == SortingType.NAME) {
            gCerts = sortMainDtoByName(gCerts, certsSearchReqPojo.getSortingOrder());
        }else if (certsSearchReqPojo.getSortingType() == SortingType.TIME) {
            gCerts = sortMainDtoByTime(gCerts, certsSearchReqPojo.getSortingOrder());
        }
        return gCerts;
    }
}
