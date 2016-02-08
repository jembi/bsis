package repository;

import java.io.File;
import java.util.List;
import java.util.Map;

import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeReasonCategory;
import model.user.User;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the ComponentStatusChangeReasonRepository
 */
public class ComponentStatusChangeReasonRepositoryTest extends DBUnitContextDependentTestSuite {
	
	@Autowired
	ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;
	
	@Override
	protected IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/ComponentStatusChangeReasonRepositoryDataset.xml");
		return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
	}
	
    @Override
    protected User getLoggedInUser() throws Exception {
      return null;
    }

	@Test
	public void testGetAll() throws Exception {
		List<ComponentStatusChangeReason> all = componentStatusChangeReasonRepository.getAllComponentStatusChangeReasons();
		Assert.assertNotNull("There are ComponentStatusChangeReason", all);
		Assert.assertEquals("There are 11 ComponentStatusChangeReason", 11, all.size());
	}
	
	@Test
	public void testGetComponentStatusChangeReasons() throws Exception {
		List<ComponentStatusChangeReason> all = componentStatusChangeReasonRepository
		        .getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.DISCARDED);
		Assert.assertNotNull("There are DISCARDED ComponentStatusChangeReason", all);
		Assert.assertEquals("There are 6 DISCARDED ComponentStatusChangeReason", 6, all.size());
	}
	
	@Test
	public void testGetComponentStatusChangeReasonsNone() throws Exception {
		List<ComponentStatusChangeReason> all = componentStatusChangeReasonRepository
		        .getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.ISSUED);
		Assert.assertNotNull("Doesn't return a null list", all);
		Assert.assertEquals("There are 0 ISSUED ComponentStatusChangeReason", 0, all.size());
	}
	
	@Test
	public void testGetComponentStatusChangeReasonById() throws Exception {
		ComponentStatusChangeReason one = componentStatusChangeReasonRepository.getComponentStatusChangeReasonById(1l);
		Assert.assertNotNull("There is a ComponentStatusChangeReason", one);
		Assert.assertEquals("ComponentStatusChangeReason matches", ComponentStatusChangeReasonCategory.DISCARDED,
		    one.getCategory());
	}
	
	@Test
	public void testGetComponentStatusChangeReasonByIdUnknown() throws Exception {
		ComponentStatusChangeReason one = componentStatusChangeReasonRepository.getComponentStatusChangeReasonById(123l);
		Assert.assertNull("There is no ComponentStatusChangeReason", one);
	}
	
	@Test
	@Ignore("Bug - getAllComponentStatusChangeReasonsAsMap only returns one ComponentStatusChangeReason per ComponentStatusChangeReasonCategory")
	public void testGetAllComponentStatusChangeReasonsAsMap() throws Exception {
		Map<ComponentStatusChangeReasonCategory, ComponentStatusChangeReason> result = componentStatusChangeReasonRepository
		        .getAllComponentStatusChangeReasonsAsMap();
		Assert.assertNotNull("Does not return a null map", result);
		Assert.assertEquals("There are 3 ComponentStatusChangeReasonCategory with ComponentStatusChangeReason", 3, result.size());
	}
}
