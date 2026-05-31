package ru.project.myCinema.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.report.ReportResponse;
import ru.project.myCinema.mapper.ReportMapper;
import ru.project.myCinema.model.Report;
import ru.project.myCinema.service.ReportService;

import java.util.List;

@Tag(name = "Эндпоинты для многопоточной работы с отчётами")
@RestController
@RequestMapping("/api/reports")
public class ApiReportController {

    private final ReportService reportService;
    private final ReportMapper reportMapper;

    @Autowired
    public ApiReportController(ReportService reportService, ReportMapper reportMapper) {
        this.reportService = reportService;
        this.reportMapper = reportMapper;
    }

    @Operation(description = "Создание и запуск формирования содержимого отчёта")
    @PostMapping
    public Long createAndGenerateReport() {
        Long reportId = reportService.create();
        reportService.generateAsync(reportId);
        return reportId;
    }

    @Operation(description = "Получение содержимого отчёта по его id")
    @GetMapping("/{id}")
    public String getContent(@PathVariable("id") Long id) {
        return reportService.getContent(id);
    }

    @Operation(description = "Получение данных всех отчётов")
    @GetMapping
    public List<ReportResponse> getReports() {
        List<Report> reports = reportService.getReports();
        return reportMapper.mapToReportResponses(reports);
    }
}
