package repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import model.donor.Donor;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRhd;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonorRepository {

  public static final int ID_LENGTH = 12;

  @PersistenceContext
  private EntityManager em;

  public void saveDonor(Donor donor) {
    em.persist(donor);
    em.flush();
  }

  public void deleteDonor(Long donorId) {
    Donor existingDonor = findDonorById(donorId);
    existingDonor.setIsDeleted(Boolean.TRUE);
    em.merge(existingDonor);
    em.flush();
  }

  public Donor findDonorById(Long donorId) {
    String queryString = "SELECT d FROM Donor d LEFT JOIN FETCH d.collectedSamples  WHERE d.id = :donorId and d.isDeleted = :isDeleted";
    TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.setParameter("donorId", donorId).getSingleResult();
  }

  public Donor findDonorById(String donorId) {
    return findDonorById(Long.parseLong(donorId));
  }

  @SuppressWarnings("unchecked")
  public List<Object> findAnyDonor(String donorNumber, String firstName,
      String lastName, List<BloodGroup> bloodGroups, Map<String, Object> pagingParams) {

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Donor> cq = cb.createQuery(Donor.class);
    Root<Donor> root = cq.from(Donor.class);

    List<Predicate> bgPredicates = new ArrayList<Predicate>();
    for (BloodGroup bg : bloodGroups) {
      Expression<Boolean> aboExp = cb.equal(root.<BloodAbo>get("bloodAbo"), bg.getBloodAbo());
      Expression<Boolean> rhdExp = cb.equal(root.<BloodRhd>get("bloodRhd"), bg.getBloodRhd());
      bgPredicates.add(cb.and(aboExp, rhdExp));
    }

    Expression<Boolean> exp1 = cb.or(bgPredicates.toArray(new Predicate[0]));
    Predicate donorNumberExp = cb.equal(root.<String>get("donorNumber"), donorNumber);

    Predicate firstNameExp;
    if (firstName.trim().equals(""))
      firstNameExp = cb.disjunction();
    else
      firstNameExp = cb.like(root.<String>get("firstName"), firstName + "%");

    Predicate lastNameExp;
    if (lastName.trim().equals(""))
      lastNameExp = cb.disjunction();
    else
      lastNameExp = cb.like(root.<String>get("lastName"), lastName + "%");

    Expression<Boolean> exp2;
    if ( (donorNumber == null || donorNumber.trim().isEmpty()) && 
         (firstName == null || firstName.trim().isEmpty()) &&
         (lastName == null || lastName.trim().isEmpty())
      )
      exp2 = cb.or(exp1, cb.or(donorNumberExp, firstNameExp, lastNameExp));
    else
      exp2 = cb.and(exp1, cb.or(donorNumberExp, firstNameExp, lastNameExp));

    Predicate notDeleted = cb.equal(root.<String>get("isDeleted"), false);
    cq.where(cb.and(notDeleted, exp2));

    int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
    int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);

    if (pagingParams.containsKey("sortColumn") && pagingParams.containsKey("sortDirection")) {
      List<Order> order = new ArrayList<Order>();
      if (pagingParams.get("sortDirection").equals("asc")) {
        order.add(cb.asc(root.<String>get((String)pagingParams.get("sortColumn"))));
      } else {
        order.add(cb.desc(root.<String>get((String)pagingParams.get("sortColumn"))));
      }
      cq.orderBy(order);
    }

    TypedQuery<Donor> query = em.createQuery(cq);
    query.setFirstResult(start);
    query.setMaxResults(length);   

    CriteriaQuery<Long> countCriteriaQuery = cb.createQuery(Long.class);
    Root<Donor> countRoot = countCriteriaQuery.from(Donor.class);
    countCriteriaQuery.where(cb.and(notDeleted, exp2));
    countCriteriaQuery.select(cb.countDistinct(countRoot));

    TypedQuery<Long> countQuery = em.createQuery(countCriteriaQuery);
    Long totalResults = countQuery.getSingleResult().longValue();
    return Arrays.asList(query.getResultList(), totalResults);
  }

  public List<Donor> getAllDonors() {
    Query query = em.createQuery(
        "SELECT d FROM Donor d WHERE d.isDeleted = :isDeleted", Donor.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.getResultList();
  }

  public Donor addDonor(Donor donor) {
    donor.setBloodAbo(BloodAbo.Unknown);
    donor.setBloodRhd(BloodRhd.Unknown);
    em.persist(donor);
    em.flush();
    return donor;
  }

  public Donor updateDonor(Donor donor) {
    Donor existingDonor = findDonorById(donor.getId());
    if (existingDonor == null) {
      return null;
    }
    existingDonor.copy(donor);
    existingDonor.setIsDeleted(false);
    em.merge(existingDonor);
    em.flush();
    return existingDonor;
  }

  public Donor findDonorByNumber(String donorNumber) {
    try {
      String queryString = "SELECT d FROM Donor d WHERE d.donorNumber = :donorNumber and d.isDeleted = :isDeleted";
      TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      return query.setParameter("donorNumber", donorNumber).getSingleResult();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public List<Donor> findAnyDonorStartsWith(String term) {

    term = term.trim();
    if (term.length() < 2)
      return Arrays.asList(new Donor[0]);

    try {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Donor> cq = cb.createQuery(Donor.class);
      Root<Donor> root = cq.from(Donor.class);

      Predicate donorNumberExp = cb.like(root.<String>get("donorNumber"), term + "%");
      Predicate firstNameExp;
      if (term.equals(""))
        firstNameExp = cb.disjunction();
      else
        firstNameExp = cb.like(root.<String>get("firstName"), term + "%");

      Predicate lastNameExp;
      if (term.equals(""))
        lastNameExp = cb.disjunction();
      else
        lastNameExp = cb.like(root.<String>get("lastName"), term + "%");
      Expression<Boolean> exp = cb.or(donorNumberExp, firstNameExp, lastNameExp);

      Predicate notDeleted = cb.equal(root.<String>get("isDeleted"), false);
      cq.where(cb.and(notDeleted, exp));
      TypedQuery<Donor> query = em.createQuery(cq);
      List<Donor> donors = query.getResultList();
      if (donors != null && donors.size() > 0)
        return donors;
      return new ArrayList<Donor>();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static String generateUniqueDonorNumber() {
    String uniqueDonorNumber;
    uniqueDonorNumber = "D-" + RandomStringUtils.randomNumeric(ID_LENGTH).toUpperCase();
    return uniqueDonorNumber;
  }

  public void addAllDonors(List<Donor> donors) {
    for (Donor donor : donors) {
      em.persist(donor);
    }
    em.flush();
  }

  public Donor findDonorByDonorNumber(String donorNumber) {
    String queryString = "SELECT d FROM Donor d LEFT JOIN FETCH d.collectedSamples  WHERE d.donorNumber = :donorNumber and d.isDeleted = :isDeleted";
    TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.setParameter("donorNumber", donorNumber).getSingleResult();
  }
}
