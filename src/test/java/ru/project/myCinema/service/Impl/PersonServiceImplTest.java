package ru.project.myCinema.service.Impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.project.myCinema.dto.person.AuthRequest;
import ru.project.myCinema.dto.person.UpdatePersonRequest;
import ru.project.myCinema.model.Person;
import ru.project.myCinema.model.PersonAccountStatus;
import ru.project.myCinema.model.Role;
import ru.project.myCinema.repository.PersonRepository;
import ru.project.myCinema.service.PersonService;

import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PersonServiceImplTest {

    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Проверка создания пользователя
     */
    @Test
    void testCreatePerson() {
        AuthRequest authRequest = new AuthRequest("user2005", "123456789");

        Person created = personService.create(authRequest);
        Assertions.assertNotNull(created);
        Assertions.assertEquals("user2005", created.getLogin());
        Assertions.assertTrue(passwordEncoder.matches("123456789", created.getPassword()));
        Assertions.assertEquals(Role.USER, created.getRole());
        Assertions.assertEquals(0.0, created.getBalance());
    }

    /**
     * Проверка получения всех пользователей
     */
    @Test
    void testGetAll() {
        personService.create(new AuthRequest("user2005", "123456789"));
        personService.create(new AuthRequest("user2006", "123456789"));

        List<Person> persons = personService.getAll();
        Assertions.assertNotNull(persons);
        Assertions.assertEquals(2, persons.size());
        Assertions.assertEquals("user2005", persons.getFirst().getLogin());
        Assertions.assertEquals("user2006", persons.get(1).getLogin());
    }

    /**
     * Проверка получения по id существующего пользователя
     */
    @Test
    void testGetByIdSuccess() {
        Person person = personService.create(new AuthRequest("user2005", "123456789"));
        Person found = personService.getById(person.getId());
        Assertions.assertEquals(person, found);
    }

    /**
     * Проверка получения по id несуществующего пользователя
     */
    @Test
    void testGetByIdFailure() {
        Assertions.assertThrows(RuntimeException.class, () -> personService.getById(999L));
    }

    /**
     * Проверка получения по логину существующего пользователя
     */
    @Test
    void testGetByLoginSuccess() {
        Person person = personService.create(new AuthRequest("user2005", "123456789"));
        Person foundPerson = personService.getByLogin("user2005");
        Assertions.assertEquals(person, foundPerson);
    }

    /**
     * Проверка получения по логину несуществующего пользователя
     */
    @Test
    void testGetByLoginFailure() {
        Assertions.assertThrows(RuntimeException.class, () -> personService.getByLogin("userUnknown"));
    }

    /**
     * Проверка проверки наличия пользователя в БД по логину
     */
    @Test
    void testIsExistsByLogin() {
        personService.create(new AuthRequest("user2005", "123456789"));

        Assertions.assertTrue(personService.isExistsByLogin("user2005"));
        Assertions.assertFalse(personService.isExistsByLogin("userUnknown"));
    }

    /**
     * Проверка обновления данных пользователя
     */
    @Test
    void testUpdatePerson() {
        Person person = personService.create(new AuthRequest("user2005", "123456789"));
        UpdatePersonRequest request = new UpdatePersonRequest("user2006", "name", "surname");
        Person updatedPerson = personService.update(request, person);

        Assertions.assertNotNull(updatedPerson);
        Assertions.assertEquals("user2006", updatedPerson.getLogin());
        Assertions.assertEquals("name", updatedPerson.getName());
        Assertions.assertEquals("surname", updatedPerson.getSurname());
    }

    /**
     * Проверка обновления пользователя с конфликтующим логином
     */
    @Test
    void testUpdateLoginConflict() {
        personService.create(new AuthRequest("user2005", "123456789"));
        Person conflictPerson = personService.create(new AuthRequest("user2006", "123456789"));
        UpdatePersonRequest request = new UpdatePersonRequest("user2005", null, null);

        Assertions.assertThrows(RuntimeException.class, () -> personService.update(request, conflictPerson));
    }

    /**
     * Проверка пополнения баланса пользователя
     */
    @Test
    void testTopUpBalance() {
        Person person = personService.create(new AuthRequest("user2005", "123456789"));
        Person updatedPerson = personService.topUpBalance(person, 100.0);

        Assertions.assertEquals(100.0, updatedPerson.getBalance());
    }

    /**
     * Проверка уменьшения баланса пользователя
     */
    @Test
    void testReduceBalance() {
        Person person = personService.create(new AuthRequest("user2005", "123456789"));
        Person topUpPerson = personService.topUpBalance(person, 200.0);
        Person reducedPerson = personService.reduceBalance(topUpPerson, 50.0);

        Assertions.assertEquals(150.0, reducedPerson.getBalance());
    }

    /**
     * Проверка блокировки и разблокировки пользователя
     */
    @Test
    void testBlockAndUnblock() {
        Person person = personService.create(new AuthRequest("user2005", "123456789"));

        Person blockedPerson = personService.block(person);
        Assertions.assertEquals(PersonAccountStatus.BLOCKED, blockedPerson.getAccountStatus());

        Person unblockedPerson = personService.unblock(person);
        Assertions.assertEquals(PersonAccountStatus.ACTIVE, unblockedPerson.getAccountStatus());
    }

    /**
     * Проверка смены роли пользователя
     */
    @Test
    void testChangeRole() {
        Person person = personService.create(new AuthRequest("user2005", "123456789"));
        Person updatedPerson = personService.changeRole(person.getId());

        Assertions.assertEquals(Role.ADMIN, updatedPerson.getRole());

        Person updatedAgainPerson = personService.changeRole(updatedPerson.getId());
        Assertions.assertEquals(Role.USER, updatedAgainPerson.getRole());
    }

    /**
     * Проверка удаления пользователя
     */
    @Test
    void testDeletePerson() {
        Person person = personService.create(new AuthRequest("user2005", "123456789"));
        personService.delete(person);
        Assertions.assertFalse(personRepository.findById(person.getId()).isPresent());
    }

    /**
     * Проверка получения количества пользователей
     */
    @Test
    void testGetPersonsCount() {
        personService.create(new AuthRequest("user2005", "123456789"));
        personService.create(new AuthRequest("user2006", "123456789"));

        long personsCount = personService.getPersonsCount();
        Assertions.assertEquals(2, personsCount);
    }
}