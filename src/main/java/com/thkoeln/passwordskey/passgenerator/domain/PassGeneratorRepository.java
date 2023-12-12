package com.thkoeln.passwordskey.passgenerator.domain;

import com.thkoeln.passwordskey.user.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PassGeneratorRepository extends MongoRepository<PassGenerator, String> {

    PassGenerator findByUser(User user);
}