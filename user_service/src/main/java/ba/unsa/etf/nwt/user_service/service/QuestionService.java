package ba.unsa.etf.nwt.user_service.service;

import ba.unsa.etf.nwt.user_service.model.Question;
import ba.unsa.etf.nwt.user_service.repository.QuestionRepository;
import ba.unsa.etf.nwt.user_service.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Optional<Question> findById(Long questionId) {
        return questionRepository.findById(questionId);
    }

    public boolean existsById(Long questionId) {
        return questionRepository.existsById(questionId);
    }

    public Optional<Question> findByTitle(String title) {
        return questionRepository.findByTitle(title);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question save(Question question) {
        return questionRepository.save(question);
    }
}
