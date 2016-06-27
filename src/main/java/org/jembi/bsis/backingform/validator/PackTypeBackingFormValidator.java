package org.jembi.bsis.backingform.validator;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.PackTypeBackingForm;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.PackTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class PackTypeBackingFormValidator extends BaseValidator<PackTypeBackingForm> {

  @Autowired
  private PackTypeRepository packTypeRepository;

  @Override
  public void validateForm(PackTypeBackingForm form, Errors errors) {
    if (!form.getTestSampleProduced() && form.getCountAsDonation()) {
      errors.rejectValue("countAsDonation", "countAsDonation.notAllowed", "Pack types that don't produce " +
          "a test sample cannot be counted as a donation");
    }

    if (isDuplicatePackTypeName(form)) {
      errors.rejectValue("packType", "name.exists", "Pack Type name already exists.");
    }
  }

  @Override
  public String getFormName() {
    return "packType";
  }

  @Override
  protected boolean formHasBaseEntity() {
    return false;
  }

  private boolean isDuplicatePackTypeName(PackTypeBackingForm packType) {
    String packTypeName = packType.getPackType();
    if (StringUtils.isBlank(packTypeName)) {
      return false;
    }

    PackType existingPackType = packTypeRepository.findPackTypeByName(packTypeName);
    if (existingPackType != null && !existingPackType.getId().equals(packType.getId())) {
      return true;
    }

    return false;
  }
}
