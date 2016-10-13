package org.jembi.bsis.model.reporting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.jembi.bsis.utils.DateTimeSerialiser;
import org.jembi.bsis.viewmodel.LocationViewModel;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class DataValue {

  private String id;
  private Date startDate;
  private Date endDate;
  private Object value;
  private LocationViewModel location;
  private List<Cohort> cohorts;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public LocationViewModel getLocation() {
    return location;
  }

  public void setLocation(LocationViewModel location) {
    this.location = location;
  }

  public List<Cohort> getCohorts() {
    return cohorts;
  }

  public void setCohorts(List<Cohort> cohorts) {
    this.cohorts = cohorts;
  }

  public void addCohort(Cohort cohort) {
    if (cohorts == null) {
      cohorts = new ArrayList<>();
    }
    cohorts.add(cohort);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof DataValue)) {
      return false;
    }

    DataValue other = (DataValue) obj;

    return Objects.equals(getStartDate(), other.getStartDate()) &&
        Objects.equals(getEndDate(), other.getEndDate()) &&
        Objects.equals(getValue(), other.getValue()) &&
        Objects.equals(getLocation(), other.getLocation()) &&
        Objects.equals(getCohorts(), other.getCohorts()) &&
        Objects.equals(getId(), other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getStartDate(), getEndDate(), getValue(), getLocation(), getCohorts());
  }

}
