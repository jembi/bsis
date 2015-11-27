package backingform.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import repository.DonationBatchRepository;
import backingform.TestBatchBackingForm;

@Component
public class TestBatchBackingFormValidator implements Validator {

	@Autowired
    private DonationBatchRepository donationBatchRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Arrays.asList(TestBatchBackingForm.class, TestBatch.class).contains(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {

     if (object == null)
      return;

        TestBatchBackingForm form = (TestBatchBackingForm) object;
        TestBatch testBatch = form.getTestBatch();
        List<Integer> donationBatchIds = form.getDonationBatchIds();
        List<DonationBatch> donationBatches = new ArrayList<DonationBatch>();
        if (donationBatchIds != null && !donationBatchIds.isEmpty()) {
            for (Integer donationBatchId : donationBatchIds) {
                DonationBatch db = donationBatchRepository.findDonationBatchById(donationBatchId);
                if (db.getTestBatch() != null) {
                	if (testBatch.getId() == null || !testBatch.getId().equals(db.getTestBatch().getId())) {
                		errors.rejectValue("donationBatchIds", "", "Donation batch at " + db.getVenue().getName() + " from " + db.getCreatedDate() + " is already in a test batch.");
                	}
                }
                donationBatches.add(db);
            }
        }
        testBatch.setDonationBatches(donationBatches);
    }

}
