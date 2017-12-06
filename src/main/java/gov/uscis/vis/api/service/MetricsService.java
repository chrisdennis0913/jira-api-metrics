package gov.uscis.vis.api.service;

import gov.uscis.vis.api.models.MetricsDto;

import java.util.List;
import java.util.Map;

/**
 * Created by cedennis on 3/10/17.
 */
public interface MetricsService {
    Map<Integer, MetricsDto> analyzeBoard(List<Integer> boardList);
}
