package repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import constant.GeneralConfigConstants;
import dto.CollectedDonationDTO;
import model.bloodtesting.TTIStatus;
import model.component.Component;
import model.component.ComponentStatus;
import model.componentbatch.ComponentBatch;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.donor.Donor;
import model.packtype.PackType;
import model.util.BloodGroup;
import repository.bloodtesting.BloodTestingRepository;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.bloodtesting.BloodTypingStatus;
import service.GeneralConfigAccessorService;
import viewmodel.BloodTestingRuleResult;

@Repository
@Transactional
public class DonationRepository {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private BloodTestingRepository bloodTypingRepository;

  @Autowired
  private GeneralConfigAccessorService generalConfigAccessorService;

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

  public List<Object> findDonations(
      String donationIdentificationNumber, List<Long> packTypeIds, List<Long> venueIds, String donationDateFrom,
      String donationDateTo, boolean includeTestedDonations, Map<String, Object> pagingParams) throws ParseException {

    String queryStr = "";
    if (StringUtils.isNotBlank(donationIdentificationNumber)) {
      queryStr = "SELECT c FROM Donation c LEFT JOIN FETCH c.donor WHERE " +
          "c.donationIdentificationNumber = :donationIdentificationNumber AND " +
          "c.packType.id IN :packTypeIds AND " +
          "c.venue.id IN :venueIds AND " +
          "c.donationDate >= :donationDateFrom AND c.donationDate <= :donationDateTo AND " +
          "c.isDeleted=:isDeleted";
    } else {
      queryStr = "SELECT c FROM Donation c LEFT JOIN FETCH c.donor WHERE " +
          "c.packType.id IN :packTypeIds AND " +
          "c.venue.id IN :venueIds AND " +
          "c.donationDate >= :donationDateFrom AND c.donationDate <= :donationDateTo AND " +
          "c.isDeleted=:isDeleted";
    }

    if (!includeTestedDonations)
      queryStr = queryStr + " AND (c.bloodTypingStatus=:bloodTypingStatus OR c.ttiStatus=:ttiStatus)";

    TypedQuery<Donation> query;
    if (pagingParams.containsKey("sortColumn")) {
      queryStr += " ORDER BY c." + pagingParams.get("sortColumn") + " " + pagingParams.get("sortDirection");
    }

    query = em.createQuery(queryStr, Donation.class);
    query.setParameter("isDeleted", Boolean.FALSE);

    if (StringUtils.isNotBlank(donationIdentificationNumber))
      query.setParameter("donationIdentificationNumber", donationIdentificationNumber);

    if (!includeTestedDonations) {
      query.setParameter("bloodTypingStatus", BloodTypingStatus.NOT_DONE);
      query.setParameter("ttiStatus", TTIStatus.NOT_DONE);
    }

    query.setParameter("packTypeIds", packTypeIds);
    query.setParameter("venueIds", venueIds);
    query.setParameter("donationDateFrom", getDonationDateFromOrDefault(donationDateFrom));
    query.setParameter("donationDateTo", getDonationDateToOrDefault(donationDateTo));


    int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
    int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);

    query.setFirstResult(start);
    query.setMaxResults(length);

    return Arrays.asList(query.getResultList(), getResultCount(queryStr, query));
  }

  private Long getResultCount(String queryStr, Query query) {
    String countQueryStr = queryStr.replaceFirst("SELECT c", "SELECT COUNT(c)");
    // removing the join fetch is important otherwise Hibernate will complain
    // owner of the fetched association was not present in the select list
    countQueryStr = countQueryStr.replaceFirst("LEFT JOIN FETCH c.donor", "");
    TypedQuery<Long> countQuery = em.createQuery(countQueryStr, Long.class);
    for (Parameter<?> parameter : query.getParameters()) {
      countQuery.setParameter(parameter.getName(), query.getParameterValue(parameter));
    }
    return countQuery.getSingleResult().longValue();
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

  private Date getDonationDateFromOrDefault(String donationDateFrom) throws ParseException {
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date from =
        (donationDateFrom == null || donationDateFrom.equals("")) ? dateFormat
            .parse("31/12/1970") : dateFormat.parse(donationDateFrom);


    return from;
  }

  private Date getDonationDateToOrDefault(String donationDateTo) throws ParseException {
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date to = null;
    to = (donationDateTo == null || donationDateTo.equals("")) ? new Date() :
        dateFormat.parse(donationDateTo);

    return to;
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

  public Donation addDonation(Donation donation) throws PersistenceException {
    donation.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    donation.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    donation.setTTIStatus(TTIStatus.NOT_DONE);
    donation.setIsDeleted(false);

    em.persist(donation);
    em.flush();
    em.refresh(donation);
    updateDonorFields(donation);
    em.flush();
    em.refresh(donation.getDonor());

    // Create initial component only if the countAsDonation is true and the config option is enabled
    if (donation.getPackType().getCountAsDonation() &&
        generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.CREATE_INITIAL_COMPONENTS)) {
      createInitialComponent(donation);
    }

    return donation;
  }

  private void createInitialComponent(Donation donation) {

    ComponentType componentType = donation.getPackType().getComponentType();

    Component component = new Component();
    component.setIsDeleted(false);
    component.setComponentIdentificationNumber(componentType.getComponentTypeCode());
    component.setDonation(donation);
    component.setStatus(ComponentStatus.QUARANTINED);
    component.setCreatedDate(donation.getCreatedDate());

    // set new component creation date to match donation date 
    component.setCreatedOn(donation.getDonationDate());
    // if bleed time is provided, update component creation time to match bleed start time 
    if (donation.getBleedStartTime() != null) {
      Calendar donationDate = Calendar.getInstance();
      donationDate.setTime(donation.getDonationDate());
      Calendar bleedTime = Calendar.getInstance();
      bleedTime.setTime(donation.getBleedStartTime());
      donationDate.set(Calendar.HOUR_OF_DAY, bleedTime.get(Calendar.HOUR_OF_DAY));
      donationDate.set(Calendar.MINUTE, bleedTime.get(Calendar.MINUTE));
      donationDate.set(Calendar.SECOND, bleedTime.get(Calendar.SECOND));
      donationDate.set(Calendar.MILLISECOND, bleedTime.get(Calendar.MILLISECOND));
      component.setCreatedOn(donationDate.getTime());
    }
    component.setCreatedBy(donation.getCreatedBy());

    // set cal to donationDate Date 
    Calendar cal = Calendar.getInstance();
    cal.setTime(donation.getDonationDate());

    // second calendar to store bleedStartTime 
    Calendar bleedStartTime = Calendar.getInstance();
    bleedStartTime.setTime(donation.getBleedStartTime());

    // update cal to set time to bleedStartTime
    cal.set(Calendar.HOUR_OF_DAY, bleedStartTime.get(Calendar.HOUR_OF_DAY));
    cal.set(Calendar.MINUTE, bleedStartTime.get(Calendar.MINUTE));
    cal.set(Calendar.SECOND, bleedStartTime.get(Calendar.SECOND));

    // update cal with initial component expiry period
    cal.add(Calendar.DATE, componentType.getExpiresAfter());
    Date expiresOn = cal.getTime();

    ComponentBatch componentBatch = donation.getDonationBatch().getComponentBatch();
    if (componentBatch == null) {
      // Set the location to the venue of the donation batch
      component.setLocation(donation.getDonationBatch().getVenue());
    } else {
      // Assign the component to the component batch
      component.setComponentBatch(componentBatch);
      // Set the location to the processing site of the component batch
      component.setLocation(componentBatch.getLocation());
    }

    component.setExpiresOn(expiresOn);
    component.setComponentType(componentType);
    em.persist(component);
    em.refresh(component);

  }

  private void updateDonorFields(Donation donation) {
    Donor donor = donation.getDonor();

    //set date of first donation
    if (donation.getDonor().getDateOfFirstDonation() == null) {
      donor.setDateOfFirstDonation(donation.getDonationDate());
    }
    //set dueToDonate
    PackType packType = donation.getPackType();
    int periodBetweenDays = packType.getPeriodBetweenDonations();
    Calendar dueToDonateDate = Calendar.getInstance();
    dueToDonateDate.setTime(donation.getDonationDate());
    dueToDonateDate.add(Calendar.DAY_OF_YEAR, periodBetweenDays);

    if (donor.getDueToDonate() == null || dueToDonateDate.getTime().after(donor.getDueToDonate())) {
      donor.setDueToDonate(dueToDonateDate.getTime());
    }

    // set dateOfLastDonation
    Date dateOfLastDonation = donor.getDateOfLastDonation();
    if (dateOfLastDonation == null || donation.getDonationDate().after(dateOfLastDonation)) {
      donor.setDateOfLastDonation(donation.getDonationDate());
    }
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
        statusMap.put(c.getId(), bloodTypingRepository.getAllTestsStatusForDonation(c.getId()));
      }
    }
    return statusMap;
  }

}
