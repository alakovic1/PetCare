package ba.unsa.etf.nwt.pet_category_service;

import ba.unsa.etf.nwt.pet_category_service.service.CommunicationsService;
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
public class RaseTests {

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
    void GetAllRasesInJSON() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rases")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetRasesInCategoryTest() throws Exception{

        Long id = 1L;

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rases/inCategory?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetRaseByIDTest() throws Exception{

        Long id = 1L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rase/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetRaseByNameTest() throws Exception{

        String raseName = "Goldfish";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rase/byName?name={raseName}", raseName)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void AddRaseTest() throws Exception{

        String novaRasa = "{\n" +
                "  \"name\": \"novaRasa\",\n" +
                "  \"description\": \"novaRasa\",\n" +
                "  \"category_id\": 1\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rase")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRasa);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"success\": true,\n" +
                        "  \"message\": \"Rase successfully added!!\",\n" +
                        "  \"status\": \"OK\"\n" +
                        "}"));
    }

    @Test
    void DeleteRaseTest() throws Exception{

        Long raseID = 3L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/rase?id={raseID}", raseID)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"success\": true,\n" +
                        "  \"message\": \"Rase successfully deleted\",\n" +
                        "  \"status\": \"OK\"\n" +
                        "}\n"));
    }


    @Test
    void UpdateRaseTest() throws Exception{

        Long id = 2L;

        String novaRase = "{\"name\": \"novaRasaa\",\"description\": \"nova\",\"category_id\": 1}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rase/update/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRase);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    //NOT FOUND tests

    @Test
    void GetRaseByIDNotFound() throws Exception{

        Long id = 50L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rase/{id}", id)
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
                        "    \"No rase with ID 50\"\n" +
                        "  ]\n" +
                        "}\n"));
    }

    @Test
    void GetRasesInCategoryNotFound() throws Exception{

        Long id = 50L;

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rases/inCategory?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void GetRaseByNameNotFound() throws Exception{

        String name = "abcd";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rase/byName?name={name}", name)
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
                        "    \"No rase with name abcd\"\n" +
                        "  ]\n" +
                        "}\n"));
    }

    @Test
    void DeleteRaseNotFound() throws Exception{

        Long id = 50L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/rase?id={id}", id)
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
                        "    \"No rase with ID 50\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void UpdateRaseNotFound1() throws Exception{

        Long id = 50L;

        String novaRase = "{\"name\": \"novaRasaa\",\"description\": \"nova\",\"category_id\": 1}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rase/update/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRase);
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
                        "    \"No rase with ID 50\"\n" +
                        "  ]\n" +
                        "}"));
    }

    @Test
    void UpdateRaseNotFound2() throws Exception{

        Long id = 1L;

        String novaRase = "{\"name\": \"gdfgd\",\"description\": \"nova\",\"category_id\": 50}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rase/update/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRase);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Exception for NOT_FOUND was thrown\",\"status\":\"NOT_FOUND\"},\"details\":[\"No category with ID 50\"]}"));
    }


    //BAD REQUEST tests

    @Test
    void AddRaseBadRequest1() throws Exception{

        String novaRasa = "{\n" +
                "  \"name\": \"\",\n" +
                "  \"description\": \"\",\n" +
                "  \"category_id\": 1\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRasa);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Validation Failed\",\"status\":\"BAD_REQUEST\"},\"details\":[\"Rase name must be between 2 and 50 characters!\",\"Rase name can't be blank!\",\"Rase description can't be blank!\"]}"));
    }

    @Test
    void AddRaseBadRequest2() throws Exception{

        String novaRasa = "{\n" +
                "  \"name\": \"g\",\n" +
                "  \"description\": \"\",\n" +
                "  \"category_id\": 1\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRasa);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Validation Failed\",\"status\":\"BAD_REQUEST\"},\"details\":[\"Rase name must be between 2 and 50 characters!\",\"Rase description can't be blank!\"]}"));
    }

    @Test
    void AddRaseBadRequest3() throws Exception{

        String novaRasa = "{\n" +
                "  \"name\": \"gfsdg\",\n" +
                "  \"description\": \"\",\n" +
                "  \"category_id\": 1\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRasa);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Validation Failed\",\"status\":\"BAD_REQUEST\"},\"details\":[\"Rase description can't be blank!\"]}"));
    }

    @Test
    void AddRaseBadRequest4() throws Exception{

        String novaRasa = "{\n" +
                "  \"name\": \"\",\n" +
                "  \"description\": \"gfjdg\",\n" +
                "  \"category_id\": 1\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRasa);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Validation Failed\",\"status\":\"BAD_REQUEST\"},\"details\":[\"Rase name can't be blank!\",\"Rase name must be between 2 and 50 characters!\"]}"));
    }

    @Test
    void AddRaseBadRequest5() throws Exception{

        String novaRasa = "{\n" +
                "  \"name\": \"Goldfish\",\n" +
                "  \"description\": \"gfjdg\",\n" +
                "  \"category_id\": 1\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rase")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRasa);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Exception for wrong input was thrown\",\"status\":\"BAD_REQUEST\"},\"details\":[\"Rase with that name already exists!\"]}"));
    }

    @Test
    void UpdateRaseBadRequest1() throws Exception{

        Long id = 5L;

        String novaRasa = "{\n" +
                "  \"name\": \"\",\n" +
                "  \"description\": \"\",\n" +
                "  \"category_id\": 1\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rase/update/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRasa);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Validation Failed\",\"status\":\"BAD_REQUEST\"},\"details\":[\"Rase name can't be blank!\",\"Rase description can't be blank!\",\"Rase name must be between 2 and 50 characters!\"]}"));
    }

    @Test
    void UpdateRaseBadRequest2() throws Exception{

        Long id = 5L;

        String novaRasa = "{\n" +
                "  \"name\": \"g\",\n" +
                "  \"description\": \"\",\n" +
                "  \"category_id\": 1\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rase/update/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRasa);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Validation Failed\",\"status\":\"BAD_REQUEST\"},\"details\":[\"Rase name must be between 2 and 50 characters!\",\"Rase description can't be blank!\"]}"));
    }

    @Test
    void UpdateRaseBadRequest3() throws Exception{

        Long id = 5L;

        String novaRasa = "{\n" +
                "  \"name\": \"gvgss\",\n" +
                "  \"description\": \"\",\n" +
                "  \"category_id\": 1\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rase/update/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRasa);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Validation Failed\",\"status\":\"BAD_REQUEST\"},\"details\":[\"Rase description can't be blank!\"]}"));
    }

    @Test
    void UpdateRaseBadRequest4() throws Exception{

        Long id = 5L;

        String novaRasa = "{\n" +
                "  \"name\": \"\",\n" +
                "  \"description\": \"fdsngj\",\n" +
                "  \"category_id\": 1\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rase/update/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRasa);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Validation Failed\",\"status\":\"BAD_REQUEST\"},\"details\":[\"Rase name must be between 2 and 50 characters!\",\"Rase name can't be blank!\"]}"));
    }

    @Test
    void UpdateRaseBadRequest5() throws Exception{

        Long id = 5L;

        String novaRasa = "{\n" +
                "  \"name\": \"\",\n" +
                "  \"description\": \"fdsngj\",\n" +
                "  \"category_id\": 1\n" +
                "}";

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rase/update/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaRasa);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Validation Failed\",\"status\":\"BAD_REQUEST\"},\"details\":[\"Rase name must be between 2 and 50 characters!\",\"Rase name can't be blank!\"]}"));
    }


}
