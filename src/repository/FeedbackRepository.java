package repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Feedback;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class FeedbackRepository {
    @PersistenceContext
    private EntityManager em;

    public void saveFeedback(Feedback feedback) {
        em.persist(feedback);
        em.flush();
    }
}
