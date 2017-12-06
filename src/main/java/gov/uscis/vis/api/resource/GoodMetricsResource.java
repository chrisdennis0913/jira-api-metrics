package gov.uscis.vis.api.resource;

import gov.uscis.vis.api.IssueTypeEnum;
import gov.uscis.vis.api.factory.CalculatorEnum;
import gov.uscis.vis.api.factory.CalculatorFactory;
import gov.uscis.vis.api.factory.ForecastCalculatorFactory;
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
@Path("/board/strategy/factory")
@Api("/board/strategy/factory")
@Component
public class GoodMetricsResource {

    private static final Logger log = LoggerFactory.getLogger(GoodMetricsResource.class);

    @Autowired
    private BestMetricsService bestMetricsService;

    private CalculatorFactory calculatorFactory = new ForecastCalculatorFactory();

    @GET
    @Path("/forecastAccuracy/{boardId}")
    @ApiOperation(value = "Pulls forecast Accuracy for a given board and X sprints in the past")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SprintMetricsDto> forecastAccuracies(@PathParam("boardId") Integer boardId) {
        Integer sprints = 1;
        return bestMetricsService.calculateMetrics(boardId, sprints,
            calculatorFactory.create(CalculatorEnum.STORYFORECAST));
    }

    @GET
    @Path("/issuesCompleted/{boardId}")
    @ApiOperation(value = "Pulls stories completed for a given board and X sprints in the past")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<SprintMetricsDto> issuesCompleted(@PathParam("boardId") Integer boardId){
        return bestMetricsService.calculateMetrics(boardId, 3,
            calculatorFactory.create(CalculatorEnum.COMPLETED));
    }

    @GET
    @Path("/workRatio/{boardId}")
    @ApiOperation(value = "Pulls sprint work ratio for a given board and X sprints in the past")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<SprintMetricsDto> workRatio(@PathParam("boardId") Integer boardId){
        return bestMetricsService.calculateMetrics(boardId, 1,
            calculatorFactory.create(CalculatorEnum.RATIO));
    }

    @GET
    @Path("/forecastBugAccuracy/{boardId}")
    @ApiOperation(value = "Pulls forecast Accuracy for bugs on a given board and X sprints in the past")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SprintMetricsDto> forecastBugAccuracies(@PathParam("boardId") Integer boardId) {
        Integer sprints = 1;
        return bestMetricsService.calculateMetrics(boardId, sprints,
            calculatorFactory.create(CalculatorEnum.BUGFORECAST));
    }
}
