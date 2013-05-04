package backingform;

import java.util.Arrays;
import java.util.List;

public class RequestsReportBackingForm {

  private String dateRequestedFrom;
  private String dateRequestedTo;
  private String aggregationCriteria;
  private List<String> sites;
  private List<String> bloodGroups;

  public RequestsReportBackingForm() {
    sites = Arrays.asList(new String[0]);
    setBloodGroups(Arrays.asList(new String[0]));
  }

  public String getDateRequestedFrom() {
    return dateRequestedFrom;
  }

  public void setDateRequestedFrom(String dateRequestedFrom) {
    this.dateRequestedFrom = dateRequestedFrom;
  }

  public String getDateRequestedTo() {
    return dateRequestedTo;
  }

  public void setDateRequestedTo(String dateRequestedTo) {
    this.dateRequestedTo = dateRequestedTo;
  }

  public String getAggregationCriteria() {
    return aggregationCriteria;
  }

  public void setAggregationCriteria(String aggregationCriteria) {
    this.aggregationCriteria = aggregationCriteria;
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
