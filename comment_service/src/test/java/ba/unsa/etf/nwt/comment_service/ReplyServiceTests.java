package ba.unsa.etf.nwt.comment_service;

import ba.unsa.etf.nwt.comment_service.service.CommunicationsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class ReplyServiceTests {
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

    @Test
    void GetAllRepliesInJSON() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/reply")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void CreateNewReply() throws Exception {
        Long commentId = 1L;

        String newReply = "{\n" +
                "    \"content\": \"Pet care\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/reply/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newReply);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "    \"success\": true,\n" +
                        "    \"message\": \"Reply added successfully!!\",\n" +
                        "    \"status\": \"OK\"\n" +
                        "}\n"));
    }


    @Test
    void CreateNewReplySmallContent() throws Exception {
        Long commentId = 1L;

        String newReply = "{\n" +
                "    \"content\": \"P\"\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/reply/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newReply);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Validation Failed\"},\"details\":[\"Content must be between 2 and 1000 characters!!\"]}"));
    }

    @Test
    void CreateNewReplyBlankContent() throws Exception {
        Long commentId = 1L;

        String newReply = "{\n" +
                "    \"content\": \"\"\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/reply/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newReply);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Validation Failed\"},\"details\":[\"Content must be between 2 and 1000 characters!!\"]}\n"));
    }

    @Test
    void GetAllCommentRepliesInJSON() throws Exception {
        Long commentID = 1L;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/reply/comment/{commentID}", commentID)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void UpdateReply() throws Exception {
        Long replyId = 1L;
        String newReply = "{\n" +
                "    \"content\": \"con\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/reply/{replyId}", replyId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newReply);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void UpdateReplySmallContent() throws Exception {
        Long replyId = 1L;
        String newReply = "{\n" +
                "    \"content\": \"c\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/reply/{replyId}", replyId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newReply);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void UpdateReplyBlankContent() throws Exception {
        Long replyId = 1L;
        String newReply = "{\n" +
                "    \"content\": \"\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/reply/{replyId}", replyId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newReply);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
