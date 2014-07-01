package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import model.admin.FormField;
import model.collectedsample.CollectedSample;
import model.donationbatch.DonationBatch;
import model.donor.Donor;
import model.donordeferral.DonorDeferral;
import model.product.Product;
import model.product.ProductStatus;
import model.producttype.ProductType;
import model.request.Request;
import model.user.User;
import model.worksheet.Worksheet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.CollectedSampleRepository;
import repository.DonationBatchRepository;
import repository.DonorRepository;
import repository.FormFieldRepository;
import repository.GenericConfigRepository;
import repository.ProductRepository;
import repository.RequestRepository;
import repository.SequenceNumberRepository;
import repository.TipsRepository;
import repository.UserRepository;
import repository.WorksheetRepository;
import security.V2VUserDetails;
import utils.DonorUtils;

@Component
public class UtilController {
  public static final String VERSION_NUMBER = "1.3";

  @Autowired
  private FormFieldRepository formFieldRepository;

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  @Autowired
  private WorksheetRepository worksheetRepository;

  @Autowired
  private TipsRepository tipsRepository;

  @Autowired
  private ServletContext servletContext;

  @Autowired
  private SequenceNumberRepository sequenceNumberRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository;
  
  @Autowired
  private UserRepository userRepository;
  
  public Map<String, Map<String, Object>> getFormFieldsForForm(String formName) {
    List<FormField> formFields = formFieldRepository.getFormFields(formName);
    Map<String, Map<String, Object>> formFieldMap = new HashMap<String, Map<String, Object>>();

    for (FormField ff : formFields) {
      Map<String, Object> fieldProperties = new HashMap<String, Object>();
      fieldProperties.put(FormField.DISPLAY_NAME, ff.getDisplayName());
      fieldProperties.put(FormField.SHORT_DISPLAY_NAME, ff.getShortDisplayName());
      fieldProperties.put(FormField.DEFAULT_VALUE, ff.getDefaultValue());
      fieldProperties.put(FormField.HIDDEN, ff.getHidden());
      fieldProperties.put(FormField.IS_AUTO_GENERATABLE, ff.getIsAutoGeneratable());
      fieldProperties.put(FormField.AUTO_GENERATE, ff.getAutoGenerate());
      fieldProperties.put(FormField.IS_TIME_FIELD, ff.getIsTimeField());
      fieldProperties.put(FormField.USE_CURRENT_TIME, ff.getUseCurrentTime());

      formFieldMap.put(ff.getField(), fieldProperties);
    }
    return formFieldMap;
  }

  private List<String> getRequiredFields(String formName) {
    return formFieldRepository.getRequiredFormFields(formName);
  }

  public void commonFieldChecks(Object form, String formName, Errors errors) {
    checkRequiredFields(form, formName, errors);
    checkFieldLengths(form, formName, errors);
  }

  public void checkFieldLengths(Object form, String formName, Errors errors) {
    try {
      @SuppressWarnings("unchecked")
      Map<String, Object> properties = BeanUtils.describe(form);
      Map<String, Integer> maxLengths = getFieldMaxLengths(formName);
      for (String field : maxLengths.keySet()) {
        if (properties.containsKey(field)) {
          Object fieldValue = properties.get(field);
          Integer maxLength = maxLengths.get(field);
          if (fieldValue != null && maxLength > 0 &&
              (fieldValue instanceof String && ((String)fieldValue).length() > maxLength)
             )
            try {
              errors.rejectValue(formName + "." + field, "fieldLength.error", "Maximum length for this field is " + maxLength);
            } catch (NotReadablePropertyException ex) {
              // just ignore this error if the property is not readable
              ex.printStackTrace();
            }
        }
      }
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private Map<String, Integer> getFieldMaxLengths(String formName) {
    return formFieldRepository.getFieldMaxLengths(formName);
  }

  public void checkRequiredFields(Object form, String formName, Errors errors) {
    try {
      @SuppressWarnings("unchecked")
      Map<String, Object> properties = BeanUtils.describe(form);
      List<String> requiredFields = getRequiredFields(formName);
      for (String requiredField : requiredFields) {
        if (properties.containsKey(requiredField)) {
          Object fieldValue = properties.get(requiredField);
          if (fieldValue == null ||
              (fieldValue instanceof String && StringUtils.isBlank((String) fieldValue))
             ) {
            errors.rejectValue(formName + "." + requiredField, "requiredField.error", "This information is required");
          }
        }
      }
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void addTipsToModel(Map<String, Object> m, String key) {
    m.put(key, tipsRepository.getTipsContent(key));
  }

  public String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  public Properties getV2VProperties() throws IOException {
    Properties prop = new Properties();
    BufferedReader reader = new BufferedReader(new InputStreamReader(servletContext.getResourceAsStream("/WEB-INF/v2v.properties")));
    String propertyFileContents = "";
    String line;
    while ((line = reader.readLine()) != null) {
      propertyFileContents += line + "\n";
    }
    prop.load(new StringReader(propertyFileContents.replace("\\","\\\\")));
    return prop;
  }

  public Map<String, Object> parsePagingParameters(HttpServletRequest request) {
    Map<String, Object> pagingParams = new HashMap<String, Object>();
    int numColumns = Integer.parseInt(request.getParameter("iColumns"));
    int sortCol = -1;
    String sortDirection = "asc";
    for (int i = 0; i < numColumns; ++i) {
      if (request.getParameter("iSortCol_" + i) != null) {
        sortCol = Integer.parseInt(request.getParameter("iSortCol_" + i));
        if (request.getParameter("sSortDir_" + i) != null)
          sortDirection = request.getParameter("sSortDir_" + i);
        break;
      }
    }

    pagingParams.put("sortColumnId", sortCol);
    pagingParams.put("sortDirection", sortDirection);
    pagingParams.put("start", request.getParameter("iDisplayStart"));
    pagingParams.put("length", request.getParameter("iDisplayLength"));
    return pagingParams;
  }

  public Map<String, String> getConfigProperty(String propertyOwner) {
    return genericConfigRepository.getConfigProperties(propertyOwner);
  }

  public boolean isFieldAutoGenerated(String formName, String fieldName) {
    FormField formField = formFieldRepository.getFormField(formName, fieldName);
    if (formField == null)
      return false;
    return formField.getAutoGenerate();
  }

  public String getNextDonorNumber() {
    return sequenceNumberRepository.getNextDonorNumber();
  }
  
  public String getSequenceNumber(String targetTable,String columnName) {
	    return sequenceNumberRepository.getSequenceNumber(targetTable,columnName);
	  }
	  

  public String getNextCollectionNumber() {
    return sequenceNumberRepository.getNextCollectionNumber();
  }

  public Donor findDonorInForm(Map<String, Object> bean) {
    // IMPORTANT: Validation code just checks if the ID exists.
    // We still need to store the collected sample as part of the product.
    String donorId = null;
    if (bean.containsKey("donorIdHidden"))
      donorId = (String) bean.get("donorIdHidden");

    String donorNumber = null;
    if (bean.containsKey("donorNumber"))
      donorNumber = (String) bean.get("donorNumber");

    Donor donor = null;
    if (donorId != null && !donorId.isEmpty()) {
      try {
        donor = donorRepository.findDonorById(donorId);
      } catch (NoResultException ex) {
        ex.printStackTrace();
      } catch (NumberFormatException ex) {
        ex.printStackTrace();
      }
    }
    else if (donorNumber != null && !donorNumber.isEmpty()) {
      try {
        donor = donorRepository.findDonorByDonorNumber(donorNumber,false);
      } catch (NoResultException ex) {
        ex.printStackTrace();
      }
    }
    return donor;
  }

  public DonationBatch findDonationBatchInForm(Map<String, Object> bean) {

    DonationBatch donationBatch = null;
    String batchNumber = null;
    if (batchNumber == null)
      batchNumber = (String) bean.get("donationBatchNumber");
    if (StringUtils.isNotBlank(batchNumber)) {
      try {
        donationBatch = donationBatchRepository.findDonationBatchByBatchNumber(batchNumber);
      } catch (NoResultException ex) {
        ex.printStackTrace();
      }
    }
    return donationBatch;
  }

  public CollectedSample findCollectionInForm(Map<String, Object> bean) {
    CollectedSample collectedSample = null;
    String collectionNumber = null;
    if (collectionNumber == null)
      collectionNumber = (String) bean.get("collectionNumber");
    if (StringUtils.isNotBlank(collectionNumber)) {
      try {
        collectedSample = collectedSampleRepository.findCollectedSampleByCollectionNumber(collectionNumber);
      } catch (NoResultException ex) {
        ex.printStackTrace();
      }
    }
    return collectedSample;
  }

  public boolean isFutureDate(Date date){
	  Date today = new Date();
	  if(date.after(today)){
		  return true;
	  }
	  else{
		  return false;
	  }
  }
  
  public String verifyDonorAge(Date birthDate) {
    Map<String, String> config = getConfigProperty("donationRequirements");
    String errorMessage = "";
    if (config.get("ageLimitsEnabled").equals("true")) {
      try {
        Integer minAge = Integer.parseInt(config.get("minimumAge"));
        Integer maxAge = Integer.parseInt(config.get("maximumAge"));
        Integer donorAge = DonorUtils.computeDonorAge(birthDate);
        if (donorAge == null) {
          errorMessage = "One of donor Date of Birth or Age must be specified";
        }
        else {
          if (donorAge < minAge || donorAge > maxAge) {
            errorMessage = "Donor age must be between " + minAge + " and " + maxAge + " years.";
          }
        }
      } catch (NumberFormatException ex) {
        ex.printStackTrace();
      }
    }
    return errorMessage;
  }

  public String isDonorDeferred(Donor donor) {
    List<DonorDeferral> donorDeferrals = donorRepository.getDonorDeferrals(donor.getId());
    String errorMessage = "";
    if (donorRepository.isCurrentlyDeferred(donorDeferrals))
      errorMessage = "Donor is currently deferred from donations";
    return errorMessage;
  }

  public Product findProduct(String collectionNumber, String productType) {
    return productRepository.findProduct(collectionNumber, productType);
  }

  public String getNextWorksheetNumber() {
    return sequenceNumberRepository.getNextWorksheetBatchNumber();
  }

  public String getNextRequestNumber() {
    return sequenceNumberRepository.getNextRequestNumber();
  }

  public Request findRequestByRequestNumber(String requestNumber) {
    return requestRepository.findRequestByRequestNumber(requestNumber);
  }

  public Product findProduct(String collectionNumber, ProductType productType) {
    List<Product> products = productRepository.findProductsByCollectionNumber(collectionNumber);
    Product matchingProduct = null; 
    for (Product product : products) {
      if (product.getProductType().equals(productType)) {
        if (matchingProduct != null &&
            matchingProduct.getStatus().equals(ProductStatus.AVAILABLE)) {
          // multiple products available have the same product type
          // cannot identify uniquely
          return null;
        }
        matchingProduct = product;
      }
    }
    return matchingProduct;
  }

  public boolean doesFieldUseCurrentTime(String formName, String fieldName) {
    FormField formField = formFieldRepository.getFormField(formName, fieldName);
    if (formField == null)
      return false;
    return formField.getUseCurrentTime();
  }

  public String getNextBatchNumber() {
    return sequenceNumberRepository.getNextBatchNumber();
  }

  public boolean isDuplicateDonorNumber(Donor donor) {
    String donorNumber = donor.getDonorNumber();
    if (StringUtils.isBlank(donorNumber))
      return false;
    Donor existingDonor = donorRepository.findDonorByDonorNumber(donorNumber,true);
    if (existingDonor != null && !existingDonor.getId().equals(donor.getId()))
      return true;
    return false;
  }
  
  public boolean donorNumberExists(String donorNumber) {
    if (StringUtils.isBlank(donorNumber))
      return false;
    Donor existingDonor = donorRepository.findDonorByDonorNumber(donorNumber,true);
    if (existingDonor != null && existingDonor.getId() != null)
      return true;
    return false;
  }

  public boolean isDuplicateCollectionNumber(CollectedSample collection) {
    String collectionNumber = collection.getCollectionNumber();
    if (StringUtils.isBlank(collectionNumber))
      return false;
    CollectedSample existingCollection = collectedSampleRepository.findCollectionByCollectionNumberIncludeDeleted(collectionNumber);
    if (existingCollection != null && !existingCollection.getId().equals(collection.getId()))
      return true;
    return false;
  }

  public boolean isDuplicateRequestNumber(Request request) {
    String requestNumber = request.getRequestNumber();
    if (StringUtils.isBlank(requestNumber))
      return false;
    Request existingRequest = requestRepository.findRequestByRequestNumberIncludeDeleted(requestNumber);
    if (existingRequest != null && !existingRequest.getId().equals(request.getId()))
      return true;
    return false;
  }

  public boolean isDuplicateDonationBatchNumber(DonationBatch donationBatch) {
    String batchNumber = donationBatch.getBatchNumber();
    if (StringUtils.isBlank(batchNumber))
      return false;
    DonationBatch existingDonationBatch = donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted(batchNumber);
    if (existingDonationBatch != null && !existingDonationBatch.getId().equals(donationBatch.getId()))
      return true;
    return false;
  }

  public boolean isDuplicateWorksheetNumber(Worksheet worksheet) {
    String worksheetNumber = worksheet.getWorksheetNumber();
    if (StringUtils.isBlank(worksheetNumber))
      return false;
    Worksheet existingWorksheet = worksheetRepository.findWorksheetByWorksheetNumberIncludeDeleted(worksheetNumber);
    if (existingWorksheet != null && !existingWorksheet.getId().equals(worksheet.getId()))
      return true;
    return false;
  }

  public Product findProductById(String productId) {
    Product product = null;
    try {
      product = productRepository.findProductById(Long.parseLong(productId));
    } catch (NumberFormatException ex) {
      ex.printStackTrace();
    }
    return product;
  }

  public boolean isFieldRequired(String formName, String fieldName) {
    FormField formField = formFieldRepository.getFormField(formName, fieldName);
    if (formField == null)
      return false;
    return formField.getIsRequired();
  }

  public String recordMachineResultsForTTI() {
    return genericConfigRepository.getConfigProperties("labsetup").get("recordMachineReadingsForTTI");
  }

  public User getCurrentUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = null;
    if (principal != null && principal instanceof V2VUserDetails)
      user = ((V2VUserDetails) principal).getUser();
    return user;
  }
  
  public String getUserPassword(Integer id){
  	User user= userRepository.findUserById(id);
  	String pwd=null;
  	if(user!=null)
  		pwd=user.getPassword();
  	return pwd;
  }
}
