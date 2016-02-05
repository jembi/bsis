package repository;

import java.io.File;

import model.user.User;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the LoginRepository
 */
public class LoginRepositoryTest extends DBUnitContextDependentTestSuite {
	
	@Autowired
	LoginRepository loginRepository;

	@Override
	protected IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/LoginRepositoryDataset.xml");
		return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
	}

	@Test
	public void testGetUser() throws Exception {
		User superUser = loginRepository.getUser("superuser");
		Assert.assertNotNull("There is a super user", superUser);
		Assert.assertEquals("The users name is correct", "Super", superUser.getFirstName());
	}
	
	@Test
	public void testGetUserUnknown() throws Exception {
		User unknown = loginRepository.getUser("Dagmar");
		Assert.assertNull("Unknown user", unknown);
	}
}
