package constraintvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import model.collectedsample.CollectedSample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.CollectedSampleRepository;

@Component
public class CollectedSampleExistsConstraintValidator implements
    ConstraintValidator<CollectedSampleExists, CollectedSample> {

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  public CollectedSampleExistsConstraintValidator() {
  }
  
  @Override
  public void initialize(CollectedSampleExists constraint) {
  }

  public boolean isValid(CollectedSample target, ConstraintValidatorContext context) {

   if (target == null)
     return true;

   try {

      CollectedSample collectedSample = null;
      if (target.getId() != null) {
        collectedSample = collectedSampleRepository.findCollectedSampleById(target.getId());
      }
      else if (target.getCollectionNumber() != null) {

        if (target.getCollectionNumber().isEmpty())
          return true;

        collectedSample = 
          collectedSampleRepository.findCollectedSampleByCollectionNumber(target.getCollectionNumber());
      }
      if (collectedSample != null) {
        return true;
      }
    } catch (Exception e) {
       e.printStackTrace();
    }
    return false;
  }

  public void setDonorRepository(CollectedSampleRepository collectedSampleRepository) {
    this.collectedSampleRepository = collectedSampleRepository;
  }
}