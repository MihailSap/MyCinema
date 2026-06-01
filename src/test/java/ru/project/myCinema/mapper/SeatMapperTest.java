package ru.project.myCinema.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.project.myCinema.dto.seat.SeatStatusResponse;
import ru.project.myCinema.dto.seat.SeatIdResponse;
import ru.project.myCinema.dto.seat.SeatStatus;
import ru.project.myCinema.model.Seat;

import java.util.List;
import java.util.Set;

/**
 * Тесты для маппера мест
 */
class SeatMapperTest {

    private final SeatMapper seatMapper = new SeatMapper();
    private Seat seat;

    @BeforeEach
    void setUp() {
        seat = new Seat();
        seat.setId(1L);
        seat.setNumber(5);
    }

    /**
     * Проверяет маппинг Seat в SeatStatusResponse для свободного места
     */
    @Test
    void testMapToSeatStatusResponseAvailable() {
        SeatStatusResponse response = seatMapper.mapToSeatStatusResponse(seat, false);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(5, response.number());
        Assertions.assertEquals(SeatStatus.AVAILABLE, response.status());
    }

    /**
     * Проверяет маппинг Seat в SeatStatusResponse для занятого места
     */
    @Test
    void testMapToSeatStatusResponseTaken() {
        SeatStatusResponse response = seatMapper.mapToSeatStatusResponse(seat, true);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(5, response.number());
        Assertions.assertEquals(SeatStatus.TAKEN, response.status());
    }

    /**
     * Проверяет маппинг списка Seat в список SeatIdResponse
     */
    @Test
    void testMapToSeatIdResponses() {
        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setNumber(10);

        List<SeatIdResponse> responses = seatMapper.mapToSeatIdResponses(List.of(seat, seat2));

        Assertions.assertEquals(2, responses.size());

        SeatIdResponse firstSeatIdResponse = responses.getFirst();
        Assertions.assertNotNull(firstSeatIdResponse);
        Assertions.assertEquals(1L, firstSeatIdResponse.id());
        Assertions.assertEquals(5, firstSeatIdResponse.number());

        SeatIdResponse secondSeatIdResponse = responses.get(1);
        Assertions.assertNotNull(secondSeatIdResponse);
        Assertions.assertEquals(2L, secondSeatIdResponse.id());
        Assertions.assertEquals(10, secondSeatIdResponse.number());
    }

    /**
     * Проверяет маппинг списка Seat в список номеров
     */
    @Test
    void testMapToSeatsNumbersList() {
        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setNumber(10);

        List<Integer> numbers = seatMapper.mapToSeatsNumbers(List.of(seat, seat2));

        Assertions.assertEquals(2, numbers.size());
        Assertions.assertTrue(numbers.contains(5));
        Assertions.assertTrue(numbers.contains(10));
    }

    /**
     * Проверяет маппинг множества Seat в список номеров
     */
    @Test
    void testMapToSeatsNumbersSet() {
        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setNumber(10);

        List<Integer> numbers = seatMapper.mapToSeatsNumbers(Set.of(seat, seat2));

        Assertions.assertEquals(2, numbers.size());
        Assertions.assertTrue(numbers.contains(5));
        Assertions.assertTrue(numbers.contains(10));
    }

    /**
     * Проверяет маппинг пустого списка Seat в список SeatIdResponse
     */
    @Test
    void testMapToSeatIdResponsesEmptyList() {
        List<SeatIdResponse> responses = seatMapper.mapToSeatIdResponses(List.of());

        Assertions.assertNotNull(responses);
        Assertions.assertTrue(responses.isEmpty());
    }

    /**
     * Проверяет маппинг пустого списка Seat в список номеров
     */
    @Test
    void testMapToSeatsNumbersEmptyList() {
        List<Integer> numbers = seatMapper.mapToSeatsNumbers(List.of());

        Assertions.assertNotNull(numbers);
        Assertions.assertTrue(numbers.isEmpty());
    }

    /**
     * Проверяет маппинг пустого множества Seat в список номеров
     */
    @Test
    void testMapToSeatsNumbersEmptySet() {
        List<Integer> numbers = seatMapper.mapToSeatsNumbers(Set.of());

        Assertions.assertNotNull(numbers);
        Assertions.assertTrue(numbers.isEmpty());
    }
}