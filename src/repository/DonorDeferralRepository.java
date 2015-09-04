package repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.donordeferral.DonorDeferral;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class DonorDeferralRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional(propagation = Propagation.MANDATORY)
    public void save(DonorDeferral donorDeferral) {
        entityManager.persist(donorDeferral);
    }

}
