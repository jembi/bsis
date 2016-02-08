package repository;

import java.io.File;
import java.util.List;

import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeReasonCategory;
import model.user.User;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the DiscardReasonRepository
 */
public class DiscardReasonRepositoryTest extends DBUnitContextDependentTestSuite {
	
	@Autowired
	DiscardReasonRepository discardReasonRepository;
	
	@Override
	protected IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/DiscardReasonRepositoryDataset.xml");
		return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
	}

    @Override
    protected User getLoggedInUser() throws Exception {
      return null;
    }

	@Test
	public void testGetAll() throws Exception {
		List<ComponentStatusChangeReason> all = discardReasonRepository.getAllDiscardReasons();
		Assert.assertNotNull("There are discard reasons defined", all);
		Assert.assertEquals("There are 6 discard reasons defined", 6, all.size());
	}
	
	@Test
	public void testGetDiscardReasonById() throws Exception {
		ComponentStatusChangeReason discardReason = discardReasonRepository.getDiscardReasonById(1l);
		Assert.assertNotNull("Discard reason with id 1 exists", discardReason);
	}
	
	@Test
	public void testFindDeferralReason() throws Exception {
		ComponentStatusChangeReason reason = discardReasonRepository.findDiscardReason("Incomplete Donation");
		Assert.assertNotNull("Discard reason exists", reason);
		Assert.assertEquals("Discard reason matches", "Incomplete Donation", reason.getStatusChangeReason());
	}
	
	@Test
	public void testFindDeferralReasonUnknown() throws Exception {
		ComponentStatusChangeReason reason = discardReasonRepository.findDiscardReason("Junit");
		Assert.assertNull("Discard reason does not exist", reason);
	}
	
	@Test
	public void testUpdateDeferralReason() throws Exception {
		ComponentStatusChangeReason reason = discardReasonRepository.getDiscardReasonById(1l);
		Assert.assertNotNull("Discard reason exists", reason);
		
		reason.setStatusChangeReason("Junit");
		discardReasonRepository.updateDiscardReason(reason);
		
		ComponentStatusChangeReason savedReason = discardReasonRepository.getDiscardReasonById(1l);
		Assert.assertNotNull("Discard reason still exists", savedReason);
		Assert.assertEquals("Reason has been updated", "Junit", savedReason.getStatusChangeReason());
	}
	
	@Test
	public void testSaveDeferralReason() throws Exception {
		ComponentStatusChangeReason reason = new ComponentStatusChangeReason();
		reason.setStatusChangeReason("Junit");
		reason.setCategory(ComponentStatusChangeReasonCategory.DISCARDED);
		discardReasonRepository.saveDiscardReason(reason);
		
		List<ComponentStatusChangeReason> all = discardReasonRepository.getAllDiscardReasons();
		Assert.assertNotNull("There are Discard reasons defined", all);
		Assert.assertEquals("There are 7 Discard reasons defined", 7, all.size());
	}
}
