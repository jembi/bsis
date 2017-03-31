package org.jembi.bsis.repository;

import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.donationtype.DonationType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonationTypeRepository extends AbstractRepository<DonationType> {

  public DonationType getDonationType(String donationType) {
    TypedQuery<DonationType> query;
    query = entityManager.createQuery("SELECT dt from DonationType dt where " +
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
      query = entityManager.createQuery("SELECT dt from DonationType dt", DonationType.class);
    } else {
      query = entityManager.createQuery("SELECT dt from DonationType dt where dt.isDeleted=:isDeleted", DonationType.class);
      query.setParameter("isDeleted", false);
    }
    return query.getResultList();
  }

  public DonationType getDonationTypeById(UUID donationTypeId) throws NoResultException {
    TypedQuery<DonationType> query;
    query = entityManager.createQuery("SELECT d from DonationType d " +
        "where d.id=:id", DonationType.class);
    query.setParameter("id", donationTypeId);
    return query.getSingleResult();
  }
}
