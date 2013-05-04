package backingform;

import java.util.Arrays;
import java.util.List;

public class DiscardedProductsReportBackingForm {

  private String dateCollectedFrom;
  private String dateCollectedTo;
  private String aggregationCriteria;
  private List<String> centers;
  private List<String> sites;
  private List<String> bloodGroups;

  public DiscardedProductsReportBackingForm() {
    centers = Arrays.asList(new String[0]);
    sites = Arrays.asList(new String[0]);
    setBloodGroups(Arrays.asList(new String[0]));
  }

  public String getDateCollectedFrom() {
    return dateCollectedFrom;
  }

  public void setDateCollectedFrom(String dateCollectedFrom) {
    this.dateCollectedFrom = dateCollectedFrom;
  }

  public String getDateCollectedTo() {
    return dateCollectedTo;
  }

  public void setDateCollectedTo(String dateCollectedTo) {
    this.dateCollectedTo = dateCollectedTo;
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

