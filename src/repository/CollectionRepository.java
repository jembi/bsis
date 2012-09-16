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

import model.Collection;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Repository
@Transactional
public class CollectionRepository {
  @PersistenceContext
  private EntityManager em;

  public void saveCollection(Collection collection) {
    em.persist(collection);
    em.flush();
  }

  public Collection updateCollection(Collection collection, Long collectionId) {
    Collection existingCollection = findCollectionById(collectionId);
    existingCollection.copy(collection);
    em.merge(existingCollection);
    em.flush();
    return existingCollection;
  }

  public Collection findCollectionById(Long collectionId) {
    return em.find(Collection.class, collectionId);
  }

  public void deleteAllCollections() {
    Query query = em.createQuery("DELETE FROM Collection c");
    query.executeUpdate();
  }

  public Collection findCollectionByNumber(String collectionNumber) {
    TypedQuery<Collection> query = em
        .createQuery(
            "SELECT c FROM Collection c WHERE c.collectionNumber = :collectionNumber and c.isDeleted= :isDeleted",
            Collection.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("collectionNumber", collectionNumber);
    List<Collection> collections = query.getResultList();
    if (CollectionUtils.isEmpty(collections)) {
      return null;
    }
    return collections.get(0);
  }

  public List<Collection> getAllCollections() {
    Query query = em.createQuery(
        "SELECT c FROM Collection c WHERE c.isDeleted= :isDeleted",
        Collection.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.getResultList();
  }

  public List<Collection> getCollections(Date fromDate, Date toDate) {
    TypedQuery<Collection> query = em
        .createQuery(
            "SELECT c FROM Collection c WHERE c.dateCollected >= :fromDate and c.dateCollected<= :toDate and c.isDeleted= :isDeleted",
            Collection.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    List<Collection> collections = query.getResultList();
    if (CollectionUtils.isEmpty(collections)) {
      return new ArrayList<Collection>();
    }
    return collections;
  }

  public void deleteCollection(Long collectionId) {
    Collection existingCollection = findCollectionById(collectionId);
    existingCollection.setIsDeleted(Boolean.TRUE);
    em.merge(existingCollection);
    em.flush();
  }

  public List<Collection> findAnyCollectionMatching(String collectionNumber,
      Long sampleNumber, Long shippingNumber, String dateCollectedFrom,
      String dateCollectedTo, List<String> centers) {

    // TODO: fix join condition
    TypedQuery<Collection> query = em.createQuery(
        "SELECT c FROM Collection c, Location L WHERE "
            + " L.locationId=c.centerId AND L.isCenter=TRUE AND ("
            + "c.collectionNumber = :collectionNumber OR "
            + "c.sampleNumber = :sampleNumber OR "
            + "c.shippingNumber = :shippingNumber OR "
            + "L.name IN (:centers)) AND ("
            + "c.dateCollected BETWEEN :dateCollectedFrom AND "
            + ":dateCollectedTo" + ") AND " + "(c.isDeleted= :isDeleted)",
        Collection.class);

    query.setParameter("isDeleted", Boolean.FALSE);
    String collectionNo = ((collectionNumber == null) ? "" : collectionNumber);
    query.setParameter("collectionNumber", collectionNo);
    Long sampleNo = ((sampleNumber == null) ? -1 : sampleNumber);
    query.setParameter("sampleNumber", sampleNo);
    Long shippingNo = ((shippingNumber == null) ? -1 : shippingNumber);
    query.setParameter("shippingNumber", shippingNo);

    query.setParameter("centers", centers);

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date from = (dateCollectedFrom == null || dateCollectedFrom.equals("")) ? dateFormat
          .parse("1970-12-31") : dateFormat.parse(dateCollectedFrom);
      query.setParameter("dateCollectedFrom", from);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    try {
      Date to = (dateCollectedTo == null || dateCollectedTo.equals("")) ? dateFormat
          .parse(dateFormat.format(new Date())) : dateFormat
          .parse(dateCollectedTo);
      query.setParameter("dateCollectedTo", to);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    List<Collection> resultList = query.getResultList();
    return resultList;
  }
}
