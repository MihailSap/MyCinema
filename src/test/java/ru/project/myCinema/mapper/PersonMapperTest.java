package ru.project.myCinema.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.project.myCinema.dto.person.PersonResponse;
import ru.project.myCinema.model.Person;
import ru.project.myCinema.model.PersonAccountStatus;
import ru.project.myCinema.model.Role;

import java.util.Collections;
import java.util.List;

/**
 * Тесты для маппера пользователей
 */
class PersonMapperTest {

    private PersonMapper personMapper;
    private Person person;

    @BeforeEach
    void setUp() {
        personMapper = new PersonMapper();

        person = new Person();
        person.setId(1L);
        person.setLogin("user");
        person.setName("Иван");
        person.setSurname("Иванов");
        person.setBalance(1000.0);
        person.setAccountStatus(PersonAccountStatus.ACTIVE);
        person.setRole(Role.USER);
    }

    /**
     * Проверяет маппинг одной сущности Person в PersonResponse
     */
    @Test
    void testMapToPersonResponse() {
        PersonResponse personResponse = personMapper.mapToPersonResponse(person);

        Assertions.assertNotNull(personResponse);
        Assertions.assertEquals(person.getId(), personResponse.id());
        Assertions.assertEquals(person.getLogin(), personResponse.login());
        Assertions.assertEquals(person.getName(), personResponse.name());
        Assertions.assertEquals(person.getSurname(), personResponse.surname());
        Assertions.assertEquals(person.getBalance(), personResponse.balance());
        Assertions.assertEquals(person.getAccountStatus(), personResponse.accountStatus());
        Assertions.assertEquals(person.getRole(), personResponse.role());
    }

    /**
     * Проверяет маппинг списка пользователей
     */
    @Test
    void testMapToPersonResponses() {
        Person secondPerson = new Person();
        secondPerson.setId(2L);
        secondPerson.setLogin("admin");
        secondPerson.setName("Пётр");
        secondPerson.setSurname("Петров");
        secondPerson.setBalance(5000.0);
        secondPerson.setAccountStatus(PersonAccountStatus.BLOCKED);
        secondPerson.setRole(Role.ADMIN);

        List<PersonResponse> responses = personMapper.mapToPersonResponses(List.of(person, secondPerson));

        Assertions.assertEquals(2, responses.size());

        PersonResponse firstPersonResponse = responses.getFirst();
        Assertions.assertNotNull(firstPersonResponse);
        Assertions.assertEquals(1L, firstPersonResponse.id());
        Assertions.assertEquals("user", firstPersonResponse.login());
        Assertions.assertEquals("Иван", firstPersonResponse.name());
        Assertions.assertEquals("Иванов", firstPersonResponse.surname());
        Assertions.assertEquals(1000.0, firstPersonResponse.balance());
        Assertions.assertEquals(PersonAccountStatus.ACTIVE, firstPersonResponse.accountStatus());
        Assertions.assertEquals(Role.USER, firstPersonResponse.role());

        PersonResponse secondPersonResponse = responses.get(1);
        Assertions.assertNotNull(secondPersonResponse);
        Assertions.assertEquals(2L, secondPersonResponse.id());
        Assertions.assertEquals("admin", secondPersonResponse.login());
        Assertions.assertEquals("Пётр", secondPersonResponse.name());
        Assertions.assertEquals("Петров", secondPersonResponse.surname());
        Assertions.assertEquals(5000.0, secondPersonResponse.balance());
        Assertions.assertEquals(PersonAccountStatus.BLOCKED, secondPersonResponse.accountStatus());
        Assertions.assertEquals(Role.ADMIN, secondPersonResponse.role());
    }

    /**
     * Проверяет маппинг пустого списка
     */
    @Test
    void testMapToPersonResponsesEmptyList() {
        List<PersonResponse> responses = personMapper.mapToPersonResponses(Collections.emptyList());

        Assertions.assertNotNull(responses);
        Assertions.assertTrue(responses.isEmpty());
    }
}