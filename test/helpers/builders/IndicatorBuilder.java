package helpers.builders;

import model.location.Location;
import model.reporting.Cohort;
import model.reporting.Indicator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IndicatorBuilder extends AbstractBuilder<Indicator> {

  private Date startDate;
  private Date endDate;
  private Object value;
  private Location venue;
  private List<Cohort> cohorts;

  public static IndicatorBuilder anIndicator() {
    return new IndicatorBuilder();
  }

  public IndicatorBuilder withStartDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }

  public IndicatorBuilder withEndDate(Date endDate) {
    this.endDate = endDate;
    return this;
  }

  public IndicatorBuilder withValue(Object value) {
    this.value = value;
    return this;
  }

  public IndicatorBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  public IndicatorBuilder withCohort(Cohort cohort) {
    if (cohorts == null) {
      cohorts = new ArrayList<>();
    }
    cohorts.add(cohort);
    return this;
  }

  @Override
  public Indicator build() {
    Indicator indicator = new Indicator();
    indicator.setStartDate(startDate);
    indicator.setEndDate(endDate);
    indicator.setValue(value);
    indicator.setVenue(venue);
    indicator.setCohorts(cohorts);
    return indicator;
  }

}
