package com.classreview.scheduler.infra.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.classreview.core.application.usecase.report.GenerateWeeklyReportUseCase;
import com.classreview.scheduler.infra.gateway.DynamoDBFeedbackGateway;

import java.util.Map;

public class WeeklyReportSchedulerHandler
        implements RequestHandler<Map<String, Object>, String> {

    private final GenerateWeeklyReportUseCase useCase;

    public WeeklyReportSchedulerHandler() {
        var gateway = new DynamoDBFeedbackGateway();
        this.useCase = new GenerateWeeklyReportUseCase(gateway);
    }

    @Override
    public String handleRequest(Map<String, Object> event, Context context) {
        context.getLogger().log("Scheduler executado em: " + event.get("time"));

        try {
            var report = useCase.execute();

            context.getLogger().log("Total feedbacks: " + report.feedbacks().size());
            context.getLogger().log("Média avaliação: " + report.averageRating());
            context.getLogger().log("Feedbacks por dia: " + report.feedbacksPerDay());
            context.getLogger().log("Feedbacks por urgência: " + report.feedbacksByUrgency());

            return "Relatório gerado com sucesso!";

        } catch (Exception e) {
            context.getLogger().log("Erro: " + e.getMessage());
            throw new RuntimeException("Falha ao gerar relatório", e);
        }
    }
}