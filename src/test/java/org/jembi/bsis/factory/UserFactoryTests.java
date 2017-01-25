package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.RoleBuilder.aRole;
import static org.jembi.bsis.helpers.builders.RoleViewModelBuilder.aRoleViewModel;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.matchers.UserViewModelMatcher.hasSameStateAsUserViewModel;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.model.user.User;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.RoleViewModel;
import org.jembi.bsis.viewmodel.UserViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UserFactoryTests extends UnitTestSuite {

  @InjectMocks
  private UserFactory userFactory;
  @Mock
  private RoleFactory roleFactory;
  
  @Test
  public void testCreateViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    User user = aUser()
        .withId(1L)
        .withEmailId("test@test.com")
        .withFirstName("Tester")
        .withLastName("Test")
        .withPasswordReset()
        .thatIsNotDeleted()
        .withRole(aRole().withId(1L).withName("Superuser").build())
        .withRole(aRole().withId(1L).withName("Admin").build())
        .build();

    List<RoleViewModel> roleViewModels = Arrays.asList(
        aRoleViewModel().withId(1L).withName("Superuser").build(),
        aRoleViewModel().withId(2L).withName("Admin").build()
        );
    UserViewModel expectedViewModel = new UserViewModel(user);
    expectedViewModel.setRoles(roleViewModels);

    // Set up mocks
    when(roleFactory.createViewModels(user.getRoles())).thenReturn(roleViewModels);
    
    // Exercise SUT
    UserViewModel returnedViewModel = userFactory.createViewModel(user);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsUserViewModel(expectedViewModel));
  }
  
  @Test
  public void testCreateViewModels_shouldReturnExpectedViewModels() {
    List<User> users = Arrays.asList(
        aUser().build(),
        aUser().build(),
        aUser().build());

    when(roleFactory.createViewModels(null)).thenReturn(new ArrayList<RoleViewModel>());

    List<UserViewModel> returnedViewModels = userFactory.createViewModels(users);
    
    assertThat(returnedViewModels.size(), is(3));
  }
}