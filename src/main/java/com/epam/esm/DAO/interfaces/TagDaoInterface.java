package com.epam.esm.DAO.interfaces;

import com.epam.esm.models.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagDaoInterface {
    List<Tag> findAll();
    Optional<Tag> findById(long id);
    boolean deleteById(long id);
    boolean save(String tagName);
}
