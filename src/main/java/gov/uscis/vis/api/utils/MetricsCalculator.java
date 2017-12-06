package gov.uscis.vis.api.utils;

import gov.uscis.vis.api.PointTypeEnum;
import gov.uscis.vis.api.models.SprintList;
import gov.uscis.vis.api.models.best.SprintMetricsDto;

import java.util.List;
import java.util.Map;

public interface MetricsCalculator {
  List<SprintMetricsDto> calculate(int boardId,
                                   int sprints,
                                   Long sprintId,
                                   Map<Long, Map<PointTypeEnum, Double>> issueTypesMap,
                                   SprintList closedSprintList);

}
