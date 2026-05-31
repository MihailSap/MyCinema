package ru.project.myCinema.dto.report;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.project.myCinema.model.ReportStatus;

@Schema(title = "Данные отчёта")
public record ReportResponse(Long id, ReportStatus reportStatus, String content) {
}
