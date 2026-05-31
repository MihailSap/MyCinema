package ru.project.myCinema.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.myCinema.dto.HallRequest;
import ru.project.myCinema.model.Hall;
import ru.project.myCinema.repository.HallRepository;
import ru.project.myCinema.service.HallService;
import ru.project.myCinema.service.SeatService;

import java.util.List;

/**
 * Реализация интерфейса для работы с залом
 */
@Service
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final SeatService seatService;

    @Autowired
    public HallServiceImpl(
            HallRepository hallRepository,
            SeatService seatService
    ) {
        this.hallRepository = hallRepository;
        this.seatService = seatService;
    }

    @Transactional
    @Override
    public Hall create(HallRequest hallRequest) {
        Integer capacity = hallRequest.capacity();

        Hall hall = new Hall();
        hall.setNumber(hallRequest.number());
        hall.setCapacity(capacity);
        hallRepository.save(hall);

        for(int i = 1; i < capacity + 1; i++){
            seatService.createByHall(i, hall);
        }

        return hall;
    }

    @Transactional(readOnly = true)
    @Override
    public Hall getById(Long id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Зал с id=%s не найден".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }

    @Transactional
    @Override
    public Hall update(HallRequest hallRequest, Hall hall) {
        Integer number = hallRequest.number();
        if (number != null) {
            hall.setNumber(number);
        }

        Integer newCapacity = hallRequest.capacity();
        if (newCapacity != null) {
            Integer oldCapacity = hall.getCapacity();
            if (newCapacity > oldCapacity) {
                for (int seatNumber = oldCapacity + 1; seatNumber <= newCapacity; seatNumber++){
                    seatService.createByHall(seatNumber, hall);
                }
            } else if (newCapacity < oldCapacity) {
                seatService.deleteByHallAndNumberGreaterThan(hall, newCapacity);
            }
            hall.setCapacity(newCapacity);
        }
        return hallRepository.save(hall);
    }

    @Transactional
    @Override
    public void delete(Hall hall) {
        hallRepository.delete(hall);
    }
}
