package model.usage;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

import model.CustomDateFormatter;
import model.collectedsample.CollectedSampleBackingForm;
import model.collectionbatch.CollectionBatch;
import model.donor.Donor;
import model.product.Product;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import viewmodel.ProductUsageViewModel;

import controller.UtilController;

public class ProductUsageBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public ProductUsageBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(ProductUsageBackingForm.class, ProductUsageViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    ProductUsageBackingForm form = (ProductUsageBackingForm) obj;

    String usageDate = form.getUsageDate();
    if (!CustomDateFormatter.isDateTimeStringValid(usageDate))
      errors.rejectValue("usage.usageDate", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());

    updateRelatedEntities(form);

    utilController.commonFieldChecks(form, "usage", errors);
  }

  private void updateRelatedEntities(ProductUsageBackingForm form) {
    Product product = utilController.findProduct(form.getCollectionNumber(), form.getProductType());
    form.setProduct(product);
  }
}

