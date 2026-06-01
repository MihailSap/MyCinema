package ru.project.myCinema.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.project.myCinema.dto.report.ReportResponse;
import ru.project.myCinema.model.Report;
import ru.project.myCinema.model.ReportStatus;

import java.util.List;

/**
 * Тесты для маппера отчётов
 */
class ReportMapperTest {

    private final ReportMapper reportMapper = new ReportMapper();;

    /**
     * Проверяет маппинг списка отчётов
     */
    @Test
    void testMapToReportResponses() {
        Report report = new Report();
        report.setId(1L);
        report.setStatus(ReportStatus.CREATED);
        report.setContent("content1");

        Report report2 = new Report();
        report2.setId(2L);
        report2.setStatus(ReportStatus.COMPLETED);
        report2.setContent("content2");

        List<ReportResponse> responses = reportMapper.mapToReportResponses(List.of(report, report2));

        Assertions.assertEquals(2, responses.size());

        ReportResponse firstReportResponse = responses.getFirst();
        Assertions.assertNotNull(firstReportResponse);
        Assertions.assertEquals(1L, firstReportResponse.id());
        Assertions.assertEquals(ReportStatus.CREATED, firstReportResponse.reportStatus());
        Assertions.assertEquals("content1", firstReportResponse.content());

        ReportResponse secondReportResponse = responses.get(1);
        Assertions.assertNotNull(secondReportResponse);
        Assertions.assertEquals(2L, secondReportResponse.id());
        Assertions.assertEquals(ReportStatus.COMPLETED, secondReportResponse.reportStatus());
        Assertions.assertEquals("content2", secondReportResponse.content());
    }

    /**
     * Проверяет маппинг пустого списка
     */
    @Test
    void testMapToReportResponsesEmptyList() {
        List<ReportResponse> responses = reportMapper.mapToReportResponses(List.of());

        Assertions.assertNotNull(responses);
        Assertions.assertTrue(responses.isEmpty());
    }
}