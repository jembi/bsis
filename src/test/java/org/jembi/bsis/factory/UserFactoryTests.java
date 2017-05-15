package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.RoleBackingFormBuilder.aRoleBackingForm;
import static org.jembi.bsis.helpers.builders.RoleBuilder.aRole;
import static org.jembi.bsis.helpers.builders.RoleViewModelBuilder.aRoleViewModel;
import static org.jembi.bsis.helpers.builders.UserBackingFormBuilder.aUserBackingForm;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.builders.UserViewModelBuilder.aUserViewModel;
import static org.jembi.bsis.helpers.matchers.UserViewModelMatcher.hasSameStateAsUserViewModel;
import static org.jembi.bsis.helpers.matchers.UserMatcher.hasSameStateAsUser;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.UserBackingForm;
import org.jembi.bsis.model.user.Role;
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
    UUID userId = UUID.randomUUID();
    UUID roleId1 = UUID.randomUUID();
    UUID roleId2 = UUID.randomUUID();
    
    User user = aUser()
        .withId(userId)
        .withUsername("test")
        .withEmailId("test@test.com")
        .withFirstName("Tester")
        .withLastName("Test")
        .withPasswordReset()
        .thatIsAdmin()
        .thatIsNotDeleted()
        .withRole(aRole().withId(roleId1).withName("Superuser").build())
        .withRole(aRole().withId(roleId1).withName("Admin").build())
        .build();

    List<RoleViewModel> roleViewModels = Arrays.asList(
        aRoleViewModel().withId(roleId1).withName("Superuser").build(),
        aRoleViewModel().withId(roleId2).withName("Admin").build()
        );
    UserViewModel expectedViewModel = aUserViewModel()
        .withId(userId)
        .withUsername("test")
        .withEmailId("test@test.com")
        .withFirstName("Tester")
        .withLastName("Test")
        .withPasswordReset()
        .withRoles(roleViewModels)
        .thatIsAdmin()
        .build();

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

  @Test
  public void testCreateEntity_shouldReturnExpectedEntity() {
    // Set up fixtures
    UUID userId = UUID.randomUUID();
    Date lastLogin = new Date();
    UserBackingForm form = aUserBackingForm()
        .withId(userId)
        .withUsername("username")
        .withPassword("password")
        .withFirstName("firstName")
        .withLastName("lastName")
        .withEmailId("emailId")
        .thatIsStaff()
        .thatIsActive()
        .thatIsAdmin()
        .thatIsNotDeleted()
        .withNotes("notes")
        .withLastLogin(lastLogin)
        .thatIsPasswordReset()
        .withRole(aRoleBackingForm().build())
        .build();

    User expectedUser = aUser()
        .withId(userId)
        .withUsername("username")
        .withPassword("password")
        .withFirstName("firstName")
        .withLastName("lastName")
        .withEmailId("emailId")
        .thatIsStaff()
        .thatIsAdmin()
        .thatIsNotDeleted()
        .withNotes("notes")
        .withLastLogin(lastLogin)
        .withPasswordReset()
        .withRole(new Role())
        .build();

    // Exercise SUT
    User returnedUser = userFactory.createEntity(form);

    // Verify
    assertThat(returnedUser, hasSameStateAsUser(expectedUser));
  }
}