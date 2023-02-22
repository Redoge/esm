package com.epam.esm.DAO.interfaces;

import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GiftCertificateDaoInterface {
    List<GiftCertificate> findAll();
    Optional<GiftCertificate> findById(long id);
    Set<GiftCertificate> findByPartNameOrDescriptionAndTagNameAndSortedByName(String nameOrDescription, String tagName, boolean sortedReversed);
    Set<GiftCertificate> findByPartNameOrDescriptionAndSortedByName(String nameOrDescription, boolean sortedReversed);
    Set<GiftCertificate> findByTagNameAndSortedByName(String tagName, boolean sortedReversed);
    boolean deleteById(long id);
    boolean update(GiftCertificate giftCertificate);
    boolean save(GiftCertificate giftCertificate);
}
