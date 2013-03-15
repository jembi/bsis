package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import model.admin.FormField;
import model.collectionbatch.CollectionBatch;
import model.donor.Donor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.CollectionBatchRepository;
import repository.DonorRepository;
import repository.FormFieldRepository;
import repository.GenericConfigRepository;
import repository.SequenceNumberRepository;
import repository.TipsRepository;

@Component
public class UtilController {
  public static final String VERSION_NUMBER = "1.1";

  @Autowired
  private FormFieldRepository formFieldRepository;

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private CollectionBatchRepository collectionBatchRepository;

  @Autowired
  private TipsRepository tipsRepository;

  @Autowired
  private ServletContext servletContext;

  @Autowired
  private SequenceNumberRepository sequenceNumberRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository;
  
  public Map<String, Object> getFormFieldsForForm(String formName) {
    List<FormField> formFields = formFieldRepository.getFormFields(formName);
    Map<String, Object> formFieldMap = new HashMap<String, Object>();

    for (FormField ff : formFields) {
      Map<String, Object> fieldProperties = new HashMap<String, Object>();
      fieldProperties.put(FormField.DISPLAY_NAME, ff.getDisplayName());
      fieldProperties.put(FormField.DEFAULT_VALUE, ff.getDefaultValue());
      fieldProperties.put(FormField.HIDDEN, ff.getHidden());
      fieldProperties.put(FormField.IS_AUTO_GENERATABLE, ff.getIsAutoGeneratable());
      fieldProperties.put(FormField.AUTO_GENERATE, ff.getAutoGenerate());

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
      Map<String, Object> properties = BeanUtils.describe(form);
      List<String> requiredFields = getRequiredFields(formName);
      for (String requiredField : requiredFields) {
        if (properties.containsKey(requiredField)) {
          Object fieldValue = properties.get(requiredField);
          if (fieldValue == null ||
              (fieldValue instanceof String && StringUtils.isBlank((String) fieldValue))
             )
            errors.rejectValue(formName + "." + requiredField, "requiredField.error", "This field is required");
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
        donor = donorRepository.findDonorByDonorNumber(donorNumber);
      } catch (NoResultException ex) {
        ex.printStackTrace();
      }
    }
    return donor;
  }

  public CollectionBatch findCollectionBatchInForm(Map<String, Object> bean) {

    CollectionBatch collectionBatch = null;
    String batchNumber = null;
    if (batchNumber == null)
      batchNumber = (String) bean.get("collectionBatchNumber");
    if (StringUtils.isNotBlank(batchNumber)) {
      try {
        collectionBatch = collectionBatchRepository.findCollectionBatchByBatchNumber(batchNumber);
      } catch (NoResultException ex) {
        ex.printStackTrace();
      }
    }
    return collectionBatch;
  }
}
