package ru.project.myCinema.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.myCinema.model.EntityReportData;
import ru.project.myCinema.model.Report;
import ru.project.myCinema.model.ReportStatus;
import ru.project.myCinema.repository.ReportRepository;
import ru.project.myCinema.service.MovieService;
import ru.project.myCinema.service.PersonService;
import ru.project.myCinema.service.ReportService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PersonService personService;
    private final MovieService movieService;

    @Autowired
    public ReportServiceImpl(
            ReportRepository reportRepository,
            PersonService personService,
            MovieService movieService) {
        this.reportRepository = reportRepository;
        this.personService = personService;
        this.movieService = movieService;
    }

    @Transactional(readOnly = true)
    @Override
    public String getContent(Long id)  {
        Report report = getById(id);
        if(!ReportStatus.COMPLETED.equals(report.getStatus())){
            return "Отчёт ещё не сформирован";
        }
        return report.getContent();
    }

    @Transactional
    @Override
    public Long create() {
        Report report = new Report();
        report.setStatus(ReportStatus.CREATED);
        return reportRepository.save(report).getId();
    }

    @Transactional
    @Override
    public void generateAsync(Long id) {
        Report report = getById(id);
        try{
            long startTime = System.currentTimeMillis();

            CompletableFuture<EntityReportData> entityReportDataPerson = supplyAsyncWithExecuteTime(personService::getPersonsCount);
            CompletableFuture<EntityReportData> entityReportDataMovie = supplyAsyncWithExecuteTime(movieService::getMovieCount);

            EntityReportData entityReportDataPersonResult = entityReportDataPerson.join();
            EntityReportData entityReportDataMovieResult = entityReportDataMovie.join();

            long endTime = System.currentTimeMillis() - startTime;

            updateWithCompleted(report, createHtmlContent(
                    endTime, entityReportDataPersonResult, entityReportDataMovieResult));
        } catch (Exception e){
            updateWithError(report);
            System.out.println(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Report> getReports() {
        return reportRepository.findAll();
    }

    /**
     * Получение отчёта по его id
     */
    private Report getById(Long id)  {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отчёт с id=%s не найден".formatted(id)));
    }

    /**
     * Асинхронное выполнение операции с подсчётом времени
     */
    private CompletableFuture<EntityReportData> supplyAsyncWithExecuteTime(Supplier<Long> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            long count = supplier.get();
            return new EntityReportData(count, System.currentTimeMillis() - startTime);
        });
    }

    /**
     * Обновление завершённого отчёта
     */
    private void updateWithCompleted(Report report, String content){
        report.setContent(content);
        report.setStatus(ReportStatus.COMPLETED);
        reportRepository.save(report);
    }

    /**
     * Обновление отчёта с ошибкой
     */
    private void updateWithError(Report report){
        report.setStatus(ReportStatus.ERROR);
        reportRepository.save(report);
    }

    /**
     * Формирование HTML строки с содержимым отчёта
     */
    private String createHtmlContent(long generateResultTime, EntityReportData person, EntityReportData book){
        return """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <title>Отчёт</title>
                    </head>
                    <body>
                    <table>
                        <b>На формирование отчёта потрачено %s миллисекунд</b>
                        <tr>
                            <th>Сущность</th>
                            <th>Количество</th>
                            <th>Время подсчёта (миллисекунды)</th>
                        </tr>
                        <tr>
                            <td>Person</td>
                            <td>%s</td>
                            <td>%s</td>
                        </tr>
                        <tr>
                            <td>Movie</td>
                            <td>%s</td>
                            <td>%s</td>
                        </tr>
                    </table>
                    </body>
                    </html>
                    """.formatted(
                generateResultTime, person.count(), person.time(), book.count(), book.time());
    }
}
