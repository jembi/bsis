package repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.collectedsample.CollectedSample;
import model.worksheet.Worksheet;

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

}
