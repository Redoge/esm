package com.epam.esm.services;

import com.epam.esm.DAO.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.models.GiftCertificate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;

    public GiftCertificateService(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    public List<GiftCertificateMainDto> getAll(){
        return giftCertificateDao.findAll();
    }
    public Optional<GiftCertificateMainDto> getById(long id){
        return giftCertificateDao.findById(id);
    }

    public Set<GiftCertificateMainDto> getByPartNameOrDescriptionAndTagName(String nameOrDescription, String tagName){
        return giftCertificateDao.findByPartNameOrDescriptionAndTagName(nameOrDescription, tagName);
    }
    public Set<GiftCertificateMainDto> getByPartNameOrDescription(String nameOrDescription){
        return giftCertificateDao.findByPartNameOrDescription(nameOrDescription);
    }
    public Set<GiftCertificateMainDto> findByTagName(String tagName){
        return giftCertificateDao.findByTagName(tagName);
    }
    public boolean deleteById(long id){
        return giftCertificateDao.deleteById(id);
    }
    public boolean update(GiftCertificateMainDto giftCertificate){
        return giftCertificateDao.update(giftCertificate);
    }
    public boolean save(GiftCertificate giftCertificate){
        return giftCertificateDao.save(giftCertificate);
    }
}
