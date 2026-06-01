package ru.project.myCinema.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.project.myCinema.dto.hall.HallResponse;
import ru.project.myCinema.model.Hall;

import java.util.List;
import java.util.Set;

/**
 * Тесты для маппера залов
 */
class HallMapperTest {

    private final SeatMapper seatMapper = new SeatMapper();
    private final HallMapper hallMapper = new HallMapper(seatMapper);

    private Hall hall;

    @BeforeEach
    void setUp() {
        hall = new Hall();
        hall.setId(1L);
        hall.setNumber(3);
        hall.setCapacity(120);
    }

    /**
     * Проверяет маппинг одной сущности Hall в HallResponse
     */
    @Test
    void testMapToHallResponse() {
        HallResponse response = hallMapper.mapToHallResponse(hall);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(hall.getId(), response.id());
        Assertions.assertEquals(hall.getNumber(), response.number());
        Assertions.assertEquals(hall.getCapacity(), response.capacity());
    }

    /**
     * Проверяет маппинг списка залов
     */
    @Test
    void testMapToHallResponses() {
        Hall hall2 = new Hall();
        hall2.setId(2L);
        hall2.setNumber(5);
        hall2.setCapacity(200);
        hall2.setSeats(Set.of());

        List<HallResponse> responses = hallMapper.mapToHallResponses(List.of(hall, hall2));

        Assertions.assertEquals(2, responses.size());

        HallResponse firstHallResponse = responses.getFirst();
        Assertions.assertNotNull(firstHallResponse);
        Assertions.assertEquals(1L, firstHallResponse.id());
        Assertions.assertEquals(3, firstHallResponse.number());
        Assertions.assertEquals(120, firstHallResponse.capacity());

        HallResponse secondHallResponse = responses.get(1);
        Assertions.assertNotNull(secondHallResponse);
        Assertions.assertEquals(2L, secondHallResponse.id());
        Assertions.assertEquals(5, secondHallResponse.number());
        Assertions.assertEquals(200, secondHallResponse.capacity());
    }

    /**
     * Проверяет маппинг пустого списка
     */
    @Test
    void testMapToHallResponsesEmptyList() {
        List<HallResponse> responses = hallMapper.mapToHallResponses(List.of());

        Assertions.assertNotNull(responses);
        Assertions.assertTrue(responses.isEmpty());
    }
}