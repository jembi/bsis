package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.donortype.DonorType;
import model.producttype.ProductType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonorTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public DonorType getDonorType(String checkDonorType) {
    TypedQuery<DonorType> query;
    query = em.createQuery("SELECT dt from DonorType dt where " +
    		                   "dt.donorType=:donorType and dt.isDeleted=:isDeleted", DonorType.class);
    query.setParameter("donorType", checkDonorType);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }

  public List<DonorType> getAllDonorTypes() {
    TypedQuery<DonorType> query;
    query = em.createQuery("SELECT dt from DonorType dt where dt.isDeleted=:isDeleted", DonorType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public DonorType fromString(String donorType) {
    TypedQuery<DonorType> query;
    query = em.createQuery("SELECT dt from DonorType dt " +
            "where dt.donorType=:donorType AND dt.isDeleted=:isDeleted", DonorType.class);
    query.setParameter("donorType", donorType);
    query.setParameter("isDeleted", false);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveAllDonorTypes(List<DonorType> allDonorTypes) {
    for (DonorType dt: allDonorTypes) {
      DonorType existingDonorType = fromString(dt.getDonorType());
      if (existingDonorType != null) {
        existingDonorType.setDonorTypeName(dt.getDonorTypeName());
        em.merge(existingDonorType);
      }
      else {
        em.persist(dt);
      }
    }
    em.flush();
  }
}
