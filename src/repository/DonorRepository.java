package repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dto.DuplicateDonorDTO;
import model.address.AddressType;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.donor.DuplicateDonorBackup;
import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;
import model.idtype.IdType;
import model.preferredlanguage.PreferredLanguage;
import model.util.Gender;
import service.GeneralConfigAccessorService;
import utils.DonorUtils;
import viewmodel.DonorSummaryViewModel;
import dto.MobileClinicDonorDTO;

@Repository
@Transactional
public class DonorRepository {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private GeneralConfigAccessorService generalConfigAccessorService;

  public void saveDonor(Donor donor) {
    em.persist(donor);
    em.flush();
  }

  public Donor findDonorById(Long donorId) throws NoResultException {
    String queryString = "SELECT d FROM Donor d LEFT JOIN FETCH d.donations WHERE d.id = :donorId and d.isDeleted = :isDeleted and d.donorStatus not in :donorStatus";
    TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("donorStatus", Arrays.asList(DonorStatus.MERGED));
    return query.setParameter("donorId", donorId).getSingleResult();

  }

  public Donor findDonorByDonorNumber(String donorNumber) throws NoResultException {
    return em.createNamedQuery(DonorNamedQueryConstants.NAME_FIND_DONOR_BY_DONOR_NUMBER, Donor.class)
        .setParameter("donorNumber", donorNumber)
        .getSingleResult();
  }

  public Donor findDonorByDonationIdentificationNumber(String donationIdentificationNumber) throws NoResultException {
    return em.createNamedQuery(DonorNamedQueryConstants.NAME_FIND_DONOR_BY_DONATION_IDENTIFICATION_NUMBER, Donor.class)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .getSingleResult();
  }

  public List<Donor> findAnyDonor(String firstName, String lastName, boolean usePhraseMatch) {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Donor> query = builder.createQuery(Donor.class);
    Root<Donor> root = query.from(Donor.class);

    List<Predicate> expressions = new ArrayList<>();

    String donorSearchMode = generalConfigAccessorService.getGeneralConfigValueByName("donor.searchMode");

    if (!usePhraseMatch) {

      if (StringUtils.isNotBlank(firstName)) {
        // Match on exact first name
        expressions.add(builder.equal(root.<String>get("firstName"), firstName));
      }

      if (StringUtils.isNotBlank(lastName)) {
        // Match on exact last name
        expressions.add(builder.equal(root.<String>get("lastName"), lastName));
      }
    } else {

      if (StringUtils.isNotBlank(firstName)) {
        // Match on similar first name
        expressions.add(builder.like(root.<String>get("firstName"), createLikeExpression(firstName, donorSearchMode)));
      }

      if (StringUtils.isNotBlank(lastName)) {
        // Match on similar last name
        expressions.add(builder.like(root.<String>get("lastName"), createLikeExpression(lastName, donorSearchMode)));
      }
    }

    // Exclude deleted donors
    expressions.add(builder.equal(root.<String>get("isDeleted"), false));
    // Exclude donors with a status of merged
    expressions.add(builder.not(root.get("donorStatus").in(Arrays.asList(DonorStatus.MERGED))));

    // Build the where clause
    query.where(builder.and(expressions.toArray(new Predicate[expressions.size()])));

    return em.createQuery(query).getResultList();
  }

  private String createLikeExpression(String search, String searchMode) {

    switch (searchMode) {

      case "start":
        return search + "%";

      case "end":
        return "%" + search;

      default:
        return "%" + search + "%";
    }
  }

  public Donor addDonor(Donor donor) throws PersistenceException {
    updateDonorAutomaticFields(donor);
    em.persist(donor);
    em.flush();
    return donor;
  }

  public Donor updateDonorDetails(Donor donor) {
    Donor existingDonor = findDonorById(donor.getId());
    if (existingDonor == null) {
      return null;
    }
    donor.getAddress().setId(existingDonor.getAddress().getId());
    donor.getContact().setId(existingDonor.getContact().getId());
    existingDonor.copy(donor);
    existingDonor.setIsDeleted(false);
    em.merge(existingDonor);
    em.flush();
    return existingDonor;
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public Donor updateDonor(Donor donor) {
    return em.merge(donor);
  }

  private void updateDonorAutomaticFields(Donor donor) {
    if (donor.getDonorStatus() == null) {
      donor.setDonorStatus(DonorStatus.NORMAL);
    }
    if (donor.getBloodAbo() == null) {
      donor.setBloodAbo("");
    }
    if (donor.getBloodRh() == null) {
      donor.setBloodRh("");
    }
    donor.setDonorHash(DonorUtils.computeDonorHash(donor));
  }

  public Donor findDonorByDonorNumber(String donorNumber, boolean isDelete, DonorStatus... withoutDonorStatus) {
    Donor donor = null;
    String queryString = "SELECT d FROM Donor d LEFT JOIN FETCH d.donations WHERE d.donorNumber = :donorNumber and d.isDeleted = :isDeleted";
    if (withoutDonorStatus != null && withoutDonorStatus.length > 0) {
      queryString = queryString + " and d.donorStatus not in :donorStatus";
    }
    TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
    query.setParameter("isDeleted", isDelete);
    if (withoutDonorStatus != null && withoutDonorStatus.length > 0) {
      query.setParameter("donorStatus", Arrays.asList(withoutDonorStatus));
    }
    try {
      donor = query.setParameter("donorNumber", donorNumber).getSingleResult();
    } catch (NoResultException ex) {
      return null;
    } catch (NonUniqueResultException ex) {
      ex.printStackTrace();
    }
    return donor;
  }

  public List<PreferredLanguage> getAllLanguages() {
    TypedQuery<PreferredLanguage> query = em.createQuery(
        "SELECT l FROM PreferredLanguage l", PreferredLanguage.class);
    return query.getResultList();
  }

  public List<IdType> getAllIdTypes() {
    TypedQuery<IdType> query = em.createQuery(
        "SELECT id FROM IdType id", IdType.class);
    return query.getResultList();
  }

  public List<DeferralReason> getDeferralReasons() {
    String queryString = "SELECT d from DeferralReason d WHERE d.isDeleted=:isDeleted";
    TypedQuery<DeferralReason> query = em.createQuery(queryString, DeferralReason.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public DonorDeferral deferDonor(DonorDeferral deferral) throws PersistenceException {
    em.persist(deferral);
    em.flush();
    return deferral;
  }

  public List<DonorDeferral> getDonorDeferrals(Long donorId) throws NoResultException {
    String queryString = "SELECT d from DonorDeferral d WHERE "
        + " d.deferredDonor.id=:donorId AND d.isVoided=:isVoided";
    TypedQuery<DonorDeferral> query = em.createQuery(queryString, DonorDeferral.class);
    query.setParameter("donorId", donorId);
    query.setParameter("isVoided", Boolean.FALSE);
    return query.getResultList();
  }

  public Date getLastDonorDeferralDate(Long donorId) {
    List<DonorDeferral> deferrals = getDonorDeferrals(donorId);

    if (deferrals == null || deferrals.isEmpty()) {
      return null;
    }

    Date lastDeferredUntil = deferrals.get(0).getDeferredUntil();
    for (DonorDeferral deferral : deferrals) {
      if (deferral.getDeferredUntil() != null && deferral.getDeferredUntil().after(lastDeferredUntil)) {
        lastDeferredUntil = deferral.getDeferredUntil();
      }
    }
    return lastDeferredUntil;
  }

  public DonorDeferral getLastDonorDeferral(Long donorId) {
    List<DonorDeferral> deferrals = getDonorDeferrals(donorId);

    if (deferrals == null || deferrals.isEmpty()) {
      return null;
    }

    DonorDeferral lastDeferral = deferrals.get(0);
    Date lastDeferredUntil = lastDeferral.getDeferredUntil();
    for (DonorDeferral deferral : deferrals) {
      if (deferral.getDeferredUntil() != null && deferral.getDeferredUntil().after(lastDeferredUntil)) {
        lastDeferral = deferral;
        lastDeferredUntil = lastDeferral.getDeferredUntil();
      }
    }
    return lastDeferral;
  }

  public List<AddressType> getAllAddressTypes() {
    TypedQuery<AddressType> query = em.createQuery(
        "SELECT addressType FROM AddressType addressType", AddressType.class);
    return query.getResultList();

  }

  public DonorSummaryViewModel findDonorSummaryByDonorNumber(String donorNumber) throws NoResultException {
    return em.createQuery(
        "SELECT NEW viewmodel.DonorSummaryViewModel(d.id, d.firstName, d.lastName, d.gender, d.birthDate) " +
            "FROM Donor d " +
            "WHERE d.donorNumber = :donorNumber " +
            "AND d.isDeleted = :isDeleted " +
            "AND d.donorStatus NOT IN :excludedStatuses ",
        DonorSummaryViewModel.class)
        .setParameter("donorNumber", donorNumber)
        .setParameter("isDeleted", false)
        .setParameter("excludedStatuses", Arrays.asList(DonorStatus.MERGED))
        .getSingleResult();
  }

  public List<Donor> findDonorsByNumbers(List<String> donorNumbers) {
    if (donorNumbers == null || donorNumbers.size() == 0) {
      return new ArrayList<Donor>();
    }
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Donor> cq = cb.createQuery(Donor.class);
    Root<Donor> donor = cq.from(Donor.class);

    Predicate inDonorNumbers = donor.get("donorNumber").in(donorNumbers);
    Predicate notDeleted = cb.equal(donor.<String>get("isDeleted"), false);
    Predicate notMerged = cb.not(donor.get("donorStatus").in(Arrays.asList(DonorStatus.MERGED)));

    cq.select(donor).where(cb.and(inDonorNumbers, notMerged, notDeleted));
    return em.createQuery(cq).getResultList();
  }

  public Donor addMergedDonor(Donor newDonor, List<Donor> mergedDonors, List<DuplicateDonorBackup> backupLogs) {
    updateDonorAutomaticFields(newDonor);
    em.persist(newDonor);
    for (Donor donor : mergedDonors) {
      em.persist(donor);
    }
    for (DuplicateDonorBackup backupLog : backupLogs) {
      em.persist(backupLog);
    }
    em.flush();
    return newDonor;
  }

  public List<DuplicateDonorDTO> getDuplicateDonors() {
    return em.createNamedQuery(DonorNamedQueryConstants.NAME_GET_ALL_DUPLICATE_DONORS, DuplicateDonorDTO.class)
        .getResultList();
  }
  
  public List<Donor> getDuplicateDonors(String firstName, String lastName, Date birthDate, Gender gender) {
    return em.createNamedQuery(DonorNamedQueryConstants.NAME_GET_DUPLICATE_DONORS, Donor.class)
        .setParameter("firstName", firstName)
        .setParameter("lastName", lastName)
        .setParameter("birthDate", birthDate)
        .setParameter("gender", gender)
        .getResultList();
  }

  public List<MobileClinicDonorDTO> findMobileClinicDonorsByVenue(Long venueId) throws NoResultException {
    return em.createNamedQuery(DonorNamedQueryConstants.NAME_MOBILE_CLINIC_LOOKUP, MobileClinicDonorDTO.class)
        .setParameter("venueId", venueId)
        .setParameter("isDeleted", false)
        .setParameter("excludedStatuses", Arrays.asList(DonorStatus.MERGED))
        .getResultList();
  }
  
  public boolean verifyDonorExists(Long id) {
    Long count = em.createNamedQuery(DonorNamedQueryConstants.NAME_COUNT_DONOR_WITH_ID, Long.class)
        .setParameter("id", id)
        .getSingleResult();
    if (count == 1) {
      return true;
    }
    return false;
  }
}
