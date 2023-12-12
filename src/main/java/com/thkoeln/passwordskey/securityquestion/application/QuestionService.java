package com.thkoeln.passwordskey.securityquestion.application;


import com.thkoeln.passwordskey.securityquestion.domain.Question;
import com.thkoeln.passwordskey.securityquestion.domain.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getAll() {
        return questionRepository.findAll();
    }

    public Question findById(String id) {
        return this.questionRepository.findById(id).orElse(null);
    }


}
