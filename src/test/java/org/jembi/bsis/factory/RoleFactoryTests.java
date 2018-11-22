package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.jembi.bsis.helpers.builders.PermissionBackingFormBuilder.aPermissionBackingForm;
import static org.jembi.bsis.helpers.builders.PermissionBuilder.aPermission;
import static org.jembi.bsis.helpers.builders.PermissionViewModelBuilder.aPermissionViewModel;
import static org.jembi.bsis.helpers.builders.RoleBackingFormBuilder.aRoleBackingForm;
import static org.jembi.bsis.helpers.builders.RoleBuilder.aRole;
import static org.jembi.bsis.helpers.builders.RoleViewModelBuilder.aRoleViewModel;
import static org.jembi.bsis.helpers.matchers.RoleMatcher.hasSameStateAsRole;
import static org.jembi.bsis.helpers.matchers.RoleViewModelMatcher.hasSameStateAsRoleViewModel;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.backingform.RoleBackingForm;
import org.jembi.bsis.backingform.PermissionBackingForm;
import org.jembi.bsis.model.user.Permission;
import org.jembi.bsis.model.user.Role;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.PermissionViewModel;
import org.jembi.bsis.viewmodel.RoleViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class RoleFactoryTests extends UnitTestSuite {

  @InjectMocks
  private RoleFactory roleFactory;
  @Mock
  private PermissionFactory permissionFactory;
  
  @Test
  public void testCreateViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    Permission perm1 = aPermission().withId(1L).withName("do this").build();
    Permission perm2 = aPermission().withId(2L).withName("do that").build();
    UUID roleId = UUID.randomUUID();
    Role role = aRole()
        .withId(roleId)
        .withName("Superuser")
        .withDescription("Can do everything")
        .withPermission(perm1)
        .withPermission(perm2)
        .build();

    Set<PermissionViewModel> permissionViewModels = new HashSet<>();
    permissionViewModels.add(aPermissionViewModel().withId(1L).withName("do this").build());
    permissionViewModels.add(aPermissionViewModel().withId(2L).withName("do that").build());
    RoleViewModel roleViewModel = aRoleViewModel()
        .withId(roleId)
        .withName("Superuser")
        .withDescription("Can do everything")
        .withPermissions(permissionViewModels)
        .build();

    when(permissionFactory.createViewModels(role.getPermissions())).thenReturn(permissionViewModels);
    
    // Exercise SUT
    RoleViewModel returnedViewModel = roleFactory.createViewModel(role);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsRoleViewModel(roleViewModel));
  }

  @Test
  public void testCreateViewModelsWithNull_shouldReturnEmptyList() {
    List<RoleViewModel> returnedViewModels = roleFactory.createViewModels(null);
    assertThat(returnedViewModels, is(notNullValue()));
    assertThat(returnedViewModels.size(), is(0));
  }

  @Test
  public void testCreateViewModels_shouldReturnExpectedViewModels() {
    List<Role> roles = Arrays.asList(
        aRole().build(),
        aRole().build(),
        aRole().build());
    
    List<RoleViewModel> returnedViewModels = roleFactory.createViewModels(roles);
    
    assertThat(returnedViewModels.size(), is(3));
  }

  @Test
  public void testCreateEntity_shouldReturnExpectedEntity() {
    // Set up fixture
    Set<PermissionBackingForm> permissions = new HashSet<>(Arrays.asList(
        aPermissionBackingForm().withId(1L).build()
        ));

    Set<Permission> expectedPermissions = new HashSet<>(Arrays.asList(
        aPermission().withId(1L).build()
        ));

    UUID roleId = UUID.randomUUID();
    RoleBackingForm form = aRoleBackingForm()
        .withId(roleId)
        .withName("role")
        .withDescription("role description")
        .withPermissions(permissions)
        .build();

    Role expectedRole = aRole()
        .withId(roleId)
        .withName("role")
        .withDescription("role description")
        .withPermissions(expectedPermissions)
        .build();

    when(permissionFactory.createEntities(permissions)).thenReturn(expectedPermissions);

    // Exercise SUT
    Role returnedRole = roleFactory.createEntity(form);

    // Verify
    assertThat(returnedRole, hasSameStateAsRole(expectedRole));
  }

  @Test
  public void testCreateEntities_shouldReturnExpectedEntities() {
    // Set up fixture
    UUID roleId1 = UUID.randomUUID();
    UUID roleId2 = UUID.randomUUID();
    List<RoleBackingForm> forms = Arrays.asList(
        aRoleBackingForm()
          .withId(roleId1)
          .build(),
        aRoleBackingForm()
          .withId(roleId2)
          .build()
        );

    // Exercise SUT
    List<Role> returnedRoles = roleFactory.createEntities(forms);

    // Verify
    assertThat(returnedRoles.size(), is(2));
  }
}