package repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.donor.DonorType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonorTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public boolean isDonorTypeValid(String checkDonorType) {
    String queryString = "SELECT dt from DonorType";
    TypedQuery<DonorType> query = em.createQuery(queryString, DonorType.class);
    for (DonorType donorType : query.getResultList()) {
      if (donorType.getDonorType().equals(checkDonorType))
        return true;
    }
    return false;
  }
}
