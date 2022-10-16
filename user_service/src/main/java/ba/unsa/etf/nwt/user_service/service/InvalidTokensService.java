package ba.unsa.etf.nwt.user_service.service;

import ba.unsa.etf.nwt.user_service.model.InvalidTokens;
import ba.unsa.etf.nwt.user_service.repository.InvalidTokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvalidTokensService {
    @Autowired
    private InvalidTokensRepository invalidTokensRepository;

    public List<InvalidTokens> getAllTokens(){
        return invalidTokensRepository.findAll();
    }

    public InvalidTokens addToken(InvalidTokens invalidToken){
        return invalidTokensRepository.save(invalidToken);
    }

    public void deleteExpiredToken(Long id){
        invalidTokensRepository.deleteById(id);
    }
}
