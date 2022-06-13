package my.app.learningspringv1.person;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // tests use a test database
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;
    private final int testId = 1;

    @Test
    @Rollback(value = false)
    @Order(1)
    void testAddPerson() {
        Person person = personRepository.save(new Person("Sean", 28, Gender.MALE));

        assertTrue(person.getId() > 0);
    }

    @Test
    @Order(2)
    void testFindPersonById() {
        Person person = personRepository.findById(testId).get();

        assertEquals("Sean", person.getName());
    }

    @Test
    @Order(3)
    void testFindAllPersons() {
        List<Person> persons = personRepository.findAll();

        assertTrue(persons.size() > 0);
    }

    @Test
    @Order(4)
    void testUpdatePerson() {
        String newName = "Buddy";

        Person person = personRepository.findById(testId).get();
        person.setName(newName);

        personRepository.save(person);

        Person updatedPerson = personRepository.findById(testId).get();
        assertEquals(newName, updatedPerson.getName());
    }

    @Test
    @Rollback(value = false)
    @Order(5)
    void testDeletePerson() {
        personRepository.deleteById(testId);

        Optional<Person> deletedPerson = personRepository.findById(testId);
        assertFalse(deletedPerson.isPresent());
    }
}