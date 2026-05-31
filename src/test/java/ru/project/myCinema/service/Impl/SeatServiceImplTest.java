package ru.project.myCinema.service.Impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.project.myCinema.model.*;
import ru.project.myCinema.repository.HallRepository;
import ru.project.myCinema.repository.SeatRepository;
import ru.project.myCinema.service.SeatService;

import java.util.List;

/**
 * Тесты сервиса для работы с местом
 */
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SeatServiceImplTest {

    @Autowired
    private SeatService seatService;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private HallRepository hallRepository;

    /**
     * Проверка получения места по id
     */
    @Test
    void testGetByIdSuccess() {
        Hall hall = createHall();
        Seat seat = createSeat(1, hall);

        Seat foundSeat = seatService.getById(seat.getId());

        Assertions.assertEquals(seat, foundSeat);
    }

    /**
     * Проверка получения несуществующего места
     */
    @Test
    void testGetByIdFailure() {
        Assertions.assertThrows(RuntimeException.class, () -> seatService.getById(999L));
    }

    /**
     * Проверка получения мест по залу
     */
    @Test
    void testGetSeatsByHall() {
        Hall hall = createHall();
        createSeat(1, hall);
        createSeat(2, hall);

        List<Seat> seats = seatService.getSeatsByHall(hall);
        Assertions.assertNotNull(seats);
        Assertions.assertEquals(2, seats.size());
    }

    /**
     * Проверка создания одного места
     */
    @Test
    void testCreateByHallSingleSeat() {
        Hall hall = createHall();
        seatService.createByHall(1, hall);
        Assertions.assertEquals(1, seatRepository.findByHall(hall).size());
    }

    /**
     * Проверка удаления места
     */
    @Test
    void testDeleteSeat() {
        Hall hall = createHall();
        Seat seat = createSeat(1, hall);

        seatService.delete(seat);
        Assertions.assertFalse(seatRepository.findById(seat.getId()).isPresent());
    }

    /**
     * Проверка удаления всех мест зала
     */
    @Test
    void testDeleteByHall() {
        Hall hall = createHall();
        createSeat(1, hall);
        createSeat(2, hall);
        seatService.deleteByHall(hall);

        Assertions.assertTrue(seatRepository.findByHall(hall).isEmpty());
    }

    /**
     * Проверка удаления мест с номером больше указанного
     */
    @Test
    void testDeleteByHallAndNumberGreaterThan() {
        Hall hall = createHall();
        createSeat(1, hall);
        createSeat(2, hall);
        createSeat(3, hall);
        createSeat(4, hall);

        seatService.deleteByHallAndNumberGreaterThan(hall, 2);

        List<Seat> seats = seatRepository.findByHall(hall);
        Assertions.assertNotNull(seats);
        Assertions.assertEquals(2, seats.size());
    }

    /**
     * Вспомогательный метод для создания зала
     */
    private Hall createHall() {
        Hall hall = new Hall();
        hall.setNumber(1);
        hall.setCapacity(10);
        return hallRepository.save(hall);
    }

    /**
     * Вспомогательный метод для создания места
     */
    private Seat createSeat(Integer number, Hall hall) {
        Seat seat = new Seat();
        seat.setNumber(number);
        seat.setHall(hall);
        return seatRepository.save(seat);
    }
}