package gov.uscis.vis.api.utils;

import gov.uscis.vis.api.PointTypeEnum;
import gov.uscis.vis.api.models.Issue;
import gov.uscis.vis.api.models.IssueList;
import gov.uscis.vis.api.models.Sprint;
import gov.uscis.vis.api.models.SprintList;
import gov.uscis.vis.api.models.best.SprintMetricsDto;
import gov.uscis.vis.api.service.StoryService;
import gov.uscis.vis.api.service.StoryServiceSample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompletedCalculator implements MetricsCalculator{
  private static final int MAX_SPRINTS_TO_PROCESS = 5;

  StoryService storyService = new StoryServiceSample();

  public CompletedCalculator(){}

  public List<SprintMetricsDto> calculate(int boardId,
                                          int sprints,
                                          Long sprintId,
                                          Map<Long, Map<PointTypeEnum,
                                              Double>> issueTypesMap,
                                          SprintList closedSprintList){

    Long latestCompletedSprintId = closedSprintList.getValues().get(closedSprintList.getValues().size()-1).getId();

    // Need to recursively keep track of "subtract X amount of completed issues from Y past sprint"
    int sprintsToProcess = Math.min(sprints, Math.min(closedSprintList.getValues().size(), MAX_SPRINTS_TO_PROCESS));

    List<SprintMetricsDto> sprintMetricsDtoList = new ArrayList<>();
    sprintMetricsDtoList.add(new SprintMetricsDto(boardId,
        latestCompletedSprintId,
        IssueUtils.getPointTypeTotal(issueTypesMap, PointTypeEnum.COMPLETED_STORIES),
        IssueUtils.getPointTypeTotal(issueTypesMap, PointTypeEnum.COMPLETED_POINTS)));

    for(int sprintIter = sprintsToProcess - 2; sprintIter >= 0; sprintIter --){ //latest sprint already processed
      double currentCompletedIssuesForSprint = 0;
      double currentCompletedPointsForSprint = 0;

      Sprint currentSprint = closedSprintList.getValues().get(sprintIter);
      IssueList currentIssueList = storyService.getIssueListForSprint(currentSprint.getId());
      List<Issue> listOfIssues = currentIssueList.getIssues().stream()
          .filter(issue -> issue.getFields().getStatus() != null
              && issue.getFields().getStatus().getName().equalsIgnoreCase("Done"))
          .collect(Collectors.toList());

      Map<Long, Integer> completedIssuesFromClosedSprints = new HashMap<>();
      Map<Long, Double> completedStoryPointsFromClosedSprints = new HashMap<>();
      for(Issue currentIssue: listOfIssues){
        currentCompletedIssuesForSprint++;
        currentCompletedPointsForSprint += currentIssue.getFields().getCustomfield_10002() != null
            ? Double.valueOf(currentIssue.getFields().getCustomfield_10002())
            : 0.0;
        if (sprintIter > 0){
          IssueUtils.trackOldIssues(completedIssuesFromClosedSprints, completedStoryPointsFromClosedSprints,
              closedSprintList.getValues().get(sprintIter - 1).getId(), currentIssue.getFields());
        }
      }

      if (!completedIssuesFromClosedSprints.isEmpty()
          && completedIssuesFromClosedSprints.containsKey(currentSprint.getId())){
        currentCompletedIssuesForSprint -= completedIssuesFromClosedSprints.get(currentSprint.getId());
        currentCompletedPointsForSprint -= completedStoryPointsFromClosedSprints.get(currentSprint.getId());
      }

      sprintMetricsDtoList.add(0, new SprintMetricsDto(boardId, currentSprint.getId(),
          currentCompletedIssuesForSprint, currentCompletedPointsForSprint));
    }

    return sprintMetricsDtoList;
  }
}
