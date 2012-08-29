package repository;

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
        Query query = em.createQuery("SELECT t FROM TestResult t WHERE t.isDeleted= :isDeleted", TestResult.class);
        query.setParameter("isDeleted", Boolean.FALSE);
        return query.getResultList();
    }

    public List<TestResult> getTestResults(Date fromDate, Date toDate) {
        TypedQuery<TestResult> query = em.createQuery(
                "SELECT t FROM TestResult t WHERE t.dateCollected >= :fromDate and t.dateCollected<= :toDate and t.isDeleted= :isDeleted", TestResult.class);
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
        Query query = em.createQuery("SELECT t FROM TestResult t WHERE t.collectionNumber= :collectionNumber and t.isDeleted= :isDeleted", TestResult.class);
        query.setParameter("isDeleted", Boolean.FALSE);
        query.setParameter("collectionNumber", collectionNumber);
        return query.getResultList();
    }

    public TestResult find(String selectedTestResultId) {
        TypedQuery<TestResult> query = em.createQuery("SELECT t FROM TestResult t WHERE t.testResultId= :testResultId and t.isDeleted= :isDeleted", TestResult.class);
        query.setParameter("isDeleted", Boolean.FALSE);
        query.setParameter("testResultId", Long.parseLong(selectedTestResultId));
        return query.getSingleResult();
    }
}
