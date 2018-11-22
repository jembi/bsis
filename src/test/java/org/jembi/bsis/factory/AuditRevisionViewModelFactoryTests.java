package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.AuditRevisionBuilder.anAuditRevision;
import static org.jembi.bsis.helpers.builders.AuditRevisionViewModelBuilder.anAuditRevisionViewModel;
import static org.jembi.bsis.helpers.builders.EntityModificationBuilder.anEntityModification;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.builders.UserViewModelBuilder.aUserViewModel;
import static org.jembi.bsis.helpers.matchers.AuditRevisionViewModelMatcher.hasSameStateAsAuditRevisionViewModel;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.hibernate.envers.RevisionType;
import org.jembi.bsis.model.audit.AuditRevision;
import org.jembi.bsis.model.audit.EntityModification;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.UserRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.AuditRevisionViewModel;
import org.jembi.bsis.viewmodel.UserViewModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class AuditRevisionViewModelFactoryTests extends UnitTestSuite {

  @InjectMocks
  private AuditRevisionViewModelFactory auditRevisionViewModelFactory;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserFactory userFactory;

  @Test
  public void testCreateAuditRevisionViewModels_shouldCreateAuditRevisionViewModelWithTheCorrectState() {
    String irrelevantUsername = "irrelevant.username";
    long irrelevantAuditRevisionId = 78;
    Date irrelevantRevisionDate = new DateTime().minusDays(7).toDate();
    UUID entityModificationId1 = UUID.randomUUID();
    UUID entityModificationId2 = UUID.randomUUID();

    List<EntityModification> entityModifications = Arrays.asList(
        anEntityModification()
            .withId(entityModificationId1)
            .withRevisionType(RevisionType.MOD)
            .withEntityName(Donor.class.getSimpleName())
            .build(),
        anEntityModification()
            .withId(entityModificationId2)
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

    UUID autidRevisionUserId = UUID.randomUUID();

    User auditRevisionUser = aUser()
        .withId(autidRevisionUserId)
        .withUsername(irrelevantUsername)
        .build();

    UserViewModel auditRevisionUserViewModel = aUserViewModel()
        .withId(autidRevisionUserId)
        .withUsername(irrelevantUsername)
        .build();

    AuditRevisionViewModel expectedViewModel = anAuditRevisionViewModel()
        .withId(irrelevantAuditRevisionId)
        .withRevisionDate(irrelevantRevisionDate)
        .withUser(auditRevisionUserViewModel)
        .withEntityModifications(new HashSet<>(entityModifications))
        .build();

    when(userRepository.findUser(irrelevantUsername)).thenReturn(auditRevisionUser);
    when(userFactory.createViewModel(auditRevisionUser)).thenReturn(auditRevisionUserViewModel);

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
}