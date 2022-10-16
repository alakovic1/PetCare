package ba.unsa.etf.nwt.adopt_service;

import ba.unsa.etf.nwt.adopt_service.service.CommunicationsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class AdoptRequestRoutesTest {
    @Autowired
    private MockMvc mockMvc;

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

    // Testovi za AdoptionRequest

    @Test
    void GetAllAdoptionRequestsInJSON() throws Exception {

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/adoption-request")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void CreateNewValidAdoptionRequest() throws Exception {
        String newRequest = "{\n" +
                "    \"userID\": 1,\n" +
                "    \"petID\": 1,\n" +
                "    \"message\": \"Pet care\",\n" +
                "    \"approved\": false\n" +
                "}\n";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/adoption-request")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newRequest);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "    \"success\": true,\n" +
                        "    \"message\": \"Request to adopt a pet with ID=1 added successfully!\",\n" +
                        "    \"status\": \"OK\"\n" +
                        "}\n"));
    }

    @Test
    void CreateNewAdoptionRequestMessageTooLong() throws Exception {
        String newRequest = "{\n" +
                "    \"userID\": 1,\n" +
                "    \"petID\": 1,\n" +
                "    \"message\": \"2PTSnKeDdMt2W5bOFniPkltpqGAVGdBioKziwAhAkXb2GAOU6Pj1LcByz46vFlm0FZBclkTJgpE7kO6qURguKMlsEFaxfaq4cQBx9n9uihggsFs0yLFdvn1WGbwjg75b4c3Z9FLNeNGuruowqSoFIiw3RDJnJasgQB8HW6CvRNrtnuoEgt9q6uB8hEQdAwGyVjU3wKes7kdWA9sxMVWR4uPUzDW58nbSxNnQxlPjucv6oTl3yOsqe9vqZkMDBpw2gslVxga09PUNziTtDRGIGz06oRJ51TY6Y67xg1UCGap3pxoSD7ascfQGXU5TFrDCNrjkTxHEnEycCh53u5XCAUcJhEDAqx0cvEf5OXoeC58qqX4o8TxnydvEVmtKgxvxdpJ6nqwtfTeLlA6jQX2YSdlAGJfct7OxOoegvMMsLlfmFKDty7HI82tH0Vj8KSsRQW06TzaFd8HtvLOLHsYZkhbjfpp9lk1psVhHuUxZT0WkNByyGNXz5lZy0dbR6VzBzY3uA8SiXcO7EQ9ZnordBI0juuZ98p3QMNKmfeILK9SSnnlrDU8lk2tBseIyCgwraOKhji20ajFHBHiKGMwCIrXP5wnuXAE0dYuUanESxHyWusSMgHYb8UkUFIY7W2Q1p5vXgrHO1iB5EHKCeHXLLBMUU4lih5PhbhG0elSqmL50CwDBTeQofTSNSMAksnYRUCE37UqBuUdsYtZscrEsIGPhJ4zRCTWxa8cllFYwYWHsPx9s5dSDaH7sITX2CEmXyH197UR7pleoWXq8XY9X9JVBE3Z2PpMWjdQI0GFztPf8XAW1xztAa5bDF35pyRAhFHuxkwiyF3sGEYEWGfmR1xeQPG5hpAxKuRhBRKMD1LO3NIiZ0kW6HMnIOmciRHYOj5LIQCnPnMlIJ2ai6hILH5LQeopwAeHwaJci8Sd7NnoTFxd61qcYLcoTCKInD29R3CH9niojbbfXLO7T1jg9kAsGQK6oiUb1hnHs06klIuGZa\",\n" +
                "    \"approved\": false\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/adoption-request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newRequest);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "\"responseMessage\": {\n" +
                        "\"success\": false,\n" +
                        "\"status\": \"BAD_REQUEST\",\n" +
                        "\"message\": \"Validation Failed\"\n" +
                        "},\n" +
                        "\"details\": [\n" +
                        "\"Request message can't have more than 1000 characters.\"\n" +
                        "]\n" +
                        "}\n"));
    }

    @Test
    void CreateNewAdoptionRequestUserIDNull() throws Exception {
        String newRequest = "{\n" +
                "    \"userID\": null,\n" +
                "    \"petID\": 1,\n" +
                "    \"message\": \"Pet care\",\n" +
                "    \"approved\": false\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/adoption-request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newRequest);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "\"responseMessage\": {\n" +
                        "\"success\": false,\n" +
                        "\"status\": \"BAD_REQUEST\",\n" +
                        "\"message\": \"Validation Failed\"\n" +
                        "},\n" +
                        "\"details\": [\n" +
                        "\"User ID can't be null.\"\n" +
                        "]\n" +
                        "}\n"));
    }

    @Test
    void CreateNewAdoptionRequestUserIDMissing() throws Exception {
        String newRequest = "{\n" +
                "    \"petID\": 1,\n" +
                "    \"message\": \"Pet care\",\n" +
                "    \"approved\": false\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/adoption-request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newRequest);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "\"responseMessage\": {\n" +
                        "\"success\": false,\n" +
                        "\"status\": \"BAD_REQUEST\",\n" +
                        "\"message\": \"Validation Failed\"\n" +
                        "},\n" +
                        "\"details\": [\n" +
                        "\"User ID can't be null.\"\n" +
                        "]\n" +
                        "}\n"));
    }

    @Test
    void CreateNewAdoptionRequestPetIDNull() throws Exception {
        String newRequest = "{\n" +
                "    \"userID\": 1,\n" +
                "    \"petID\": null,\n" +
                "    \"message\": \"Pet care\",\n" +
                "    \"approved\": false\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/adoption-request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newRequest);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "\"responseMessage\": {\n" +
                        "\"success\": false,\n" +
                        "\"status\": \"BAD_REQUEST\",\n" +
                        "\"message\": \"Validation Failed\"\n" +
                        "},\n" +
                        "\"details\": [\n" +
                        "\"Pet ID can't be null.\"\n" +
                        "]\n" +
                        "}"));
    }

    @Test
    void CreateNewAdoptionRequestPetIDMissing() throws Exception {
        String newRequest = "{\n" +
                "    \"userID\": 1,\n" +
                "    \"message\": \"Pet care\",\n" +
                "    \"approved\": false\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/adoption-request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newRequest);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "\"responseMessage\": {\n" +
                        "\"success\": false,\n" +
                        "\"status\": \"BAD_REQUEST\",\n" +
                        "\"message\": \"Validation Failed\"\n" +
                        "},\n" +
                        "\"details\": [\n" +
                        "\"Pet ID can't be null.\"\n" +
                        "]\n" +
                        "}"
                ));
    }

    @Test
    void GetApprovedAdoptionRequestsInJSON() throws Exception {

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/adoption-request/approved")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetNotApprovedAdoptionRequestsInJSON() throws Exception {

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/adoption-request/not-approved")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetAdoptionRequestsByUserIDInJSON() throws Exception {

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/adoption-request/user/{userID}", 1)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetAdoptionRequestsByNewPetIDInJSON() throws Exception {

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/adoption-request/pet/{petID}", 400)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
