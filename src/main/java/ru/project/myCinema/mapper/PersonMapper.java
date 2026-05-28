package ru.project.myCinema.mapper;

import org.springframework.stereotype.Component;
import ru.project.myCinema.dto.PersonResponse;
import ru.project.myCinema.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Маппер пользователя
 */
@Component
public class PersonMapper {

    /**
     * Маппинг сущностей Person в PersonResponse
     */
    public List<PersonResponse> mapToPersonResponses(List<Person> persons) {
        List<PersonResponse> personResponses = new ArrayList<>();
        for (Person person : persons) {
            personResponses.add(mapToPersonResponse(person));
        }
        return personResponses;
    }

    /**
     * Маппинг сущности Person в PersonResponse
     */
    public PersonResponse mapToPersonResponse(Person person){
        return new PersonResponse(
                person.getId(),
                person.getLogin(),
                person.getName(),
                person.getSurname()
        );
    }
}
