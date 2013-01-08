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
    query = em.createQuery("SELECT b from BloodBagType b where b.isDeleted=:isDeleted", BloodBagType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }
  
  public BloodBagType getBloodBagType(String checkBloodBagType) {
    TypedQuery<BloodBagType> query;
    query = em.createQuery("SELECT b from BloodBagType b " +
            "where b.bloodBagType=:bloodBagType AND isDeleted=:isDeleted", BloodBagType.class);
    query.setParameter("bloodBagType", checkBloodBagType);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }

  public BloodBagType fromString(String bloodBagType) {
    TypedQuery<BloodBagType> query;
    query = em.createQuery("SELECT b from BloodBagType b " +
    		    "where b.bloodBagType=:bloodBagType AND b.isDeleted=:isDeleted", BloodBagType.class);
    query.setParameter("bloodBagType", bloodBagType);
    query.setParameter("isDeleted", false);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveAllBloodBagTypes(List<BloodBagType> allBloodBagTypes) {
      for (BloodBagType pt: allBloodBagTypes) {
        BloodBagType existingBloodBagType = fromString(pt.getBloodBagType());
        if (existingBloodBagType != null) {
          existingBloodBagType.setBloodBagTypeName(pt.getBloodBagTypeName());
          em.merge(existingBloodBagType);
        }
        else {
          em.persist(pt);
        }
    }
    em.flush();
  }
}
