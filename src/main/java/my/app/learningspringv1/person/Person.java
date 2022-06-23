package my.app.learningspringv1.person;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Person {

    @Id
    @GeneratedValue(generator = "person_sequence",
                   strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "person_sequence",
                        sequenceName = "person_sequence",
                        allocationSize = 1)
    private Integer id;
    private String name;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public Person(String name, Integer age, Gender gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
}
