package com.epam.esm.services;

import com.epam.esm.DAO.TagDao;
import com.epam.esm.dto.TagMainDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagDao tagDao;

    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public List<TagMainDto> getAll(){
        return tagDao.findAll();
    }
    public Optional<TagMainDto> getById(long id){
        return tagDao.findById(id);
    }
    public Optional<TagMainDto> getByName(String name){
        return tagDao.findByName(name);
    }
    public boolean deleteById(long id){
        return tagDao.deleteById(id);
    }
    public boolean save(String tagName){
        return tagDao.save(tagName);
    }
}
