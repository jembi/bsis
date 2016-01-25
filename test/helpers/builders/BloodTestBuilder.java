package helpers.builders;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestType;

public class BloodTestBuilder extends AbstractEntityBuilder<BloodTest> {

    private Long id;
    private BloodTestType bloodTestType;
    private String positiveResults;
    private String validResults;
    private Boolean isEmptyAllowed;
    private boolean flagComponentsForDiscard;

    public BloodTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }
    
    public BloodTestBuilder withBloodTestType(BloodTestType bloodTestType) {
        this.bloodTestType = bloodTestType;
        return this;
    }
    
    public BloodTestBuilder withPositiveResults(String positiveResults) {
        this.positiveResults = positiveResults;
        return this;
    }
    
    public BloodTestBuilder withValidResults(String validResults) {
      this.validResults = validResults;
      return this;
    }
    
    public BloodTestBuilder withIsEmptyAllowed(Boolean isEmptyAllowed) {
      this.isEmptyAllowed = isEmptyAllowed;
      return this;
    }
    
    public BloodTestBuilder withFlagComponentsForDiscard(boolean flagComponentsForDiscard) {
        this.flagComponentsForDiscard = flagComponentsForDiscard;
        return this;
    }

    @Override
    public BloodTest build() {
        BloodTest bloodTest = new BloodTest();
        bloodTest.setId(id);
        bloodTest.setBloodTestType(bloodTestType);
        bloodTest.setPositiveResults(positiveResults);
        bloodTest.setValidResults(validResults);
        bloodTest.setIsEmptyAllowed(isEmptyAllowed);
        bloodTest.setFlagComponentsForDiscard(flagComponentsForDiscard);
        return bloodTest;
    }
    
    public static BloodTestBuilder aBloodTest() {
        return new BloodTestBuilder();
    }

}
