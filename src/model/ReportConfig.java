package model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ReportConfig {
	@Id
	private String reportType;
	private String fieldNames;

	public ReportConfig(String reportType, String fieldNames) {

		this.reportType = reportType;
		this.fieldNames = fieldNames;
	}

	public ReportConfig() {
	}

	public String getReportType() {
		return reportType;
	}

	public String getFieldNames() {
		return fieldNames;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public void setFieldNames(String fieldNames) {
		this.fieldNames = fieldNames;
	}
}
