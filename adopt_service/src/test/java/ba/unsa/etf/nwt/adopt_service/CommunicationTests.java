package ba.unsa.etf.nwt.adopt_service;

import ba.unsa.etf.nwt.adopt_service.service.AddPetRequestService;
import ba.unsa.etf.nwt.adopt_service.service.AdoptionRequestService;
import ba.unsa.etf.nwt.adopt_service.service.CommunicationsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class CommunicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    private CommunicationsService communicationsService;

    public String getToken(){
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String input = "{\n" +
                    "  \"password\": \"Password123!\",\n" +
                    "  \"usernameOrEmail\": \"alakovic1\"\n" +
                    "}";

            HttpEntity<String> httpEntity = new HttpEntity<>(input, headers);

            final String route = communicationsService.getUri("user_service") + "/api/auth/login/token";
            URI uri = new URI(route);

            return restTemplate.postForObject(uri,
                    httpEntity, String.class);

        } catch (Exception e){
            System.out.println("Can't connect to user_service");
        }
        return null;
    }

    @Test
    public void getCurrentUserIDFromUserService() {
        Long id = 1L;
        Mockito
                .when(restTemplate.getForEntity(
                        communicationsService.getUri("user_service")
                                + "/user/me/id", Long.class))
                .thenReturn(new ResponseEntity(id, HttpStatus.OK));
    }

    @Test
    public void getCurrentPetIDFromPetService() {
        Long id = 1L;
        Mockito
                .when(restTemplate.getForEntity(
                        communicationsService.getUri("pet_category_service")
                                + "/current/pet/petID" + id, Long.class))
                .thenReturn(new ResponseEntity(id, HttpStatus.OK));
    }


    @Test
    void AddAdoptionRequest() throws Exception {
        String newAdoptRequest = "{\n" +
                "    \"userID\": 1,\n" +
                "    \"message\": \"Pet care\",\n" +
                "    \"approved\": false\n" +
                "}\n";

        Long petId = 1L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/eurekaa/adoption-request/{petId}", petId, newAdoptRequest)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newAdoptRequest);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        //pregled svih adoption zahtjeva
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/adoption-request")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder2)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[3].message").value("Pet care"));
    }

    @Test
    void AddAdoptionRequestPetIDNotFound() throws Exception {
        String newAdoptRequest = "{\n" +
                "    \"userID\": 1,\n" +
                "    \"message\": \"Pet care\",\n" +
                "    \"approved\": false\n" +
                "}\n";

        Long petId = 50L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/eurekaa/adoption-request/{petId}", petId, newAdoptRequest)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newAdoptRequest);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
