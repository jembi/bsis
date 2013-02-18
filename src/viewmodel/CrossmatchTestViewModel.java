package viewmodel;

import java.util.Date;

import javax.validation.Valid;

import model.CustomDateFormatter;
import model.crossmatch.CrossmatchTest;
import model.crossmatch.CrossmatchType;
import model.product.Product;
import model.request.Request;
import model.user.User;

public class CrossmatchTestViewModel {

  @Valid
  private CrossmatchTest crossmatchTest;

  public CrossmatchTestViewModel() {
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

  public Long getId() {
    return crossmatchTest.getId();
  }

  public Request getForRequest() {
    return crossmatchTest.getForRequest();
  }

  public String getProductNumber() {
    if (crossmatchTest == null || crossmatchTest.getTestedProduct() == null ||
        crossmatchTest.getTestedProduct().getProductNumber() == null
       )
      return "";
    return crossmatchTest.getTestedProduct().getProductNumber();
  }

  public Product getTestedProduct() {
    return crossmatchTest.getTestedProduct();
  }

  public Boolean getTransfusedBefore() {
    return crossmatchTest.getTransfusedBefore();
  }

  public String getCompatibilityResult() {
    if (crossmatchTest.getCompatibilityResult() == null)
      return "";
    else
      return crossmatchTest.getCompatibilityResult().toString();
  }

  public String getCrossmatchType() {
    CrossmatchType crossmatchType = crossmatchTest.getCrossmatchType();
    if (crossmatchType == null)
      return "";
    else
      return crossmatchType.getId().toString();
  }

  public String getTestedBy() {
    return crossmatchTest.getTestedBy();
  }

  public String getCrossmatchTestDate() {
    if (crossmatchTest == null)
      return "";
    return CustomDateFormatter.getDateTimeString(crossmatchTest.getCrossmatchTestDate());
  }

  public String getNotes() {
    return crossmatchTest.getNotes();
  }
}
