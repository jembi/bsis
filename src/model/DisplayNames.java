package model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DisplayNames {
	@Id
	private String formType;
	private String fieldNames;

	public DisplayNames(String formType, String fieldNames) {

		this.formType = formType;
		this.fieldNames = fieldNames;
	}

	public DisplayNames() {
	}

	public String getReportType() {
		return formType;
	}

	public String getFieldNames() {
		return fieldNames;
	}

	public void setReportType(String reportType) {
		this.formType = reportType;
	}

	public void setFieldNames(String fieldNames) {
		this.fieldNames = fieldNames;
	}
}
