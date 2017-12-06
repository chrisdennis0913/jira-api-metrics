package gov.uscis.vis.api.utils;

import gov.uscis.vis.api.IssueTypeEnum;
import gov.uscis.vis.api.PointTypeEnum;
import gov.uscis.vis.api.models.SprintList;
import gov.uscis.vis.api.models.best.SprintMetricsDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ForecastCalculator implements MetricsCalculator{
  IssueTypeEnum[] issueTypeEnumArray;

  public ForecastCalculator(IssueTypeEnum[] issueTypeEnumArray){
    this.issueTypeEnumArray = issueTypeEnumArray;
  }

  public List<SprintMetricsDto> calculate(int boardId, int sprints, Long sprintId,
                                          Map<Long, Map<PointTypeEnum, Double>> issueTypesMap,
                                          SprintList closedSprintList){
    Double issuesCompleted = 0.0;
    Double totalIssues = 0.0;
    Double storyPointsCompleted = 0.0;
    Double totalStoryPoints = 0.0;

    for(IssueTypeEnum issueTypeEnum: issueTypeEnumArray){
      issuesCompleted += issueTypesMap.get(issueTypeEnum.getId()).get(PointTypeEnum.COMPLETED_STORIES);
      totalIssues += issueTypesMap.get(issueTypeEnum.getId()).get(PointTypeEnum.TOTAL_STORIES);
      storyPointsCompleted += issueTypesMap.get(issueTypeEnum.getId()).get(PointTypeEnum.COMPLETED_POINTS);
      totalStoryPoints += issueTypesMap.get(issueTypeEnum.getId()).get(PointTypeEnum.TOTAL_POINTS);
    }

    SprintMetricsDto sprintMetricsDto = new SprintMetricsDto(boardId, sprintId, issuesCompleted * 100 / totalIssues, storyPointsCompleted * 100 / totalStoryPoints);

    return Arrays.asList(sprintMetricsDto);
  }
}
