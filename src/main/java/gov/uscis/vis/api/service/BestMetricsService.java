package gov.uscis.vis.api.service;

import gov.uscis.vis.api.models.best.SprintMetricsDto;
import gov.uscis.vis.api.utils.MetricsCalculator;

import java.util.List;

/**
 * Created by cedennis on 3/10/17.
 */
public interface BestMetricsService {
    List<SprintMetricsDto> calculateMetrics(int boardId, int sprints, MetricsCalculator metricsCalculator);
}
