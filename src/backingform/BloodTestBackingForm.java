package backingform;

import model.bloodtesting.BloodTest;

/**
 *
 * @author srikanth
 */
public class BloodTestBackingForm {

    private BloodTest bloodTest;

    private Integer numberOfConfirmatoryTests;

    private String bloodTestCategory;

    public BloodTestBackingForm() {
        bloodTest = new BloodTest();
    }

    public BloodTest getBloosTest() {
        return bloodTest;
    }

    public void setBloosTest(BloodTest bloodTest) {
        this.bloodTest = bloodTest;
    }

    public String getTestName() {
        return bloodTest.getTestName();
    }

    public String getTestNameShort() {
        return bloodTest.getTestNameShort();
    }

    public BloodTest getBloodTest() {
        return bloodTest;
    }

    public Integer getNumberOfConfirmatoryTests() {
        return numberOfConfirmatoryTests;
    }

    public void setBloodTest(BloodTest bloodTest) {
        this.bloodTest = bloodTest;
    }

    public void setNumberOfConfirmatoryTests(Integer numberOfConfirmatoryTests) {
        this.numberOfConfirmatoryTests = numberOfConfirmatoryTests;
    }

    public void setTestName(String testName) {
        bloodTest.setTestName(testName);
    }

    public void setTestNameShort(String testNameShort) {
        bloodTest.setTestNameShort(testNameShort);
    }

    public String getBloodTestCategory() {
        return bloodTestCategory;
    }

    public void setBloodTestCategory(String bloodTestCategory) {
        this.bloodTestCategory = bloodTestCategory;
    }

}
