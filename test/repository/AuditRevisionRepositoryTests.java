package repository;

import static helpers.builders.AuditRevisionBuilder.anAuditRevision;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.audit.AuditRevision;

import org.joda.time.DateTime;
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
    
    @Test
    public void testFindRecentAuditRevisions_shouldReturnAuditRevisionsOrderedByTimestamp() {
        
        AuditRevision chronologicallyFirstAuditRevision = anAuditRevision()
                .withRevisionDate(new DateTime().minusDays(7).toDate())
                .build();
        AuditRevision chronologicallySecondAuditRevision = anAuditRevision()
                .withRevisionDate(new DateTime().minusDays(2).toDate())
                .build();
        
        entityManager.persist(chronologicallyFirstAuditRevision);
        entityManager.persist(chronologicallySecondAuditRevision);
        
        List<AuditRevision> returnedAuditRevisions = auditRevisionRepository.findRecentAuditRevisions();
        
        assertThat(returnedAuditRevisions.size(), is(2));
        assertThat(returnedAuditRevisions.get(0), is(chronologicallySecondAuditRevision));
        assertThat(returnedAuditRevisions.get(1), is(chronologicallyFirstAuditRevision));
    }

}
