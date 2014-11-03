package backingform.validator;

import backingform.TestBatchBackingForm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.collectionbatch.CollectionBatch;
import model.testbatch.TestBatch;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import repository.CollectionBatchRepository;

public class TestBatchBackingFormValidator implements Validator {

    private CollectionBatchRepository collectionBatchRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Arrays.asList(TestBatchBackingForm.class, TestBatch.class).contains(clazz);
    }
    
    private Validator validator;

    public TestBatchBackingFormValidator(Validator validator, CollectionBatchRepository collectionBatchRepository) {
        this.validator = validator;
        this.collectionBatchRepository = collectionBatchRepository;
    }
    
    

    @Override
    public void validate(Object object, Errors errors) {

     if (object == null || validator == null)
      return;
    
    ValidationUtils.invokeValidator(validator, object, errors);
        TestBatchBackingForm form = (TestBatchBackingForm) object;
        TestBatch testBatch = form.getTestBatch();
        List<Integer> collectionBatchIds = form.getCollectionBatchIds();
        List<CollectionBatch> collectionBatches = new ArrayList<CollectionBatch>();
        if (!collectionBatchIds.isEmpty() && collectionBatchIds != null) {
            for (Integer donationBatchId : collectionBatchIds) {
                CollectionBatch cb = collectionBatchRepository.findCollectionBatchById(donationBatchId);
                if(cb.getTestBatch() != null)
                    errors.rejectValue("collectionBatchIds", "", "Donation  batch with id " + cb.getId() + " is already in test batch ");
                collectionBatches.add(cb);
            }
        }
        testBatch.setCollectionBatches(collectionBatches);
    }

}
