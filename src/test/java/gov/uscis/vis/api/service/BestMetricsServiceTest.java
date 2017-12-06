package gov.uscis.vis.api.service;

import gov.uscis.vis.api.IssueTypeEnum;
import gov.uscis.vis.api.models.best.SprintMetricsDto;
import gov.uscis.vis.api.utils.CompletedCalculator;
import gov.uscis.vis.api.utils.ForecastCalculator;
import gov.uscis.vis.api.utils.MetricsCalculator;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    BestMetricsServiceImpl.class
})
public class BestMetricsServiceTest {
  @Autowired
  BestMetricsService bestMetricsService;

  public List<SprintMetricsDto> expectedMetrics(){
    SprintMetricsDto sprintMetricsDto = new SprintMetricsDto(1,2352,100,100);
    List<SprintMetricsDto> sprintMetricsDtoList = new ArrayList<>();
    sprintMetricsDtoList.add(sprintMetricsDto);
    return sprintMetricsDtoList;
  }

  @Test
  public void testCalculateMetricsForecastAccuracy(){

    MetricsCalculator metricsCalculator = new ForecastCalculator(new IssueTypeEnum[]{IssueTypeEnum.STORY});
    List<SprintMetricsDto> results = bestMetricsService.calculateMetrics(1, 1, metricsCalculator);

    Assertions.assertThat(results).isEqualTo(expectedMetrics());
  }

  @Test
  public void testCalculateMetricsCompleted(){
    List<SprintMetricsDto> expected = expectedMetrics();
    expected.get(0).setIssueMetric(12);
    expected.get(0).setStoryPointMetric(76);

    MetricsCalculator metricsCalculator = new CompletedCalculator();
    List<SprintMetricsDto> results = bestMetricsService.calculateMetrics(1, 1, metricsCalculator);

    Assertions.assertThat(results).isEqualTo(expected);
  }
}
