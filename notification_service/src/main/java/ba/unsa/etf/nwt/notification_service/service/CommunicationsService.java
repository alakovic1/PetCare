package ba.unsa.etf.nwt.notification_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CommunicationsService {

    @Autowired
    private DiscoveryClient discoveryClient;

    public String getUri(String applicationName){
        List<ServiceInstance> instances = this.discoveryClient.getInstances(applicationName);
        String uri = "";
        for (ServiceInstance instance : instances) {
            uri = instance.getUri().toString();
        }
        return uri;
    }

    public Boolean validateToken(String secret, String token){
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(getUri("user_service")
                + "/auth/load/invalid/token/" + secret+ "/" + token, Boolean.class);
    }

}
