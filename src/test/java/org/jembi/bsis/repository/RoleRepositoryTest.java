package org.jembi.bsis.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.user.Permission;
import org.jembi.bsis.model.user.Role;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the RoleRepository
 */
public class RoleRepositoryTest extends DBUnitContextDependentTestSuite {


  @Autowired
  RoleRepository roleRepository;

  @Autowired
  UserRepository userRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/RoleRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Test
  public void testGetAllPermissions() throws Exception {
    List<Permission> all = roleRepository.getAllPermissions();
    Assert.assertNotNull("There are Permissions", all);
    Assert.assertEquals("There are 5 Permissions", 5, all.size());
  }

  @Test
  public void testGetRoles() throws Exception {
    List<Role> all = roleRepository.getAllRoles();
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
    Role superUser = roleRepository.findRoleDetailById(UUID.fromString("49077a94-a105-4df6-9aea-16cce81ea701"));
    Assert.assertNotNull("Role exists", superUser);
    Assert.assertEquals("Role is correct", "Super User", superUser.getDescription());
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testFindRoleDetailByIdUnknown() throws Exception {
    UUID irrelevantId = UUID.randomUUID();
    roleRepository.findRoleDetailById(irrelevantId);
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
    Set<Permission> permissions = new HashSet<Permission>();
    permissions.add(roleRepository.findPermissionByPermissionId(1));
    permissions.add(roleRepository.findPermissionByPermissionId(3));
    permissions.add(roleRepository.findPermissionByPermissionId(5));
    one.setPermissions(permissions);
    List<User> users = new ArrayList<User>();
    users.add(userRepository.findUserById(UUID.fromString("9bb07a38-eb7f-4e35-b5b1-34e77ad79a81")));
    users.add(userRepository.findUserById(UUID.fromString("9bb07a38-eb7f-4e35-b5b1-34e77ad79a82")));
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
    UUID roleId = UUID.fromString("49077a94-a105-4df6-9aea-16cce81ea701");
    Role one = roleRepository.findRoleDetailById(roleId);
    one.setName("Testing");
    one.setDescription("123");
    one.getPermissions().add(roleRepository.findPermissionByPermissionId(5));
    roleRepository.updateRole(one);
    Role savedOne = roleRepository.findRoleDetailById(roleId);
    Assert.assertEquals("Role updated correctly", "Testing", savedOne.getName());
    Assert.assertEquals("Role updated correctly", "123", savedOne.getDescription());
    Assert.assertEquals("Role updated correctly", 5, savedOne.getPermissions().size());
  }

  @Test
  @Ignore("Bug? - after the delete, all queries fail due to foreign key reference:  integrity constraint violation: foreign key no action; FK_TC5K40I3KIT8944SYRD366VY1 table: USER_ROLE")
  public void testDelete() throws Exception {
    UUID roleId = UUID.fromString("49077a94-a105-4df6-9aea-16cce81ea701");
    Role one = roleRepository.findRoleDetailById(roleId);
    one.setPermissions(new HashSet<Permission>());
    one.setUsers(new ArrayList<User>());
    roleRepository.updateRole(one); // clear out foreign key references
    roleRepository.deleteRole(roleId);
    List<Role> all = roleRepository.getAllRoles();
    Assert.assertNotNull("Role was permanently deleted", all);
    Assert.assertEquals("Role was permanently deleted", 2, all);
  }
}
