package com.epam.esm.services;

import com.epam.esm.DAO.TagDao;
import com.epam.esm.dto.TagMainDto;
import com.epam.esm.models.Tag;
import com.epam.esm.util.formatters.TimeFormatter;
import com.epam.esm.util.mappers.GiftCertificateMapper;
import com.epam.esm.util.mappers.TagMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    private final TagDao tagDao = mock(TagDao.class);
    private final TagMapper tagMapper = new TagMapper(new GiftCertificateMapper(new TimeFormatter()));
    private final TagService service = new TagService(tagDao, tagMapper);
    private final List<Tag> tagList = createTagListForTest();
    private final List<TagMainDto> tagDtoList = tagList.stream()
            .map(tagMapper::mapTagToMainDto).toList();


    @Test
    void getAllTest() {
        when(tagDao.findAll()).thenReturn(tagList);
        assertEquals(mapTagDtoToString(service.getAll()), mapTagDtoToString(tagDtoList));
    }

    @Test
    void getByIdTest() {
        when(tagDao.findById(1L)).thenReturn(Optional.ofNullable(tagList.get(0)));
        assertEquals(service.getById(1L).get().getName(), tagDtoList.get(0).getName());
    }

    @Test
    void getByNameTest() {
        when(tagDao.findByName("name2")).thenReturn(Optional.ofNullable(tagList.get(1)));
        assertEquals(service.getByName("name2").get().toString(), tagDtoList.get(1).toString());
    }

    @Test
    void deleteByIdTest() {
        when(tagDao.deleteById(3L)).thenReturn(true);
        assertTrue(service.deleteById(3L));
    }

    @Test
    void saveTest() {
        when(tagDao.save("name56")).thenReturn(true);
        assertTrue(service.save("name56"));
    }

    private List<String> mapTagDtoToString(List<TagMainDto> tagDto){
        return tagDto.stream().map(TagMainDto::getName).toList();
    }

    private List<Tag> createTagListForTest() {
        return List.of(
                new Tag(1L, "name1", List.of()),
                new Tag(2L, "name2", List.of()),
                new Tag(3L, "name3", List.of()),
                new Tag(4L, "name4", List.of()),
                new Tag(5L, "name5", List.of())
        );
    }
}