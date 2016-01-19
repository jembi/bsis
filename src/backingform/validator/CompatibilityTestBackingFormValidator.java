package backingform.validator;

import java.util.List;

import javax.persistence.NoResultException;

import model.component.Component;
import model.component.ComponentStatus;
import model.componenttype.ComponentType;
import model.request.Request;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import repository.ComponentRepository;
import repository.RequestRepository;
import utils.CustomDateFormatter;
import backingform.CompatibilityTestBackingForm;

@org.springframework.stereotype.Component
public class CompatibilityTestBackingFormValidator extends BaseValidator<CompatibilityTestBackingForm> {
  
  private static final Logger LOGGER = Logger.getLogger(CompatibilityTestBackingFormValidator.class);
  
  @Autowired
  private RequestRepository requestRepository;
  
  @Autowired
  private ComponentRepository componentRepository;

  @Override
  public void validateForm(CompatibilityTestBackingForm form, Errors errors) throws Exception {
    String crossmatchTestDate = form.getCompatibilityTestDate();
    if (!CustomDateFormatter.isDateTimeStringValid(crossmatchTestDate)) {
      errors.rejectValue("compatiblityTest.compatibilityTestDate", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());
    }

    String requestNumber = form.getRequestNumber();
    Request componentRequest = null;
    if (requestNumber != null && !requestNumber.isEmpty()) {
      componentRequest = findRequestByRequestNumber(requestNumber);
    }
    form.setForRequest(componentRequest);

    String donationIdentificationNumber = form.getDonationIdentificationNumber();
    if (StringUtils.isNotBlank(donationIdentificationNumber) && componentRequest != null) {
      Component testedComponent = findComponent(donationIdentificationNumber, componentRequest.getComponentType());
      if (testedComponent == null)
        errors.rejectValue("compatibilityTest.testedComponent", "compatibilitytest.testedComponent.notFound",
            "Component with this donation identification number and component type not found or not available");
      form.setTestedComponent(testedComponent);
    } else {
      form.setTestedComponent(null);
    }

    commonFieldChecks(form, errors);
  }
  
  @Override
  public String getFormName() {
    return "compatibilityTest";
  }

  private Request findRequestByRequestNumber(String requestNumber) {
    Request matchingRequest = null;
    try {
      matchingRequest = requestRepository.findRequestByRequestNumber(requestNumber);
    } catch (NoResultException ex) {
      LOGGER.warn("Could not find Request with requestNumber '" + requestNumber + "'.");
    }
    return matchingRequest;
  }
  
  private Component findComponent(String donationIdentificationNumber, ComponentType componentType) {
    Component matchingComponent = null;
    try {
      List<Component> components = componentRepository.findComponentsByDonationIdentificationNumber(donationIdentificationNumber);
      for (Component component : components) {
        if (component.getComponentType().equals(componentType)) {
          if (matchingComponent != null && matchingComponent.getStatus().equals(ComponentStatus.AVAILABLE)) {
            // multiple components available have the same component type - cannot identify uniquely
            return null;
          }
          matchingComponent = component;
        }
      }
    } catch (NoResultException ex) {
      LOGGER.warn("Could not find Component with donationIdentificationNumber '" + donationIdentificationNumber + "'.");
    }
    return matchingComponent;
  }
}
