package factory;

import static helpers.builders.AuditRevisionBuilder.anAuditRevision;
import static helpers.builders.AuditRevisionViewModelBuilder.anAuditRevisionViewModel;
import static helpers.builders.UserBuilder.aUser;
import static helpers.matchers.AuditRevisionViewModelMatcher.hasSameStateAsAuditRevisionViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import model.audit.AuditRevision;
import model.user.User;

import org.joda.time.DateTime;
import org.junit.Test;

import repository.UserRepository;
import viewmodel.AuditRevisionViewModel;

public class AuditRevisionViewModelFactoryTests {
    
    private AuditRevisionViewModelFactory auditRevisionViewModelFactory;
    private UserRepository userRepository;
    
    @Test
    public void testCreateAuditRevisionViewModelsWithAuditRevisionWithUsername_shouldCreateAuditRevisionViewModelWithUser() {
        // Set up fixture
        setUpFixture();
        
        String irrelevantUsername = "irrelevant.username";
        int irrelevantUserId = 56;
        int irrelevantAuditRevisionId = 78;
        Date irrelevantRevisionDate = new DateTime().minusDays(7).toDate();

        User auditRevisionUser = aUser()
                .withId(irrelevantUserId)
                .withUsername(irrelevantUsername)
                .build();

        AuditRevisionViewModel expectedViewModel = anAuditRevisionViewModel()
                .withId(irrelevantAuditRevisionId)
                .withRevisionDate(irrelevantRevisionDate)
                .withUser(auditRevisionUser)
                .build();

        AuditRevision auditRevision = anAuditRevision()
                .withId(irrelevantAuditRevisionId)
                .withRevisionDate(irrelevantRevisionDate)
                .withUsername(irrelevantUsername)
                .build();
        
        when(userRepository.findUser(irrelevantUsername)).thenReturn(auditRevisionUser);
        
        // Exercise SUT
        List<AuditRevisionViewModel> returnedViewModels = auditRevisionViewModelFactory
                .createAuditRevisionViewModels(Arrays.asList(auditRevision));
        
        // Verify
        verify(userRepository).findUser(irrelevantUsername);
        verifyNoMoreInteractions(userRepository);
        
        assertThat(returnedViewModels.size(), is(1));
        assertThat(returnedViewModels.get(0), hasSameStateAsAuditRevisionViewModel(expectedViewModel));
    }
    
    @Test
    public void testCreateAuditRevisionViewModelsWithAuditRevisionWithNoUsername_shouldCreateAuditRevisionViewModelWithNoUser() {
        // Set up fixture
        setUpFixture();
        
        int irrelevantAuditRevisionId = 3;
        Date irrelevantRevisionDate = new DateTime().minusDays(4).toDate();

        AuditRevisionViewModel expectedViewModel = anAuditRevisionViewModel()
                .withId(irrelevantAuditRevisionId)
                .withRevisionDate(irrelevantRevisionDate)
                .build();

        AuditRevision auditRevision = anAuditRevision()
                .withId(irrelevantAuditRevisionId)
                .withRevisionDate(irrelevantRevisionDate)
                .build();

        // Exercise SUT
        List<AuditRevisionViewModel> returnedViewModels = auditRevisionViewModelFactory
                .createAuditRevisionViewModels(Arrays.asList(auditRevision));
        
        // Verify
        verifyZeroInteractions(userRepository);
        
        assertThat(returnedViewModels.size(), is(1));
        assertThat(returnedViewModels.get(0), hasSameStateAsAuditRevisionViewModel(expectedViewModel));
    }
    
    private void setUpFixture() {
        userRepository = mock(UserRepository.class);

        auditRevisionViewModelFactory = new AuditRevisionViewModelFactory();
        auditRevisionViewModelFactory.setUserRepository(userRepository);
    }

}
