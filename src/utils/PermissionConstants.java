package utils;

public class PermissionConstants {

	// Donor Permissions
	public static final String ADD_DONOR     = "Add Donor";
	public static final String VIEW_DONOR    = "View Donor";
	public static final String EDIT_DONOR    = "Edit Donor";
	public static final String VOID_DONOR    = "Void Donor";
	public static final String ADD_DEFERRAL  = "Add Deferral";
	public static final String VIEW_DEFERRAL = "View Deferral";
	public static final String EDIT_DEFERRAL = "Edit Deferral";
	public static final String VOID_DEFERRAL = "Void Deferral";
	public static final String ADD_DONOR_CODE  = "Add Donor Code";
	public static final String VIEW_DONOR_CODE = "View Donor Code";
	public static final String EDIT_DONOR_CODE = "Edit Donor Code";
	public static final String VOID_DONOR_CODE = "Void Donor Code";
	public static final String VIEW_DONOR_CODE_GROUP = "View Donor Code Group";
	
	// Donation Permissions
	public static final String ADD_DONATION  = "Add Donation";
	public static final String VIEW_DONATION = "View Donation";
	public static final String EDIT_DONATION = "Edit Donation";
	public static final String VOID_DONATION = "Void Donation";
	
	//Donation Batch Permissions
	public static final String ADD_DONATION_BATCH  = "Add Donation Batch";
	public static final String VIEW_DONATION_BATCH = "View Donation Batch";
	public static final String EDIT_DONATION_BATCH = "Edit Donation Batch";
	public static final String VOID_DONATION_BATCH = "Void Donation Batch";
	
	// Mobile Clinic Permissions
	public static final String IMPORT_CLINIC_DATA = "Import Clinic Data";
	public static final String EXPORT_CLINIC_DATA = "Export Clinic Data";
	
	// Component Permissions
	public static final String ADD_COMPONENT  = "Add Component";
	public static final String VIEW_COMPONENT = "View Component";
	public static final String EDIT_COMPONENT = "Edit Component";
	public static final String VOID_COMPONENT = "Void Component";
	
	//Request Permissions
	public static final String ADD_REQUEST  = "Add Request";
	public static final String EDIT_REQUEST  = "Edit Request";
	public static final String VIEW_REQUEST  = "View Request";
	public static final String VOID_REQUEST  = "Void Request";
	
	//Test Results Permissions
	public static final String ADD_TEST_OUTCOME  = "Add Test Outcome";
	public static final String EDIT_TEST_OUTCOME = "Edit Test Outcome";
	public static final String VIEW_TEST_OUTCOME = "View Test Outcome";
	public static final String VOID_TEST_OUTCOME  = "Void Test Outcome";
	
	// TTI Testing permissions
	public static final String ADD_TTI_OUTCOME  = "Add TTI Outcome";
	public static final String EDIT_TTI_OUTCOME = "Edit TTI Outcome";
	public static final String VIEW_TTI_OUTCOME  = "View TTI Outcome";
	
	// Blood Typing Permissions
	public static final String EDIT_BLOOD_TYPING_OUTCOMES = "Edit Blood Typing Outcome";
	public static final String ADD_BLOOD_TYPING_OUTCOME = "Add Blood Typing Outcome";
	public static final String VIEW_BLOOD_TYPING_OUTCOME = "View Blood Typing Outcome";
	
	// Test Batch Permissions
	public static final String ADD_TEST_BATCH  = "Add Test Batch";
	public static final String VIEW_TEST_BATCH = "View Test Batch";
	public static final String EDIT_TEST_BATCH = "Edit Test Batch";
	public static final String VOID_TEST_BATCH = "Void Test Batch";
	
	// Blood Bank Staff Permissions
	public static final String BLOOD_CROSS_MATCH_CHECK = "Blood Cross Match Check";
	public static final String ISSUE_COMPONENT= "Issue Component";
	
	// Inventory Permissions
	public static final String LABEL_COMPONENT = "Label Component";
	
	// Discard Permissions
	public static final String DISCARD_COMPONENT = "Discard Component";
	public static final String VIEW_DISCARDS = "View Discards";
	
	// Reporting Permissions
	public static final String DONATIONS_REPORTING = "Reporting - Donations";
	public static final String REQUESTS_REPORTING = "Reporting - Requests";
	public static final String TTI_REPORTING = "Reporting - TTI Testing";
	public static final String COMPONENTS_ISSUED_REPORTING = "Reporting - Components";
	public static final String COMPONENTS_DISCARDED_REPORTING = "Reporting - Discards";
	
	// Admin and Super-User Permissions
	public static final String MANAGE_USERS = "Manage Users";
	public static final String MANAGE_ROLES = "Manage Roles";
	public static final String MANAGE_DONATION_SITES = "Manage Donation Sites";
	public static final String MANAGE_DONATION_TYPES = "Manage Donation Types";
	public static final String MANAGE_DONATION_CATEGORIES = "Manage Donation Categories";
	public static final String MANAGE_COMPONENT_TYPES = "Manage Component Types";
	public static final String MANAGE_COMPONENT_COMBINATIONS = "Manage Component Combinations";
	public static final String MANAGE_CROSS_MATCH_TYPES = "Manage Cross Match Types";
	public static final String MANAGE_BLOOD_TYPING_RULES = "Manage Blood Typing Rules";
	public static final String MANAGE_PACK_TYPES = "Manage Pack Types";
	public static final String MANAGE_DISCARD_REASONS = "Manage Discard Reasons";
	public static final String MANAGE_DEFERRAL_REASONS = "Manage Deferral Reasons";
	public static final String MANAGE_DONOR_CODES = "Manage Donor Codes";
	public static final String MANAGE_DIAGNOSES_CODES = "Manage Diagnoses Codes";
	public static final String MANAGE_LAB_SETUP = "Manage Lab Setup";
	public static final String MANAGE_DATA_SETUP = "Manage Data Setup";
	public static final String MANAGE_FORMS = "Manage Forms";
	public static final String MANAGE_BACKUP_DATA = "Manage Backup Data";
	public static final String MANAGE_BLOOD_TESTS = "Manage Blood Tests";
	public static final String MANAGE_TIPS = "Manage Tips";
	public static final String MANAGE_REQUESTS = "Manage Requests";
	public static final String MANAGE_GENERAL_CONFIGS = "Manage General Configs";

	//Page Control Permissions
	public static final String VIEW_DONOR_INFORMATION = "View Donor Information";
	public static final String VIEW_DONATION_INFORMATION = "View Donation Information";
	public static final String VIEW_MOBILE_CLINIC_INFORMATION = "View Mobile Clinic Information";
	public static final String VIEW_COMPONENT_INFORMATION = "View Component Information";
	public static final String VIEW_TESTING_INFORMATION = "View Testing Information";
	public static final String VIEW_BLOOD_BANK_INFORMATION = "View Blood Bank Information";
	public static final String VIEW_INVENTORY_INFORMATION = "View Inventory Information";
	public static final String VIEW_DISCARD_INFORMATION = "View Discard Information";
	public static final String VIEW_REPORTING_INFORMATION = "View Reporting Information";
	public static final String VIEW_ADMIN_INFORMATION = "View Admin Information";
    
	//General Authenticated Permission
	public static final String AUTHENTICATED = "Authenticated";
	
	// Audit Permissions
	public static final String VIEW_AUDIT_LOG = "View Audit Log";
	
	// Post Donation Counselling Permissions
	public static final String VIEW_POST_DONATION_COUNSELLING_DONORS = "View Post Donation Counselling Donors";
	public static final String ADD_POST_DONATION_COUNSELLING = "Add Post Donation Counselling";
	public static final String EDIT_POST_DONATION_COUNSELLING = "Edit Post Donation Counselling";
	public static final String VIEW_POST_DONATION_COUNSELLING = "View Post Donation Counselling";
	public static final String VOID_POST_DONATION_COUNSELLING = "Void Post Donation Counselling";
	
	// Adverse Event Permissions
	public static final String ADD_ADVERSE_EVENT_TYPES = "Add Adverse Event Types";
	public static final String EDIT_ADVERSE_EVENT_TYPES = "Edit Adverse Event Types";
	public static final String VIEW_ADVERSE_EVENT_TYPES = "View Adverse Event Types";
	public static final String VOID_ADVERSE_EVENT_TYPES = "Void Adverse Event Types";

}
