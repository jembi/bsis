package backingform.validator;

import java.util.Arrays;

import javax.persistence.NoResultException;

import model.compatibility.CompatibilityTest;
import model.component.Component;
import model.request.Request;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import utils.CustomDateFormatter;
import viewmodel.CompatibilityTestViewModel;
import backingform.CompatibilityTestBackingForm;
import controller.UtilController;

public class CompatibilityTestBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public CompatibilityTestBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(CompatibilityTestBackingForm.class, CompatibilityTest.class, CompatibilityTestViewModel.class, CompatibilityTestBackingForm.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    CompatibilityTestBackingForm form = (CompatibilityTestBackingForm) obj;
    String crossmatchTestDate = form.getCompatibilityTestDate();
    if (!CustomDateFormatter.isDateTimeStringValid(crossmatchTestDate)) {
      errors.rejectValue("compatiblityTest.compatibilityTestDate", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());
    }

    String requestNumber = form.getRequestNumber();
    Request productRequest = null;
    if (requestNumber != null && !requestNumber.isEmpty()) {
      try {
        productRequest = utilController.findRequestByRequestNumber(requestNumber);
        form.setForRequest(productRequest);
      } catch (NoResultException ex) {
        form.setForRequest(null);
        ex.printStackTrace();
      }
    } else {
      form.setForRequest(null);
    }

    String donationIdentificationNumber = form.getDonationIdentificationNumber();
    if (StringUtils.isNotBlank(donationIdentificationNumber) && productRequest != null) {
      try {
        Component testedComponent = utilController.findComponent(donationIdentificationNumber, productRequest.getComponentType());
        if (testedComponent == null)
          errors.rejectValue("compatibilityTest.testedComponent", "compatibilitytest.testedComponent.notFound",
              "Component with this donation identification number and product type not found or not available");
        form.setTestedComponent(testedComponent);
      } catch (NoResultException ex) {
        form.setTestedComponent(null);
        ex.printStackTrace();
      }
    } else {
      form.setTestedComponent(null);
    }

    utilController.commonFieldChecks(form, "compatibilityTest", errors);
  }
}
