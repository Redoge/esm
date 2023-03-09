package com.epam.esm.DAO.interfaces;

import com.epam.esm.models.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDaoInterface {
    List<GiftCertificate> findAll();
    Optional<GiftCertificate> findById(long id);
    List<GiftCertificate> findByPartNameOrDescriptionAndTagName(String nameOrDescription, String tagName);
    List<GiftCertificate> findByPartNameOrDescription(String nameOrDescription);
    List<GiftCertificate> findByTagName(String tagName);
    boolean deleteById(long id);
    boolean update(GiftCertificate giftCertificate);
    boolean save(GiftCertificate giftCertificate);

    Optional<GiftCertificate> findByName(String name);
}
