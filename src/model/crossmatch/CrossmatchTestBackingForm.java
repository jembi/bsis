package model.crossmatch;

import java.text.ParseException;
import java.util.Date;

import javax.validation.Valid;

import model.CustomDateFormatter;
import model.product.Product;
import model.request.Request;
import model.user.User;

public class CrossmatchTestBackingForm {

  @Valid
  private CrossmatchTest crossmatchTest;

  private String crossmatchTestDate;

  public CrossmatchTestBackingForm() {
    crossmatchTest = new CrossmatchTest();
  }

  public Date getLastUpdated() {
    return crossmatchTest.getLastUpdated();
  }

  public Date getCreatedDate() {
    return crossmatchTest.getCreatedDate();
  }

  public User getCreatedBy() {
    return crossmatchTest.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return crossmatchTest.getLastUpdatedBy();
  }

  public void setLastUpdated(Date lastUpdated) {
    crossmatchTest.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    crossmatchTest.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    crossmatchTest.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    crossmatchTest.setLastUpdatedBy(lastUpdatedBy);
  }

  public Long getId() {
    return crossmatchTest.getId();
  }

  public void setId(Long id) {
    crossmatchTest.setId(id);
  }

  public Request getForRequest() {
    return crossmatchTest.getForRequest();
  }

  public void setForRequest(Request forRequest) {
    crossmatchTest.setForRequest(forRequest);
  }

  public String getProductNumber() {
    if (crossmatchTest == null || crossmatchTest.getTestedProduct() == null ||
        crossmatchTest.getTestedProduct().getProductNumber() == null
       )
      return "";
    return crossmatchTest.getTestedProduct().getProductNumber();
  }

  public void setProductNumber(String productNumber) {
    Product product = new Product();
    product.setProductNumber(productNumber);
    crossmatchTest.setTestedProduct(product);
  }


  public Product getTestedProduct() {
    return crossmatchTest.getTestedProduct();
  }

  public void setTestedProduct(Product testedProduct) {
    crossmatchTest.setTestedProduct(testedProduct);
  }

  public Boolean getTransfusedBefore() {
    return crossmatchTest.getTransfusedBefore();
  }

  public void setTransfusedBefore(Boolean transfusedBefore) {
    crossmatchTest.setTransfusedBefore(transfusedBefore);
  }

  public String getCompatibilityResult() {
    if (crossmatchTest.getCompatibilityResult() == null)
      return "";
    else
      return crossmatchTest.getCompatibilityResult().toString();
  }

  public void setCompatibilityResult(String compatibilityResult) {
    crossmatchTest.setCompatibilityResult(CompatibilityResult.lookup(compatibilityResult));
  }

  public String getCrossmatchType() {
    CrossmatchType crossmatchType = crossmatchTest.getCrossmatchType();
    if (crossmatchType == null)
      return "";
    else
      return crossmatchType.getId().toString();
  }

  public void setCrossmatchType(String crossmatchTypeId) {
    if (crossmatchTypeId == null) {
      crossmatchTest.setCrossmatchType(null);
    }
    else {
      CrossmatchType ct = new CrossmatchType();
      ct.setId(Integer.parseInt(crossmatchTypeId));
      crossmatchTest.setCrossmatchType(ct);
    }
  }

  public String getTestedBy() {
    return crossmatchTest.getTestedBy();
  }

  public String getCrossmatchTestDate() {
    if (crossmatchTestDate != null)
      return crossmatchTestDate;
    if (crossmatchTest == null)
      return "";
    return CustomDateFormatter.getDateTimeString(crossmatchTest.getCrossmatchTestDate());
  }

  public void setTestedBy(String testedBy) {
    crossmatchTest.setTestedBy(testedBy);
  }

  public void setCrossmatchTestDate(String crossmatchTestDate) {
    this.crossmatchTestDate = crossmatchTestDate;
    try {
      crossmatchTest.setCrossmatchTestDate(CustomDateFormatter.getDateTimeFromString(crossmatchTestDate));
    } catch (ParseException ex) {
      ex.printStackTrace();
      crossmatchTest.setCrossmatchTestDate(null);
    }
  }

  public String getNotes() {
    return crossmatchTest.getNotes();
  }

  public void setNotes(String notes) {
    crossmatchTest.setNotes(notes);
  }

  public CrossmatchTest getCrossmatchTest() {
    return crossmatchTest;
  }

  public void setCrossmatchTest(CrossmatchTest crossmatchTest) {
    this.crossmatchTest = crossmatchTest;
  }
}
