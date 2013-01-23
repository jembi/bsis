package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import model.admin.FormField;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.FormFieldRepository;
import repository.TipsRepository;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UtilController {
  public static final String VERSION_NUMBER = "1.0";

  @Autowired
  private FormFieldRepository formFieldRepository;

  @Autowired
  private TipsRepository tipsRepository;

  @Autowired
  private ServletContext servletContext;
  
  public Map<String, Object> getFormFieldsForForm(String formName) {
    List<FormField> formFields = formFieldRepository.getFormFields(formName);
    Map<String, Object> formFieldMap = new HashMap<String, Object>();

    Map<String, String> mirroredFields = new HashMap<String, String>();
    for (FormField ff : formFields) {
      Map<String, Object> fieldProperties = new HashMap<String, Object>();
      fieldProperties.put(FormField.DISPLAY_NAME, ff.getDisplayName());
      fieldProperties.put(FormField.DEFAULT_VALUE, ff.getDefaultValue());
      fieldProperties.put(FormField.HIDDEN, ff.getHidden());
      fieldProperties.put(FormField.DERIVED, ff.getDerived());
      fieldProperties.put(FormField.SOURCE_FIELD, ff.getSourceField());

      formFieldMap.put(ff.getField(), fieldProperties);
      if (ff.getDerived())
        mirroredFields.put(ff.getField(), ff.getSourceField());
    }

    ObjectMapper mapper = new ObjectMapper();
    StringWriter writer = new StringWriter();
    try {
      mapper.writeValue(writer, mirroredFields);
    } catch (JsonGenerationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println(writer.toString());
    formFieldMap.put("mirroredFields", writer.toString());

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
              (fieldValue instanceof String && ((String)fieldValue).isEmpty())
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
}
