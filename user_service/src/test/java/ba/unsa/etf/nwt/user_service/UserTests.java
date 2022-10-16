package ba.unsa.etf.nwt.user_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class UserTests {

    @Autowired
    private MockMvc mockMvc;

    private void addUser() throws Exception{
        Long questionId = 1L;

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odgg\"\n" +
                "  },\n" +
                "  \"email\": \"newmail@etf.unsa.ba\",\n" +
                "  \"name\": \"nameee\",\n" +
                "  \"password\": \"Password12345!!\",\n" +
                "  \"surname\": \"surnameee\",\n" +
                "  \"username\": \"usernameee\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/register/{questionId}", questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful());
    }

    private String getUserToken() throws Exception {
        String input = "{\n" +
                "  \"password\": \"Password12345!!\",\n" +
                "  \"usernameOrEmail\": \"usernameee\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
        ResultActions result  = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("accessToken").toString();
    }

    private String getUserChangedToken() throws Exception {
        String input = "{\n" +
                "  \"password\": \"Password12345!!\",\n" +
                "  \"usernameOrEmail\": \"newusername\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
        ResultActions result  = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("accessToken").toString();
    }

    private String getToken() throws Exception {
        String input = "{\n" +
                "  \"password\": \"Password123!\",\n" +
                "  \"usernameOrEmail\": \"alakovic1\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
        ResultActions result  = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("accessToken").toString();
    }

    @Test
    void GetAllUsersInJSON() throws Exception{

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetUserWithUsernameSuccess() throws Exception{

        String username = "alakovic1";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/{username}", username)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"name\": \"Amila\",\n" +
                        "  \"surname\": \"Lakovic\",\n" +
                        "  \"username\": \"alakovic1\",\n" +
                        "  \"email\": \"alakovic1@etf.unsa.ba\"\n" +
                        "}"));
    }

    @Test
    void GetUserWithUsernameNotFound() throws Exception{

        String username = "usernameeee";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/{username}", username)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": false,\n" +
                        "    \"status\": \"NOT_FOUND\",\n" +
                        "    \"message\": \"Exception for NOT_FOUND was thrown\"\n" +
                        "  },\n" +
                        "  \"details\": [\n" +
                        "    \"User not found!\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void CheckUsernameAvailable() throws Exception{

        String username = "username";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/usernameCheck/")
                .header(HttpHeaders.AUTHORIZATION, token)
                .param("username", username)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void CheckEmailAvailable() throws Exception{

        String email = "email@etf.unsa.ba";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/emailCheck/")
                .header(HttpHeaders.AUTHORIZATION, token)
                .param("email", email)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void UpdateUserProfileSuccess() throws Exception{

        String newUserInfo = "{\n" +
                "  \"email\": \"newmail@etf.unsa.ba\",\n" +
                "  \"name\": \"amilaa\",\n" +
                "  \"surname\": \"lakovic\",\n" +
                "  \"username\": \"newusername\"\n" +
                "}";

        String token = "Bearer " + getUserToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/update")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserInfo);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"success\": true,\n" +
                        "  \"status\": \"OK\",\n" +
                        "  \"message\": \"Profile successfully updated.\"\n" +
                        "}"));
    }

    @Test
    void UpdateUserProfileUserNotFound() throws Exception{

        String newUserInfo = "{\n" +
                "  \"email\": \"alakovic111@etf.unsa.ba\",\n" +
                "  \"name\": \"amilaaa\",\n" +
                "  \"surname\": \"lakovic\",\n" +
                "  \"username\": \"newusername\"\n" +
                "}";

        String token = "Bearer " + getUserToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/update")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserInfo);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": false,\n" +
                        "    \"status\": \"BAD_REQUEST\",\n" +
                        "    \"message\": \"Exception for wrong input was thrown\"\n" +
                        "  },\n" +
                        "  \"details\": [\n" +
                        "    \"Email not the same as current users!\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void UpdateUserProfileEmailNotValid() throws Exception{

        String newUserInfo = "{\n" +
                "  \"email\": \"alakovic1\",\n" +
                "  \"name\": \"amilaaa\",\n" +
                "  \"surname\": \"lakovic\",\n" +
                "  \"username\": \"newusername\"\n" +
                "}";

        addUser();
        String token = "Bearer " + getUserToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/update")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserInfo);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": false,\n" +
                        "    \"status\": \"BAD_REQUEST\",\n" +
                        "    \"message\": \"Validation Failed\"\n" +
                        "  },\n" +
                        "  \"details\": [\n" +
                        "    \"Email should be valid\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void UpdateUserProfileUsernameNotValid() throws Exception{

        String newUserInfo = "{\n" +
                "  \"email\": \"newmail@etf.unsa.ba\",\n" +
                "  \"name\": \"amilaaa\",\n" +
                "  \"surname\": \"lakovic\",\n" +
                "  \"username\": \"\"\n" +
                "}";

        String token = "Bearer " + getUserToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/update")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserInfo);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": false,\n" +
                        "    \"status\": \"BAD_REQUEST\",\n" +
                        "    \"message\": \"Validation Failed\"\n" +
                        "  },\n" +
                        "  \"details\": [\n" +
                        "    \"Username can't be blank\",\n" +
                        "    \"Usernames min length is 4, max length is 40\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void UpdateUserProfileSurnameNotValid() throws Exception{

        String newUserInfo = "{\n" +
                "  \"email\": \"alakovic1@etf.unsa.ba\",\n" +
                "  \"name\": \"amilaaa\",\n" +
                "  \"surname\": \"\",\n" +
                "  \"username\": \"username\"\n" +
                "}";

        String token = "Bearer " + getUserChangedToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/update")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserInfo);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": false,\n" +
                        "    \"status\": \"BAD_REQUEST\",\n" +
                        "    \"message\": \"Validation Failed\"\n" +
                        "  },\n" +
                        "  \"details\": [\n" +
                        "    \"Surname can't be blank\",\n" +
                        "    \"Surnames min length is 3, max length is 50\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void UpdateUserProfileNameNotValid() throws Exception{

        String newUserInfo = "{\n" +
                "  \"email\": \"alakovic1@etf.unsa.ba\",\n" +
                "  \"name\": \"\",\n" +
                "  \"surname\": \"lakovic\",\n" +
                "  \"username\": \"username\"\n" +
                "}";

        String token = "Bearer " + getUserChangedToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/update")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserInfo);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": false,\n" +
                        "    \"status\": \"BAD_REQUEST\",\n" +
                        "    \"message\": \"Validation Failed\"\n" +
                        "  },\n" +
                        "  \"details\": [\n" +
                        "    \"Names min length is 3, max length is 50\",\n" +
                        "    \"Name can't be blank\"\n" +
                        "  ]\n" +
                        "}"));
    }

}
