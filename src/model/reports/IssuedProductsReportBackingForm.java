package model.reports;

import java.util.Arrays;
import java.util.List;

public class IssuedProductsReportBackingForm {

  private String dateIssuedFrom;
  private String dateIssuedTo;
  private String aggregationCriteria;
  private List<String> centers;
  private List<String> sites;
  private List<String> bloodGroups;

  public IssuedProductsReportBackingForm() {
    centers = Arrays.asList(new String[0]);
    sites = Arrays.asList(new String[0]);
    setBloodGroups(Arrays.asList(new String[0]));
  }

  public String getDateIssuedFrom() {
    return dateIssuedFrom;
  }

  public void setDateIssuedFrom(String dateIssuedFrom) {
    this.dateIssuedFrom = dateIssuedFrom;
  }

  public String getDateIssuedTo() {
    return dateIssuedTo;
  }

  public void setDateIssuedTo(String dateIssuedTo) {
    this.dateIssuedTo = dateIssuedTo;
  }

  public String getAggregationCriteria() {
    return aggregationCriteria;
  }

  public void setAggregationCriteria(String aggregationCriteria) {
    this.aggregationCriteria = aggregationCriteria;
  }

  public List<String> getCenters() {
    return centers;
  }

  public void setCenters(List<String> centers) {
    this.centers = centers;
  }

  public List<String> getSites() {
    return sites;
  }

  public void setSites(List<String> sites) {
    this.sites = sites;
  }

  public List<String> getBloodGroups() {
    return bloodGroups;
  }

  public void setBloodGroups(List<String> bloodGroups) {
    this.bloodGroups = bloodGroups;
  }
}

