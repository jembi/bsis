package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.matchers.UserViewModelMatcher.hasSameStateAsUserViewModel;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.model.user.User;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.UserViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;

public class UserFactoryTests extends UnitTestSuite {

  @InjectMocks
  private UserFactory userFactory;
  
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
        .build();

    UserViewModel userViewModel = new UserViewModel(user);
    
    // Exercise SUT
    UserViewModel returnedViewModel = userFactory.createViewModel(user);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsUserViewModel(userViewModel));
  }

  
  @Test
  public void testCreateViewModels_shouldReturnExpectedViewModels() {
    List<User> users = Arrays.asList(
        aUser().build(),
        aUser().build(),
        aUser().build());
    
    List<UserViewModel> returnedViewModels = userFactory.createViewModels(users);
    
    assertThat(returnedViewModels.size(), is(3));
  }
}