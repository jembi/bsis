package repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.sequencenumber.SequenceNumberStore;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SequenceNumberRepository {

  @PersistenceContext
  private EntityManager em;
  
  synchronized public String getNextCollectionNumber() {
    String queryStr = "SELECT s from SequenceNumberStore s " +
    		              "where s.targetTable=:targetTable AND " +
    		              " s.columnName=:columnName AND " +
    		              "s.sequenceNumberContext=:sequenceNumberContext";
    TypedQuery<SequenceNumberStore> query = em.createQuery(queryStr, SequenceNumberStore.class);
    query.setParameter("targetTable", "CollectedSample");
    query.setParameter("columnName", "collectionNumber");
    // use last two digits of year
    DateTime today = new DateTime();
    Integer yy = today.yearOfCentury().get();
    query.setParameter("sequenceNumberContext", yy.toString());

    SequenceNumberStore seqNumStore = null;
    Long lastNumber = (long)0;
    boolean valuePresentInTable = true;
    try {
      seqNumStore = query.getSingleResult();
      lastNumber = seqNumStore.getLastNumber();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      valuePresentInTable = false;
      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable("CollectedSample");
      seqNumStore.setColumnName("collectionNumber");
      seqNumStore.setSequenceNumberContext(yy.toString());
    }

    String yyStr = String.format("%02d", yy);
    String lastNumberStr = String.format("%06d", lastNumber);
    // may need a prefix for center where the number is generated
    String collectionNumber = "" + yyStr + lastNumberStr;
    lastNumber = lastNumber + 1;
    seqNumStore.setLastNumber(lastNumber);
    if (valuePresentInTable) {
      em.merge(seqNumStore);
    } else {
      em.persist(seqNumStore);
    }

    em.flush();
    return collectionNumber;
  }

  synchronized public String getNextRequestNumber() {
    String queryStr = "SELECT s from SequenceNumberStore s " +
                      "where s.targetTable=:targetTable AND " +
                      " s.columnName=:columnName AND " +
                      "s.sequenceNumberContext=:sequenceNumberContext";
    TypedQuery<SequenceNumberStore> query = em.createQuery(queryStr, SequenceNumberStore.class);
    query.setParameter("targetTable", "Request");
    query.setParameter("columnName", "requestNumber");
    // use last two digits of year
    DateTime today = new DateTime();
    Integer yy = today.yearOfCentury().get();
    query.setParameter("sequenceNumberContext", yy.toString());

    SequenceNumberStore seqNumStore = null;
    Long lastNumber = (long)0;
    boolean valuePresentInTable = true;
    try {
      seqNumStore = query.getSingleResult();
      lastNumber = seqNumStore.getLastNumber();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      valuePresentInTable = false;
      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable("Request");
      seqNumStore.setColumnName("requestNumber");
      seqNumStore.setSequenceNumberContext(yy.toString());
    }

    String yyStr = String.format("%02d", yy);
    String lastNumberStr = String.format("%06d", lastNumber);
    // may need a prefix for center where the number is generated
    String collectionNumber = "" + yyStr + lastNumberStr;
    lastNumber = lastNumber + 1;
    seqNumStore.setLastNumber(lastNumber);
    if (valuePresentInTable) {
      em.merge(seqNumStore);
    } else {
      em.persist(seqNumStore);
    }

    em.flush();
    return collectionNumber;
  }

}
