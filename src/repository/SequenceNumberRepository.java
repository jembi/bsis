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
    Integer mm = today.monthOfYear().get();
    String mmStr = String.format("%02d", mm);
    String yyStr = String.format("%02d", yy);
    query.setParameter("sequenceNumberContext", mmStr + yyStr);

    SequenceNumberStore seqNumStore = null;
    Long lastNumber = (long)0;
    String prefix;
    boolean valuePresentInTable = true;
    try {
      seqNumStore = query.getSingleResult();
      lastNumber = seqNumStore.getLastNumber();
      prefix = seqNumStore.getPrefix();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      valuePresentInTable = false;
      prefix = "C";
      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable("CollectedSample");
      seqNumStore.setColumnName("collectionNumber");
      seqNumStore.setPrefix(prefix);
      seqNumStore.setSequenceNumberContext(mmStr + yyStr);
    }

    String lastNumberStr = String.format("%06d", lastNumber);
    // may need a prefix for center where the number is generated
    String collectionNumber = prefix + mmStr + yyStr + lastNumberStr;
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
    Integer mm = today.monthOfYear().get();
    Integer yy = today.yearOfCentury().get();

    String mmStr = String.format("%02d", mm);
    String yyStr = String.format("%02d", yy);
    query.setParameter("sequenceNumberContext", mmStr + yyStr);

    SequenceNumberStore seqNumStore = null;
    Long lastNumber = (long)0;
    String prefix;
    boolean valuePresentInTable = true;
    try {
      seqNumStore = query.getSingleResult();
      lastNumber = seqNumStore.getLastNumber();
      prefix = seqNumStore.getPrefix();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      valuePresentInTable = false;
      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable("Request");
      seqNumStore.setColumnName("requestNumber");
      prefix = "R";
      seqNumStore.setPrefix(prefix);
      seqNumStore.setSequenceNumberContext(mmStr + yyStr);
    }

    String lastNumberStr = String.format("%06d", lastNumber);
    // may need a prefix for center where the number is generated
    String requestNumber = prefix + mmStr + yyStr + lastNumberStr;
    lastNumber = lastNumber + 1;
    seqNumStore.setLastNumber(lastNumber);
    if (valuePresentInTable) {
      em.merge(seqNumStore);
    } else {
      em.persist(seqNumStore);
    }

    em.flush();
    return requestNumber;
  }
}
