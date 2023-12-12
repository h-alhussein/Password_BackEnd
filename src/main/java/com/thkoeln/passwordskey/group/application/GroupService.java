package com.thkoeln.passwordskey.group.application;


import com.thkoeln.passwordskey.account.domain.AccountRepository;
import com.thkoeln.passwordskey.collection.domain.Collection;
import com.thkoeln.passwordskey.collection.domain.CollectionRepository;
import com.thkoeln.passwordskey.group.domain.Group;
import com.thkoeln.passwordskey.group.domain.GroupRepository;
import com.thkoeln.passwordskey.image.application.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final AccountRepository accountRepository;
    private final ImageService imageService;

    public List<Group> getAllByCollectionId(String collectionId) {
        List<Group> groups = groupRepository.findAllByCollectionId(collectionId);
        return groups;
    }

    public void addGroup(GroupDto groupDto) {
        String imageId = null;
        if (groupDto.getImage() != null)
            imageId = imageService.save(groupDto.getImage());
        Group group = Group.builder()
                .name(groupDto.getName())
                .collection(Collection.builder().id(groupDto.getCollectionId()).build())
                .imageId(imageId).build();
        groupRepository.save(group);

    }

    public ResponseEntity<?> updateGroup(String id, GroupDto groupDto) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            if (groupDto.getImage() != null)
                group.get().setImageId(imageService.save(groupDto.getImage()));
            group.get().setName(groupDto.getName());
            groupRepository.save(group.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    public ResponseEntity<?> deleteGroup(String id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            // delete all  group accounts
            accountRepository.deleteByGroupId(id);
            // delete the  group
            groupRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }


}
