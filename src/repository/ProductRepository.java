package repository;

import java.io.IOException;
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

import model.bloodtesting.TTIStatus;
import model.compatibility.CompatibilityResult;
import model.compatibility.CompatibilityTest;
import model.donation.Donation;
import model.product.Product;
import model.product.ProductStatus;
import model.productmovement.ProductStatusChange;
import model.productmovement.ProductStatusChangeReason;
import model.productmovement.ProductStatusChangeReasonCategory;
import model.productmovement.ProductStatusChangeType;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import model.request.Request;
import model.util.BloodGroup;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import controller.UtilController;
import repository.bloodtesting.BloodTypingStatus;
import utils.CustomDateFormatter;
import viewmodel.DonationViewModel;
import viewmodel.MatchingProductViewModel;
import backingform.ProductCombinationBackingForm;

import javax.persistence.PessimisticLockException;

import org.apache.commons.lang3.StringUtils;

@Repository
@Transactional
public class ProductRepository {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private ProductTypeRepository productTypeRepository;

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private UtilController utilController;

  /**
   * some fields like product status are cached internally.
   * must be called whenever any changes are made to rows related to the product.
   * eg. Test result update should update the product status.
   * @param product
   */
  public boolean updateProductInternalFields(Product product) {
    return updateProductStatus(product);
  }

  private boolean updateProductStatus(Product product) {

    // if a product has been explicitly discarded maintain that status.
    // if the product has been issued do not change its status.
    // suppose a product from a donation tested as safe was issued
    // then some additional tests were done for some reason and it was
    // discovered that the product was actually unsafe and it should not
    // have been issued then it should be easy to track down all products
    // created from that sample which were issued. By maintaining the status as
    // issued even if the product is unsafe we can search for all products created
    // from that donation and then look at which ones were already issued.
    // Conclusion is do not change the product status once it is marked as issued.
    // Similar reasoning for not changing USED status for a product. It should be
    // easy to track which used products were made from unsafe donations.
    // of course if the test results are not available or the donation is known
    // to be unsafe it should not have been issued in the first place.
    // In exceptional cases an admin can always delete this product and create a new one
    // if he wants to change the status to a new one.
    // once a product has been labeled as split it does not exist anymore so we just mark
    // it as SPLIT/PROCESSED. Even if the donation is found to be unsafe later it should not matter
    // as SPLIT/PROCESSED products are not allowed to be issued
    List<ProductStatus> statusNotToBeChanged =
        Arrays.asList(ProductStatus.DISCARDED, ProductStatus.ISSUED,
            ProductStatus.USED, ProductStatus.SPLIT, ProductStatus.PROCESSED);

    ProductStatus oldProductStatus = product.getStatus();
    
    // nothing to do if the product has any of these statuses
    if (product.getStatus() != null && statusNotToBeChanged.contains(product.getStatus()))
      return false;

    if (product.getDonation() == null)
      return false;
    Long donationId = product.getDonation().getId();
    Donation c = donationRepository.findDonationById(donationId);
    BloodTypingStatus bloodTypingStatus = c.getBloodTypingStatus();
    TTIStatus ttiStatus = c.getTTIStatus();

    ProductStatus newProductStatus = ProductStatus.QUARANTINED;
    if (bloodTypingStatus.equals(BloodTypingStatus.COMPLETE) &&
        ttiStatus.equals(TTIStatus.TTI_SAFE)) {
      newProductStatus = ProductStatus.AVAILABLE;
    }

    // just mark it as expired or unsafe
    // note that expired or unsafe status should override
    // available, quarantined status hence this check is done
    // later in the code
    if (product.getExpiresOn().before(new Date())) {
      newProductStatus = ProductStatus.EXPIRED;
    }

    if (ttiStatus.equals(TTIStatus.TTI_UNSAFE)) {
      newProductStatus = ProductStatus.UNSAFE;
    }

    if (!newProductStatus.equals(oldProductStatus)) {
      product.setStatus(newProductStatus);
      return true;
    }
    return false;
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
  
  public List<Product> findAnyProduct(String donationIdentificationNumber, List<Integer> productTypes, List<ProductStatus> status, 
		  Date donationDateFrom, Date donationDateTo, Map<String, Object> pagingParams){
	  	TypedQuery<Product> query;
	    String queryStr = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.donation WHERE " +     
	                      "p.isDeleted= :isDeleted ";
	    
	    if(status != null && !status.isEmpty()){
	    	queryStr += "AND p.status IN :status ";
	    }	
	    if(!StringUtils.isBlank(donationIdentificationNumber)){
	    	queryStr += "AND p.donation.donationIdentificationNumber = :donationIdentificationNumber ";
	    }
	    if(productTypes != null && !productTypes.isEmpty()){
	    	queryStr += "AND p.productType.id IN (:productTypeIds) ";
	    }	    
	    if(donationDateFrom != null){
	    	queryStr += "AND p.donation.donationDate >= :donationDateFrom ";
	    }
	    if(donationDateTo != null){
	    	queryStr += "AND p.donation.donationDate <= :donationDateTo ";
	    }
	    
	    if (pagingParams.containsKey("sortColumn")) {
	    	queryStr += " ORDER BY p." + pagingParams.get("sortColumn") + " " + pagingParams.get("sortDirection");
	    }
	
	    query = em.createQuery(queryStr, Product.class);
	    query.setParameter("isDeleted", Boolean.FALSE);
	    
	    if(status != null && !status.isEmpty()){
	    	query.setParameter("status", status);
	    }
	    if(!StringUtils.isBlank(donationIdentificationNumber)){
	    	query.setParameter("donationIdentificationNumber", donationIdentificationNumber);
	    }
	    if (productTypes != null && !productTypes.isEmpty()) {
	    	query.setParameter("productTypeIds", productTypes);
	    }
	    if(donationDateFrom != null){
	    	query.setParameter("donationDateFrom", donationDateFrom);
	    }
	    if(donationDateTo != null){
	    	query.setParameter("donationDateTo", donationDateTo);
	    }
	
	    int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
	    int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);
	
	    query.setFirstResult(start);
	    query.setMaxResults(length);
	    
	    return query.getResultList();
  }

  public List<Product> findProductByDonationIdentificationNumber(
      String donationIdentificationNumber, List<ProductStatus> status, Map<String, Object> pagingParams) {

    TypedQuery<Product> query;
    String queryStr = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.donation WHERE " +
                      "p.donation.donationIdentificationNumber = :donationIdentificationNumber AND " +
                      "p.status IN :status AND " +
                      "p.isDeleted= :isDeleted";

    String queryStrWithoutJoin = "SELECT p FROM Product p WHERE " +
        "p.donation.donationIdentificationNumber = :donationIdentificationNumber AND " +
        "p.status IN :status AND " +
        "p.isDeleted= :isDeleted";

    if (pagingParams.containsKey("sortColumn")) {
      queryStr += " ORDER BY p." + pagingParams.get("sortColumn") + " " + pagingParams.get("sortDirection");
    }

    query = em.createQuery(queryStr, Product.class);
    query.setParameter("status", status);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("donationIdentificationNumber", donationIdentificationNumber);

    int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
    int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);

    query.setFirstResult(start);
    query.setMaxResults(length);

    //return Arrays.asList(query.getResultList(), getResultCount(queryStrWithoutJoin, query));
    return query.getResultList();
  }
  
  public List<Product> findProductByProductTypes(
      List<Integer> productTypeIds, List<ProductStatus> status,
      Map<String, Object> pagingParams) {

    String queryStr = "SELECT p FROM Product p LEFT JOIN FETCH p.donation WHERE " +
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
    query.setParameter("status", status);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productTypeIds", productTypeIds);

    int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
    int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);

    query.setFirstResult(start);
    query.setMaxResults(length);

    //return Arrays.asList(query.getResultList(), getResultCount(queryStrWithoutJoin, query));
    return query.getResultList();
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
    String queryString = "SELECT p FROM Product p where p.isDeleted = :isDeleted and p.isIssued= :isIssued and p.createdOn > :minDate";
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

  public boolean isProductCreated(String donationIdentificationNumber) {
    String queryString = "SELECT p FROM Product p WHERE p.donationIdentificationNumber = :donationIdentificationNumber and p.isDeleted = :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<Product> products = query.setParameter("donationIdentificationNumber",
        donationIdentificationNumber).getResultList();
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
            "SELECT p FROM Product p WHERE  p.createdOn >= :fromDate and p.createdOn<= :toDate and p.isDeleted = :isDeleted",
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
    String queryString = "SELECT p FROM Product p where p.type = :productType and p.abo= :abo and p.rhd= :rhd and p.isDeleted = :isDeleted and p.isIssued= :isIssued and p.createdOn > :minDate";
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

  public Product findProductById(Long productId)throws NoResultException{
    String queryString = "SELECT p FROM Product p LEFT JOIN FETCH p.donation LEFT JOIN FETCH p.issuedTo where p.id = :productId AND p.isDeleted = :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productId", productId);
    Product product = null;
    product = query.getSingleResult();
    return product;
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

  public Product addProduct(Product product) {
    updateProductInternalFields(product);
    em.persist(product);
    //em.flush();
    em.refresh(product);
    return product;
  }

  public void deleteProduct(Long productId) throws IllegalArgumentException{
    Product existingProduct = findProductById(productId);
    existingProduct.setIsDeleted(Boolean.TRUE);
    em.merge(existingProduct);
    em.flush();
  }

  public List<MatchingProductViewModel> findMatchingProductsForRequest(Long requestId) {

    Date today = new Date();
    Request request = requestRepository.findRequestById(requestId);
    
    TypedQuery<Product> query = em.createQuery(
                 "SELECT p from Product p LEFT JOIN FETCH p.donation WHERE " +
                 "p.productType = :productType AND " +
                 "p.expiresOn >= :today AND " +
                 "p.status = :status AND " + 
                 "p.donation.ttiStatus = :ttiStatus AND " +
                 "((p.donation.bloodAbo = :bloodAbo AND p.donation.bloodRh = :bloodRh) OR " +
                 "(p.donation.bloodAbo = :bloodAboO AND p.donation.bloodRh = :bloodRhNeg)) AND " +
                 "p.isDeleted = :isDeleted " +
                 "ORDER BY p.expiresOn ASC",
                  Product.class);

    query.setParameter("productType", request.getProductType());
    query.setParameter("today", today);
    query.setParameter("status", ProductStatus.AVAILABLE);
    query.setParameter("ttiStatus", TTIStatus.TTI_SAFE);
    query.setParameter("bloodAbo", request.getPatientBloodAbo());
    query.setParameter("bloodRh", request.getPatientBloodRh());
    query.setParameter("bloodAboO", "O");
    query.setParameter("bloodRhNeg", "-");
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
      System.out.println("here");
      Long productId = product.getId();
      if (crossmatchTestMap.containsKey(productId))
        continue;
      matchingProducts.add(new MatchingProductViewModel(product));
    }

    return matchingProducts;
  }
  
  public Map<String, Object> generateInventorySummaryFast(List<String> status, List<Long> panelIds) {
    Map<String, Object> inventory = new HashMap<String, Object>();
    // IMPORTANT: Distinct is necessary to avoid a cartesian product of test results and products from being returned
    // Also LEFT JOIN FETCH prevents the N+1 queries problem associated with Lazy Many-to-One joins
    TypedQuery<Product> q = em.createQuery(
                             "SELECT DISTINCT p from Product p " +
                             "WHERE p.status IN :status AND " +
                             "p.donation.donorPanel.id IN (:panelIds) AND " +
                             "p.isDeleted=:isDeleted",
                             Product.class);
    List<ProductStatus> productStatus = new ArrayList<ProductStatus>();
    for (String s : status) {
      productStatus.add(ProductStatus.lookup(s));
    }
    q.setParameter("status", productStatus);
    q.setParameter("panelIds", panelIds);
    q.setParameter("isDeleted", false);
//    q.setParameter("expiresOn", DateUtils.round(new Date(), Calendar.DATE));

    TypedQuery<ProductType> productTypeQuery = em.createQuery("SELECT pt FROM ProductType pt", ProductType.class);

    for (ProductType productType : productTypeQuery.getResultList()) {
      Map<String, Map<Long, Long>> inventoryByBloodGroup = new HashMap<String, Map<Long, Long>>();

      inventoryByBloodGroup.put("", getMapWithNumDaysWindows());
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
      @SuppressWarnings("unchecked")
      Map<String, Map<Long, Long>> inventoryByBloodGroup = (Map<String, Map<Long, Long>>) inventory.get(productType);
      DonationViewModel donation;
      donation = new DonationViewModel(product.getDonation());
      Map<Long, Long> numDayMap = inventoryByBloodGroup.get(donation.getBloodGroup());
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

  public void addAllProducts(List<Product> products) {
    for (Product p : products) {
      updateProductInternalFields(p);
      em.persist(p);
    }
    em.flush();
  }

  public void updateQuarantineStatus() {
    String queryString = "SELECT p FROM Product p LEFT JOIN FETCH p.donation where p.status is NULL AND p.isDeleted = :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<Product> products = query.getResultList();
    //System.out.println("number of products to update: " + products.size());
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
    statusChange.setChangedBy(utilController.getCurrentUser());
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
    //System.out.println("Number of rows updated: " + numUpdated);
  }

  public List<Product> getProductsFromProductIds(String[] productIds) {
    List<Product> products = new ArrayList<Product>();
    for (String productId : productIds) {
      products.add(findProductById(Long.parseLong(productId)));
    }
    return products;
  }

  public Map<String, Map<Long, Long>> findNumberOfDiscardedProducts(
      Date donationDateFrom, Date donationDateTo, String aggregationCriteria,
      List<String> panels, List<String> bloodGroups) throws ParseException {

    List<Long> panelIds = new ArrayList<Long>();
    if (panels != null) {
      for (String panel : panels) {
    	panelIds.add(Long.parseLong(panel));
      }
    } else {
      panelIds.add((long)-1);
    }

    Map<String, Map<Long, Long>> resultMap = new HashMap<String, Map<Long,Long>>();
    for (String bloodGroup : bloodGroups) {
      resultMap.put(bloodGroup, new HashMap<Long, Long>());
    }

    TypedQuery<Object[]> query = em.createQuery(
        "SELECT count(p), p.donation.donationDate, p.donation.bloodAbo, " +
        "p.donation.bloodRh FROM Product p WHERE " +
        "p.donation.donorPanel.id IN (:panelIds) AND " +
        "p.donation.donationDate BETWEEN :donationDateFrom AND :donationDateTo AND " +
        "p.status IN (:discardedStatuses) AND " +
        "(p.isDeleted= :isDeleted) " +
        "GROUP BY bloodAbo, bloodRh, donationDate", Object[].class);

    query.setParameter("panelIds", panelIds);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("discardedStatuses",
                       Arrays.asList(ProductStatus.DISCARDED,
                                     ProductStatus.UNSAFE,
                                     ProductStatus.EXPIRED));

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
      Date lowerDate =  resultDateFormat.parse(resultDateFormat.format(donationDateFrom));
      Date upperDate =  resultDateFormat.parse(resultDateFormat.format(donationDateTo));
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

  public Map<String, Map<Long, Long>> findNumberOfIssuedProducts(
      Date donationDateFrom, Date donationDateTo, String aggregationCriteria,
      List<String> panels, List<String> bloodGroups) throws ParseException {

	List<Long> panelIds = new ArrayList<Long>();
    if (panels != null) {
      for (String panel : panels) {
    	panelIds.add(Long.parseLong(panel));
      }
    } else {
      panelIds.add((long)-1);
    }

    Map<String, Map<Long, Long>> resultMap = new HashMap<String, Map<Long,Long>>();
    for (String bloodGroup : bloodGroups) {
      resultMap.put(bloodGroup, new HashMap<Long, Long>());
    }

    TypedQuery<Object[]> query = em.createQuery(
        "SELECT count(p), p.issuedOn, p.donation.bloodAbo, " +
        "p.donation.bloodRh FROM Product p WHERE " +
        "p.donation.donorPanel.id IN (:panelIds) AND " +
        "p.donation.donationDate BETWEEN :donationDateFrom AND :donationDateTo AND " +
        "p.status=:issuedStatus AND " +
        "(p.isDeleted= :isDeleted) " +
        "GROUP BY bloodAbo, bloodRh, donationDate", Object[].class);

    query.setParameter("panelIds", panelIds);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("issuedStatus", ProductStatus.ISSUED);

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

  public Product findProduct(String donationIdentificationNumber, String productTypeId) {
    String queryStr = "SELECT p from Product p WHERE " +
                      "p.donation.donationIdentificationNumber = :donationIdentificationNumber AND " +
                      "p.productType.id = :productTypeId";
    TypedQuery<Product> query = em.createQuery(queryStr, Product.class);
    query.setParameter("donationIdentificationNumber", donationIdentificationNumber);
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
    statusChange.setChangedBy(utilController.getCurrentUser());
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

  public List<Product> findProductsByDonationIdentificationNumber(String donationIdentificationNumber) {
    String queryStr = "SELECT p from Product p WHERE " +
        "p.donation.donationIdentificationNumber=:donationIdentificationNumber AND p.isDeleted=:isDeleted";
    TypedQuery<Product> query = em.createQuery(queryStr, Product.class);
    query.setParameter("donationIdentificationNumber", donationIdentificationNumber);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<Product> addProductCombination(ProductCombinationBackingForm form) throws PessimisticLockException, ParseException {
    List<Product> products = new ArrayList<Product>();
    String expiresOn = form.getExpiresOn();
    ObjectMapper mapper = new ObjectMapper();

    Map<String, String> expiryDateByProductType = null;
      try {
          expiryDateByProductType = mapper.readValue(expiresOn, HashMap.class);
      } catch (IOException ex) {
          ex.printStackTrace();
      }

    ProductTypeCombination productTypeCombination;
    productTypeCombination = productTypeRepository.getProductTypeCombinationById(Integer.parseInt(form.getProductTypeCombination()));
    for (ProductType productType : productTypeCombination.getProductTypes()) {
      Product product = new Product();
      product.setDonation(form.getDonation());
      product.setProductType(productType);
      product.setCreatedOn(form.getProduct().getCreatedOn());
      String expiryDateStr = expiryDateByProductType.get(productType.getId().toString());
      product.setExpiresOn(CustomDateFormatter.getDateTimeFromString(expiryDateStr));
      product.setIsDeleted(false);
      updateProductInternalFields(product);
      em.persist(product);
      em.flush();
      em.refresh(product);
      products.add(product);
    }

    return products;
  }

  public boolean splitProduct(Long productId, Integer numProductsAfterSplitting) {

    Product product = findProduct(productId);
    if (product == null || product.getStatus().equals(ProductStatus.SPLIT))
      return false;

    ProductType pediProductType = product.getProductType().getPediProductType();
    if (pediProductType == null) {
      return false;
    }

    char nextSubdivisionCode = 'A';
    for (int i = 0; i < numProductsAfterSplitting; ++i) {
      Product newProduct = new Product();
      // just set the id temporarily before copying all the fields
      newProduct.setId(productId);
      newProduct.copy(product);
      newProduct.setId(null);
      newProduct.setProductType(pediProductType);
      newProduct.setSubdivisionCode("" + nextSubdivisionCode);
      newProduct.setParentProduct(product);
      newProduct.setIsDeleted(false);
      updateProductInternalFields(newProduct);
      em.persist(newProduct);
      // Assuming we do not split into more than 26 products this should be fine
      nextSubdivisionCode++;
    }

    product.setStatus(ProductStatus.SPLIT);
    ProductStatusChange statusChange = new ProductStatusChange();
    statusChange.setStatusChangeType(ProductStatusChangeType.SPLIT);
    statusChange.setNewStatus(ProductStatus.SPLIT);

    String queryStr = "SELECT p FROM ProductStatusChangeReason p WHERE " +
    		"p.category=:category AND p.isDeleted=:isDeleted";
    TypedQuery<ProductStatusChangeReason> query = em.createQuery(queryStr,
        ProductStatusChangeReason.class);
    query.setParameter("category", ProductStatusChangeReasonCategory.SPLIT);
    query.setParameter("isDeleted", false);
    List<ProductStatusChangeReason> productStatusChangeReasons = query.getResultList();
    statusChange.setStatusChangedOn(new Date());
    // expect only one product status change reason
    statusChange.setStatusChangeReason(productStatusChangeReasons.get(0));
    statusChange.setStatusChangeReasonText("");
    statusChange.setChangedBy(utilController.getCurrentUser());
    if (product.getStatusChanges() == null)
      product.setStatusChanges(new ArrayList<ProductStatusChange>());
    product.getStatusChanges().add(statusChange);
    statusChange.setProduct(product);
    em.persist(statusChange);
    em.merge(product);
    return true;
  }

  public ProductType findProductTypeBySelectedProductType(int productTypeId) throws NoResultException{
    String queryString = "SELECT p FROM ProductType p where p.id = :productTypeId";
    TypedQuery<ProductType> query = em.createQuery(queryString, ProductType.class);
    query.setParameter("productTypeId", productTypeId);
    ProductType productType =  productType = query.getSingleResult();
    return productType;
  }
  
  public ProductType findProductTypeByProductTypeName(String productTypeName) throws NoResultException{
    String queryString = "SELECT p FROM ProductType p where p.productType = :productTypeName";
    TypedQuery<ProductType> query = em.createQuery(queryString, ProductType.class);
    query.setParameter("productTypeName", productTypeName);
    ProductType productType = productType = query.getSingleResult();
    return productType;
  }
  
  public void setProductStatusToProcessed(long productId) throws NoResultException {
  	 String queryString = "SELECT p FROM Product p where p.id = :productId";
     TypedQuery<Product> query = em.createQuery(queryString, Product.class);
     query.setParameter("productId", productId);
     Product product = null;
     	product = query.getSingleResult();
     	product.setStatus(ProductStatus.PROCESSED);
     	em.merge(product);
  }
}
