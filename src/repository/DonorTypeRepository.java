package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.donortype.DonorType;

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
    		                   "donorType=:donorType and isDeleted=:isDeleted", DonorType.class);
    query.setParameter("donorType", checkDonorType);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }

  public List<DonorType> getAllDonorTypes() {
    TypedQuery<DonorType> query;
    query = em.createQuery("SELECT dt from DonorType dt", DonorType.class);
    return query.getResultList();
  }

  public DonorType fromString(String donorType) {
    TypedQuery<DonorType> query;
    query = em.createQuery("SELECT dt from DonorType dt " +
            "where b.donorType=:donorType", DonorType.class);
    query.setParameter("donorType", donorType);
    return query.getSingleResult();
  }
}
