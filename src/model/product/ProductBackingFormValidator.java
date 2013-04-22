package model.product;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import model.CustomDateFormatter;
import model.collectedsample.CollectedSample;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import viewmodel.ProductViewModel;
import controller.UtilController;

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
    return Arrays.asList(FindProductBackingForm.class,
                         ProductBackingForm.class,
                         ProductViewModel.class,
                         ProductCombinationBackingForm.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
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
    System.out.println(form.getCollectionNumber());
    System.out.println(form.getCreatedOn());
    System.out.println(form.getExpiresOn());
    System.out.println(form.getProductTypeCombination());

    String createdOn = form.getCreatedOn();
    if (!CustomDateFormatter.isDateTimeStringValid(createdOn))
      errors.rejectValue("product.createdOn", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());

    String expiresOn = form.getExpiresOn();
    ObjectMapper mapper = new ObjectMapper();

    try {

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
      CollectedSample collectedSample = utilController.findCollectionInForm(bean);
      form.setCollectedSample(collectedSample);
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
      CollectedSample collectedSample = utilController.findCollectionInForm(bean);
      form.setCollectedSample(collectedSample);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

}
