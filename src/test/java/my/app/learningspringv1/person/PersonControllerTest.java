package my.app.learningspringv1.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.util.NestedServletException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private PersonController personController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;
    private final int testId = 1;

    @Test
    void addPerson() throws Exception {
        Person person = new Person(1, "Sean", 27, Gender.MALE);
        RequestBuilder request = MockMvcRequestBuilders.post("/api/person/add")
                        .content(new ObjectMapper().writeValueAsString(person))
                        .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();
        assertEquals("Saved " + person.getName(), result.getResponse().getContentAsString());
    }

    @Test
    void findPersonById_OK() throws Exception {
        Person person = new Person(testId, "Sean", 27, Gender.MALE);

        Mockito.when(personService.findPersonById(testId)).thenReturn(Optional.of(person));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/person/find?id=" + testId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findPersonById_NotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/api/person/find?id=" + testId);
        MvcResult result = mockMvc.perform(request).andReturn();

        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    void findAllPersons() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/api/person/find/all");
        MvcResult result = mockMvc.perform(request).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    void updatePerson() throws Exception {
        Person person = new Person(testId, "Sean", 27, Gender.MALE);
        Mockito.when(personService.updatePerson(testId, person)).thenReturn(true);

        RequestBuilder request = MockMvcRequestBuilders.put("/api/person/update?id=" + testId)
                .content(new ObjectMapper().writeValueAsString(person))
                .contentType(MediaType.APPLICATION_JSON);

        String expected = "Updated person of id=" + testId + " successfully";

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expected, result.getResponse().getContentAsString()));
    }

    @Test
    void updatePerson_NoSuchElementExceptionThrown() throws Exception, Throwable {
        RequestBuilder request = MockMvcRequestBuilders.put("/api/person/update?id=" + testId)
                .content(new ObjectMapper().writeValueAsString(new Person()))
                .contentType(MediaType.APPLICATION_JSON);

        // todo: assert that an exception is thrown as that is the current behavior expected when trying to update with an id that does not exist

//        assertThrows(NoSuchElementException.class, () -> {
//            mockMvc.perform(request)
//                    .andExpect(status().isInternalServerError());
//        },
//                "Expected NoSuchElementException to be thrown");

//        try {
//            mockMvc.perform(request)
//                    .andExpect(status().isInternalServerError());
//        } catch (NestedServletException ex) {
//            throw ex.getCause();
//        }
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
        int personId = 1;
        Person person = new Person(personId, "Sean", 27, Gender.MALE);

        // todo
    }

    @Test
    void deletePerson_NoSuchElementExceptionThrown() {
        int testId = 1;
        RequestBuilder request = MockMvcRequestBuilders.delete("/api/person/delete?id=" + testId);

        // todo

    }
}