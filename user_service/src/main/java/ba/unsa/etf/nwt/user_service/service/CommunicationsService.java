package ba.unsa.etf.nwt.user_service.service;

import ba.unsa.etf.nwt.user_service.model.InvalidTokens;
import ba.unsa.etf.nwt.user_service.security.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationsService {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private InvalidTokensService invalidTokensService;

    public String getUri(String applicationName){
        List<ServiceInstance> instances = this.discoveryClient.getInstances(applicationName);
        String uri = "";
        for (ServiceInstance instance : instances) {
            uri = instance.getUri().toString();
        }
        return uri;
    }

    public Boolean isValidToken(String secret, String token){
        List<InvalidTokens> invalidTokens = invalidTokensService.getAllTokens();

        //obrisati sve tokene koji su istekli
        for(InvalidTokens invalidToken : invalidTokens){
            try {
                Jwts.parser().setSigningKey(secret).parseClaimsJws(invalidToken.getToken());
            } catch (ExpiredJwtException ex) {
                System.out.println("Expired JWT token in InvalidTokens table");
                invalidTokensService.deleteExpiredToken(invalidToken.getId());
            }
        }

        List<InvalidTokens> newInvalidTokens = invalidTokensService.getAllTokens();

        //ako se nalazi u listi, validacija ne smije proci
        for(InvalidTokens invalidToken : newInvalidTokens){
            if(invalidToken.getToken().equals(token)) {
                System.out.println("Token is one of the invalid tokens, UNAUTHORIZED REQUEST!");
                return false;
            }
        }

        return true;
    }

}
