package ru.project.myCinema.dto;

import ru.project.myCinema.model.ReportStatus;

public record ReportResponse(Long id, ReportStatus reportStatus, String content) {
}
