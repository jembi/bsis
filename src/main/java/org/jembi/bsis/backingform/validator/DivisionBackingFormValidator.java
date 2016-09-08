package org.jembi.bsis.backingform.validator;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.DivisionBackingForm;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.DivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class DivisionBackingFormValidator extends BaseValidator<DivisionBackingForm> {

  @Autowired
  private DivisionRepository divisionRepository;

  @Override
  public void validateForm(DivisionBackingForm form, Errors errors) {
    commonFieldChecks(form, errors);

    // Validate name
    if (form.getName() != null && isDuplicateDivision(form)) {
      errors.rejectValue("name", "duplicate", "Division name already exists");
    }

    // Validate parent
    Division parent = validateParent(form, errors);

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

  private Division validateParent(DivisionBackingForm form, Errors errors) {
    Division parent = null;
    if (form.getParent() != null) {
      if (form.getParent().getId() == null) {
        errors.rejectValue("parent.id", "required", "Parent division id is required");
      } else {
        try {
          parent = divisionRepository.findDivisionById(form.getParent().getId());
        } catch (NoResultException e) {
          errors.rejectValue("parent", "invalid", "Parent division is invalid");
        }
      }
    }
    return parent;
  }

  private boolean isDuplicateDivision(DivisionBackingForm form) {
    Division existingDivision = divisionRepository.findDivisionByName(form.getName());
    // If there is an existing division and the division IDs are different then this is an invalid
    // duplicate.
    if (existingDivision != null && !existingDivision.getId().equals(form.getId())) {
      return true;
    }
    return false;
  }

}
