package backingform.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import model.collectedsample.CollectedSample;
import model.collectedsample.CollectionConstants;
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
import viewmodel.CollectedSampleViewModel;
import backingform.CollectedSampleBackingForm;
import backingform.DonorBackingForm;
import backingform.FindCollectedSampleBackingForm;
import backingform.WorksheetBackingForm;
import controller.UtilController;

public class CollectedSampleBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public CollectedSampleBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(FindCollectedSampleBackingForm.class,
                         CollectedSampleBackingForm.class,
                         CollectedSample.class,
                         CollectedSampleViewModel.class,
                         WorksheetBackingForm.class
                         ).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;

    ValidationUtils.invokeValidator(validator, obj, errors);

    CollectedSampleBackingForm form = (CollectedSampleBackingForm) obj;
    updateAutoGeneratedFields(form);

    if (utilController.isDuplicateCollectionNumber(form.getCollectedSample()))
      errors.rejectValue("collectedSample.collectionNumber", "collectionNumber.nonunique",
          "There exists a collection with the same collection number.");

    String collectedOn = form.getCollectedOn();
    if (!CustomDateFormatter.isDateTimeStringValid(collectedOn))
      errors.rejectValue("collectedSample.collectedOn", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());

    updateRelatedEntities(form);
    inheritParametersFromCollectionBatch(form, errors);
    Donor donor = form.getDonor();
    if (donor != null) {
      String errorMessageDonorAge = utilController.verifyDonorAge(donor.getBirthDate());
      if (StringUtils.isNotBlank(errorMessageDonorAge))
        errors.rejectValue("collectedSample.donor", "donor.age", errorMessageDonorAge);
      
      String errorMessageDonorDeferral = utilController.isDonorDeferred(donor);
      if (StringUtils.isNotBlank(errorMessageDonorDeferral))
        errors.rejectValue("collectedSample.donor", "donor.deferral", errorMessageDonorDeferral);
      
      if (donor.getDonorStatus().equals(DonorStatus.POSITIVE_TTI))
        errors.rejectValue("collectedSample.donor", "donor.tti", "Donor is not allowed to donate.");
    }

    validateRangeForHaemoglobinCount(form,errors);
    validateRangeForDonorPulse(form, errors);
    validateRangeDonorWeight(form,errors);
    validateBloodPressure(form,errors);
    utilController.commonFieldChecks(form, "collectedSample", errors);
  }
  
  private void validateBloodPressure(CollectedSampleBackingForm collectionForm, Errors errors)
  {
	 String bloodPressureSyStolic="";
	 if(!StringUtils.isBlank(collectionForm.getBloodPressureSystolic()))
		 bloodPressureSyStolic = collectionForm.getBloodPressureSystolic();
	 
	 String bloodPressureDiastolic="";
	 if(!StringUtils.isBlank(collectionForm.getBloodPressureDiastolic()))
		 bloodPressureDiastolic = collectionForm.getBloodPressureDiastolic();
	
	 String regex="[0-9]+"; 
	 if(!bloodPressureSyStolic.isEmpty()  || !bloodPressureDiastolic.isEmpty())
	  {
		 
		   if(!bloodPressureSyStolic.matches(regex) || !( Integer.parseInt(collectionForm.getBloodPressureSystolic()) >= CollectionConstants.BLOOD_PRESSURE_MIN_VALUE && Integer.parseInt(collectionForm.getBloodPressureSystolic()) <= CollectionConstants.BLOOD_PRESSURE_SYSTOLIC_MAX_VALUE))
			  		errors.rejectValue("collectedSample.bloodPressureSystolic","bloodPressureSystolic.incorrect" ,"Enter a value between 0 to 250.");
	             
	
			  	if(!bloodPressureDiastolic.matches(regex) || !( (Integer.parseInt(collectionForm.getBloodPressureDiastolic()) >= CollectionConstants.BLOOD_PRESSURE_MIN_VALUE && Integer.parseInt( collectionForm.getBloodPressureDiastolic() )<= CollectionConstants.BLOOD_PRESSURE_DIASTOLIC_MAX_VALUE)))
			  		errors.rejectValue("collectedSample.bloodPressureDiastolic","bloodPressureDiastolic.incorrect" ,"Enter a value between 0 to 150.");
	  }
	  return;
			  
  }
		 
			 
  private void validateRangeForHaemoglobinCount(CollectedSampleBackingForm form, Errors errors) {
  
  	if(!StringUtils.isBlank(form.getHaemoglobinCount())){
	String haemoglobinCount = form.getHaemoglobinCount();
   	String regex="[0-9]*\\.?[0-9]+";
   	if( !haemoglobinCount.matches(regex) ||! (Float.parseFloat(haemoglobinCount) >= 0 && Float.parseFloat(haemoglobinCount)<= 30))
   		errors.rejectValue("collectedSample.haemoglobinCount","haemoglobinCount.incorrect" ,"Enter a value between 0 and 30.");
    }
return;  	
  	
}
  
  private void validateRangeDonorWeight(CollectedSampleBackingForm form, Errors errors) {
  	 if(!StringUtils.isBlank(form.getDonorWeight())){
  		String donorWeight = form.getDonorWeight();
    	String regex="[0-9]*\\.?[0-9]+";
    	if( !donorWeight.matches(regex) || !(Float.parseFloat(donorWeight) >= 0 && Float.parseFloat(donorWeight)<= 300))
    	errors.rejectValue("collectedSample.donorWeight","donorWeight.incorrect" ,"Enter a value between 0 and 300.");
     }
  	 
 return;
  	
  }
  
  private void validateRangeForDonorPulse(CollectedSampleBackingForm form, Errors errors) {
  	
  	
	 if(!StringUtils.isBlank(form.getDonorPulse())){
   
	  String donorPulse = form.getDonorPulse();
      String regex="[0-9]+";
  	  if( !donorPulse.matches(regex) || !(Integer.parseInt(donorPulse) >= 0 && Integer.parseInt(donorPulse)<= 290))
  		errors.rejectValue("collectedSample.donorPulse","donorPulse.incorrect" ,"Enter a value between 0 to 290.");
   }

       return;
  	
  }
  
  private void inheritParametersFromCollectionBatch(
      CollectedSampleBackingForm form, Errors errors) {
    if (form.getUseParametersFromBatch()) {
      DonationBatch donationBatch = form.getDonationBatch();
      if (donationBatch == null) {
        errors.rejectValue("collectedSample.donationBatch", "collectionbatch.notspecified", "Collection batch should be specified");
        return;
      }
      Location center = donationBatch.getDonationCenter();
      if (center == null) {
        errors.rejectValue("useParametersFromBatch", "collectionCenter.notspecified",
            "Collection center not present in batch and is required.");
      } else {
        form.setCollectionCenter(center.getId().toString());
      }
      Location site = donationBatch.getDonationSite();
      if (site == null) {
        errors.rejectValue("useParametersFromBatch", "collectionSite.notspecified",
            "Collection site not present in batch and is required.");
      } else {
        form.setCollectionSite(site.getId().toString());
      }
    }
  }

  private void updateAutoGeneratedFields(CollectedSampleBackingForm form) {
    if (StringUtils.isBlank(form.getCollectionNumber()) &&
        utilController.isFieldAutoGenerated("collectedSample", "collectionNumber")) {
      form.setCollectionNumber(utilController.getNextCollectionNumber());
    }
    if (StringUtils.isBlank(form.getCollectedOn()) &&
        utilController.doesFieldUseCurrentTime("collectedSample", "collectedOn")) {
      form.getCollectedSample().setCollectedOn(new Date());
    }
  }
  


  @SuppressWarnings("unchecked")
  private void updateRelatedEntities(CollectedSampleBackingForm form) {
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
