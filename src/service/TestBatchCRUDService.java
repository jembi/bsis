package service;

import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.TestBatchRepository;

@Service
@Transactional
public class TestBatchCRUDService {

    private static final Logger LOGGER = Logger.getLogger(TestBatchCRUDService.class);

    @Autowired
    private TestBatchRepository testBatchRepository;
    @Autowired
    private TestBatchConstraintChecker testBatchConstraintChecker;
    @Autowired
    private TestBatchStatusChangeService testBatchStatusChangeService;

    public TestBatch updateTestBatchStatus(Long testBatchId, TestBatchStatus newStatus) {

        LOGGER.info("Updating status of test batch " + testBatchId + " to " + newStatus);

        TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);

        if (newStatus == testBatch.getStatus()) {
            // The status is not being changed so return early
            return testBatch;
        }

        if (newStatus == TestBatchStatus.RELEASED) {

            if (!testBatchConstraintChecker.canReleaseTestBatch(testBatch)) {
                throw new IllegalStateException("Test batch cannot be released");
            }

            testBatchStatusChangeService.handleRelease(testBatch);

        } else if (newStatus == TestBatchStatus.CLOSED && !testBatchConstraintChecker.canCloseTestBatch(testBatch)) {

            throw new IllegalStateException("Only released test batches can be closed");
        }

        // Set the new status
        testBatch.setStatus(newStatus);

        return testBatchRepository.updateTestBatch(testBatch);
    }

}
