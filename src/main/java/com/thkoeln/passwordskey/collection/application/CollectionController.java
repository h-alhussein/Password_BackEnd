package com.thkoeln.passwordskey.collection.application;

import com.thkoeln.passwordskey.collection.domain.CollectionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/collections")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        collectionService.getUserCollections();
        return new ResponseEntity<>(collectionService.getUserCollections(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return new ResponseEntity<>("Your getById has been reset successfully .", HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> add(@Valid @ModelAttribute CollectionDto collectionDto) {
        collectionService.addCollection(collectionDto);
        return new ResponseEntity<>("Your add has been reset successfully .", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @ModelAttribute CollectionDto collectionDto) {
        return collectionService.updateCollection(id, collectionDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        return collectionService.deleteCollection(id);
    }



  @GetMapping("/groups")
    public ResponseEntity<?> getGroups(){
        return new ResponseEntity(collectionService.findallGroups(),HttpStatus.OK);
  }

    }







