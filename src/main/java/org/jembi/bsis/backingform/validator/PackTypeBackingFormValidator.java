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
    
    if (form.getCountAsDonation()) {
      if (form.getMinWeight() == null) {
        errors.rejectValue("minWeight", "minWeight.required", "minWeight required for Pack types that can produce Components");
      } else {
        Integer minWeight = form.getMinWeight();
        if (minWeight <= 0 || minWeight >= 1000) {
          errors.rejectValue("minWeight", "minWeight.invalid", "minWeight should be between 0 and 1000");
        }
      }
      if (form.getMaxWeight() == null) {
        errors.rejectValue("maxWeight", "maxWeight.required", "maxWeight required for Pack types that can produce Components");
      } else {
        Integer maxWeight = form.getMaxWeight();
        if (maxWeight <= 0 || maxWeight >= 1000) {
          errors.rejectValue("maxWeight", "maxWeight.invalid", "maxWeight should be between 0 and 1000");
        }
      }
      if (form.getLowVolumeWeight() == null) {
        errors.rejectValue("lowVolumeWeight", "lowVolumeWeight.required", "lowVolumeWeight required for Pack types that can produce Components");
      } else {
        Integer lowVolumeWeight = form.getLowVolumeWeight();
        if (lowVolumeWeight <= 0 || lowVolumeWeight >= 1000) {
          errors.rejectValue("lowVolumeWeight", "lowVolumeWeight.invalid", "lowVolumeWeight should be between 0 and 1000");
        }
      }
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
