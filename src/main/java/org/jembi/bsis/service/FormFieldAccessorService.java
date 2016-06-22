package org.jembi.bsis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.model.admin.FormField;
import org.jembi.bsis.repository.FormFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormFieldAccessorService {

  @Autowired
  private FormFieldRepository formFieldRepository;

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
}
