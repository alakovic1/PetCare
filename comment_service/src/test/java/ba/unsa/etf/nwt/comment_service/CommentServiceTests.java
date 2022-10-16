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
class CommentServiceTests {
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
    void CreateNewComment() throws Exception {
        Long mainRoleId = 1L;

        String newComment = "{\n" +
                "    \"categoryID\": 1,\n" +
                "    \"title\": \"Pet care\",\n" +
                "    \"content\": \"How to take care of my pet?\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comment/{mainRoleId}", mainRoleId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void CreateNewCommentSmallTitle() throws Exception {
        Long mainRoleId = 1L;

        String newComment = "{\n" +
                "    \"title\": \"P\",\n" +
                "    \"content\": \"How to take care of my pet?\"\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comment/{mainRoleId}", mainRoleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\": {\n" +
                        "        \"success\": false,\n" +
                        "        \"status\": \"BAD_REQUEST\",\n" +
                        "        \"message\": \"Validation Failed\"\n" +
                        "    },\n" +
                        "    \"details\": [\n" +
                        "        \"Title must be between 2 and 100 characters!!\"\n" +
                        "    ]\n" +
                        "}\n}\n"));
    }

    @Test
    void CreateNewCommentSmallContent() throws Exception {
        Long mainRoleId = 1L;

        String newComment = "{\n" +
                "    \"title\": \"Pet care\",\n" +
                "    \"content\": \"H\"\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comment/{mainRoleId}", mainRoleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Validation Failed\"},\"details\":[\"Content must be between 2 and 1000 characters!!\"]}"));
    }

    @Test
    void CreateNewCommentSmallContentSmallTitle() throws Exception {
        Long mainRoleId = 1L;

        String newComment = "{\n" +
                "    \"userID\": 1,\n" +
                "    \"title\": \"P\",\n" +
                "    \"content\": \"H\"\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comment/{mainRoleId}", mainRoleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Validation Failed\"},\"details\":[\"Content must be between 2 and 1000 characters!!\",\"Title must be between 2 and 100 characters!!\"]}"));
    }

    @Test
    void CreateNewCommentNoUsernameSmallContentAndTitle() throws Exception {
        Long mainRoleId = 1L;

        String newComment = "{\n" +
                "    \"title\": \"P\",\n" +
                "    \"content\": \"H\"\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comment/{mainRoleId}", mainRoleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Validation Failed\"},\"details\":[\"Title must be between 2 and 100 characters!!\",\"Content must be between 2 and 1000 characters!!\"]}"));
    }

    @Test
    void CreateNewCommentNoUsernameSmallContent() throws Exception {
        Long mainRoleId = 1L;
        String newComment = "{\n" +
                "    \"title\": \"Pet care\",\n" +
                "    \"content\": \"H\"\n" +
                "}\n";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comment/{mainRoleId}", mainRoleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Validation Failed\"},\"details\":[\"Content must be between 2 and 1000 characters!!\"]}"));
    }

    @Test
    void CreateNewCommentNoUsernameSmallTitle() throws Exception {
        Long mainRoleId = 1L;

        String newComment = "{\n" +
                "    \"title\": \"P\",\n" +
                "    \"content\": \"Content\"\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comment/{mainRoleId}", mainRoleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Validation Failed\"},\"details\":[\"Title must be between 2 and 100 characters!!\"]}"));
    }

    @Test
    void CreateNewCommentNoUsername() throws Exception {
        Long mainRoleId = 1L;

        String newComment = "{\n" +
                "    \"categoryID\": 1,\n" +
                "    \"title\": \"Pet care\",\n" +
                "    \"content\": \"Content\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comment/{mainRoleId}", mainRoleId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"success\":true,\"status\":\"OK\",\"message\":\"Comment added successfully!!\"}"));
    }

    @Test
    void CreateNewCommentNoTitle() throws Exception {
        Long mainRoleId = 1L;

        String newComment = "{\n" +
                "    \"content\": \"How to take care of my pet?\"\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comment/{mainRoleId}", mainRoleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Validation Failed\"},\"details\":[\"Title can't be blank\"]}\n"));
    }

    @Test
    void CreateNewCommentNoTitleSmallContent() throws Exception {
        Long mainRoleId = 1L;

        String newComment = "{\n" +
                "    \"content\": \"H\"\n" +
                "}\n";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comment/{mainRoleId}", mainRoleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Validation Failed\"},\"details\":[\"Content must be between 2 and 1000 characters!!\",\"Title can't be blank\"]}\n"));
    }

    @Test
    void GetOneCommentInJSON() throws Exception {
        Long commentID = 1L;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/comment/{commentID}", commentID)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void UpdateComment() throws Exception {
        Long commentId = 1L;
        String newComment = "{\n" +
                "    \"title\": \"Test\",\n" +
                "    \"content\": \"Update\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/comment/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Exception for wrong input was thrown\"},\"details\":[\"This comment doesn't belong to current user!\"]}"));
    }

    @Test
    void UpdateCommentSmallTitle() throws Exception {
        Long commentId = 1L;
        String newComment = "{\n" +
                "    \"title\": \"T\",\n" +
                "    \"content\": \"Update\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/comment/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\": {\n" +
                        "        \"success\": false,\n" +
                        "        \"status\": \"BAD_REQUEST\",\n" +
                        "        \"message\": \"Validation Failed\"\n" +
                        "    },\n" +
                        "    \"details\": [\n" +
                        "        \"Title must be between 2 and 100 characters!!\"\n" +
                        "    ]\n" +
                        "}\n}\n"));
    }

    @Test
    void UpdateCommentSmallContent() throws Exception {
        Long commentId = 1L;
        String newComment = "{\n" +
                "    \"title\": \"Pet\",\n" +
                "    \"content\": \"U\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/comment/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Validation Failed\"},\"details\":[\"Content must be between 2 and 1000 characters!!\"]}"));
    }

    @Test
    void UpdateCommentSmallContentSmallTitle() throws Exception {
        Long commentId = 1L;
        String newComment = "{\n" +
                "    \"title\": \"P\",\n" +
                "    \"content\": \"U\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/comment/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Validation Failed\"},\"details\":[\"Content must be between 2 and 1000 characters!!\",\"Title must be between 2 and 100 characters!!\"]}"));
    }

    @Test
    void UpdateCommentSmallContentNoTitle() throws Exception {
        Long commentId = 1L;
        String newComment = "{\n" +
                "    \"content\": \"U\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/comment/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Validation Failed\"},\"details\":[\"Content must be between 2 and 1000 characters!!\",\"Title can't be blank\"]}"));
    }

    @Test
    void UpdateCommentNoTitle() throws Exception {
        Long commentId = 1L;
        String newComment = "{\n" +
                "    \"content\": \"Content\"\n" +
                "}\n";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/comment/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newComment);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"status\":\"BAD_REQUEST\",\"message\":\"Validation Failed\"},\"details\":[\"Title can't be blank\"]}"));
    }

    @Test
    void GetAllCategoryCommentsInJSON() throws Exception {
        Long roleType = 1L;
        Long categoryID = 1L;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/comment/category/{roleType}/{categoryID}", roleType, categoryID)
                .contentType(MediaType.APPLICATION_JSON);
    }
}
