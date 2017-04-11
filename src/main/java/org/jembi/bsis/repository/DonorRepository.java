package org.jembi.bsis.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jembi.bsis.dto.DonorExportDTO;
import org.jembi.bsis.dto.DuplicateDonorDTO;
import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.model.address.AddressType;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.donor.DuplicateDonorBackup;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.idtype.IdType;
import org.jembi.bsis.model.preferredlanguage.PreferredLanguage;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.jembi.bsis.utils.DonorUtils;
import org.jembi.bsis.viewmodel.DonorSummaryViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonorRepository {
  
  private static final Logger LOGGER = Logger.getLogger(DonorRepository.class);

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private GeneralConfigAccessorService generalConfigAccessorService;

  public void saveDonor(Donor donor) {
    em.persist(donor);
    em.flush();
  }

  public Donor findDonorById(UUID donorId) throws NoResultException {
    String queryString = "SELECT d FROM Donor d LEFT JOIN FETCH d.donations WHERE d.id = :donorId and d.isDeleted = :isDeleted and d.donorStatus not in :donorStatus";
    TypedQuery<Donor> query = em.createQuery(queryString, Donor.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("donorStatus", Arrays.asList(DonorStatus.MERGED));
    return query.setParameter("donorId", donorId).getSingleResult();

  }

  public Donor findDonorByDonorNumber(String donorNumber) throws NoResultException {
    return em.createNamedQuery(DonorNamedQueryConstants.NAME_FIND_DONOR_BY_DONOR_NUMBER, Donor.class)
        .setParameter("donorNumber", donorNumber)
        .setParameter("excludedStatuses", Arrays.asList(DonorStatus.MERGED))
        .setParameter("isDeleted", Boolean.FALSE)
        .getSingleResult();
  }

  public Donor findDonorByDonationIdentificationNumber(String donationIdentificationNumber) throws NoResultException {
    return em.createNamedQuery(DonorNamedQueryConstants.NAME_FIND_DONOR_BY_DONATION_IDENTIFICATION_NUMBER, Donor.class)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .setParameter("excludedStatuses", Arrays.asList(DonorStatus.MERGED))
        .setParameter("isDeleted", Boolean.FALSE)
        .setParameter("isDonationDeleted", Boolean.FALSE)
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
      LOGGER.error("Duplicate Donor with  donorNumber '" + donorNumber + "'", ex);
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

  public List<DonorDeferral> getDonorDeferrals(UUID donorId) throws NoResultException {
    String queryString = "SELECT d from DonorDeferral d WHERE "
        + " d.deferredDonor.id=:donorId AND d.isVoided=:isVoided";
    TypedQuery<DonorDeferral> query = em.createQuery(queryString, DonorDeferral.class);
    query.setParameter("donorId", donorId);
    query.setParameter("isVoided", Boolean.FALSE);
    return query.getResultList();
  }

  public Date getLastDonorDeferralDate(UUID donorId) {
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

  public DonorDeferral getLastDonorDeferral(UUID donorId) {
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
        "SELECT NEW org.jembi.bsis.viewmodel.DonorSummaryViewModel(d.id, d.firstName, d.lastName, d.gender, d.birthDate) " +
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

  public List<MobileClinicDonorDTO> findMobileClinicDonorsByVenues(Set<UUID> venueIds) throws NoResultException {
    boolean includeAllVenues = venueIds == null || venueIds.isEmpty();

    // using empty list as a parameter causes a bad SQL Grammar exception. If the list is empty we have to
    // override the parameter to null.
    if (venueIds != null && venueIds.isEmpty()) {
      venueIds = null;
    }
    return em.createNamedQuery(DonorNamedQueryConstants.NAME_FIND_MOBILE_CLINIC_DONORS_BY_VENUES, MobileClinicDonorDTO.class)
        .setParameter("isDeleted", false)
        .setParameter("excludedStatuses", Arrays.asList(DonorStatus.MERGED))
        .setParameter("venueIds", venueIds)
        .setParameter("includeAllVenues", includeAllVenues)
        .getResultList();
}
  
  public boolean verifyDonorExists(UUID id) {
    Long count = em.createNamedQuery(DonorNamedQueryConstants.NAME_COUNT_DONOR_WITH_ID, Long.class)
        .setParameter("id", id)
        .setParameter("excludedStatuses", Arrays.asList(DonorStatus.MERGED))
        .setParameter("isDeleted", false)
        .getSingleResult();
    if (count == 1) {
      return true;
    }
    return false;
  }
  
  public List<DonorExportDTO> findDonorsForExport() {
    return em.createNamedQuery(DonorNamedQueryConstants.NAME_FIND_DONORS_FOR_EXPORT, DonorExportDTO.class)
        .setParameter("deleted", false)
        .getResultList();
  }
}