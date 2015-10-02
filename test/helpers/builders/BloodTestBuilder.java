package helpers.builders;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestType;

public class BloodTestBuilder extends AbstractEntityBuilder<BloodTest> {

    private BloodTestType bloodTestType;
    private String positiveResults;
    
    public BloodTestBuilder withBloodTestType(BloodTestType bloodTestType) {
        this.bloodTestType = bloodTestType;
        return this;
    }
    
    public BloodTestBuilder withPositiveResults(String positiveResults) {
        this.positiveResults = positiveResults;
        return this;
    }

    @Override
    public BloodTest build() {
        BloodTest bloodTest = new BloodTest();
        bloodTest.setBloodTestType(bloodTestType);
        bloodTest.setPositiveResults(positiveResults);
        return bloodTest;
    }
    
    public static BloodTestBuilder aBloodTest() {
        return new BloodTestBuilder();
    }

}
