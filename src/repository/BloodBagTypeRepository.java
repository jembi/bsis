package repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.collectedsample.BloodBagType;
import model.donor.DonorType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BloodBagTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public boolean isBloodBagTypeValid(String checkBloodBagType) {
    String queryString = "SELECT b from BloodBagType";
    TypedQuery<BloodBagType> query = em.createQuery(queryString, BloodBagType.class);
    for (BloodBagType bloodBagType : query.getResultList()) {
      if (bloodBagType.getBloodBagType().equals(checkBloodBagType))
        return true;
    }
    return false;
  }
}
