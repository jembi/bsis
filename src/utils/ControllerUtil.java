package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.RecordFieldsConfig;
import repository.DisplayNamesRepository;
import repository.RecordFieldsConfigRepository;
import repository.ReportConfigRepository;

public class ControllerUtil {

	private static Map<String, String> COLLECTION_DEFAULT_NAMES;
	private static Map<String, String> DONOR_DEFAULT_NAMES;
	private static Map<String, String> TEST_RESULT_DEFAULT_NAMES;
	private static Map<String, String> PRODUCT_DEFAULT_NAMES;
	private static Map<String, String> REQUEST_DEFAULT_NAMES;
	private static Map<String, String> ISSUE_DEFAULT_NAMES;
	private static Map<String, String> USAGE_DEFAULT_NAMES;
	private static Map<String, String> REPORTS_DEFAULT_NAMES;

	static {
		initDefaultCollectionFieldNames();
		initDefaultDonorFieldNames();
		initDefaultTestResultFieldNames();
		initDefaultProductFieldNames();
		initDefaultRequestFieldNames();
		initDefaultIssueFieldNames();
		initDefaultUsageFieldNames();
		initDefaultReportsFieldNames();
	}

	private static void initDefaultCollectionFieldNames() {
		COLLECTION_DEFAULT_NAMES = new HashMap<String, String>();
		COLLECTION_DEFAULT_NAMES.put("collectionNo", "Collection No.");
		COLLECTION_DEFAULT_NAMES.put("center", "Center");
		COLLECTION_DEFAULT_NAMES.put("site", "Site");
		COLLECTION_DEFAULT_NAMES.put("dateCollected", "Date Collected");
		COLLECTION_DEFAULT_NAMES.put("sampleNo", "Sample No.");
		COLLECTION_DEFAULT_NAMES.put("shippingNo", "Shipping No.");
		COLLECTION_DEFAULT_NAMES.put("donorNo", "Donor No.");
		COLLECTION_DEFAULT_NAMES.put("donorType", "Donor Type");
		COLLECTION_DEFAULT_NAMES.put("comment", "Comment");
		COLLECTION_DEFAULT_NAMES.put("bloodGroup", "Blood Group");
		COLLECTION_DEFAULT_NAMES.put("rhd", "RhD");
		COLLECTION_DEFAULT_NAMES.put("tips", "");
	}

	private static void initDefaultDonorFieldNames() {
		DONOR_DEFAULT_NAMES = new HashMap<String, String>();
		DONOR_DEFAULT_NAMES.put("donorID", "Donor ID");
		DONOR_DEFAULT_NAMES.put("firstName", "First Name");
		DONOR_DEFAULT_NAMES.put("lastName", "Last Name");
		DONOR_DEFAULT_NAMES.put("gender", "Gender");
		DONOR_DEFAULT_NAMES.put("bloodType", "Blood Type");
		DONOR_DEFAULT_NAMES.put("dob", "Date of Birth");
		DONOR_DEFAULT_NAMES.put("age", "Age");
		DONOR_DEFAULT_NAMES.put("address", "Address");
		DONOR_DEFAULT_NAMES.put("tips", "");
	}

	private static void initDefaultTestResultFieldNames() {
		TEST_RESULT_DEFAULT_NAMES = new HashMap<String, String>();
		TEST_RESULT_DEFAULT_NAMES.put("collectionNo", "Collection No.");
		TEST_RESULT_DEFAULT_NAMES.put("dateCollected", "Date Collected");
		TEST_RESULT_DEFAULT_NAMES.put("dateTested", "Date Tested");
		TEST_RESULT_DEFAULT_NAMES.put("hiv", "HIV");
		TEST_RESULT_DEFAULT_NAMES.put("hbv", "HBV");
		TEST_RESULT_DEFAULT_NAMES.put("hcv", "HCV");
		TEST_RESULT_DEFAULT_NAMES.put("syphilis", "Syphilis");
		TEST_RESULT_DEFAULT_NAMES.put("abo", "ABO");
		TEST_RESULT_DEFAULT_NAMES.put("rhd", "RhD");
		TEST_RESULT_DEFAULT_NAMES.put("bloodGroup", "Blood Group");
		TEST_RESULT_DEFAULT_NAMES.put("comment", "Comment");
		TEST_RESULT_DEFAULT_NAMES.put("tips", "");
	}

	private static void initDefaultProductFieldNames() {
		PRODUCT_DEFAULT_NAMES = new HashMap<String, String>();
		PRODUCT_DEFAULT_NAMES.put("productNo", "Product No.");
		PRODUCT_DEFAULT_NAMES.put("collectionNo", "Collection No.");
		PRODUCT_DEFAULT_NAMES.put("dateCollected", "Date Collected");
		PRODUCT_DEFAULT_NAMES.put("productType", "Product Type");
		PRODUCT_DEFAULT_NAMES.put("bloodGroup", "Blood Group");
		PRODUCT_DEFAULT_NAMES.put("tips", "");
	}

	private static void initDefaultRequestFieldNames() {
		REQUEST_DEFAULT_NAMES = new HashMap<String, String>();
		REQUEST_DEFAULT_NAMES.put("requestNo", "Request No.");
		REQUEST_DEFAULT_NAMES.put("requestDate", "Request Date");
		REQUEST_DEFAULT_NAMES.put("requiredDate", "Required Date");
		REQUEST_DEFAULT_NAMES.put("site", "Site");
		REQUEST_DEFAULT_NAMES.put("productType", "Product Type");
		REQUEST_DEFAULT_NAMES.put("abo", "ABO");
		REQUEST_DEFAULT_NAMES.put("rhd", "RhD");
		REQUEST_DEFAULT_NAMES.put("bloodGroup", "Blood Group");
		REQUEST_DEFAULT_NAMES.put("quantity", "Quantity");
		REQUEST_DEFAULT_NAMES.put("comment", "Comment");
		REQUEST_DEFAULT_NAMES.put("status", "Status");
		REQUEST_DEFAULT_NAMES.put("tips", "");
	}

	private static void initDefaultIssueFieldNames() {
		ISSUE_DEFAULT_NAMES = new HashMap<String, String>();
		ISSUE_DEFAULT_NAMES.put("site", "Site");
		ISSUE_DEFAULT_NAMES.put("issueDate", "Issue Date");
		ISSUE_DEFAULT_NAMES.put("issue", "Issue");
		ISSUE_DEFAULT_NAMES.put("issueTips", "");
	}

	private static void initDefaultUsageFieldNames() {
		USAGE_DEFAULT_NAMES = new HashMap<String, String>();
		USAGE_DEFAULT_NAMES.put("productNo", "Product No.");
		USAGE_DEFAULT_NAMES.put("usageDate", "Usage Date");
		USAGE_DEFAULT_NAMES.put("hospital", "Hospital");
		USAGE_DEFAULT_NAMES.put("ward", "Ward");
		USAGE_DEFAULT_NAMES.put("useIndication", "Use Indication");
		USAGE_DEFAULT_NAMES.put("comment", "Comment");
		USAGE_DEFAULT_NAMES.put("tips", "");
	}

	private static void initDefaultReportsFieldNames() {
		REPORTS_DEFAULT_NAMES = new HashMap<String, String>();
		REPORTS_DEFAULT_NAMES.put("inventorySummaryTips", "");
		REPORTS_DEFAULT_NAMES.put("inventoryDetailsTips", "");
		REPORTS_DEFAULT_NAMES.put("collectionsTips", "");
		REPORTS_DEFAULT_NAMES.put("testResultsTips", "");
		REPORTS_DEFAULT_NAMES.put("productsTips", "");

	}

	public static void addFieldsToDisplay(String recordType,
			Map<String, Object> model,
			RecordFieldsConfigRepository recordFieldsConfigRepository) {
		String fieldNames = recordFieldsConfigRepository.getRecordFieldsConfig(
				recordType).getFieldNames();
		String[] fields = fieldNames.split(",");
		for (String field : fields) {
			model.put("show" + field, true);
		}
	}

	public static void addDonorDisplayNamesToModel(Map<String, Object> model,
			DisplayNamesRepository displayNamesRepository) {
		addDisplayNamesToModel(model, displayNamesRepository, "donor",
				DONOR_DEFAULT_NAMES);
	}

	public static void addCollectionDisplayNamesToModel(
			Map<String, Object> model,
			DisplayNamesRepository displayNamesRepository) {
		addDisplayNamesToModel(model, displayNamesRepository, "collection",
				COLLECTION_DEFAULT_NAMES);
	}

	public static void addTestResultDisplayNamesToModel(
			Map<String, Object> model,
			DisplayNamesRepository displayNamesRepository) {
		addDisplayNamesToModel(model, displayNamesRepository, "testResults",
				TEST_RESULT_DEFAULT_NAMES);
	}

	public static void addProductDisplayNamesToModel(Map<String, Object> model,
			DisplayNamesRepository displayNamesRepository) {
		addDisplayNamesToModel(model, displayNamesRepository, "product",
				PRODUCT_DEFAULT_NAMES);
	}

	public static void addRequestDisplayNamesToModel(Map<String, Object> model,
			DisplayNamesRepository displayNamesRepository) {
		addDisplayNamesToModel(model, displayNamesRepository, "request",
				REQUEST_DEFAULT_NAMES);
	}

	public static void addIssueDisplayNamesToModel(Map<String, Object> model,
			DisplayNamesRepository displayNamesRepository) {
		addDisplayNamesToModel(model, displayNamesRepository, "issue",
				ISSUE_DEFAULT_NAMES);
	}

	public static void addUsageDisplayNamesToModel(Map<String, Object> model,
			DisplayNamesRepository displayNamesRepository) {
		addDisplayNamesToModel(model, displayNamesRepository, "usage",
				USAGE_DEFAULT_NAMES);
	}

	public static void addReportsDisplayNamesToModel(Map<String, Object> model,
			DisplayNamesRepository displayNamesRepository) {
		addDisplayNamesToModel(model, displayNamesRepository, "reports",
				REPORTS_DEFAULT_NAMES);

	}

	public static void addCollectionReportConfigFieldsToModel(
			Map<String, Object> model,
			ReportConfigRepository reportConfigRepository) {
		addReportConfigToModel(model, reportConfigRepository, "collection");
	}

	public static void addProductReportConfigFieldsToModel(
			Map<String, Object> model,
			ReportConfigRepository reportConfigRepository) {
		addReportConfigToModel(model, reportConfigRepository, "product");
	}

	public static void addTestResultsReportConfigFieldsToModel(
			Map<String, Object> model,
			ReportConfigRepository reportConfigRepository) {
		addReportConfigToModel(model, reportConfigRepository, "testResults");
	}

	private static void addReportConfigToModel(Map<String, Object> model,
			ReportConfigRepository reportConfigRepository, String reportType) {
		String fieldNames = reportConfigRepository.getReportConfig(reportType)
				.getFieldNames();
		String[] fields = fieldNames.split(",");
		for (String field : fields) {
			model.put(field + "FieldName", true);
		}
	}

	private static void addDisplayNamesToModel(Map<String, Object> model,
			DisplayNamesRepository displayNamesRepository, String recordType,
			Map<String, String> default_names) {
		String[] fields = displayNamesRepository.getDisplayName(recordType)
				.getFieldNames().split(",");
		for (String field : fields) {
			String[] pair = field.split(":");
			String displayName;
			if (pair.length == 1) {
				displayName = default_names.get(pair[0]);
			} else {
				displayName = pair[1];
			}
			model.put(pair[0] + "DisplayName", displayName);
		}
	}

	public static Long getOptionalParamValue(Long param,
			RecordFieldsConfig recordFields, String fieldName) {
		String fieldNames = recordFields.getFieldNames();
		if (fieldNames.contains(fieldName)) {
			return param;
		}
		return -9999L;
	}

	public static String getOptionalParamValue(String param,
			RecordFieldsConfig recordFields, String fieldName) {
		String fieldNames = recordFields.getFieldNames();
		if (fieldNames.contains(fieldName)) {
			return param;
		}
		return "NA";
	}

	public static Date getOptionalParamValue(Date param,
			RecordFieldsConfig recordFields, String fieldName) {
		String fieldNames = recordFields.getFieldNames();
		if (fieldNames.contains(fieldName)) {
			return param;
		}
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		try {
			String date = "01/01/0001";
			if (date.length() > 0) {
				return (Date) formatter.parse(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Integer getOptionalParamValue(Integer param,
			RecordFieldsConfig recordFields, String fieldName) {
		String fieldNames = recordFields.getFieldNames();
		if (fieldNames.contains(fieldName)) {
			return param;
		}
		return -9999;
	}

	public static Date getDate(String dateParam) {
		if (dateParam == null) {
			return null;
		}
		DateFormat formatter;
		formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date collectionDate = null;
		try {
			if (dateParam.length() > 0) {
				collectionDate = (Date) formatter.parse(dateParam);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return collectionDate;
	}
}
