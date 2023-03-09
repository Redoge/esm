package com.epam.esm.DAO.interfaces;

import com.epam.esm.models.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDaoInterface {
    List<Tag> findAll();
    Optional<Tag> findById(long id);
    Optional<Tag> findByName(String name);
    boolean deleteById(long id);
    boolean saveByName(String tagName);

}
