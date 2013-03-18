package viewmodel;

import java.util.List;

import model.microtiterplate.MicrotiterPlate;
import model.rawbloodtest.RawBloodTest;
import model.rawbloodtest.RawBloodTestDataType;
import model.rawbloodtest.RawBloodTestGroup;

public class RawBloodTestViewModel {

  private RawBloodTest rawBloodTest;

  public RawBloodTestViewModel(RawBloodTest rawBloodTest) {
    this.rawBloodTest = rawBloodTest;
  }

  public RawBloodTest getRawBloodTest() {
    return rawBloodTest;
  }

  public void setRawBloodTest(RawBloodTest rawBloodTest) {
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

  public RawBloodTestDataType getDataType() {
    return rawBloodTest.getDataType();
  }

  public String getValidResults() {
    return rawBloodTest.getValidResults();
  }

  public String getNegativeResults() {
    return rawBloodTest.getNegativeResults();
  }

  public Boolean getNegativeRequiredForUse() {
    return rawBloodTest.getNegativeRequiredForUse();
  }

  public MicrotiterPlate getPlateUsedForTesting() {
    return rawBloodTest.getPlateUsedForTesting();
  }

  public List<RawBloodTestGroup> getRawBloodTestGroups() {
    return rawBloodTest.getRawBloodTestGroups();
  }

  public List<RawBloodTest> getTestsRequiredIfPositive() {
    return rawBloodTest.getTestsRequiredIfPositive();
  }

  public List<RawBloodTest> getTestsRequiredIfNegative() {
    return rawBloodTest.getTestsRequiredIfNegative();
  }

  public Boolean getIsConfidential() {
    return rawBloodTest.getIsConfidential();
  }

  public Boolean getIsActive() {
    return rawBloodTest.getIsActive();
  }

  public Integer getRankOnPlate() {
    return rawBloodTest.getRankOnPlate();
  }
}
