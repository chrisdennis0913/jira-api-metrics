package gov.uscis.vis.api.resource;

import gov.uscis.vis.api.IssueTypeEnum;
import gov.uscis.vis.api.models.bad.ForecastAccuracyDto;
import gov.uscis.vis.api.models.bad.IssuesCompletedDto;
import gov.uscis.vis.api.models.MetricsDto;
import gov.uscis.vis.api.models.bad.WorkRatioDto;
import gov.uscis.vis.api.service.BadMetricsService;
import gov.uscis.vis.api.service.ExcelService;
import gov.uscis.vis.api.service.MetricsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cedennis on 3/10/17.
 */
@Path("/board")
@Api("/board")
@Component
public class MetricsResource {

    private static final Logger log = LoggerFactory.getLogger(MetricsResource.class);

    @Autowired
    private MetricsService metricsService;

    @Autowired
    private BadMetricsService badMetricsService;

    @Autowired
    private ExcelService excelService;

    @POST
    @Path("/analyze")
    @ApiOperation(value = "Pulls metrics for a Jira board specified by ids. SAVE Mod is 1332, Everify is 722")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Integer, MetricsDto> analyzeJiraBoard(
        @ApiParam(name = "boardList", value = "List of Board Ids") List<Integer> boardList) {
        if (boardList == null || boardList.isEmpty()){
            boardList = new ArrayList<>();
            boardList.add(722);
            boardList.add(1332);
        }
        return metricsService.analyzeBoard(boardList);
    }

    @GET
    @Path("/forecastAccuracy/{boardId}")
    @ApiOperation(value = "Pulls forecast Accuracy for a given board and X sprints in the past")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ForecastAccuracyDto> forecastAccuracies(@PathParam("boardId") Integer boardId) {
        Integer sprints = 1;

        return badMetricsService.forecastAccuracy(boardId, sprints, new IssueTypeEnum[]{IssueTypeEnum.STORY});
    }

    @GET
    @Path("/issuesCompleted/{boardId}")
    @ApiOperation(value = "Pulls stories completed for a given board and X sprints in the past")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IssuesCompletedDto issuesCompleted(@PathParam("boardId") Integer boardId){
        return badMetricsService.issuesCompleted(boardId, 1);
    }

    @GET
    @Path("/workRatio/{boardId}")
    @ApiOperation(value = "Pulls sprint work ratio for a given board and X sprints in the past")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorkRatioDto> workRatio(@PathParam("boardId") Integer boardId){
        return badMetricsService.workRatio(boardId, 1);
    }

    @GET
    @Path("/forecastBugAccuracy/{boardId}")
    @ApiOperation(value = "Pulls forecast Accuracy for bugs on a given board and X sprints in the past")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ForecastAccuracyDto> forecastBugAccuracies(
        @PathParam("boardId") Integer boardId) {
        Integer sprints = 1;

        return badMetricsService.forecastAccuracy(boardId, sprints,
            new IssueTypeEnum[]{IssueTypeEnum.BUG, IssueTypeEnum.PREVIEW_DEFECT, IssueTypeEnum.PRODUCTION_DEFECT});
    }

    @GET
    @Path("/dashboard")
    @ApiOperation(value = "Reads dashboard from excel file")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int readDashboard(
        @ApiParam(name = "saveDashboardPath", value = "Path to Save Dashboard") String saveDashboardPath) {
        if (saveDashboardPath == null || saveDashboardPath.isEmpty()){
//            saveDashboardPath = "/Users/cedennis/Documents/SaveDashboard.xlsx";
            saveDashboardPath = "/Users/cedennis/Documents/Status Dump copy.xlsx";
        }
        excelService.getDataDumpFromSaveDashboard(saveDashboardPath);
        return 0;
    }
}
