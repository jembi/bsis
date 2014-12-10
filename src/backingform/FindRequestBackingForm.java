/**
 *Reason - Not used anywhere $[209] 
 *
package backingform;

import java.util.Arrays;
import java.util.List;

public class FindRequestBackingForm {

  private String requestNumber;

  private List<String> requestSites;
  private List<String> productTypes;

  private String requestedAfter;
  private String requiredBy;

  private Boolean includeSatisfiedRequests;
  
  public FindRequestBackingForm() {
    requestSites = Arrays.asList(new String[0]);
    productTypes = Arrays.asList(new String[0]);
  }

  public String getRequestNumber() {
    return requestNumber;
  }
  
  public void setRequestNumber(String requestNumber) {
    this.requestNumber = requestNumber;
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

  public Boolean getIncludeSatisfiedRequests() {
    return includeSatisfiedRequests;
  }
  
  public void setIncludeSatisfiedRequests(Boolean includeSatisfiedRequests) {
    this.includeSatisfiedRequests = includeSatisfiedRequests;
  }
}
* */