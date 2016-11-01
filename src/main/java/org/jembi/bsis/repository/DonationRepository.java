package org.jembi.bsis.repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.dto.CollectedDonationDTO;
import org.jembi.bsis.dto.DonationExportDTO;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.BloodGroup;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTypingStatus;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonationRepository {

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

  public Donation findDonationById(Long donationId) throws NoResultException {
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

  public List<Donation> findAnyDonationMatching(String donationIdentificationNumber,
                                                String sampleNumber, String shippingNumber, String donationDateFrom,
                                                String donationDateTo, List<String> centers) {

    TypedQuery<Donation> query = em.createQuery(
        "SELECT c FROM Donation c JOIN c.center center WHERE "
            + "(c.donationIdentificationNumber = :donationIdentificationNumber OR "
            + "c.sampleNumber = :sampleNumber OR "
            + "c.shippingNumber = :shippingNumber OR "
            + "center.id IN (:centers)) AND ("
            + "c.donationDate BETWEEN :donationDateFrom AND "
            + ":donationDateTo" + ") AND " + "(c.isDeleted= :isDeleted)",
        Donation.class);

    query.setParameter("isDeleted", Boolean.FALSE);
    String donationNo = ((donationIdentificationNumber == null) ? "" : donationIdentificationNumber);
    query.setParameter("donationIdentificationNumber", donationNo);
    query.setParameter("sampleNumber", sampleNumber);
    query.setParameter("shippingNumber", shippingNumber);

    query.setParameter("centers", centers);

    List<Donation> resultList = query.getResultList();
    return resultList;
  }

  public Map<String, Map<Long, Long>> findNumberOfDonations(Date donationDateFrom,
                                                            Date donationDateTo, String aggregationCriteria,
                                                            List<String> venues, List<String> bloodGroups) throws ParseException {

    List<Long> venueIds = new ArrayList<Long>();
    if (venues != null) {
      for (String venue : venues) {
        venueIds.add(Long.parseLong(venue));
      }
    } else {
      venueIds.add((long) -1);
    }

    Map<String, Map<Long, Long>> resultMap = new HashMap<String, Map<Long, Long>>();
    for (String bloodGroup : bloodGroups) {
      resultMap.put(bloodGroup, new HashMap<Long, Long>());
    }

    TypedQuery<Object[]> query = em.createQuery(
        "SELECT count(c), c.donationDate, c.bloodAbo, c.bloodRh FROM Donation c WHERE " +
            "c.venue.id IN (:venueIds) AND " +
            "c.donationDate BETWEEN :donationDateFrom AND " +
            ":donationDateTo AND (c.isDeleted= :isDeleted) GROUP BY " +
            "bloodAbo, bloodRh, donationDate", Object[].class);

    query.setParameter("venueIds", venueIds);
    query.setParameter("isDeleted", Boolean.FALSE);

    query.setParameter("donationDateFrom", donationDateFrom);
    query.setParameter("donationDateTo", donationDateTo);

    DateFormat resultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    int incrementBy = Calendar.DAY_OF_YEAR;
    if (aggregationCriteria.equals("monthly")) {
      incrementBy = Calendar.MONTH;
      resultDateFormat = new SimpleDateFormat("MM/01/yyyy");
    } else if (aggregationCriteria.equals("yearly")) {
      incrementBy = Calendar.YEAR;
      resultDateFormat = new SimpleDateFormat("01/01/yyyy");
    }

    List<Object[]> resultList = query.getResultList();

    for (String bloodGroup : bloodGroups) {
      Map<Long, Long> m = new HashMap<Long, Long>();
      Calendar gcal = new GregorianCalendar();
      Date lowerDate = resultDateFormat.parse(resultDateFormat.format(donationDateFrom));
      Date upperDate = resultDateFormat.parse(resultDateFormat.format(donationDateTo));
      gcal.setTime(lowerDate);
      while (gcal.getTime().before(upperDate) || gcal.getTime().equals(upperDate)) {
        m.put(gcal.getTime().getTime(), (long) 0);
        gcal.add(incrementBy, 1);
      }
      resultMap.put(bloodGroup, m);
    }

    for (Object[] result : resultList) {
      Date d = (Date) result[1];
      String bloodAbo = (String) result[2];
      String bloodRh = (String) result[3];
      BloodGroup bloodGroup = new BloodGroup(bloodAbo, bloodRh);
      Map<Long, Long> m = resultMap.get(bloodGroup.toString());
      if (m == null)
        continue;
      Date formattedDate = null;
      formattedDate = resultDateFormat.parse(resultDateFormat.format(d));
      Long utcTime = formattedDate.getTime();
      if (m.containsKey(utcTime)) {
        Long newVal = m.get(utcTime) + (Long) result[0];
        m.put(utcTime, newVal);
      } else {
        m.put(utcTime, (Long) result[0]);
      }

    }

    return resultMap;
  }

  public Donation findDonationByDonationIdentificationNumber(
      String donationIdentificationNumber) throws NoResultException, NonUniqueResultException {
    String queryString = "SELECT c FROM Donation c LEFT JOIN FETCH c.donor WHERE c.donationIdentificationNumber = :donationIdentificationNumber and c.isDeleted = :isDeleted";
    TypedQuery<Donation> query = em.createQuery(queryString, Donation.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("donationIdentificationNumber", donationIdentificationNumber);
    Donation c = null;
    c = query.getSingleResult();
    return c;
  }

  public Donation findDonationByDonationIdentificationNumberIncludeDeleted(
      String donationIdentificationNumber) {
    String queryString = "SELECT c FROM Donation c WHERE c.donationIdentificationNumber = :donationIdentificationNumber";
    TypedQuery<Donation> query = em.createQuery(queryString, Donation.class);
    query.setParameter("donationIdentificationNumber", donationIdentificationNumber);
    Donation c = null;
    try {
      c = query.getSingleResult();
    } catch (Exception ex) {
    }
    return c;
  }

  public Donation verifyDonationIdentificationNumber(String donationIdentificationNumber) {
    Donation donation = new Donation();
    donation.setDonationIdentificationNumber(donationIdentificationNumber);
    donation = findDonationByDonationIdentificationNumber(donationIdentificationNumber);
    if (donation != null) {
      return donation;
    } else {
      return null;
    }
  }

  public List<Donation> verifyDonationIdentificationNumbers(List<String> donationIdentificationNumbers) {
    List<Donation> donations = new ArrayList<Donation>();
    for (String donationIdentificationNumber : donationIdentificationNumbers) {
      if (StringUtils.isBlank(donationIdentificationNumber))
        continue;
      Donation donation = new Donation();
      donation.setDonationIdentificationNumber(donationIdentificationNumber);
      donation = findDonationByDonationIdentificationNumber(donationIdentificationNumber);
      if (donation != null) {
        donations.add(donation);
      } else {
        donations.add(null);
      }
    }
    return donations;
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

  public Date findDateOfFirstDonationForDonor(long donorId) {
    List<Date> results = em.createNamedQuery(
        DonationNamedQueryConstants.NAME_FIND_ASCENDING_DONATION_DATES_FOR_DONOR,
        Date.class)
        .setParameter("donorId", donorId)
        .setParameter("deleted", false)
        .setMaxResults(1)
        .getResultList();

    return results.isEmpty() ? null : results.get(0);
  }

  public Date findDateOfLastDonationForDonor(long donorId) {
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

  public Date findLatestDueToDonateDateForDonor(long donorId) {

    List<Date> results = em.createNamedQuery(
        DonationNamedQueryConstants.NAME_FIND_LATEST_DUE_TO_DONATE_DATE_FOR_DONOR,
        Date.class)
        .setParameter("donorId", donorId)
        .setParameter("deleted", false)
        .setMaxResults(1)
        .getResultList();

    return results.isEmpty() ? null : results.get(0);
  }

  public Map<Long, BloodTestingRuleResult> filterDonationsWithBloodTypingResults(
      Collection<Donation> donations) {
    Iterator<Donation> iter = donations.iterator();
    Map<Long, BloodTestingRuleResult> statusMap = new HashMap<Long, BloodTestingRuleResult>();
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

}
