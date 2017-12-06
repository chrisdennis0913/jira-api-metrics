package gov.uscis.vis.api.utils;

import gov.uscis.vis.api.PointTypeEnum;
import gov.uscis.vis.api.models.Field;
import gov.uscis.vis.api.models.Sprint;

import java.util.Map;

public class IssueUtils {

  public static void trackOldIssues(Map<Long, Integer> completedStoriesFromClosedSprints,
                                    Map<Long, Double> completedStoryPointsFromClosedSprints, Long previousSprintId,
                                    Field currentField) {
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

  public static int getPointTypeTotal(Map<Long, Map<PointTypeEnum,Double>> issueTypeMap, PointTypeEnum pointTypeEnum) {
    int pointTypeTotal = 0;
    for (Map<PointTypeEnum, Double> pointTypeEnumMap : issueTypeMap.values()) {
      if (pointTypeEnumMap.containsKey(pointTypeEnum)) {
        pointTypeTotal += pointTypeEnumMap.get(pointTypeEnum);
      }
    }
    return pointTypeTotal;
  }
}
