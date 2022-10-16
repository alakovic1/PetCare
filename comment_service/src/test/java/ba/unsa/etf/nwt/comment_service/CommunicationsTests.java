package ba.unsa.etf.nwt.comment_service;

import ba.unsa.etf.nwt.comment_service.service.CommunicationsService;
import net.minidev.json.JSONObject;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class CommunicationsTests {

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
    public void getCurrentUserUsernameFromUserService() {
        String username = "alakovic1";
        Mockito
                .when(restTemplate.getForEntity(
                        communicationsService.getUri("user_service")
                                + "/user/me/username", String.class))
          .thenReturn(new ResponseEntity(username, HttpStatus.OK));
    }

    @Test
    public void getUsernameByUsername(){
        String username = "user";
        Mockito
                .when(restTemplate.getForEntity(
                        communicationsService.getUri("user_service")
                                + "/user/" + username, String.class))
                .thenReturn(new ResponseEntity(username, HttpStatus.OK));
    }

    @Test
    public void getCategoryByPetId(){
        Long categoryId = 63L;
        Mockito
                .when(restTemplate.getForEntity(
                        communicationsService.getUri("pet_category_service")
                                + "/current/pet/petID/" + categoryId, Long.class))
                .thenReturn(new ResponseEntity(categoryId, HttpStatus.OK));
    }

    @Test
    public void getCategoryByRaseId(){
        Long categoryId = 63L;
        Mockito
                .when(restTemplate.getForEntity(
                       communicationsService.getUri("pet_category_service")
                               +  "/current/pet/petID/" + categoryId, Long.class))
                .thenReturn(new ResponseEntity(categoryId, HttpStatus.OK));
    }

    //user-comment communication
    @Test
    void AddAndGetCommentsUsername() throws Exception {
        Long mainRoleId = 1L;

        String newComment = "{\n" +
                "    \"categoryID\": 1,\n" +
                "    \"title\": \"Pet care\",\n" +
                "    \"content\": \"How to take care of my pet?\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        //dodavanje novog komentara (poziva se user_service)
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comment/{mainRoleId}", mainRoleId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        //pregled svih komentara
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/comment")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder2)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("username"))
                .andExpect(jsonPath("$[1].username").value("username"));
    }

    //user-reply communication
    @Test
    void AddAndGetRepliesUsername() throws Exception {
        Long commentId = 1L;

        String newComment = "{\n" +
                "    \"content\": \"Feed this pet 2 times a day.\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        //dodavanje novog odgovora (poziva se user_service)
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/reply/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        //pregled svih odgovora (poziva se user_service)
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/reply")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder2)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("alakovic1"));
    }
}
