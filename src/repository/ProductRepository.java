package repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.bloodtest.BloodTest;
import model.collectedsample.CollectedSample;
import model.product.Product;
import model.producttype.ProductType;
import model.request.Request;
import model.testresults.TestResult;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRhd;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import viewmodel.MatchingProductViewModel;

@Repository
@Transactional
public class ProductRepository {
  @PersistenceContext
  private EntityManager em;

  @Autowired
  private BloodTestRepository bloodTestRepository;

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  public void discardIfQuarantinedProduct(Product product) {

    CollectedSample c = collectedSampleRepository.findCollectedSampleById(product.getCollectedSample().getId());
    List<TestResult> testResults = c.getTestResults();
    List<BloodTest> bloodTests = bloodTestRepository.getAllBloodTests();
    Map<String, String> correctTestResults = new HashMap<String, String>();
    for (BloodTest bt : bloodTests) {
      if (bt.getIsRequired()) {
        correctTestResults.put(bt.getName(), bt.getCorrectResult().toLowerCase());
      }
    }

    Set<String> testResultsFound = new HashSet<String>();
    boolean quarantined = false;
    for (TestResult t : testResults) {
      if (t.getIsDeleted())
        continue;
      String testName = t.getBloodTest().getName();
      String testResult = t.getResult();
      if (correctTestResults.containsKey(testName)) {
        if (testResult == null || !testResult.toLowerCase().equals(correctTestResults.get(testName)))
          quarantined = true;
          testResultsFound.add(testName);
      }
    }

    // none of the required test results should be missing
    if (testResultsFound.size() != correctTestResults.size())
      quarantined = true;

    product.setIsQuarantined(quarantined);
  }

  public void updateBloodGroup(Product product) {

    CollectedSample c = collectedSampleRepository.findCollectedSampleById(product.getCollectedSample().getId());

    BloodAbo bloodAbo = product.getBloodAbo();
    BloodRhd bloodRhd = product.getBloodRhd();

    Date aboDate = new Date(0);
    Date rhdDate = new Date(0);

    for (TestResult t : c.getTestResults()) {
      if (t.getIsDeleted())
        continue;
      BloodTest bt = t.getBloodTest();
      String testName = bt.getName();
      String testResult = t.getResult();
      Date testedOn = t.getTestedOn();

      if (testName != null && testName.equals("Blood ABO") && testedOn.after(aboDate)) {
        bloodAbo = BloodAbo.valueOf(testResult);
        aboDate = testedOn;
      }
      if (testName != null && testName.equals("Blood Rh") && testedOn.after(rhdDate)) {
        bloodRhd = BloodRhd.valueOf(testResult);
        rhdDate = testedOn;
      }
    }
    product.setBloodAbo(bloodAbo);
    product.setBloodRhd(bloodRhd);
  }
  
  public Product findProduct(String productNumber) {
    Product product = null;
    if (productNumber != null && productNumber.length() > 0) {
      String queryString = "SELECT p FROM Product p WHERE p.productNumber = :productNumber and p.isDeleted= :isDeleted";
      TypedQuery<Product> query = em.createQuery(queryString, Product.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      List<Product> products = query.setParameter("productNumber",
          productNumber).getResultList();
      if (products != null && products.size() > 0) {
        product = products.get(0);
      }
    }
    return product;
  }

  private Date getDateExpiresFromOrDefault(String dateExpiresFrom) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date from = null;
    try {
      from = (dateExpiresFrom == null || dateExpiresFrom.equals("")) ? dateFormat
          .parse("12/31/1970") : dateFormat.parse(dateExpiresFrom);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return from;      
  }

  private Date getDateExpiresToOrDefault(String dateExpiresTo) {
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

  public Product findProductByProductNumber(String productNumber) {
    TypedQuery<Product> query = em
        .createQuery(
            "SELECT p FROM Product p WHERE p.productNumber = :productNumber and p.isDeleted= :isDeleted",
            Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productNumber", productNumber);
    List<Product> products = query.getResultList();
    if (CollectionUtils.isEmpty(products)) {
      return null;
    }
    return products.get(0);
  }

  public List<Product> findProductByCollectionNumber(
      String collectionNumber, List<String> available, List<String> safe, String dateExpiresFrom,
      String dateExpiresTo) {

    TypedQuery<Product> query;
    String queryStr = "SELECT p FROM Product p WHERE " +
                      "p.collectedSample.collectionNumber = :collectionNumber and " +
                      "((p.expiresOn is NULL) or " +
                      " (p.expiresOn >= :fromDate and p.expiresOn <= :toDate)) and " +
                      "p.isDeleted= :isDeleted";

    if (available.size() == 1) {
      queryStr += " AND p.isAvailable=:isAvailable";
    }
    if (safe.size() == 1 && safe.contains("not_safe")) {
      queryStr += " AND (p.isQuarantined = :isQuarantined OR p.expiresOn <= :expiresOn)";
    }
    if (safe.size() == 1 && safe.contains("safe")) {
      queryStr += " AND (p.isQuarantined = :isQuarantined AND p.expiresOn >= :expiresOn)";
    }

    query = em.createQuery(queryStr, Product.class);

    if (available.size() == 1 && available.contains("available")) {
      query.setParameter("isAvailable", true);
    }
    if (available.size() == 1 && available.contains("not_available")) {
      query.setParameter("isAvailable", false);
    }
    if (safe.size() == 1 && safe.contains("not_safe")) {
      query.setParameter("isQuarantined", true);
      query.setParameter("expiresOn", new Date());
    }
    if (safe.size() == 1 && safe.contains("safe")) {
      queryStr += " AND p.expiresOn <= :expiresOn";
      query.setParameter("isQuarantined", false);
      query.setParameter("expiresOn", new Date());
    }

    Date from = getDateExpiresFromOrDefault(dateExpiresFrom);
    Date to = getDateExpiresToOrDefault(dateExpiresTo);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("collectionNumber", collectionNumber);
    query.setParameter("fromDate", from);
    query.setParameter("toDate", to);

    return query.getResultList();
  }

  public List<Object> findProductByProductTypes(
      List<String> productTypes, List<String> available, List<String> safe,
      String dateExpiresFrom, String dateExpiresTo,
      Map<String, Object> pagingParams) {

    System.out.println(pagingParams);

    String queryStr = "SELECT p FROM Product p WHERE " +
        "p.productType.productType IN (:productTypes) and " +
        "((p.expiresOn is NULL) or " +
        " (p.expiresOn >= :fromDate and p.expiresOn <= :toDate)) and " +
        "p.isDeleted= :isDeleted";
    
    if (available.size() == 1) {
      queryStr += " AND p.isAvailable=:isAvailable";
    }
    if (safe.size() == 1 && safe.contains("not_safe")) {
      queryStr += " AND (p.isQuarantined = :isQuarantined OR p.expiresOn <= :expiresOn)";
    }
    if (safe.size() == 1 && safe.contains("safe")) {
      queryStr += " AND (p.isQuarantined = :isQuarantined AND p.expiresOn >= :expiresOn)";
    }

    TypedQuery<Product> query = em.createQuery(queryStr, Product.class);

    if (available.size() == 1 && available.contains("available")) {
      query.setParameter("isAvailable", true);
    }
    if (available.size() == 1 && available.contains("not_available")) {
      query.setParameter("isAvailable", false);
    }
    if (safe.size() == 1 && safe.contains("not_safe")) {
      query.setParameter("isQuarantined", true);
      query.setParameter("expiresOn", new Date());
    }
    if (safe.size() == 1 && safe.contains("safe")) {
      query.setParameter("isQuarantined", false);
      query.setParameter("expiresOn", new Date());
    }

    Date from = getDateExpiresFromOrDefault(dateExpiresFrom);
    Date to = getDateExpiresToOrDefault(dateExpiresTo);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productTypes", productTypes);
    query.setParameter("fromDate", from);
    query.setParameter("toDate", to);

    String countQueryStr = queryStr.replaceFirst("SELECT p", "SELECT COUNT(p)");
    TypedQuery<Long> countQuery = em.createQuery(countQueryStr, Long.class);

    for (Parameter<?> parameter : query.getParameters()) {
      countQuery.setParameter(parameter.getName(), query.getParameterValue(parameter));
    }

    int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
    int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);

    query.setFirstResult(start);
    query.setMaxResults(length);

    Long totalResults = countQuery.getSingleResult().longValue();

    return Arrays.asList(query.getResultList(), totalResults);
  }

  public List<Product> getAllUnissuedProducts() {
    String queryString = "SELECT p FROM Product p where p.isDeleted = :isDeleted and p.isIssued= :isIssued";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("isIssued", Boolean.FALSE);
    return query.getResultList();
  }

  public List<Product> getAllUnissuedThirtyFiveDayProducts() {
    String queryString = "SELECT p FROM Product p where p.isDeleted = :isDeleted and p.isIssued= :isIssued and p.dateCollected > :minDate";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("isIssued", Boolean.FALSE);
    query.setParameter("minDate", new DateTime(new Date()).minusDays(35)
        .toDate());
    return query.getResultList();
  }

  public List<Product> getAllProducts() {
    String queryString = "SELECT p FROM Product p where p.isDeleted = :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.getResultList();
  }

  public boolean isProductCreated(String collectionNumber) {
    String queryString = "SELECT p FROM Product p WHERE p.collectionNumber = :collectionNumber and p.isDeleted = :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<Product> products = query.setParameter("collectionNumber",
        collectionNumber).getResultList();
    if (products != null && products.size() > 0) {
      return true;
    }
    return false;
  }

  public void deleteAllProducts() {
    Query query = em.createQuery("DELETE FROM Product p");
    query.executeUpdate();
  }

  public List<Product> getAllProducts(String productType) {
    String queryString = "SELECT p FROM Product p where p.type = :productType and p.isDeleted = :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productType", productType);
    return query.getResultList();
  }

  public List<Product> getProducts(Date fromDate, Date toDate) {
    TypedQuery<Product> query = em
        .createQuery(
            "SELECT p FROM Product p WHERE  p.dateCollected >= :fromDate and p.dateCollected<= :toDate and p.isDeleted = :isDeleted",
            Product.class);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<Product> products = query.getResultList();
    if (CollectionUtils.isEmpty(products)) {
      return new ArrayList<Product>();
    }
    return products;
  }

  public List<Product> getAllUnissuedProducts(String productType, String abo,
      String rhd) {
    String queryString = "SELECT p FROM Product p where p.type = :productType and p.abo= :abo and p.rhd= :rhd and p.isDeleted = :isDeleted and p.isIssued= :isIssued";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("isIssued", Boolean.FALSE);
    query.setParameter("productType", productType);
    query.setParameter("abo", abo);
    query.setParameter("rhd", rhd);
    return query.getResultList();
  }

  public List<Product> getAllUnissuedThirtyFiveDayProducts(String productType,
      String abo, String rhd) {
    String queryString = "SELECT p FROM Product p where p.type = :productType and p.abo= :abo and p.rhd= :rhd and p.isDeleted = :isDeleted and p.isIssued= :isIssued and p.dateCollected > :minDate";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("isIssued", Boolean.FALSE);
    query.setParameter("productType", productType);
    query.setParameter("abo", abo);
    query.setParameter("rhd", rhd);
    query.setParameter("minDate", new DateTime(new Date()).minusDays(35)
        .toDate());

    return query.getResultList();
  }

  public Product findProduct(Long productId) {
    return em.find(Product.class, productId);
  }

  public Product findProductById(Long productId) {
    String queryString = "SELECT p FROM Product p LEFT JOIN FETCH p.collectedSample where p.id = :productId AND p.isDeleted = :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productId", productId);
    return query.getSingleResult();
  }

  public List<Product> findAnyProductMatching(String productNumber,
      String collectionNumber, List<ProductType> types, List<String> availability) {

    return findAnyProductMatching(productNumber, collectionNumber,
        Arrays.asList(BloodAbo.A, BloodAbo.B, BloodAbo.O, BloodAbo.AB),
        Arrays.asList(BloodRhd.POSITIVE, BloodRhd.NEGATIVE), types, availability);
  }

  public List<Product> findAnyProductMatching(String productNumber,
      String collectionNumber, List<BloodAbo> bloodAbo, List<BloodRhd> bloodRhd,
      List<ProductType> types, List<String> availability) {

    TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE "
        + "(p.productNumber = :productNumber OR "
        + "p.collectionNumber = :collectionNumber "
        + "OR p.type IN (:types)) AND (p.isIssued IN (:isIssued)) AND "
        + "p.abo IN (:bloodAbo) AND p.rhd IN (:bloodRhd) AND "
        + "(p.isDeleted= :isDeleted)", Product.class);

    query.setParameter("isDeleted", Boolean.FALSE);
    String productNo = ((productNumber == null) ? "" : productNumber);
    query.setParameter("productNumber", productNo);
    String collectionNo = ((collectionNumber == null) ? "" : collectionNumber);
    query.setParameter("collectionNumber", collectionNo);
    query.setParameter("bloodAbo", bloodAbo);
    query.setParameter("bloodRhd", bloodRhd);
    query.setParameter("types", types);
    query.setParameter("isIssued", getIssuedListFromAvailability(availability));

    List<Product> resultList = query.getResultList();
    return resultList;
  }

  private List<Boolean> getIssuedListFromAvailability(List<String> availability) {
    List<Boolean> issued = new ArrayList<Boolean>();
    if (availability == null)
      return issued;
    for (String available : availability) {
      if (available.equals("available"))
        issued.add(false);
      if (available.equals("notAvailable")) {
        issued.add(true);
      }
    }
    return issued;
  }

  public List<Product> findProductByProductNumber(String productNumber, List<String> available, List<String> safe, String dateExpiresFrom, String dateExpiresTo) {

    String queryStr = "SELECT p FROM Product p WHERE p.productNumber = :productNumber AND p.isDeleted= :isDeleted AND" +
        "((p.expiresOn is NULL) or " +
        " (p.expiresOn >= :fromDate and p.expiresOn <= :toDate))";

    TypedQuery<Product> query;
    if (available.size() == 1) {
      queryStr += " AND p.isAvailable=:isAvailable";
    }
    if (safe.size() == 1 && safe.contains("not_safe")) {
      queryStr += " AND (p.isQuarantined = :isQuarantined OR p.expiresOn <= :expiresOn)";
    }
    if (safe.size() == 1 && safe.contains("safe")) {
      queryStr += " AND (p.isQuarantined = :isQuarantined AND p.expiresOn >= :expiresOn)";
    }

    query = em.createQuery(queryStr, Product.class);

    if (available.size() == 1 && available.contains("available")) {
      query.setParameter("isAvailable", true);
    }
    if (available.size() == 1 && available.contains("not_available")) {
      query.setParameter("isAvailable", false);
    }
    if (safe.size() == 1 && safe.contains("not_safe")) {
      query.setParameter("isQuarantined", true);
      query.setParameter("expiresOn", new Date());
    }
    if (safe.size() == 1 && safe.contains("safe")) {
      queryStr += " AND p.expiresOn <= :expiresOn";
      query.setParameter("isQuarantined", false);
      query.setParameter("expiresOn", new Date());
    }

    Date from = getDateExpiresFromOrDefault(dateExpiresFrom);
    Date to = getDateExpiresToOrDefault(dateExpiresTo);

    query.setParameter("fromDate", from);
    query.setParameter("toDate", to);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productNumber", productNumber);
    return query.getResultList();
  }

  public Product updateProduct(Product product) {
    Product existingProduct = findProductById(product.getId());
    if (existingProduct == null) {
      return null;
    }
    discardIfQuarantinedProduct(existingProduct);
    updateBloodGroup(existingProduct);
    existingProduct.copy(product);
    em.merge(existingProduct);
    em.flush();
    return existingProduct;
  }

  public void deleteProduct(String productNumber) {
    Product existingProduct = findProductByProductNumber(productNumber);
    existingProduct.setIsDeleted(Boolean.TRUE);
    em.merge(existingProduct);
    em.flush();
  }

  public void issueProduct(String productNumber) {
    Product existingProduct = findProductByProductNumber(productNumber);
    em.merge(existingProduct);
    em.flush();
  }

  public void addProduct(Product product) {
    discardIfQuarantinedProduct(product);
    updateBloodGroup(product);
    em.persist(product);
    em.flush();
  }

  public void deleteProduct(Long productId) {
    Product existingProduct = findProductById(productId);
    existingProduct.setIsDeleted(Boolean.TRUE);
    em.merge(existingProduct);
    em.flush();
  }

  public List<MatchingProductViewModel> findMatchingProductsForRequest(Request productRequest) {
    Date today = new Date();
    TypedQuery<Product> query = em.createQuery(
                 "SELECT p from Product p where p.productType = :productType AND " +
                 "p.expiresOn >= :today AND " +
                 "p.isAvailable = :isAvailable AND " +
                 "p.isQuarantined = :isQuarantined AND " +
                 "((p.bloodAbo = :bloodAbo AND p.bloodRhd = :bloodRhd) OR " +
                 "(p.bloodAbo = :bloodO)) AND " +
                 "p.isDeleted = :isDeleted",
                  Product.class);
    query.setParameter("productType", productRequest.getProductType());
    query.setParameter("today", today);
    query.setParameter("isAvailable", true);
    query.setParameter("isQuarantined", false);
    query.setParameter("bloodAbo", productRequest.getBloodAbo());
    query.setParameter("bloodO", BloodAbo.O);
    query.setParameter("bloodRhd", productRequest.getBloodRhd());
    query.setParameter("isDeleted", false);

    List<MatchingProductViewModel> products = new ArrayList<MatchingProductViewModel>();
    for (Product product : query.getResultList()) {
      products.add(new MatchingProductViewModel(product));
    }

    return products;
  }

  private boolean bloodCrossmatch(String abo1, String rhd1, String abo2, String rhd2) {
    if (abo1.equals(abo2) && rhd1.equals(rhd2))
      return true;
    if (abo1.equals("O") && (rhd1.equals(rhd2) || rhd1.equals("NEGATIVE")))
      return true;
    return false;
  }
  
  public Product findSingleProductByProductNumber(String productNumber) {
    List<Product> products = findProductByProductNumber(productNumber, Arrays.asList(new String[0]), Arrays.asList(new String[0]), "", "");
    if (products != null && products.size() == 1)
      return products.get(0);
    return null;
  }

  public Map<String, Object> generateInventorySummaryFast() {
    Map<String, Object> inventory = new HashMap<String, Object>();
    // IMPORTANT: Distinct is necessary to avoid a cartesian product of test results and products from being returned
    // Also LEFT JOIN FETCH prevents the N+1 queries problem associated with Lazy Many-to-One joins
    TypedQuery<Product> q = em.createQuery(
                             "SELECT DISTINCT p from Product p " +
                             "where p.isAvailable=:isAvailable AND p.isDeleted=:isDeleted AND p.isQuarantined=:isQuarantined AND p.expiresOn>=:expiresOn",
                             Product.class);
    q.setParameter("isAvailable", true);
    q.setParameter("isQuarantined", false);
    q.setParameter("isDeleted", false);
    q.setParameter("expiresOn", new Date());

    TypedQuery<ProductType> productTypeQuery = em.createQuery("SELECT pt FROM ProductType pt", ProductType.class);

    for (ProductType productType : productTypeQuery.getResultList()) {
      Map<String, Map<Long, Long>> inventoryByBloodGroup = new HashMap<String, Map<Long, Long>>();

      inventoryByBloodGroup.put("A+", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("B+", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("AB+", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("O+", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("A-", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("B-", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("AB-", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("O-", getMapWithNumDaysWindows());

      inventory.put(productType.getProductTypeName(), inventoryByBloodGroup);
    }

    DateTime today = new DateTime();
    for (Product product : q.getResultList()) {
      String productType = product.getProductType().getProductTypeName();
      Map<String, Map<Long, Long>> inventoryByBloodGroup = (Map<String, Map<Long, Long>>) inventory.get(productType);
      String bloodGroup = new BloodGroup(product.getBloodAbo(), product.getBloodRhd()).toString();
      Map<Long, Long> numDayMap = inventoryByBloodGroup.get(bloodGroup);
      DateTime createdOn = new DateTime(product.getCreatedOn().getTime());
      Long age = (long) Days.daysBetween(createdOn, today).getDays();
      // compute window based on age
      age = Math.abs((age / 5) * 5);
      if (age > 30)
        age = (long) 30;
      Long count = numDayMap.get(age);
      numDayMap.put(age, count+1);
    }
    
    return inventory;
  }

//  public Map<String, Object> generateInventorySummary() {
//
//    Map<String, Object> inventory = new HashMap<String, Object>();
//    Session session = em.unwrap(Session.class);
//    session.enableFilter("availableProductsNotExpiredFilter");
//
//    TypedQuery<ProductType> productTypeQuery = em.createQuery("select pt from ProductType pt", ProductType.class);
//
//    DateTime today = new DateTime();
//    for (ProductType productType : productTypeQuery.getResultList()) {
//
//      Map<String, Map<Long, Long>> inventoryByBloodGroup = new HashMap<String, Map<Long, Long>>();
//
//      inventoryByBloodGroup.put("A+", getMapWithNumDaysWindows());
//      inventoryByBloodGroup.put("B+", getMapWithNumDaysWindows());
//      inventoryByBloodGroup.put("AB+", getMapWithNumDaysWindows());
//      inventoryByBloodGroup.put("O+", getMapWithNumDaysWindows());
//      inventoryByBloodGroup.put("A-", getMapWithNumDaysWindows());
//      inventoryByBloodGroup.put("B-", getMapWithNumDaysWindows());
//      inventoryByBloodGroup.put("AB-", getMapWithNumDaysWindows());
//      inventoryByBloodGroup.put("O-", getMapWithNumDaysWindows());
//
//      for (Product product : productType.getProducts()) {
//        String bloodGroup = getBloodGroupForProduct(product).toString();
//        Map<Long, Long> numDayMap = inventoryByBloodGroup.get(bloodGroup);
//        DateTime createdOn = new DateTime(product.getCreatedOn().getTime());
//        Long age = (long) Days.daysBetween(createdOn, today).getDays();
//        // compute window based on age
//        age = Math.abs((age / 5) * 5);
//        if (age > 30)
//          age = (long) 30;
//        Long count = numDayMap.get(age);
//        System.out.println(numDayMap);
//        System.out.println(count);
//        System.out.println(age);
//        numDayMap.put(age, count+1);
//      }
//
//      inventory.put(productType.getProductType(), inventoryByBloodGroup);
//    }
//    
//    session.disableFilter("availableProductsNotExpiredFilter");
//    return inventory;
//  }

  private Map<Long, Long> getMapWithNumDaysWindows() {
    Map<Long, Long> m = new HashMap<Long, Long>();
    m.put((long)0, (long)0); // age < 5 days
    m.put((long)5, (long)0); // 5 <= age < 10 days
    m.put((long)10, (long)0); // 10 <= age < 15 days
    m.put((long)15, (long)0); // 15 <= age < 20 days
    m.put((long)20, (long)0); // 20 <= age < 25 days
    m.put((long)25, (long)0); // 25 <= age < 30 days
    m.put((long)30, (long)0); // age > 30 days
    return m;
  }

  private BloodGroup getBloodGroupForProduct(Product product) {

    CollectedSample c = product.getCollectedSample();
    String abo = null;
    String rh = null;

    for (TestResult t : c.getTestResults()) {
      String testName = t.getBloodTest().getName();
      if (testName.equals("Blood ABO"))
        abo = t.getResult();
      else if (testName.equals("Blood Rh"))
        rh = t.getResult();
    }

    if (abo == null || rh == null) {
      return new BloodGroup(BloodAbo.Unknown, BloodRhd.Unknown);
    }

    return new BloodGroup(BloodAbo.valueOf(abo), BloodRhd.valueOf(rh));
  }

  public BloodGroup getBloodGroupForProduct(Long productId) {
    Product product = findProductById(productId);
    return getBloodGroupForProduct(product);
  }

  public void addAllProducts(List<Product> products) {
    for (Product p : products) {
      em.persist(p);
    }
    em.flush();
  }
}
