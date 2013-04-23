package model.usage;

import java.util.Arrays;

import model.CustomDateFormatter;
import model.product.Product;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import viewmodel.ProductUsageViewModel;
import viewmodel.RequestViewModel;
import controller.UtilController;

public class UsageBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public UsageBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(ProductUsageBackingForm.class, ProductUsageViewModel.class, RequestViewModel.class).contains(clazz);
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

