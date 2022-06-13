package my.app.learningspringv1.person;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}
