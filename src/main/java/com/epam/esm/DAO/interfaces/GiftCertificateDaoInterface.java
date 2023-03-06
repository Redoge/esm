package com.epam.esm.DAO.interfaces;

import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GiftCertificateDaoInterface {
    List<GiftCertificate> findAll();
    Optional<GiftCertificate> findById(long id);
    List<GiftCertificate> findByPartNameOrDescriptionAndTagName(String nameOrDescription, String tagName);
    List<GiftCertificate> findByPartNameOrDescription(String nameOrDescription);
    List<GiftCertificate> findByTagName(String tagName);
    boolean deleteById(long id);
    boolean update(GiftCertificate giftCertificate, long id);
    boolean save(GiftCertificate giftCertificate);

    Optional<GiftCertificate> findByName(String name);
}
