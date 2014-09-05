/**
 * issue - #209{Adapt Bsis To Expose rest Services}
 * Reason - Not required in later versions as get requests should not be bindied to be objects
 *
package backingform;

import java.util.List;

public class FindCollectedSampleBackingForm {

  private String collectionNumber;
  private String shippingNumber;
  private String sampleNumber;

  private List<String> centers;
  private List<String> sites;

  private List<String> bloodBagTypes;

  private Boolean includeTestedCollections;

  private String dateCollectedFrom;
  private String dateCollectedTo;

  public String getCollectionNumber() {
    return collectionNumber;
  }

  public String getShippingNumber() {
    return shippingNumber;
  }

  public String getSampleNumber() {
    return sampleNumber;
  }

  public List<String> getCollectionCenters() {
    return centers;
  }

  public String getDateCollectedFrom() {
    return dateCollectedFrom;
  }

  public String getDateCollectedTo() {
    return dateCollectedTo;
  }

  public void setCollectionNumber(String collectionNumber) {
    this.collectionNumber = collectionNumber;
  }

  public void setShippingNumber(String shippingNumber) {
    this.shippingNumber = shippingNumber;
  }

  public void setSampleNumber(String sampleNumber) {
    this.sampleNumber = sampleNumber;
  }

  public void setCollectionCenters(List<String> centers) {
    this.centers = centers;
  }

  public void setDateCollectedFrom(String dateCollectedFrom) {
    this.dateCollectedFrom = dateCollectedFrom;
  }

  public void setDateCollectedTo(String dateCollectedTo) {
    this.dateCollectedTo = dateCollectedTo;
  }

  public List<String> getCollectionSites() {
    return sites;
  }

  public void setCollectionSites(List<String> sites) {
    this.sites = sites;
  }

  public List<String> getBloodBagTypes() {
    return bloodBagTypes;
  }

  public void setBloodBagTypes(List<String> bloodBagTypes) {
    this.bloodBagTypes = bloodBagTypes;
  }

  public Boolean getIncludeTestedCollections() {
    return includeTestedCollections;
  }

  public void setIncludeTestedCollections(Boolean includeTestedCollections) {
    this.includeTestedCollections = includeTestedCollections;
  }
}
* */