package backingform.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import model.donation.Donation;
import model.donation.DonationConstants;
import model.donationbatch.DonationBatch;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.location.Location;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import utils.CustomDateFormatter;
import viewmodel.DonationViewModel;
import backingform.DonationBackingForm;
import controller.UtilController;

public class DonationBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public DonationBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
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
    if (obj == null || validator == null)
      return;

    ValidationUtils.invokeValidator(validator, obj, errors);

    DonationBackingForm form = (DonationBackingForm) obj;
    updateAutoGeneratedFields(form);

    Donation donation = form.getDonation();
    if (utilController.isDuplicateDonationIdentificationNumber(donation))
      errors.rejectValue("donation.donationIdentificationNumber", "donationIdentificationNumber.nonunique",
          "There exists a donation with the same donation identification number.");

    String donationDate = form.getDonationDate();
    if (!CustomDateFormatter.isDateStringValid(donationDate))
      errors.rejectValue("donation.donationDate", "dateFormat.incorrect",
          CustomDateFormatter.getDateErrorMessage());
    
    String bleedStartTime = form.getBleedStartTime();
    if (!CustomDateFormatter.isDateTimeStringValid(bleedStartTime))
      errors.rejectValue("donation.bleedStartTime", "timeFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());
    
    String bleedEndTime = form.getBleedEndTime();
    if (!CustomDateFormatter.isDateTimeStringValid(bleedEndTime))
      errors.rejectValue("donation.bleedEndTime", "timeFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());

    updateRelatedEntities(form);
    inheritParametersFromDonationBatch(form, errors);
    Donor donor = form.getDonor();
    if (donor != null) {
      String errorMessageDonorAge = utilController.verifyDonorAge(donor.getBirthDate());
      if (StringUtils.isNotBlank(errorMessageDonorAge))
        errors.rejectValue("donation.donor", "donor.age", errorMessageDonorAge);
      
      String errorMessageDonorDeferral = utilController.isDonorDeferred(donor);
      if (StringUtils.isNotBlank(errorMessageDonorDeferral))
        errors.rejectValue("donation.donor", "donor.deferral", errorMessageDonorDeferral);
      
      if (donor.getDonorStatus().equals(DonorStatus.POSITIVE_TTI))
        errors.rejectValue("donation.donor", "donor.tti", "Donor is not allowed to donate.");
    }

    if(donation.getBleedStartTime() != null || donation.getBleedEndTime() != null){
        validateBleedTimes(donation.getBleedStartTime(), donation.getBleedEndTime(), errors);
    }

    Location donorPanel = form.getDonation().getDonorPanel();
    if (donorPanel == null) {
      errors.rejectValue("donation.donorPanel", "donorPanel.empty",
        "Donor Panel is required.");
    } 
    else if (utilController.isDonorPanel(donorPanel.getId()) == false) {
      errors.rejectValue("donation.donorPanel", "donorPanel.invalid",
    	"Location is not a Donor Panel.");
    } 

    validateBloodPressure(form,errors);
    utilController.commonFieldChecks(form, "donation", errors);
  }
  
  public void validateBleedTimes(Date bleedStartTime, Date bleedEndTime, Errors errors){
      if(bleedStartTime == null){
          errors.rejectValue("donation.bleedStartTime", "", "This is required");
          return;
      }
      if(bleedEndTime == null){
          errors.rejectValue("donation.bleedEndTime", "", "This is required");
          return;
      }
      if(bleedStartTime.after(bleedEndTime))
          errors.rejectValue("donation", "", "Bleed End time should be after start time");

  }
  private void validateBloodPressure(DonationBackingForm donationForm, Errors errors)
  {
	 Integer bloodPressureSystolic = null;
         Integer bloodPressureDiastolic = null;
         
         if(donationForm.getBloodPressureSystolic() != null)
             bloodPressureSystolic = donationForm.getBloodPressureSystolic();
         
         if(donationForm.getBloodPressureDiastolic() != null)
             bloodPressureDiastolic = donationForm.getBloodPressureDiastolic();

	
	 if( bloodPressureSystolic != null || bloodPressureDiastolic != null)
	  {
		 
		   if(bloodPressureSystolic == null || !( bloodPressureSystolic >= DonationConstants.BLOOD_PRESSURE_MIN_VALUE && 
                         bloodPressureSystolic  <= DonationConstants.BLOOD_PRESSURE_SYSTOLIC_MAX_VALUE))
			  		errors.rejectValue("donation.bloodPressureSystolic","bloodPressureSystolic.incorrect" ,"Enter a value between 0 to 250.");
	             
	
			  	if(bloodPressureDiastolic == null || !( bloodPressureDiastolic >= DonationConstants.BLOOD_PRESSURE_MIN_VALUE && 
                                        bloodPressureDiastolic <= DonationConstants.BLOOD_PRESSURE_DIASTOLIC_MAX_VALUE))
			  		errors.rejectValue("donation.bloodPressureDiastolic","bloodPressureDiastolic.incorrect" ,"Enter a value between 0 to 150.");
	  }
	  return;
			  
  }
		 

  
  private void inheritParametersFromDonationBatch(
      DonationBackingForm form, Errors errors) {
    if (form.getUseParametersFromBatch()) {
      DonationBatch donationBatch = form.getDonationBatch();
      if (donationBatch == null) {
        errors.rejectValue("donation.donationBatch", "donationbatch.notspecified", "Donation batch should be specified");
        return;
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
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
