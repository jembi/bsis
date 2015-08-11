package repository;

import static helpers.builders.AuditRevisionBuilder.anAuditRevision;
import static helpers.builders.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.audit.AuditRevision;
import model.user.User;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
@Transactional
public class AuditRevisionRepositoryTests {
    
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AuditRevisionRepository auditRevisionRepository;
    
    @Ignore("Pending changes to old tests in 318")
    @Test
    public void testFindRecentAuditRevisions_shouldReturnAuditRevisionsOrderedByTimestamp() {
        
        AuditRevision chronologicallyFirstAuditRevision = anAuditRevision()
                .withRevisionDate(new DateTime().minusDays(7).toDate())
                .buildAndPersist(entityManager);
        AuditRevision chronologicallySecondAuditRevision = anAuditRevision()
                .withRevisionDate(new DateTime().minusDays(2).toDate())
                .buildAndPersist(entityManager);
        
        List<AuditRevision> returnedAuditRevisions = auditRevisionRepository.findRecentAuditRevisions();
        
        assertThat(returnedAuditRevisions.size(), is(2));
        assertThat(returnedAuditRevisions.get(0), is(chronologicallySecondAuditRevision));
        assertThat(returnedAuditRevisions.get(1), is(chronologicallyFirstAuditRevision));
    }
    
    @Ignore("Pending changes to old tests in 318")
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
        
        
        List<AuditRevision> expectedAuditRevisions = Arrays.asList(
                anAuditRevision()
                    .withUsername(userWithMatchingUsername.getUsername())
                    .buildAndPersist(entityManager),
                anAuditRevision()
                    .withUsername(userWithMatchingFirstName.getUsername())
                    .buildAndPersist(entityManager),
                anAuditRevision()
                    .withUsername(userWithMatchingLastName.getUsername())
                    .buildAndPersist(entityManager)
        );
        
        // Should not be returned
        anAuditRevision()
            .withUsername(userWithNoMatches.getUsername())
            .buildAndPersist(entityManager);
        
        List<AuditRevision> returnedAuditRevisions = auditRevisionRepository.findAuditRevisionsByUser("matcH");
        
        assertThat(returnedAuditRevisions, is(expectedAuditRevisions));
    }

}
