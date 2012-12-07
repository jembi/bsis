package model.request;

import java.util.Arrays;
import java.util.List;

public class FindRequestBackingForm {

  private List<String> requestSites;
  private List<String> productTypes;

  private String requestedAfter;
  private String requiredBy;

  public FindRequestBackingForm() {
    requestSites = Arrays.asList(new String[0]);
    productTypes = Arrays.asList(new String[0]);
  }

  public List<String> getRequestSites() {
    return requestSites;
  }

  public List<String> getProductTypes() {
    return productTypes;
  }

  public String getRequestedAfter() {
    return requestedAfter;
  }

  public String getRequiredBy() {
    return requiredBy;
  }

  public void setRequestSites(List<String> requestSites) {
    this.requestSites = requestSites;
  }

  public void setProductTypes(List<String> productTypes) {
    this.productTypes = productTypes;
  }

  public void setRequestedAfter(String requestedAfter) {
    this.requestedAfter = requestedAfter;
  }

  public void setRequiredBy(String requiredBy) {
    this.requiredBy = requiredBy;
  }

}