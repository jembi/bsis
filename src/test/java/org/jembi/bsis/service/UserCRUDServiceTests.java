package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.matchers.UserMatcher.hasSameStateAsUser;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.UserRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UserCRUDServiceTests extends UnitTestSuite {
  
  private static final UUID IRRELEVANT_USER_ID = UUID.randomUUID();
  
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
