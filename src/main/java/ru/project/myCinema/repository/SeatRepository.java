package ru.project.myCinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.myCinema.model.Hall;
import ru.project.myCinema.model.Seat;

import java.util.List;

/**
 * Репозиторий для работы с сущностью места
 */
@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    /**
     * Удаление мест в зале
     */
    void deleteByHall(Hall hall);

    /**
     * Поиск мест по залу
     */
    List<Seat> findByHall(Hall hall);

    /**
     * Удаление мест в зале с количеством больше указанного
     */
    void deleteByHallAndNumberGreaterThan(Hall hall, Integer seatNumber);
}
