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
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.bloodtest.BloodTest;
import model.collectedsample.CollectedSample;
import model.product.Product;
import model.testresults.TestResult;
import model.worksheet.CollectionsWorksheet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Repository
@Transactional
public class TestResultRepository {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;
  
  @Autowired
  private ProductRepository productRepository;

  private List<Product> getProductsToUpdate(TestResult testResult) {
    if (testResult.getCollectedSample() == null || testResult.getCollectedSample().getId() == null)
      return Arrays.asList(new Product[0]);
    CollectedSample c = collectedSampleRepository.findCollectedSampleById(testResult.getCollectedSample().getId());
    if (c == null)
      return Arrays.asList(new Product[0]);
    return c.getProducts();
  }
  
  private void updateProductStatus(TestResult testResult) {
    for (Product product : getProductsToUpdate(testResult)) {
      productRepository.updateProductInternalFields(product);
      em.merge(product);
    }
  }

  public List<TestResult> getAllTestResults() {
    TypedQuery<TestResult> query = em.createQuery(
        "SELECT t FROM TestResult t WHERE t.isDeleted= :isDeleted",
        TestResult.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.getResultList();
  }

  public List<TestResult> getTestResults(Date fromDate, Date toDate) {
    TypedQuery<TestResult> query = em.createQuery(
            "SELECT t FROM TestResult t WHERE t.dateCollected >= :fromDate and t.dateCollected<= :toDate and t.isDeleted= :isDeleted",
            TestResult.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    List<TestResult> testResults = query.getResultList();
    if (CollectionUtils.isEmpty(testResults)) {
      return new ArrayList<TestResult>();
    }
    return testResults;
  }

  public void deleteTestResult(Long testResultId) {
    TestResult existingTestResult = findTestResultById(testResultId);
    existingTestResult.setIsDeleted(Boolean.TRUE);
    updateProductStatus(existingTestResult);
    em.merge(existingTestResult);
    em.flush();
  }

  public List<TestResult> getAllTestResults(String collectionNumber) {
    TypedQuery<TestResult> query = em
        .createQuery(
            "SELECT t FROM TestResult t WHERE t.collectionNumber= :collectionNumber and t.isDeleted= :isDeleted",
            TestResult.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("collectionNumber", collectionNumber);
    return query.getResultList();
  }

  public TestResult find(String selectedTestResultId) {
    TypedQuery<TestResult> query = em
        .createQuery(
            "SELECT t FROM TestResult t WHERE t.testResultId= :testResultId and t.isDeleted= :isDeleted",
            TestResult.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("testResultId", Long.parseLong(selectedTestResultId));
    return query.getSingleResult();
  }

  public List<TestResult> findAnyTestResultMatching(String collectionNumber,
      String hiv, String hbv, String hcv, String syphilis,
      String dateTestedFrom, String dateTestedTo) {

    TypedQuery<TestResult> query = em.createQuery(
        "SELECT t FROM TestResult t WHERE "
            + "(t.collectionNumber = :collectionNumber OR "
            + "t.hiv = :hiv OR t.hbv = :hbv OR t.hcv = :hcv "
            + "OR t.syphilis = :syphilis) AND"
            + "(t.dateTested BETWEEN :dateTestedFrom and :dateTestedTo) AND "
            + "(t.isDeleted= :isDeleted)", TestResult.class);

    query.setParameter("isDeleted", Boolean.FALSE);
    String collectionNo = ((collectionNumber == null) ? "" : collectionNumber);
    query.setParameter("collectionNumber", collectionNo);
    String hivValue = ((hiv == null) ? "" : hiv);
    query.setParameter("hiv", hivValue);
    String hbvValue = ((hbv == null) ? "" : hbv);
    query.setParameter("hbv", hbvValue);
    String hcvValue = ((hcv == null) ? "" : hcv);
    query.setParameter("hcv", hcvValue);
    String syphilisValue = ((syphilis == null) ? "" : syphilis);
    query.setParameter("syphilis", syphilisValue);

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    try {
      Date from = (dateTestedFrom == null || dateTestedFrom.equals("")) ? dateFormat
          .parse("12/31/1970") : dateFormat.parse(dateTestedFrom);
      query.setParameter("dateTestedFrom", from);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    try {
      Date to = (dateTestedTo == null || dateTestedTo.equals("")) ? dateFormat
          .parse(dateFormat.format(new Date())) : dateFormat
          .parse(dateTestedTo);
      query.setParameter("dateTestedTo", to);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    List<TestResult> resultList = query.getResultList();
    return resultList;
  }

  public List<TestResult> findTestResultsByCollectionNumber(String collectionNumber) {
    TypedQuery<TestResult> query = em
        .createQuery(
            "SELECT t FROM TestResult t WHERE t.collectionNumber = :collectionNumber and t.isDeleted= :isDeleted",
            TestResult.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("collectionNumber", collectionNumber);
    return query.getResultList();
  }

  public Map<String, Map<Long, Long>> findNumberOfPositiveTests(String dateTestedFrom,
      String dateTestedTo, String aggregationCriteria, List<String> centers, List<String> sites) {

    TypedQuery<Object[]> query = em.createQuery("SELECT count(t), t.testedOn, t.bloodTest.name FROM TestResult t WHERE "
        + "t.result != t.bloodTest.correctResult AND "
        + "t.collectedSample.collectionCenter.id IN (:centerIds) AND "
        + "t.collectedSample.collectionSite.id IN (:siteIds) AND "
        + "t.testedOn BETWEEN :dateTestedFrom AND "
        + ":dateTestedTo AND (t.isDeleted= :isDeleted) AND (t.bloodTest.correctResult != '') "
        + " GROUP BY t.bloodTest.name, testedOn", Object[].class);

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

    query.setParameter("isDeleted", false);
    query.setParameter("centerIds", centerIds);
    query.setParameter("siteIds", siteIds);

    Map<String, Map<Long, Long>> resultMap = new HashMap<String, Map<Long,Long>>();
    TypedQuery<BloodTest> bloodTestQuery = em.createQuery("SELECT t FROM BloodTest t", BloodTest.class);
    for (BloodTest bt : bloodTestQuery.getResultList()) {
      if (!bt.getCorrectResult().isEmpty())
        resultMap.put(bt.getName(), new HashMap<Long, Long>());
    }

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date from = null;
    Date to = null;
    try {
      from = (dateTestedFrom == null || dateTestedFrom.equals("")) ? dateFormat
          .parse("11/01/2012") : dateFormat.parse(dateTestedFrom);
      query.setParameter("dateTestedFrom", from);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    try {
      to = (dateTestedTo == null || dateTestedTo.equals("")) ? dateFormat
          .parse(dateFormat.format(new Date())) : dateFormat
          .parse(dateTestedTo);
      query.setParameter("dateTestedTo", to);
    } catch (ParseException e) {
      e.printStackTrace();
    }

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

    Calendar gcal = new GregorianCalendar();
    Date lowerDate = null;
    Date upperDate = null;
    try {
      lowerDate = resultDateFormat.parse(resultDateFormat.format(from));
      upperDate = resultDateFormat.parse(resultDateFormat.format(to));
    } catch (ParseException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    
    for (String bloodTestName : resultMap.keySet()) {
      Map<Long, Long> m = resultMap.get(bloodTestName);
      gcal.setTime(lowerDate);
      while (gcal.getTime().before(upperDate) || gcal.getTime().equals(upperDate)) {
        m.put(gcal.getTime().getTime(), (long) 0);
        gcal.add(incrementBy, 1);
      }
    }

    for (Object[] result : resultList) {
      Date d = (Date) result[1];
      try {
        Date formattedDate = resultDateFormat.parse(resultDateFormat.format(d));
        Long utcTime = formattedDate.getTime();
        Map<Long, Long> m = resultMap.get(result[2]);
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

  public TestResult findTestResultById(Long testResultId) {
    return em.find(TestResult.class, testResultId);
  }

  public void addTestResult(TestResult testResult) {
    em.persist(testResult);
    updateProductStatus(testResult);
    em.flush();
  }

  public List<TestResult> findTestResults(
      String collectionNumber, String dateTestedFrom, String dateTestedTo) {

    TypedQuery<TestResult> query = null;
    if (collectionNumber == null || collectionNumber.isEmpty()) {
         if ((dateTestedFrom == null || dateTestedFrom.equals("")) && 
             (dateTestedTo == null || dateTestedTo.equals(""))
             )
           query = null;
         else {
           query = em.createQuery(
               "SELECT t FROM TestResult t WHERE " +
               "((t.testedOn is NULL) or " +
               " (t.testedOn >= :fromDate and t.testedOn <= :toDate)) and " +
               "t.isDeleted= :isDeleted",
               TestResult.class);

           Date from = getDateTestedFromOrDefault(dateTestedFrom);
           Date to = getDateTestedToOrDefault(dateTestedTo);
           query.setParameter("fromDate", from);
           query.setParameter("toDate", to);
           query.setParameter("isDeleted", Boolean.FALSE);
         }
    }
    else {
      query = em.createQuery("SELECT t FROM TestResult t WHERE " +
          "t.collectedSample.collectionNumber = :collectionNumber AND " +
          "t.isDeleted = :isDeleted",
          TestResult.class);
      query.setParameter("collectionNumber", collectionNumber);      
      query.setParameter("isDeleted", Boolean.FALSE);
    }

    if (query == null)
      return Arrays.asList(new TestResult[0]);
    else
      return query.getResultList();
  }

  private Date getDateTestedFromOrDefault(String dateTestedFrom) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date from = null;
    try {
      from = (dateTestedFrom == null || dateTestedFrom.equals("")) ? dateFormat
          .parse("11/01/2012") : dateFormat.parse(dateTestedFrom);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return from;      
  }

  private Date getDateTestedToOrDefault(String dateTestedTo) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date to = null;
    try {
      to = (dateTestedTo == null || dateTestedTo.equals("")) ? new Date() :
              dateFormat.parse(dateTestedTo);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return to;      
  }

  public TestResult updateTestResult(TestResult testResult) {
    TestResult existingTestResult = findTestResultById(testResult.getId());
    if (existingTestResult == null) {
      return null;
    }
    existingTestResult.copy(testResult);
    existingTestResult = em.merge(existingTestResult);
    updateProductStatus(existingTestResult);
    em.flush();
    return existingTestResult;
  }

  public void addAllTestResults(List<TestResult> testResults) {
    for (TestResult testResult : testResults) {
      updateProductStatus(testResult);
      em.persist(testResult);
    }
    em.flush();    
  }

  public List<CollectedSample> findUntestedCollections(String dateCollectedFrom,
      String dateCollectedTo) {

    Date from = getDateTestedFromOrDefault(dateCollectedFrom);
    Date to = getDateTestedToOrDefault(dateCollectedTo);
    TypedQuery<CollectedSample> query = em.createQuery(
        "SELECT c from CollectedSample c where " +
        "c.collectedOn >= :dateCollectedFrom AND c.collectedOn <= :dateCollectedTo AND " +
        "c.collectionNumber NOT IN (SELECT DISTINCT(t.collectedSample.collectionNumber) from TestResult t)",
        CollectedSample.class);
    query.setParameter("dateCollectedFrom", from);
    query.setParameter("dateCollectedTo", to);
    return query.getResultList();
  }

  public void saveTestResultsToWorksheet(String worksheetBatchId,
      Map<String, Map<String, String>> testResultChanges) {

    String worksheetQueryStr = "SELECT w from CollectionsWorksheet w LEFT JOIN FETCH w.testResults where w.worksheetBatchId = :worksheetBatchId";
    TypedQuery<CollectionsWorksheet> worksheetQuery = em.createQuery(worksheetQueryStr, CollectionsWorksheet.class);
    worksheetQuery.setParameter("worksheetBatchId", worksheetBatchId);
    CollectionsWorksheet worksheet = worksheetQuery.getSingleResult();
    worksheet = em.merge(worksheet);
    if (worksheet == null)
      return;

    for (String collectionId : testResultChanges.keySet()) {
      String queryStr = "SELECT t FROM TestResult t WHERE " +
      		              "t.collectedSample.id = :collectionId AND " +
      		              "t.worksheet.id = :worksheetId AND " +
      		              "t.isDeleted = :isDeleted";
      TypedQuery<TestResult> query = em.createQuery(queryStr, TestResult.class);
      query.setParameter("collectionId", Long.parseLong(collectionId));
      query.setParameter("worksheetId", worksheet.getId());
      query.setParameter("isDeleted", false);
      List<TestResult> testResults = query.getResultList();
      Map<String, TestResult> testResultsMap = new HashMap<String, TestResult>();
      for (TestResult t : testResults) {
        testResultsMap.put(t.getBloodTest().getName(), t);
      }

      CollectedSample collectedSample;
      collectedSample = collectedSampleRepository.findCollectedSampleById(Long.parseLong(collectionId));
      Map<String, String> changesForCollection = testResultChanges.get(collectionId);
      for (String testName : changesForCollection.keySet()) {
        String result = changesForCollection.get(testName);
        TestResult t = testResultsMap.get(testName);
        if (t != null) {
          if (result.trim().equals("")) {
            t.setIsDeleted(true);
          } else {
            t.setResult(result);
          }
          em.merge(t);
        } else {
          t = new TestResult();
          BloodTest bt = new BloodTest();
          bt.setName(testName);
          t.setBloodTest(bt);
          t.setTestedOn(new Date());
          t.setResult(result);
          t.setCollectedSample(collectedSample);
          t.setIsDeleted(false);
          t.setNotes("");
          t.setWorksheet(worksheet);
          worksheet.getTestResults().add(t);
          em.persist(t);
        }
      }
      for (Product p : collectedSample.getProducts()) {
        productRepository.updateProductInternalFields(p);
      }
    }
    em.flush();
  }

}
