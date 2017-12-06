package gov.uscis.vis.api.resource;

import gov.uscis.vis.api.IssueTypeEnum;
import gov.uscis.vis.api.models.best.SprintMetricsDto;
import gov.uscis.vis.api.service.BestMetricsService;
import gov.uscis.vis.api.utils.CompletedCalculator;
import gov.uscis.vis.api.utils.ForecastCalculator;
import gov.uscis.vis.api.utils.MetricsCalculator;
import gov.uscis.vis.api.utils.RatioCalculator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by cedennis on 3/10/17.
 */
@Path("/board/strategy")
@Api("/board/strategy")
@Component
public class BestMetricsResource {

    private static final Logger log = LoggerFactory.getLogger(BestMetricsResource.class);

    @Autowired
    private BestMetricsService bestMetricsService;

    @GET
    @Path("/forecastAccuracy/{boardId}")
    @ApiOperation(value = "Pulls forecast Accuracy for a given board and X sprints in the past")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SprintMetricsDto> forecastAccuracies(@PathParam("boardId") Integer boardId) {
        Integer sprints = 1;

        MetricsCalculator calculator = new ForecastCalculator(new IssueTypeEnum[]{IssueTypeEnum.STORY});
        return bestMetricsService.calculateMetrics(boardId, sprints, calculator);
    }

    @GET
    @Path("/issuesCompleted/{boardId}")
    @ApiOperation(value = "Pulls stories completed for a given board and X sprints in the past")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<SprintMetricsDto> issuesCompleted(@PathParam("boardId") Integer boardId){

        MetricsCalculator calculator = new CompletedCalculator();
        return bestMetricsService.calculateMetrics(boardId, 3, calculator);
    }

    @GET
    @Path("/workRatio/{boardId}")
    @ApiOperation(value = "Pulls sprint work ratio for a given board and X sprints in the past")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<SprintMetricsDto> workRatio(@PathParam("boardId") Integer boardId){
        MetricsCalculator calculator = new RatioCalculator();
        return bestMetricsService.calculateMetrics(boardId, 1, calculator);
    }

    @GET
    @Path("/forecastBugAccuracy/{boardId}")
    @ApiOperation(value = "Pulls forecast Accuracy for bugs on a given board and X sprints in the past")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SprintMetricsDto> forecastBugAccuracies(@PathParam("boardId") Integer boardId) {
        Integer sprints = 1;

        MetricsCalculator calculator = new ForecastCalculator(new IssueTypeEnum[]{IssueTypeEnum.BUG, IssueTypeEnum.PREVIEW_DEFECT, IssueTypeEnum.PRODUCTION_DEFECT});
        return bestMetricsService.calculateMetrics(boardId, sprints, calculator);
    }
}
