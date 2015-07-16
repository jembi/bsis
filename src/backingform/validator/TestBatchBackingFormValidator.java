package backingform.validator;

import backingform.TestBatchBackingForm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import repository.DonationBatchRepository;

public class TestBatchBackingFormValidator implements Validator {

    private DonationBatchRepository donationBatchRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Arrays.asList(TestBatchBackingForm.class, TestBatch.class).contains(clazz);
    }
    
    private Validator validator;

    public TestBatchBackingFormValidator(Validator validator, DonationBatchRepository donationBatchRepository) {
        this.validator = validator;
        this.donationBatchRepository = donationBatchRepository;
    }
    
    

    @Override
    public void validate(Object object, Errors errors) {

     if (object == null || validator == null)
      return;
    
    ValidationUtils.invokeValidator(validator, object, errors);
        TestBatchBackingForm form = (TestBatchBackingForm) object;
        TestBatch testBatch = form.getTestBatch();
        List<Integer> donationBatchIds = form.getDonationBatchIds();
        List<DonationBatch> donationBatches = new ArrayList<DonationBatch>();
        if (!donationBatchIds.isEmpty() && donationBatchIds != null) {
            for (Integer donationBatchId : donationBatchIds) {
                DonationBatch cb = donationBatchRepository.findDonationBatchById(donationBatchId);
                if(cb.getTestBatch() != null)
                    errors.rejectValue("donationBatchIds", "", "Donation  batch with id " + cb.getId() + " is already in test batch ");
                donationBatches.add(cb);
            }
        }
        testBatch.setDonationBatches(donationBatches);
    }

}
