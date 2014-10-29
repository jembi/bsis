package repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import model.collectedsample.CollectedSample;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TestBatchRepository {

  @PersistenceContext
  private EntityManager em;

 
  public void saveTestBatch(String firstDIN , String lastDIN ,String testBatchNumber) {
	  TestBatch testBatch = new TestBatch();
	  testBatch.setIsDeleted(0);
	  testBatch.setBatchNumber(testBatchNumber);
	  testBatch.setStatus(TestBatchStatus.OPEN);
	  updateCollectedSampleWithTestBatch(firstDIN , lastDIN ,  testBatch);
  }
  
  public void updateCollectedSampleWithTestBatch(String firstDIN , String lastDIN , TestBatch testBatch)
  {
	  	String queryString = "SELECT c FROM CollectedSample c WHERE c.collectionNumber >= :firstDIN and c.collectionNumber<= :lastDIN and c.isDeleted= :isDeleted";
		TypedQuery<CollectedSample> query = em.createQuery(queryString, CollectedSample.class);
		query.setParameter("isDeleted", false);
		query.setParameter("firstDIN", firstDIN);
		query.setParameter("lastDIN", lastDIN);
		
		List<CollectedSample> collectedSamples = query.getResultList();
		if (!collectedSamples.isEmpty()) {
		 
			for(CollectedSample collectedSample : collectedSamples){
				em.persist(testBatch);
				collectedSample.setTestBatch(testBatch);
				em.merge(collectedSample);
			}
		}
		
  }
  
  public List<TestBatch> getAllTestBatch() {
	    TypedQuery<TestBatch> query = em.createQuery(
	        "SELECT t FROM TestBatch t WHERE t.isDeleted= :isDeleted",
	        TestBatch.class);
	    query.setParameter("isDeleted", false);
	    return query.getResultList();
	  }

  public List<Object> findCollectedSamplesByTestBatch(
	      String testBatchNumber, String createdAfterDate,
	      String createdBeforeDate,Map<String, Object> pagingParams) {

	    String queryStr = "";
	    if (StringUtils.isNotBlank(testBatchNumber)) {
	      queryStr = "SELECT c FROM CollectedSample c LEFT JOIN FETCH c.testBatch WHERE " +
	                 "c.testBatch.batchNumber = :testBatchNumber AND " +
	                 "c.isDeleted=:isDeleted AND " +
	                 "c.testBatch.isDeleted=:isDeletedTestBatch";
	    } 
	    
	    TypedQuery<CollectedSample> query;
	    if (pagingParams.containsKey("sortColumn")) {
	      queryStr += " ORDER BY c." + pagingParams.get("sortColumn") + " " + pagingParams.get("sortDirection");
	    }
	    
	    query = em.createQuery(queryStr, CollectedSample.class);
	    query.setParameter("isDeleted", Boolean.FALSE);
	    query.setParameter("isDeletedTestBatch", 0);
	    query.setParameter("testBatchNumber", testBatchNumber);
	    
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
	    countQueryStr = countQueryStr.replaceFirst("LEFT JOIN FETCH c.testBatch", "");
	    TypedQuery<Long> countQuery = em.createQuery(countQueryStr, Long.class);
	    for (Parameter<?> parameter : query.getParameters()) {
	      countQuery.setParameter(parameter.getName(), query.getParameterValue(parameter));
	    }
	    return countQuery.getSingleResult().longValue();
	  }  
}