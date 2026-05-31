package ru.project.myCinema.model;

import jakarta.persistence.*;

/**
 * Сущность отчёта
 */
@Entity
@Table(name = "report")
public class Report extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    /**
     * Содержимое отчёта
     */
    private String content;

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
