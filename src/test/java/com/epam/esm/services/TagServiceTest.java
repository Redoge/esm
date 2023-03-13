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

    private final long TEST_ID = 1L;
    private final String TEST_NAME = "name2";
    private final String TEST_NAME_INCORRECT = "name969";
    @Test
    void getAllTest() {
        when(tagDao.findAll()).thenReturn(tagList);

        var result = service.getAll();
        assertEquals(tagDtoList, result);
    }

    @Test
    void getByIdTest() {
        when(tagDao.findById(1L)).thenReturn(Optional.ofNullable(tagList.get(0)));

        var result = service.getById(TEST_ID).get();
        assertEquals(tagDtoList.get(0), result);
    }

    @Test
    void getByNameTest() {
        when(tagDao.findByName(TEST_NAME)).thenReturn(Optional.ofNullable(tagList.get(1)));

        var result = service.getByName(TEST_NAME).get();
        assertEquals(tagDtoList.get(1), result);
    }

    @Test
    void deleteByIdTest() {
        when(tagDao.deleteById(TEST_ID)).thenReturn(true);

        var result = service.deleteById(TEST_ID);
        assertTrue(result);
    }

    @Test
    void saveTest() {
        when(tagDao.saveByName(TEST_NAME_INCORRECT)).thenReturn(true);

        var result = service.save(TEST_NAME_INCORRECT);
        assertTrue(result);
    }

    private List<Tag> createTagListForTest(){
        var tagList = new ArrayList<Tag>();
        tagList.add(new Tag(1, "name1", List.of()));
        tagList.add(new Tag(2, "name2", List.of()));
        tagList.add(new Tag(3, "name3", List.of()));
        tagList.add(new Tag(4, "name4", List.of()));
        tagList.add(new Tag(5, "name5", List.of()));
        return tagList;
    }

}