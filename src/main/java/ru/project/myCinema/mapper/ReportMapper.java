package ru.project.myCinema.mapper;

import org.springframework.stereotype.Component;
import ru.project.myCinema.dto.report.ReportResponse;
import ru.project.myCinema.model.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * Маппер сущности отчёта
 */
@Component
public class ReportMapper {

    /**
     * Маппинг списка Report в список ReportResponse
     */
    public List<ReportResponse> mapToReportResponses(List<Report> reports) {
        List<ReportResponse> reportResponses = new ArrayList<>();
        for (Report report : reports) {
            reportResponses.add(mepToReportResponse(report));
        }
        return reportResponses;
    }

    /**
     * Маппинг Report в список Report
     */
    public ReportResponse mepToReportResponse(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getStatus(),
                report.getContent()
        );
    }
}
