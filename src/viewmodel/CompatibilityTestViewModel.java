package viewmodel;

import java.util.Date;

import javax.validation.Valid;

import model.CustomDateFormatter;
import model.compatibility.CompatibilityTest;
import model.compatibility.CrossmatchType;
import model.product.Product;
import model.request.Request;
import model.user.User;

public class CompatibilityTestViewModel {

  @Valid
  private CompatibilityTest compatibilityTest;

  public CompatibilityTestViewModel() {
    compatibilityTest = new CompatibilityTest();
  }

  public Date getLastUpdated() {
    return compatibilityTest.getLastUpdated();
  }

  public Date getCreatedDate() {
    return compatibilityTest.getCreatedDate();
  }

  public User getCreatedBy() {
    return compatibilityTest.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return compatibilityTest.getLastUpdatedBy();
  }

  public Long getId() {
    return compatibilityTest.getId();
  }

  public Request getForRequest() {
    return compatibilityTest.getForRequest();
  }

  public Product getTestedProduct() {
    return compatibilityTest.getTestedProduct();
  }

  public Boolean getTransfusedBefore() {
    return compatibilityTest.getTransfusedBefore();
  }

  public String getCompatibilityResult() {
    if (compatibilityTest.getCompatibilityResult() == null)
      return "";
    else
      return compatibilityTest.getCompatibilityResult().toString();
  }

  public String getCrossmatchType() {
    CrossmatchType crossmatchType = compatibilityTest.getCrossmatchType();
    if (crossmatchType == null)
      return "";
    else
      return crossmatchType.getId().toString();
  }

  public String getTestedBy() {
    return compatibilityTest.getTestedBy();
  }

  public String getCrossmatchTestDate() {
    if (compatibilityTest == null)
      return "";
    return CustomDateFormatter.getDateTimeString(compatibilityTest.getCompatibilityTestDate());
  }

  public String getNotes() {
    return compatibilityTest.getNotes();
  }
}
