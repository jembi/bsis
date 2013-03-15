package model.collectionbatch;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.CollectionBatchRepository;

@Component
public class CollectionBatchExistsConstraintValidator implements
    ConstraintValidator<CollectionBatchExists, CollectionBatch> {

  @Autowired
  private CollectionBatchRepository collectionBatchRepository;

  public CollectionBatchExistsConstraintValidator() {
  }
  
  @Override
  public void initialize(CollectionBatchExists constraint) {
  }

  public boolean isValid(CollectionBatch target, ConstraintValidatorContext context) {

    if (target == null)
      return true;

    try {

       CollectionBatch collectionBatch = null;
       if (target.getId() != null) {
         collectionBatch = collectionBatchRepository.findCollectionBatchById(target.getId());
       }
       else if (target.getBatchNumber() != null) {

         if (target.getBatchNumber().isEmpty())
           return true;

         collectionBatch = 
           collectionBatchRepository.findCollectionBatchByBatchNumber(target.getBatchNumber());
       }
       if (collectionBatch != null) {
         return true;
       }
     } catch (Exception e) {
        e.printStackTrace();
     }
     return false;
  }

  public void setDonorRepository(CollectionBatchRepository collectionBatchRepository) {
    this.collectionBatchRepository = collectionBatchRepository;
  }
}