package repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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

import model.bloodtest.BloodTest;
import model.collectedsample.CollectedSample;
import model.testresults.TestResult;
import model.testresults.TestedStatus;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRh;
import model.worksheet.CollectionsWorksheet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import viewmodel.TestResultViewModel;

@Repository
@Transactional
public class CollectedSampleRepository {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private TestResultRepository testResultRepository;

  @Autowired
  private BloodTestRepository bloodTestRepository;

  public void updateCollectedSampleInternalFields(CollectedSample c) {
    updateCollectedSampleBloodGroup(c);
    updateCollectedSampleTestedStatus(c);
  }

  public void updateCollectedSampleBloodGroup(CollectedSample c) {

    BloodAbo bloodAbo = c.getBloodAbo();
    BloodRh bloodRhd = c.getBloodRhd();

    Map<String, TestResult> testResultsMap = testResultRepository.getRecentTestResultsForCollection(c.getId());

    TestResult t = testResultsMap.get("Blood ABO");
    if (t != null && !t.getIsDeleted()) {
      try {
        bloodAbo = BloodAbo.valueOf(t.getResult());
      } catch (IllegalArgumentException ex) {
        ex.printStackTrace();
        bloodAbo = BloodAbo.Unknown;
      } 
    }
    
    t = testResultsMap.get("Blood Rh");
    if (t != null && !t.getIsDeleted()) {
      try {
        bloodRhd = BloodRh.valueOf(t.getResult());
      } catch (IllegalArgumentException ex) {
        ex.printStackTrace();
        bloodRhd = BloodRh.Unknown;
      }
    }
    c.setBloodAbo(bloodAbo);
    c.setBloodRhd(bloodRhd);
  }
  
  public void saveCollectedSample(CollectedSample collectedSample) {
    em.persist(collectedSample);
    em.flush();
  }

  public CollectedSample updateCollectedSample(CollectedSample collectedSample) {
    CollectedSample existingCollectedSample = findCollectedSampleById(collectedSample.getId());
    if (existingCollectedSample == null) {
      return null;
    }
    existingCollectedSample.copy(collectedSample);
    existingCollectedSample = em.merge(existingCollectedSample);
    updateCollectedSampleInternalFields(existingCollectedSample);
    em.flush();
    return existingCollectedSample;
  }

  public CollectedSample findCollectedSampleById(Long collectedSampleId) {
    String queryString = "SELECT c FROM CollectedSample c LEFT JOIN FETCH c.donor WHERE c.id = :collectedSampleId and c.isDeleted = :isDeleted";
    TypedQuery<CollectedSample> query = em.createQuery(queryString, CollectedSample.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.setParameter("collectedSampleId", collectedSampleId).getSingleResult();
  }

  public List<Object> findCollectedSamples(
      String collectionNumber, List<Integer> bloodBagTypeIds, List<Long> centerIds, List<Long> siteIds, String dateCollectedFrom,
      String dateCollectedTo, boolean includeTestedCollections, Map<String, Object> pagingParams) {

    String queryStr = "";
    if (StringUtils.isNotBlank(collectionNumber)) {
      queryStr = "SELECT c FROM CollectedSample c LEFT JOIN FETCH c.donor WHERE " +
      		       "c.collectionNumber = :collectionNumber AND " +
                 "c.bloodBagType.id IN :bloodBagTypeIds AND " +
                 "c.collectionCenter.id IN :centerIds AND " +
                 "c.collectionSite.id IN :siteIds AND " +
                 "c.collectedOn >= :dateCollectedFrom AND c.collectedOn <= :dateCollectedTo AND " +
                 "c.isDeleted=:isDeleted";
    } else {
      queryStr = "SELECT c FROM CollectedSample c LEFT JOIN FETCH c.donor WHERE " +
          "c.bloodBagType.id IN :bloodBagTypeIds AND " +
          "c.collectionCenter.id IN :centerIds AND " +
          "c.collectionSite.id IN :siteIds AND " +
          "c.collectedOn >= :dateCollectedFrom AND c.collectedOn <= :dateCollectedTo AND " +
          "c.isDeleted=:isDeleted";
    }

    if (!includeTestedCollections)
      queryStr = queryStr + " AND c.testedStatus=:testedStatus";

    TypedQuery<CollectedSample> query;
    if (pagingParams.containsKey("sortColumn")) {
      queryStr += " ORDER BY c." + pagingParams.get("sortColumn") + " " + pagingParams.get("sortDirection");
    }
    
    query = em.createQuery(queryStr, CollectedSample.class);
    query.setParameter("isDeleted", Boolean.FALSE);

    if (StringUtils.isNotBlank(collectionNumber))
      query.setParameter("collectionNumber", collectionNumber);

    if (!includeTestedCollections)
      query.setParameter("testedStatus", TestedStatus.NOT_TESTED);
    
    query.setParameter("bloodBagTypeIds", bloodBagTypeIds);
    query.setParameter("centerIds", centerIds);
    query.setParameter("siteIds", siteIds);
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

  public List<CollectedSample> getAllCollectedSamples() {
    TypedQuery<CollectedSample> query = em.createQuery(
        "SELECT c FROM CollectedSample c WHERE c.isDeleted= :isDeleted",
        CollectedSample.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.getResultList();
  }

  public List<CollectedSample> getCollectedSamples(Date fromDate, Date toDate) {
    TypedQuery<CollectedSample> query = em
        .createQuery(
            "SELECT c FROM CollectedSample c WHERE c.dateCollected >= :fromDate and c.dateCollected<= :toDate and c.isDeleted= :isDeleted",
            CollectedSample.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    List<CollectedSample> collectedSamples = query.getResultList();
    if (CollectionUtils.isEmpty(collectedSamples)) {
      return new ArrayList<CollectedSample>();
    }
    return collectedSamples;
  }

  public void deleteCollectedSample(Long collectedSampleId) {
    CollectedSample existingCollectedSample = findCollectedSampleById(collectedSampleId);
    existingCollectedSample.setIsDeleted(Boolean.TRUE);
    em.merge(existingCollectedSample);
    em.flush();
  }

  public List<CollectedSample> findAnyCollectedSampleMatching(String collectionNumber,
      String sampleNumber, String shippingNumber, String dateCollectedFrom,
      String dateCollectedTo, List<String> centers) {

    TypedQuery<CollectedSample> query = em.createQuery(
        "SELECT c FROM CollectedSample c JOIN c.center center WHERE "
            + "(c.collectionNumber = :collectionNumber OR "
            + "c.sampleNumber = :sampleNumber OR "
            + "c.shippingNumber = :shippingNumber OR "
            + "center.id IN (:centers)) AND ("
            + "c.collectedOn BETWEEN :dateCollectedFrom AND "
            + ":dateCollectedTo" + ") AND " + "(c.isDeleted= :isDeleted)",
        CollectedSample.class);

    query.setParameter("isDeleted", Boolean.FALSE);
    String collectedSampleNo = ((collectionNumber == null) ? "" : collectionNumber);
    query.setParameter("collectionNumber", collectedSampleNo);
    query.setParameter("sampleNumber", sampleNumber);
    query.setParameter("shippingNumber", shippingNumber);

    query.setParameter("centers", centers);

    List<CollectedSample> resultList = query.getResultList();
    return resultList;
  }

  private Date getDateCollectedFromOrDefault(String dateCollectedFrom) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date from = null;
    try {
      from = (dateCollectedFrom == null || dateCollectedFrom.equals("")) ? dateFormat
          .parse("12/31/1970") : dateFormat.parse(dateCollectedFrom);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return from;      
  }

  private Date getDateCollectedToOrDefault(String dateCollectedTo) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date to = null;
    try {
      to = (dateCollectedTo == null || dateCollectedTo.equals("")) ? new Date() :
              dateFormat.parse(dateCollectedTo);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return to;      
  }

  public Map<String, Map<Long, Long>> findNumberOfCollectedSamples(Date dateCollectedFrom,
      Date dateCollectedTo, String aggregationCriteria,
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
        "SELECT count(c), c.collectedOn, c.bloodAbo, c.bloodRhd FROM CollectedSample c WHERE " +
        "c.collectionCenter.id IN (:centerIds) AND c.collectionSite.id IN (:siteIds) AND " +
        "c.collectedOn BETWEEN :dateCollectedFrom AND " +
        ":dateCollectedTo AND (c.isDeleted= :isDeleted) GROUP BY " +
        "bloodAbo, bloodRhd, collectedOn", Object[].class);

    query.setParameter("centerIds", centerIds);
    query.setParameter("siteIds", siteIds);
    query.setParameter("isDeleted", Boolean.FALSE);

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
      BloodRh bloodRhd = (BloodRh) result[3];
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

  public List<TestResultViewModel> findUntestedCollectedSamples(String dateCollectedFrom,
      String dateCollectedTo) {

    TypedQuery<CollectedSample> query = em
        .createQuery(
            "SELECT c FROM CollectedSample c WHERE c.dateCollected >= :fromDate "
                + "AND c.dateCollected<= :toDate AND c.isDeleted= :isDeleted AND "
                + "c.collectionNumber NOT IN (SELECT t.collectionNumber FROM TestResult t)",
            CollectedSample.class);

    query.setParameter("isDeleted", Boolean.FALSE);
    
    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    try {
      query.setParameter("fromDate", formatter.parse(dateCollectedFrom));
      query.setParameter("toDate", formatter.parse(dateCollectedTo));
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    List<TestResultViewModel> testResults = new ArrayList<TestResultViewModel>();
    for (CollectedSample collectedSample : query.getResultList()) {
      TestResult testResult = new TestResult();
      testResult.setCollectedSample(collectedSample);
//      testResults.add(testResult);
    }

    return testResults;
  }

  public CollectedSample addCollectedSample(CollectedSample collectedSample) {
    updateCollectedSampleInternalFields(collectedSample);
    collectedSample.setTestedStatus(TestedStatus.NOT_TESTED);
    em.persist(collectedSample);
    em.flush();
    em.refresh(collectedSample);
    return collectedSample;
  }

  public List<CollectedSample> findCollectedSampleByCenters(
      List<Long> centerIds, String dateCollectedFrom, String dateCollectedTo) {
    TypedQuery<CollectedSample> query = em
        .createQuery(
            "SELECT c FROM CollectedSample c WHERE " +
            "c.collectionCenter.id IN (:centers) and " +
            "((c.collectedOn is NULL) or " +
            " (c.collectedOn >= :fromDate and c.collectedOn <= :toDate)) and " +
            "c.isDeleted= :isDeleted",
            CollectedSample.class);

    Date from = getDateCollectedFromOrDefault(dateCollectedFrom);
    Date to = getDateCollectedToOrDefault(dateCollectedTo);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("centers", centerIds);
    query.setParameter("fromDate", from);
    query.setParameter("toDate", to);

    return query.getResultList();
  }

  public void addAllCollectedSamples(List<CollectedSample> collectedSamples) {
    for (CollectedSample c : collectedSamples) {
      c.setTestedStatus(TestedStatus.NOT_TESTED);
      em.persist(c);
    }
    em.flush();
  }

  public CollectedSample findCollectedSampleByCollectionNumber(
      String collectionNumber) {
    String queryString = "SELECT c FROM CollectedSample c LEFT JOIN FETCH c.donor WHERE c.collectionNumber = :collectionNumber and c.isDeleted = :isDeleted";
    TypedQuery<CollectedSample> query = em.createQuery(queryString, CollectedSample.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("collectionNumber", collectionNumber);
    CollectedSample c = null;
    try {
       c = query.getSingleResult();
    } catch (NoResultException ex) {
      System.out.println("Collection number not found: " + collectionNumber);
    }
    return c;
  }

  public void saveAsWorksheet(String collectionNumber,
      List<Integer> bloodBagTypeIds, List<Long> centerIds,
      List<Long> siteIds, String dateCollectedFrom, String dateCollectedTo, boolean includeUntestedCollections, String worksheetBatchId) throws Exception {

    Map<String, Object> pagingParams = new HashMap<String, Object>();
    List<Object> results = findCollectedSamples(collectionNumber, bloodBagTypeIds,
                                          centerIds, siteIds,
                                          dateCollectedFrom, dateCollectedTo,
                                          includeUntestedCollections,
                                          pagingParams);
    CollectionsWorksheet worksheet = new CollectionsWorksheet();
    worksheet.setWorksheetBatchId(worksheetBatchId);
    List<CollectedSample> collectedSamples = (List<CollectedSample>) results.get(0);
    for (CollectedSample c : collectedSamples) {
      worksheet.getCollectedSamples().add(c);
      c.getWorksheets().add(worksheet);
    }
    em.persist(worksheet);
    em.flush();
  }

  public CollectionsWorksheet findWorksheet(String worksheetBatchId) {
    String queryStr = "SELECT w from CollectionsWorksheet w LEFT JOIN FETCH w.collectedSamples c " +
        "where w.worksheetBatchId = :worksheetBatchId";

    TypedQuery<CollectionsWorksheet> query = em.createQuery(queryStr, CollectionsWorksheet.class);
    query.setParameter("worksheetBatchId", worksheetBatchId);
    CollectionsWorksheet worksheet = null;
    try {
    worksheet = query.getSingleResult();
    } catch (NoResultException ex) {
    ex.printStackTrace();
    }
    
    if (worksheet == null)
      return null;
    return worksheet;
  }

  public List<CollectedSample> findCollectionsInWorksheet(String worksheetBatchId) {

    CollectionsWorksheet worksheet = findWorksheet(worksheetBatchId);
    if (worksheet == null)
      return null;

    List<CollectedSample> collectedSamples = worksheet.getCollectedSamples();
    Collections.sort(collectedSamples);
    return collectedSamples;
  }

  public List<Object> findCollectionsInWorksheet(String worksheetBatchId, Map<String, Object> pagingParams) {

    try {
      String collectionsQueryStr = "SELECT c from CollectedSample c LEFT JOIN FETCH c.worksheets w " +
                                   "WHERE w.worksheetBatchId = :worksheetBatchId ORDER BY c.id ASC";
      TypedQuery<CollectedSample> collectionsQuery = em.createQuery(collectionsQueryStr, CollectedSample.class);
      collectionsQuery.setParameter("worksheetBatchId", worksheetBatchId);
  
      int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
      int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);
  
      collectionsQuery.setFirstResult(start);
      collectionsQuery.setMaxResults(length);
  
      List<CollectedSample> collectedSamples = collectionsQuery.getResultList();
  
      return Arrays.asList(collectedSamples, getTotalCollectionsInWorksheet(worksheetBatchId));
    } catch (NoResultException ex){
      return Arrays.asList(Arrays.asList(new CollectedSample[0]), new Long(0));
    }
  }

  private Long getTotalCollectionsInWorksheet(String worksheetBatchId) {
    String queryStr = "SELECT COUNT(c) from CollectionsWorksheet w LEFT JOIN w.collectedSamples c " +
        "where w.worksheetBatchId = :worksheetBatchId";

    TypedQuery<Long> query = em.createQuery(queryStr, Long.class);
    query.setParameter("worksheetBatchId", worksheetBatchId);
    return query.getSingleResult().longValue();
  }

  public void updateCollectedSampleTestedStatus(CollectedSample collectedSample) {
    Map<String, TestResult> testResults = testResultRepository.getRecentTestResultsForCollection(collectedSample.getId());
    List<BloodTest> allBloodTests = bloodTestRepository.getAllBloodTests();
    boolean tested = true;
    for (BloodTest bt: allBloodTests) {
    	TestResult t = testResults.get(bt.getName());
    	if (t == null) {
    		tested = false;
    		break;
    	}
    	if (!bt.getAllowedResults().contains(t.getResult())) {
    		tested = false;
    		break;
    	}
    }
    if (tested)
      collectedSample.setTestedStatus(TestedStatus.TESTED);
    else
      collectedSample.setTestedStatus(TestedStatus.NOT_TESTED);
  }

  public Map<Integer, CollectedSample> verifyCollectionNumbers(List<String> collectionNumbers) {
    Map<Integer, CollectedSample> collections = new HashMap<Integer, CollectedSample>();
    int i = 1;
    for (String collectionNumber : collectionNumbers) {
      if (StringUtils.isBlank(collectionNumber))
        continue;
      CollectedSample collectedSample = new CollectedSample();
      collectedSample.setCollectionNumber(collectionNumber);
      collectedSample = findCollectedSampleByCollectionNumber(collectionNumber);
      if (collectedSample != null) {
        collections.put(i, collectedSample);
      } else {
        collections.put(i, null);
      }
      i++;
    }
    return collections;
  }
}
