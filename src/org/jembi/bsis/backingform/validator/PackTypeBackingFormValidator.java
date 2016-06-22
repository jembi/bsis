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
    if (!form.getType().getTestSampleProduced() && form.getType().getCountAsDonation()) {
      errors.rejectValue("type.countAsDonation", "countAsDonation.notAllowed", "Pack types that don't produce " +
          "a test sample cannot be counted as a donation");
    }

    if (isDuplicatePackTypeName(form.getType())) {
      errors.rejectValue("type.packType", "name.exists", "Pack Type name already exists.");
    }
  }

  @Override
  public String getFormName() {
    return "packType";
  }

  private boolean isDuplicatePackTypeName(PackType packType) {
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
