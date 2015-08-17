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
import javax.persistence.PessimisticLockException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.bloodtesting.TTIStatus;
import model.compatibility.CompatibilityResult;
import model.compatibility.CompatibilityTest;
import model.component.Component;
import model.component.ProductStatus;
import model.donation.Donation;
import model.productmovement.ProductStatusChange;
import model.productmovement.ProductStatusChangeReason;
import model.productmovement.ProductStatusChangeReasonCategory;
import model.productmovement.ProductStatusChangeType;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import model.request.Request;
import model.util.BloodGroup;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import repository.bloodtesting.BloodTypingStatus;
import utils.CustomDateFormatter;
import viewmodel.DonationViewModel;
import viewmodel.MatchingComponentViewModel;
import backingform.ComponentCombinationBackingForm;

import com.fasterxml.jackson.databind.ObjectMapper;

import controller.UtilController;

@Repository
@Transactional
public class ComponentRepository {

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
   * must be called whenever any changes are made to rows related to the component.
   * eg. Test result update should update the product status.
   * @param component
   */
  public boolean updateComponentInternalFields(Component component) {
    return updateComponentStatus(component);
  }

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
    // Conclusion is do not change the product status once it is marked as issued.
    // Similar reasoning for not changing USED status for a component. It should be
    // easy to track which used components were made from unsafe donations.
    // of course if the test results are not available or the donation is known
    // to be unsafe it should not have been issued in the first place.
    // In exceptional cases an admin can always delete this component and create a new one
    // if he wants to change the status to a new one.
    // once a component has been labeled as split it does not exist anymore so we just mark
    // it as SPLIT/PROCESSED. Even if the donation is found to be unsafe later it should not matter
    // as SPLIT/PROCESSED components are not allowed to be issued
    List<ProductStatus> statusNotToBeChanged =
        Arrays.asList(ProductStatus.DISCARDED, ProductStatus.ISSUED,
            ProductStatus.USED, ProductStatus.SPLIT, ProductStatus.PROCESSED);

    ProductStatus oldProductStatus = component.getStatus();
    
    // nothing to do if the component has any of these statuses
    if (component.getStatus() != null && statusNotToBeChanged.contains(component.getStatus()))
      return false;

    if (component.getDonation() == null)
      return false;
    Long donationId = component.getDonation().getId();
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
    if (component.getExpiresOn().before(new Date())) {
      newProductStatus = ProductStatus.EXPIRED;
    }

    if (ttiStatus.equals(TTIStatus.TTI_UNSAFE)) {
      newProductStatus = ProductStatus.UNSAFE;
    }

    if (!newProductStatus.equals(oldProductStatus)) {
      component.setStatus(newProductStatus);
      return true;
    }
    return false;
  }

  public Component findComponent(String productNumber) {
    Component component = null;
    if (productNumber != null && productNumber.length() > 0) {
      String queryString = "SELECT c FROM Component c WHERE c.productNumber = :productNumber and c.isDeleted= :isDeleted";
      TypedQuery<Component> query = em.createQuery(queryString, Component.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      List<Component> components = query.setParameter("productNumber",
          productNumber).getResultList();
      if (components != null && components.size() > 0) {
        component = components.get(0);
      }
    }
    return component;
  }
  
  public List<Component> findAnyComponent(String donationIdentificationNumber, List<Integer> productTypes, List<ProductStatus> status, 
		  Date donationDateFrom, Date donationDateTo, Map<String, Object> pagingParams){
	  	TypedQuery<Component> query;
	    String queryStr = "SELECT DISTINCT c FROM Component c LEFT JOIN FETCH c.donation WHERE " +     
	                      "c.isDeleted= :isDeleted ";
	    
	    if(status != null && !status.isEmpty()){
	    	queryStr += "AND c.status IN :status ";
	    }	
	    if(!StringUtils.isBlank(donationIdentificationNumber)){
	    	queryStr += "AND c.donation.donationIdentificationNumber = :donationIdentificationNumber ";
	    }
	    if(productTypes != null && !productTypes.isEmpty()){
	    	queryStr += "AND c.productType.id IN (:productTypeIds) ";
	    }	    
	    if(donationDateFrom != null){
	    	queryStr += "AND c.donation.donationDate >= :donationDateFrom ";
	    }
	    if(donationDateTo != null){
	    	queryStr += "AND c.donation.donationDate <= :donationDateTo ";
	    }
	    
	    if (pagingParams.containsKey("sortColumn")) {
	    	queryStr += " ORDER BY c." + pagingParams.get("sortColumn") + " " + pagingParams.get("sortDirection");
	    }
	
	    query = em.createQuery(queryStr, Component.class);
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

  public List<Component> findComponentByDonationIdentificationNumber(
      String donationIdentificationNumber, List<ProductStatus> status, Map<String, Object> pagingParams) {

    TypedQuery<Component> query;
    String queryStr = "SELECT DISTINCT c FROM Component c LEFT JOIN FETCH c.donation WHERE " +
                      "c.donation.donationIdentificationNumber = :donationIdentificationNumber AND " +
                      "c.status IN :status AND " +
                      "c.isDeleted= :isDeleted";

    String queryStrWithoutJoin = "SELECT c FROM Component c WHERE " +
        "c.donation.donationIdentificationNumber = :donationIdentificationNumber AND " +
        "c.status IN :status AND " +
        "c.isDeleted= :isDeleted";

    if (pagingParams.containsKey("sortColumn")) {
      queryStr += " ORDER BY c." + pagingParams.get("sortColumn") + " " + pagingParams.get("sortDirection");
    }

    query = em.createQuery(queryStr, Component.class);
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
  
  public List<Component> findComponentByProductTypes(
      List<Integer> productTypeIds, List<ProductStatus> status,
      Map<String, Object> pagingParams) {

    String queryStr = "SELECT c FROM Component c LEFT JOIN FETCH c.donation WHERE " +
        "c.productType.id IN (:productTypeIds) AND " +
        "c.status IN :status AND " +
        "c.isDeleted= :isDeleted";

    String queryStrWithoutJoin = "SELECT c FROM Component c WHERE " +
        "c.productType.id IN (:productTypeIds) AND " +
        "c.status IN :status AND " +
        "c.isDeleted= :isDeleted";


    if (pagingParams.containsKey("sortColumn")) {
      queryStr += " ORDER BY c." + pagingParams.get("sortColumn") + " " + pagingParams.get("sortDirection");
    }

    TypedQuery<Component> query = em.createQuery(queryStr, Component.class);
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
    String countQueryStr = queryStr.replaceFirst("SELECT c", "SELECT COUNT(c)");
    TypedQuery<Long> countQuery = em.createQuery(countQueryStr, Long.class);
    for (Parameter<?> parameter : query.getParameters()) {
      countQuery.setParameter(parameter.getName(), query.getParameterValue(parameter));
    }
    return countQuery.getSingleResult().longValue();
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

  public boolean isComponentCreated(String donationIdentificationNumber) {
    String queryString = "SELECT c FROM Component c WHERE c.donationIdentificationNumber = :donationIdentificationNumber and c.isDeleted = :isDeleted";
    TypedQuery<Component> query = em.createQuery(queryString, Component.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<Component> components = query.setParameter("donationIdentificationNumber",
        donationIdentificationNumber).getResultList();
    if (components != null && components.size() > 0) {
      return true;
    }
    return false;
  }

  public void deleteAllComponents() {
    Query query = em.createQuery("DELETE FROM Component c");
    query.executeUpdate();
  }

  public List<Component> getAllComponents(String productType) {
    String queryString = "SELECT c FROM Component c where c.type = :productType and c.isDeleted = :isDeleted";
    TypedQuery<Component> query = em.createQuery(queryString, Component.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productType", productType);
    return query.getResultList();
  }

  public List<Component> getComponents(Date fromDate, Date toDate) {
    TypedQuery<Component> query = em
        .createQuery(
            "SELECT c FROM Component c WHERE  c.createdOn >= :fromDate and c.createdOn<= :toDate and c.isDeleted = :isDeleted",
            Component.class);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<Component> components = query.getResultList();
    if (CollectionUtils.isEmpty(components)) {
      return new ArrayList<Component>();
    }
    return components;
  }

  public List<Component> getAllUnissuedComponents(String productType, String abo,
      String rhd) {
    String queryString = "SELECT c FROM Component c where c.type = :productType and c.abo= :abo and c.rhd= :rhd and c.isDeleted = :isDeleted and c.isIssued= :isIssued";
    TypedQuery<Component> query = em.createQuery(queryString, Component.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("isIssued", Boolean.FALSE);
    query.setParameter("productType", productType);
    query.setParameter("abo", abo);
    query.setParameter("rhd", rhd);
    return query.getResultList();
  }

  public List<Component> getAllUnissuedThirtyFiveDayComponents(String productType,
      String abo, String rhd) {
    String queryString = "SELECT c FROM Component c where c.type = :productType and c.abo= :abo and c.rhd= :rhd and c.isDeleted = :isDeleted and c.isIssued= :isIssued and c.createdOn > :minDate";
    TypedQuery<Component> query = em.createQuery(queryString, Component.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("isIssued", Boolean.FALSE);
    query.setParameter("productType", productType);
    query.setParameter("abo", abo);
    query.setParameter("rhd", rhd);
    query.setParameter("minDate", new DateTime(new Date()).minusDays(35)
        .toDate());

    return query.getResultList();
  }

  public Component findComponent(Long componentId) {
    return em.find(Component.class, componentId);
  }

  public Component findComponentById(Long componentId)throws NoResultException{
    String queryString = "SELECT c FROM Component c LEFT JOIN FETCH c.donation LEFT JOIN FETCH c.issuedTo where c.id = :componentId AND c.isDeleted = :isDeleted";
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

  public void deleteComponent(Long componentId) throws IllegalArgumentException{
    Component existingComponent = findComponentById(componentId);
    existingComponent.setIsDeleted(Boolean.TRUE);
    em.merge(existingComponent);
    em.flush();
  }

  public List<MatchingComponentViewModel> findMatchingComponentsForRequest(Long requestId) {

    Date today = new Date();
    Request request = requestRepository.findRequestById(requestId);
    
    TypedQuery<Component> query = em.createQuery(
                 "SELECT c from Component c LEFT JOIN FETCH c.donation WHERE " +
                 "c.productType = :productType AND " +
                 "c.expiresOn >= :today AND " +
                 "c.status = :status AND " + 
                 "c.donation.ttiStatus = :ttiStatus AND " +
                 "((c.donation.bloodAbo = :bloodAbo AND c.donation.bloodRh = :bloodRh) OR " +
                 "(c.donation.bloodAbo = :bloodAboO AND c.donation.bloodRh = :bloodRhNeg)) AND " +
                 "c.isDeleted = :isDeleted " +
                 "ORDER BY c.expiresOn ASC",
                  Component.class);

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
        "ct.testedComponent.status = :testedComponentStatus AND " +
        "isDeleted=:isDeleted", CompatibilityTest.class);

    crossmatchQuery.setParameter("forRequestId", requestId);
    crossmatchQuery.setParameter("testedComponentStatus", ProductStatus.AVAILABLE);
    crossmatchQuery.setParameter("isDeleted", false);

    List<CompatibilityTest> crossmatchTests = crossmatchQuery.getResultList();
    List<MatchingComponentViewModel> matchingComponents = new ArrayList<MatchingComponentViewModel>();

    Map<Long, CompatibilityTest> crossmatchTestMap = new HashMap<Long, CompatibilityTest>();
    for (CompatibilityTest crossmatchTest : crossmatchTests) {
      Component component = crossmatchTest.getTestedComponent();
      if (component == null)
        continue;
      crossmatchTestMap.put(component.getId(), crossmatchTest);
      if (!crossmatchTest.getCompatibilityResult().equals(CompatibilityResult.NOT_COMPATIBLE))
        matchingComponents.add(new MatchingComponentViewModel(component, crossmatchTest));
    }

    for (Component component : query.getResultList()) {
      System.out.println("here");
      Long componentId = component.getId();
      if (crossmatchTestMap.containsKey(componentId))
        continue;
      matchingComponents.add(new MatchingComponentViewModel(component));
    }

    return matchingComponents;
  }
  
  public Map<String, Object> generateInventorySummaryFast(List<String> status, List<Long> panelIds) {
    Map<String, Object> inventory = new HashMap<String, Object>();
    // IMPORTANT: Distinct is necessary to avoid a cartesian product of test results and components from being returned
    // Also LEFT JOIN FETCH prevents the N+1 queries problem associated with Lazy Many-to-One joins
    TypedQuery<Component> q = em.createQuery(
                             "SELECT DISTINCT c from Component c " +
                             "WHERE c.status IN :status AND " +
                             "c.donation.donorPanel.id IN (:panelIds) AND " +
                             "c.isDeleted=:isDeleted",
                             Component.class);
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
    for (Component component : q.getResultList()) {
      String productType = component.getProductType().getProductTypeName();
      @SuppressWarnings("unchecked")
      Map<String, Map<Long, Long>> inventoryByBloodGroup = (Map<String, Map<Long, Long>>) inventory.get(productType);
      DonationViewModel donation;
      donation = new DonationViewModel(component.getDonation());
      Map<Long, Long> numDayMap = inventoryByBloodGroup.get(donation.getBloodGroup());
      DateTime createdOn = new DateTime(component.getCreatedOn().getTime());
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

  public void addAllComponents(List<Component> components) {
    for (Component c : components) {
      updateComponentInternalFields(c);
      em.persist(c);
    }
    em.flush();
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

  public void discardComponent(Long componentId,
      ProductStatusChangeReason discardReason,
      String discardReasonText) {
    Component existingComponent = findComponentById(componentId);
    existingComponent.setStatus(ProductStatus.DISCARDED);
    existingComponent.setDiscardedOn(new Date());
    ProductStatusChange statusChange = new ProductStatusChange();
    statusChange.setStatusChangeType(ProductStatusChangeType.DISCARDED);
    statusChange.setNewStatus(ProductStatus.DISCARDED);
    statusChange.setStatusChangedOn(new Date());
    statusChange.setStatusChangeReason(discardReason);
    statusChange.setStatusChangeReasonText(discardReasonText);
    statusChange.setChangedBy(utilController.getCurrentUser());
    if (existingComponent.getStatusChanges() == null)
      existingComponent.setStatusChanges(new ArrayList<ProductStatusChange>());
    existingComponent.getStatusChanges().add(statusChange);
    statusChange.setComponent(existingComponent);
    em.persist(statusChange);
    em.merge(existingComponent);
    em.flush();
  }

  public void updateExpiryStatus() {
    String updateExpiryQuery = "UPDATE Component c SET c.status=:status WHERE " +
                               "c.status=:availableStatus AND " +
                               "c.expiresOn < :today";
    Query query = em.createQuery(updateExpiryQuery);
    query.setParameter("status", ProductStatus.EXPIRED);
    query.setParameter("availableStatus", ProductStatus.AVAILABLE);
    query.setParameter("today", new Date());
    int numUpdated = query.executeUpdate();
    //System.out.println("Number of rows updated: " + numUpdated);
  }

  public List<Component> getComponentsFromComponentIds(String[] componentIds) {
    List<Component> components = new ArrayList<Component>();
    for (String componentId : componentIds) {
      components.add(findComponentById(Long.parseLong(componentId)));
    }
    return components;
  }

  public Map<String, Map<Long, Long>> findNumberOfDiscardedComponents(
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
        "SELECT count(c), c.donation.donationDate, c.donation.bloodAbo, " +
        "c.donation.bloodRh FROM Component c WHERE " +
        "c.donation.donorPanel.id IN (:panelIds) AND " +
        "c.donation.donationDate BETWEEN :donationDateFrom AND :donationDateTo AND " +
        "c.status IN (:discardedStatuses) AND " +
        "(c.isDeleted= :isDeleted) " +
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

  public Map<String, Map<Long, Long>> findNumberOfIssuedComponents(
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
        "SELECT count(c), c.issuedOn, c.donation.bloodAbo, " +
        "c.donation.bloodRh FROM Component c WHERE " +
        "c.donation.donorPanel.id IN (:panelIds) AND " +
        "c.donation.donationDate BETWEEN :donationDateFrom AND :donationDateTo AND " +
        "c.status=:issuedStatus AND " +
        "(c.isDeleted= :isDeleted) " +
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

  public Component findComponent(String donationIdentificationNumber, String productTypeId) {
    String queryStr = "SELECT c from Component c WHERE " +
                      "c.donation.donationIdentificationNumber = :donationIdentificationNumber AND " +
                      "c.productType.id = :productTypeId";
    TypedQuery<Component> query = em.createQuery(queryStr, Component.class);
    query.setParameter("donationIdentificationNumber", donationIdentificationNumber);
    query.setParameter("productTypeId", Integer.parseInt(productTypeId));
    Component component = null;
    try {
      component = query.getSingleResult();
    } catch (NoResultException ex) {
      ex.printStackTrace();
    }
    return component;
  }

  public void returnComponent(Long componentId,
      ProductStatusChangeReason returnReason, String returnReasonText) {
    Component existingComponent = findComponentById(componentId);
    updateComponentStatus(existingComponent);
    ProductStatusChange statusChange = new ProductStatusChange();
    statusChange.setStatusChangedOn(new Date());
    statusChange.setStatusChangeType(ProductStatusChangeType.RETURNED);
    statusChange.setStatusChangeReason(returnReason);
    statusChange.setNewStatus(existingComponent.getStatus());
    statusChange.setStatusChangeReasonText(returnReasonText);
    statusChange.setChangedBy(utilController.getCurrentUser());
    if (existingComponent.getStatusChanges() == null)
      existingComponent.setStatusChanges(new ArrayList<ProductStatusChange>());
    existingComponent.getStatusChanges().add(statusChange);
    statusChange.setComponent(existingComponent);
    em.persist(statusChange);
    em.merge(existingComponent);
    em.flush();
  }

  public List<ProductStatusChange> getComponentStatusChanges(Component component) {
    String queryStr = "SELECT p FROM ProductStatusChange p WHERE " +
        "p.component.id=:componentId";
    TypedQuery<ProductStatusChange> query = em.createQuery(queryStr, ProductStatusChange.class);
    query.setParameter("componentId", component.getId());
    List<ProductStatusChange> statusChanges = query.getResultList();
    return statusChanges;
  }

  public List<Component> findComponentsByDonationIdentificationNumber(String donationIdentificationNumber) {
    String queryStr = "SELECT c from Component c WHERE " +
        "c.donation.donationIdentificationNumber=:donationIdentificationNumber AND c.isDeleted=:isDeleted";
    TypedQuery<Component> query = em.createQuery(queryStr, Component.class);
    query.setParameter("donationIdentificationNumber", donationIdentificationNumber);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<Component> addComponentCombination(ComponentCombinationBackingForm form) throws PessimisticLockException, ParseException {
    List<Component> components = new ArrayList<Component>();
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
      Component component = new Component();
      component.setDonation(form.getDonation());
      component.setProductType(productType);
      component.setCreatedOn(form.getComponent().getCreatedOn());
      String expiryDateStr = expiryDateByProductType.get(productType.getId().toString());
      component.setExpiresOn(CustomDateFormatter.getDateTimeFromString(expiryDateStr));
      component.setIsDeleted(false);
      updateComponentInternalFields(component);
      em.persist(component);
      em.flush();
      em.refresh(component);
      components.add(component);
    }

    return components;
  }

  public boolean splitComponent(Long componentId, Integer numComponentsAfterSplitting) {

    Component component = findComponent(componentId);
    if (component == null || component.getStatus().equals(ProductStatus.SPLIT))
      return false;

    ProductType pediProductType = component.getProductType().getPediProductType();
    if (pediProductType == null) {
      return false;
    }

    char nextSubdivisionCode = 'A';
    for (int i = 0; i < numComponentsAfterSplitting; ++i) {
      Component newComponent = new Component();
      // just set the id temporarily before copying all the fields
      newComponent.setId(componentId);
      newComponent.copy(component);
      newComponent.setId(null);
      newComponent.setProductType(pediProductType);
      newComponent.setSubdivisionCode("" + nextSubdivisionCode);
      newComponent.setParentComponent(component);
      newComponent.setIsDeleted(false);
      updateComponentInternalFields(newComponent);
      em.persist(newComponent);
      // Assuming we do not split into more than 26 products this should be fine
      nextSubdivisionCode++;
    }

    component.setStatus(ProductStatus.SPLIT);
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
    if (component.getStatusChanges() == null)
      component.setStatusChanges(new ArrayList<ProductStatusChange>());
    component.getStatusChanges().add(statusChange);
    statusChange.setComponent(component);
    em.persist(statusChange);
    em.merge(component);
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
  
  public void setProductStatusToProcessed(long componentId) throws NoResultException {
  	 String queryString = "SELECT c FROM Component c where c.id = :componentId";
     TypedQuery<Component> query = em.createQuery(queryString, Component.class);
     query.setParameter("componentId", componentId);
     Component component = null;
     	component = query.getSingleResult();
     	component.setStatus(ProductStatus.PROCESSED);
     	em.merge(component);
  }
}
