package ru.project.myCinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.report.ReportResponse;
import ru.project.myCinema.mapper.ReportMapper;
import ru.project.myCinema.model.Report;
import ru.project.myCinema.service.ReportService;

import java.util.List;

/**
 * Контроллер для работы с отчётами
 */
@Controller
@RequestMapping("/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;
    private final ReportMapper reportMapper;

    @Autowired
    public ReportController(
            ReportService reportService,
            ReportMapper reportMapper
    ) {
        this.reportService = reportService;
        this.reportMapper = reportMapper;
    }

    /**
     * Страница с отчётами
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String reportsPage(Model model) {
        List<Report> reports = reportService.getReports();
        List<ReportResponse> responses = reportMapper.mapToReportResponses(reports);
        model.addAttribute("reports", responses);
        return "reports/list";
    }

    /**
     * Создание отчёта
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createReport() {
        Long reportId = reportService.create();
        reportService.generateAsync(reportId);
        return "redirect:/admin/reports";
    }

    /**
     * Страница конкретного отчёта
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public String reportContent(@PathVariable("id") Long id, Model model) {
        model.addAttribute("content", reportService.getContent(id));
        return "reports/content";
    }
}