package com.epam.esm.controllers;

import com.epam.esm.dto.TagMainDto;
import com.epam.esm.models.Tag;
import com.epam.esm.services.TagService;
import com.epam.esm.util.ResponseWrapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("")
    public ResponseEntity<List<TagMainDto>> getTags(){
        return ResponseEntity.ok(tagService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTagById(@PathVariable long id){
        var tag = tagService.getById(id);
        if(tag.isPresent()){
            return ResponseEntity.ok(tag.get());
        }
        return new ResponseEntity<>(new ResponseWrapper(404, String.format("Not Found! (id = %d)", id),4041),
                HttpStatusCode.valueOf(404));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeTagById(@PathVariable long id){
        boolean success = tagService.deleteById(id);
        return success?ResponseEntity.ok(new ResponseWrapper(200, String.format("Removed successfully! (id = %d)", id), 2001)) :
                new ResponseEntity<>(new ResponseWrapper(400, String.format("Deletion is not successful! (id = %d)", id), 4001),
                        HttpStatusCode.valueOf(400));
    }

    @PostMapping("")
    public ResponseEntity<?> createTag(@RequestBody Tag tag){
        boolean success = false;
        if(tag.getName()!=null && tag.getName().length()!=0){
            success = tagService.save(tag.getName());
        }
        if(success) tag.setId(tagService.getByName(tag.getName()).get().getId());
        return success? new ResponseEntity<>(new ResponseWrapper(201, String.format("Created successfully! " +
                "(Name = %s, id = %d)", tag.getName(), tag.getId()), 2011),HttpStatusCode.valueOf(201))
                : new ResponseEntity<>(new ResponseWrapper(400, "Created is not successful!", 4002),
                HttpStatusCode.valueOf(400));
    }


}
