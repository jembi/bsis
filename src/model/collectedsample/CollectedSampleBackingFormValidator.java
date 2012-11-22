package model.collectedsample;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import repository.BloodBagTypeRepository;
import repository.DonorRepository;
import repository.DonorTypeRepository;

public class CollectedSampleBackingFormValidator implements Validator {

  private Validator validator;
  private DonorRepository donorRepository;
  private DonorTypeRepository donorTypeRepository;
  private BloodBagTypeRepository bloodBagTypeRepository;

  public CollectedSampleBackingFormValidator(Validator validator,
                                             DonorRepository donorRepository,
                                             DonorTypeRepository donorTypeRepository,
                                             BloodBagTypeRepository bloodBagTypeRepository) {
    super();
    this.validator = validator;
    this.donorRepository = donorRepository;
    this.donorTypeRepository = donorTypeRepository;
    this.bloodBagTypeRepository = bloodBagTypeRepository;
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
    if (!donorTypeRepository.isDonorTypeValid(form.getDonorType())) {
      errors.rejectValue("collectedSample.donorType",
                         "donorType.invalid",
                         "Invalid Donor Type Specified");
    }
    if (!bloodBagTypeRepository.isBloodBagTypeValid(form.getBloodBagType())) {
      errors.rejectValue("collectedSample.bloodBagType",
                         "bloodBagType.invalid",
                         "Invalid Blood Bag Type Specified");
    }
  }
}
