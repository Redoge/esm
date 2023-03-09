package com.epam.esm.services;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagMainDto;
import com.epam.esm.models.Tag;
import com.epam.esm.util.formatters.TimeFormatter;
import com.epam.esm.util.mappers.GiftCertificateMapper;
import com.epam.esm.util.mappers.TagMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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

        var result = service.getAll();
        assertEquals(tagDtoList, result);
    }

    @Test
    void getByIdTest() {
        when(tagDao.findById(1L)).thenReturn(Optional.ofNullable(tagList.get(0)));

        var result = service.getById(1L).get();
        assertEquals(tagDtoList.get(0), result);
    }

    @Test
    void getByNameTest() {
        when(tagDao.findByName("name2")).thenReturn(Optional.ofNullable(tagList.get(1)));

        var result = service.getByName("name2").get();
        assertEquals(tagDtoList.get(1), result);
    }

    @Test
    void deleteByIdTest() {
        when(tagDao.deleteById(3L)).thenReturn(true);

        assertTrue(service.deleteById(3L));
    }

    @Test
    void saveTest() {
        when(tagDao.saveByName("name56")).thenReturn(true);

        assertTrue(service.save("name56"));
    }


    private List<Tag> createTagListForTest() {
        var tagList = new ArrayList<Tag>();
        for(int i = 1; i <= 5; i++){
            var name = "name"+i;
            tagList.add(new Tag(i, name, List.of()));
        }
        return tagList;
    }
}