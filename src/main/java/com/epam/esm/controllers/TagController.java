package com.epam.esm.controllers;

import com.epam.esm.dto.TagMainDto;
import com.epam.esm.models.Tag;
import com.epam.esm.services.interfaces.TagServiceInterface;
import com.epam.esm.util.ResponseWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tags")
public class TagController {
    private final TagServiceInterface tagService;

    public TagController(TagServiceInterface tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagMainDto>> getTags() {
        return ResponseEntity.ok(tagService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTagById(@PathVariable long id) {
        var tag = tagService.getById(id);
        return tag.isPresent() ? ResponseEntity.ok(tag.get()) :
                new ResponseEntity<>(new ResponseWrapper(HttpStatus.NOT_FOUND.value(),
                        String.format("Not Found! (id = %d)", id), 4041), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeTagById(@PathVariable long id) {
        boolean success = tagService.deleteById(id);
        return success ? ResponseEntity.ok(new ResponseWrapper(HttpStatus.OK.value(),
                String.format("Removed successfully! (id = %d)", id), 2001)) :
                new ResponseEntity<>(new ResponseWrapper(HttpStatus.BAD_REQUEST.value(),
                        String.format("Deletion is not successful! (id = %d)", id), 4001),
                        HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        boolean success = false;
        if (StringUtils.isNotEmpty(tag.getName())) {
            success = tagService.save(tag.getName());
        }
        if (success) tag.setId(tagService.getByName(tag.getName()).get().getId());
        return success ? new ResponseEntity<>(new ResponseWrapper(HttpStatus.CREATED.value(),
                String.format("Created successfully! (Name = %s, id = %d)", tag.getName(), tag.getId()),
                2011), HttpStatus.CREATED) :
                new ResponseEntity<>(new ResponseWrapper(HttpStatus.BAD_REQUEST.value(),
                        "Created is not successful!", 4002), HttpStatus.BAD_REQUEST);
    }
}
