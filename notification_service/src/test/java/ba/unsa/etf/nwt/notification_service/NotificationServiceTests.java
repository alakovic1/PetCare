package ba.unsa.etf.nwt.notification_service;

import ba.unsa.etf.nwt.notification_service.service.CommunicationsService;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class NotificationServiceTests {
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

    public String getToken2(){
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String input = "{\n" +
                    "  \"password\": \"string123!A\",\n" +
                    "  \"usernameOrEmail\": \"username\"\n" +
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
    void GetAllUnreadNotificationsForUserAdmin() throws Exception {

        Long userId = 1L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notifications/all/unread/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    void GetAllUnreadNotificationsForUserNotSuccess1() throws Exception {

        Long userId = 1L;

        String token = "Bearer " + getToken2();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notifications/all/unread/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.details[0]").value("This notification doesn't belong to current user!"));

    }

    @Test
    void GetAllUnreadNotificationsForUserNotSuccess2() throws Exception {

        Long userId = 3L;

        String token = "Bearer " + getToken2();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notifications/all/unread/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.details[0]").value("This notification doesn't belong to current user!"));

    }

    @Test
    void GetAllUnreadNotificationsForUserSuccess() throws Exception {

        Long userId = 5L;

        String token = "Bearer " + getToken2();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notifications/all/unread/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    void GetAllNotificationsForUserAdmin() throws Exception {
        Long userId = 1L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notifications/all/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetAllNotificationsForUserNotSuccess1() throws Exception {

        Long userId = 1L;

        String token = "Bearer " + getToken2();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notifications/all/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.details[0]").value("This notification doesn't belong to current user!"));

    }

    @Test
    void GetAllNotificationsForUserNotSuccess2() throws Exception {

        Long userId = 3L;

        String token = "Bearer " + getToken2();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notifications/all/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.details[0]").value("This notification doesn't belong to current user!"));

    }

    @Test
    void GetAllNotificationsForUserSuccess() throws Exception {

        Long userId = 5L;

        String token = "Bearer " + getToken2();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notifications/all/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void SetAllUnreadNotificationsToReadUserAdmin() throws Exception {
        Long userId = 1L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/notifications/setRead/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void SetAllUnreadNotificationsToReadUserSuccess() throws Exception {
        Long userId = 5L;

        String token = "Bearer " + getToken2();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/notifications/setRead/" + userId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void DeleteNotificationAdmin() throws Exception {
        Long userId = 1L;
        Long id = 1L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/notifications/delete/" + userId + "/" + id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void DeleteNotificationAdminNotSuccess() throws Exception {
        Long userId = 1L;
        Long id = 11L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/notifications/delete/" + userId + "/" + id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.details[0]").value("Notification not found!"));
    }

    @Test
    void DeleteNotificationUser() throws Exception {
        Long userId = 5L;
        Long id = 6L;

        String token = "Bearer " + getToken2();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/notifications/delete/" + userId + "/" + id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void DeleteNotificationUserNotSuccess() throws Exception {
        Long userId = 5L;
        Long id = 11L;

        String token = "Bearer " + getToken2();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/notifications/delete/" + userId + "/" + id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.details[0]").value("Notification not found!"));
    }

}
