package backingform;

import java.util.List;
import java.util.ArrayList;
import javax.validation.Valid;
import model.testbatch.TestBatch;
import model.collectionbatch.CollectionBatch;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TestBatchBackingForm {
	
	@Valid
	@JsonIgnore
	private TestBatch testBatch;
	
	public TestBatchBackingForm() {
		testBatch = new TestBatch();
	}
	
	public TestBatch getTestBatch() {
	    return testBatch;
	}
	
	public void setTestBatch(TestBatch testBatch) {
	    this.testBatch = testBatch;
	}

    public List<Integer> getCollectionBatchIds() {
    	List<CollectionBatch> collectionBatches = testBatch.getCollectionBatches();
    	List<Integer> collectionBatchIds = new ArrayList<Integer>();
    	if(!collectionBatches.isEmpty() && collectionBatches != null){
	    	for (CollectionBatch collectionBatch : collectionBatches){
	    		Integer cbId = collectionBatch.getId();
	    		collectionBatchIds.add(cbId);
	    	}
    	}
        return collectionBatchIds;
    }

    public void setCollectionBatches(List<Integer> collectionBatchIds) {
    	List<CollectionBatch> collectionBatches = new ArrayList<CollectionBatch>();
    	if(!collectionBatchIds.isEmpty() && collectionBatchIds != null){
	    	for (Integer donationBatchId : collectionBatchIds){
	    		CollectionBatch cb = new CollectionBatch();
	    		cb.setId(donationBatchId);
	    		collectionBatches.add(cb);
	    	}
    	}
    	testBatch.setCollectionBatches(collectionBatches);
    }

}
