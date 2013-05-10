package repository;

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
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.product.Product;
import model.product.ProductStatus;
import model.productmovement.ProductStatusChange;
import model.productmovement.ProductStatusChangeType;
import model.request.Request;
import model.util.BloodGroup;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import controller.UtilController;

@Repository
@Transactional
public class RequestRepository {

  public static final int ID_LENGTH = 12;

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private UtilController utilController;
  
  public void saveRequest(Request request) {
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

  public Request findRequest(String requestNumber) {
    Request request = null;
    if (requestNumber != null && requestNumber.length() > 0) {
      String queryString = "SELECT r FROM Request r WHERE r.requestNumber = :requestNumber and r.isDeleted= :isDeleted";
      TypedQuery<Request> query = em.createQuery(queryString, Request.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      query.setParameter("requestNumber", requestNumber);
      try {
        request = query.getSingleResult();
      } catch (NoResultException ex) {
        ex.printStackTrace();
      }
    }
    return request;
  }

  public Request findRequestWithIssuedProducts(String requestNumber) {
    Request request = null;
    if (requestNumber != null && requestNumber.length() > 0) {
      String queryString = "SELECT r FROM Request r LEFT JOIN FETCH r.issuedProducts WHERE " +
          "r.requestNumber = :requestNumber and r.isDeleted= :isDeleted";
      TypedQuery<Request> query = em.createQuery(queryString, Request.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      query.setParameter("requestNumber", requestNumber);
      try {
        request = query.getSingleResult();
      } catch (NoResultException ex) {
        ex.printStackTrace();
      }
    }
    return request;
  }

  public Request findRequestById(Long requestId) {
    Request request = null;
    if (requestId != null) {
      String queryString = "SELECT DISTINCT r FROM Request r LEFT JOIN FETCH r.issuedProducts WHERE " +
                           "r.id = :requestId and r.isDeleted= :isDeleted";
      TypedQuery<Request> query = em.createQuery(queryString, Request.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      List<Request> requests = query.setParameter("requestId", requestId)
          .getResultList();
      if (requests != null && requests.size() > 0) {
        request = requests.get(0);
      }
    }
    return request;
  }

  public ArrayList<Request> getAllRequests() {
    String queryString = "SELECT DISTINCT r FROM Request r LEFT JOIN FETCH r.issuedProducts WHERE " +
                         "r.isDeleted = :isDeleted order by r.dateRequested";
    TypedQuery<Request> query = em.createQuery(queryString, Request.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return new ArrayList<Request>(query.getResultList());
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
      return new ArrayList<Request>();
    }
    return requests;
  }

  public ArrayList<Request> getAllUnfulfilledRequests() {
    String queryString = "SELECT p FROM Request p where p.status = 'pending' or p.status='partiallyFulfilled' and p.isDeleted = :isDeleted order by p.dateRequested";
    TypedQuery<Request> query = em.createQuery(queryString, Request.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return new ArrayList<Request>(query.getResultList());
  }

  public List<Request> findAnyRequestMatching(String requestNumber,
      String dateRequestedFrom, String dateRequestedTo,
      String dateRequiredFrom, String dateRequiredTo, List<String> sites,
      List<String> productTypes, List<String> statuses) {

    TypedQuery<Request> query = em
        .createQuery(
            "SELECT r FROM Request r, Location L WHERE "
                + "(L.locationId=r.siteId AND L.isCollectionSite=TRUE) AND "
                + "(r.requestNumber = :requestNumber OR L.name IN (:sites) OR "
                + "r.productType IN (:productTypes)) AND (r.status IN (:statuses)) AND "
                + "((r.dateRequested BETWEEN :dateRequestedFrom AND "
                + ":dateRequestedTo) AND (r.dateRequired BETWEEN "
                + ":dateRequiredFrom AND " + ":dateRequiredTo)) AND "
                + "(r.isDeleted= :isDeleted)", Request.class);

    query.setParameter("isDeleted", Boolean.FALSE);

    query.setParameter("requestNumber", requestNumber == null ? ""
        : requestNumber);
    query.setParameter("sites", sites);
    query.setParameter("productTypes", productTypes);
    query.setParameter("statuses", statuses);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    try {
      Date from = (dateRequestedFrom == null || dateRequestedFrom.equals("")) ? dateFormat
          .parse("31/12/1970") : dateFormat.parse(dateRequestedFrom);
      query.setParameter("dateRequestedFrom", from);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    try {
      Date to = (dateRequestedTo == null || dateRequestedTo.equals("")) ? dateFormat
          .parse(dateFormat.format(new Date())) : dateFormat
          .parse(dateRequestedTo);
      query.setParameter("dateRequestedTo", to);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    try {
      Date from = (dateRequiredFrom == null || dateRequiredFrom.equals("")) ? dateFormat
          .parse("31/12/1970") : dateFormat.parse(dateRequiredFrom);
      query.setParameter("dateRequiredFrom", from);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    try {
      Date to = (dateRequiredTo == null || dateRequiredTo.equals("")) ? dateFormat
          .parse(dateFormat.format(new Date())) : dateFormat
          .parse(dateRequiredTo);
      query.setParameter("dateRequiredTo", to);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    List<Request> resultList = query.getResultList();
    return resultList;
  }

  public Request findRequestByRequestNumber(String requestNumber) {
    TypedQuery<Request> query = em
        .createQuery(
            "SELECT r FROM Request r WHERE r.requestNumber = :requestNumber and r.isDeleted= :isDeleted",
            Request.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("requestNumber", requestNumber);
    Request request = null;
    try {
      request = query.getSingleResult();
    } catch (NoResultException ex) {
      ex.printStackTrace();
    }
    return request;
  }

  public Request findRequestByRequestNumberIncludeDeleted(String requestNumber) {
    TypedQuery<Request> query = em
        .createQuery(
            "SELECT r FROM Request r WHERE r.requestNumber = :requestNumber",
            Request.class);
    query.setParameter("requestNumber", requestNumber);
    Request request = null;
    try {
      request = query.getSingleResult();
    } catch (NoResultException ex) {
      ex.printStackTrace();
    }
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
            + "(L.locationId=r.siteId AND L.isCollectionSite=TRUE) AND"
            + "(r.status NOT IN (:statuses)) AND "
            + "(r.isDeleted= :isDeleted)", Request.class);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("statuses", Arrays.asList("fulfilled"));
    List<Request> resultList = query.getResultList();
    return resultList;
  }

  public void deleteRequest(String requestNumber) {
    Request existingRequest = findRequestByRequestNumber(requestNumber);
    existingRequest.setIsDeleted(Boolean.TRUE);
    em.merge(existingRequest);
    em.flush();
  }

  public static String generateUniqueRequestNumber() {
    String uniqueRequestNumber;
    uniqueRequestNumber = "R-" + RandomStringUtils.randomNumeric(ID_LENGTH).toUpperCase();
    return uniqueRequestNumber;
  }

  public Request addRequest(Request productRequest) {
    updateNewRequestFields(productRequest);
    em.persist(productRequest);
    em.flush();
    em.refresh(productRequest);
    return productRequest;
  }

  private Date getDateRequestedAfterOrDefault(String requestedAfter) {
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date from = null;
    try {
      from = (requestedAfter == null || requestedAfter.equals("")) ? dateFormat
          .parse("31/12/1970") : dateFormat.parse(requestedAfter);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return from;      
  }

  private Date getDateRequiredByOrDefault(String dateRequiredBy) {
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date to = null;
    try {
      if (StringUtils.isBlank(dateRequiredBy)) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 365);
        to = cal.getTime();
      }
      else {
        to = dateFormat.parse(dateRequiredBy);
      }
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return to;      
  }

  public List<Object> findRequests(String requestNumber, List<Integer> productTypeIds,
      List<Long> requestSiteIds, String requestedAfter,
      String requiredBy, Boolean includeSatisfiedRequests, Map<String, Object> pagingParams) {

    String queryStr = "";
    if (StringUtils.isNotBlank(requestNumber)) {
      queryStr = "SELECT r FROM Request r LEFT JOIN FETCH r.issuedProducts WHERE " +
                 "r.requestNumber =:requestNumber AND " +
                 "r.isDeleted= :isDeleted";
    } else {
      queryStr = "SELECT r FROM Request r LEFT JOIN FETCH r.issuedProducts WHERE " +
          "(r.productType.id IN (:productTypeIds) AND " +
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
    }
    else {
      query.setParameter("productTypeIds", productTypeIds);
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
    countQueryStr = countQueryStr.replaceFirst("LEFT JOIN FETCH r.issuedProducts", "");
    TypedQuery<Long> countQuery = em.createQuery(countQueryStr, Long.class);
    for (Parameter<?> parameter : query.getParameters()) {
      countQuery.setParameter(parameter.getName(), query.getParameterValue(parameter));
    }
    return countQuery.getSingleResult().longValue();
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

  public void issueProductsToRequest(Long requestId, String productsToIssue) throws Exception {
    Request request = findRequestById(requestId);
    productsToIssue = productsToIssue.replaceAll("\"", "");
    productsToIssue = productsToIssue.replaceAll("\\[", "");
    productsToIssue = productsToIssue.replaceAll("\\]", "");
    String[] productIds = productsToIssue.split(",");
    int numUnitsIssued = 0;
    if (request.getNumUnitsIssued() != null)
      numUnitsIssued = request.getNumUnitsIssued();
    for (String productId : productIds) {
      Product product = em.find(Product.class, Long.parseLong(productId));
      // handle the case where the product, test result has been updated
      // between the time when matching products are searched and selected
      // for issuing
      if (!canIssueProduct(product, request))
        throw new Exception("Could not issue products");
    }

    for (String productId : productIds) {
      Product product = em.find(Product.class, Long.parseLong(productId));
      // we know these products can be issued now
      // although there is some doubt about the behavior of locks between the check above and now
      Date today = new Date();
      ProductStatusChange productIssue = new ProductStatusChange();
      productIssue.setNewStatus(ProductStatus.ISSUED);
      productIssue.setStatusChangedOn(today);
      productIssue.setStatusChangeType(ProductStatusChangeType.ISSUED);
      productIssue.setChangedBy(utilController.getCurrentUser());
      productIssue.setIssuedTo(request);
      productIssue.setProduct(product);
      numUnitsIssued++;
      product.setStatus(ProductStatus.ISSUED);
      product.setIssuedOn(today);
      product.setIssuedTo(request);
      em.persist(productIssue);
      em.merge(product);
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

  private boolean canIssueProduct(Product product, Request request) {
    // first make sure the product is up-to-date
    // the product may have expired so this update is required
    // we update the expiry date of a product periodically
    productRepository.updateProductInternalFields(product);
    String requestedProductType = request.getProductType().getProductType();
    String productType = product.getProductType().getProductType();

    if (!productType.equals(requestedProductType))
      return false;
    
    // product available or not
    if (!product.getStatus().equals(ProductStatus.AVAILABLE))
      return false;

    if (product.getIsDeleted())
      return false;

    Date today = new Date();
    if (product.getExpiresOn().before(today))
      return false;
    
    String bloodAbo = product.getCollectedSample().getBloodAbo();
    String bloodRh = product.getCollectedSample().getBloodRh();

    boolean canIssue = true;

    String requestedAbo = request.getPatientBloodAbo();
    String requestedRh = request.getPatientBloodRh(); 
    if (canIssue && bloodCrossmatch(bloodAbo, bloodRh, requestedAbo, requestedRh)) {
      return true;
    }

    return false;
  }

  private boolean bloodCrossmatch(String abo1, String rh1, String abo2, String rh2) {
    System.out.println("matching " + abo1 + ", " + ", " + rh1 + ", " + abo2 + ", " + rh2);
    if (abo1.equals(abo2) && rh1.equals(rh2))
      return true;
    if (abo1.equals("O") && (rh1.equals(rh2) || rh1.equals("-")))
      return true;
    return false;
  }

  public Request findRequestById(String requestId) {
    Request request = null;
    try {
      request = findRequestById(Long.parseLong(requestId));
    } catch (NumberFormatException ex) {
      ex.printStackTrace();
    }
    return request;
  }

  public List<Product> getIssuedProductsForRequest(Long requestId) {
    String queryString = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.collectedSample WHERE " +
                         "p.issuedTo.id = :requestId AND p.isDeleted= :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("requestId", requestId);
    List<Product> issuedProducts = query.getResultList();
    return issuedProducts;
  }

  public Map<String, Map<Long, Long>> findNumberOfRequests(Date dateRequestedFrom,
      Date dateRequestedTo, String aggregationCriteria,
      List<String> sites, List<String> bloodGroups) {

    List<Long> siteIds = new ArrayList<Long>();
    if (sites != null) {
      for (String site : sites) {
        siteIds.add(Long.parseLong(site));
      }
    } else {
      siteIds.add((long)-1);
    }

    Map<String, Map<Long, Long>> resultMap = new HashMap<String, Map<Long,Long>>();
    for (String bloodGroup : bloodGroups) {
      resultMap.put(bloodGroup, new HashMap<Long, Long>());
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
      Map<Long, Long> m = new HashMap<Long, Long>();
      Calendar gcal = new GregorianCalendar();
      Date lowerDate = null;
      Date upperDate = null;
      try {
        lowerDate = resultDateFormat.parse(resultDateFormat.format(dateRequestedFrom));
        upperDate = resultDateFormat.parse(resultDateFormat.format(dateRequestedTo));
      } catch (ParseException e1) {
        // TODO Auto-generated catch block
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
      String bloodRhd = (String) result[3];
      BloodGroup bloodGroup = new BloodGroup(bloodAbo, bloodRhd);
      Map<Long, Long> m = resultMap.get(bloodGroup.toString());
      if (m == null)
        continue;
      try {
        Date formattedDate = resultDateFormat.parse(resultDateFormat.format(d));
        Long utcTime = formattedDate.getTime();
        if (m.containsKey(utcTime)) {
          Long newVal = m.get(utcTime) + (Long) result[0];
          m.put(utcTime, newVal);
        } else {
          m.put(utcTime, (Long) result[0]);
        }
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
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

  public void updateNewRequestFields(Request request) {
    if (request.getNumUnitsIssued() == null)
      request.setNumUnitsIssued(0);
    if (request.getNumUnitsIssued() < request.getNumUnitsRequested())
      request.setFulfilled(false);
    else
      request.setFulfilled(true);
  }
}
