package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import model.TestResult;

public class TestResultViewModel {

	private TestResult testResult;

	public TestResultViewModel(TestResult testResult) {

		this.testResult = testResult;
	}

	public String getCollectionNumber() {
		return testResult.getCollectionNumber();
	}

	public String getDateCollected() {
	  if (testResult.getDateCollected() == null)
	    return "";
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(testResult.getDateCollected());
	}

	public String getDateTested() {
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(testResult.getDateTested());
	}

	public String getTestResultId() {
		return getStringValue(testResult.getTestResultId());
	}

	public String getComments() {
		return testResult.getComments();
	}

	public String getHiv() {
		return testResult.getHiv();
	}

	public String getHbv() {
		return testResult.getHbv();
	}

	public String getHcv() {
		return testResult.getHcv();
	}

	public String getSyphilis() {
		return testResult.getSyphilis();
	}

	public String getAbo() {
		return testResult.getAbo();
	}

	public String getRhd() {
		return testResult.getRhd();
	}

	public Boolean getIsDeleted() {
		return testResult.getIsDeleted();
	}

	private String getStringValue(Long value) {
		return value == null ? "" : value.toString();
	}
}
