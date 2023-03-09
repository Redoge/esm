package com.epam.esm.services;

import com.epam.esm.dao.interfaces.TagDaoInterface;
import com.epam.esm.dto.TagMainDto;
import com.epam.esm.services.interfaces.TagServiceInterface;
import com.epam.esm.util.mappers.TagMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService implements TagServiceInterface {
    private final TagDaoInterface tagDao;
    private final TagMapper mapper;

    public TagService(TagDaoInterface tagDao, TagMapper mapper) {
        this.tagDao = tagDao;
        this.mapper = mapper;
    }

    public List<TagMainDto> getAll() {
        return tagDao.findAll()
                .stream()
                .map(mapper::mapTagToMainDto)
                .toList();
    }

    public Optional<TagMainDto> getById(long id) {
        return tagDao.findById(id)
                .map(mapper::mapTagToMainDto);
    }

    public Optional<TagMainDto> getByName(String name) {
        return tagDao.findByName(name)
                .map(mapper::mapTagToMainDto);
    }

    public boolean deleteById(long id) {
        return tagDao.deleteById(id);
    }

    public boolean save(String tagName) {
        return tagDao.saveByName(tagName);
    }
}
