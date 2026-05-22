package com.classreview.infra.web.controller.report;

import com.classreview.core.application.dto.report.WeeklyReportDTO;
import com.classreview.core.application.usecase.report.GenerateWeeklyReportUseCase;

import com.classreview.infra.web.mapper.report.ReportWebMapper;
import com.classreview.infra.web.payload.report.WeeklyReportResponsePayload;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import jakarta.ws.rs.core.MediaType;

@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
public class ReportController {

    private final GenerateWeeklyReportUseCase useCase;

    public ReportController(
            GenerateWeeklyReportUseCase useCase
    ) {

        this.useCase = useCase;
    }

    @GET
    @Path("/weekly")
    public WeeklyReportResponsePayload generateWeeklyReport() {

        WeeklyReportDTO response =
                useCase.execute();

        return ReportWebMapper.toResponse(
                response
        );
    }
}
