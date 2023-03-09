package com.epam.esm.services;

import com.epam.esm.dao.interfaces.GiftCertificateDaoInterface;
import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.pojo.GiftCertificateSaveRequestPojo;
import com.epam.esm.pojo.GiftCertificateSearchRequestPojo;
import com.epam.esm.services.interfaces.GiftCertificateServiceInterface;
import com.epam.esm.util.filters.GiftCertificateFilter;
import com.epam.esm.util.mappers.GiftCertificateMapper;
import com.epam.esm.util.sorters.GiftCertificateSorter;
import com.epam.esm.util.validators.GiftCertificateValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
public class GiftCertificateService implements GiftCertificateServiceInterface {
    private final GiftCertificateDaoInterface giftCertificateDao;
    private final GiftCertificateMapper giftCertificateMapper;
    private final GiftCertificateValidator giftCertificateValidator;
    private final GiftCertificateFilter giftCertificateFilter;
    private final GiftCertificateSorter giftCertificateSorter;


    public GiftCertificateService(GiftCertificateDaoInterface giftCertificateDao, GiftCertificateMapper giftCertificateMapper,
                                  GiftCertificateValidator giftCertificateValidator, GiftCertificateFilter giftCertificateFilter,
                                  GiftCertificateSorter giftCertificateSorter) {
        this.giftCertificateDao = giftCertificateDao;
        this.giftCertificateMapper = giftCertificateMapper;
        this.giftCertificateValidator = giftCertificateValidator;
        this.giftCertificateFilter = giftCertificateFilter;
        this.giftCertificateSorter = giftCertificateSorter;
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

    public List<GiftCertificateMainDto> getByPartNameOrDescriptionAndTagName(String nameOrDescription, String tagName) {
        return giftCertificateFilter.filterGiftCertificateByTagName(giftCertificateDao.findByPartNameOrDescription(nameOrDescription), tagName)
                .stream()
                .map(giftCertificateMapper::mapGiftCertToMainDto)
                .toList();

    }

    public List<GiftCertificateMainDto> getByPartNameOrDescription(String nameOrDescription) {
        return giftCertificateDao.findByPartNameOrDescription(nameOrDescription)
                .stream()
                .map(giftCertificateMapper::mapGiftCertToMainDto)
                .toList();
    }

    public List<GiftCertificateMainDto> getByTagName(String tagName) {
        return giftCertificateFilter.filterGiftCertificateByTagName(List.copyOf(giftCertificateDao.findAll()), tagName)
                .stream()
                .map(giftCertificateMapper::mapGiftCertToMainDto)
                .toList();
    }

    @Transactional
    public boolean deleteById(long id) {
        return giftCertificateDao.deleteById(id);
    }

    @Transactional
    public boolean update(GiftCertificateSaveRequestPojo giftCertificate) {
        GiftCertificate gCert = giftCertificateMapper.mapCertificateSaveRequestPojoToGiftCertificateTransfer(giftCertificate);
        return giftCertificateDao.update(gCert);
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
        return giftCertificateSorter.sortedGiftCertificateMainDtoBySearchReq(gCerts, req);
    }

    private List<GiftCertificateMainDto> findGiftCertificateMainDtoBySearchReq(GiftCertificateSearchRequestPojo certsSearchReqPojo) {
        List<GiftCertificateMainDto> gCerts;
        var nameIsPresent = isNotEmpty(certsSearchReqPojo.getName());
        var descriptionIsPresent = isNotEmpty(certsSearchReqPojo.getDescription());
        var tagIsPresent = isNotEmpty(certsSearchReqPojo.getTagName());
        if (nameIsPresent || descriptionIsPresent) {
            String nameOrDescriptionString = certsSearchReqPojo.getName() != null ? certsSearchReqPojo.getName()
                    : certsSearchReqPojo.getDescription();

            gCerts = tagIsPresent ? getByPartNameOrDescriptionAndTagName(nameOrDescriptionString, certsSearchReqPojo.getTagName()) :
                    getByPartNameOrDescription(nameOrDescriptionString);
        } else {
            gCerts = tagIsPresent ? getByTagName(certsSearchReqPojo.getTagName()) :
                    getAll();
        }
        return gCerts;
    }
}
