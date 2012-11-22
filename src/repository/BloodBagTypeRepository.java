package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.bloodbagtype.BloodBagType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BloodBagTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public List<BloodBagType> getAllBloodBagTypes() {
    TypedQuery<BloodBagType> query;
    query = em.createQuery("SELECT b from BloodBagType b", BloodBagType.class);
    return query.getResultList();
  }
  
  public boolean isBloodBagTypeValid(String checkBloodBagType) {
    String queryString = "SELECT b from BloodBagType b";
    TypedQuery<BloodBagType> query = em.createQuery(queryString, BloodBagType.class);
    for (BloodBagType bloodBagType : query.getResultList()) {
      if (bloodBagType.getBloodBagType().equals(checkBloodBagType))
        return true;
    }
    return false;
  }

  public BloodBagType fromString(String bloodBagType) {
    TypedQuery<BloodBagType> query;
    query = em.createQuery("SELECT b from BloodBagType b " +
    		    "where b.bloodBagType=:bloodBagType", BloodBagType.class);
    query.setParameter("bloodBagType", bloodBagType);
    return query.getSingleResult();
  }
}
