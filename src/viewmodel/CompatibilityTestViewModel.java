package viewmodel;

import model.compatibility.CompatibilityTest;
import model.compatibility.CrossmatchType;
import model.component.Component;
import model.request.Request;
import model.user.User;
import utils.CustomDateFormatter;

import javax.validation.Valid;
import java.util.Date;

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

  public Component getTestedComponent() {
    return compatibilityTest.getTestedComponent();
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
