/**
 * Reason - Not used anywhere #[209]
 *
package backingform;

import java.util.Arrays;
import java.util.List;

public class DiscardedProductsReportBackingForm {

  private String donationDateFrom;
  private String donationDateTo;
  private String aggregationCriteria;
  private List<String> centers;
  private List<String> sites;
  private List<String> bloodGroups;

  public DiscardedProductsReportBackingForm() {
    centers = Arrays.asList(new String[0]);
    sites = Arrays.asList(new String[0]);
    setBloodGroups(Arrays.asList(new String[0]));
  }

  public String getDonationDateFrom() {
    return donationDateFrom;
  }

  public void setDonationDateFrom(String donationDateFrom) {
    this.donationDateFrom = donationDateFrom;
  }

  public String getDonationDateTo() {
    return donationDateTo;
  }

  public void setDonationDateTo(String donationDateTo) {
    this.donationDateTo = donationDateTo;
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
*/