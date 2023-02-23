package com.epam.esm.services;

import com.epam.esm.DAO.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateMainDto;
import com.epam.esm.dto.GiftCertificateNestedDto;
import com.epam.esm.models.GiftCertificate;
import com.epam.esm.models.Tag;
import com.epam.esm.pojo.GiftCertificateSaveRequestPojo;
import com.epam.esm.pojo.GiftCertificateSearchRequestPojo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    public Optional<GiftCertificateMainDto> getByName(String name){
        return giftCertificateDao.findByName(name);
    }

    public Set<GiftCertificateMainDto> getByPartNameOrDescriptionAndTagName(String nameOrDescription, String tagName){
//        return giftCertificateDao.findByPartNameOrDescriptionAndTagName(nameOrDescription, tagName); //TODO: try to change query for will be work without stream
        return giftCertificateDao.findByPartNameOrDescription(nameOrDescription)
                .stream()
                .filter(c-> c.getTags().
                        stream().
                        anyMatch(tag -> tag.getName()
                                .equalsIgnoreCase(tagName)))
                .collect(Collectors.toSet());
    }
    public Set<GiftCertificateMainDto> getByPartNameOrDescription(String nameOrDescription){
        return giftCertificateDao.findByPartNameOrDescription(nameOrDescription);
    }
    public Set<GiftCertificateMainDto> getByTagName(String tagName){
        return giftCertificateDao.findByTagName(tagName);
    }
    public boolean deleteById(long id){
        return giftCertificateDao.deleteById(id);
    }
    public boolean update(GiftCertificateSaveRequestPojo giftCertificate, long id){
        GiftCertificate gc = giftCertificateSaveRequestPojoToGiftCertificateTransfer(giftCertificate);
        return giftCertificateDao.update(gc, id);
    }
    public boolean save(GiftCertificateSaveRequestPojo giftCertificate){
        if(correctCertificate(giftCertificate)){
            GiftCertificate gc = giftCertificateSaveRequestPojoToGiftCertificateTransfer(giftCertificate);
            return giftCertificateDao.save(gc);
        }
        return false;
    }




    public List<GiftCertificateMainDto> getByGiftCertificateSearchRequestPojo(GiftCertificateSearchRequestPojo req) {
        List<GiftCertificateMainDto> result;
        if ((req.getName() != null && req.getName().length() > 0) ||
                (req.getDescription() != null && req.getDescription().length() > 0)){
            String nameOrDescriptionString = (req.getName() != null && req.getName().length() > 0)?req.getName():
                    req.getDescription();
            if(req.getTagName() != null && req.getTagName().length() > 0){
                result = List.copyOf(getByPartNameOrDescriptionAndTagName(nameOrDescriptionString, req.getTagName()));
            }else{
                result = List.copyOf(getByPartNameOrDescription(nameOrDescriptionString));
            }
        }else{
            if(req.getTagName() != null && req.getTagName().length() > 0){
                result = List.copyOf(getByTagName( req.getTagName()));
            }else{
                result = getAll();
            }
        }if(req.getSortByName() != null && req.getSortByName().length()>0){
            String sortByName = req.getSortByName();
            if(sortByName.equalsIgnoreCase("ASC")){
                result.sort(Comparator.comparing(GiftCertificateMainDto::getName));
            }else if((sortByName.equalsIgnoreCase("DESC"))) {
                result.sort(Comparator.comparing(GiftCertificateMainDto::getName).reversed());
            }
        }else if(req.getSortByTime() != null && req.getSortByTime().length()>0){
            String sortByName = req.getSortByTime();
            if(sortByName.equalsIgnoreCase("ASC")){
                result.sort(Comparator.comparing(GiftCertificateMainDto::getLastUpdateDate));
            }else if((sortByName.equalsIgnoreCase("DESC"))) {
                result.sort(Comparator.comparing(GiftCertificateMainDto::getLastUpdateDate).reversed());
            }
        }
        return result;
    }


    private boolean correctCertificate(GiftCertificateSaveRequestPojo giftCertificate){
        return ((giftCertificate.getName()!=null && giftCertificate.getName().length()>0)
                && (giftCertificate.getDescription()!=null && giftCertificate.getDescription().length()>0)
                && (giftCertificate.getPrice()!=null&&giftCertificate.getPrice().intValue()>=0)
                && (giftCertificate.getDuration()>=0));
    }
    private GiftCertificate giftCertificateSaveRequestPojoToGiftCertificateTransfer(GiftCertificateSaveRequestPojo giftCertificatePojo) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(giftCertificatePojo.getName());
        giftCertificate.setDescription(giftCertificatePojo.getDescription());
        giftCertificate.setDuration(giftCertificatePojo.getDuration());
        giftCertificate.setPrice(giftCertificatePojo.getPrice());
        if(giftCertificatePojo.getTags()!=null) {
            giftCertificate.setTags(
                    giftCertificatePojo.getTags().stream().map(o -> {
                        Tag tag = new Tag();
                        tag.setName(o);
                        return tag;
                    }).collect(Collectors.toList()));
        }
        return giftCertificate;
    }
}
