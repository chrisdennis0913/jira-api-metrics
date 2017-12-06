package gov.uscis.vis.api.factory;

import gov.uscis.vis.api.IssueTypeEnum;
import gov.uscis.vis.api.utils.CompletedCalculator;
import gov.uscis.vis.api.utils.ForecastCalculator;
import gov.uscis.vis.api.utils.MetricsCalculator;
import gov.uscis.vis.api.utils.RatioCalculator;

public class CalculatorFactory {

  public MetricsCalculator create(CalculatorEnum calculatorEnum){
    switch (calculatorEnum) {
      case FORECAST:
        return new ForecastCalculator(new IssueTypeEnum[]{IssueTypeEnum.STORY});
      case COMPLETED:
        return new CompletedCalculator();
      case RATIO:
        return new RatioCalculator();
    }
    return null;
  }
}
