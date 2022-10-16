package ba.unsa.etf.nwt.user_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class AuthTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void CheckRegistrationSuccess() throws Exception{
        Long questionId = 1L;

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odgovor\"\n" +
                "  },\n" +
                "  \"email\": \"email999999@etf.unsa.ba\",\n" +
                "  \"name\": \"Name\",\n" +
                "  \"password\": \"Password1234!\",\n" +
                "  \"surname\": \"Surname\",\n" +
                "  \"username\": \"Username\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/register/{questionId}", questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"success\": true,\n" +
                        "  \"status\": \"OK\",\n" +
                        "  \"message\": \"User registered successfully\"\n" +
                        "}"));
    }

    @Test
    void CheckRegistrationInputNotValid() throws Exception{
        Long questionId = 1L;

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odgovor\"\n" +
                "  },\n" +
                "  \"email\": \"email@etf.unsa.ba\",\n" +
                "  \"name\": \"\",\n" +
                "  \"password\": \"pass!\",\n" +
                "  \"surname\": \"\",\n" +
                "  \"username\": \"Username\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/register/{questionId}", questionId)
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
                        "    \"Name can't be blank\",\n" +
                        "    \"Surname can't be blank\",\n" +
                        "    \"Names min length is 3, max length is 50\",\n" +
                        "    \"Surnames min length is 3, max length is 50\",\n" +
                        "    \"Passwords min length is 6, max length is 40\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void CheckRegistrationEmailNotValid() throws Exception{
        Long questionId = 1L;

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odgovor\"\n" +
                "  },\n" +
                "  \"email\": \"email1\",\n" +
                "  \"name\": \"Name\",\n" +
                "  \"password\": \"pass1A!\",\n" +
                "  \"surname\": \"Surname\",\n" +
                "  \"username\": \"Username\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/register/{questionId}", questionId)
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
    void CheckRegistrationUsernameTaken() throws Exception{
        Long questionId = 1L;

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odgovor\"\n" +
                "  },\n" +
                "  \"email\": \"email@etf.unsa.ba\",\n" +
                "  \"name\": \"Name\",\n" +
                "  \"password\": \"pass1A!\",\n" +
                "  \"surname\": \"Surname\",\n" +
                "  \"username\": \"alakovic1\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/register/{questionId}", questionId)
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
                        "    \"Username is already taken!\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void CheckRegistrationEmailTaken() throws Exception{
        Long questionId = 1L;

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"odgovor\"\n" +
                "  },\n" +
                "  \"email\": \"alakovic1@etf.unsa.ba\",\n" +
                "  \"name\": \"Name\",\n" +
                "  \"password\": \"pass1A!\",\n" +
                "  \"surname\": \"Surname\",\n" +
                "  \"username\": \"usernameeeeeeee\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/register/{questionId}", questionId)
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
                        "    \"Email Address already in use!\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void CheckRegistrationAnswerNotValid() throws Exception{
        Long questionId = 1L;

        String input = "{\n" +
                "  \"answer\": {\n" +
                "    \"text\": \"\"\n" +
                "  },\n" +
                "  \"email\": \"emaill@etf.unsa.ba\",\n" +
                "  \"name\": \"Name\",\n" +
                "  \"password\": \"pass1A!\",\n" +
                "  \"surname\": \"Surname\",\n" +
                "  \"username\": \"usernameyyyy\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/register/{questionId}", questionId)
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
                        "    \"Answer text must not be empty!\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void CheckLoginInvalidInput() throws Exception{
        String input = "{\n" +
                "  \"password\": \"\",\n" +
                "  \"usernameOrEmail\": \"\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/login")
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
                        "    \"Username/Email can't be blank\",\n" +
                        "    \"Password can't be blank\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void CheckLoginWithUsernameWrongPassword() throws Exception{
        String input = "{\n" +
                "  \"password\": \"pass\",\n" +
                "  \"usernameOrEmail\": \"epita1\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/login")
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
                        "    \"Wrong username/email or password!\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void CheckLoginWithEmailWrongPassword() throws Exception{
        String input = "{\n" +
                "  \"password\": \"pass\",\n" +
                "  \"usernameOrEmail\": \"epita1@etf.unsa.ba\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/login")
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
                        "    \"Wrong username/email or password!\"\n" +
                        "  ]\n" +
                        "}"));
    }
}
