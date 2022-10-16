package ba.unsa.etf.nwt.user_service.service;

import ba.unsa.etf.nwt.user_service.model.Answer;
import ba.unsa.etf.nwt.user_service.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {
    @Autowired
    private QuestionService questionService;

    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public List<Answer> findAll() {
        return answerRepository.findAll();
    }

    public List<Answer> find(Long questionId) {
        return answerRepository.findByQuestionId(questionId);
    }

    public Answer save(Answer answer) {
        return  answerRepository.save(answer);
    }
}
