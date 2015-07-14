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
import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.bloodbagtype.BloodBagType;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donor.Donor;
import model.product.Product;
import model.product.ProductStatus;
import model.producttype.ProductType;
import model.util.BloodGroup;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import repository.bloodtesting.BloodTestingRepository;
import repository.bloodtesting.BloodTypingStatus;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.events.ApplicationContextProvider;
import repository.events.CollectionUpdatedEvent;
import viewmodel.BloodTestingRuleResult;

@Repository
@Transactional
public class DonationRepository {
	
	
	/**
	  * The Constant LOGGER.
	  */
	  private static final Logger LOGGER = Logger.getLogger(DonationRepository.class);

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private BloodTestingRepository bloodTypingRepository;

  @Autowired
  private WorksheetRepository worksheetRepository;

  @Autowired
  private ProductRepository productRepository;
  
  public void saveDonation(Donation donation) {
    em.persist(donation);
    em.flush();
  }

  public Donation updateDonation(Donation donation) throws NoResultException{
    Donation existingDonation = findDonationById(donation.getId());
    if (existingDonation == null) {
      return null;
    }
    existingDonation.copy(donation);
    existingDonation = em.merge(existingDonation);
    ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    applicationContext.publishEvent(new CollectionUpdatedEvent("10", donation));
    em.flush();
    return existingDonation;
  }

  public Donation findDonationById(Long donationId) {
    String queryString = "SELECT c FROM Donation c LEFT JOIN FETCH c.donor WHERE c.id = :donationId and c.isDeleted = :isDeleted";
    TypedQuery<Donation> query = em.createQuery(queryString, Donation.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    try{
    return query.setParameter("donationId", donationId).getSingleResult();
    }catch(NoResultException ex){
        throw new NoResultException("No Donation Exists with ID :" + donationId);
    }
  }

  public List<Object> findDonations(
      String collectionNumber, List<Integer> bloodBagTypeIds, List<Long> panelIds, String dateCollectedFrom,
      String dateCollectedTo, boolean includeTestedDonations, Map<String, Object> pagingParams) throws ParseException {

    String queryStr = "";
    if (StringUtils.isNotBlank(collectionNumber)) {
      queryStr = "SELECT c FROM Donation c LEFT JOIN FETCH c.donor WHERE " +
                 "c.collectionNumber = :collectionNumber AND " +
                 "c.bloodBagType.id IN :bloodBagTypeIds AND " +
                 "c.donorPanel.id IN :donorPanelIds AND " +
                 "c.collectedOn >= :dateCollectedFrom AND c.collectedOn <= :dateCollectedTo AND " +
                 "c.isDeleted=:isDeleted";
    } else {
      queryStr = "SELECT c FROM Donation c LEFT JOIN FETCH c.donor WHERE " +
          "c.bloodBagType.id IN :bloodBagTypeIds AND " +
          "c.donorPanel.id IN :panelIds AND " +
          "c.collectedOn >= :dateCollectedFrom AND c.collectedOn <= :dateCollectedTo AND " +
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

    if (StringUtils.isNotBlank(collectionNumber))
      query.setParameter("collectionNumber", collectionNumber);

    if (!includeTestedDonations) {
      query.setParameter("bloodTypingStatus", BloodTypingStatus.NOT_DONE);
      query.setParameter("ttiStatus", TTIStatus.NOT_DONE);
    }
    
    query.setParameter("bloodBagTypeIds", bloodBagTypeIds);
    query.setParameter("panelIds", panelIds);
    query.setParameter("dateCollectedFrom", getDateCollectedFromOrDefault(dateCollectedFrom));
    query.setParameter("dateCollectedTo", getDateCollectedToOrDefault(dateCollectedTo));
             

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

  public List<Donation> getAllDonations() {
    TypedQuery<Donation> query = em.createQuery(
        "SELECT c FROM Donation c WHERE c.isDeleted= :isDeleted",
        Donation.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.getResultList();
  }

  public List<Donation> getDonations(Date fromDate, Date toDate) {
    TypedQuery<Donation> query = em
        .createQuery(
            "SELECT c FROM Donation c WHERE c.dateCollected >= :fromDate and c.dateCollected<= :toDate and c.isDeleted= :isDeleted",
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

  public void deleteDonation(Long donationId) {
    Donation existingDonation = findDonationById(donationId);
    existingDonation.setIsDeleted(Boolean.TRUE);
    em.merge(existingDonation);
    em.flush();
  }

  public List<Donation> findAnyDonationMatching(String collectionNumber,
      String sampleNumber, String shippingNumber, String dateCollectedFrom,
      String dateCollectedTo, List<String> centers) {

    TypedQuery<Donation> query = em.createQuery(
        "SELECT c FROM Donation c JOIN c.center center WHERE "
            + "(c.collectionNumber = :collectionNumber OR "
            + "c.sampleNumber = :sampleNumber OR "
            + "c.shippingNumber = :shippingNumber OR "
            + "center.id IN (:centers)) AND ("
            + "c.collectedOn BETWEEN :dateCollectedFrom AND "
            + ":dateCollectedTo" + ") AND " + "(c.isDeleted= :isDeleted)",
        Donation.class);

    query.setParameter("isDeleted", Boolean.FALSE);
    String donationNo = ((collectionNumber == null) ? "" : collectionNumber);
    query.setParameter("collectionNumber", donationNo);
    query.setParameter("sampleNumber", sampleNumber);
    query.setParameter("shippingNumber", shippingNumber);

    query.setParameter("centers", centers);

    List<Donation> resultList = query.getResultList();
    return resultList;
  }

  private Date getDateCollectedFromOrDefault(String dateCollectedFrom) throws ParseException{
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date from = 
             (dateCollectedFrom == null || dateCollectedFrom.equals("")) ? dateFormat
                          .parse("31/12/1970") : dateFormat.parse(dateCollectedFrom);
              
 
    return from;      
  }

  private Date getDateCollectedToOrDefault(String dateCollectedTo) throws ParseException{
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date to = null;
                  to = (dateCollectedTo == null || dateCollectedTo.equals("")) ? new Date() :
                          dateFormat.parse(dateCollectedTo);
              
    return to;      
  }

  public Map<String, Map<Long, Long>> findNumberOfDonations(Date dateCollectedFrom,
      Date dateCollectedTo, String aggregationCriteria,
      List<String> panels, List<String> bloodGroups)throws ParseException{

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
        "SELECT count(c), c.collectedOn, c.bloodAbo, c.bloodRh FROM Donation c WHERE " +
        "c.donorPanel.id IN (:panelIds) AND " +
        "c.collectedOn BETWEEN :dateCollectedFrom AND " +
        ":dateCollectedTo AND (c.isDeleted= :isDeleted) GROUP BY " +
        "bloodAbo, bloodRh, collectedOn", Object[].class);

    query.setParameter("panelIds", panelIds);
    query.setParameter("isDeleted", Boolean.FALSE);

    query.setParameter("dateCollectedFrom", dateCollectedFrom);
    query.setParameter("dateCollectedTo", dateCollectedTo);

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
      Date lowerDate = resultDateFormat.parse(resultDateFormat.format(dateCollectedFrom));
      Date upperDate =  resultDateFormat.parse(resultDateFormat.format(dateCollectedTo));
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

  public Donation addDonation(Donation donation) throws PersistenceException{
    donation.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    donation.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    donation.setTTIStatus(TTIStatus.NOT_DONE);
    donation.setIsDeleted(false);
    
    em.persist(donation);
    em.flush();
    em.refresh(donation);
    updateDonorFields(donation);
    
    ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    applicationContext.publishEvent(new CollectionUpdatedEvent("10", donation));
    
    em.refresh(donation);
   
    //Create initial component only if the countAsDonation is true
    if( donation.getBloodBagType().isCountAsDonation() == true)
        createInitialComponent(donation);
  
    return donation;
  }
  
  public void createInitialComponent(Donation donation){
    
    ProductType productType = donation.getBloodBagType().getProductType();
      
    Product product = new Product();
    product.setIsDeleted(false);
    product.setComponentIdentificationNumber(donation.getCollectionNumber() +"-"+productType.getProductTypeNameShort());
    product.setDonation(donation);
    product.setStatus(ProductStatus.QUARANTINED);
    product.setCreatedDate(donation.getCreatedDate());

    // set new component creation date to match donation date 
    product.setCreatedOn(donation.getCollectedOn());
    // if bleed time is provided, update component creation time to match bleed start time 
    if (donation.getBleedStartTime() != null){
    	Calendar donationDate = Calendar.getInstance();
    	donationDate.setTime(donation.getCollectedOn());
    	Calendar bleedTime = Calendar.getInstance();
    	bleedTime.setTime(donation.getBleedStartTime());
    	donationDate.set(Calendar.HOUR_OF_DAY, bleedTime.get(Calendar.HOUR_OF_DAY));
    	donationDate.set(Calendar.MINUTE, bleedTime.get(Calendar.MINUTE));
    	donationDate.set(Calendar.SECOND, bleedTime.get(Calendar.SECOND));
    	donationDate.set(Calendar.MILLISECOND, bleedTime.get(Calendar.MILLISECOND));
    	product.setCreatedOn(donationDate.getTime());
    }
    product.setCreatedBy(donation.getCreatedBy());
    
    // set cal to collectedOn Date 
    Calendar cal = Calendar.getInstance();
    cal.setTime(donation.getCollectedOn());
    
    // second calendar to store bleedStartTime 
    Calendar bleedStartTime = Calendar.getInstance();
    bleedStartTime.setTime(donation.getBleedStartTime());
    
    // update cal to set time to bleedStartTime
    cal.set(Calendar.HOUR_OF_DAY, bleedStartTime.get(Calendar.HOUR_OF_DAY));
    cal.set(Calendar.MINUTE, bleedStartTime.get(Calendar.MINUTE));
    cal.set(Calendar.SECOND, bleedStartTime.get(Calendar.SECOND));
    
    // update cal with initial component expiry period
    cal.add(Calendar.DATE, productType.getExpiresAfter());
    Date expiresOn = cal.getTime();    

    product.setExpiresOn(expiresOn);
    product.setProductType(productType);
    em.persist(product);
    em.refresh(product);
   
  }
  
  private void updateDonorFields(Donation donation){
           Donor donor = donation.getDonor();
     
     //set date of first donation 
     if (donation.getDonor().getDateOfFirstDonation() == null) {
          donor.setDateOfFirstDonation(donation.getCollectedOn());
      }
       //set dueToDonate
      BloodBagType packType = donation.getBloodBagType();
      int periodBetweenDays = packType.getPeriodBetweenDonations();
      Calendar dueToDonateDate = Calendar.getInstance();
      dueToDonateDate.setTime(donation.getCollectedOn());
      dueToDonateDate.add(Calendar.DAY_OF_YEAR, periodBetweenDays);

      if (donor.getDueToDonate() == null || dueToDonateDate.getTime().after(donor.getDueToDonate())) {
          donor.setDueToDonate(dueToDonateDate.getTime());
      }
  }

  public List<Donation> addAllDonations(List<Donation> donations) {
    ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    for (Donation c : donations) {
      c.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
      c.setTTIStatus(TTIStatus.NOT_DONE);
      em.persist(c);
      applicationContext.publishEvent(new CollectionUpdatedEvent("10", c));
      em.refresh(c);
    }
    em.flush();
    return donations;
  }

  public Donation findDonationByCollectionNumber(
      String collectionNumber)throws NoResultException, NonUniqueResultException{
    String queryString = "SELECT c FROM Donation c LEFT JOIN FETCH c.donor WHERE c.collectionNumber = :collectionNumber and c.isDeleted = :isDeleted";
    TypedQuery<Donation> query = em.createQuery(queryString, Donation.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("collectionNumber", collectionNumber);
    Donation c = null;
    c = query.getSingleResult();
    return c;
  }

  public Donation findDonationByCollectionNumberIncludeDeleted(
      String collectionNumber){
    String queryString = "SELECT c FROM Donation c WHERE c.collectionNumber = :collectionNumber";
    TypedQuery<Donation> query = em.createQuery(queryString, Donation.class);
    query.setParameter("collectionNumber", collectionNumber);
    Donation c = null;
    try{
    c = query.getSingleResult();
    }catch(Exception ex){}
    return c;
  }

  public Donation verifyCollectionNumber(String collectionNumber) {
	  Donation donation = new Donation();
	  donation.setCollectionNumber(collectionNumber);
	  donation = findDonationByCollectionNumber(collectionNumber);
	  if (donation != null) {
	    return donation;
	  } else {
	    return null;
	  }
  }
  
  public List<Donation> verifyCollectionNumbers(List<String> collectionNumbers) {
    List<Donation> donations = new ArrayList<Donation>();
    for (String collectionNumber : collectionNumbers) {
      if (StringUtils.isBlank(collectionNumber))
        continue;
      Donation donation = new Donation();
      donation.setCollectionNumber(collectionNumber);
      donation = findDonationByCollectionNumber(collectionNumber);
      if (donation != null) {
        donations.add(donation);
      } else {
        donations.add(null);
      }
    }
    return donations;
  }

  public Map<Long, BloodTestingRuleResult> filterCollectionsWithBloodTypingResults(
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
