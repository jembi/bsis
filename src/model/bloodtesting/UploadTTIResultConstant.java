package model.bloodtesting;

public class UploadTTIResultConstant {

	public static final String UPLOAD_DIRECTORY = "/bsis";
	public static final String SUCCESS_ROW = " Rows Successfully Imported.";
	public static final String FAIL_ROW = " Rows Failed to Import.";
	public static final String FILE_SPLIT = "\\.";
	public static final String FILE_EXTENTION = "tsv";
	public static final String DATE_FORMAT = "dd.MM.yyyy hh:mm";
	public static final String TSV_FILE_EXTENTION = ".tsv";
	public static final String MESSAGE1 = "Please Select File";
	public static final String MESSAGE2 = "Please Select tsv File";

	// upload settings
	public static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
	public static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
	public static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
}
