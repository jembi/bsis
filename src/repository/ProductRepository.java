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
      String testName = t.getBloodTest().getName();
      String testResult = t.getResult();
      if (correctTestResults.containsKey(testName)) {
        if (testResult == null || !testResult.toLowerCase().equals(correctTestResults.get(testName)))
          quarantined = true;
          testResultsFound.add(testName);
      }
    }

    System.out.println(testResultsFound);
    System.out.println(correctTestResults);
    // none of the required test results should be missing
    if (testResultsFound.size() != correctTestResults.size())
      quarantined = true;

    product.setIsQuarantined(quarantined);
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

  public List<Product> findProductByProductNumber(
      String productNumber, String dateExpiresFrom,
      String dateExpiresTo) {

    TypedQuery<Product> query = em
        .createQuery(
            "SELECT p FROM Product p WHERE " +
            "p.productNumber = :productNumber and " +
            "((p.expiresOn is NULL) or " +
            " (p.expiresOn >= :fromDate and p.expiresOn <= :toDate)) and " +
            "p.isDeleted= :isDeleted",
            Product.class);

    Date from = getDateExpiresFromOrDefault(dateExpiresFrom);
    Date to = getDateExpiresToOrDefault(dateExpiresTo);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productNumber", productNumber);
    query.setParameter("fromDate", from);
    query.setParameter("toDate", to);

    return query.getResultList();
  }

  public List<Product> findProductByCollectionNumber(
      String collectionNumber, String dateExpiresFrom,
      String dateExpiresTo) {

    TypedQuery<Product> query = em
        .createQuery(
            "SELECT p FROM Product p WHERE " +
            "p.collectedSample.collectionNumber = :collectionNumber and " +
            "((p.expiresOn is NULL) or " +
            " (p.expiresOn >= :fromDate and p.expiresOn <= :toDate)) and " +
            "p.isDeleted= :isDeleted",
            Product.class);

    Date from = getDateExpiresFromOrDefault(dateExpiresFrom);
    Date to = getDateExpiresToOrDefault(dateExpiresTo);
    System.out.println(from);
    System.out.println(to);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("collectionNumber", collectionNumber);
    query.setParameter("fromDate", from);
    query.setParameter("toDate", to);

    return query.getResultList();
  }

  public List<Product> findProductByProductTypes(
      List<String> productTypes, String dateExpiresFrom, String dateExpiresTo) {

    TypedQuery<Product> query = em
        .createQuery(
            "SELECT p FROM Product p WHERE " +
            "p.productType.productType IN (:productTypes) and " +
            "((p.expiresOn is NULL) or " +
            " (p.expiresOn >= :fromDate and p.expiresOn <= :toDate)) and " +
            "p.isDeleted= :isDeleted",
            Product.class);

    Date from = getDateExpiresFromOrDefault(dateExpiresFrom);
    Date to = getDateExpiresToOrDefault(dateExpiresTo);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productTypes", productTypes);
    query.setParameter("fromDate", from);
    query.setParameter("toDate", to);

    return query.getResultList();
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

  public Product updateProduct(Product product) {
    Product existingProduct = findProductById(product.getId());
    discardIfQuarantinedProduct(existingProduct);
    if (existingProduct == null) {
      return null;
    }
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
                 "p.isDeleted = :isDeleted",
                  Product.class);
    query.setParameter("productType", productRequest.getProductType());
    query.setParameter("today", today);
    query.setParameter("isAvailable", true);
    query.setParameter("isDeleted", false);

    List<Product> products = query.getResultList();
    List<MatchingProductViewModel> safeProducts = new ArrayList<MatchingProductViewModel>();
    for (Product product : products) {
      boolean isSafe = true;
      CollectedSample collectedSample = product.getCollectedSample();

      List<TestResult> results = collectedSample.getTestResults();

      String bloodAbo = "";
      String bloodRh = "";

      for (TestResult testResult : results) {
        BloodTest bloodTest = testResult.getBloodTest();
        String actualResult = testResult.getResult();

        System.out.println("name: " + bloodTest.getName());
        System.out.println("result: " + actualResult);
        if (bloodTest.getName().equals("Blood Rh")) {
          System.out.println("here");
          bloodRh = actualResult;
        }

        if (bloodTest.getName().equals("Blood ABO")) {
          bloodAbo = actualResult;
        }

        String correctResult = bloodTest.getCorrectResult();
        if (correctResult.isEmpty())
          continue;
        if (!correctResult.equals(actualResult)) {
          isSafe = false;
          break;
        }
      }

      String requestedAbo = productRequest.getBloodAbo().toString();
      String requestedRh = productRequest.getBloodRhd().toString(); 
      if (isSafe && bloodCrossmatch(bloodAbo, bloodRh, requestedAbo, requestedRh)) {
        BloodGroup bg = new BloodGroup(BloodAbo.valueOf(bloodAbo), BloodRhd.valueOf(bloodRh));
        safeProducts.add(new MatchingProductViewModel(product, bg));
      }
    }
    return safeProducts;
  }

  private boolean bloodCrossmatch(String abo1, String rhd1, String abo2, String rhd2) {
    System.out.println("matching " + abo1 + ", " + ", " + rhd1 + ", " + abo2 + ", " + rhd2);
    if (abo1.equals(abo2) && rhd1.equals(rhd2))
      return true;
    if (abo1.equals("O") && (rhd1.equals(rhd2) || rhd1.equals("NEGATIVE")))
      return true;
    return false;
  }
  
  public Product findSingleProductByProductNumber(String productNumber) {
    List<Product> products = findProductByProductNumber(productNumber, "", "");
    if (products != null && products.size() == 1)
      return products.get(0);
    return null;
  }

  public Map<String, Object> generateInventorySummaryFast() {
    Map<String, Object> inventory = new HashMap<String, Object>();
    // IMPORTANT: Distinct is necessary to avoid a cartesian product of test results and products from being returned
    // Also LEFT JOIN FETCH prevents the N+1 queries problem associated with Lazy Many-to-One joins
    TypedQuery<Product> q = em.createQuery(
                             "SELECT DISTINCT p from Product p LEFT JOIN FETCH p.collectedSample c LEFT JOIN FETCH c.testResults " +
    		                     "where p.isAvailable=:isAvailable AND p.isDeleted=:isDeleted AND p.isQuarantined=:isQuarantined",
    		                     Product.class);
    q.setParameter("isAvailable", true);
    q.setParameter("isQuarantined", false);
    q.setParameter("isDeleted", false);

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
      String bloodGroup = getBloodGroupForProduct(product).toString();
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
