package ru.project.myCinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.myCinema.model.Person;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностями пользователей
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * Поиск пользователя по логину
     */
    Optional<Person> findByLogin(String login);
}
