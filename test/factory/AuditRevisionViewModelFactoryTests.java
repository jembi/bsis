package factory;

import static helpers.builders.AuditRevisionBuilder.anAuditRevision;
import static helpers.builders.AuditRevisionViewModelBuilder.anAuditRevisionViewModel;
import static helpers.builders.EntityModificationBuilder.anEntityModification;
import static helpers.builders.UserBuilder.aUser;
import static helpers.matchers.AuditRevisionViewModelMatcher.hasSameStateAsAuditRevisionViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.*;

import model.audit.AuditRevision;
import model.audit.EntityModification;
import model.donation.Donation;
import model.donor.Donor;
import model.user.User;

import org.hibernate.envers.RevisionType;
import org.joda.time.DateTime;
import org.junit.Test;

import repository.UserRepository;
import viewmodel.AuditRevisionViewModel;

public class AuditRevisionViewModelFactoryTests {

  private AuditRevisionViewModelFactory auditRevisionViewModelFactory;
  private UserRepository userRepository;

  @Test
  public void testCreateAuditRevisionViewModels_shouldCreateAuditRevisionViewModelWithTheCorrectState() {
    // Set up fixture
    setUpFixture();

    String irrelevantUsername = "irrelevant.username";
    int irrelevantAuditRevisionId = 78;
    Date irrelevantRevisionDate = new DateTime().minusDays(7).toDate();

    List<EntityModification> entityModifications = Arrays.asList(
        anEntityModification()
            .withId(88L)
            .withRevisionType(RevisionType.MOD)
            .withEntityName(Donor.class.getSimpleName())
            .build(),
        anEntityModification()
            .withId(107L)
            .withRevisionType(RevisionType.ADD)
            .withEntityName(Donation.class.getSimpleName())
            .build()
    );

    AuditRevision auditRevision = anAuditRevision()
        .withId(irrelevantAuditRevisionId)
        .withRevisionDate(irrelevantRevisionDate)
        .withUsername(irrelevantUsername)
        .withEntityModifications(new HashSet<>(entityModifications))
        .build();

    User auditRevisionUser = aUser()
        .withId(56L)
        .withUsername(irrelevantUsername)
        .build();

    AuditRevisionViewModel expectedViewModel = anAuditRevisionViewModel()
        .withId(irrelevantAuditRevisionId)
        .withRevisionDate(irrelevantRevisionDate)
        .withUser(auditRevisionUser)
        .withEntityModifications(new HashSet<>(entityModifications))
        .build();

    when(userRepository.findUser(irrelevantUsername)).thenReturn(auditRevisionUser);

    // Exercise SUT
    List<AuditRevisionViewModel> returnedViewModels = auditRevisionViewModelFactory
        .createAuditRevisionViewModels(Collections.singletonList(auditRevision));

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
        .createAuditRevisionViewModels(Collections.singletonList(auditRevision));

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
