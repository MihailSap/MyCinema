package ru.project.myCinema.service;

import ru.project.myCinema.model.Report;

import java.util.List;

/**
 * Сервис для работы с отчётом
 */
public interface ReportService {

    /**
     * Получение содержимого отчёта по его id
     */
    String getContent(Long id);

    /**
     * Создание отчёта
     */
    Long create();

    /**
     * Асинхронный метод, запускающий формирование содержимого отчёта
     */
    void generateAsync(Long id);

    /**
     * Получение всех отчётов
     */
    List<Report> getReports();
}

