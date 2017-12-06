package gov.uscis.vis.api.utils;

import gov.uscis.vis.api.IssueTypeEnum;
import gov.uscis.vis.api.PointTypeEnum;
import gov.uscis.vis.api.models.SprintList;
import gov.uscis.vis.api.models.best.SprintMetricsDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RatioCalculator implements MetricsCalculator{
  IssueTypeEnum[] issueTypeEnumArray;

  public RatioCalculator(){}

  public List<SprintMetricsDto> calculate(int boardId, int sprints, Long sprintId, Map<Long,
      Map<PointTypeEnum, Double>> issueTypesMap, SprintList closedSprintList){
    // 5) Sprint work ratio: (New User Stories + product enhancement user stories) / (technical debt user stories + production incidents + other user stories)
    // story totals + tasks + spikes / bugs + preview defects + production defects
    double issueRatio = (issueTypesMap.get(IssueTypeEnum.STORY.getId()).get(PointTypeEnum.TOTAL_STORIES)
        + issueTypesMap.get(IssueTypeEnum.TASK.getId()).get(PointTypeEnum.TOTAL_STORIES)
        + issueTypesMap.get(IssueTypeEnum.SPIKE.getId()).get(PointTypeEnum.TOTAL_STORIES))
        / (issueTypesMap.get(IssueTypeEnum.BUG.getId()).get(PointTypeEnum.TOTAL_STORIES)
        + issueTypesMap.get(IssueTypeEnum.PREVIEW_DEFECT.getId()).get(PointTypeEnum.TOTAL_STORIES)
        + issueTypesMap.get(IssueTypeEnum.PRODUCTION_DEFECT.getId()).get(PointTypeEnum.TOTAL_STORIES));
    double storyPointRatio = (issueTypesMap.get(IssueTypeEnum.STORY.getId()).get(PointTypeEnum.TOTAL_POINTS)
        + issueTypesMap.get(IssueTypeEnum.TASK.getId()).get(PointTypeEnum.TOTAL_POINTS)
        + issueTypesMap.get(IssueTypeEnum.SPIKE.getId()).get(PointTypeEnum.TOTAL_POINTS))
        / (issueTypesMap.get(IssueTypeEnum.BUG.getId()).get(PointTypeEnum.TOTAL_POINTS)
        + issueTypesMap.get(IssueTypeEnum.PREVIEW_DEFECT.getId()).get(PointTypeEnum.TOTAL_POINTS)
        + issueTypesMap.get(IssueTypeEnum.PRODUCTION_DEFECT.getId()).get(PointTypeEnum.TOTAL_POINTS));

    SprintMetricsDto sprintMetricsDto = new SprintMetricsDto(boardId, sprintId, issueRatio, storyPointRatio);

    return Arrays.asList(sprintMetricsDto);
  }
}
