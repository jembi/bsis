package repository;

import java.util.List;

import javax.persistence.*;

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
            "dt.donationType=:donationType and dt.isDeleted=:isDeleted", DonationType.class);
    query.setParameter("donationType", checkDonationType);
    query.setParameter("isDeleted", false);
    DonationType result = null;
    try {
      result = query.getSingleResult();
    } catch (NoResultException ex) {
      return null;
    } catch (NonUniqueResultException ex) {
      throw new NonUniqueResultException("More than one donation type exists with name :" + checkDonationType);
    }
    return result;
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

 /*
  issue - #209 - Not used anywhere
  *
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
  */
  
  public DonationType saveDonationType(DonationType donationType){
      em.persist(donationType);
      em.flush();
      return donationType;
  }
  
  public DonationType updateDonationType(DonationType donationType){
      em.merge(donationType);
      em.flush();
      return donationType;
  }

}
