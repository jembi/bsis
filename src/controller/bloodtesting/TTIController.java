package controller.bloodtesting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestType;
import model.bloodtesting.TSVFileHeaderName;
import model.bloodtesting.UploadTTIResultConstant;
import model.donation.Donation;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import repository.DonationRepository;
import repository.GenericConfigRepository;
import repository.WellTypeRepository;
import repository.bloodtesting.BloodTestingRepository;
import utils.FileUploadUtils;
import utils.PermissionConstants;
import viewmodel.BloodTestViewModel;
import viewmodel.BloodTestingRuleResult;
import viewmodel.DonationViewModel;
import au.com.bytecode.opencsv.CSVReader;
import backingform.TestResultBackingForm;

import com.fasterxml.jackson.databind.ObjectMapper;

import controller.UtilController;

@RestController
@RequestMapping("ttitests")
public class TTIController {

	private static final Logger LOGGER = Logger.getLogger(TTIController.class);
	
	@Autowired
	private UtilController utilController;

	@Autowired
	private DonationRepository donationRepository;


	@Autowired
	private GenericConfigRepository genericConfigRepository;

	@Autowired
	private BloodTestingRepository bloodTestingRepository;

	@Autowired
	private WellTypeRepository wellTypeRepository;

	public TTIController() {
	}

	public static String getUrl(HttpServletRequest req) {
		String reqUrl = req.getRequestURL().toString();
		String queryString = req.getQueryString(); // d=789
		if (queryString != null) {
			reqUrl += "?" + queryString;
		}
		return reqUrl;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	@PreAuthorize("hasRole('"+PermissionConstants.ADD_TTI_OUTCOME+"')")
	public Map<String, Object> getTTIForm(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		//map.put("ttiFormFields", utilController.getFormFieldsForForm("TTIForm"));

		List<BloodTestViewModel> basicTTITests = getBasicTTITests();
		map.put("basicTTITests", basicTTITests);
		
		List<BloodTestViewModel> pendingTTITests = getConfirmatoryTTITests();
		map.put("pendingTTITests", pendingTTITests);

		return map;
	}

	public List<BloodTestViewModel> getBasicTTITests() {
		List<BloodTestViewModel> tests = new ArrayList<>();
		for (BloodTest rawBloodTest : bloodTestingRepository
				.getBloodTestsOfType(BloodTestType.BASIC_TTI)) {
			tests.add(new BloodTestViewModel(rawBloodTest));
		}
		return tests;
	}
	
	public List<BloodTestViewModel> getConfirmatoryTTITests() {
		List<BloodTestViewModel> tests = new ArrayList<>();
		for (BloodTest rawBloodTest : bloodTestingRepository
				.getBloodTestsOfType(BloodTestType.CONFIRMATORY_TTI)) {
			tests.add(new BloodTestViewModel(rawBloodTest));
		}
		return tests;
	}

	@RequestMapping(value = "/results/{donationId}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('"+PermissionConstants.VIEW_TTI_OUTCOME+"')")
	public Map<String, Object> showTTIResultsForDonation(HttpServletRequest request,
			@PathVariable String donationId) {
                Map<String, Object> map = new HashMap<>();
		donationId = donationId.trim();
		Long donationIdLong = Long.parseLong(donationId);
		Donation donation = donationRepository
				.findDonationById(donationIdLong);
		// using test status to find existing test results and determine pending
		// tests
		BloodTestingRuleResult ruleResult = bloodTestingRepository
				.getAllTestsStatusForDonation(donationIdLong);
		map.put("donation", new DonationViewModel(donation));
		map.put("overview", ruleResult);

		return map;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('"+PermissionConstants.ADD_TTI_OUTCOME+"')")
	@RequestMapping(value = "/results/additional", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> saveAdditionalTTITests(
			@RequestBody TestResultBackingForm form) {

		HttpStatus httpStatus = HttpStatus.CREATED;
                Map<String, Object> m = new HashMap<>();

		
			Map<Long, Map<Long, String>> ttiTestResultsMap = new HashMap<>();
			Map<Long, String> saveTestsDataWithLong = new HashMap<>();
			ObjectMapper mapper = new ObjectMapper();
			Map<Long, String> saveTestsData = form.getTestResults();
			for (Long testIdStr : saveTestsData.keySet()) {
				saveTestsDataWithLong.put(testIdStr,
						saveTestsData.get(testIdStr));
			}
                        Donation donation = 
                                donationRepository.verifyDonationIdentificationNumber(form.getDonationIdentificationNumber());
			ttiTestResultsMap.put(donation.getId(),
					saveTestsDataWithLong);
			Map<String, Object> results = bloodTestingRepository
					.saveBloodTestingResults(ttiTestResultsMap, true);
			Map<String, Object> errorMap = (Map<String, Object>) results
					.get("errors");
			if (errorMap != null && !errorMap.isEmpty())
				httpStatus = HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(m, httpStatus);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "results/onplate", method = RequestMethod.POST)
	@PreAuthorize("hasRole('"+PermissionConstants.ADD_TTI_OUTCOME+"')")
	public Map<String, Object> saveTTIResultsOnPlate(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "ttiTestId") Long ttiTestId,
			@RequestParam(value = "ttiResults") String ttiResults) {

		Map<String, Object> map = new HashMap<>();

		ObjectMapper mapper = new ObjectMapper();
		boolean success = false;
		try {
			Map<String, Map<String, Object>> ttiResultsMap = mapper.readValue(
					ttiResults, HashMap.class);
			Map<String, Object> results = bloodTestingRepository
					.saveTTIResultsOnPlate(ttiResultsMap, ttiTestId);
			if (results.get("errorsFound").equals(false))
				success = true;

			map.put("errorsByWellNumber",
					results.get("errorsByWellNumber"));
			map.put("errorsByWellNumberAsJSON", mapper
					.writeValueAsString(results.get("errorsByWellNumber")));
			map.put("errorsByWellNumber",
					results.get("errorsByWellNumber"));
			map.put("errorsByWellNumberAsJSON", mapper
					.writeValueAsString(results.get("errorsByWellNumber")));
			map.put("donations", results.get("donations"));
			map.put("bloodTestingResults",
					results.get("bloodTestingResults"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage() + e.getStackTrace());
		}

		map.put("success", success);
		if (!success) {
			map.put("errorMessage",
					"Please correct the errors on the highlighted wells before proceeding.");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		map.put("plate", bloodTestingRepository.getPlate("tti"));
		map.put("ttiTestId", ttiTestId);
		map.put("ttiTestResults", ttiResults);
		map.put("ttiTest",
				bloodTestingRepository.findBloodTestById(ttiTestId));
		map.put("ttiConfig",
				genericConfigRepository.getConfigProperties("ttiWells"));
		map.put("allWellTypes", wellTypeRepository.getAllWellTypes());

		return map;
	}

	/**
         * 
         *  
         *
        @RequestMapping(value = "/uploadTTIResultsFormGenerator", method = RequestMethod.GET)
	@PreAuthorize("hasRole('"+PermissionConstants.ADD_TTI_OUTCOME+"')")
	public ModelAndView uploadTTIResultsFormGenerator(HttpServletRequest request) {
		ModelAndView map = new ModelAndView("bloodtesting/uploadTTIResults");
		return map;
	}
        * */

	@RequestMapping(value = "results/upload", method = RequestMethod.POST)
	@PreAuthorize("hasRole('"+PermissionConstants.ADD_TTI_OUTCOME+"')")
	public ResponseEntity<Map<String, Object>> uploadTTIResultsGenerator(
			MultipartHttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {

		HttpStatus httpStatus = HttpStatus.CREATED;
                Map<String, Object> map = new HashMap<>();
		MultipartFile tsvFile = null;
		String fileName, uploadPath;
		boolean success = true;
		
		try{
			Iterator<String> iterator = request.getFileNames();
			if (!iterator.hasNext()) {
				map.put("errorMessage", UploadTTIResultConstant.MESSAGE1);
				success = false;
				map.put("success", success);
				return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
			}
			if (iterator.hasNext()) {
				tsvFile = request.getFile(iterator.next());
			}
					
			fileName = tsvFile.getOriginalFilename();			
			String getFullRealPath = request.getSession().getServletContext().getRealPath("/");
		  String[] path=getFullRealPath.split(".metadata");
		  uploadPath = path[0];
		  String[] tsvFilestr;
	
			tsvFilestr = tsvFile.getOriginalFilename()
					.split(UploadTTIResultConstant.FILE_SPLIT);
			if (StringUtils.isBlank(tsvFilestr.toString())	|| 
					!tsvFilestr[1].equals(UploadTTIResultConstant.FILE_EXTENTION)) {
				map.put("errorMessage",UploadTTIResultConstant.MESSAGE2);
				success = false;
				map.put("success", success);
				return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
			}
			
			String fileWithExt= FileUploadUtils.splitFilePath(fileName);
			writeTSVFile(fileWithExt, uploadPath, tsvFile);
			String file = uploadPath + fileWithExt;
			readTSVToDB(request, map, tsvFilestr, file);
			map.put("success", success);
		}	
		catch (Exception ex){
			ex.printStackTrace();
			success = false;
			map.put("success", success);
                        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<>(map, httpStatus);

	}

	private void readTSVToDB(MultipartHttpServletRequest request,
			Map<String, Object> map, String[] tsvFilestr, String file)
			throws IOException, ParseException {
		CSVReader csvReader;
		String successRows;
		String failedRows;
		if (StringUtils.isNotBlank(tsvFilestr.toString())) {

			try {
				csvReader = new CSVReader(new FileReader(file), '\t', '\'', 1);
				SimpleDateFormat formatter = new SimpleDateFormat(
						UploadTTIResultConstant.DATE_FORMAT);
				String[] next = null;
				List<TSVFileHeaderName> tSVFileHeaderNameList = new ArrayList<>();
				List<TSVFileHeaderName> tSVFailedList = new ArrayList<>();

				TSVFileHeaderName tSVFileHeaderNameObj, tSVFileFailedObj;
				while ((next = csvReader.readNext()) != null) {
					if(next.length > 1){
						tSVFileHeaderNameObj = new TSVFileHeaderName();
						tSVFileFailedObj = new TSVFileHeaderName();
						if (next[1] == null || next[6] == null || next[8] == null
								|| next[9] == null || next[16] == null
								|| next[18] == null || next[20] == null) {
							tSVFileFailedObj = new TSVFileHeaderName();
							tSVFailedList.add(tSVFileFailedObj);
						} else {
							tSVFileHeaderNameObj = new TSVFileHeaderName();
							//tSVFileHeaderNameObj.setSID(Long.valueOf(next[1].trim()));
							tSVFileHeaderNameObj.setSID(next[1].trim());
							tSVFileHeaderNameObj.setAssayNumber(Integer
									.valueOf(next[6]));
							tSVFileHeaderNameObj.setResult(BigDecimal
									.valueOf(Double.valueOf(next[8].trim())));
							tSVFileHeaderNameObj.setInterpretation(next[9]);
							tSVFileHeaderNameObj.setCompleted(formatter
									.parse(next[16]));
							tSVFileHeaderNameObj.setOperatorID(Integer
									.parseInt(next[18].trim()));
							tSVFileHeaderNameObj.setReagentLotNumber(next[20]);
							tSVFileHeaderNameList.add(tSVFileHeaderNameObj);
							
						}
						
					}
				}

				bloodTestingRepository
						.saveTestResultsToDatabase(tSVFileHeaderNameList);

				successRows = tSVFileHeaderNameList.size()
						+ UploadTTIResultConstant.SUCCESS_ROW;
				failedRows = tSVFailedList.size()
						+ UploadTTIResultConstant.FAIL_ROW;
				map.put("SuccessRows", successRows);
				map.put("FailedRows", failedRows);
			} catch (FileNotFoundException e) {
				LOGGER.error("File Not Found:" + e);
			}
		}

	}

	 private void writeTSVFile(String fileName, String uploadPath,
		   MultipartFile tsvFile) {
		  InputStream inputStream;
		  OutputStream outputStream;
		  try {
		   inputStream = tsvFile.getInputStream();
		   File newFile = new File(uploadPath + fileName);
		   if (!newFile.exists()) {
		    newFile.createNewFile();
		   }
		   outputStream = new FileOutputStream(newFile);
		   int read = 0;
		   byte[] bytes = new byte[1024];
		   while ((read = inputStream.read(bytes)) != -1) {
		    outputStream.write(bytes, 0, read);
		   }
		  } catch (IOException e) {
		   LOGGER.error("Error occurred while writing to disk: " + e);
		  }
		 }	 	 	 
}
