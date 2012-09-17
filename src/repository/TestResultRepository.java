package repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

  public void deleteTestResult(Long testResultId) {
    TestResult existingTestResult = em.find(TestResult.class, testResultId);
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

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date from = (dateTestedFrom == null || dateTestedFrom.equals("")) ? dateFormat
          .parse("1970-12-31") : dateFormat.parse(dateTestedFrom);
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
}
