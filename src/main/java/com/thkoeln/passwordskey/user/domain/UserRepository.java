package com.thkoeln.passwordskey.user.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    //   Optional<User> findByEmail(String email);
    @Query(value = "{ 'email.address' : ?0 }")
    Optional<User> findByEmailAddress(String email);

    @Query(value = "{ 'email.address' :{ $regex : ?0 } }")
    Page<User> findAllByEmailAddressLike(String username, Pageable pageable);

    Page<User> findAll(Pageable pageable);

    @Query(value = "{ 'email.verifyCode' : ?0  }")
    Optional<User> findByEmailVerifyCode(String code);


    Optional<User> findByResetPasswordCode(String resetPasswordCode);
}
