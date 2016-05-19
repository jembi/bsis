package backingform.validator;

import model.util.BloodGroup;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.ComponentTypeRepository;
import backingform.OrderFormItemBackingForm;

@Component
public class OrderFormItemBackingFormValidator extends BaseValidator<OrderFormItemBackingForm> {
  
  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  @Override
  public void validateForm(OrderFormItemBackingForm form, Errors errors) {
    // check componentType;
    if (form.getComponentType() == null || form.getComponentType().getComponentType() == null
        || form.getComponentType().getComponentType().getId() == null) {
      errors.rejectValue("componentType", "required", "componentType is required");
    } else {
      if (!componentTypeRepository.verifyComponentTypeExists(form.getComponentType().getComponentType().getId())) {
        errors.rejectValue("componentType", "invalid", "Invalid componentType");
      }
    }
    
    // check bloodGroup;
    if (StringUtils.isBlank(form.getBloodGroup())) {
      errors.rejectValue("items.bloodGroup", "required", "bloodGroup is required");
    } else {
      BloodGroup bloodGroup = new BloodGroup(form.getBloodGroup());
      if (StringUtils.isBlank(bloodGroup.getBloodAbo()) || StringUtils.isBlank(bloodGroup.getBloodRh())) {
        errors.rejectValue("bloodGroup", "invalid", "Invalid bloodGroup");
      }
    }
    
    // numberOfUnits;
    if (form.getNumberOfUnits() < 0) {
      errors.rejectValue("numberOfUnits", "invalid", "numberOfUnits should be greater than 0");
    }
  }

  @Override
  public String getFormName() {
    return "OrderFormItem";
  }

}
