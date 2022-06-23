package my.app.learningspringv1.person;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public void addPerson(Person person) {
        personRepository.save(person);
    }

    public boolean updatePerson(int id, Person newPerson) {
        if (findPersonById(id).isPresent()) {
            personRepository.save(
                new Person(
                    id,
                    newPerson.getName(),
                    newPerson.getAge(),
                    newPerson.getGender()
                )
            );
            return true;
        }
        return false;
    }

    public Optional<Person> findPersonById(Integer id) {
        return personRepository.findById(id);
    }

    public List<Person> findAllPersons() {
        return personRepository.findAll();
    }

    public boolean deletePersonById(Integer id) {
        if (findPersonById(id).isPresent()) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
