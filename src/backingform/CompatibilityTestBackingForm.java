package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.text.ParseException;
import java.util.Date;

import javax.validation.Valid;

import model.compatibility.CompatibilityResult;
import model.compatibility.CompatibilityTest;
import model.compatibility.CrossmatchType;
import model.product.Product;
import model.request.Request;
import model.user.User;
import utils.CustomDateFormatter;

public class CompatibilityTestBackingForm {

  @Valid
  private CompatibilityTest compatibilityTest;

  private String collectionNumber;

  private String compatiblityTestDate;

  public CompatibilityTestBackingForm() {
    compatibilityTest = new CompatibilityTest();
  }

  
  public Long getId() {
    return compatibilityTest.getId();
  }

  public void setId(Long id) {
    compatibilityTest.setId(id);
  }

  @JsonIgnore
  public Request getForRequest() {
    return compatibilityTest.getForRequest();
  }

  public void setForRequest(Request forRequest) {
    compatibilityTest.setForRequest(forRequest);
  }

  public String getRequestNumber() {
    if (compatibilityTest == null || compatibilityTest.getForRequest() == null ||
        compatibilityTest.getForRequest().getRequestNumber() == null
       )
      return "";
    return compatibilityTest.getForRequest().getRequestNumber();
  }

  public void setRequestNumber(String requestNumber) {
    Request request = new Request();
    request.setRequestNumber(requestNumber);
    compatibilityTest.setForRequest(request);
  }

  @JsonIgnore
  public Product getTestedProduct() {
    return compatibilityTest.getTestedProduct();
  }

  public void setTestedProduct(Product testedProduct) {
    compatibilityTest.setTestedProduct(testedProduct);
  }

  public Boolean getTransfusedBefore() {
    return compatibilityTest.getTransfusedBefore();
  }

  public void setTransfusedBefore(Boolean transfusedBefore) {
    compatibilityTest.setTransfusedBefore(transfusedBefore);
  }

  public String getCompatibilityResult() {
    if (compatibilityTest.getCompatibilityResult() == null)
      return "";
    else
      return compatibilityTest.getCompatibilityResult().toString();
  }

  public void setCompatibilityResult(String compatibilityResult) {
    compatibilityTest.setCompatibilityResult(CompatibilityResult.lookup(compatibilityResult));
  }

  public String getCrossmatchType() {
    CrossmatchType crossmatchType = compatibilityTest.getCrossmatchType();
    if (crossmatchType == null)
      return "";
    else
      return crossmatchType.getId().toString();
  }

  public void setCrossmatchType(String crossmatchTypeId) {
    if (crossmatchTypeId == null) {
      compatibilityTest.setCrossmatchType(null);
    }
    else {
      CrossmatchType ct = new CrossmatchType();
      ct.setId(Integer.parseInt(crossmatchTypeId));
      compatibilityTest.setCrossmatchType(ct);
    }
  }

  public String getTestedBy() {
    return compatibilityTest.getTestedBy();
  }

  public String getCompatibilityTestDate() {
    if (compatiblityTestDate != null)
      return compatiblityTestDate;
    if (compatibilityTest == null)
      return "";
    return CustomDateFormatter.getDateTimeString(compatibilityTest.getCompatibilityTestDate());
  }

  public void setTestedBy(String testedBy) {
    compatibilityTest.setTestedBy(testedBy);
  }

  public void setCompatibilityTestDate(String compatibilityTestDate) {
    this.compatiblityTestDate = compatibilityTestDate;
    try {
      compatibilityTest.setCompatibilityTestDate(CustomDateFormatter.getDateTimeFromString(compatibilityTestDate));
    } catch (ParseException ex) {
      ex.printStackTrace();
      compatibilityTest.setCompatibilityTestDate(null);
    }
  }

  public String getNotes() {
    return compatibilityTest.getNotes();
  }

  public void setNotes(String notes) {
    compatibilityTest.setNotes(notes);
  }

  @JsonIgnore
  public CompatibilityTest getCompatibilityTest() {
    return compatibilityTest;
  }

  public void setCompatibilityTest(CompatibilityTest crossmatchTest) {
    this.compatibilityTest = crossmatchTest;
  }

  public void setCollectionNumber(String collectionNumber) {
    this.collectionNumber = collectionNumber;
  }

  public String getCollectionNumber() {
    return collectionNumber;
  }
}
