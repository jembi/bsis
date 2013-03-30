package viewmodel;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestDataType;

public class BloodTestViewModel {

  private BloodTest rawBloodTest;

  public BloodTestViewModel(BloodTest rawBloodTest) {
    this.rawBloodTest = rawBloodTest;
  }

  public BloodTest getRawBloodTest() {
    return rawBloodTest;
  }

  public void setRawBloodTest(BloodTest rawBloodTest) {
    this.rawBloodTest = rawBloodTest;
  }

  public Integer getId() {
    return rawBloodTest.getId();
  }

  public String getTestNameShort() {
    return rawBloodTest.getTestNameShort();
  }

  public String getTestName() {
    return rawBloodTest.getTestName();
  }

  public BloodTestDataType getDataType() {
    return rawBloodTest.getDataType();
  }

  public String getValidResults() {
    return rawBloodTest.getValidResults();
  }

  public String getNegativeResults() {
    return rawBloodTest.getNegativeResults();
  }

  public Boolean getIsActive() {
    return rawBloodTest.getIsActive();
  }

  public Integer getRankInCategory() {
    return rawBloodTest.getRankInCategory();
  }
}
