package gov.uscis.vis.api.service;

import gov.uscis.vis.api.IssueTypeEnum;
import gov.uscis.vis.api.PointTypeEnum;
import gov.uscis.vis.api.models.Field;
import gov.uscis.vis.api.models.bad.ForecastAccuracyDto;
import gov.uscis.vis.api.models.Issue;
import gov.uscis.vis.api.models.IssueList;
import gov.uscis.vis.api.models.IssueType;
import gov.uscis.vis.api.models.bad.IssuesCompletedDto;
import gov.uscis.vis.api.models.Sprint;
import gov.uscis.vis.api.models.SprintList;
import gov.uscis.vis.api.models.StateEnum;
import gov.uscis.vis.api.models.bad.WorkRatioDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cedennis on 3/10/17.
 */
@Service("badMetricsService")
public class BadMetricsServiceImpl implements BadMetricsService{
    private static final Logger log = LoggerFactory.getLogger(BadMetricsServiceImpl.class);

    private static final int TOTAL_SPRINTS_TO_PROCESS = 4;

    private Map<Long, Map<PointTypeEnum, Double>> issueTypesMap; // issue type
    private Map<PointTypeEnum, Double> pointsMap; //completed vs total

    private StoryService storyService; //everify board = 722, save mod =1332, vdm board = 853

    @Override
    public List<ForecastAccuracyDto> forecastAccuracy(int boardId, int sprints, IssueTypeEnum[] issueTypeEnumArray){
        storyService = new StoryServiceSample();
        initiateIssueTypesMap();

        SprintList closedSprintList = storyService.getSprintListWithState(boardId, StateEnum.CLOSED);
        Collections.sort(closedSprintList.getValues());
        Long latestCompletedSprintId = closedSprintList.getValues().get(closedSprintList.getValues().size()-1).getId();
        Long previousSprintId = closedSprintList.getValues().get(closedSprintList.getValues().size()-2).getId();
        IssueList issueList = storyService.getIssueListForSprint(latestCompletedSprintId);

        Map<Long, Integer> completedStoriesFromClosedSprints = new HashMap<>();
        Map<Long, Double> completedStoryPointsFromClosedSprints = new HashMap<>();

        //issuetype: 1=Bug, 3=Task, 7=Story, 11200=Spike, 12102=Preview Defect, 12103=Production Defect
        extractMetricsFromIssueList(issueList, completedStoriesFromClosedSprints, completedStoryPointsFromClosedSprints, previousSprintId);



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

        ForecastAccuracyDto forecastAccuracyDto = new ForecastAccuracyDto(boardId, latestCompletedSprintId, issuesCompleted * 100 / totalIssues, storyPointsCompleted * 100 / totalStoryPoints);

        return Arrays.asList(forecastAccuracyDto);
    }

    @Override
    public IssuesCompletedDto issuesCompleted(int boardId, int sprints){
        storyService = new StoryServiceSample();
        initiateIssueTypesMap();

        SprintList closedSprintList = storyService.getSprintListWithState(boardId, StateEnum.CLOSED);
        Collections.sort(closedSprintList.getValues());
        Long latestCompletedSprintId = closedSprintList.getValues().get(closedSprintList.getValues().size()-1).getId();
        Long previousSprintId = closedSprintList.getValues().get(closedSprintList.getValues().size()-2).getId();
        IssueList issueList = storyService.getIssueListForSprint(latestCompletedSprintId);

        Map<Long, Integer> completedIssuesFromClosedSprints = new HashMap<>();
        Map<Long, Double> completedStoryPointsFromClosedSprints = new HashMap<>();

        //issuetype: 1=Bug, 3=Task, 7=Story, 11200=Spike, 12102=Preview Defect, 12103=Production Defect
        extractMetricsFromIssueList(issueList, completedIssuesFromClosedSprints, completedStoryPointsFromClosedSprints, previousSprintId);



        // Need to recursively keep track of "subtract X amount of completed issues from Y past sprint"
        int sprintsToProcess = Math.min(closedSprintList.getValues().size(), TOTAL_SPRINTS_TO_PROCESS);

        int[] storiesCompletedPerSprint     = new int[sprintsToProcess];
        double[] storyPointsCompletedPerSprint = new double[sprintsToProcess];
        long[] sprintIdArray = new long[sprintsToProcess];
        storiesCompletedPerSprint[sprintsToProcess - 1] = getPointTypeTotal(issueTypesMap, PointTypeEnum.COMPLETED_STORIES);
        storyPointsCompletedPerSprint[sprintsToProcess - 1] = getPointTypeTotal(issueTypesMap, PointTypeEnum.COMPLETED_POINTS);
        sprintIdArray[sprintsToProcess - 1] = latestCompletedSprintId;

        for(int sprintIter = sprintsToProcess - 2; sprintIter >= 0; sprintIter --){ //latest sprint already processed
            int currentCompletedIssuesForSprint = 0;
            double currentCompletedPointsForSprint = 0;

            Sprint currentSprint = closedSprintList.getValues().get(sprintIter);
            sprintIdArray[sprintIter] = currentSprint.getId();
            IssueList currentIssueList = storyService.getIssueListForSprint(currentSprint.getId());
            List<Issue> listOfIssues = currentIssueList.getIssues().stream()
                .filter(issue -> issue.getFields().getStatus() != null && issue.getFields().getStatus().getName().equalsIgnoreCase("Done"))
                .collect(Collectors.toList());
            for(Issue currentIssue: listOfIssues){
                currentCompletedIssuesForSprint++;
                currentCompletedPointsForSprint += currentIssue.getFields().getCustomfield_10002() != null ? Double.valueOf(currentIssue.getFields().getCustomfield_10002()) : 0.0;
                if (sprintIter > 0){
                    trackOldIssues(completedIssuesFromClosedSprints, completedStoryPointsFromClosedSprints, closedSprintList.getValues().get(sprintIter - 1).getId(), currentIssue.getFields());
                }
            }

            if (!completedIssuesFromClosedSprints.isEmpty() && completedIssuesFromClosedSprints.containsKey(currentSprint.getId())){
                currentCompletedIssuesForSprint -= completedIssuesFromClosedSprints.get(currentSprint.getId());
                currentCompletedPointsForSprint -= completedStoryPointsFromClosedSprints.get(currentSprint.getId());
            }

            storiesCompletedPerSprint[sprintIter] = currentCompletedIssuesForSprint;
            storyPointsCompletedPerSprint[sprintIter] = currentCompletedPointsForSprint;
        }

        return new IssuesCompletedDto(boardId, sprintIdArray, storiesCompletedPerSprint, storyPointsCompletedPerSprint);
    }

    @Override
    public List<WorkRatioDto> workRatio(int boardId, int sprints){
        storyService = new StoryServiceSample();
        initiateIssueTypesMap();

        SprintList closedSprintList = storyService.getSprintListWithState(boardId, StateEnum.CLOSED);
        Collections.sort(closedSprintList.getValues());
        Long latestCompletedSprintId = closedSprintList.getValues().get(closedSprintList.getValues().size()-1).getId();
        Long previousSprintId = closedSprintList.getValues().get(closedSprintList.getValues().size()-2).getId();
        IssueList issueList = storyService.getIssueListForSprint(latestCompletedSprintId);

        Map<Long, Integer> completedStoriesFromClosedSprints = new HashMap<>();
        Map<Long, Double> completedStoryPointsFromClosedSprints = new HashMap<>();

        //issuetype: 1=Bug, 3=Task, 7=Story, 11200=Spike, 12102=Preview Defect, 12103=Production Defect
        extractMetricsFromIssueList(issueList, completedStoriesFromClosedSprints, completedStoryPointsFromClosedSprints, previousSprintId);



        // 5) Sprint work ratio: (New User Stories + product enhancement user stories) / (technical debt user stories + production incidents + other user stories)
        // story totals + tasks + spikes / bugs + preview defects + production defects
        double issueRatio = (issueTypesMap.get(IssueTypeEnum.STORY.getId()).get(PointTypeEnum.TOTAL_STORIES) + issueTypesMap.get(IssueTypeEnum.TASK.getId()).get(PointTypeEnum.TOTAL_STORIES) + issueTypesMap.get(IssueTypeEnum.SPIKE.getId()).get(PointTypeEnum.TOTAL_STORIES))
            / (issueTypesMap.get(IssueTypeEnum.BUG.getId()).get(PointTypeEnum.TOTAL_STORIES) + issueTypesMap.get(IssueTypeEnum.PREVIEW_DEFECT.getId()).get(PointTypeEnum.TOTAL_STORIES)+issueTypesMap.get(IssueTypeEnum.PRODUCTION_DEFECT.getId()).get(PointTypeEnum.TOTAL_STORIES));
        double storyPointRatio = (issueTypesMap.get(IssueTypeEnum.STORY.getId()).get(PointTypeEnum.TOTAL_POINTS) + issueTypesMap.get(IssueTypeEnum.TASK.getId()).get(PointTypeEnum.TOTAL_POINTS) + issueTypesMap.get(IssueTypeEnum.SPIKE.getId()).get(PointTypeEnum.TOTAL_POINTS))
            / (issueTypesMap.get(IssueTypeEnum.BUG.getId()).get(PointTypeEnum.TOTAL_POINTS) + issueTypesMap.get(IssueTypeEnum.PREVIEW_DEFECT.getId()).get(PointTypeEnum.TOTAL_POINTS)+issueTypesMap.get(IssueTypeEnum.PRODUCTION_DEFECT.getId()).get(PointTypeEnum.TOTAL_POINTS));

        WorkRatioDto workRatioDto = new WorkRatioDto(boardId, latestCompletedSprintId, issueRatio, storyPointRatio);

        return Arrays.asList(workRatioDto);
    }

    private void extractMetricsFromIssueList(IssueList issueList, Map<Long, Integer> completedStoriesFromClosedSprints, Map<Long, Double> completedStoryPointsFromClosedSprints, Long previousSprintId) {
        for (Issue issue : issueList.getIssues()){
            Field currentField = issue.getFields();
            IssueType currentIssueType = currentField.getIssuetype();
            incrementPointValue(currentIssueType, PointTypeEnum.TOTAL_STORIES, "1");
            incrementPointValue(currentIssueType, PointTypeEnum.TOTAL_POINTS, currentField.getCustomfield_10002());
            if (currentField.getStatus() != null && currentField.getStatus().getId().equals(10018L)) { //status id for DONE is 10018L
                incrementPointValue(currentIssueType, PointTypeEnum.COMPLETED_STORIES, "1");
                incrementPointValue(currentIssueType, PointTypeEnum.COMPLETED_POINTS, currentField.getCustomfield_10002());

                if (currentIssueType != null && currentIssueType.getId().equals(IssueTypeEnum.STORY.getId())) {
                    trackOldIssues(completedStoriesFromClosedSprints, completedStoryPointsFromClosedSprints, previousSprintId, currentField);
                }
            }
        }
    }

    private void trackOldIssues(Map<Long, Integer> completedStoriesFromClosedSprints, Map<Long, Double> completedStoryPointsFromClosedSprints, Long previousSprintId, Field currentField) {
        if (currentField.getClosedSprints() != null && !currentField.getClosedSprints().isEmpty()) {
            for (Sprint closedSprint : currentField.getClosedSprints()) {
                if(closedSprint.getId().equals(previousSprintId)){
                    int storyCount = 1;
                    double storyPointCount = currentField.getCustomfield_10002()!= null ? Double.valueOf(currentField.getCustomfield_10002()) : 0;
                    if (completedStoriesFromClosedSprints.containsKey(closedSprint.getId())) {
                        storyCount += completedStoriesFromClosedSprints.get(closedSprint.getId());
                        storyPointCount += completedStoryPointsFromClosedSprints.get(closedSprint.getId());
                    }
                    completedStoriesFromClosedSprints.put(closedSprint.getId(), storyCount);
                    completedStoryPointsFromClosedSprints.put(closedSprint.getId(), storyPointCount);
                }
            }
        }
    }

    private void initiateIssueTypesMap() {
        issueTypesMap = new HashMap<>();
        for (IssueTypeEnum issueTypeEnum : IssueTypeEnum.values()){
            pointsMap = new HashMap<>();
            for (PointTypeEnum pointTypeEnum : PointTypeEnum.values()){
                pointsMap.put(pointTypeEnum, 0.0);
            }
            issueTypesMap.put(issueTypeEnum.getId(), pointsMap);
        }
    }

    private void incrementPointValue(IssueType issueType, PointTypeEnum pointTypeEnum, String stringValue){
        log.trace(issueType.getName() + " pte: " + pointTypeEnum + " count " + stringValue);
        double pointValue = 0;
        try{
            pointValue = Double.valueOf(stringValue);
        } catch (NullPointerException npe) {
            log.debug("issueType= " + issueType + ", pointTypeEnum= " + pointTypeEnum + " was null");
        }

        pointsMap = issueTypesMap.get(issueType.getId());
        if (pointsMap == null) pointsMap = new HashMap<>();
        if (pointsMap.containsKey(pointTypeEnum)){
            pointValue += pointsMap.get(pointTypeEnum);
        }
        pointsMap.put(pointTypeEnum, pointValue);

        issueTypesMap.put(issueType.getId(), pointsMap);
    }

    private int getPointTypeTotal(Map<Long, Map<PointTypeEnum,Double>> issueTypeMap, PointTypeEnum pointTypeEnum) {
        int pointTypeTotal = 0;
        for (Map<PointTypeEnum,Double> pointTypeEnumMap : issueTypeMap.values()) {
            if (pointTypeEnumMap.containsKey(pointTypeEnum)) {
                pointTypeTotal += pointTypeEnumMap.get(pointTypeEnum);
            }
        }
        return pointTypeTotal;
    }
}
