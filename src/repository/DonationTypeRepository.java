package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.donationtype.DonationType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonationTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public DonationType getDonationType(String checkDonationType) {
    TypedQuery<DonationType> query;
    query = em.createQuery("SELECT dt from DonationType dt where " +
    		                   "dt.donorType=:donorType and dt.isDeleted=:isDeleted", DonationType.class);
    query.setParameter("donorType", checkDonationType);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }

  public List<DonationType> getAllDonationTypes() {
    TypedQuery<DonationType> query;
    query = em.createQuery("SELECT dt from DonationType dt where dt.isDeleted=:isDeleted", DonationType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public DonationType getDonationTypeById(Integer donorTypeId) {
    TypedQuery<DonationType> query;
    query = em.createQuery("SELECT d from DonationType d " +
            "where d.id=:id AND d.isDeleted=:isDeleted", DonationType.class);
    query.setParameter("isDeleted", false);
    query.setParameter("id", donorTypeId);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveAllDonationTypes(List<DonationType> allDonationTypes) {
    for (DonationType dt: allDonationTypes) {
      if (dt.getId() == null)
        em.persist(dt);
      else {
        DonationType existingDonationType = getDonationTypeById(dt.getId());
        if (existingDonationType != null) {
          existingDonationType.setDonationType(dt.getDonationType());
          em.merge(existingDonationType);
        }
      }
      em.flush();
    }
  }

}
