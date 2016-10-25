package org.jembi.bsis.backingform.validator;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.ComponentTypeCombinationBackingForm;
import org.jembi.bsis.repository.ComponentTypeCombinationRepository;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ComponentTypeCombinationBackingFormValidator extends BaseValidator<ComponentTypeCombinationBackingForm> {

  private static final Integer MAX_LENGTH_NAME = 255;

  @Autowired
  private ComponentTypeCombinationRepository componentTypeCombinationRepository;

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  @Override
  public void validateForm(ComponentTypeCombinationBackingForm form, Errors errors) {

    // Validate combinationName
    if (StringUtils.isBlank(form.getCombinationName())) {
      errors.rejectValue("combinationName", "errors.required", "Combination name is required");
    } else if (form.getCombinationName().length() > MAX_LENGTH_NAME) {
      errors.rejectValue("combinationName", "errors.fieldLength",
          "Maximum length for this field is " + MAX_LENGTH_NAME);
    } else {
      if (!componentTypeCombinationRepository.isUniqueCombinationName(form.getId(), form.getCombinationName())) {
        errors.rejectValue("combinationName", "errors.nonUnique", "Combination name already exists");
      }
    }

    // Validate source componentTypes
    Set<ComponentTypeBackingForm> sourceComponentTypes = form.getSourceComponentTypes();
    if (sourceComponentTypes == null || sourceComponentTypes.isEmpty()) {
      errors.rejectValue("sourceComponentTypes", "errors.required", "Produced component types are required");
    } else {
      int i = 0;
      Iterator<ComponentTypeBackingForm> it = sourceComponentTypes.iterator();
      while (it.hasNext()) {
        errors.pushNestedPath("sourceComponentTypes[" + i + "]");
        try {
          validateComponentType(it.next(), errors);
        } finally {
          errors.popNestedPath();
        }
        i++;
      }
    }

    // Validate produced componentTypes
    List<ComponentTypeBackingForm> producedComponentTypes = form.getComponentTypes();
    if (producedComponentTypes == null || producedComponentTypes.isEmpty()) {
      errors.rejectValue("componentTypes", "errors.required", "Produced component types are required");
    } else {
      for (int i = 0, len = producedComponentTypes.size(); i < len; i++) {
        errors.pushNestedPath("componentTypes[" + i + "]");
        try {
          validateComponentType(producedComponentTypes.get(i), errors);
        } finally {
          errors.popNestedPath();
        }
      }
    }
  }

  @Override
  public String getFormName() {
    return "ComponentTypeCombination";
  }

  private void validateComponentType(ComponentTypeBackingForm componentType, Errors errors) {
    if (componentType.getId() == null) {
      errors.rejectValue("id", "errors.required", "componentType id is required.");
    } else {
      if (!componentTypeRepository.verifyComponentTypeExists(componentType.getId())) {
        errors.rejectValue("id", "errors.invalid", "componentType id is invalid.");
      }
    }
  }
}
