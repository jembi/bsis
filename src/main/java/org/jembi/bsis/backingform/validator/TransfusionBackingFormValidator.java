package org.jembi.bsis.backingform.validator;

import java.util.List;
import java.util.Objects;

import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.TransfusionBackingForm;
import org.jembi.bsis.backingform.TransfusionReactionTypeBackingForm;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.TransfusionRepository;
import org.jembi.bsis.service.DateGeneratorService;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

@org.springframework.stereotype.Component
public class TransfusionBackingFormValidator extends BaseValidator<TransfusionBackingForm> {

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private ComponentRepository componentRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private TransfusionRepository transfusionRepository;

  @Autowired
  private DateGeneratorService dateGeneratorService;

  @Override
  public void validateForm(TransfusionBackingForm form, Errors errors) {
    Component component = validateDINAndComponentAndRetrieveComponent(form, errors);

    validateTransfusionOutcome(form, errors);

    validateDateTransfused(form, errors, component);

    validateReceivedFromLocation(form, errors);

    validatePatientNames(form, errors);
  }

  private Component validateDINAndComponentAndRetrieveComponent(TransfusionBackingForm form, Errors errors) {
    Donation donation = null;
    if (StringUtils.isBlank(form.getDonationIdentificationNumber())) {
      errors.rejectValue("donationIdentificationNumber", "errors.required", "donationIdentificationNumber is required");
    } else {
      try {
        donation =
            donationRepository.findDonationByDonationIdentificationNumber(form.getDonationIdentificationNumber());
      } catch (NoResultException e) {
        errors.rejectValue("donationIdentificationNumber", "errors.invalid", "Invalid donationIdentificationNumber");
      }
    }

    Component component = null;
    List<Component> componentsFromDINAndComponentTypeList = null;
    if (form.getComponentType() != null && form.getComponentType().getId() != null) {
      if (!componentTypeRepository.verifyComponentTypeExists(form.getComponentType().getId())) {
        errors.rejectValue("componentType", "errors.invalid",
            "Invalid componentType");
      } else {
        // validate the components only if a valid Donation was found
        if (donation != null) {
          componentsFromDINAndComponentTypeList = componentRepository.findComponentsByDINAndType(
              form.getDonationIdentificationNumber(), form.getComponentType().getId());

          if (componentsFromDINAndComponentTypeList == null || componentsFromDINAndComponentTypeList.size() == 0) {
            errors.rejectValue("componentType", "errors.invalid.noComponents",
                "No components with the specified component type exist for the specified Donation");
          } else if (form.getComponentCode() == null && componentsFromDINAndComponentTypeList.size() > 1) {
            errors.rejectValue("componentType", "errors.invalid.multipleComponents",
                "More than one component returned for given component Type. Please enter a componentCode");
          } else if (componentsFromDINAndComponentTypeList.size() == 1) {
            component = componentsFromDINAndComponentTypeList.get(0);
            if (form.getId() == null) { // new transfusion
              if (component != null && component.getStatus() != ComponentStatus.ISSUED) {
                errors.rejectValue("componentType", "errors.invalid.componentStatus",
                    "There is no component in ISSUED state for specified donationIdentificationNumber and componentType");
              }
            } else { // update existing transfusion
              if (component != null && component.getStatus() != ComponentStatus.TRANSFUSED) {
                errors.rejectValue("componentType", "errors.invalid.componentStatus",
                    "There is no component in TRANSFUSED state for specified donationIdentificationNumber and componentType");
              }
            }
          }
        }
      }
    }

    if ((form.getComponentType() == null || form.getComponentType().getId() == null)
          && StringUtils.isBlank(form.getComponentCode())) {
      errors.rejectValue("componentType", "errors.required", "componentType is required");
      errors.rejectValue("componentCode", "errors.required", "componentCode is required");
    } else if (donation != null && StringUtils.isNotBlank(form.getComponentCode())) {
      try {
        // note that if the component Code was specified it is used with higher priority for the
        component = componentRepository.findComponentByCodeAndDIN(form.getComponentCode(),
            form.getDonationIdentificationNumber());
        if (form.getId() == null) { // new transfusion
          if (component != null && component.getStatus() != ComponentStatus.ISSUED) {
            errors.rejectValue("componentCode", "errors.invalid.componentStatus",
                "There is no component in ISSUED state for specified donationIdentificationNumber and componentCode");
          }
        } else { // update existing transfusion
          if (component != null && component.getStatus() != ComponentStatus.TRANSFUSED) {
            errors.rejectValue("componentCode", "errors.invalid.componentStatus",
                "There is no component in TRANSFUSED state for specified donationIdentificationNumber and componentCode");
          }
        }
      } catch (NoResultException e) {
        errors.rejectValue("componentCode", "errors.invalid", "Invalid componentCode");
      }
      // validate componentType is the same as componentCode if both are configured
      if (componentsFromDINAndComponentTypeList != null && componentsFromDINAndComponentTypeList.size() > 0) {
        if (component != null && 
            !Objects.equals(component.getComponentType().getId(),
                componentsFromDINAndComponentTypeList.get(0).getComponentType().getId())) {
          errors.rejectValue("componentType", "errors.invalid",
              "ComponentType does not match the entered componentCode");
        }
      }
    }

    if (component != null && form.getId() != null) {
      Transfusion transfusion = transfusionRepository.findTransfusionById(form.getId());

      if (!component.getId().equals(transfusion.getComponent().getId())) {
        errors.rejectValue("donationIdentificationNumber", "errors.invalid.componentCantBeEdited",
            "The component cannot be modified");
      }
    }

    return component;
  }

  private void validateTransfusionOutcome(TransfusionBackingForm form, Errors errors) {
    TransfusionOutcome transfusionOutcome = form.getTransfusionOutcome();
    TransfusionReactionTypeBackingForm transfusionReactionType = form.getTransfusionReactionType();
    if (transfusionOutcome == null) {
      errors.rejectValue("transfusionOutcome", "errors.required", "transfusionOutcome is required");
    } else {
      if (transfusionOutcome == TransfusionOutcome.TRANSFUSION_REACTION_OCCURRED) {
        if (transfusionReactionType == null) {
          errors.rejectValue("transfusionReactionType", "errors.required",
              "transfusionReactionType is required if a Transfusion Reaction Occurred,");
        }
      } else {
        if (transfusionReactionType != null) {
          errors.rejectValue("transfusionReactionType", "errors.invalid",
              "TransfusionReactionType is not valid if the Transfusion Outcome indicates that a "
                  + "reaction had not occurred i.e. Outcome is != TRANSFUSION_REACTION_OCCURRED");
        }
      }
    }
  }

  private void validateDateTransfused(TransfusionBackingForm form, Errors errors, Component component) {
    LocalDate transfusionDate = dateGeneratorService.generateLocalDate(form.getDateTransfused());
    if (transfusionDate == null) {
      errors.rejectValue("dateTransfused", "errors.required", "dateTransfused is required");
    } else {
      LocalDate currentDate = dateGeneratorService.generateLocalDate();
      if (currentDate.isBefore(transfusionDate)) {
        errors.rejectValue("dateTransfused", "errors.invalid", "dateTransfused must be in the past");
      } else if (component != null) {
        LocalDate createdOnDate = dateGeneratorService.generateLocalDate(component.getCreatedOn());
        if (transfusionDate.isBefore(createdOnDate)) {
          errors.rejectValue("dateTransfused", "errors.invalid.dateTransfusedAfterComponentCreated",
            "dateTransfused must be after the date that the component was created on");
        }
      }
    }
  }

  private void validateReceivedFromLocation(TransfusionBackingForm form, Errors errors) {
    if (form.getReceivedFrom() == null) {
      errors.rejectValue("receivedFrom", "errors.required", "receivedFrom is required");
    } else {
      try {
        Location recievedFromLocation = locationRepository.getLocation(form.getReceivedFrom().getId());
        if (!recievedFromLocation.getIsUsageSite()) {
          errors.rejectValue("receivedFrom", "errors.invalid", "receivedFrom Location must be of type Usage Site");
        }
      } catch (NoResultException e) {
        errors.rejectValue("receivedFrom", "errors.invalid", "receivedFrom Location specified is not valid");
      }
    }
  }

  private void validatePatientNames(TransfusionBackingForm form, Errors errors) {
    if (form.getPatient() == null) {
      errors.rejectValue("patient", "errors.required", "Patient is required");
    } else {
      if (StringUtils.isBlank(form.getPatient().getName1())) {
        errors.rejectValue("patient.name1", "errors.required", "The Patient name1 field is mandatory");
      } else if (form.getPatient().getName1().length() > OrderFormBackingFormValidator.MAX_LENGTH_PATIENT_NAME) {
        errors.rejectValue("patient.name1", "errors.invalid",
            "Maximum length for this field is " + OrderFormBackingFormValidator.MAX_LENGTH_PATIENT_NAME);
      }

      if (StringUtils.isBlank(form.getPatient().getName2())) {
        errors.rejectValue("patient.name2", "errors.required", "The Patient name2 field is mandatory");
      } else if (form.getPatient().getName2().length() > OrderFormBackingFormValidator.MAX_LENGTH_PATIENT_NAME) {
        errors.rejectValue("patient.name2", "errors.invalid",
            "Maximum length for this field is " + OrderFormBackingFormValidator.MAX_LENGTH_PATIENT_NAME);
      }
    }
  }

  @Override
  public String getFormName() {
    return "TransfusionBackingForm";
  }
}
