package ru.project.myCinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.myCinema.model.Booking;
import ru.project.myCinema.model.BookingStatus;
import ru.project.myCinema.model.Person;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с сущностью заказа
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Поиск заказов предстоящих сеансов по пользователю и статусу
     */
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.person = :person
              AND b.status = :status
              AND b.session.startDateTime >= :now
            ORDER BY b.session.startDateTime
            """)
    List<Booking> findActivePendingBookings(
            @Param("person") Person person,
            @Param("status") BookingStatus status,
            @Param("now") LocalDateTime now
    );
}
