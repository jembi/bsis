package repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.TestResult;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Repository
@Transactional
public class TestResultRepository {
  @PersistenceContext
  private EntityManager em;

  public void saveTestResult(TestResult testResult) {
    em.persist(testResult);
    em.flush();
  }

  public TestResult updateTestResult(TestResult testResult, Long testResultId) {
    TestResult existingTestResult = em.find(TestResult.class, testResultId);
    existingTestResult.copy(testResult);
    em.merge(existingTestResult);
    em.flush();
    return existingTestResult;
  }

  public void deleteAllTestResults() {
    Query query = em.createQuery("DELETE FROM TestResult t");
    query.executeUpdate();
  }

  public List<TestResult> getAllTestResults() {
    Query query = em.createQuery(
        "SELECT t FROM TestResult t WHERE t.isDeleted= :isDeleted",
        TestResult.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.getResultList();
  }

  public List<TestResult> getTestResults(Date fromDate, Date toDate) {
    TypedQuery<TestResult> query = em
        .createQuery(
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

  public void deleteTestResult(String collectionNumber) {
    TestResult existingTestResult = findTestResultByCollectionNumber(collectionNumber);
    existingTestResult.setIsDeleted(Boolean.TRUE);
    em.merge(existingTestResult);
    em.flush();
  }

  public List<TestResult> getAllTestResults(String collectionNumber) {
    Query query = em
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

  public TestResult findTestResultByCollectionNumber(String collectionNumber) {
    TypedQuery<TestResult> query = em
        .createQuery(
            "SELECT t FROM TestResult t WHERE t.collectionNumber = :collectionNumber and t.isDeleted= :isDeleted",
            TestResult.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("collectionNumber", collectionNumber);
    List<TestResult> testResults = query.getResultList();
    if (CollectionUtils.isEmpty(testResults)) {
      return null;
    }
    return testResults.get(0);
  }

  public TestResult updateOrAddTestResult(TestResult testResult) {
    TestResult existingTestResult = findTestResultByCollectionNumber(testResult
        .getCollectionNumber());
    if (existingTestResult == null) {
      testResult.setIsDeleted(false);
      saveTestResult(testResult);
      return testResult;
    }
    existingTestResult.copy(testResult);
    existingTestResult.setIsDeleted(false);
    em.merge(existingTestResult);
    em.flush();
    return existingTestResult;
  }

  public Map<Long, Long> findNumberOfPositiveTests(String dateTestedFrom,
      String dateTestedTo, String aggregationCriteria, String hiv, String hbv,
      String hcv, String syphilis, String none) {
    String queryStr = "SELECT count(t), t.dateTested FROM TestResult t WHERE "
        + "t.dateTested BETWEEN :dateTestedFrom AND "
        + ":dateTestedTo AND (t.isDeleted= :isDeleted) ";
    List<String> conditions = new ArrayList<String>();
    if (hiv.equals("reactive"))
      conditions.add(" hiv='reactive' ");
    if (hbv.equals("reactive"))
      conditions.add(" hbv='reactive' ");
    if (hcv.equals("reactive"))
      conditions.add(" hcv='reactive' ");
    if (syphilis.equals("reactive"))
      conditions.add(" syphilis='reactive' ");
    if (none.equals("none"))
      conditions
          .add("(hiv='negative' AND hbv='negative' AND hcv='negative' AND syphilis='negative')");

    if (conditions.size() > 0) {
      String conditionStr = " (" + conditions.get(0);
      for (int i = 1; i < conditions.size(); ++i) {
        conditionStr += "OR " + conditions.get(i) + " ";
      }
      conditionStr += ") ";
      queryStr += "AND " + conditionStr;
    }
    TypedQuery<Object[]> query = em.createQuery(queryStr
        + " GROUP BY dateTested", Object[].class);

    query.setParameter("isDeleted", Boolean.FALSE);

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date from = null;
    Date to = null;
    try {
      from = (dateTestedFrom == null || dateTestedFrom.equals("")) ? dateFormat
          .parse("12/31/2011") : dateFormat.parse(dateTestedFrom);
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
    String groupByFunc = "DAY";
    int incrementBy = Calendar.DAY_OF_YEAR;
    if (aggregationCriteria.equals("monthly")) {
      groupByFunc = "MONTH";
      incrementBy = Calendar.MONTH;
      resultDateFormat = new SimpleDateFormat("MM/01/yyyy");
    } else if (aggregationCriteria.equals("yearly")) {
      groupByFunc = "YEAR";
      incrementBy = Calendar.YEAR;
      resultDateFormat = new SimpleDateFormat("01/01/yyyy");
    }

    List<Object[]> resultList = query.getResultList();

    Map<Long, Long> m = new HashMap<Long, Long>();
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
    gcal.setTime(lowerDate);
    while (gcal.getTime().before(upperDate) || gcal.getTime().equals(upperDate)) {
      m.put(gcal.getTime().getTime(), (long) 0);
      gcal.add(incrementBy, 1);
    }

    for (Object[] result : resultList) {
      Date d = (Date) result[1];
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
    return m;
  }

}
