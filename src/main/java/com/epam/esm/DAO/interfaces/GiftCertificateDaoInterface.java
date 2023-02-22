package com.epam.esm.DAO.interfaces;

import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GiftCertificateDaoInterface {
    List<GiftCertificateMainDto> findAll();
    Optional<GiftCertificateMainDto> findById(long id);
    Set<GiftCertificateMainDto> findByPartNameOrDescriptionAndTagName(String nameOrDescription, String tagName);
    Set<GiftCertificateMainDto> findByPartNameOrDescription(String nameOrDescription);
    Set<GiftCertificateMainDto> findByTagName(String tagName);
    boolean deleteById(long id);
    boolean update(GiftCertificateMainDto giftCertificate);
    boolean save(GiftCertificate giftCertificate);
}
