/**
 *
 */
package org.jembi.bsis.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.model.user.Permission;
import org.jembi.bsis.model.user.Role;
import org.jembi.bsis.repository.RoleRepository;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

public class RoleControllerTest extends ContextDependentTestSuite  {

  @SuppressWarnings("unused")
  private MockHttpServletRequest request;
  @SuppressWarnings("unused")
  private MockHttpServletResponse response;
  @SuppressWarnings("unused")
  private MockMvc mockMvc;

  // Use @Mock annotation to specify Mock objects

  //Controller that is being tested.
  @InjectMocks
  private RoleController roleController;

  @Autowired
  RoleRepository roleRepository;

  @Ignore
  @Test
  public void addRoleTest() {
    String permissiondata = "1~2~";
    String[] splitpermission = permissiondata.split("~");
    Set<Permission> permissions = setPermissionValues(splitpermission);
    Role role = new Role();
    role.setPermissions(permissions);
    role.setName("New Role");
    role.setDescription("Add New Role For Test");
    roleRepository.addRole(role);
  }

  @Ignore
  @Test
  public void updateRoleTest() {
    String permissiondata = "1~2~3~4~";
    String[] splitpermission = permissiondata.split("~");
    Set<Permission> permissions = setPermissionValues(splitpermission);
    Role role = new Role();
    role.setPermissions(permissions);
    role.setName("Update role");
    role.setDescription("Update Role For Test");
    role.setId(UUID.randomUUID());
    roleRepository.updateRole(role);
  }

  private Set<Permission> setPermissionValues(String[] splitPermissionArr) {
    Set<Permission> permissions = new HashSet<Permission>();
    for (String permissionId : splitPermissionArr) {
      Permission permission = roleRepository
          .findPermissionByPermissionId(Long.parseLong(permissionId));
      permissions.add(permission);
    }
    return permissions;
  }
}
