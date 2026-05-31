package ru.project.myCinema.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.myCinema.dto.AuthRequest;
import ru.project.myCinema.dto.UpdatePersonRequest;
import ru.project.myCinema.model.Person;
import ru.project.myCinema.model.PersonAccountStatus;
import ru.project.myCinema.model.Role;
import ru.project.myCinema.repository.PersonRepository;
import ru.project.myCinema.service.PersonService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса для работы с пользователями
 */
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonServiceImpl(
            PersonRepository personRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public Person create(AuthRequest authRequest) {
        Person person = new Person();
        person.setLogin(authRequest.login());
        person.setPassword(passwordEncoder.encode(authRequest.password()));
        person.setRole(Role.USER);
        person.setBalance(0.0);
        person.setAccountStatus(PersonAccountStatus.ACTIVE);
        return personRepository.save(person);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Person getById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    @Transactional(readOnly = true)
    @Override
    public Person getByLogin(String login){
        return personRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isExistsByLogin(String login) {
        Optional<Person> person = personRepository.findByLogin(login);
        return person.isPresent();
    }

    @Transactional
    @Override
    public Person update(UpdatePersonRequest updatePersonRequest, Person person) {
        String login = updatePersonRequest.login();
        if(login != null && !login.isEmpty()) {
            if(isExistsByLogin(login) && !person.getLogin().equals(login)) {
               throw new RuntimeException("Пользователь с login=%s уже существует".formatted(login));
            }
            person.setLogin(login);
        }

        String name = updatePersonRequest.name();
        if(name != null && !name.isEmpty()) {
            person.setName(name);
        }

        String surname = updatePersonRequest.surname();
        if(surname != null && !surname.isEmpty()) {
            person.setSurname(surname);
        }

        return personRepository.save(person);
    }

    @Transactional
    @Override
    public Person topUpBalance(Person person, Double amount) {
        Double currentBalance = person.getBalance();
        if(currentBalance.isNaN()){
            currentBalance = 0.0;
        }

        currentBalance += amount;
        person.setBalance(currentBalance);

        return personRepository.save(person);
    }

    @Transactional
    @Override
    public Person reduceBalance(Person person, Double amount) {
        person.setBalance(person.getBalance() - amount);
        return personRepository.save(person);
    }

    @Transactional
    @Override
    public Person block(Person person) {
        person.setAccountStatus(PersonAccountStatus.BLOCKED);
        return personRepository.save(person);
    }

    @Transactional
    @Override
    public Person unblock(Person person) {
        person.setAccountStatus(PersonAccountStatus.ACTIVE);
        return personRepository.save(person);
    }

    @Transactional
    @Override
    public Person changeRole(Person person) {
        if(Role.USER.equals(person.getRole())) {
            person.setRole(Role.ADMIN);
        } else {
            person.setRole(Role.USER);
        }
        return personRepository.save(person);
    }

    @Transactional
    @Override
    public void delete(Person person) {
        personRepository.delete(person);
    }

    @Transactional(readOnly = true)
    @Override
    public long getPersonsCount() {
        return personRepository.count();
    }
}
