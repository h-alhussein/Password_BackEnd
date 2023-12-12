package com.thkoeln.passwordskey.group.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping("/{collectionId}")
    public ResponseEntity<?> getAllByCollectionId(@PathVariable String collectionId) {
        return new ResponseEntity<>(groupService.getAllByCollectionId(collectionId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @ModelAttribute GroupDto groupDto) {
        groupService.addGroup(groupDto);
        return new ResponseEntity<>("Your add has been reset successfully .", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @ModelAttribute GroupDto groupDto) {
        return groupService.updateGroup(id, groupDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        return groupService.deleteGroup(id);
    }


}
