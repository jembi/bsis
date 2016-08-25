package org.jembi.bsis.backingform.validator;

import javax.persistence.NoResultException;

import org.apache.log4j.Logger;
import org.jembi.bsis.backingform.DivisionBackingForm;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.DivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class DivisionBackingFormValidator extends BaseValidator<DivisionBackingForm> {

  private static final Logger LOGGER = Logger.getLogger(DivisionBackingFormValidator.class);

  @Autowired
  private DivisionRepository divisionRepository;

  @Override
  public void validateForm(DivisionBackingForm form, Errors errors) {
    commonFieldChecks(form, errors);

    // Validate name
    if (form.getName() != null && isDuplicateDivisionName(form)) {
      errors.rejectValue("name", "duplicate", "Division name already exists");
    }

    // Validate parent
    Division parent = null;
    if (form.getParent() != null) {
      parent = divisionRepository.findDivisionById(form.getParent().getId());
      if (parent == null) {
        errors.rejectValue("parent", "invalid", "Parent division is invalid");
      }
    }

    // Validate level
    Integer level = form.getLevel();
    if (level != null) {

      if (level < 1 || level > 3) {
        errors.rejectValue("level", "invalid", "Invalid level");
      } else {

        if (level == 2) {
          if (form.getParent() == null) {
            errors.rejectValue("parent", "required", "Parent division is required.");
          } else if (parent != null && parent.getLevel() != 1) {
            errors.rejectValue("parent", "invalid", "Parent division is invalid");
          }
        } else if (level == 3) {
          if (form.getParent() == null) {
            errors.rejectValue("parent", "required", "Parent division is required");
          } else if (parent != null && parent.getLevel() != 2) {
            errors.rejectValue("parent", "invalid", "Parent division is invalid");
          }
        }
      }
    }
  }

  @Override
  public String getFormName() {
    return "division";
  }

  @Override
  protected boolean formHasBaseEntity() {
    return false;
  }

  private boolean isDuplicateDivisionName(DivisionBackingForm form) {
    Division existingDivision = null;
    try {
      existingDivision = divisionRepository.findDivisionByName(form.getName());
      if (!existingDivision.getId().equals(form.getId())) {
        return true;
      }
    } catch (NoResultException e) {
      // ignore
    }
    return false;
  }

}
