package viewmodel;

import model.bloodtesting.BloodTestResult;


public class BloodTestResultViewModel {
    
    private BloodTestResult testResult;

    public BloodTestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(BloodTestResult testResult) {
        this.testResult = testResult;
    }

    public BloodTestResultViewModel(BloodTestResult testResult) {
        this.testResult = testResult;
    }
    
    public Long getId(){
        return testResult.getId();
    }
    
    public String getResult(){
        return testResult.getResult();
    }
    
    public String getBloodtestName(){
        return testResult.getBloodTest().getTestNameShort();
    }
    
}