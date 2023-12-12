package com.thkoeln.passwordskey.securityquestion.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuestionRepository extends MongoRepository<Question, String> {

    Optional<Question> findByQuestion(String question);
}