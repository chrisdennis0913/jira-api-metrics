package gov.uscis.vis.api.service;

import gov.uscis.vis.api.IssueTypeEnum;
import gov.uscis.vis.api.models.bad.ForecastAccuracyDto;
import gov.uscis.vis.api.models.bad.IssuesCompletedDto;
import gov.uscis.vis.api.models.bad.WorkRatioDto;

import java.util.List;

/**
 * Created by cedennis on 3/10/17.
 */
public interface BadMetricsService {
    List<ForecastAccuracyDto> forecastAccuracy(int boardId, int sprints, IssueTypeEnum[] issueTypeEnumArray);
    IssuesCompletedDto issuesCompleted(int boardId, int sprints);
    List<WorkRatioDto> workRatio(int boardId, int sprints);
}
