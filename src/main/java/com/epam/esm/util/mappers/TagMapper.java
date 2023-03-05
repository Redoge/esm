package com.epam.esm.util.mappers;

import com.epam.esm.dto.TagMainDto;
import com.epam.esm.models.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    private final GiftCertificateMapper giftCertificateMapper;

    public TagMapper(GiftCertificateMapper giftCertificateMapper) {
        this.giftCertificateMapper = giftCertificateMapper;
    }

    public TagMainDto mapTagToMainDto(Tag tag){
        return new TagMainDto(tag.getId(),
                tag.getName(),
                tag.getCertificates().stream().map(giftCertificateMapper::mapGiftCertToNestedDto).toList());
    }
}
