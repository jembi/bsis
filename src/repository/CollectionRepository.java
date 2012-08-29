package repository;

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

    public Collection findCollection(String collectionNumber) {
        TypedQuery<Collection> query = em.createQuery(
                "SELECT c FROM Collection c WHERE c.collectionNumber = :collectionNumber and c.isDeleted= :isDeleted", Collection.class);
        query.setParameter("isDeleted", Boolean.FALSE);
        query.setParameter("collectionNumber", collectionNumber);
        List<Collection> collections = query.getResultList();
        if (CollectionUtils.isEmpty(collections)) {
            return null;
        }
        return collections.get(0);
    }

    public List<Collection> getAllCollections() {
        Query query = em.createQuery("SELECT c FROM Collection c WHERE c.isDeleted= :isDeleted", Collection.class);
        query.setParameter("isDeleted", Boolean.FALSE);
        return query.getResultList();
    }

    public List<Collection> getCollections(Date fromDate, Date toDate) {
        TypedQuery<Collection> query = em.createQuery(
                "SELECT c FROM Collection c WHERE c.dateCollected >= :fromDate and c.dateCollected<= :toDate and c.isDeleted= :isDeleted", Collection.class);
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
}
