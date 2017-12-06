package gov.uscis.vis.api.service;

import gov.uscis.vis.api.IssueTypeEnum;
import gov.uscis.vis.api.PointTypeEnum;
import gov.uscis.vis.api.models.Field;
import gov.uscis.vis.api.models.Issue;
import gov.uscis.vis.api.models.IssueList;
import gov.uscis.vis.api.models.IssueType;
import gov.uscis.vis.api.models.SprintList;
import gov.uscis.vis.api.models.StateEnum;
import gov.uscis.vis.api.models.best.SprintMetricsDto;
import gov.uscis.vis.api.utils.IssueUtils;
import gov.uscis.vis.api.utils.MetricsCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cedennis on 3/10/17.
 */
@Service("bestMetricsService")
public class BestMetricsServiceImpl implements BestMetricsService {
    private static final Logger log = LoggerFactory.getLogger(BestMetricsServiceImpl.class);

    private Map<Long, Map<PointTypeEnum, Double>> issueTypesMap; // issue type
    private Map<PointTypeEnum, Double> pointsMap; //completed vs total

    private StoryService storyService; //everify board = 722, save mod =1332, vdm board = 853

    @Override
    public List<SprintMetricsDto> calculateMetrics(int boardId, int sprints, MetricsCalculator metricsCalculator){
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
        extractIssuesFromIssueListByType(issueList, completedStoriesFromClosedSprints,
            completedStoryPointsFromClosedSprints, previousSprintId);

        return metricsCalculator.calculate(boardId, sprints, latestCompletedSprintId, issueTypesMap, closedSprintList);
    }

    private void extractIssuesFromIssueListByType(IssueList issueList,
                                                  Map<Long, Integer> completedStoriesFromClosedSprints,
                                                  Map<Long, Double> completedStoryPointsFromClosedSprints,
                                                  Long previousSprintId) {
        for (Issue issue : issueList.getIssues()){
            Field currentField = issue.getFields();
            IssueType currentIssueType = currentField.getIssuetype();
            incrementPointValue(currentIssueType, PointTypeEnum.TOTAL_STORIES, "1");
            incrementPointValue(currentIssueType, PointTypeEnum.TOTAL_POINTS, currentField.getCustomfield_10002());
            if (currentField.getStatus() != null
                && currentField.getStatus().getId().equals(10018L)) { //status id for DONE is 10018L
                incrementPointValue(currentIssueType, PointTypeEnum.COMPLETED_STORIES, "1");
                incrementPointValue(currentIssueType, PointTypeEnum.COMPLETED_POINTS,
                    currentField.getCustomfield_10002());

                if (currentIssueType != null && currentIssueType.getId().equals(IssueTypeEnum.STORY.getId())) {
                    IssueUtils.trackOldIssues(completedStoriesFromClosedSprints, completedStoryPointsFromClosedSprints,
                        previousSprintId, currentField);
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
}
