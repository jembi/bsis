package helpers.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.location.Location;
import model.reporting.Cohort;
import model.reporting.DataValue;

public class DataValueBuilder extends AbstractBuilder<DataValue> {

  private Date startDate;
  private Date endDate;
  private Object value;
  private Location venue;
  private List<Cohort> cohorts;

  public DataValueBuilder withStartDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }

  public DataValueBuilder withEndDate(Date endDate) {
    this.endDate = endDate;
    return this;
  }

  public DataValueBuilder withValue(Object value) {
    this.value = value;
    return this;
  }

  public DataValueBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  public DataValueBuilder withCohort(Cohort cohort) {
    if (cohorts == null) {
      cohorts = new ArrayList<>();
    }
    cohorts.add(cohort);
    return this;
  }

  @Override
  public DataValue build() {
    DataValue dataValue = new DataValue();
    dataValue.setStartDate(startDate);
    dataValue.setEndDate(endDate);
    dataValue.setValue(value);
    dataValue.setVenue(venue);
    dataValue.setCohorts(cohorts);
    return dataValue;
  }

  public static DataValueBuilder aDataValue() {
    return new DataValueBuilder();
  }

}
