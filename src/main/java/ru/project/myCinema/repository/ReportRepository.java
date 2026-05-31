package ru.project.myCinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.myCinema.model.Report;

import java.util.Optional;

/**
 * Сервис для работы с отчётом
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    /**
     * Получение отчёта по id
     */
    Optional<Report> findById(Long id);
}
