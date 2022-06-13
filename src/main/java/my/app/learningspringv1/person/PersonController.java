package my.app.learningspringv1.person;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/person")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping("add")
    public String addPerson(@RequestBody Person person) {
        personService.addPerson(person);
        return "Saved " + person.getName();
    }

    @GetMapping("find")
    public ResponseEntity<Person> findPersonById(@RequestParam("id") Integer id) {
        Optional<Person> person = personService.findPersonById(id);
        return person.map(value -> ResponseEntity.ok().body(value))
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("find/all")
    public List<Person> findAllPersons() {
        return personService.findAllPersons();
    }

    @PutMapping("update")
    public String updatePersonById(@RequestParam("id") Integer id, @RequestBody Person person) {
        if (personService.updatePerson(id, person))
            return "Updated person of id=" + id + " successfully";

//        return "No person of id=" + id + " found to update"; // testing purposes
        throw new NoSuchElementException("No person of id=" + id + " found to update");
    }

    @DeleteMapping("delete")
    public String deletePersonById(@RequestParam("id") Integer id) {
        if (personService.deletePersonById(id))
            return "Deleted person of id=" + id + " successfully";

//        return "No person of id=" + id + " found to delete"; // testing purposes
        throw new NoSuchElementException("No person of id=" + id + " found to delete");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleMissingRequestBody(HttpMessageNotReadableException ex) {
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
