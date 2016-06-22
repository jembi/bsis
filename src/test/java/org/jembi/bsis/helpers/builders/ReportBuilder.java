package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;

public class ReportBuilder extends AbstractBuilder<Report> {

  private Date startDate;
  private Date endDate;
  private List<DataValue> dataValues;

  public ReportBuilder withStartDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }

  public ReportBuilder withEndDate(Date endDate) {
    this.endDate = endDate;
    return this;
  }

  public ReportBuilder withDataValue(DataValue dataValue) {
    if (dataValues == null) {
      dataValues = new ArrayList<>();
    }
    dataValues.add(dataValue);
    return this;
  }

  public ReportBuilder withDataValues(List<DataValue> dataValues) {
    this.dataValues = dataValues;
    return this;
  }

  @Override
  public Report build() {
    Report report = new Report();
    report.setStartDate(startDate);
    report.setEndDate(endDate);
    report.setDataValues(dataValues);
    return report;
  }

  public static ReportBuilder aReport() {
    return new ReportBuilder();
  }

}
