package com.thkoeln.passwordskey.account.application;


import com.thkoeln.passwordskey.account.domain.Account;
import com.thkoeln.passwordskey.account.domain.AccountRepository;
import com.thkoeln.passwordskey.collection.application.CollectionService;
import com.thkoeln.passwordskey.collection.domain.Collection;
import com.thkoeln.passwordskey.collection.domain.CollectionRepository;
import com.thkoeln.passwordskey.group.application.GroupService;
import com.thkoeln.passwordskey.group.domain.Group;
import com.thkoeln.passwordskey.image.application.ImageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ImageService imageService;
    private final CollectionService collectionService;
    private final GroupService groupService;
    private final AESService aesService;
    private final CollectionRepository collectionRepository;

    public List<Account> getAllByGroupId(String groupId) {
        List<Account> accounts = accountRepository.findAllByGroupId(groupId);
        return decryptList(accounts);
    }

    public void addAccount(AccountDto accountDto) {
        String imageId = null;
        if (accountDto.getImage() != null)
            imageId = imageService.save(accountDto.getImage());
        Account account = Account.builder()
                .name(accountDto.getName())
                .username(aesService.encrypt(accountDto.getUsername()))
                .password(aesService.encrypt(accountDto.getPassword()))
                .website(aesService.encrypt(accountDto.getWebsite()))
                .notes(aesService.encrypt(accountDto.getNotes()))
                .group(Group.builder().id(accountDto.getGroupId()).build())
                .fav(false)
                .createdTime(new Date())
                .imageId(imageId).build();
        accountRepository.save(account);
    }

    public ResponseEntity<?> updateAccount(String id, AccountDto accountDto) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            if (accountDto.getImage() != null)
                account.get().setImageId(imageService.save(accountDto.getImage()));
            account.get().setName(accountDto.getName());
            account.get().setUsername(aesService.encrypt(accountDto.getUsername()));
            account.get().setPassword(aesService.encrypt(accountDto.getPassword()));
            account.get().setWebsite(aesService.encrypt(accountDto.getWebsite()));
            account.get().setNotes(aesService.encrypt(accountDto.getNotes()));
            accountRepository.save(account.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    public ResponseEntity<?> deleteAccount(String id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            accountRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }


    public ResponseEntity<?> changeFav(String id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            account.get().setFav(!account.get().isFav());
            accountRepository.save(account.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    public ResponseEntity<?> getFav() {
        List<Account> favAccount = new ArrayList<>();
        List<Collection> collections = collectionService.getUserCollections();
        for (Collection collection : collections) {
            List<Group> groups = groupService.getAllByCollectionId(collection.getId());
            for (Group group : groups)
                favAccount.addAll(accountRepository.findAllByGroupIdAndFav(group.getId(), true));
        }
        for (Account account : favAccount)
            account.getGroup().setCollection(null);
        return new ResponseEntity<>(decryptList(favAccount), HttpStatus.OK);
    }

    public List<Account> decryptList(List<Account> accounts){
        for (Account account:accounts) {
            account.setUsername(aesService.decrypt(account.getUsername()));
            account.setPassword(aesService.decrypt(account.getPassword()));
            account.setWebsite(aesService.decrypt(account.getWebsite()));
            account.setNotes(aesService.decrypt(account.getNotes()));

        }
        return accounts;

    }


//
    //
}
