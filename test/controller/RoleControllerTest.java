/**
 *
 */
package controller;

import model.user.Permission;
import model.user.Role;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/bsis-servlet.xml")
@WebAppConfiguration

public class RoleControllerTest {

  @Autowired
  RoleRepository roleRepository;
  @SuppressWarnings("unused")
  private MockHttpServletRequest request;
  @SuppressWarnings("unused")
  private MockHttpServletResponse response;

  // Use @Mock annotation to specify Mock objects
  @SuppressWarnings("unused")
  private MockMvc mockMvc;
  //Controller that is being tested.
  @InjectMocks
  private RoleController roleController;

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
    role.setId(1L);
    roleRepository.updateRole(role);
  }

  private Set<Permission> setPermissionValues(String[] splitPermissionArr) {
    Set<Permission> permissions = new HashSet<>();
    for (String permissionId : splitPermissionArr) {
      Permission permission = roleRepository
          .findPermissionByPermissionId(Long.parseLong(permissionId));
      permissions.add(permission);
    }
    return permissions;
  }
}
