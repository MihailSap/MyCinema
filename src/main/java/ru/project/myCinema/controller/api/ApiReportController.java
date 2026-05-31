package ru.project.myCinema.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.project.myCinema.dto.report.ReportResponse;
import ru.project.myCinema.mapper.ReportMapper;
import ru.project.myCinema.model.Report;
import ru.project.myCinema.service.ReportService;

import java.util.List;

@Controller
@RequestMapping("/reports")
public class ApiReportController {

    private final ReportService reportService;
    private final ReportMapper reportMapper;

    @Autowired
    public ApiReportController(ReportService reportService, ReportMapper reportMapper) {
        this.reportService = reportService;
        this.reportMapper = reportMapper;
    }

    /**
     * Создание и запуск формирования содержимого отчёта
     */
    @PostMapping
    public Long createAndGenerateReport() {
        Long reportId = reportService.create();
        reportService.generateAsync(reportId);
        return reportId;
    }

    /**
     * Получение содержимого отчёта по его {@code id}
     */
    @GetMapping("/{id}")
    public String getContent(@PathVariable("id") Long id) {
        return reportService.getContent(id);
    }

    /**
     * Получение данных всех отчётов
     */
    @GetMapping
    public List<ReportResponse> getReports() {
        List<Report> reports = reportService.getReports();
        return reportMapper.mapToReportResponses(reports);
    }
}
