package com.epam.esm.DAO.interfaces;

import com.epam.esm.dto.TagMainDto;
import com.epam.esm.models.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagDaoInterface {
    List<TagMainDto> findAll();
    Optional<TagMainDto> findById(long id);
    Optional<TagMainDto> findByName(String name);
    boolean deleteById(long id);
    boolean save(String tagName);
    boolean removeFromGiftCertificateByTagIdAndCertId(long tagId, long certId);
    boolean addTagToCertificateByTagIdAndCertId(long tagId, long certId);
}
