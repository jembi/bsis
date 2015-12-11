package repository;

import controller.UtilController;
import model.component.Component;
import model.component.ComponentStatus;
import model.componentmovement.ComponentStatusChange;
import model.componentmovement.ComponentStatusChangeType;
import model.request.Request;
import model.util.BloodGroup;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
@Transactional
public class RequestRepository {

  private static final int ID_LENGTH = 12;

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private ComponentRepository componentRepository;

  @Autowired
  private UtilController utilController;

  public static String generateUniqueRequestNumber() {
    String uniqueRequestNumber;
    uniqueRequestNumber = "R-" + RandomStringUtils.randomNumeric(ID_LENGTH).toUpperCase();
    return uniqueRequestNumber;
  }

  private void saveRequest(Request request) {
    em.persist(request);
    em.flush();
  }

  public Request updateRequest(Request request, String existingRequestNumber) {
    Request existingRequest = findRequest(existingRequestNumber);
    existingRequest.copy(request);
    em.merge(existingRequest);
    em.flush();
    return existingRequest;
  }

  public Request findRequest(String requestNumber) throws NoResultException, NonUniqueResultException {
    Request request = null;
    if (requestNumber != null && requestNumber.length() > 0) {
      String queryString = "SELECT r FROM Request r WHERE r.requestNumber = :requestNumber and r.isDeleted= :isDeleted";
      TypedQuery<Request> query = em.createQuery(queryString, Request.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      query.setParameter("requestNumber", requestNumber);
      request = query.getSingleResult();

    }
    return request;
  }

  public Request findRequestWithIssuedComponents(String requestNumber) throws NoResultException, NonUniqueResultException {
    Request request = null;
    if (requestNumber != null && requestNumber.length() > 0) {
      String queryString = "SELECT r FROM Request r LEFT JOIN FETCH r.issuedComponents WHERE " +
              "r.requestNumber = :requestNumber and r.isDeleted= :isDeleted";
      TypedQuery<Request> query = em.createQuery(queryString, Request.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      query.setParameter("requestNumber", requestNumber);
      request = query.getSingleResult();
    }
    return request;
  }

  public Request findRequestById(Long requestId) throws NoResultException, NonUniqueResultException {
    String queryString = "SELECT DISTINCT r FROM Request r LEFT JOIN FETCH r.issuedComponents WHERE " +
            "r.id = :requestId and r.isDeleted= :isDeleted";
    TypedQuery<Request> query = em.createQuery(queryString, Request.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    Request request = query.setParameter("requestId", requestId)
            .getSingleResult();
    return request;
  }

  public ArrayList<Request> getAllRequests() {
    String queryString = "SELECT DISTINCT r FROM Request r LEFT JOIN FETCH r.issuedComponents WHERE " +
            "r.isDeleted = :isDeleted order by r.dateRequested";
    TypedQuery<Request> query = em.createQuery(queryString, Request.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return new ArrayList<>(query.getResultList());
  }

  public void deleteAllRequests() {
    Query query = em.createQuery("DELETE FROM Request p");
    query.executeUpdate();
  }

  public List<Request> getRequests(Date fromDateRequested, Date toDateRequested) {
    TypedQuery<Request> query = em
            .createQuery(
                    "SELECT r FROM Request r WHERE  r.dateRequested >= :fromDate and r.dateRequested<= :toDate and r.isDeleted = :isDeleted",
                    Request.class);
    query.setParameter("fromDate", fromDateRequested);
    query.setParameter("toDate", toDateRequested);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<Request> requests = query.getResultList();
    if (CollectionUtils.isEmpty(requests)) {
      return new ArrayList<>();
    }
    return requests;
  }

  public ArrayList<Request> getAllUnfulfilledRequests() {
    String queryString = "SELECT p FROM Request p where p.status = 'pending' or p.status='partiallyFulfilled' and p.isDeleted = :isDeleted order by p.dateRequested";
    TypedQuery<Request> query = em.createQuery(queryString, Request.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return new ArrayList<>(query.getResultList());
  }

  public List<Request> findAnyRequestMatching(String requestNumber,
                                              String dateRequestedFrom, String dateRequestedTo,
                                              String dateRequiredFrom, String dateRequiredTo, List<String> sites,
                                              List<String> componentTypes, List<String> statuses) throws ParseException {

    TypedQuery<Request> query = em
            .createQuery(
                    "SELECT r FROM Request r, Location L WHERE "
                            + "(L.locationId=r.siteId AND L.isVenue=TRUE) AND "
                            + "(r.requestNumber = :requestNumber OR L.name IN (:sites) OR "
                            + "r.componentType IN (:componentTypes)) AND (r.status IN (:statuses)) AND "
                            + "((r.dateRequested BETWEEN :dateRequestedFrom AND "
                            + ":dateRequestedTo) AND (r.dateRequired BETWEEN "
                            + ":dateRequiredFrom AND " + ":dateRequiredTo)) AND "
                            + "(r.isDeleted= :isDeleted)", Request.class);

    query.setParameter("isDeleted", Boolean.FALSE);

    query.setParameter("requestNumber", requestNumber == null ? ""
            : requestNumber);
    query.setParameter("sites", sites);
    query.setParameter("componentTypes", componentTypes);
    query.setParameter("statuses", statuses);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date from = (dateRequestedFrom == null || dateRequestedFrom.equals("")) ? dateFormat
            .parse("31/12/1970") : dateFormat.parse(dateRequestedFrom);
    query.setParameter("dateRequestedFrom", from);
    Date to = (dateRequestedTo == null || dateRequestedTo.equals("")) ? dateFormat
            .parse(dateFormat.format(new Date())) : dateFormat
            .parse(dateRequestedTo);
    query.setParameter("dateRequestedTo", to);

/*
      //dupliate code
      Date from = (dateRequiredFrom == null || dateRequiredFrom.equals("")) ? dateFormat
          .parse("31/12/1970") : dateFormat.parse(dateRequiredFrom);
      query.setParameter("dateRequiredFrom", from);
      Date to = (dateRequiredTo == null || dateRequiredTo.equals("")) ? dateFormat
          .parse(dateFormat.format(new Date())) : dateFormat
          .parse(dateRequiredTo);
      query.setParameter("dateRequiredTo", to);
*/

    List<Request> resultList = query.getResultList();
    return resultList;
  }

  public Request findRequestByRequestNumber(String requestNumber) throws NoResultException, NonUniqueResultException {
    TypedQuery<Request> query = em
            .createQuery(
                    "SELECT r FROM Request r WHERE r.requestNumber = :requestNumber and r.isDeleted= :isDeleted",
                    Request.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("requestNumber", requestNumber);
    Request request = null;
    request = query.getSingleResult();
    return request;
  }

  public Request findRequestByRequestNumberIncludeDeleted(String requestNumber) throws NoResultException, NonUniqueResultException {
    TypedQuery<Request> query = em
            .createQuery(
                    "SELECT r FROM Request r WHERE r.requestNumber = :requestNumber",
                    Request.class);
    query.setParameter("requestNumber", requestNumber);
    Request request = null;
    request = query.getSingleResult();
    return request;
  }

  public Request updateOrAddRequest(Request request) {
    Request existingRequest = findRequestByRequestNumber(request
            .getRequestNumber());
    if (existingRequest == null) {
      request.setIsDeleted(false);
      saveRequest(request);
      return request;
    }
    existingRequest.copy(request);
    existingRequest.setIsDeleted(false);
    em.merge(existingRequest);
    em.flush();
    return existingRequest;
  }

  public List<Request> findRequestsNotFulfilled() {
    TypedQuery<Request> query = em.createQuery(
            "SELECT r FROM Request r, Location L WHERE "
                    + "(L.locationId=r.siteId AND L.isVenue=TRUE) AND"
                    + "(r.status NOT IN (:statuses)) AND "
                    + "(r.isDeleted= :isDeleted)", Request.class);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("statuses", Collections.singletonList("fulfilled"));
    List<Request> resultList = query.getResultList();
    return resultList;
  }

  public void deleteRequest(String requestNumber) {
    Request existingRequest = findRequestByRequestNumber(requestNumber);
    existingRequest.setIsDeleted(Boolean.TRUE);
    em.merge(existingRequest);
    em.flush();
  }

  public Request addRequest(Request componentRequest) {
    updateNewRequestFields(componentRequest);
    em.persist(componentRequest);
    em.flush();
    em.refresh(componentRequest);
    return componentRequest;
  }

  private Date getDateRequestedAfterOrDefault(String requestedAfter) throws ParseException {
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date from = null;
    from = (requestedAfter == null || requestedAfter.equals("")) ? dateFormat
            .parse("31/12/1970") : dateFormat.parse(requestedAfter);
    return from;
  }

  private Date getDateRequiredByOrDefault(String dateRequiredBy) throws ParseException {
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date to = null;
    if (StringUtils.isBlank(dateRequiredBy)) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      cal.add(Calendar.DATE, 365);
      to = cal.getTime();
    } else {
      to = dateFormat.parse(dateRequiredBy);
    }
    return to;
  }

  public List<Object> findRequests(String requestNumber, List<Integer> componentTypeIds,
                                   List<Long> requestSiteIds, String requestedAfter,
                                   String requiredBy, Boolean includeSatisfiedRequests, Map<String, Object> pagingParams) throws ParseException {

    String queryStr = "";
    if (StringUtils.isNotBlank(requestNumber)) {
      queryStr = "SELECT r FROM Request r LEFT JOIN FETCH r.issuedComponents WHERE " +
              "r.requestNumber =:requestNumber AND " +
              "r.isDeleted= :isDeleted";
    } else {
      queryStr = "SELECT r FROM Request r LEFT JOIN FETCH r.issuedComponents WHERE " +
              "(r.componentType.id IN (:componentTypeIds) AND " +
              "r.requestSite.id IN (:requestSiteIds)) AND" +
              "(r.requestDate >= :requestedAfter and r.requiredDate <= :requiredBy) AND " +
              "r.isDeleted= :isDeleted";
      if (!includeSatisfiedRequests)
        queryStr = queryStr + " AND (r.fulfilled = :fulfilled)";
    }


    TypedQuery<Request> query;
    if (pagingParams.containsKey("sortColumn")) {
      queryStr += " ORDER BY r." + pagingParams.get("sortColumn") + " " + pagingParams.get("sortDirection");
    }

    query = em.createQuery(queryStr, Request.class);
    query.setParameter("isDeleted", Boolean.FALSE);

    if (StringUtils.isNotBlank(requestNumber)) {
      query.setParameter("requestNumber", requestNumber);
    } else {
      query.setParameter("componentTypeIds", componentTypeIds);
      query.setParameter("requestSiteIds", requestSiteIds);
      query.setParameter("requestedAfter", getDateRequestedAfterOrDefault(requestedAfter));
      query.setParameter("requiredBy", getDateRequiredByOrDefault(requiredBy));
      if (!includeSatisfiedRequests)
        query.setParameter("fulfilled", Boolean.FALSE);
    }

    int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
    int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);

    query.setFirstResult(start);
    query.setMaxResults(length);

    return Arrays.asList(query.getResultList(), getResultCount(queryStr, query));
  }

  private Long getResultCount(String queryStr, Query query) {
    String countQueryStr = queryStr.replaceFirst("SELECT r", "SELECT COUNT(r)");
    // removing the join fetch is important otherwise Hibernate will complain
    // owner of the fetched association was not present in the select list
    countQueryStr = countQueryStr.replaceFirst("LEFT JOIN FETCH r.issuedComponents", "");
    TypedQuery<Long> countQuery = em.createQuery(countQueryStr, Long.class);
    for (Parameter<?> parameter : query.getParameters()) {
      countQuery.setParameter(parameter.getName(), query.getParameterValue(parameter));
    }
    return countQuery.getSingleResult();
  }

  public void deleteRequest(Long requestId) {
    Request existingRequest = findRequestById(requestId);
    existingRequest.setIsDeleted(Boolean.TRUE);
    em.merge(existingRequest);
    em.flush();
  }

  public Request updateRequest(Request request) {
    Request existingRequest = findRequestById(request.getId());
    if (existingRequest == null) {
      return null;
    }
    existingRequest.copy(request);
    em.merge(existingRequest);
    em.flush();
    return existingRequest;
  }

  public void issueComponentsToRequest(Long requestId, String componentsToIssue) throws RuntimeException {
    Request request = findRequestById(requestId);
    componentsToIssue = componentsToIssue.replaceAll("\"", "");
    componentsToIssue = componentsToIssue.replaceAll("\\[", "");
    componentsToIssue = componentsToIssue.replaceAll("\\]", "");
    String[] componentIds = componentsToIssue.split(",");
    int numUnitsIssued = 0;
    if (request.getNumUnitsIssued() != null)
      numUnitsIssued = request.getNumUnitsIssued();
    for (String componentId : componentIds) {
      Component component = em.find(Component.class, Long.parseLong(componentId));
      // handle the case where the component, test result has been updated
      // between the time when matching components are searched and selected
      // for issuing
      if (!canIssueComponent(component, request))
        throw new RuntimeException("Could not issue components");
    }

    for (String componentId : componentIds) {
      Component component = em.find(Component.class, Long.parseLong(componentId));
      // we know these components can be issued now
      // although there is some doubt about the behavior of locks between the check above and now
      Date today = new Date();
      ComponentStatusChange componentIssue = new ComponentStatusChange();
      componentIssue.setNewStatus(ComponentStatus.ISSUED);
      componentIssue.setStatusChangedOn(today);
      componentIssue.setStatusChangeType(ComponentStatusChangeType.ISSUED);
      componentIssue.setChangedBy(utilController.getCurrentUser());
      componentIssue.setIssuedTo(request);
      componentIssue.setComponent(component);
      numUnitsIssued++;
      component.setStatus(ComponentStatus.ISSUED);
      component.setIssuedOn(today);
      component.setIssuedTo(request);
      em.persist(componentIssue);
      em.merge(component);
    }

    em.flush();
    Integer numUnitsRequested = request.getNumUnitsRequested();
    if (numUnitsRequested != null && numUnitsIssued >= numUnitsRequested) {
      request.setFulfilled(true);
    }
    request.setNumUnitsIssued(numUnitsIssued);
    em.merge(request);
    em.flush();
  }

  private boolean canIssueComponent(Component component, Request request) {
    // first make sure the component is up-to-date
    // the component may have expired so this update is required
    // we update the expiry date of a component periodically
    componentRepository.updateComponentInternalFields(component);
    String requestedComponentType = request.getComponentType().getComponentTypeName();
    String componentType = component.getComponentType().getComponentTypeName();

    if (!componentType.equals(requestedComponentType))
      return false;

    // component available or not
    if (!component.getStatus().equals(ComponentStatus.AVAILABLE))
      return false;

    if (component.getIsDeleted())
      return false;

    Date today = new Date();
    if (component.getExpiresOn().before(today))
      return false;

    String bloodAbo = component.getDonation().getBloodAbo();
    String bloodRh = component.getDonation().getBloodRh();

    boolean canIssue = true;

    String requestedAbo = request.getPatientBloodAbo();
    String requestedRh = request.getPatientBloodRh();
    return canIssue && bloodCrossmatch(bloodAbo, bloodRh, requestedAbo, requestedRh);

  }

  private boolean bloodCrossmatch(String abo1, String rh1, String abo2, String rh2) {
    System.out.println("matching " + abo1 + ", " + ", " + rh1 + ", " + abo2 + ", " + rh2);
    return abo1.equals(abo2) && rh1.equals(rh2) || abo1.equals("O") && (rh1.equals(rh2) || rh1.equals("-"));
  }

  public Request findRequestById(String requestId) {
    Request request = null;
    request = findRequestById(Long.parseLong(requestId));
    return request;
  }

  public List<Component> getIssuedComponentsForRequest(Long requestId) {
    String queryString = "SELECT DISTINCT p FROM Component p LEFT JOIN FETCH p.donation WHERE " +
            "p.issuedTo.id = :requestId AND p.isDeleted= :isDeleted";
    TypedQuery<Component> query = em.createQuery(queryString, Component.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("requestId", requestId);
    List<Component> issuedComponents = query.getResultList();
    return issuedComponents;
  }

  public Map<String, Map<Long, Long>> findNumberOfRequests(Date dateRequestedFrom,
                                                           Date dateRequestedTo, String aggregationCriteria,
                                                           List<String> sites, List<String> bloodGroups) throws ParseException {

    List<Long> siteIds = new ArrayList<>();
    if (sites != null) {
      for (String site : sites) {
        siteIds.add(Long.parseLong(site));
      }
    } else {
      siteIds.add((long) -1);
    }

    Map<String, Map<Long, Long>> resultMap = new HashMap<>();
    for (String bloodGroup : bloodGroups) {
      resultMap.put(bloodGroup, new HashMap<>());
    }

    TypedQuery<Object[]> query = em.createQuery(
            "SELECT count(r), r.requestDate, r.patientBloodAbo, r.patientBloodRh FROM Request r WHERE " +
                    "r.requestSite.id IN (:siteIds) AND " +
                    "r.requestDate BETWEEN :dateRequestedFrom AND " +
                    ":dateRequestedTo AND (r.isDeleted= :isDeleted) GROUP BY " +
                    "patientBloodAbo, patientBloodRh, requestDate", Object[].class);

    query.setParameter("siteIds", siteIds);
    query.setParameter("isDeleted", Boolean.FALSE);

    query.setParameter("dateRequestedFrom", dateRequestedFrom);
    query.setParameter("dateRequestedTo", dateRequestedTo);

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
      Map<Long, Long> m = new HashMap<>();
      Calendar gcal = new GregorianCalendar();
      Date lowerDate = resultDateFormat.parse(resultDateFormat.format(dateRequestedFrom));
      Date upperDate = resultDateFormat.parse(resultDateFormat.format(dateRequestedTo));

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
      String bloodRhd = (String) result[3];
      BloodGroup bloodGroup = new BloodGroup(bloodAbo, bloodRhd);
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

  public void addAllRequests(List<Request> requests) {
    for (Request request : requests) {
      updateNewRequestFields(request);
      em.persist(request);
    }
    em.flush();
  }

  private void updateNewRequestFields(Request request) {
    if (request.getNumUnitsIssued() == null)
      request.setNumUnitsIssued(0);
    if (request.getNumUnitsIssued() < request.getNumUnitsRequested())
      request.setFulfilled(false);
    else
      request.setFulfilled(true);
  }
}
