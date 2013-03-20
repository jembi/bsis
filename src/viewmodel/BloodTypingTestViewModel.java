package viewmodel;

import model.bloodtyping.BloodTestDataType;
import model.bloodtyping.BloodTypingTest;

public class BloodTypingTestViewModel {

  private BloodTypingTest rawBloodTest;

  public BloodTypingTestViewModel(BloodTypingTest rawBloodTest) {
    this.rawBloodTest = rawBloodTest;
  }

  public BloodTypingTest getRawBloodTest() {
    return rawBloodTest;
  }

  public void setRawBloodTest(BloodTypingTest rawBloodTest) {
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
