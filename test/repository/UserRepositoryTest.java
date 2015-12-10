package repository;

import model.user.Role;
import model.user.User;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import viewmodel.UserViewModel;

import javax.sql.DataSource;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**
 * Test using DBUnit to test the UserRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  private DataSource dataSource;

  private IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/UserRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  private IDatabaseConnection getConnection() throws SQLException {
    IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
    DatabaseConfig config = connection.getConfig();
    config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
    return connection;
  }

  @Before
  public void init() throws Exception {
    IDatabaseConnection connection = getConnection();
    try {
      IDataSet dataSet = getDataSet();
      DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
    } finally {
      connection.close();
    }
  }

  @AfterTransaction
  public void after() throws Exception {
    IDatabaseConnection connection = getConnection();
    try {
      IDataSet dataSet = getDataSet();
      DatabaseOperation.DELETE_ALL.execute(connection, dataSet);
    } finally {
      connection.close();
    }
  }

  @Test
  public void testGetAllUsers() throws Exception {
    List<UserViewModel> all = userRepository.getAllUsers();
    Assert.assertNotNull("There are Users defined", all);
    Assert.assertEquals("There are 16 Users", 16, all.size());
  }

  @Test
  public void testGetAllRoles() throws Exception {
    List<Role> all = userRepository.getUserRole(new String[]{"1", "2", "3"});
    Assert.assertNotNull("There are Roles defined", all);
    Assert.assertEquals("There are 3 Roles", 3, all.size());
  }

  @Test
  public void testGetAllRolesNull() throws Exception {
    List<Role> all = userRepository.getUserRole(null);
    Assert.assertNotNull("There are Roles defined", all);
    Assert.assertTrue("There are no Roles", all.isEmpty());
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testGetAllRolesUnknown() throws Exception {
    userRepository.getUserRole(new String[]{"123"});
  }

  @Test
  public void testFindUserById() throws Exception {
    User user = userRepository.findUserById(1);
    Assert.assertNotNull("User is defined", user);
    Assert.assertEquals("User is correct", "superuser", user.getUsername());
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testFindUserByIdUnknown() throws Exception {
    userRepository.findUserById(123);
  }

  @Test
  public void testAddUser() throws Exception {
    User user = new User();
    user.setUsername("tester");
    user.setFirstName("Test");
    user.setLastName("Er");
    user.setPassword("tester");
    user.setIsActive(true);
    User savedUser = userRepository.addUser(user);
    Assert.assertNotNull("User id is set after save", savedUser.getId());
  }

  @Test
  public void testDeleteUserById() throws Exception {
    userRepository.deleteUserById(1); // this is a hard delete
    List<UserViewModel> all = userRepository.getAllUsers();
    Assert.assertNotNull("There are Users defined", all);
    Assert.assertEquals("There are now 15 Users", 15, all.size());
  }

  @Test
  public void testDeleteUser() throws Exception {
    userRepository.deleteUser("superuser");
    // can't assert this is true because all queries ignore deleted users
  }

  @Test
  public void testUpdateBasicUserInfo() throws Exception {
    User user = userRepository.findUserById(1);
    user.setFirstName("Test");
    user.setLastName("Tester");
    user.setEmailId("test@jembi.org");
    userRepository.updateBasicUserInfo(user, false);
    User savedUser = userRepository.findUserById(1);
    Assert.assertEquals("FirstName changed", "Test", savedUser.getFirstName());
    Assert.assertEquals("LastName changed", "Tester", savedUser.getLastName());
    Assert.assertEquals("Email changed", "test@jembi.org", savedUser.getEmailId());
    Assert.assertEquals("Password was not changed", user.getPassword(), savedUser.getPassword());
  }

  @Test
  public void testUpdateBasicUserInfoPassword() throws Exception {
    User user = userRepository.findUserById(1);
    user.setPassword("newpassword");
    userRepository.updateBasicUserInfo(user, true);
    User savedUser = userRepository.findUserById(1);
    Assert.assertFalse("Password was changed", !user.getPassword().equals(savedUser.getPassword()));
  }

  @Test
  public void testUpdateLastLogin() throws Exception {
    User user = userRepository.findUserById(1);
    userRepository.updateLastLogin(user);
    User savedUser = userRepository.findUserById(1);
    Assert.assertFalse("Last login was changed", !user.getLastLogin().equals(savedUser.getLastLogin()));
  }
}
