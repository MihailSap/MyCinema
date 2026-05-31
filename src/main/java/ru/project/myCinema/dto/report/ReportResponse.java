package ru.project.myCinema.dto.report;

import ru.project.myCinema.model.ReportStatus;

public record ReportResponse(Long id, ReportStatus reportStatus, String content) {
}
