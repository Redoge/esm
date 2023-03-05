package com.epam.esm.services.interfaces;

import com.epam.esm.dto.TagMainDto;

import java.util.List;
import java.util.Optional;

public interface TagServiceInterface {
    List<TagMainDto> getAll();

    Optional<TagMainDto> getById(long id);

    Optional<TagMainDto> getByName(String name);

    boolean deleteById(long id);

    boolean save(String tagName);
}
