package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.PermissionBuilder.aPermission;
import static org.jembi.bsis.helpers.builders.RoleBuilder.aRole;
import static org.jembi.bsis.helpers.matchers.RoleViewModelMatcher.hasSameStateAsRoleViewModel;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.model.user.Role;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.RoleViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;

public class RoleFactoryTests extends UnitTestSuite {

  @InjectMocks
  private RoleFactory roleFactory;
  
  @Test
  public void testCreateViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    Role role = aRole()
        .withId(1L)
        .withName("Superuser")
        .withDescription("Can do everything")
        .withPermission(aPermission().withName("do this").build())
        .withPermission(aPermission().withName("do that").build())
        .build();

    RoleViewModel roleViewModel = new RoleViewModel(role);
    
    // Exercise SUT
    RoleViewModel returnedViewModel = roleFactory.createViewModel(role);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsRoleViewModel(roleViewModel));
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
}