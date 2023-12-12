package com.thkoeln.passwordskey.collection.application;


import com.thkoeln.passwordskey.account.application.AccountDto;
import com.thkoeln.passwordskey.account.application.AccountService;
import com.thkoeln.passwordskey.account.domain.Account;
import com.thkoeln.passwordskey.account.domain.AccountRepository;
import com.thkoeln.passwordskey.collection.domain.Collection;
import com.thkoeln.passwordskey.collection.domain.CollectionRepository;
import com.thkoeln.passwordskey.group.application.GroupDto;
import com.thkoeln.passwordskey.group.domain.Group;
import com.thkoeln.passwordskey.group.domain.GroupRepository;
import com.thkoeln.passwordskey.image.application.ImageService;
import com.thkoeln.passwordskey.user.application.UserService;
import com.thkoeln.passwordskey.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final GroupRepository groupRepository;
    private final AccountRepository accountRepository;

    private final ImageService imageService;
    private final UserService userService;


    public List<Collection> getUserCollections() {
        User user = userService.getCurrentUser();
        List<Collection> collections = collectionRepository.findAllByUser(user);
        for (Collection collection : collections) collection.setUser(null);
        return collections;
    }

    public ResponseEntity<?> addCollection(CollectionDto collectionDto) {
        String imageId = null;
        if (collectionDto.getImage() != null)
            imageId = imageService.save(collectionDto.getImage());
        // get user
        User user = userService.getCurrentUser();
        // validate data

        // create and save
        Collection collection = Collection.builder()
                .name(collectionDto.getName())
                .user(user)
                .imageId(imageId).build();
        collectionRepository.save(collection);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateCollection(String id, CollectionDto collectionDto) {
        Optional<Collection> collection = collectionRepository.findById(id);
        if (collection.isPresent()) {
            if (collectionDto.getImage() != null)
                collection.get().setImageId(imageService.save(collectionDto.getImage()));
            collection.get().setName(collectionDto.getName());

            collectionRepository.save(collection.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    public ResponseEntity<?> deleteCollection(String id) {
        Optional<Collection> collection = collectionRepository.findById(id);
        if (collection.isPresent()) {
            // get all Collection groups
            List<Group> groups = groupRepository.findAllByCollectionId(id);

            // delete all accounts in groups
            for (Group group : groups)
                accountRepository.deleteByGroupId(group.getId());

            // delete all Collection groups
            groupRepository.deleteByCollectionId(id);

            // delete the collection
            collectionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    public List<Account> findallAccounts(String id){
        List<Group> groups = groupRepository.findAllByCollectionId(id);
        List<Account> accounts = new ArrayList<>();

        for (Group group :groups){
           List<Account> s = accountRepository.findAllByGroupId(group.getId());
           for (Account i : s){

               accounts.add(i);

           }
        }
        return accounts;

    }

    public List<Group> findallGroups(){
        List<Group> allGroups= new ArrayList<Group>();
        for (Collection collection : getUserCollections()){
            List<Group> groups = groupRepository.findAllByCollectionId(collection.getId());
            for (Group group : groups){
                allGroups.add(group);
            }

        }
        return allGroups;
    }


}
