package backingform.validator;

import backingform.DonationBackingForm;
import controller.UtilController;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.donor.Donor;
import model.location.Location;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.CustomDateFormatter;
import viewmodel.DonationViewModel;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class DonationBackingFormValidator implements Validator {

  private UtilController utilController;
  private AdverseEventBackingFormValidator adverseEventBackingFormValidator;

  public DonationBackingFormValidator(UtilController utilController,
                                      AdverseEventBackingFormValidator adverseEventBackingFormValidator) {
    super();
    this.utilController = utilController;
    this.adverseEventBackingFormValidator = adverseEventBackingFormValidator;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(DonationBackingForm.class,
        Donation.class,
        DonationViewModel.class
    ).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {

    if (obj == null)
      return;

    DonationBackingForm form = (DonationBackingForm) obj;
    updateAutoGeneratedFields(form);

    Donation donation = form.getDonation();
    if (utilController.isDuplicateDonationIdentificationNumber(donation))
      errors.rejectValue("donation.donationIdentificationNumber", "donationIdentificationNumber.nonunique",
          "There exists a donation with the same donation identification number.");

    String donationDate = form.getDonationDate();
    if (!CustomDateFormatter.isDateStringValid(donationDate))
      errors.rejectValue("donation.donationDate", "donationDate.incorrect",
          CustomDateFormatter.getDateErrorMessage());

    updateRelatedEntities(form);
    inheritParametersFromDonationBatch(form, errors);
    Donor donor = form.getDonor();
    if (donor == null) {
      errors.rejectValue("donation.donor", "donor.invalid", "Please supply a valid donor");
    }

    if (donation.getBleedStartTime() != null || donation.getBleedEndTime() != null) {
      validateBleedTimes(donation.getBleedStartTime(), donation.getBleedEndTime(), errors);
    }

    Location venue = form.getDonation().getVenue();
    if (venue == null) {
      errors.rejectValue("donation.venue", "venue.empty",
          "Venue is required.");
    } else if (!utilController.isVenue(venue.getId())) {
      errors.rejectValue("donation.venue", "venue.invalid",
          "Location is not a Venue.");
    }

    validateBloodPressure(form, errors);
    validateHaemoglobinCount(form, errors);
    validateWeight(form, errors);
    validatePulse(form, errors);

    adverseEventBackingFormValidator.validate(form.getAdverseEvent(), errors);

    utilController.commonFieldChecks(form, "donation", errors);
  }

  private void validateBleedTimes(Date bleedStartTime, Date bleedEndTime, Errors errors) {
    if (bleedStartTime == null) {
      errors.rejectValue("donation.bleedStartTime", "bleedStartTime.empty", "This is required");
      return;
    }
    if (bleedEndTime == null) {
      errors.rejectValue("donation.bleedEndTime", "bleedEndTime.empty", "This is required");
      return;
    }
    if (bleedStartTime.after(bleedEndTime))
      errors.rejectValue("donation", "bleedEndTime.outOfRange", "Bleed End time should be after start time");

  }

  private void validateBloodPressure(DonationBackingForm donationBackingForm, Errors errors) {

    Integer bloodPressureSystolic = null;
    Integer bloodPressureDiastolic = null;

    Integer bloodPressureSystolicMin = Integer.parseInt(utilController.getGeneralConfigValueByName("donation.donor.bpSystolicMin"));
    Integer bloodPressureSystolicMax = Integer.parseInt(utilController.getGeneralConfigValueByName("donation.donor.bpSystolicMax"));

    Integer bloodPressureDiastolicMin = Integer.parseInt(utilController.getGeneralConfigValueByName("donation.donor.bpDiastolicMin"));
    Integer bloodPressureDiastolicMax = Integer.parseInt(utilController.getGeneralConfigValueByName("donation.donor.bpDiastolicMax"));

    if (donationBackingForm.getBloodPressureSystolic() != null) {
      bloodPressureSystolic = donationBackingForm.getBloodPressureSystolic();
      if (bloodPressureSystolic < bloodPressureSystolicMin)
        errors.rejectValue("donation.bloodPressureSystolic", "bloodPressureSystolic.outOfRange", "BP value should be above " + bloodPressureSystolicMin);
      if (bloodPressureSystolic > bloodPressureSystolicMax)
        errors.rejectValue("donation.bloodPressureSystolic", "bloodPressureSystolic.outOfRange", "BP value should be below " + bloodPressureSystolicMax);
    }

    if (donationBackingForm.getBloodPressureDiastolic() != null) {
      bloodPressureDiastolic = donationBackingForm.getBloodPressureDiastolic();
      if (bloodPressureDiastolic < bloodPressureDiastolicMin)
        errors.rejectValue("donation.bloodPressureDiastolic", "bloodPressureDiastolic.outOfRange", "BP value should be above " + bloodPressureDiastolicMax);
      if (bloodPressureDiastolic > bloodPressureDiastolicMax)
        errors.rejectValue("donation.bloodPressureDiastolic", "bloodPressureDiastolic.outOfRange", "BP value should be below " + bloodPressureDiastolicMax);
    }
  }

  private void validateHaemoglobinCount(DonationBackingForm donationBackingForm, Errors errors) {
    Integer haemoglobinCount = null;
    Integer hbMin = Integer.parseInt(utilController.getGeneralConfigValueByName("donation.donor.hbMin"));
    Integer hbMax = Integer.parseInt(utilController.getGeneralConfigValueByName("donation.donor.hbMax"));

    if (donationBackingForm.getHaemoglobinCount() != null) {
      haemoglobinCount = donationBackingForm.getHaemoglobinCount().intValue();
      if (haemoglobinCount < hbMin)
        errors.rejectValue("donation.haemoglobinCount", "haemoglobinCount.outOfRange", "Hb value should be above " + hbMin);
      if (haemoglobinCount > hbMax)
        errors.rejectValue("donation.haemoglobinCount", "haemoglobinCount.outOfRange", "Hb value should be below " + hbMax);
    }
  }

  private void validateWeight(DonationBackingForm donationBackingForm, Errors errors) {
    Integer weight = null;
    Integer weightMin = Integer.parseInt(utilController.getGeneralConfigValueByName("donation.donor.weightMin"));
    Integer weightMax = Integer.parseInt(utilController.getGeneralConfigValueByName("donation.donor.weightMax"));

    if (donationBackingForm.getDonorWeight() != null) {
      weight = donationBackingForm.getDonorWeight().intValue();
      if (weight < weightMin)
        errors.rejectValue("donation.donorWeight", "donorWeight.outOfRange", "Weight value should be above " + weightMin);
      if (weight > weightMax)
        errors.rejectValue("donation.donorWeight", "donorWeight.outOfRange", "Weight value should be below " + weightMax);
    }
  }

  private void validatePulse(DonationBackingForm donationBackingForm, Errors errors) {
    Integer pulse = null;
    Integer pulseMin = Integer.parseInt(utilController.getGeneralConfigValueByName("donation.donor.pulseMin"));
    Integer pulseMax = Integer.parseInt(utilController.getGeneralConfigValueByName("donation.donor.pulseMax"));

    if (donationBackingForm.getDonorPulse() != null) {
      pulse = donationBackingForm.getDonorPulse();
      if (pulse < pulseMin)
        errors.rejectValue("donation.donorPulse", "donorPulse.outOfRange", "Pulse value should be above " + pulseMin);
      if (pulse > pulseMax)
        errors.rejectValue("donation.donorPulse", "donorPulse.outOfRange", "Pulse value should be below " + pulseMax);
    }
  }


  private void inheritParametersFromDonationBatch(
      DonationBackingForm form, Errors errors) {
    if (form.getUseParametersFromBatch()) {
      DonationBatch donationBatch = form.getDonationBatch();
      if (donationBatch == null) {
        errors.rejectValue("donation.donationBatch", "donationBatch.empty", "Donation batch should be specified");
      }
    }
  }

  private void updateAutoGeneratedFields(DonationBackingForm form) {
    if (StringUtils.isBlank(form.getDonationIdentificationNumber()) &&
        utilController.isFieldAutoGenerated("donation", "donationIdentificationNumber")) {
      form.setDonationIdentificationNumber(utilController.getNextDonationIdentificationNumber());
    }
    if (StringUtils.isBlank(form.getDonationDate()) &&
        utilController.doesFieldUseCurrentTime("donation", "donationDate")) {
      form.getDonation().setDonationDate(new Date());
    }
  }


  @SuppressWarnings("unchecked")
  private void updateRelatedEntities(DonationBackingForm form) {
    Map<String, Object> bean = null;
    try {
      bean = BeanUtils.describe(form);
      Donor donor = utilController.findDonorInForm(bean);
      form.setDonor(donor);
      DonationBatch donationBatch = utilController.findDonationBatchInForm(bean);
      form.setDonationBatch(donationBatch);
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
