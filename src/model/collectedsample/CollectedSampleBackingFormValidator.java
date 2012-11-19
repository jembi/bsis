package model.collectedsample;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import repository.BloodBagTypeRepository;
import repository.DonorTypeRepository;

public class CollectedSampleBackingFormValidator implements Validator {

  public Validator validator;
  @Autowired
  public DonorTypeRepository donorTypeRepository;
  @Autowired
  public BloodBagTypeRepository bloodBagTypeRepository;

  public CollectedSampleBackingFormValidator(Validator validator) {
    super();
    this.validator = validator;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(CollectedSampleBackingForm.class, CollectedSample.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    CollectedSampleBackingForm form = (CollectedSampleBackingForm) obj;
    if (!donorTypeRepository.isDonorTypeValid(form.getDonorType()))
      errors.rejectValue("collectedSample.donorType",
                         "donorType.invalid",
                         "Invalid Donor Type Specified");
    if (!bloodBagTypeRepository.isBloodBagTypeValid(form.getBloodBagType()))
      errors.rejectValue("collectedSample.bloodBagType",
                         "bloodBagType.invalid",
                         "Invalid Blood Bag Type Specified");
  }
}
