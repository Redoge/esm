package com.epam.esm.services;

import com.epam.esm.DAO.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.pojo.GiftCertificateSaveRequestPojo;
import com.epam.esm.pojo.GiftCertificateSearchRequestPojo;
import com.epam.esm.services.interfaces.GiftCertificateServiceInterface;
import com.epam.esm.util.filters.GiftCertificateFilter;
import com.epam.esm.util.mappers.GiftCertificateMapper;
import com.epam.esm.util.validators.GiftCertificateValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
public class GiftCertificateService implements GiftCertificateServiceInterface {
    private final GiftCertificateDao giftCertificateDao;
    private final GiftCertificateMapper giftCertificateMapper;
    private final GiftCertificateValidator giftCertificateValidator;
    private final GiftCertificateFilter giftCertificateFilter;

    public GiftCertificateService(GiftCertificateDao giftCertificateDao, GiftCertificateMapper giftCertificateMapper,
                                  GiftCertificateValidator giftCertificateValidator, GiftCertificateFilter giftCertificateFilter) {
        this.giftCertificateDao = giftCertificateDao;
        this.giftCertificateMapper = giftCertificateMapper;
        this.giftCertificateValidator = giftCertificateValidator;
        this.giftCertificateFilter = giftCertificateFilter;
    }

    public List<GiftCertificateMainDto> getAll() {
        return giftCertificateDao.findAll()
                .stream()
                .map(giftCertificateMapper::mapGiftCertToMainDto)
                .toList();
    }

    public Optional<GiftCertificateMainDto> getById(long id) {
        return giftCertificateDao.findById(id)
                .map(giftCertificateMapper::mapGiftCertToMainDto);
    }

    public Optional<GiftCertificateMainDto> getByName(String name) {
        return giftCertificateDao.findByName(name)
                .map(giftCertificateMapper::mapGiftCertToMainDto);
    }

    public Set<GiftCertificateMainDto> getByPartNameOrDescriptionAndTagName(String nameOrDescription, String tagName) {
        return giftCertificateFilter.filterGiftCertificateByTagName(giftCertificateDao.findByPartNameOrDescription(nameOrDescription), tagName)
                .stream()
                .map(giftCertificateMapper::mapGiftCertToMainDto)
                .collect(Collectors.toSet());

    }

    public Set<GiftCertificateMainDto> getByPartNameOrDescription(String nameOrDescription) {
        return giftCertificateDao.findByPartNameOrDescription(nameOrDescription)
                .stream()
                .map(giftCertificateMapper::mapGiftCertToMainDto)
                .collect(Collectors.toSet());
    }

    public Set<GiftCertificateMainDto> getByTagName(String tagName) {
        return giftCertificateFilter.filterGiftCertificateByTagName(Set.copyOf(giftCertificateDao.findAll()), tagName)
                .stream()
                .map(giftCertificateMapper::mapGiftCertToMainDto)
                .collect(Collectors.toSet());
    }

    @Transactional
    public boolean deleteById(long id) {
        return giftCertificateDao.deleteById(id);
    }

    @Transactional
    public boolean update(GiftCertificateSaveRequestPojo giftCertificate, long id) {
        GiftCertificate gCert = giftCertificateMapper.mapCertificateSaveRequestPojoToGiftCertificateTransfer(giftCertificate);
        return giftCertificateDao.update(gCert, id);
    }

    @Transactional
    public boolean save(GiftCertificateSaveRequestPojo giftCertificate) {
        if (giftCertificateValidator.isValidCertificateRequestPojo(giftCertificate)) {
            GiftCertificate gCert = giftCertificateMapper.mapCertificateSaveRequestPojoToGiftCertificateTransfer(giftCertificate);
            return giftCertificateDao.save(gCert);
        }
        return false;
    }

    @Transactional
    public List<GiftCertificateMainDto> getByGiftCertificateSearchRequestPojo(GiftCertificateSearchRequestPojo req) {
        List<GiftCertificateMainDto> gCerts = findGiftCertificateMainDtoBySearchReq(req);
        sortedGiftCertificateMainDtoBySearchReq(gCerts, req);
        return gCerts;
    }

    private List<GiftCertificateMainDto> findGiftCertificateMainDtoBySearchReq(GiftCertificateSearchRequestPojo certsSearchReqPojo) {
        List<GiftCertificateMainDto> gCerts;
        if (isNotEmpty(certsSearchReqPojo.getName()) || (isNotEmpty(certsSearchReqPojo.getDescription()))) {
            String nameOrDescriptionString = isNotEmpty(certsSearchReqPojo.getName()) ?
                    certsSearchReqPojo.getName() :
                    certsSearchReqPojo.getDescription();
            if (isNotEmpty(certsSearchReqPojo.getTagName())) {
                gCerts = List.copyOf(getByPartNameOrDescriptionAndTagName(nameOrDescriptionString,
                        certsSearchReqPojo.getTagName()));
            } else {
                gCerts = List.copyOf(getByPartNameOrDescription(nameOrDescriptionString));
            }
        } else {
            if (isNotEmpty(certsSearchReqPojo.getTagName())) {
                gCerts = List.copyOf(getByTagName(certsSearchReqPojo.getTagName()));
            } else {
                gCerts = getAll();
            }
        }
        return gCerts;
    }

    private void sortedGiftCertificateMainDtoBySearchReq(List<GiftCertificateMainDto> gCerts, GiftCertificateSearchRequestPojo certsSearchReqPojo) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        if (isNotEmpty(certsSearchReqPojo.getSortByName())) {
            String sortByName = certsSearchReqPojo.getSortByName();
            if (sortByName.equalsIgnoreCase("ASC")) {
                gCerts.sort(Comparator.comparing(GiftCertificateMainDto::getName));
            } else if ((sortByName.equalsIgnoreCase("DESC"))) {
                gCerts.sort(Comparator.comparing(GiftCertificateMainDto::getName).reversed());
            }
        } else if (isNotEmpty(certsSearchReqPojo.getSortByTime())) {
            String sortByName = certsSearchReqPojo.getSortByTime();
            gCerts.sort(Comparator.comparing(x -> LocalDateTime.parse(x.getLastUpdateDate(), formatter)));
            if ((sortByName.equalsIgnoreCase("DESC"))) {
                Collections.reverse(gCerts);
            }
        }
    }
}
