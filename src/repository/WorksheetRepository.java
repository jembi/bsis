package repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.collectedsample.CollectedSample;
import model.worksheet.Worksheet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class WorksheetRepository {

  @PersistenceContext
  private EntityManager em;

  public Worksheet addWorksheet(Worksheet worksheet) {
    em.persist(worksheet);
    em.flush();
    em.refresh(worksheet);
    return worksheet;
  }

  public Worksheet findWorksheetById(Long worksheetId) {
    String queryStr = "SELECT w from Worksheet w WHERE " +
    		"w.id=:worksheetId AND w.isDeleted=:isDeleted";
    TypedQuery<Worksheet> query = em.createQuery(queryStr, Worksheet.class);
    query.setParameter("worksheetId", worksheetId);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }

  public void addCollectionsToWorksheet(Long worksheetId, List<String> newCollectionNumbers) {

    String queryStr = "SELECT w from Worksheet w LEFT JOIN FETCH w.collectedSamples " +
    		"WHERE w.id=:worksheetId AND w.isDeleted=:isDeleted";
    TypedQuery<Worksheet> query = em.createQuery(queryStr, Worksheet.class);
    query.setParameter("worksheetId", worksheetId);
    query.setParameter("isDeleted", false);

    String collectionQueryStr = "SELECT c from CollectedSample c " +
    		"LEFT JOIN FETCH c.worksheets WHERE " +
        "c.collectionNumber IN :collectionNumbers";
    TypedQuery<CollectedSample> collectionsQuery = em.createQuery(collectionQueryStr, CollectedSample.class);
    collectionsQuery.setParameter("collectionNumbers", newCollectionNumbers);
    List<CollectedSample> newCollections = collectionsQuery.getResultList();

    Worksheet worksheet = query.getSingleResult();
    Set<String> existingCollectionNumbers = new HashSet<String>();
    for (CollectedSample c : worksheet.getCollectedSamples()) {
      existingCollectionNumbers.add(c.getCollectionNumber());
    }

    for (CollectedSample c : newCollections) {
      if (existingCollectionNumbers.contains(c.getCollectionNumber()))
        continue;
      worksheet.getCollectedSamples().add(c);
      c.getWorksheets().add(worksheet);
    }

    em.merge(worksheet);
    em.flush();
  }

  public List<Worksheet> findWorksheets(String worksheetNumber, List<String> worksheetTypes) {

    List<Integer> worksheetTypeIds = new ArrayList<Integer>();
    for (String worksheetTypeIdStr : worksheetTypes) {
      worksheetTypeIds.add(Integer.parseInt(worksheetTypeIdStr));
    }

    worksheetTypeIds.add(-1);
    TypedQuery<Worksheet> query = null;
    if (StringUtils.isBlank(worksheetNumber)) {
      String queryStr = "SELECT DISTINCT w FROM Worksheet w LEFT JOIN FETCH w.collectedSamples WHERE " +
      		"w.worksheetType.id IN (:worksheetTypeIds) AND w.isDeleted=:isDeleted";
      query = em.createQuery(queryStr, Worksheet.class);
      query.setParameter("worksheetTypeIds", worksheetTypeIds);
      query.setParameter("isDeleted", false);
    } else {
      String queryStr = "SELECT DISTINCT w FROM Worksheet w LEFT JOIN FETCH w.collectedSamples WHERE " +
      		"w.worksheetNumber = :worksheetNumber AND w.isDeleted=:isDeleted";
      query = em.createQuery(queryStr, Worksheet.class);
      query.setParameter("worksheetNumber", worksheetNumber);
      query.setParameter("isDeleted", false);
    }
    if (query == null)
      return Arrays.asList(new Worksheet[0]);
    return query.getResultList();
  }

  public void deleteWorksheet(Long worksheetId) {
    Worksheet existingWorksheet = findWorksheetById(worksheetId);
    existingWorksheet.setIsDeleted(Boolean.TRUE);
    em.merge(existingWorksheet);
    em.flush();
  }


}
