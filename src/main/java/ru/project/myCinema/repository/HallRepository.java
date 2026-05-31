package ru.project.myCinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.myCinema.model.Hall;

/**
 * Репозиторий для работы с сущностью зала
 */
@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
}
