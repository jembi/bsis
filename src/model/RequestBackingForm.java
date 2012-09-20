package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RequestBackingForm {

  private Request request;
  private List<String> sites;
  private List<String> productTypes;
  private List<String> statuses;
  private String dateRequestedFrom;
  private String dateRequestedTo;
  private String dateRequiredFrom;
  private String dateRequiredTo;

  public RequestBackingForm() {
    request = new Request();
  }

  public RequestBackingForm(Request request) {
    this.request = request;
  }

  public void copy(Request request) {
    request.copy(request);
  }

  public boolean equals(Object obj) {
    return request.equals(obj);
  }

  public void setRequestNumber(String requestNumber) {
    request.setRequestNumber(requestNumber);
  }

  public void setDateRequested(String dateRequested) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    try {
      request.setDateRequested(dateFormat.parse(dateRequested));
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void setDateRequired(String dateRequired) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    try {
      request.setDateRequired(dateFormat.parse(dateRequired));
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void setSiteId(Long siteId) {
    request.setSiteId(siteId);
  }

  public void setProductType(String productType) {
    request.setProductType(productType);
  }

  public void setRhd(String rhd) {
    request.setRhd(rhd);
  }

  public void setQuantity(Integer quantity) {
    request.setQuantity(quantity);
  }

  public void setComment(String comment) {
    request.setComment(comment);
  }

  public void setStatus(String status) {
    request.setStatus(status);
  }

  public void setIsDeleted(Boolean isDeleted) {
    request.setIsDeleted(isDeleted);
  }

  public Long getRequestId() {
    return request.getRequestId();
  }

  public String getRequestNumber() {
    return request.getRequestNumber();
  }

  public String getDateRequested() {
    Date dateRequested = request.getDateRequested();
    if (dateRequested == null)
      return null;
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    return dateFormat.format(dateRequested);
  }

  public String getDateRequired() {
    Date dateRequired = request.getDateRequired();
    if (dateRequired == null)
      return null;
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    return dateFormat.format(dateRequired);
  }

  public Long getSiteId() {
    return request.getSiteId();
  }

  public String getProductType() {
    return request.getProductType();
  }

  public String getAbo() {
    return request.getAbo();
  }

  public String getRhd() {
    return request.getRhd();
  }

  public Integer getQuantity() {
    return request.getQuantity();
  }

  public String getComments() {
    return request.getComments();
  }

  public String getStatus() {
    return request.getStatus();
  }

  public Boolean getDeleted() {
    return request.getDeleted();
  }

  public int hashCode() {
    return request.hashCode();
  }

  public void setAbo(String abo) {
    request.setAbo(abo);
  }

  public String toString() {
    return request.toString();
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

  public String getDateRequiredFrom() {
    return dateRequiredFrom;
  }

  public void setDateRequiredFrom(String dateRequiredFrom) {
    this.dateRequiredFrom = dateRequiredFrom;
  }

  public String getDateRequiredTo() {
    return dateRequiredTo;
  }

  public void setDateRequiredTo(String dateRequiredTo) {
    this.dateRequiredTo = dateRequiredTo;
  }

  public List<String> getSites() {
    return sites;
  }

  public void setSites(List<String> sites) {
    this.sites = sites;
  }

  public Request getRequest() {
    return request;
  }

  public void setRequest(Request request) {
    this.request = request;
  }

  public List<String> getProductTypes() {
    return productTypes;
  }

  public void setProductTypes(List<String> productTypes) {
    this.productTypes = productTypes;
  }

  public List<String> getStatuses() {
    return this.statuses;
  }

  public void setStatuses(List<String> statuses) {
    this.statuses = statuses;
  }
}
