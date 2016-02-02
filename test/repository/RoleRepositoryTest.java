package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import model.user.Permission;
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import viewmodel.RoleViewModel;

/**
 * Test using DBUnit to test the RoleRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class RoleRepositoryTest {

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  private DataSource dataSource;

  private IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/RoleRepositoryDataset.xml");
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
  public void testGetAllPermissions() throws Exception {
    List<Permission> all = roleRepository.getAllPermissions();
    Assert.assertNotNull("There are Permissions", all);
    Assert.assertEquals("There are 5 Permissions", 5, all.size());
  }

  @Test
  public void testGetRoles() throws Exception {
    List<RoleViewModel> all = roleRepository.getAllRoles();
    Assert.assertNotNull("There are Roles", all);
    Assert.assertEquals("There are 3 Roles", 3, all.size());
  }

  @Test
  public void testFindRoleByName() throws Exception {
    Role superUser = roleRepository.findRoleByName("Super User");
    Assert.assertNotNull("Role exists", superUser);
    Assert.assertEquals("Role is correct", "Super User", superUser.getDescription());
  }

  @Test
  public void testFindRoleDetailById() throws Exception {
    Role superUser = roleRepository.findRoleDetailById(1L);
    Assert.assertNotNull("Role exists", superUser);
    Assert.assertEquals("Role is correct", "Super User", superUser.getDescription());
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testFindRoleDetailByIdUnknown() throws Exception {
    roleRepository.findRoleDetailById(123L);
  }

  @Test
  public void testGetAllPermissionsByName() throws Exception {
    List<Permission> all = roleRepository.getAllPermissionsByName();
    Assert.assertNotNull("There are Permissions", all);
    Assert.assertEquals("There are 5 Permissions", 5, all.size());
    Assert.assertEquals("Permissions are organised alphabetically", "Add Stuff", all.get(0).getName());
  }

  @Test
  public void testFindPermissionByPermissionId() throws Exception {
    Permission permission = roleRepository.findPermissionByPermissionId(1);
    Assert.assertNotNull("Permission exists", permission);
    Assert.assertEquals("Permission is correct", "View Stuff", permission.getName());
  }

  @Test
  public void testFindPermissionByPermissionIdUnknown() throws Exception {
    Permission perm = roleRepository.findPermissionByPermissionId(12345);
    Assert.assertNull("Permission not found", perm);
  }

  @Test
  public void testSave() throws Exception {
    Role one = new Role();
    one.setName("New Role");
    one.setDescription("A new role");
    Set<Permission> permissions = new HashSet<>();
    permissions.add(roleRepository.findPermissionByPermissionId(1));
    permissions.add(roleRepository.findPermissionByPermissionId(3));
    permissions.add(roleRepository.findPermissionByPermissionId(5));
    one.setPermissions(permissions);
    List<User> users = new ArrayList<>();
    users.add(userRepository.findUserById(1L));
    users.add(userRepository.findUserById(2L));
    one.setUsers(users);
    Role savedRole = roleRepository.addRole(one);
    Role retrievedRole = roleRepository.findRoleDetailById(savedRole.getId());
    Assert.assertNotNull("Role saved correctly", retrievedRole);
    Assert.assertEquals("Role saved correctly", "New Role", retrievedRole.getName());
    Assert.assertNotNull("Role saved correctly", retrievedRole.getPermissions());
    Assert.assertEquals("Role saved correctly", 3, retrievedRole.getPermissions().size());
  }

  @Test
  public void testUpdate() throws Exception {
    Role one = roleRepository.findRoleDetailById(1L);
    one.setName("Testing");
    one.setDescription("123");
    one.getPermissions().add(roleRepository.findPermissionByPermissionId(5));
    roleRepository.updateRole(one);
    Role savedOne = roleRepository.findRoleDetailById(1L);
    Assert.assertEquals("Role updated correctly", "Testing", savedOne.getName());
    Assert.assertEquals("Role updated correctly", "123", savedOne.getDescription());
    Assert.assertEquals("Role updated correctly", 5, savedOne.getPermissions().size());
  }

  @Test
  @Ignore("Bug? - after the delete, all queries fail due to foreign key reference:  integrity constraint violation: foreign key no action; FK_TC5K40I3KIT8944SYRD366VY1 table: USER_ROLE")
  public void testDelete() throws Exception {
    Role one = roleRepository.findRoleDetailById(1L);
    one.setPermissions(new HashSet<>());
    one.setUsers(new ArrayList<>());
    roleRepository.updateRole(one); // clear out foreign key references
    roleRepository.deleteRole(1L);
    List<RoleViewModel> all = roleRepository.getAllRoles();
    Assert.assertNotNull("Role was permanently deleted", all);
    Assert.assertEquals("Role was permanently deleted", 2, all);
  }
}
