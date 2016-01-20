package helpers.builders;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestResult;

public class BloodTestResultBuilder extends AbstractEntityBuilder<BloodTestResult> {

  private Long id;
  private String result;
  private BloodTest bloodTest;

  public static BloodTestResultBuilder aBloodTestResult() {
    return new BloodTestResultBuilder();
  }

  public BloodTestResultBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public BloodTestResultBuilder withResult(String result) {
    this.result = result;
    return this;
  }

  public BloodTestResultBuilder withBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
    return this;
  }

  @Override
  public BloodTestResult build() {
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setId(id);
    bloodTestResult.setResult(result);
    bloodTestResult.setBloodTest(bloodTest);
    return bloodTestResult;
  }

}
