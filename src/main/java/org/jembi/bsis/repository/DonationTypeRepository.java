package org.jembi.bsis.repository;

import java.util.List;

import javax.persistence.*;

import org.jembi.bsis.model.donationtype.DonationType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonationTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public DonationType getDonationType(String donationType) {
    TypedQuery<DonationType> query;
    query = em.createQuery("SELECT dt from DonationType dt where " +
        "dt.donationType=:donationType", DonationType.class);
    query.setParameter("donationType", donationType);

    DonationType result = null;
    try {
      result = query.getSingleResult();
    } catch (NoResultException ex) {
      return null;
    } catch (NonUniqueResultException ex) {
      throw new NonUniqueResultException("More than one donation type exists with name :" + donationType);
    }
    return result;
  }

  public List<DonationType> getAllDonationTypes() {
    return getAllDonationTypes(false);
  }

  public List<DonationType> getAllDonationTypes(Boolean includeDeleted) {
    TypedQuery<DonationType> query;
    if (includeDeleted) {
      query = em.createQuery("SELECT dt from DonationType dt", DonationType.class);
    } else {
      query = em.createQuery("SELECT dt from DonationType dt where dt.isDeleted=:isDeleted", DonationType.class);
      query.setParameter("isDeleted", false);
    }
    return query.getResultList();
  }

  public DonationType getDonationTypeById(Long donorTypeId) {
    TypedQuery<DonationType> query;
    query = em.createQuery("SELECT d from DonationType d " +
        "where d.id=:id", DonationType.class);
    query.setParameter("id", donorTypeId);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public DonationType saveDonationType(DonationType donationType) {
    em.persist(donationType);
    em.flush();
    return donationType;
  }

  public DonationType updateDonationType(DonationType donationType) {
    em.merge(donationType);
    em.flush();
    return donationType;
  }

}
