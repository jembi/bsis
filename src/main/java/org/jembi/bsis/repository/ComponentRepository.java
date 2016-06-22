package org.jembi.bsis.repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.model.util.BloodGroup;
import org.jembi.bsis.repository.bloodtesting.BloodTypingStatus;
import org.jembi.bsis.service.DonationConstraintChecker;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class ComponentRepository {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private DonationConstraintChecker donationConstraintChecker;

  /**
   * some fields like component status are cached internally. must be called whenever any changes
   * are made to rows related to the component. eg. Test result update should update the component
   * status.
   */
  public boolean updateComponentInternalFields(Component component) {
    return updateComponentStatus(component);
  }

  /*
   * FIXME: This method needs comprehensive tests and the allowed status changes should be documented. It would be
   * best to write tests for the expected behaviours and check that the method handles those rather than basing the
   * tests on what is currently implemented.
   */
  private boolean updateComponentStatus(Component component) {

    // if a component has been explicitly discarded maintain that status.
    // if the component has been issued do not change its status.
    // suppose a component from a donation tested as safe was issued
    // then some additional tests were done for some reason and it was
    // discovered that the component was actually unsafe and it should not
    // have been issued then it should be easy to track down all components
    // created from that sample which were issued. By maintaining the status as
    // issued even if the component is unsafe we can search for all components created
    // from that donation and then look at which ones were already issued.
    // Conclusion is do not change the component status once it is marked as issued.
    // Similar reasoning for not changing USED status for a component. It should be
    // easy to track which used components were made from unsafe donations.
    // of course if the test results are not available or the donation is known
    // to be unsafe it should not have been issued in the first place.
    // In exceptional cases an admin can always delete this component and create a new one
    // if he wants to change the status to a new one.
    // once a component has been labeled as split it does not exist anymore so we just mark
    // it as SPLIT/PROCESSED. Even if the donation is found to be unsafe later it should not matter
    // as SPLIT/PROCESSED components are not allowed to be issued
    List<ComponentStatus> statusNotToBeChanged =
        Arrays.asList(ComponentStatus.DISCARDED, ComponentStatus.ISSUED,
            ComponentStatus.USED, ComponentStatus.SPLIT, ComponentStatus.PROCESSED);

    ComponentStatus oldComponentStatus = component.getStatus();

    // nothing to do if the component has any of these statuses
    if (component.getStatus() != null && statusNotToBeChanged.contains(component.getStatus()))
      return false;

    if (component.getDonation() == null)
      return false;
    Long donationId = component.getDonation().getId();
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTypingStatus bloodTypingStatus = donation.getBloodTypingStatus();

    TestBatch testBatch = donation.getDonationBatch().getTestBatch();
    boolean donationReleased = testBatch != null &&
        testBatch.getStatus() != TestBatchStatus.OPEN &&
        !donationConstraintChecker.donationHasDiscrepancies(donation);
    // If the donation has not been released yet, then don't use its TTI status
    TTIStatus ttiStatus = donationReleased ? donation.getTTIStatus() : TTIStatus.NOT_DONE;

    // Start with the old status if there is one.
    ComponentStatus newComponentStatus = oldComponentStatus == null ? ComponentStatus.QUARANTINED : oldComponentStatus;

    if (bloodTypingStatus.equals(BloodTypingStatus.COMPLETE) &&
        ttiStatus.equals(TTIStatus.TTI_SAFE) &&
        oldComponentStatus != ComponentStatus.UNSAFE) {
      newComponentStatus = ComponentStatus.AVAILABLE;
    }

    // just mark it as expired or unsafe
    // note that expired or unsafe status should override
    // available, quarantined status hence this check is done
    // later in the code
    if (component.getExpiresOn().before(new Date())) {
      newComponentStatus = ComponentStatus.EXPIRED;
    }

    if (ttiStatus.equals(TTIStatus.TTI_UNSAFE)) {
      newComponentStatus = ComponentStatus.UNSAFE;
    }

    if (!newComponentStatus.equals(oldComponentStatus)) {
      component.setStatus(newComponentStatus);
      return true;
    }
    return false;
  }

  public List<Component> findAnyComponent(String donationIdentificationNumber, List<Long> componentTypes, List<ComponentStatus> status,
      Date donationDateFrom, Date donationDateTo) {
    TypedQuery<Component> query;
    String queryStr = "SELECT DISTINCT c FROM Component c LEFT JOIN FETCH c.donation WHERE " +
        "c.isDeleted= :isDeleted ";

    if (status != null && !status.isEmpty()) {
      queryStr += "AND c.status IN :status ";
    }
    if (!StringUtils.isBlank(donationIdentificationNumber)) {
      queryStr += "AND c.donation.donationIdentificationNumber = :donationIdentificationNumber ";
    }
    if (componentTypes != null && !componentTypes.isEmpty()) {
      queryStr += "AND c.componentType.id IN (:componentTypeIds) ";
    }
    if (donationDateFrom != null) {
      queryStr += "AND c.donation.donationDate >= :donationDateFrom ";
    }
    if (donationDateTo != null) {
      queryStr += "AND c.donation.donationDate <= :donationDateTo ";
    }

    queryStr += " ORDER BY c.id ASC";

    query = em.createQuery(queryStr, Component.class);
    query.setParameter("isDeleted", Boolean.FALSE);

    if (status != null && !status.isEmpty()) {
      query.setParameter("status", status);
    }
    if (!StringUtils.isBlank(donationIdentificationNumber)) {
      query.setParameter("donationIdentificationNumber", donationIdentificationNumber);
    }
    if (componentTypes != null && !componentTypes.isEmpty()) {
      query.setParameter("componentTypeIds", componentTypes);
    }
    if (donationDateFrom != null) {
      query.setParameter("donationDateFrom", donationDateFrom);
    }
    if (donationDateTo != null) {
      query.setParameter("donationDateTo", donationDateTo);
    }

    return query.getResultList();
  }

  public List<Component> findComponentsByDonationIdentificationNumber(String donationIdentificationNumber) {
    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_COMPONENTS_BY_DIN, Component.class)
        .setParameter("isDeleted", Boolean.FALSE)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .getResultList();
  }

  public List<Component> getAllUnissuedComponents() {
    String queryString = "SELECT c FROM Component c where c.isDeleted = :isDeleted and c.isIssued= :isIssued";
    TypedQuery<Component> query = em.createQuery(queryString, Component.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("isIssued", Boolean.FALSE);
    return query.getResultList();
  }

  public List<Component> getAllUnissuedThirtyFiveDayComponents() {
    String queryString = "SELECT c FROM Component c where c.isDeleted = :isDeleted and c.isIssued= :isIssued and c.createdOn > :minDate";
    TypedQuery<Component> query = em.createQuery(queryString, Component.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("isIssued", Boolean.FALSE);
    query.setParameter("minDate", new DateTime(new Date()).minusDays(35)
        .toDate());
    return query.getResultList();
  }

  public List<Component> getAllComponents() {
    String queryString = "SELECT c FROM Component c where c.isDeleted = :isDeleted";
    TypedQuery<Component> query = em.createQuery(queryString, Component.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.getResultList();
  }

  public Component findComponent(Long componentId) {
    return em.find(Component.class, componentId);
  }

  public Component findComponentById(Long componentId) throws NoResultException {
    String queryString = "SELECT c FROM Component c LEFT JOIN FETCH c.donation where c.id = :componentId AND c.isDeleted = :isDeleted";
    TypedQuery<Component> query = em.createQuery(queryString, Component.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("componentId", componentId);
    Component component = null;
    component = query.getSingleResult();
    return component;
  }

  public Component updateComponent(Component component) {
    Component existingComponent = findComponentById(component.getId());
    if (existingComponent == null) {
      return null;
    }
    existingComponent.copy(component);
    updateComponentInternalFields(existingComponent);
    em.merge(existingComponent);
    em.flush();
    return existingComponent;
  }

  public Component addComponent(Component component) {
    updateComponentInternalFields(component);
    em.persist(component);
    //em.flush();
    em.refresh(component);
    return component;
  }

  public void updateQuarantineStatus() {
    String queryString = "SELECT c FROM Component c LEFT JOIN FETCH c.donation where c.status is NULL AND c.isDeleted = :isDeleted";
    TypedQuery<Component> query = em.createQuery(queryString, Component.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<Component> components = query.getResultList();
    //System.out.println("number of components to update: " + components.size());
    for (Component component : components) {
      updateComponentInternalFields(component);
      em.merge(component);
    }
    em.flush();
  }

  public void updateExpiryStatus() {
    String updateExpiryQuery = "UPDATE Component c SET c.status=:status WHERE " +
        "c.status=:availableStatus AND " +
        "c.expiresOn < :today";
    Query query = em.createQuery(updateExpiryQuery);
    query.setParameter("status", ComponentStatus.EXPIRED);
    query.setParameter("availableStatus", ComponentStatus.AVAILABLE);
    query.setParameter("today", new Date());
    query.executeUpdate();
  }

  public Map<String, Map<Long, Long>> findNumberOfDiscardedComponents(
      Date donationDateFrom, Date donationDateTo, String aggregationCriteria,
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
        "SELECT count(c), c.donation.donationDate, c.donation.bloodAbo, " +
            "c.donation.bloodRh FROM Component c WHERE " +
            "c.donation.venue.id IN (:venueIds) AND " +
            "c.donation.donationDate BETWEEN :donationDateFrom AND :donationDateTo AND " +
            "c.status IN (:discardedStatuses) AND " +
            "(c.isDeleted= :isDeleted) " +
            "GROUP BY bloodAbo, bloodRh, donationDate", Object[].class);

    query.setParameter("venueIds", venueIds);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("discardedStatuses",
        Arrays.asList(ComponentStatus.DISCARDED,
            ComponentStatus.UNSAFE,
            ComponentStatus.EXPIRED));

    query.setParameter("donationDateFrom", donationDateFrom);
    query.setParameter("donationDateTo", donationDateTo);

    DateFormat resultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    int incrementBy = Calendar.DAY_OF_YEAR;
    if (aggregationCriteria.equals("monthly")) {
      incrementBy = Calendar.MONTH;
      resultDateFormat = new SimpleDateFormat("01/MM/yyyy");
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

      Date formattedDate = resultDateFormat.parse(resultDateFormat.format(d));
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

  public Map<String, Map<Long, Long>> findNumberOfIssuedComponents(
      Date donationDateFrom, Date donationDateTo, String aggregationCriteria,
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
        "SELECT count(c), c.issuedOn, c.donation.bloodAbo, " +
            "c.donation.bloodRh FROM Component c WHERE " +
            "c.donation.venue.id IN (:venueIds) AND " +
            "c.donation.donationDate BETWEEN :donationDateFrom AND :donationDateTo AND " +
            "c.status=:issuedStatus AND " +
            "(c.isDeleted= :isDeleted) " +
            "GROUP BY bloodAbo, bloodRh, donationDate", Object[].class);

    query.setParameter("venueIds", venueIds);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("issuedStatus", ComponentStatus.ISSUED);

    query.setParameter("donationDateFrom", donationDateFrom);
    query.setParameter("donationDateTo", donationDateTo);

    DateFormat resultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    int incrementBy = Calendar.DAY_OF_YEAR;
    if (aggregationCriteria.equals("monthly")) {
      incrementBy = Calendar.MONTH;
      resultDateFormat = new SimpleDateFormat("01/MM/yyyy");
    } else if (aggregationCriteria.equals("yearly")) {
      incrementBy = Calendar.YEAR;
      resultDateFormat = new SimpleDateFormat("01/01/yyyy");
    }

    List<Object[]> resultList = query.getResultList();

    for (String bloodGroup : bloodGroups) {
      Map<Long, Long> m = new HashMap<Long, Long>();
      Calendar gcal = new GregorianCalendar();
      Date lowerDate = null;
      Date upperDate = null;
      try {
        lowerDate = resultDateFormat.parse(resultDateFormat.format(donationDateFrom));
        upperDate = resultDateFormat.parse(resultDateFormat.format(donationDateTo));
      } catch (ParseException e1) {
        e1.printStackTrace();
      }
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
      Date formattedDate = resultDateFormat.parse(resultDateFormat.format(d));
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

  public List<Component> findComponentsByDINAndType(String donationIdentificationNumber, long componentTypeId) {
    String queryStr = "SELECT c from Component c WHERE " +
        "c.donation.donationIdentificationNumber = :donationIdentificationNumber AND " +
        "c.componentType.id = :componentTypeId";
    TypedQuery<Component> query = em.createQuery(queryStr, Component.class);
    query.setParameter("donationIdentificationNumber", donationIdentificationNumber);
    query.setParameter("componentTypeId", componentTypeId);
    return query.getResultList();
  }

  // TODO: Test
  public int countChangedComponentsForDonation(long donationId) {
    return em.createNamedQuery(
        ComponentNamedQueryConstants.NAME_COUNT_CHANGED_COMPONENTS_FOR_DONATION,
        Number.class)
        .setParameter("donationId", donationId)
        .setParameter("deleted", false)
        .setParameter("initialStatus", ComponentStatus.QUARANTINED)
        .getSingleResult()
        .intValue();
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void updateComponentStatusesForDonor(List<ComponentStatus> oldStatuses, ComponentStatus newStatus,
                                              Donor donor) {

    em.createNamedQuery(ComponentNamedQueryConstants.NAME_UPDATE_COMPONENT_STATUSES_FOR_DONOR)
        .setParameter("oldStatuses", oldStatuses)
        .setParameter("newStatus", newStatus)
        .setParameter("donor", donor)
        .executeUpdate();
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void updateComponentStatusForDonation(List<ComponentStatus> oldStatuses, ComponentStatus newStatus,
                                               Donation donation) {

    em.createNamedQuery(ComponentNamedQueryConstants.NAME_UPDATE_COMPONENT_STATUSES_FOR_DONATION)
        .setParameter("oldStatuses", oldStatuses)
        .setParameter("newStatus", newStatus)
        .setParameter("donation", donation)
        .executeUpdate();
  }
  
  public Component findComponentByCodeAndDIN(String componentCode, String donationIdentificationNumber) {
    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_COMPONENT_BY_CODE_AND_DIN, Component.class)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .setParameter("componentCode", componentCode)
        .getSingleResult();
  }

  public boolean verifyComponentExists(Long id) {
    Long count = em.createNamedQuery(ComponentNamedQueryConstants.NAME_COUNT_COMPONENT_WITH_ID, Long.class)
        .setParameter("id", id).getSingleResult();
    if (count == 1) {
      return true;
    }
    return false;
  }
}
