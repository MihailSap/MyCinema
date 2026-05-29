package ru.project.myCinema.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.project.myCinema.dto.SessionResponse;
import ru.project.myCinema.model.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Маппер сущности Session
 */
@Component
public class SessionMapper {

    private final MovieMapper movieMapper;
    private final HallMapper hallMapper;

    @Autowired
    public SessionMapper(MovieMapper movieMapper, HallMapper hallMapper) {
        this.movieMapper = movieMapper;
        this.hallMapper = hallMapper;
    }

    /**
     * Маппинг сущностей Session в SessionResponse
     */
    public List<SessionResponse> mapToSessionResponses(List<Session> sessions) {
        List<SessionResponse> sessionResponses = new ArrayList<>();
        for (Session session : sessions) {
            sessionResponses.add(mapToSessionResponse(session));
        }
        return sessionResponses;
    }

    /**
     * Маппинг сущности Session в SessionResponse
     */
    public SessionResponse mapToSessionResponse(Session session) {
        return new SessionResponse(
                session.getId(),
                session.getTicketPrice(),
                session.getStartDateTime().toString(),
                hallMapper.mapToHallResponse(session.getHall()),
                movieMapper.mapToMovieResponse(session.getMovie())
        );
    }
}
