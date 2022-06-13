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
        Optional<Person> oldPerson = findPersonById(id);
        if (oldPerson.isPresent()) {
            personRepository.save(
                    new Person(
                        id,
                        (newPerson.getName() != null) ? newPerson.getName() : oldPerson.get().getName(),
                        (newPerson.getAge() != null) ? newPerson.getAge() : oldPerson.get().getAge(),
                        (newPerson.getGender() != null) ? newPerson.getGender() : oldPerson.get().getGender()
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
