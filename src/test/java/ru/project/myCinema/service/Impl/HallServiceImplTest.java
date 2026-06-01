package ru.project.myCinema.service.Impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.project.myCinema.dto.hall.HallRequest;
import ru.project.myCinema.exception.NotFoundException;
import ru.project.myCinema.model.Hall;
import ru.project.myCinema.model.Seat;
import ru.project.myCinema.repository.HallRepository;
import ru.project.myCinema.repository.SeatRepository;
import ru.project.myCinema.service.HallService;

import java.util.List;

/**
 * Тесты сервиса для работы с залом
 */
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class HallServiceImplTest {

    @Autowired
    private HallService hallService;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private SeatRepository seatRepository;

    /**
     * Проверка создания зала
     */
    @Test
    void testCreateHall() {
        int number = 1;
        int capacity = 10;
        HallRequest request = new HallRequest(number, capacity);
        Hall hall = hallService.create(request);

        Assertions.assertNotNull(hall);
        Assertions.assertEquals(number, hall.getNumber());
        Assertions.assertEquals(capacity, hall.getCapacity());

        List<Seat> seats = seatRepository.findByHall(hall);

        Assertions.assertNotNull(seats);
        Assertions.assertEquals(10, seats.size());
    }

    /**
     * Проверка получения зала по id
     */
    @Test
    void testGetByIdSuccess() {

        Hall hall = hallService.create(new HallRequest(1, 10));
        Hall foundHall = hallService.getById(hall.getId());

        Assertions.assertNotNull(foundHall);
        Assertions.assertEquals(hall, foundHall);
    }

    /**
     * Проверка получения несуществующего зала по id
     */
    @Test
    void testGetByIdFailure() {
        Assertions.assertThrows(NotFoundException.class, () -> hallService.getById(999L));
    }

    /**
     * Проверка получения всех залов
     */
    @Test
    void testGetAllHalls() {
        hallService.create(new HallRequest(1, 10));
        hallService.create(new HallRequest(2, 20));
        List<Hall> halls = hallService.getAllHalls();

        Assertions.assertNotNull(halls);
        Assertions.assertEquals(2, halls.size());
    }

    /**
     * Проверка обновления номера зала
     */
    @Test
    void testUpdateHallNumber() {
        Hall hall = hallService.create(new HallRequest(1, 10));
        HallRequest request = new HallRequest(2, null);
        Hall updatedHall = hallService.update(request, hall);

        Assertions.assertNotNull(updatedHall);
        Assertions.assertEquals(2, updatedHall.getNumber());
    }

    /**
     * Проверка увеличения вместимости
     */
    @Test
    void testUpdateCapacity() {
        Hall hall = hallService.create(new HallRequest(1, 5));

        int newCapacity = 10;
        HallRequest request = new HallRequest(null, newCapacity);
        Hall updatedHall = hallService.update(request, hall);

        Assertions.assertNotNull(updatedHall);
        Assertions.assertEquals(newCapacity, updatedHall.getCapacity());
        Assertions.assertEquals(newCapacity, seatRepository.findByHall(updatedHall).size());
    }

    /**
     * Проверка уменьшения вместимости
     */
    @Test
    void testUpdateLowerCapacity() {
        Hall hall = hallService.create(new HallRequest(1, 10));

        int newCapacity = 5;
        HallRequest request = new HallRequest(null, newCapacity);
        Hall updatedHall = hallService.update(request, hall);

        Assertions.assertNotNull(updatedHall);
        Assertions.assertEquals(newCapacity, updatedHall.getCapacity());
        Assertions.assertEquals(newCapacity, seatRepository.findByHall(updatedHall).size());
    }

    /**
     * Проверка удаления зала
     */
    @Test
    void testDeleteHall() {
        Hall hall = hallService.create(new HallRequest(1, 10));
        hallService.delete(hall);
        Assertions.assertFalse(hallRepository.findById(hall.getId()).isPresent());
    }
}