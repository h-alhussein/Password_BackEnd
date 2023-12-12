package com.thkoeln.passwordskey.account.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccountRepository extends MongoRepository<Account, String> {

    List<Account> findAllByGroupId(String groupId);
    List<Account> findAllByGroupIdAndFav(String groupId,boolean fav);


    void deleteByGroupId(String id);
}