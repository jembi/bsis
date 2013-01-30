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
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.bloodbagtype.BloodBagType;
import model.collectedsample.CollectedSample;
import model.testresults.TestResult;
import model.worksheet.CollectionsWorksheet;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import viewmodel.TestResultViewModel;

@Repository
@Transactional
public class CollectedSampleRepository {
  @PersistenceContext
  private EntityManager em;

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
    em.merge(existingCollectedSample);
    em.flush();
    return existingCollectedSample;
  }

  public CollectedSample findCollectedSampleById(Long collectedSampleId) {
    String queryString = "SELECT c FROM CollectedSample c LEFT JOIN FETCH c.donor WHERE c.id = :collectedSampleId and c.isDeleted = :isDeleted";
    TypedQuery<CollectedSample> query = em.createQuery(queryString, CollectedSample.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.setParameter("collectedSampleId", collectedSampleId).getSingleResult();
  }

  public List<Object> findCollectedSampleByCollectionNumber(
      String collectionNumber, List<BloodBagType> bloodBagTypes, List<Long> centerIds, List<Long> siteIds, String dateCollectedFrom,
      String dateCollectedTo, Map<String, Object> pagingParams) {

    String queryStr = "";
    if (collectionNumber != null && !collectionNumber.trim().isEmpty()) {
      queryStr = "SELECT c FROM CollectedSample c LEFT JOIN FETCH c.donor WHERE " +
      		       "c.collectionNumber = :collectionNumber AND " +
                 "c.bloodBagType IN :bloodBagTypes AND " +
                 "c.collectionCenter.id IN :centerIds AND " +
                 "c.collectionSite.id IN :siteIds AND " +
                 "c.collectedOn >= :dateCollectedFrom AND c.collectedOn <= :dateCollectedTo AND " +
                 "c.isDeleted=:isDeleted";
    } else {
      queryStr = "SELECT c FROM CollectedSample c LEFT JOIN FETCH c.donor WHERE " +
          "c.bloodBagType IN :bloodBagTypes AND " +
          "c.collectionCenter.id IN :centerIds AND " +
          "c.collectionSite.id IN :siteIds AND " +
          "c.collectedOn >= :dateCollectedFrom AND c.collectedOn <= :dateCollectedTo AND " +
          "c.isDeleted=:isDeleted";
    }

    TypedQuery<CollectedSample> query;
    if (pagingParams.containsKey("sortColumn")) {
      queryStr += " ORDER BY c." + pagingParams.get("sortColumn") + " " + pagingParams.get("sortDirection");
    }
    
    query = em.createQuery(queryStr, CollectedSample.class);
    query.setParameter("isDeleted", Boolean.FALSE);

    if (collectionNumber != null && !collectionNumber.trim().isEmpty())
      query.setParameter("collectionNumber", collectionNumber);

    query.setParameter("bloodBagTypes", bloodBagTypes);
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

  public List<CollectedSample> findCollectedSampleByShippingNumber(
      String shippingNumber, String dateCollectedFrom,
      String dateCollectedTo) {

    TypedQuery<CollectedSample> query = em
        .createQuery(
            "SELECT c FROM CollectedSample c WHERE " +
            "c.shippingNumber = :shippingNumber and " +
            "((c.collectedOn is NULL) or " +
            " (c.collectedOn >= :fromDate and c.collectedOn <= :toDate)) and " +
            "c.isDeleted= :isDeleted",
            CollectedSample.class);

    Date from = getDateCollectedFromOrDefault(dateCollectedFrom);
    Date to = getDateCollectedToOrDefault(dateCollectedTo);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("shippingNumber", shippingNumber);
    query.setParameter("fromDate", from);
    query.setParameter("toDate", to);

    return query.getResultList();
  }

  public List<CollectedSample> findCollectedSampleBySampleNumber(
      String sampleNumber, String dateCollectedFrom,
      String dateCollectedTo) {

    TypedQuery<CollectedSample> query = em
        .createQuery(
            "SELECT c FROM CollectedSample c WHERE " +
            "c.sampleNumber = :sampleNumber and " +
            "((c.collectedOn is NULL) or " +
            " (c.collectedOn >= :fromDate and c.collectedOn <= :toDate)) and " +
            "c.isDeleted= :isDeleted",
            CollectedSample.class);

    Date from = getDateCollectedFromOrDefault(dateCollectedFrom);
    Date to = getDateCollectedToOrDefault(dateCollectedTo);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("sampleNumber", sampleNumber);
    query.setParameter("fromDate", from);
    query.setParameter("toDate", to);

    return query.getResultList();
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

  public CollectedSample updateOrAddCollectedSample(CollectedSample collectedSample) {
//    CollectedSample existingCollectedSample =
//        findCollectedSampleByCollectionNumber(collectedSample.getCollectionNumber());
//    if (existingCollectedSample == null) {
//      collectedSample.setIsDeleted(false);
//      saveCollectedSample(collectedSample);
//      return collectedSample;
//    }
//    existingCollectedSample.copy(collectedSample);
//    existingCollectedSample.setIsDeleted(false);
//    em.merge(existingCollectedSample);
//    em.flush();
//    return existingCollectedSample;
    return null;
  }

  public Map<Long, Long> findNumberOfCollectedSamples(String dateCollectedFrom,
      String dateCollectedTo, String aggregationCriteria, List<String> centers, List<String> sites) {

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

    TypedQuery<Object[]> query = em.createQuery(
        "SELECT count(c), c.collectedOn FROM CollectedSample c WHERE " +
        "c.collectionCenter.id IN (:centerIds) AND c.collectionSite.id IN (:siteIds) AND " +
        "c.collectedOn BETWEEN :dateCollectedFrom AND " +
        ":dateCollectedTo AND (c.isDeleted= :isDeleted) GROUP BY " +
        "collectedOn", Object[].class);

    query.setParameter("centerIds", centerIds);
    query.setParameter("siteIds", siteIds);
    query.setParameter("isDeleted", Boolean.FALSE);

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date from = null;
    Date to = null;
    try {
      from = (dateCollectedFrom == null || dateCollectedFrom.equals("")) ? dateFormat
          .parse("11/01/2012") : dateFormat.parse(dateCollectedFrom);
      query.setParameter("dateCollectedFrom", from);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    try {
      to = (dateCollectedTo == null || dateCollectedTo.equals("")) ? dateFormat
          .parse(dateFormat.format(new Date())) : dateFormat
          .parse(dateCollectedTo);
      query.setParameter("dateCollectedTo", to);
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

  public void addCollectedSample(CollectedSample collectedSample) {
    em.persist(collectedSample);
    em.flush();
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

  public CollectedSample findSingleCollectedSampleByCollectionNumber(
      String collectionNumber) {
    List<CollectedSample> collectedSamples = findCollectedSampleByCollectionNumber(collectionNumber);
    if (collectedSamples != null && collectedSamples.size() == 1)
      return collectedSamples.get(0);
    return null;
  }

  public void addAllCollectedSamples(List<CollectedSample> collectedSamples) {
    for (CollectedSample c : collectedSamples) {
      em.persist(c);
    }
    em.flush();
  }

  public List<CollectedSample> findCollectedSampleByCollectionNumber(
      String collectionNumber) {
    String queryString = "SELECT c FROM CollectedSample c LEFT JOIN FETCH c.donor WHERE c.collectionNumber = :collectionNumber and c.isDeleted = :isDeleted";
    TypedQuery<CollectedSample> query = em.createQuery(queryString, CollectedSample.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("collectionNumber", collectionNumber);
    return query.getResultList();
  }

  public void saveAsWorksheet(String collectionNumber,
      List<BloodBagType> bloodBagTypes, List<Long> centerIds,
      List<Long> siteIds, String dateCollectedFrom, String dateCollectedTo, String worksheetBatchId) throws Exception {

    Map<String, Object> pagingParams = new HashMap<String, Object>();
    List<Object> results = findCollectedSampleByCollectionNumber(collectionNumber, bloodBagTypes,
                                          centerIds, siteIds,
                                          dateCollectedFrom, dateCollectedTo,
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
}
