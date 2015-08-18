package backingform.validator;

import backingform.ComponentBackingForm;
import backingform.ComponentCombinationBackingForm;
import backingform.RecordComponentBackingForm;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import controller.UtilController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import model.donation.Donation;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import utils.CustomDateFormatter;
import viewmodel.ComponentViewModel;

public class ComponentBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public ComponentBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(ComponentBackingForm.class,
                         ComponentViewModel.class,
                         ComponentCombinationBackingForm.class,
                         RecordComponentBackingForm.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    //ValidationUtils.invokeValidator(validator, obj, errors);
    if (obj instanceof ComponentBackingForm)
      validateComponentBackingForm((ComponentBackingForm) obj, errors);
    if (obj instanceof ComponentCombinationBackingForm)
      validateComponentCombinationBackingForm((ComponentCombinationBackingForm) obj, errors);
  }

  private void validateComponentBackingForm(ComponentBackingForm form, Errors errors) {
    String createdOn = form.getCreatedOn();
    if (!CustomDateFormatter.isDateTimeStringValid(createdOn))
      errors.rejectValue("component.createdOn", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());

    String expiresOn = form.getExpiresOn();
    if (!CustomDateFormatter.isDateStringValid(expiresOn))
      errors.rejectValue("component.expiresOn", "dateFormat.incorrect",
          CustomDateFormatter.getDateErrorMessage());

    updateRelatedEntities(form);
    utilController.commonFieldChecks(form, "component", errors);
  }

  private void validateComponentCombinationBackingForm(ComponentCombinationBackingForm form, Errors errors) {

    if (StringUtils.isBlank(form.getComponentTypeCombination()))
      errors.rejectValue("componentTypeCombination", "component.componentTypeCombination",
          "Component type combination should be specified");

    String createdOn = form.getCreatedOn();
    if (!CustomDateFormatter.isDateTimeStringValid(createdOn))
      errors.rejectValue("component.createdOn", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());

    String expiresOn = form.getExpiresOn();
    ObjectMapper mapper = new ObjectMapper();

    try {

      @SuppressWarnings("unchecked")
      Map<String, String> expiryDateByComponentType = mapper.readValue(expiresOn, HashMap.class);
      for (String componentTypeId : expiryDateByComponentType.keySet()) {
        String expiryDate = expiryDateByComponentType.get(componentTypeId);
        if (!CustomDateFormatter.isDateTimeStringValid(expiryDate))
          errors.rejectValue("component.expiresOn", "dateFormat.incorrect",
              CustomDateFormatter.getDateErrorMessage());
      }

    } catch (JsonParseException e) {
      errors.rejectValue("component.expiresOn", "dateFormat.incorrect", "Invalid expiry date specified");
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonMappingException e) {
      errors.rejectValue("component.expiresOn", "dateFormat.incorrect", "Invalid expiry date specified");
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      errors.rejectValue("component.expiresOn", "dateFormat.incorrect", "Invalid expiry date specified");
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    updateRelatedEntities(form);

    utilController.commonFieldChecks(form, "component", errors);
  }

  @SuppressWarnings("unchecked")
  private void updateRelatedEntities(ComponentBackingForm form) {
    Map<String, Object> bean = null;
    try {
      bean = BeanUtils.describe(form);
      Donation donation = utilController.findDonationInForm(bean);
      form.setDonation(donation);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("unchecked")
  private void updateRelatedEntities(ComponentCombinationBackingForm form) {
    Map<String, Object> bean = null;
    try {
      bean = BeanUtils.describe(form);
      Donation donation = utilController.findDonationInForm(bean);
      form.setDonation(donation);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

}
