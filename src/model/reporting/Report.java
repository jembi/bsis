package model.reporting;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import utils.DateTimeSerialiser;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Report {

  private Date startDate;
  private Date endDate;
  private List<Indicator> indicators;

  @JsonSerialize(using = DateTimeSerialiser.class)
  private Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  private Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  private List<Indicator> getIndicators() {
    return indicators;
  }

  public void setIndicators(List<Indicator> indicators) {
    this.indicators = indicators;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Report)) {
      return false;
    }

    Report other = (Report) obj;

    return Objects.equals(getStartDate(), other.getStartDate()) &&
            Objects.equals(getEndDate(), other.getEndDate()) &&
            Objects.equals(getIndicators(), other.getIndicators());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getStartDate(), getEndDate(), getIndicators());
  }

}
