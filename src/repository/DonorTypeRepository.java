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

  public boolean isDonorTypeValid(String checkDonorType) {
    String queryString = "SELECT dt from DonorType dt";
    TypedQuery<DonorType> query = em.createQuery(queryString, DonorType.class);
    for (DonorType donorType : query.getResultList()) {
      System.out.println(donorType);
      System.out.println(checkDonorType);
      if (donorType.getDonorType().equals(checkDonorType))
        return true;
    }
    return false;
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
