package helpers.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.reporting.Indicator;
import model.reporting.Report;

public class ReportBuilder extends AbstractBuilder<Report> {

  private Date startDate;
  private Date endDate;
  private List<Indicator> indicators;

  public ReportBuilder withStartDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }

  public ReportBuilder withEndDate(Date endDate) {
    this.endDate = endDate;
    return this;
  }

  public ReportBuilder withIndicator(Indicator indicator) {
    if (indicators == null) {
      indicators = new ArrayList<>();
    }
    indicators.add(indicator);
    return this;
  }

  public ReportBuilder withIndicators(List<Indicator> indicators) {
    this.indicators = indicators;
    return this;
  }

  @Override
  public Report build() {
    Report report = new Report();
    report.setStartDate(startDate);
    report.setEndDate(endDate);
    report.setIndicators(indicators);
    return report;
  }

  public static ReportBuilder aReport() {
    return new ReportBuilder();
  }

}
