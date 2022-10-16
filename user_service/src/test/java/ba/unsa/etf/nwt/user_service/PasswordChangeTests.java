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
public class PasswordChangeTests {

    @Autowired
    private MockMvc mockMvc;

    private void addUser() throws Exception{
        Long questionId = 1L;

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odg\"\n" +
                "  },\n" +
                "  \"email\": \"newEMail@etf.unsa.ba\",\n" +
                "  \"name\": \"FirstName\",\n" +
                "  \"password\": \"Password1234!!\",\n" +
                "  \"surname\": \"LastName\",\n" +
                "  \"username\": \"newUsername\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/register/{questionId}", questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful());
    }

    private String getToken() throws Exception {
        String input = "{\n" +
                "  \"password\": \"Password1234!!\",\n" +
                "  \"usernameOrEmail\": \"newUsername\"\n" +
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
    void GetSecurityQuestionForChangePasswordSuccess() throws Exception{

        String input = "{\n" +
                "  \"email\": \"newEMail@etf.unsa.ba\"\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/change/securityquestion")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": true,\n" +
                        "    \"status\": \"OK\",\n" +
                        "    \"message\": \"Valid email, question found.\"\n" +
                        "  },\n" +
                        "  \"question\": {\n" +
                        "    \"id\": 1,\n" +
                        "    \"title\": \"What is the name of the town where you were born?\",\n" +
                        "    \"description\": \"Home town\"\n" +
                        "  }\n" +
                        "}"));
    }

    @Test
    void GetSecurityQuestionForChangePasswordEmailNotValid() throws Exception{

        String input = "{\n" +
                "  \"email\": \"alakovic1\"\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/change/securityquestion")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
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
    void GetSecurityQuestionForChangePasswordUserNotFound() throws Exception{

        String input = "{\n" +
                "  \"email\": \"alakovic@etf.unsa.ba\"\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/change/securityquestion")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
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
    void AnswerQuestionCorrectly() throws Exception{

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odg\"\n" +
                "  },\n" +
                "  \"email\": \"newEMail@etf.unsa.ba\"\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/change/answerQuestion")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"success\": true,\n" +
                        "  \"status\": \"OK\",\n" +
                        "  \"message\": \"You have successfully answered the question.\"\n" +
                        "}"));
    }

    @Test
    void AnswerQuestionNotCorrectly() throws Exception{

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odgovor\"\n" +
                "  },\n" +
                "  \"email\": \"newEMail@etf.unsa.ba\"\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/change/answerQuestion")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": false,\n" +
                        "    \"status\": \"BAD_REQUEST\",\n" +
                        "    \"message\": \"Exception for wrong input was thrown\"\n" +
                        "  },\n" +
                        "  \"details\": [\n" +
                        "    \"Wrong answer!\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void AnswerQuestionEmailNotValid() throws Exception{

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odgovor\"\n" +
                "  },\n" +
                "  \"email\": \"alakovic1\"\n" +
                "}";

        addUser();
        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/change/answerQuestion")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
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
    void AnswerQuestionUserNotFound() throws Exception{

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odgovor\"\n" +
                "  },\n" +
                "  \"email\": \"email3333@etf.unsa.ba\"\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/change/answerQuestion")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
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
    void CreateNewPasswordSuccess() throws Exception{

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odg\"\n" +
                "  },\n" +
                "  \"email\": \"newEMail@etf.unsa.ba\",\n" +
                "  \"newPassword\": \"newPass111&\",\n" +
                "  \"oldPassword\": \"Password1234!!\"\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/change/newPassword")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"success\": true,\n" +
                        "  \"status\": \"OK\",\n" +
                        "  \"message\": \"You have successfully changed your password.\"\n" +
                        "}"));
    }

    @Test
    void CreateNewPasswordWrongAnswer() throws Exception{

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odgovor\"\n" +
                "  },\n" +
                "  \"email\": \"newEMail@etf.unsa.ba\",\n" +
                "  \"newPassword\": \"newPass1?\",\n" +
                "  \"oldPassword\": \"Password1234!!\"\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/change/newPassword")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": false,\n" +
                        "    \"status\": \"BAD_REQUEST\",\n" +
                        "    \"message\": \"Exception for wrong input was thrown\"\n" +
                        "  },\n" +
                        "  \"details\": [\n" +
                        "    \"Wrong answer!\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void CreateNewPasswordEmailNotValid() throws Exception{

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odgovor\"\n" +
                "  },\n" +
                "  \"email\": \"alakovic1\",\n" +
                "  \"newPassword\": \"newPass1\",\n" +
                "  \"oldPassword\": \"password1\"\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/change/newPassword")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
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
                        "    \"Password not valid (at least 6 characters, 1 big letter, 1 small letter, 1 sign)\",\n" +
                        "    \"Email should be valid\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void CreateNewPasswordUserNotFound() throws Exception{

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odgovor\"\n" +
                "  },\n" +
                "  \"email\": \"email333@etf.unsa.ba\",\n" +
                "  \"newPassword\": \"newPass1?\",\n" +
                "  \"oldPassword\": \"password1\"\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/change/newPassword")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
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
    void CreateNewPasswordPaswordNotValid() throws Exception{

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odg\"\n" +
                "  },\n" +
                "  \"email\": \"newEMail@etf.unsa.ba\",\n" +
                "  \"newPassword\": \"\",\n" +
                "  \"oldPassword\": \"password1\"\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/change/newPassword")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
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
                        "    \"Password can't be blank\",\n" +
                        "    \"Password not valid (at least 6 characters, 1 big letter, 1 small letter, 1 sign)\",\n" +
                        "    \"Passwords min length is 6, max length is 40\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void CreateNewPasswordOldPasswordNotAMatch() throws Exception{

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odg\"\n" +
                "  },\n" +
                "  \"email\": \"newEMail@etf.unsa.ba\",\n" +
                "  \"newPassword\": \"Pass123?\",\n" +
                "  \"oldPassword\": \"password1\"\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/change/newPassword")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": false,\n" +
                        "    \"status\": \"BAD_REQUEST\",\n" +
                        "    \"message\": \"Exception for wrong input was thrown\"\n" +
                        "  },\n" +
                        "  \"details\": [\n" +
                        "    \"Old password is not a match!\"\n" +
                        "  ]\n" +
                        "}"));
    }
}
