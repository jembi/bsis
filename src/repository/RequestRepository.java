package repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.bloodtest.BloodTest;
import model.product.Product;
import model.product.ProductStatus;
import model.productmovement.ProductIssue;
import model.request.Request;
import model.testresults.TestResult;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import utils.BloodTestUtils;

import filter.UserInfoAddToThreadFilter;

@Repository
@Transactional
public class RequestRepository {

  public static final int ID_LENGTH = 12;

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private TestResultRepository testResultRepository;

  @Autowired
  private BloodTestUtils bloodTestUtils;

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
      String queryString = "SELECT p FROM Request p WHERE p.requestNumber = :requestNumber and p.isDeleted= :isDeleted";
      TypedQuery<Request> query = em.createQuery(queryString, Request.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      List<Request> requests = query.setParameter("requestNumber",
          requestNumber).getResultList();
      if (requests != null && requests.size() > 0) {
        request = requests.get(0);
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

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    try {
      Date from = (dateRequestedFrom == null || dateRequestedFrom.equals("")) ? dateFormat
          .parse("12/31/1970") : dateFormat.parse(dateRequestedFrom);
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
          .parse("12/31/1970") : dateFormat.parse(dateRequiredFrom);
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
    List<Request> requests = query.getResultList();
    if (CollectionUtils.isEmpty(requests)) {
      return null;
    }
    return requests.get(0);
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

  public void addRequest(Request productRequest) {
    em.persist(productRequest);
    em.flush();
  }

  private Date getDateRequestedAfterOrDefault(String requestedAfter) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date from = null;
    try {
      from = (requestedAfter == null || requestedAfter.equals("")) ? dateFormat
          .parse("12/31/1970") : dateFormat.parse(requestedAfter);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return from;      
  }

  private Date getDateRequiredByOrDefault(String dateExpiresTo) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date to = null;
    try {
      if (dateExpiresTo == null || dateExpiresTo.equals("")) {
        to = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(to);
        cal.add(Calendar.DATE, 365);
        to = cal.getTime();
      }
      else {
        to = dateFormat.parse(dateExpiresTo);
      }
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return to;      
  }

  public List<Request> findRequests(List<Integer> productTypeIds,
      List<Long> requestSiteIds, String requestedAfter,
      String requiredBy, Boolean includeSatisfiedRequests) {
	  String queryStr = "SELECT DISTINCT r FROM Request r LEFT JOIN FETCH r.issuedProducts WHERE " +
	            "(r.productType.id IN (:productTypeIds) AND " +
	            "r.requestSite.id IN (:requestSiteIds)) AND" +
	            "(r.requestDate >= :requestedAfter and r.requiredDate <= :requiredBy) AND " +
	            "r.isDeleted= :isDeleted";

	if (!includeSatisfiedRequests)
		queryStr = queryStr + " AND (r.fulfilled = :fulfilled)";

    TypedQuery<Request> query = em.createQuery(queryStr, Request.class);

    Date from = getDateRequestedAfterOrDefault(requestedAfter);
    Date to = getDateRequiredByOrDefault(requiredBy);

    if (!includeSatisfiedRequests)
    	query.setParameter("fulfilled", false);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productTypeIds", productTypeIds);
    query.setParameter("requestSiteIds", requestSiteIds);
    query.setParameter("requestedAfter", from);
    query.setParameter("requiredBy", to);

    return query.getResultList();
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
      if (canIssueProduct(product, request)) {
        ProductIssue productIssue = new ProductIssue();
        productIssue.setIssuedBy(UserInfoAddToThreadFilter.threadLocal.get());
        productIssue.setIssuedTo(request);
        productIssue.setIssuedOn(new Date());
        productIssue.setProduct(product);
        numUnitsIssued++;
        productRepository.updateProductInternalFields(product);
        product.setStatus(ProductStatus.ISSUED);
        em.persist(productIssue);
        em.merge(product);
      }
      else {
        throw new Exception("Could not issue products");
      }
    }

    em.flush();
    Integer numUnitsRequested = request.getNumUnitsRequested();
    if (numUnitsRequested != null && numUnitsIssued >= numUnitsRequested) {
      request.setFulfilled(true);
      em.merge(request);
    }
    em.flush();
  }

  private boolean canIssueProduct(Product product, Request request) {
    productRepository.updateProductInternalFields(product);
    Map<String, TestResult> testResults = testResultRepository.getRecentTestResultsForCollection(product.getCollectedSample().getId());
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
    
    String bloodAbo = "";
    String bloodRh = "";

    boolean canIssue = true;
    for (TestResult testResult : testResults.values()) {
      BloodTest bloodTest = testResult.getBloodTest();
      String actualResult = testResult.getResult();

      if (bloodTest.getName().equals("Blood Rh")) {
        System.out.println("here");
        bloodRh = actualResult;
      }

      if (bloodTest.getName().equals("Blood ABO")) {
        bloodAbo = actualResult;
      }

      if (!bloodTestUtils.isTestResultCorrect(bloodTest, actualResult)) {
        canIssue = false;
        break;
      }
    }

    String requestedAbo = request.getPatientBloodAbo().toString();
    String requestedRh = request.getPatientBloodRhd().toString(); 
    if (canIssue && bloodCrossmatch(bloodAbo, bloodRh, requestedAbo, requestedRh)) {
      return true;
    }

    return false;
  }

  private boolean bloodCrossmatch(String abo1, String rhd1, String abo2, String rhd2) {
    System.out.println("matching " + abo1 + ", " + ", " + rhd1 + ", " + abo2 + ", " + rhd2);
    if (abo1.equals(abo2) && rhd1.equals(rhd2))
      return true;
    if (abo1.equals("O") && (rhd1.equals(rhd2) || rhd1.equals("NEGATIVE")))
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
}
