package com.thkoeln.passwordskey.collection.domain;

import com.thkoeln.passwordskey.user.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CollectionRepository extends MongoRepository<Collection, String> {

    List<Collection> findAllByUser(User user);
}