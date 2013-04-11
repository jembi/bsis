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

import model.collectedsample.CollectedSample;
import model.compatibility.CompatibilityResult;
import model.compatibility.CompatibilityTest;
import model.product.Product;
import model.product.ProductStatus;
import model.productmovement.ProductStatusChange;
import model.productmovement.ProductStatusChangeReason;
import model.productmovement.ProductStatusChangeType;
import model.producttype.ProductType;
import model.request.Request;
import model.testresults.TTIStatus;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRh;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import repository.bloodtesting.BloodTypingStatus;
import viewmodel.CollectedSampleViewModel;
import viewmodel.MatchingProductViewModel;
import filter.UserInfoAddToThreadFilter;

@Repository
@Transactional
public class ProductRepository {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  public RequestRepository requestRepository;

  /**
   * some fields like product status are cached internally.
   * must be called whenever any changes are made to rows related to the product.
   * eg. Test result update should update the product status.
   * @param product
   */
  public void updateProductInternalFields(Product product) {
    updateProductStatus(product);
  }

  private void updateProductStatus(Product product) {

    // if a product has been explicitly discarded maintain that status.
    // if the product has been issued do not change its status.
    // suppose a product from a collection tested as safe was issued
    // then some additional tests were done for some reason and it was
    // discovered that the product was actually unsafe and it should not
    // have been issued then it should be easy to track down all products
    // created from that sample which were issued. By maintaining the status as
    // issued even if the product is unsafe we can search for all products created
    // from that collection and then look at which ones were already issued.
    // Conclusion is do not change the product status once it is marked as issued.
    // Similar reasoning for not changing USED status for a product. It should be
    // easy to track which used products were made from unsafe collected samples.
    // of course if the test results are not available or the collection is known
    // to be unsafe it should not have been issued in the first place.
    // In exceptional cases an admin can always delete this product and create a new one
    // if he wants to change the status to a new one.
    List<ProductStatus> statusNotToBeChanged =
        Arrays.asList(ProductStatus.DISCARDED, ProductStatus.ISSUED,
            ProductStatus.USED);
    
    // nothing to do if the product has any of these statuses
    if (product.getStatus() != null && statusNotToBeChanged.contains(product.getStatus()))
      return;

    if (product.getCollectedSample() == null)
      return;
    Long collectedSampleId = product.getCollectedSample().getId();
    CollectedSample c = collectedSampleRepository.findCollectedSampleById(collectedSampleId);
    BloodTypingStatus bloodTypingStatus = c.getBloodTypingStatus();
    TTIStatus ttiStatus = c.getTTIStatus();

    ProductStatus productStatus = ProductStatus.QUARANTINED;
    if (bloodTypingStatus.equals(BloodTypingStatus.COMPLETE) &&
        ttiStatus.equals(TTIStatus.TTI_SAFE)) {
      productStatus = ProductStatus.AVAILABLE;
    }

    // just mark it as expired or unsafe
    // note that expired or unsafe status should override
    // available, quarantined status hence this check is done
    // later in the code
    if (product.getExpiresOn().before(new Date())) {
      productStatus = ProductStatus.EXPIRED;
    }

    if (ttiStatus.equals(TTIStatus.TTI_UNSAFE)) {
      productStatus = ProductStatus.UNSAFE;
    }


    product.setStatus(productStatus);
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

  public List<Object> findProductByCollectionNumber(
      String collectionNumber, List<String> status, Map<String, Object> pagingParams) {

    TypedQuery<Product> query;
    String queryStr = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.collectedSample WHERE " +
                      "p.collectedSample.collectionNumber = :collectionNumber AND " +
                      "p.status IN :status AND " +
                      "p.isDeleted= :isDeleted";

    String queryStrWithoutJoin = "SELECT p FROM Product p WHERE " +
        "p.collectedSample.collectionNumber = :collectionNumber AND " +
        "p.status IN :status AND " +
        "p.isDeleted= :isDeleted";

    if (pagingParams.containsKey("sortColumn")) {
      queryStr += " ORDER BY p." + pagingParams.get("sortColumn") + " " + pagingParams.get("sortDirection");
    }

    query = em.createQuery(queryStr, Product.class);
    query.setParameter("status", statusStringToProductStatus(status));
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("collectionNumber", collectionNumber);

    int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
    int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);

    query.setFirstResult(start);
    query.setMaxResults(length);

    return Arrays.asList(query.getResultList(), getResultCount(queryStrWithoutJoin, query));
  }

  private List<ProductStatus> statusStringToProductStatus(List<String> statusList) {
    List<ProductStatus> productStatusList = new ArrayList<ProductStatus>();
    if (statusList != null) {
      for (String status : statusList) {
        productStatusList.add(ProductStatus.lookup(status));
      }
    }
    return productStatusList;
  }
  
  public List<Object> findProductByProductTypes(
      List<Integer> productTypeIds, List<String> status,
      Map<String, Object> pagingParams) {

    String queryStr = "SELECT p FROM Product p LEFT JOIN FETCH p.collectedSample WHERE " +
        "p.productType.id IN (:productTypeIds) AND " +
        "p.status IN :status AND " +
        "p.isDeleted= :isDeleted";

    String queryStrWithoutJoin = "SELECT p FROM Product p WHERE " +
        "p.productType.id IN (:productTypeIds) AND " +
        "p.status IN :status AND " +
        "p.isDeleted= :isDeleted";


    if (pagingParams.containsKey("sortColumn")) {
      queryStr += " ORDER BY p." + pagingParams.get("sortColumn") + " " + pagingParams.get("sortDirection");
    }

    TypedQuery<Product> query = em.createQuery(queryStr, Product.class);
    query.setParameter("status", statusStringToProductStatus(status));
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productTypeIds", productTypeIds);

    int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
    int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);

    query.setFirstResult(start);
    query.setMaxResults(length);

    return Arrays.asList(query.getResultList(), getResultCount(queryStrWithoutJoin, query));
  }

  public List<Object> findProductByProductNumber(
      String productNumber, List<String> status, Map<String, Object> pagingParams) {

    String queryStr = "SELECT p FROM Product p WHERE p.productNumber = :productNumber AND " +
                      "p.status IN :status AND " +
    		              "p.isDeleted=:isDeleted";

    TypedQuery<Product> query;
    if (pagingParams.containsKey("sortColumn")) {
      queryStr += " ORDER BY " + pagingParams.get("sortColumn") + " " + pagingParams.get("sortDirection");
    }

    query = em.createQuery(queryStr, Product.class);
    query.setParameter("status", statusStringToProductStatus(status));
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productNumber", productNumber);

    int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
    int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);

    query.setFirstResult(start);
    query.setMaxResults(length);

    return Arrays.asList(query.getResultList(), getResultCount(queryStr, query));
  }

  private Long getResultCount(String queryStr, Query query) {
    String countQueryStr = queryStr.replaceFirst("SELECT p", "SELECT COUNT(p)");
    TypedQuery<Long> countQuery = em.createQuery(countQueryStr, Long.class);
    for (Parameter<?> parameter : query.getParameters()) {
      countQuery.setParameter(parameter.getName(), query.getParameterValue(parameter));
    }
    return countQuery.getSingleResult().longValue();
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
    String queryString = "SELECT p FROM Product p LEFT JOIN FETCH p.collectedSample LEFT JOIN FETCH p.issuedTo where p.id = :productId AND p.isDeleted = :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productId", productId);
    return query.getSingleResult();
  }

  public List<Product> findAnyProductMatching(String productNumber,
      String collectionNumber, List<ProductType> types, List<String> availability) {

    return findAnyProductMatching(productNumber, collectionNumber,
        Arrays.asList(BloodAbo.A, BloodAbo.B, BloodAbo.O, BloodAbo.AB),
        Arrays.asList(BloodRh.POSITIVE, BloodRh.NEGATIVE), types, availability);
  }

  public List<Product> findAnyProductMatching(String productNumber,
      String collectionNumber, List<BloodAbo> bloodAbo, List<BloodRh> bloodRh,
      List<ProductType> types, List<String> availability) {

    TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE "
        + "(p.productNumber = :productNumber OR "
        + "p.collectionNumber = :collectionNumber "
        + "OR p.type IN (:types)) AND (p.isIssued IN (:isIssued)) AND "
        + "p.abo IN (:bloodAbo) AND p.rhd IN (:bloodRh) AND "
        + "(p.isDeleted= :isDeleted)", Product.class);

    query.setParameter("isDeleted", Boolean.FALSE);
    String productNo = ((productNumber == null) ? "" : productNumber);
    query.setParameter("productNumber", productNo);
    String collectionNo = ((collectionNumber == null) ? "" : collectionNumber);
    query.setParameter("collectionNumber", collectionNo);
    query.setParameter("bloodAbo", bloodAbo);
    query.setParameter("bloodRh", bloodRh);
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

  public Product updateProduct(Product product) {
    Product existingProduct = findProductById(product.getId());
    if (existingProduct == null) {
      return null;
    }
    existingProduct.copy(product);
    updateProductInternalFields(existingProduct);
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

  public Product addProduct(Product product) {
    updateProductInternalFields(product);
    em.persist(product);
    em.flush();
    em.refresh(product);
    return product;
  }

  public void deleteProduct(Long productId) {
    Product existingProduct = findProductById(productId);
    existingProduct.setIsDeleted(Boolean.TRUE);
    em.merge(existingProduct);
    em.flush();
  }

  public List<MatchingProductViewModel> findMatchingProductsForRequest(Long requestId) {
    Date today = new Date();

    Request request = requestRepository.findRequestById(requestId);
    
    TypedQuery<Product> query = em.createQuery(
                 "SELECT p from Product p LEFT JOIN FETCH p.collectedSample WHERE " +
                 "p.productType = :productType AND " +
                 "p.expiresOn >= :today AND " +
                 "p.status = :status AND " +
                 "p.collectedSample.testedStatus = :testedStatus AND " +
                 "((p.bloodAbo = :bloodAbo AND p.bloodRh = :bloodRh) OR " +
                 "(p.bloodAbo = :bloodAboO AND p.bloodRh = :bloodRhNeg)) AND " +
                 "p.isDeleted = :isDeleted " +
                 "ORDER BY p.expiresOn ASC",
                  Product.class);
    query.setParameter("productType", request.getProductType());
    query.setParameter("today", today);
    query.setParameter("status", ProductStatus.AVAILABLE);
    query.setParameter("ttiStatus", TTIStatus.TTI_SAFE);
    query.setParameter("bloodAbo", request.getPatientBloodAbo());
    query.setParameter("bloodAboO", BloodAbo.O);
    query.setParameter("bloodRhNeg", BloodRh.NEGATIVE);
    query.setParameter("bloodRh", request.getPatientBloodRh());
    query.setParameter("isDeleted", false);

    TypedQuery<CompatibilityTest> crossmatchQuery = em.createQuery(
        "SELECT ct from CompatibilityTest ct where ct.forRequest.id=:forRequestId AND " +
        "ct.testedProduct.status = :testedProductStatus AND " +
        "isDeleted=:isDeleted", CompatibilityTest.class);

    crossmatchQuery.setParameter("forRequestId", requestId);
    crossmatchQuery.setParameter("testedProductStatus", ProductStatus.AVAILABLE);
    crossmatchQuery.setParameter("isDeleted", false);

    List<CompatibilityTest> crossmatchTests = crossmatchQuery.getResultList();
    List<MatchingProductViewModel> matchingProducts = new ArrayList<MatchingProductViewModel>();

    Map<Long, CompatibilityTest> crossmatchTestMap = new HashMap<Long, CompatibilityTest>();
    for (CompatibilityTest crossmatchTest : crossmatchTests) {
      Product product = crossmatchTest.getTestedProduct();
      if (product == null)
        continue;
      crossmatchTestMap.put(product.getId(), crossmatchTest);
      if (!crossmatchTest.getCompatibilityResult().equals(CompatibilityResult.NOT_COMPATIBLE))
        matchingProducts.add(new MatchingProductViewModel(product, crossmatchTest));
    }

    for (Product product : query.getResultList()) {
      Long productId = product.getId();
      if (crossmatchTestMap.containsKey(productId))
        continue;
      matchingProducts.add(new MatchingProductViewModel(product));
    }

    return matchingProducts;
  }
  
  public Product findSingleProductByProductNumber(String productNumber) {
    Product product = findProductByProductNumber(productNumber);
    return product;
  }

  public Map<String, Object> generateInventorySummaryFast(List<String> status, List<Long> centerIds) {
    Map<String, Object> inventory = new HashMap<String, Object>();
    // IMPORTANT: Distinct is necessary to avoid a cartesian product of test results and products from being returned
    // Also LEFT JOIN FETCH prevents the N+1 queries problem associated with Lazy Many-to-One joins
    TypedQuery<Product> q = em.createQuery(
                             "SELECT DISTINCT p from Product p " +
                             "WHERE p.status IN :status AND " +
                             "p.collectedSample.collectionCenter.id IN (:collectionCenterIds) AND " +
                             "p.isDeleted=:isDeleted",
                             Product.class);
    List<ProductStatus> productStatus = new ArrayList<ProductStatus>();
    for (String s : status) {
      productStatus.add(ProductStatus.lookup(s));
    }
    q.setParameter("status", productStatus);
    q.setParameter("collectionCenterIds", centerIds);
    q.setParameter("isDeleted", false);
//    q.setParameter("expiresOn", DateUtils.round(new Date(), Calendar.DATE));

    TypedQuery<ProductType> productTypeQuery = em.createQuery("SELECT pt FROM ProductType pt", ProductType.class);

    for (ProductType productType : productTypeQuery.getResultList()) {
      Map<String, Map<Long, Long>> inventoryByBloodGroup = new HashMap<String, Map<Long, Long>>();

      inventoryByBloodGroup.put("Unknown", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("A+", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("B+", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("AB+", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("O+", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("A-", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("B-", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("AB-", getMapWithNumDaysWindows());
      inventoryByBloodGroup.put("O-", getMapWithNumDaysWindows());

      inventory.put(productType.getProductType(), inventoryByBloodGroup);
    }

    DateTime today = new DateTime();
    for (Product product : q.getResultList()) {
      String productType = product.getProductType().getProductType();
      Map<String, Map<Long, Long>> inventoryByBloodGroup = (Map<String, Map<Long, Long>>) inventory.get(productType);
      CollectedSampleViewModel collectedSample;
      collectedSample = new CollectedSampleViewModel(product.getCollectedSample());
      Map<Long, Long> numDayMap = inventoryByBloodGroup.get(collectedSample.getBloodGroup());
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

//    for (TestResult t : c.getTestResults()) {
//      String testName = t.getBloodTest().getName();
//      if (testName.equals("Blood ABO"))
//        abo = t.getResult();
//      else if (testName.equals("Blood Rh"))
//        rh = t.getResult();
//    }
//
    if (abo == null || rh == null) {
      return new BloodGroup(BloodAbo.Unknown, BloodRh.Unknown);
    }

    return new BloodGroup(BloodAbo.valueOf(abo), BloodRh.valueOf(rh));
  }

  public BloodGroup getBloodGroupForProduct(Long productId) {
    Product product = findProductById(productId);
    return getBloodGroupForProduct(product);
  }

  public void addAllProducts(List<Product> products) {
    for (Product p : products) {
      updateProductInternalFields(p);
      em.persist(p);
    }
    em.flush();
  }

  public void updateQuarantineStatus() {
    String queryString = "SELECT p FROM Product p LEFT JOIN FETCH p.collectedSample where p.status is NULL AND p.isDeleted = :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<Product> products = query.getResultList();
    System.out.println("number of products to update: " + products.size());
    for (Product product : products) {
      updateProductInternalFields(product);
      em.merge(product);
    }
    em.flush();
  }

  public void discardProduct(Long productId,
      ProductStatusChangeReason discardReason,
      String discardReasonText) {
    Product existingProduct = findProductById(productId);
    existingProduct.setStatus(ProductStatus.DISCARDED);
    existingProduct.setDiscardedOn(new Date());
    ProductStatusChange statusChange = new ProductStatusChange();
    statusChange.setStatusChangeType(ProductStatusChangeType.DISCARDED);
    statusChange.setNewStatus(ProductStatus.DISCARDED);
    statusChange.setStatusChangedOn(new Date());
    statusChange.setStatusChangeReason(discardReason);
    statusChange.setStatusChangeReasonText(discardReasonText);
    statusChange.setChangedBy(UserInfoAddToThreadFilter.threadLocal.get());
    if (existingProduct.getStatusChanges() == null)
      existingProduct.setStatusChanges(new ArrayList<ProductStatusChange>());
    existingProduct.getStatusChanges().add(statusChange);
    statusChange.setProduct(existingProduct);
    em.persist(statusChange);
    em.merge(existingProduct);
    em.flush();
  }

  public void updateExpiryStatus() {
    String updateExpiryQuery = "UPDATE Product p SET p.status=:status WHERE " +
    		                       "p.status=:availableStatus AND " +
    		                       "p.expiresOn < :today";
    Query query = em.createQuery(updateExpiryQuery);
    query.setParameter("status", ProductStatus.EXPIRED);
    query.setParameter("availableStatus", ProductStatus.AVAILABLE);
    query.setParameter("today", new Date());
    int numUpdated = query.executeUpdate();
    System.out.println("Number of rows updated: " + numUpdated);
  }

  public List<Product> getProductsFromProductIds(String[] productIds) {
    List<Product> products = new ArrayList<Product>();
    for (String productId : productIds) {
      products.add(findProductById(Long.parseLong(productId)));
    }
    return products;
  }

  public Map<String, Map<Long, Long>> findNumberOfDiscardedProducts(
      Date dateCollectedFrom, Date dateCollectedTo, String aggregationCriteria,
      List<String> centers, List<String> sites, List<String> bloodGroups) {

    List<Long> centerIds = new ArrayList<Long>();
    if (centers != null) {
      for (String center : centers) {
        centerIds.add(Long.parseLong(center));
      }
    } else {
      centerIds.add((long)-1);
    }

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
        "SELECT count(p), p.collectedSample.collectedOn, p.collectedSample.bloodAbo, " +
        "p.collectedSample.bloodRh FROM Product p WHERE " +
        "p.collectedSample.collectionCenter.id IN (:centerIds) AND " +
        "p.collectedSample.collectionSite.id IN (:siteIds) AND " +
        "p.collectedSample.collectedOn BETWEEN :dateCollectedFrom AND :dateCollectedTo AND " +
        "p.status IN (:discardedStatuses) AND " +
        "(p.isDeleted= :isDeleted) " +
        "GROUP BY bloodAbo, bloodRh, collectedOn", Object[].class);

    query.setParameter("centerIds", centerIds);
    query.setParameter("siteIds", siteIds);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("discardedStatuses",
                       Arrays.asList(ProductStatus.DISCARDED,
                                     ProductStatus.UNSAFE,
                                     ProductStatus.EXPIRED));

    query.setParameter("dateCollectedFrom", dateCollectedFrom);
    query.setParameter("dateCollectedTo", dateCollectedTo);

    DateFormat resultDateFormat = new SimpleDateFormat("MM/dd/yyyy");
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
        lowerDate = resultDateFormat.parse(resultDateFormat.format(dateCollectedFrom));
        upperDate = resultDateFormat.parse(resultDateFormat.format(dateCollectedTo));
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
      BloodAbo bloodAbo = (BloodAbo) result[2];
      BloodRh bloodRh = (BloodRh) result[3];
      BloodGroup bloodGroup = new BloodGroup(bloodAbo, bloodRh);
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

  public Map<String, Map<Long, Long>> findNumberOfIssuedProducts(
      Date dateCollectedFrom, Date dateCollectedTo, String aggregationCriteria,
      List<String> centers, List<String> sites, List<String> bloodGroups) {

    List<Long> centerIds = new ArrayList<Long>();
    if (centers != null) {
      for (String center : centers) {
        centerIds.add(Long.parseLong(center));
      }
    } else {
      centerIds.add((long)-1);
    }

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
        "SELECT count(p), p.issuedOn, p.collectedSample.bloodAbo, " +
        "p.collectedSample.bloodRh FROM Product p WHERE " +
        "p.collectedSample.collectionCenter.id IN (:centerIds) AND " +
        "p.collectedSample.collectionSite.id IN (:siteIds) AND " +
        "p.collectedSample.collectedOn BETWEEN :dateCollectedFrom AND :dateCollectedTo AND " +
        "p.status=:issuedStatus AND " +
        "(p.isDeleted= :isDeleted) " +
        "GROUP BY bloodAbo, bloodRh, collectedOn", Object[].class);

    query.setParameter("centerIds", centerIds);
    query.setParameter("siteIds", siteIds);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("issuedStatus", ProductStatus.ISSUED);

    query.setParameter("dateCollectedFrom", dateCollectedFrom);
    query.setParameter("dateCollectedTo", dateCollectedTo);

    DateFormat resultDateFormat = new SimpleDateFormat("MM/dd/yyyy");
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
        lowerDate = resultDateFormat.parse(resultDateFormat.format(dateCollectedFrom));
        upperDate = resultDateFormat.parse(resultDateFormat.format(dateCollectedTo));
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
      BloodAbo bloodAbo = (BloodAbo) result[2];
      BloodRh bloodRh = (BloodRh) result[3];
      BloodGroup bloodGroup = new BloodGroup(bloodAbo, bloodRh);
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

  public Product findProduct(String collectionNumber, String productTypeId) {
    String queryStr = "SELECT p from Product p WHERE " +
    		              "p.collectedSample.collectionNumber = :collectionNumber AND " +
    		              "p.productType.id = :productTypeId";
    TypedQuery<Product> query = em.createQuery(queryStr, Product.class);
    query.setParameter("collectionNumber", collectionNumber);
    query.setParameter("productTypeId", Integer.parseInt(productTypeId));
    Product product = null;
    try {
      product = query.getSingleResult();
    } catch (NoResultException ex) {
      ex.printStackTrace();
    }
    return product;
  }

  public void returnProduct(Long productId,
      ProductStatusChangeReason returnReason, String returnReasonText) {
    Product existingProduct = findProductById(productId);
    updateProductStatus(existingProduct);
    ProductStatusChange statusChange = new ProductStatusChange();
    statusChange.setStatusChangedOn(new Date());
    statusChange.setStatusChangeType(ProductStatusChangeType.RETURNED);
    statusChange.setStatusChangeReason(returnReason);
    statusChange.setNewStatus(existingProduct.getStatus());
    statusChange.setStatusChangeReasonText(returnReasonText);
    statusChange.setChangedBy(UserInfoAddToThreadFilter.threadLocal.get());
    if (existingProduct.getStatusChanges() == null)
      existingProduct.setStatusChanges(new ArrayList<ProductStatusChange>());
    existingProduct.getStatusChanges().add(statusChange);
    statusChange.setProduct(existingProduct);
    em.persist(statusChange);
    em.merge(existingProduct);
    em.flush();
  }

  public List<ProductStatusChange> getProductStatusChanges(Product product) {
    String queryStr = "SELECT p FROM ProductStatusChange p WHERE " +
    		"p.product.id=:productId";
    TypedQuery<ProductStatusChange> query = em.createQuery(queryStr, ProductStatusChange.class);
    query.setParameter("productId", product.getId());
    List<ProductStatusChange> statusChanges = query.getResultList();
    return statusChanges;
  }
}
