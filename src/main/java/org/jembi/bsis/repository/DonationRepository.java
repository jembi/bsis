package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.jembi.bsis.dto.CollectedDonationDTO;
import org.jembi.bsis.dto.DonationExportDTO;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonationRepository extends AbstractRepository<Donation> {

  private static final Logger LOGGER = Logger.getLogger(DonationRepository.class);

  public Donation findDonationById(UUID donationId) throws NoResultException {
    String queryString = "SELECT c FROM Donation c LEFT JOIN FETCH c.donor WHERE c.id = :donationId and c.isDeleted = :isDeleted";
    TypedQuery<Donation> query = entityManager.createQuery(queryString, Donation.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    try {
      return query.setParameter("donationId", donationId).getSingleResult();
    } catch (NoResultException ex) {
      throw new NoResultException("No Donation Exists with ID :" + donationId);
    }
  }

  public Donation findDonationByDonationIdentificationNumber(String donationIdentificationNumber)
      throws NoResultException, NonUniqueResultException {
    return entityManager.createNamedQuery(
        DonationNamedQueryConstants.NAME_FIND_DONATION_BY_DONATION_IDENTIFICATION_NUMBER, Donation.class)
        .setParameter("isDeleted", Boolean.FALSE)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .getSingleResult();
  }

  public Donation findDonationByDonationIdentificationNumberIncludeDeleted(
      String donationIdentificationNumber) {
    Donation donation = null; 

    try {
      donation = entityManager.createNamedQuery(DonationNamedQueryConstants.NAME_FIND_DONATION_BY_DONATION_IDENTIFICATION_NUMBER_INCLUDE_DELETED, Donation.class) 
          .setParameter("donationIdentificationNumber", donationIdentificationNumber) 
          .getSingleResult(); 
    } catch(NoResultException e) {
      LOGGER.debug("No donation found for DIN '" + donationIdentificationNumber + "'");
    }
    return donation;
  }

  public int countDonationsForDonor(Donor donor) {

    return entityManager.createNamedQuery(
        DonationNamedQueryConstants.NAME_COUNT_DONATIONS_FOR_DONOR,
        Number.class)
        .setParameter("donor", donor)
        .setParameter("deleted", false)
        .getSingleResult()
        .intValue();
  }

  public Date findDateOfFirstDonationForDonor(UUID donorId) {
    List<Date> results = entityManager.createNamedQuery(
        DonationNamedQueryConstants.NAME_FIND_ASCENDING_DONATION_DATES_FOR_DONOR,
        Date.class)
        .setParameter("donorId", donorId)
        .setParameter("deleted", false)
        .setMaxResults(1)
        .getResultList();

    return results.isEmpty() ? null : results.get(0);
  }

  public Date findDateOfLastDonationForDonor(UUID donorId) {
    List<Date> results = entityManager.createNamedQuery(
        DonationNamedQueryConstants.NAME_FIND_DESCENDING_DONATION_DATES_FOR_DONOR,
        Date.class)
        .setParameter("donorId", donorId)
        .setParameter("deleted", false)
        .setMaxResults(1)
        .getResultList();

    return results.isEmpty() ? null : results.get(0);
  }

  public List<CollectedDonationDTO> findCollectedDonationsReportIndicators(Date startDate, Date endDate) {
    return entityManager.createNamedQuery(
        DonationNamedQueryConstants.NAME_FIND_COLLECTED_DONATION_VALUE_OBJECTS_FOR_DATE_RANGE,
        CollectedDonationDTO.class)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("deleted", false)
        .setParameter("countAsDonation", true)
        .getResultList();
  }

  public Date findLatestDueToDonateDateForDonor(UUID donorId) {

    List<Date> results = entityManager.createNamedQuery(
        DonationNamedQueryConstants.NAME_FIND_LATEST_DUE_TO_DONATE_DATE_FOR_DONOR,
        Date.class)
        .setParameter("donorId", donorId)
        .setParameter("deleted", false)
        .setMaxResults(1)
        .getResultList();

    return results.isEmpty() ? null : results.get(0);
  }
  
  public List<Donation> findLastDonationsByDonorVenueAndDonationDate(Location donorVenue, Date startDate, Date endDate) {
    return entityManager.createNamedQuery(DonationNamedQueryConstants.NAME_FIND_LAST_DONATIONS_BY_DONOR_VENUE_AND_DONATION_DATE,
        Donation.class)
        .setParameter("venue", donorVenue)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("deleted", false)
        .getResultList();
  }
  
  public List<DonationExportDTO> findDonationsForExport() {
    return entityManager.createNamedQuery(DonationNamedQueryConstants.NAME_FIND_DONATIONS_FOR_EXPORT, DonationExportDTO.class)
        .setParameter("deleted", false)
        .getResultList();
  }

  public List<Donation> findDonationsBetweenTwoDins(String fromDIN, String toDIN){
    return entityManager.createNamedQuery(DonationNamedQueryConstants.NAME_FIND_DONATIONS_BETWEEN_TWO_DINS, Donation.class)
        .setParameter("fromDIN", fromDIN)
        .setParameter("toDIN", toDIN)
        .setParameter("deleted", false)
        .getResultList();
  }

  public List<Donation> findInRange(Date startDate, Date endDate) {
    return findByVenueAndPackTypeInRange(null, null, startDate, endDate);
  }

  public List<Donation> findByVenueAndPackTypeInRange(UUID venueId, UUID packTypeId, Date startDate, Date endDate) {
    if (venueId == null && packTypeId == null) {
      return entityManager.createNamedQuery(DonationNamedQueryConstants.NAME_FIND_IN_RANGE, Donation.class)
          .setParameter("startDate", startDate)
          .setParameter("endDate", endDate)
          .setParameter("deleted", false)
          .setParameter("testSampleProduced", true)
          .getResultList();
    } else if (packTypeId == null) {
      return entityManager.createNamedQuery(DonationNamedQueryConstants.NAME_FIND_BY_VENUE_ID_IN_RANGE, Donation.class)
          .setParameter("venueId", venueId)
          .setParameter("startDate", startDate)
          .setParameter("endDate", endDate)
          .setParameter("deleted", false)
          .setParameter("testSampleProduced", true)
          .getResultList();
    } else if (venueId == null) {
      return entityManager.createNamedQuery(DonationNamedQueryConstants.NAME_FIND_BY_PACK_TYPE_ID_IN_RANGE,
          Donation.class)
          .setParameter("packTypeId", packTypeId)
          .setParameter("startDate", startDate)
          .setParameter("endDate", endDate)
          .setParameter("deleted", false)
          .setParameter("testSampleProduced", true)
          .getResultList();
    } else {
      return entityManager.createNamedQuery(DonationNamedQueryConstants.NAME_FIND_BY_VENUE_ID_AND_PACK_TYPE_ID_IN_RANGE,
          Donation.class)
          .setParameter("venueId", venueId)
          .setParameter("packTypeId", packTypeId)
          .setParameter("startDate", startDate)
          .setParameter("endDate", endDate)
          .setParameter("deleted", false)
          .setParameter("testSampleProduced", true)
          .getResultList();
    }
  }
}
