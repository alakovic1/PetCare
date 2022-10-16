package ba.unsa.etf.nwt.pet_category_service;

import ba.unsa.etf.nwt.pet_category_service.service.CommunicationsService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
public class CategoryTests {

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
    void AddCategoryTest() throws Exception{

        String novaCat = "{\"name\": \"prvaCat\",\"description\": \"string\"}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/category")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaCat);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"success\": true, \"message\": \"Category added successfully!\",\"status\": \"OK\"}"));
    }

    @Test
    void GetAllCategoriesInJSON() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/categories")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetCategoryByIDTest() throws Exception{

        Long id = 1L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/category/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetCategoryByNameTest() throws Exception{

        String categoryName = "Cat";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/category/byName?name={categoryName}", categoryName)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\":\"Cat\",\"description\":\"Cats, also called domestic cats (Felis catus), are small, carnivorous (meat-eating) mammals, of the family Felidae.\"}"));
    }

    @Test
    void UpdateCategoryTest() throws Exception{

        Long id = 3L;

        String novaCat = "{\"name\": \"novaCat\",\"description\": \"string\"}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/category/update/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaCat);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\": \"novaCat\",\"description\": \"string\"}"));
    }

    @Test
    void DeleteCategoryTest() throws Exception{

        Long categoryID = 4L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/category?id={categoryID}", categoryID)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"success\": true, \"message\": \"Category successfully deleted!\",\"status\": \"OK\"}"));
    }

    //NOT FOUND test
    @Test
    void GetCategoryByIDNotFound() throws Exception{

        Long id = 50L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/category/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": false,\n" +
                        "    \"message\": \"Exception for NOT_FOUND was thrown\",\n" +
                        "    \"status\": \"NOT_FOUND\"\n" +
                        "  },\n" +
                        "  \"details\": [\n" +
                        "    \"No category with ID 50\"\n" +
                        "  ]\n" +
                        "}\n"));
    }

    @Test
    void GetCategoryByNameNotFound() throws Exception{

        String name = "abcd";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/category/byName?name={name}", name)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": false,\n" +
                        "    \"message\": \"Exception for NOT_FOUND was thrown\",\n" +
                        "    \"status\": \"NOT_FOUND\"\n" +
                        "  },\n" +
                        "  \"details\": [\n" +
                        "    \"No category with name abcd\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void DeleteCategoryNotFound() throws Exception{

        Long id = 50L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/category?id={id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": false,\n" +
                        "    \"message\": \"Exception for NOT_FOUND was thrown\",\n" +
                        "    \"status\": \"NOT_FOUND\"\n" +
                        "  },\n" +
                        "  \"details\": [\n" +
                        "    \"No category with ID 50\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void UpdateCategoryNotFound() throws Exception{

        Long id = 50L;

        String novaCat = "{\"name\": \"novaCat\",\"description\": \"string\"}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/category/update/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaCat);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"responseMessage\": {\n" +
                        "    \"success\": false,\n" +
                        "    \"message\": \"Exception for NOT_FOUND was thrown\",\n" +
                        "    \"status\": \"NOT_FOUND\"\n" +
                        "  },\n" +
                        "  \"details\": [\n" +
                        "    \"No category with ID 50\"\n" +
                        "  ]\n" +
                        "}"));
    }

    //BAD REQUEST tests
    @Test
    void UpdateCategoryBadRequest2() throws Exception{

        Long id = 1L;

        String novaCat = "{\"name\": \"g\",\"description\": \"fhghjhn\"}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/category/update/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaCat);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Validation Failed\",\"status\":\"BAD_REQUEST\"},\"details\":[\"Category name must be between 2 and 50 characters!\"]}"));
    }

    @Test
    void UpdateCategoryBadRequest3() throws Exception{

        Long id = 1L;

        String novaCat = "{\"name\": \"gghj\",\"description\": \"\"}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/category/update/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaCat);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void UpdateCategoryBadRequest4() throws Exception{

        Long id = 2L;

        String novaCat = "{\"name\": \"Bird\",\"description\": \"dfhj\"}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/category/update/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaCat);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Exception for wrong input was thrown\",\"status\":\"BAD_REQUEST\"},\"details\":[\"Category with that name already exists!\"]}"));
    }

    @Test
    void AddCategoryBadRequest1() throws Exception{

        String novaCat = "{\"name\": \"\",\"description\": \"\"}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaCat);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void AddCategoryBadRequest3() throws Exception{

        String novaCat = "{\"name\": \"\",\"description\": \"string\"}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaCat);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Validation Failed\",\"status\":\"BAD_REQUEST\"},\"details\":[\"Category name must be between 2 and 50 characters!\",\"Category name can't be blank!\"]}"));
    }

    @Test
    void AddCategoryBadRequest4() throws Exception{

        String novaCat = "{\"name\": \"Dog\",\"description\": \"string\"}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/category")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaCat);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

}
