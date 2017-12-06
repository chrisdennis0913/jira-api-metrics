package gov.uscis.vis.api.factory;

import gov.uscis.vis.api.IssueTypeEnum;
import gov.uscis.vis.api.utils.CompletedCalculator;
import gov.uscis.vis.api.utils.ForecastCalculator;
import gov.uscis.vis.api.utils.MetricsCalculator;
import gov.uscis.vis.api.utils.RatioCalculator;

public class ForecastCalculatorFactory extends CalculatorFactory{

  public MetricsCalculator create(CalculatorEnum calculatorEnum){
    switch (calculatorEnum) {
      case STORYFORECAST:
        return new ForecastCalculator(new IssueTypeEnum[]{IssueTypeEnum.STORY});
      case BUGFORECAST:
        return new ForecastCalculator(new IssueTypeEnum[]{IssueTypeEnum.BUG, IssueTypeEnum.PREVIEW_DEFECT,
            IssueTypeEnum.PRODUCTION_DEFECT});
    }
    return super.create(calculatorEnum);
  }
}
