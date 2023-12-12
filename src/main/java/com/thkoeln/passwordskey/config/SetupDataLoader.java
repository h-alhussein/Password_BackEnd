package com.thkoeln.passwordskey.config;

import com.thkoeln.passwordskey.collection.domain.Collection;
import com.thkoeln.passwordskey.collection.domain.CollectionRepository;
import com.thkoeln.passwordskey.group.domain.Group;
import com.thkoeln.passwordskey.group.domain.GroupRepository;
import com.thkoeln.passwordskey.passgenerator.application.PassGeneratorService;
import com.thkoeln.passwordskey.passgenerator.domain.PassGenerator;
import com.thkoeln.passwordskey.passgenerator.domain.PassGeneratorRepository;
import com.thkoeln.passwordskey.securityquestion.domain.Question;
import com.thkoeln.passwordskey.securityquestion.domain.QuestionRepository;
import com.thkoeln.passwordskey.user.domain.User;
import com.thkoeln.passwordskey.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final QuestionRepository questionRepository;
    private final PassGeneratorService passGeneratorService;
    private final PassGeneratorRepository passGeneratorRepository;
    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;
    private final GroupRepository groupRepository;
    private boolean alreadySetup = false;

    @SneakyThrows
    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        addAllQuestions();

        //        PassGenerator passGenerator = PassGenerator.builder()
//                .length(20)
//                .upperCase(true)
//                .digits(true)
//                .space(true)
//                .special(true)
//                .underLine(true)
//                .minus(true)
//                .brackets(true)
//                .build();
//
//        System.out.println("Passay Password: " + passGeneratorService.generatePassword(passGenerator));
//        System.out.println("Passay Password: " + passGeneratorService.generatePassword(passGenerator));
//        System.out.println("Passay Password: " + passGeneratorService.generatePassword(passGenerator));
//        System.out.println("Passay Password: " + passGeneratorService.generatePassword(passGenerator));

        addPassGenerator();


        alreadySetup = true;
    }

    private void addPassGenerator() {

        for(User user:userRepository.findAll())
            if(passGeneratorRepository.findByUser(user)==null){
                PassGenerator passGenerator = new PassGenerator();
                passGenerator.setUser(user);
                passGeneratorRepository.save(passGenerator);
            }



    }

    private void addAllQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("What is your mom first name?"));
        questions.add(new Question("What is your first bite name?"));
        questions.add(new Question("What is your first car color?"));
        questions.add(new Question("What is your first car model?"));

        for (Question question : questions)
            if (!questionRepository.findByQuestion(question.getQuestion()).isPresent())
                questionRepository.save(question);


    }

    private void addCollections() {
        List<User> users = userRepository.findAll();
        for (User user : users)
            if (collectionRepository.findAllByUser(user).size() == 0)
                addCollection(user);
    }

    private void addCollection(User user) {
        Collection collection = collectionRepository.save(Collection.builder().name("Default Collection").user(user).build());
        groupRepository.save(Group.builder().name("Default Group").collection(collection).build());

    }

}