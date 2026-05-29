package com.classreview.scheduler.infra.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.classreview.core.application.dto.report.WeeklyReportDTO;
import com.classreview.core.application.usecase.report.GenerateWeeklyReportUseCase;
import com.classreview.scheduler.infra.exception.GenerateReportException;
import com.classreview.scheduler.infra.exception.ReportNotificationException;
import com.classreview.scheduler.infra.gateway.DynamoDBFeedbackGateway;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.Map;

public class WeeklyReportSchedulerHandler
        implements RequestHandler<Map<String, Object>, String> {

    private final GenerateWeeklyReportUseCase useCase;
    private final SnsClient snsClient = SnsClient.create();
    private final String topicArn = System.getenv("WEEKLY_REPORT_TOPIC_ARN");

    public WeeklyReportSchedulerHandler() {

        var gateway = new DynamoDBFeedbackGateway();

        this.useCase = new GenerateWeeklyReportUseCase(gateway);
    }

    @Override
    public String handleRequest(
            Map<String, Object> event,
            Context context
    ) {

        context.getLogger().log("Scheduler executado em: " + event.get("time")
        );

        try {

            WeeklyReportDTO report = useCase.execute();
            String reportMessage = buildReportMessage(report);

            sendReport(reportMessage);

            context.getLogger().log(reportMessage);
            context.getLogger().log("Weekly report sent successfully");

            return "Relatório gerado e enviado com sucesso!";

        } catch (Exception e) {
            context.getLogger().log("Erro ao gerar relatório: " + e.getMessage());
            throw new GenerateReportException("Falha ao gerar relatório: " + e);
        }
    }

    private String buildReportMessage(
            WeeklyReportDTO report
    ) {

        return """
                CLASS REVIEW - WEEKLY REPORT
                
                Summary
                ------------------------
                Total Feedbacks : %d
                Average Rating  : %.2f
                
                Urgency Distribution
                ------------------------
                %s
                
                Daily Distribution
                ------------------------
                %s
                """
                .formatted(
                        report.feedbacks().size(),
                        report.averageRating(),
                        formatUrgencyData(report.feedbacksByUrgency()),
                        formatDailyData(report.feedbacksPerDay())
                );
    }

    private String formatUrgencyData(
            Map<String, Long> urgencyData
    ) {

        StringBuilder builder =
                new StringBuilder();

        urgencyData.forEach((urgency, total) -> {

            String label =
                    switch (urgency) {
                        case "CRITICAL" -> "Críticos";
                        case "MEDIUM" -> "Médios";
                        case "GOOD" -> "Positivos";
                        default -> urgency;
                    };

            builder.append("- ")
                    .append(label)
                    .append(": ")
                    .append(total)
                    .append("\n");
        });

        return builder.toString();
    }

    private String formatDailyData(
            Map<String, Long> dailyData
    ) {

        StringBuilder builder = new StringBuilder();

        dailyData.forEach(
                (date, total) ->
                        builder.append("- ")
                                .append(date)
                                .append(": ")
                                .append(total)
                                .append("\n")
        );

        return builder.toString();
    }

    private void sendReport(
            String reportMessage
    ) {

        try {

            PublishRequest request =
                    PublishRequest.builder()
                            .topicArn(topicArn)
                            .subject("Weekly Feedback Report")
                            .message(reportMessage)
                            .build();

            snsClient.publish(request);

        } catch (Exception e) {
            throw new ReportNotificationException("Erro ao enviar relatório semanal: " + e);
        }
    }
}