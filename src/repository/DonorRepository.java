package repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Collection;
import model.Donor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Repository
@Transactional
public class DonorRepository {
  @PersistenceContext
  private EntityManager em;

  public void saveDonor(Donor donor) {
    em.persist(donor);
    em.flush();
  }

  public Donor updateOrAddDonor(Donor donor) {
    Donor existingDonor = findDonorByNumber(donor.getDonorNumber());
    if (existingDonor == null) {
      donor.setIsDeleted(false);
      saveDonor(donor);
      return donor;
    }
    existingDonor.copy(donor);
    existingDonor.setIsDeleted(false);
    em.merge(existingDonor);
    em.flush();
    return existingDonor;
  }

  public void deleteDonor(String donorId) {
    Donor existingDonor = findDonorByNumber(donorId);
    existingDonor.setIsDeleted(Boolean.TRUE);
    em.merge(existingDonor);
    em.flush();
  }

  public Donor findDonorById(Long donorId) {
    try {
      String queryString = "SELECT d FROM Donor d WHERE d.donorId = :donorId and d.isDeleted = :isDeleted";
      TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      return query.setParameter("donorId", donorId).getSingleResult();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public Donor findDonorByNumber(String donorNumber) {
    try {
      String queryString = "SELECT d FROM Donor d WHERE d.donorNumber = :donorNumber and d.isDeleted = :isDeleted";
      TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      Donor resultDonor = query.setParameter("donorNumber", donorNumber)
          .getSingleResult();
      if (resultDonor != null) {
        resultDonor.setIsDeleted(Boolean.FALSE);
      }
      return resultDonor;
    } catch (NoResultException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public void deleteAllDonors() {
    Query query = em.createQuery("DELETE FROM Donor d");
    query.executeUpdate();
  }

  public List<Donor> find(String donorNumber, String firstName, String lastName) {
    if (StringUtils.hasText(donorNumber)) {
      String queryString = "SELECT d FROM Donor d WHERE d.donorNumber = :donorNumber and d.isDeleted = :isDeleted";
      TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      List<Donor> donors = query.setParameter("donorNumber", donorNumber)
          .getResultList();
      if (donors != null && donors.size() > 0) {
        return donors;
      } else {
        if (StringUtils.hasText(firstName) || StringUtils.hasText(lastName)) {
          return findDonorByName(firstName, lastName);
        }
      }
    }
    if (StringUtils.hasText(firstName) || StringUtils.hasText(lastName)) {
      return findDonorByName(firstName, lastName);
    }
    return null;
  }

  public List<Donor> findAnyDonor(String donorNumber, String firstName,
      String lastName, List<String> bloodTypes) {

    String queryString = "SELECT d from Donor d WHERE "
        + "(isDeleted = :isDeleted) AND (" + "d.donorNumber = :donorNumber "
        + "OR d.firstName = :firstName " + "OR d.lastName = :lastName "
        + "OR d.bloodType IN (:bloodTypes)" + ")";

    TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("donorNumber", donorNumber);
    query.setParameter("firstName", firstName);
    query.setParameter("lastName", lastName);
    query.setParameter("bloodTypes",
        bloodTypes == null || bloodTypes.size() == 0 ? Arrays.asList("")
            : bloodTypes);

    List<Donor> donors = query.getResultList();
    if (donors != null && donors.size() > 0)
      return donors;
    return new ArrayList<Donor>();
  }

  private List<Donor> findDonorByName(String firstName, String lastName) {
    if (StringUtils.hasText(lastName)) {
      String queryString = "SELECT d FROM Donor d WHERE d.lastName = :lastName and d.isDeleted = :isDeleted";
      TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      List<Donor> donors = query.setParameter("lastName", lastName)
          .getResultList();
      if (donors == null || donors.size() == 0) {
        return null;
      }
      if (StringUtils.hasText(firstName)) {
        return filterByFirstName(donors, firstName);
      }
    } else if (StringUtils.hasText(firstName)) {
      String queryString = "SELECT d FROM Donor d WHERE d.firstName = :firstName and d.isDeleted = :isDeleted";
      TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      List<Donor> donors = query.setParameter("firstName", firstName)
          .getResultList();
      if (donors == null || donors.size() == 0) {
        return null;
      }
      return donors;
    }
    return null;
  }

  private List<Donor> filterByFirstName(List<Donor> donors,
      final String firstName) {
    List<Donor> filteredDonors = (List<Donor>) CollectionUtils.select(donors,
        new Predicate() {
          public boolean evaluate(Object o) {
            Donor donor = (Donor) o;
            return firstName.equals(donor.getFirstName());
          }
        });
    if (filteredDonors == null || filteredDonors.size() == 0) {
      return donors;
    } else {
      return filteredDonors;
    }

  }

  public List<Donor> getAllDonors() {
    Query query = em.createQuery(
        "SELECT d FROM Donor d WHERE d.isDeleted = :isDeleted", Donor.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.getResultList();
  }

  public List<Collection> getDonorHistory(String donorNumber) {
    TypedQuery<Collection> query = em
        .createQuery(
            "SELECT c FROM Collection c WHERE c.donorNumber = :donorNumber and c.isDeleted= :isDeleted",
            Collection.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("donorNumber", donorNumber);
    List<Collection> collections = query.getResultList();
    if (org.springframework.util.CollectionUtils.isEmpty(collections)) {
      return null;
    }
    return collections;
  }
}
