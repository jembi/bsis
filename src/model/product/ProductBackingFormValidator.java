package model.product;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

import model.CustomDateFormatter;
import model.collectedsample.CollectedSample;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

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

  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(FindProductBackingForm.class, ProductBackingForm.class, ProductViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    ProductBackingForm form = (ProductBackingForm) obj;

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

  @SuppressWarnings("unchecked")
  private void updateRelatedEntities(ProductBackingForm form) {
    Map<String, Object> bean = null;
    try {
      bean = BeanUtils.describe(form);
      CollectedSample collectedSample = utilController.findCollectionInForm(bean);
      form.setCollectedSample(collectedSample);
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

}
