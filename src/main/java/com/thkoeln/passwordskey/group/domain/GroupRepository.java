package com.thkoeln.passwordskey.group.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {


    List<Group> findAllByCollectionId(String strings);

    void deleteByCollectionId(String id);

}