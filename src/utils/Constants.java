package utils;

public class Constants {
	
	//role constants
	public static final String ROLE_ADMIN="Admin";
	public static final String ROLE_SUPER_USER = "Super User";
	public static final String ROLE_DONOR_SUPERVISOR = "Donor Clinic Supervisor";
	public static final String ROLE_DONOR_CLINIC_STAFF = "Donor Clinic Staff";
	public static final String ROLE_COMMUNICATION_STAFF = "Donor Communicatios Staff";
	public static final String ROLE_DONOR_COUNSELOR = "Donor Counselor" ;
	public static final String ROLE_DATA_ENTRY_CLERK = "Data Entry Clerk";
	public static final String ROLE_MEDICAL_OFFICER = "Medical Officer";
	public static final String ROLE_COMPONENT_SUPERVISOR = "Component Supervisor";
	public static final String ROLE_COMPONENT_SYAFF = "Component Staff";
	public static final String ROLE_TTI_TESTING_SUPERVISOR = "TTI Testing Supervisor";
	public static final String ROLE_TTIT_TESTING_STAFF = "TTI Testing Staff";
	public static final String ROLE_SEROLOGY_SUPERVISOR = "Serology Supervisor";
	public static final String ROLE_SEROLOGY_STAFF = "Serology Staff";
    public static final String ROLE_INVENTORY_STAFF = "Inventory Staff";
    public static final String ROLE_BLOOD_BANK_STAFF = "Blood Bank Staff";
    
    //Permission Constants
    
    //Donor Permissions
    public static final String PERM_ADD_DONOR="Add Donor";
    public static final String PERM_VIEW_DONOR = "View Donor";
    public static final String PERM_EDIT_DONOR	= "Edit Donor";
   	public static final	String PERM_VOID_DONOR = "Void Donor";
   	public static final String PERM_ADD_DEFERRAL = "Add Deferral";
   	public static final String PERM_VIEW_DEFERRAL="View Deferral";
   	public static final String 	PERM_EDIT_DEFERRAL = "Edit Deferral";
   	public static final	String PERM_VOID_DEFERRAL ="Void Deferral";
   	//Donation Permissions
  	public static final	String PERM_ADD_DONATION = "Add Donation";
    public static final	String PERM_VIEW_DONATION = "View Donation";
   	public static final	String PERM_EDIT_DONATION = "Edit Donation";
   	public static final String PERM_VOID_DONATION = "Void Donation";
   	//Mobile Clinic Permissions
   	public static final	String PERM_IMPORT_CLINIC_DATA = "Import Clinic Data";
    public static final	String PERM_EXPORT_CLINIC_DATA	="Export clinic Data";
    //Component Permissions
    public static final	String PERM_ADD_COMPONENT	="Add Component";
    public static final String PERM_VIEW_COMPONENT = "View Component";
    public static final String PERM_EDIT_COMPONENT = "Edit Component";
    public static final String PERM_VOID_COMPONENT	= "Void Component";
    //TTI Testing permissions
    public static final String PERM_ADD_TTL_OUTCOMES = "Add TTI Outcomes";
    public static final String PERM_EDIT_TTI_OUTCOMES = "Edit TTI Outcomes";
    public static final String StringPERM_VIEW_TTI_RESULTS = "View TTi Results";
    //Blood Typing Permissions
    public static final String StringPERM_EDIT_BLOOD_TYPING_OUTCOMES="Edit Blood Typing Outcomes";
	public static final String PERM_ADD_BLOOD_TYPING_OUTCOMES = "Add Blood Typing Outcomes";
    public static final String StringPERM_VIEW_BLOOD_TYPING_DOCUMENT = "View Blood Typing Outcomes";
    //Blood Bank Staff Permissions
    public static final String PERM_BLOOD_CROSS_MATCH_CHECK="Blood Cross Match Check";
    public static final String PERM_ISSUE_BLOOD = "Issue Blood";
    //Inventory Permissions
    public static final String PERM_COMPONET_LABELLING = "Component Labelling";
    public static final String PERM_ISSUE_Components = "Issue Components";
    //Discars Permissions
    public static final String PERM_DISCARD_COMPONENTS = "Discard Components";
    public static final String PERM_VIEW_DISCARDED_COMPONENTS="view discarded Components";
    //Reporting Permissions
    public static final String PERM_DONATIONS_REPORTING	="Donations Reporting";
    public static final String PERM_REQUESTS_REPORTING="Requests Reporting";
    public static final String PERM_TTI_REPORTING="	TTI Reporting";
    public static final	String PERM_COMPONENTS_ISSUED_REPORTING	="Components Issued Reporting";
    public static final String PERM_COMPONENTS_DISCARDED_REPORTING	="Components Discarded Reporting";
    //Admin and Super-User Permissions
    public static final String PERM_MANAGE_USERS = "Manage users";
    public static final	String PERM_MANAGE_ROLES = "Manage Roles";
    public static final String PERM_MANAGE_DONATION_SITES = "Manage Donation sites";
    public static final String PERM_MANAGE_DONATION_TYPES = "Manage Donation Types";
    public static final String PERM_MANAGE_DONATION_CATEGORIES	="Manage Donation Categories";
    public static final String PERM_MANAGE_COMPONENT_COMBINATIONS = "Manage Component Combinations";
    public static final String PERM_MANAGE_CROSS_MATCH_TYPES = "Manage Cross Match Types";
 	public static final String PERM_MANAGE_BLOOD_TYPING_ROLES = "Manage Blood Typing Rules";
    public static final String PERM_MANAGE_BLOOD_BAG_TYPES = "Manage Blood Bag Types";
    public static final String PERM_MANAGE_DISCARD_REASONS = "Manage Discard Reasons";
 	public static final String PERM_MANAGE_DONOR_DEFER_REASONS = "Manage Donor Defer Reasons";
    public static final String PERM_MANAGE_DONOR_CODES = "Manage Donor Codes";
    public static final String PERM_MANAGE_DIAGNOSES_CODES = "Manage Diagnoses Codes";
    public static final String PERM_MANAGE_LAB_SETUP = "Manage Lab Setup";
    public static final String PERM_MANAGE_DATA_SETUP = "Manage Data Setup";
    public static final String PERM_MANAGE_FORMS = "Manage Forms";
    public static final String PERM_MANAGE_BACKUP_DATA	="Manage Backup Data";
    public static final String PERM_MANAGE_BLOOD_TESTS	="Manage Blood Tests";
    
    

}
