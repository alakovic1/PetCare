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
public class PetTests {

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
    void GetAllPetsInJSON() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pets")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetPetByIDTest() throws Exception{

        Long id = 1L;

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pet/{id}", id)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetPetsInRaseTest() throws Exception{

        Long id = 1L;

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pets/inRase?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetPetsInCategoryTest() throws Exception{

        Long id = 1L;

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pets/inCategory?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetPetByNameTest() throws Exception{

        String petName = "Rex";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pet/byName?name={petName}", petName)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetPetByNameContainsTest() throws Exception{

        String substring = "ex";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pets/name/contains?substring={substring}", substring)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void GetPetByRaseContainsTest() throws Exception{

        String substring = "dog";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pets/rase/contains?substring={substring}", substring)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void DeletePetTest() throws Exception{

        Long petID = 4L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/pet?id={petID}", petID)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\n" +
                        "  \"success\": true,\n" +
                        "  \"message\": \"Pet successfully deleted!\",\n" +
                        "  \"status\": \"OK\"\n" +
                        "}\n"));
    }

    //NOT FOUND tests

    @Test
    void GetPetByIDNotFound() throws Exception{

        Long id = 50L;

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pet/{id}", id)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Exception for NOT_FOUND was thrown\",\"status\":\"NOT_FOUND\"},\"details\":[\"No pet with ID 50\"]}"));
    }

    @Test
    void GetPetsInRaseNotFound() throws Exception{

        Long id = 50L;

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pets/inRase?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void GetPetsInCategoryNotFound() throws Exception{

        Long id = 50L;

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pets/inCategory?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void GetPetByNameNotFound() throws Exception{

        String name = "behwbf";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pet/byName?name={name}", name)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Exception for NOT_FOUND was thrown\",\"status\":\"NOT_FOUND\"},\"details\":[\"No pet with name behwbf\"]}"));
    }

    @Test
    void GetPetByNameContainsNotFound() throws Exception{

        String substring = "bebf";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pets/name/contains?substring={substring}", substring)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void GetPetByRaseContainsNotFound() throws Exception{

        String substring = "bebf";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pets/rase/contains?substring={substring}", substring)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void DeletePetNotFound() throws Exception{

        Long id = 50L;

        String token = "Bearer " + getToken();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/pet/delete?id={id}", id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"responseMessage\":{\"success\":false,\"message\":\"Exception for NOT_FOUND was thrown\",\"status\":\"NOT_FOUND\"},\"details\":[\"Pet not found!\"]}"));
    }
}
