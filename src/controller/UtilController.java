package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.admin.FormField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.FormFieldRepository;

@Component
public class UtilController {
  public static final String VERSION_NUMBER = "1.0";

  @Autowired
  private FormFieldRepository formFieldRepository;

  public Map<String, Object> getFormFieldsForForm(String formName) {
    List<FormField> formFields = formFieldRepository.getFormFields(formName);
    Map<String, Object> formFieldMap = new HashMap<String, Object>();
    for (FormField ff : formFields) {
      Map<String, Object> fieldProperties = new HashMap<String, Object>();
      fieldProperties.put(FormField.DISPLAY_NAME, ff.getDisplayName());
      fieldProperties.put(FormField.DEFAULT_VALUE, ff.getDefaultValue());
      fieldProperties.put(FormField.HIDDEN, ff.getHidden());
      fieldProperties.put(FormField.DERIVED, ff.getDerived());
      fieldProperties.put(FormField.SOURCE_FIELD, ff.getSourceField());

      formFieldMap.put(ff.getField(), fieldProperties);
    }
    return formFieldMap;
  }
}
