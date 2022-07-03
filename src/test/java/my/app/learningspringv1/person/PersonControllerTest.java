package my.app.learningspringv1.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.app.learningspringv1.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    private final int testId = 1;

    @Test
    void addPerson() throws Exception {
        Person person = new Person(testId, "Sean", 28, Gender.MALE);
        RequestBuilder request = post("/api/person/add")
                        .content(new ObjectMapper().writeValueAsString(person))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        String expected = "{\"data\":{\"id\":1,\"name\":\"Sean\",\"age\":28,\"gender\":\"MALE\"},\"message\":\"Saved data successfully\",\"status\":201}";

        mockMvc.perform(request)
                        .andExpect(status().isCreated())
                        .andExpect(result -> assertEquals(expected, result.getResponse().getContentAsString()));
    }

    @Test
    void addPerson_handleMissingRequestBody() throws Exception {
        RequestBuilder request = post("/api/person/add");

        String expected = "Required request body is missing: " +
                          "public org.springframework.http.ResponseEntity<java.lang.Object> " +
                          "my.app.learningspringv1.person.PersonController.addPerson(my.app.learningspringv1.person.Person)";

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(expected, result.getResponse().getContentAsString()));
    }

    @Test
    void findPersonById_OK() throws Exception {
        Person person = new Person(testId, "Sean", 28, Gender.MALE);

        Mockito.when(personService.findPersonById(testId)).thenReturn(Optional.of(person));

        String expected = "{\"data\":{\"id\":1,\"name\":\"Sean\",\"age\":28,\"gender\":\"MALE\"},\"message\":\"Retrieved person of id 1\",\"status\":200}";

        mockMvc.perform(get("/api/person/find?id=" + testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expected, result.getResponse().getContentAsString()));
    }

    @Test
    void findPersonById_NotFound() throws Exception {
        mockMvc.perform(get("/api/person/find?id=" + testId))
               .andExpect(status().isNotFound())
               .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
               .andExpect(result -> assertEquals("Could not find person of id " + testId,
                                                          result.getResolvedException().getMessage()));
    }

    @Test
    void findAllPersons() throws Exception {
        mockMvc.perform(get("/api/person/find/all"))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void updatePerson() throws Exception {
        Person person = new Person(testId, "Sean", 27, Gender.MALE);
        Person updatedPerson = new Person(testId, "Buddy", 7, Gender.MALE);

        Mockito.when(personService.findPersonById(testId)).thenReturn(Optional.of(person));
        Mockito.when(personService.addPerson(any(Person.class))).thenReturn(updatedPerson);
        // todo: this test does not work quite yet; having trouble with the mocking...

        RequestBuilder request = put("/api/person/update?id=" + testId)
                .content(new ObjectMapper().writeValueAsString(person))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Buddy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("MALE"));
    }

    @Test
    void updatePerson_ResourceNotFoundExceptionThrown() throws Throwable {
        RequestBuilder request = put("/api/person/update?id=" + testId)
                .content(new ObjectMapper().writeValueAsString(new Person()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
               .andExpect(status().isNotFound())
               .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
               .andExpect(result -> assertEquals("No person of id " + testId + " found to update.",
                                                          result.getResolvedException().getMessage()));
    }

    @Test
    void updatePerson_handleMissingRequestBody() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/api/person/update?id=" + testId);

        String returned = "Required request body is missing: " +
                "public java.lang.String my.app.learningspringv1.person.PersonController.updatePersonById" +
                "(java.lang.Integer,my.app.learningspringv1.person.Person)";

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(returned, result.getResponse().getContentAsString()));
    }

    @Test
    void deletePerson() {
        Person person = new Person(testId, "Sean", 27, Gender.MALE);

        // todo

    }

    @Test
    void deletePerson_ResourceNotFoundExceptionThrown() throws Exception {
        RequestBuilder request = delete("/api/person/delete?id=" + testId);

        mockMvc.perform(request)
               .andExpect(status().isNotFound())
               .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
               .andExpect(result -> assertEquals("No person of id " + testId + " found to delete.",
                                                          result.getResolvedException().getMessage()));
    }

}