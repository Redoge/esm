package com.epam.esm.controllers;

import com.epam.esm.dto.TagMainDto;
import com.epam.esm.models.Tag;
import com.epam.esm.services.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class TagControllerTest {
    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;
    private final List<TagMainDto> tagList = createTagListForTest();

    @Test
    void getTagsTest() {
        when(tagService.getAll()).thenReturn(tagList);
        ResponseEntity<?> result = tagController.getTags();
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
    }

    @Test
    void getTagByIdTest() {
        when(tagService.getById(1L)).thenReturn(Optional.ofNullable(tagList.get(0)));
        ResponseEntity<?> result = tagController.getTagById(1L);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
    }
    @Test
    void getTagByIdIncorrectTest() {
        when(tagService.getById(99L)).thenReturn(Optional.empty());
        ResponseEntity<?> result = tagController.getTagById(99L);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(404));
    }

    @Test
    void removeTagByIdTest() {
        when(tagService.deleteById(1L)).thenReturn(true);
        ResponseEntity<?> result = tagController.removeTagById(1L);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
    }
    @Test
    void removeTagByIdIncorrectTest() {
        when(tagService.deleteById(1L)).thenReturn(false);
        ResponseEntity<?> result = tagController.removeTagById(1L);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(400));
    }

    @Test
    void createTagTest() {
        var tag = new Tag(1L, "name1");
        var tagDto = new TagMainDto(1L, "name1");
        when(tagService.save( "name1")).thenReturn(true);
        when(tagService.getByName( "name1")).thenReturn(Optional.of(tagDto));
        ResponseEntity<?> result = tagController.createTag(tag);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(201));
    }
    @Test
    void createTagIncorrectTest() {
        when(tagService.save( "name1")).thenReturn(false);
        ResponseEntity<?> result = tagController.createTag(new Tag(1L, "name1"));
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(400));
    }
    private List<TagMainDto> createTagListForTest(){
        return List.of(
                new TagMainDto(1L, "name1"),
                new TagMainDto(2L, "name2"),
                new TagMainDto(3L, "name3"),
                new TagMainDto(4L, "name4"),
                new TagMainDto(5L, "name5")
        );
    }

}