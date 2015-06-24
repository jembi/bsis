package viewmodel;

import java.util.Date;
import model.bloodtesting.BloodTestResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import viewmodel.BloodTestViewModel;
import utils.CustomDateFormatter;

public class BloodTestResultViewModel {
    
	@JsonIgnore
    private BloodTestResult testResult;
	
	public BloodTestResultViewModel(BloodTestResult testResult) {
        this.testResult = testResult;
    }

    public BloodTestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(BloodTestResult testResult) {
        this.testResult = testResult;
    }

    public Long getId(){
        return testResult.getId();
    }
    
    public BloodTestViewModel getBloodTest() {
	    return new BloodTestViewModel(testResult.getBloodTest());
	}
	
	public String getNotes() {
	    return testResult.getNotes();
	}
    
    public String getResult(){
        return testResult.getResult();
    }
    
    public Long getTestedOn() {
    	Date testedOn = testResult.getTestedOn();
        if (testedOn != null) {
          return CustomDateFormatter.getUnixTimestampLong(testedOn);
        } else {
          return null;
        }
	}
    
    public String getReagentLotNumber() {
		return testResult.getReagentLotNumber();
	}
    
}