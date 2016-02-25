package service;

import static helpers.builders.UserBuilder.aUser;
import static helpers.matchers.UserMatcher.hasSameStateAsUser;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import model.user.User;
import repository.UserRepository;
import suites.UnitTestSuite;

public class UserCRUDServiceTests extends UnitTestSuite {
  
  private static final Long IRRELEVANT_USER_ID = 116L;
  
  @InjectMocks
  private UserCRUDService userCRUDService;
  
  @Mock
  private UserRepository userRepository;
  
  @Test
  public void testDeleteUser_shouldSetUserToDeleted() {
    
    // Set up fixture
    User existingUser = aUser()
        .thatIsNotDeleted()
        .withId(IRRELEVANT_USER_ID)
        .build();
    
    User expectedUser = aUser()
        .thatIsDeleted()
        .withId(IRRELEVANT_USER_ID)
        .build();

    // Set up expectations
    when(userRepository.findUserById(IRRELEVANT_USER_ID)).thenReturn(existingUser);
    
    // Exercise SUT
    userCRUDService.deleteUser(IRRELEVANT_USER_ID);
    
    // Verify
    verify(userRepository).save(argThat(hasSameStateAsUser(expectedUser)));
  }

}
