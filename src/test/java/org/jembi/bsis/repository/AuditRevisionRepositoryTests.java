package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.AuditRevisionBuilder.anAuditRevision;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jembi.bsis.model.audit.AuditRevision;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuditRevisionRepositoryTests extends ContextDependentTestSuite  {

  private static final DateTime END_OF_RANGE = new DateTime().minusDays(1);
  private static final DateTime START_OF_RANGE = END_OF_RANGE.minusDays(7);

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private AuditRevisionRepository auditRevisionRepository;

  @Test
  public void testFindRecentAuditRevisions_shouldReturnAuditRevisionsOrderedByTimestamp() {

    AuditRevision chronologicallyFirstAuditRevision = anAuditRevision()
        .withRevisionDate(START_OF_RANGE.plusDays(1).toDate())
        .buildAndPersist(entityManager);
    AuditRevision chronologicallySecondAuditRevision = anAuditRevision()
        .withRevisionDate(START_OF_RANGE.plusDays(3).toDate())
        .buildAndPersist(entityManager);

    // Excluded by date before start of range
    anAuditRevision()
        .withRevisionDate(START_OF_RANGE.minusDays(1).toDate())
        .buildAndPersist(entityManager);

    // Excluded by date after end of range
    anAuditRevision()
        .withRevisionDate(END_OF_RANGE.plusDays(1).toDate())
        .buildAndPersist(entityManager);

    List<AuditRevision> returnedAuditRevisions = auditRevisionRepository.findAuditRevisions(
        START_OF_RANGE.toDate(), END_OF_RANGE.toDate());

    assertThat(returnedAuditRevisions.size(), is(2));
    assertThat(returnedAuditRevisions.get(0), is(chronologicallySecondAuditRevision));
    assertThat(returnedAuditRevisions.get(1), is(chronologicallyFirstAuditRevision));
  }

  @Test
  public void testFindAuditRevisionsByUser_shouldReturnAuditRevisionsMatchingSearch() {

    User userWithMatchingUsername = aUser()
        .withUsername("username.that.matches")
        .buildAndPersist(entityManager);
    User userWithMatchingFirstName = aUser()
        .withUsername("irrelevant.username.1")
        .withFirstName("matching")
        .buildAndPersist(entityManager);
    User userWithMatchingLastName = aUser()
        .withUsername("irrelevant.username.2")
        .withLastName("doesMatch")
        .buildAndPersist(entityManager);
    User userWithNoMatches = aUser().buildAndPersist(entityManager);

    Date dateInRange = START_OF_RANGE.plusDays(1).toDate();

    List<AuditRevision> expectedAuditRevisions = Arrays.asList(
        anAuditRevision()
            .withUsername(userWithMatchingUsername.getUsername())
            .withRevisionDate(dateInRange)
            .buildAndPersist(entityManager),
        anAuditRevision()
            .withUsername(userWithMatchingFirstName.getUsername())
            .withRevisionDate(dateInRange)
            .buildAndPersist(entityManager),
        anAuditRevision()
            .withUsername(userWithMatchingLastName.getUsername())
            .withRevisionDate(dateInRange)
            .buildAndPersist(entityManager)
    );

    // Excluded by user
    anAuditRevision()
        .withUsername(userWithNoMatches.getUsername())
        .withRevisionDate(dateInRange)
        .buildAndPersist(entityManager);

    // Excluded by date before start of range
    anAuditRevision()
        .withUsername(userWithNoMatches.getUsername())
        .withRevisionDate(START_OF_RANGE.minusDays(1).toDate())
        .buildAndPersist(entityManager);

    // Excluded by date after end of range
    anAuditRevision()
        .withUsername(userWithNoMatches.getUsername())
        .withRevisionDate(END_OF_RANGE.plusDays(1).toDate())
        .buildAndPersist(entityManager);

    List<AuditRevision> returnedAuditRevisions = auditRevisionRepository.findAuditRevisionsByUser(
        "matcH", START_OF_RANGE.toDate(), END_OF_RANGE.toDate());

    assertThat(returnedAuditRevisions, is(expectedAuditRevisions));
  }

  @Test
  public void testFindAuditRevisionsByUserWithFullName_shouldReturnMatchingAuditRevision() {

    User userWithMatchingName = aUser()
        .withUsername("irrelevant.username.1")
        .withFirstName("Test")
        .withLastName("User")
        .buildAndPersist(entityManager);

    User userWithNonMatchingName = aUser()
        .withUsername("irrelevant.username.2")
        .withFirstName("Test")
        .withLastName("Abuser")
        .buildAndPersist(entityManager);

    Date dateInRange = START_OF_RANGE.plusDays(1).toDate();

    List<AuditRevision> expectedAuditRevisions = Arrays.asList(
        anAuditRevision()
            .withUsername(userWithMatchingName.getUsername())
            .withRevisionDate(dateInRange)
            .buildAndPersist(entityManager)
    );

    // Excluded by user
    anAuditRevision()
        .withUsername(userWithNonMatchingName.getUsername())
        .withRevisionDate(dateInRange)
        .buildAndPersist(entityManager);

    List<AuditRevision> returnedAuditRevisions = auditRevisionRepository.findAuditRevisionsByUser(
        "Test User", START_OF_RANGE.toDate(), END_OF_RANGE.toDate());

    assertThat(returnedAuditRevisions, is(expectedAuditRevisions));
  }

}
