package org.jembi.bsis.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.jembi.bsis.dto.CollectedDonationDTO;
import org.jembi.bsis.dto.DonationExportDTO;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonationRepository {

  private static final Logger LOGGER = Logger.getLogger(DonationRepository.class);

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  public void saveDonation(Donation donation) {
    em.persist(donation);
    em.flush();
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public Donation updateDonation(Donation donation) {
    return em.merge(donation);
  }

  public Donation findDonationById(UUID donationId) throws NoResultException {
    String queryString = "SELECT c FROM Donation c LEFT JOIN FETCH c.donor WHERE c.id = :donationId and c.isDeleted = :isDeleted";
    TypedQuery<Donation> query = em.createQuery(queryString, Donation.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    try {
      return query.setParameter("donationId", donationId).getSingleResult();
    } catch (NoResultException ex) {
      throw new NoResultException("No Donation Exists with ID :" + donationId);
    }
  }

  public List<Donation> getDonations(Date fromDate, Date toDate) {
    TypedQuery<Donation> query = em
        .createQuery(
            "SELECT c FROM Donation c WHERE c.donationDate >= :fromDate and c.donationDate<= :toDate and c.isDeleted= :isDeleted",
            Donation.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    List<Donation> donations = query.getResultList();
    if (donations.isEmpty()) {
      return new ArrayList<Donation>();
    }
    return donations;
  }

  public Donation findDonationByDonationIdentificationNumber(
      String donationIdentificationNumber) throws NoResultException, NonUniqueResultException {

    Donation donation = em.createNamedQuery(DonationNamedQueryConstants.NAME_FIND_DONATION_BY_DONATION_IDENTIFICATION_NUMBER, Donation.class) 
        .setParameter("isDeleted", Boolean.FALSE)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .getSingleResult();

    return donation;
  }

  public Donation findDonationByDonationIdentificationNumberIncludeDeleted(
      String donationIdentificationNumber) {
    Donation donation = null; 

    try {
      donation = em.createNamedQuery(DonationNamedQueryConstants.NAME_FIND_DONATION_BY_DONATION_IDENTIFICATION_NUMBER_INCLUDE_DELETED, Donation.class) 
          .setParameter("donationIdentificationNumber", donationIdentificationNumber) 
          .getSingleResult(); 
    } catch(NoResultException e) {
      LOGGER.debug("No donation found for DIN '" + donationIdentificationNumber + "'");
    }
    return donation;
  }

  public int countDonationsForDonor(Donor donor) {

    return em.createNamedQuery(
        DonationNamedQueryConstants.NAME_COUNT_DONATIONS_FOR_DONOR,
        Number.class)
        .setParameter("donor", donor)
        .setParameter("deleted", false)
        .getSingleResult()
        .intValue();
  }

  public Date findDateOfFirstDonationForDonor(UUID donorId) {
    List<Date> results = em.createNamedQuery(
        DonationNamedQueryConstants.NAME_FIND_ASCENDING_DONATION_DATES_FOR_DONOR,
        Date.class)
        .setParameter("donorId", donorId)
        .setParameter("deleted", false)
        .setMaxResults(1)
        .getResultList();

    return results.isEmpty() ? null : results.get(0);
  }

  public Date findDateOfLastDonationForDonor(UUID donorId) {
    List<Date> results = em.createNamedQuery(
        DonationNamedQueryConstants.NAME_FIND_DESCENDING_DONATION_DATES_FOR_DONOR,
        Date.class)
        .setParameter("donorId", donorId)
        .setParameter("deleted", false)
        .setMaxResults(1)
        .getResultList();

    return results.isEmpty() ? null : results.get(0);
  }

  public List<CollectedDonationDTO> findCollectedDonationsReportIndicators(Date startDate, Date endDate) {
    return em.createNamedQuery(
        DonationNamedQueryConstants.NAME_FIND_COLLECTED_DONATION_VALUE_OBJECTS_FOR_DATE_RANGE,
        CollectedDonationDTO.class)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("deleted", false)
        .getResultList();
  }

  public Date findLatestDueToDonateDateForDonor(UUID donorId) {

    List<Date> results = em.createNamedQuery(
        DonationNamedQueryConstants.NAME_FIND_LATEST_DUE_TO_DONATE_DATE_FOR_DONOR,
        Date.class)
        .setParameter("donorId", donorId)
        .setParameter("deleted", false)
        .setMaxResults(1)
        .getResultList();

    return results.isEmpty() ? null : results.get(0);
  }

  public Map<UUID, BloodTestingRuleResult> filterDonationsWithBloodTypingResults(
      Collection<Donation> donations) {
    Iterator<Donation> iter = donations.iterator();
    Map<UUID, BloodTestingRuleResult> statusMap = new HashMap<UUID, BloodTestingRuleResult>();
    while (iter.hasNext()) {
      Donation c = iter.next();
      BloodTypingStatus bloodTypingStatus = c.getBloodTypingStatus();
      if (bloodTypingStatus != null && !bloodTypingStatus.equals(BloodTypingStatus.NOT_DONE)) {
        statusMap.put(c.getId(), bloodTestingRepository.getAllTestsStatusForDonation(c.getId()));
      }
    }
    return statusMap;
  }
  
  public List<Donation> findLastDonationsByDonorVenueAndDonationDate(Location donorVenue, Date startDate, Date endDate) {
    return em.createNamedQuery(DonationNamedQueryConstants.NAME_FIND_LAST_DONATIONS_BY_DONOR_VENUE_AND_DONATION_DATE,
        Donation.class)
        .setParameter("venue", donorVenue)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("deleted", false)
        .getResultList();
  }
  
  public List<DonationExportDTO> findDonationsForExport() {
    return em.createNamedQuery(DonationNamedQueryConstants.NAME_FIND_DONATIONS_FOR_EXPORT, DonationExportDTO.class)
        .setParameter("deleted", false)
        .getResultList();
  }

  public List<Donation> findDonationsBetweenTwoDins(String fromDIN, String toDIN){
    return em.createNamedQuery(DonationNamedQueryConstants.NAME_FIND_DONATIONS_BETWEEN_TWO_DINS, Donation.class)
        .setParameter("fromDIN", fromDIN)
        .setParameter("toDIN", toDIN)
        .setParameter("deleted", false)
        .getResultList();
  }
}
