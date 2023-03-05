package com.epam.esm.services.interfaces;

import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.pojo.GiftCertificateSaveRequestPojo;
import com.epam.esm.pojo.GiftCertificateSearchRequestPojo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GiftCertificateServiceInterface {
    List<GiftCertificateMainDto> getAll();

    Optional<GiftCertificateMainDto> getById(long id);

    Optional<GiftCertificateMainDto> getByName(String name);

    Set<GiftCertificateMainDto> getByPartNameOrDescriptionAndTagName(String nameOrDescription, String tagName);

    Set<GiftCertificateMainDto> getByPartNameOrDescription(String nameOrDescription);

    Set<GiftCertificateMainDto> getByTagName(String tagName);

    boolean deleteById(long id);

    boolean update(GiftCertificateSaveRequestPojo giftCertificate, long id);

    boolean save(GiftCertificateSaveRequestPojo giftCertificate);

    List<GiftCertificateMainDto> getByGiftCertificateSearchRequestPojo(GiftCertificateSearchRequestPojo req);
}