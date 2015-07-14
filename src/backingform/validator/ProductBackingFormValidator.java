package backingform.validator;

import backingform.ProductBackingForm;
import backingform.ProductCombinationBackingForm;
import backingform.RecordProductBackingForm;

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
import viewmodel.ProductViewModel;

public class ProductBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public ProductBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(ProductBackingForm.class,
                         ProductViewModel.class,
                         ProductCombinationBackingForm.class,
                         RecordProductBackingForm.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    //ValidationUtils.invokeValidator(validator, obj, errors);
    if (obj instanceof ProductBackingForm)
      validateProductBackingForm((ProductBackingForm) obj, errors);
    if (obj instanceof ProductCombinationBackingForm)
      validateProductCombinationBackingForm((ProductCombinationBackingForm) obj, errors);
  }

  private void validateProductBackingForm(ProductBackingForm form, Errors errors) {
    String createdOn = form.getCreatedOn();
    if (!CustomDateFormatter.isDateTimeStringValid(createdOn))
      errors.rejectValue("product.createdOn", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());

    String expiresOn = form.getExpiresOn();
    if (!CustomDateFormatter.isDateStringValid(expiresOn))
      errors.rejectValue("product.expiresOn", "dateFormat.incorrect",
          CustomDateFormatter.getDateErrorMessage());

    updateRelatedEntities(form);
    utilController.commonFieldChecks(form, "product", errors);
  }

  private void validateProductCombinationBackingForm(ProductCombinationBackingForm form, Errors errors) {

    if (StringUtils.isBlank(form.getProductTypeCombination()))
      errors.rejectValue("productTypeCombination", "product.productTypeCombination",
          "Product type combination should be specified");

    String createdOn = form.getCreatedOn();
    if (!CustomDateFormatter.isDateTimeStringValid(createdOn))
      errors.rejectValue("product.createdOn", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());

    String expiresOn = form.getExpiresOn();
    ObjectMapper mapper = new ObjectMapper();

    try {

      @SuppressWarnings("unchecked")
      Map<String, String> expiryDateByProductType = mapper.readValue(expiresOn, HashMap.class);
      for (String productTypeId : expiryDateByProductType.keySet()) {
        String expiryDate = expiryDateByProductType.get(productTypeId);
        if (!CustomDateFormatter.isDateTimeStringValid(expiryDate))
          errors.rejectValue("product.expiresOn", "dateFormat.incorrect",
              CustomDateFormatter.getDateErrorMessage());
      }

    } catch (JsonParseException e) {
      errors.rejectValue("product.expiresOn", "dateFormat.incorrect", "Invalid expiry date specified");
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonMappingException e) {
      errors.rejectValue("product.expiresOn", "dateFormat.incorrect", "Invalid expiry date specified");
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      errors.rejectValue("product.expiresOn", "dateFormat.incorrect", "Invalid expiry date specified");
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    updateRelatedEntities(form);

    utilController.commonFieldChecks(form, "product", errors);
  }

  @SuppressWarnings("unchecked")
  private void updateRelatedEntities(ProductBackingForm form) {
    Map<String, Object> bean = null;
    try {
      bean = BeanUtils.describe(form);
      Donation donation = utilController.findCollectionInForm(bean);
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
  private void updateRelatedEntities(ProductCombinationBackingForm form) {
    Map<String, Object> bean = null;
    try {
      bean = BeanUtils.describe(form);
      Donation donation = utilController.findCollectionInForm(bean);
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
