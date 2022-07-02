package my.app.learningspringv1.person;

import lombok.AllArgsConstructor;
import my.app.learningspringv1.exception.ResourceNotFoundException;
import my.app.learningspringv1.response.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/person")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping("add")
    public ResponseEntity<Object> addPerson(@RequestBody Person person) {
        personService.addPerson(person);
        return ResponseHandler.generateResponse(
                "Saved data successfully",
                HttpStatus.OK,
                person
        );
    }

    @GetMapping("find")
    public ResponseEntity<Object> findPersonById(@RequestParam("id") Integer id) throws ResourceNotFoundException {
        Optional<Person> person = personService.findPersonById(id);
        return person.map(value -> ResponseHandler.generateResponse("Retrieved person of id " + id, HttpStatus.OK, person))
                  .orElseThrow(() -> new ResourceNotFoundException("Could not find person of id " + id));
    }

    @GetMapping("find/all")
    public List<Person> findAllPersons() {
        return personService.findAllPersons();
    }


    @PutMapping("update")
    public ResponseEntity<Object> updatePersonById(@RequestParam("id") Integer id, @RequestBody Person person) throws ResourceNotFoundException {
        if (personService.updatePerson(id, person)) {
            person.setId(id);
            return ResponseHandler.generateResponse("Updated person of id=" + id + " successfully", HttpStatus.OK, person);
        }

        throw new ResourceNotFoundException("No person of id " + id + " found to update.");
    }

    @DeleteMapping("delete")
    public ResponseEntity<Object> deletePersonById(@RequestParam("id") Integer id) throws ResourceNotFoundException {
        if (personService.deletePersonById(id))
            return ResponseHandler.generateResponse("Deleted person of id=" + id + " successfully", HttpStatus.OK, null);

        throw new ResourceNotFoundException("No person of id " + id + " found to delete.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleMissingRequestBody(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
