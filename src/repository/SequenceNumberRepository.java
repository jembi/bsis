package repository;

import model.sequencenumber.SequenceNumberStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class SequenceNumberRepository {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private GeneralConfigRepository generalConfigRepository;

  synchronized public String getNextDonationIdentificationNumber() {
    String queryStr = "SELECT s from SequenceNumberStore s " +
            "where s.targetTable=:targetTable AND " +
            " s.columnName=:columnName";
    TypedQuery<SequenceNumberStore> query = em.createQuery(queryStr, SequenceNumberStore.class);
    query.setParameter("targetTable", "Donation");
    query.setParameter("columnName", "donationIdentificationNumber");

    SequenceNumberStore seqNumStore = null;
    Long lastNumber = (long) 0;
    String prefix;
    boolean valuePresentInTable = true;
    try {
      seqNumStore = query.getSingleResult();
      lastNumber = seqNumStore.getLastNumber();
      prefix = seqNumStore.getPrefix();
    } catch (NoResultException ex) {
      //ex.printStackTrace();
      valuePresentInTable = false;
      prefix = "C";
      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable("Donation");
      seqNumStore.setColumnName("donationIdentificationNumber");
      seqNumStore.setPrefix(prefix);
    }

    String lastNumberStr = String.format("%06d", lastNumber);
    // may need a prefix for center where the number is generated
    String donationIdentificationNumber = prefix + lastNumberStr;
    lastNumber = lastNumber + 1;
    seqNumStore.setLastNumber(lastNumber);
    if (valuePresentInTable) {
      em.merge(seqNumStore);
    } else {
      em.persist(seqNumStore);
    }

    em.flush();
    return donationIdentificationNumber;
  }

  synchronized public String getNextRequestNumber() {
    String queryStr = "SELECT s from SequenceNumberStore s " +
            "where s.targetTable=:targetTable AND " +
            " s.columnName=:columnName";
    TypedQuery<SequenceNumberStore> query = em.createQuery(queryStr, SequenceNumberStore.class);
    query.setParameter("targetTable", "Request");
    query.setParameter("columnName", "requestNumber");

    SequenceNumberStore seqNumStore = null;
    Long lastNumber = (long) 0;
    String prefix;
    boolean valuePresentInTable = true;
    try {
      seqNumStore = query.getSingleResult();
      lastNumber = seqNumStore.getLastNumber();
      prefix = seqNumStore.getPrefix();
    } catch (NoResultException ex) {
      //ex.printStackTrace();
      valuePresentInTable = false;
      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable("Request");
      seqNumStore.setColumnName("requestNumber");
      prefix = "R";
      seqNumStore.setPrefix(prefix);
    }

    String lastNumberStr = String.format("%06d", lastNumber);
    // may need a prefix for center where the number is generated
    String requestNumber = prefix + lastNumberStr;
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

  synchronized public String getNextDonorNumber() {
    String queryStr = "SELECT s from SequenceNumberStore s " +
            "where s.targetTable=:targetTable AND " +
            " s.columnName=:columnName";
    TypedQuery<SequenceNumberStore> query = em.createQuery(queryStr, SequenceNumberStore.class);
    query.setParameter("targetTable", "Donor");
    query.setParameter("columnName", "donorNumber");

    SequenceNumberStore seqNumStore = null;
    Long lastNumber = (long) 0;
    String prefix;
    boolean valuePresentInTable = true;
    try {
      seqNumStore = query.getSingleResult();
      lastNumber = seqNumStore.getLastNumber();
      //prefix = seqNumStore.getPrefix();
    } catch (NoResultException ex) {
      //ex.printStackTrace();
      valuePresentInTable = false;
      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable("Donor");
      seqNumStore.setColumnName("donorNumber");
    }


    if (lastNumber == 0) {
      lastNumber++;
    }
    String donorNumberFormat = generalConfigRepository.getGeneralConfigByName("donor.donorNumberFormat").getValue();
    lastNumber = lastNumber + 1;
    seqNumStore.setLastNumber(lastNumber);
    if (valuePresentInTable) {
      em.merge(seqNumStore);
    } else {
      em.persist(seqNumStore);
    }

    em.flush();
    return String.format(donorNumberFormat, lastNumber);
  }


  synchronized public String getSequenceNumber(String targetTable, String columnName) {
    String queryStr = "SELECT s from SequenceNumberStore s " +
            "where s.targetTable=:targetTable AND " +
            " s.columnName=:columnName ";
    TypedQuery<SequenceNumberStore> query = em.createQuery(queryStr, SequenceNumberStore.class);
    query.setParameter("targetTable", targetTable);
    query.setParameter("columnName", columnName);

    SequenceNumberStore seqNumStore = null;
    Long lastNumber = (long) 0;


    try {
      seqNumStore = query.getSingleResult();
      lastNumber = seqNumStore.getLastNumber();

    } catch (NoResultException ex) {
      //ex.printStackTrace();

      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable("Donor");
      seqNumStore.setColumnName("donorNumber");


    }


    if (lastNumber == 0) {
      lastNumber++;
    }


    em.flush();
    return String.format("%06d", lastNumber);
  }


  synchronized public List<String> getBatchDonationIdentificationNumbers(int numDonations) {
    String queryStr = "SELECT s from SequenceNumberStore s " +
            "where s.targetTable=:targetTable AND " +
            " s.columnName=:columnName";
    TypedQuery<SequenceNumberStore> query = em.createQuery(queryStr, SequenceNumberStore.class);
    query.setParameter("targetTable", "Donation");
    query.setParameter("columnName", "donationIdentificationNumber");

    SequenceNumberStore seqNumStore = null;
    Long lastNumber = (long) 0;
    String prefix;
    boolean valuePresentInTable = true;
    try {
      seqNumStore = query.getSingleResult();
      lastNumber = seqNumStore.getLastNumber();
      prefix = seqNumStore.getPrefix();
    } catch (NoResultException ex) {
      //ex.printStackTrace();
      valuePresentInTable = false;
      prefix = "C";
      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable("Donation");
      seqNumStore.setColumnName("donationIdentificationNumber");
      seqNumStore.setPrefix(prefix);
    }

    List<String> donationIdentificationNumbers = new ArrayList<>();
    for (int i = 0; i < numDonations; ++i) {
      String lastNumberStr = String.format("%06d", lastNumber + i);
      // may need a prefix for center where the number is generated
      String donationIdentificationNumber = prefix + lastNumberStr;
      donationIdentificationNumbers.add(donationIdentificationNumber);
    }
    lastNumber = lastNumber + numDonations;
    seqNumStore.setLastNumber(lastNumber);
    if (valuePresentInTable) {
      em.merge(seqNumStore);
    } else {
      em.persist(seqNumStore);
    }

    em.flush();
    return donationIdentificationNumbers;
  }

  synchronized public List<String> getBatchRequestNumbers(int numRequests) {
    String queryStr = "SELECT s from SequenceNumberStore s " +
            "where s.targetTable=:targetTable AND " +
            " s.columnName=:columnName";
    TypedQuery<SequenceNumberStore> query = em.createQuery(queryStr, SequenceNumberStore.class);
    query.setParameter("targetTable", "Request");
    query.setParameter("columnName", "requestNumber");

    SequenceNumberStore seqNumStore = null;
    Long lastNumber = (long) 0;
    String prefix;
    boolean valuePresentInTable = true;
    try {
      seqNumStore = query.getSingleResult();
      lastNumber = seqNumStore.getLastNumber();
      prefix = seqNumStore.getPrefix();
    } catch (NoResultException ex) {
      //ex.printStackTrace();
      valuePresentInTable = false;
      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable("Request");
      seqNumStore.setColumnName("requestNumber");
      prefix = "R";
      seqNumStore.setPrefix(prefix);
    }

    List<String> requestNumbers = new ArrayList<>();
    for (int i = 0; i < numRequests; ++i) {
      String lastNumberStr = String.format("%06d", lastNumber + i);
      // may need a prefix for center where the number is generated
      String requestNumber = prefix + lastNumberStr;
      requestNumbers.add(requestNumber);
    }
    lastNumber = lastNumber + numRequests;
    seqNumStore.setLastNumber(lastNumber);
    if (valuePresentInTable) {
      em.merge(seqNumStore);
    } else {
      em.persist(seqNumStore);
    }

    em.flush();
    return requestNumbers;
  }

  private String getNextNumber(String targetTable, String columnName, String numberPrefix) {
    String queryStr = "SELECT s from SequenceNumberStore s "
            + "where s.targetTable=:targetTable AND "
            + " s.columnName=:columnName";
    TypedQuery<SequenceNumberStore> query = em.createQuery(queryStr,
            SequenceNumberStore.class);
    query.setParameter("targetTable", targetTable);
    query.setParameter("columnName", columnName);

    SequenceNumberStore seqNumStore = null;
    Long lastNumber = (long) 0;
    String prefix;
    boolean valuePresentInTable = true;
    try {
      seqNumStore = query.getSingleResult();
      lastNumber = seqNumStore.getLastNumber();
      prefix = seqNumStore.getPrefix();
    } catch (NoResultException ex) {
      //ex.printStackTrace();
      valuePresentInTable = false;
      prefix = numberPrefix;
      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable(targetTable);
      seqNumStore.setColumnName(columnName);
      seqNumStore.setPrefix(prefix);
    }

    // may need a prefix for center where the number is generated
    lastNumber = lastNumber + 1;
    seqNumStore.setLastNumber(lastNumber);
    if (valuePresentInTable) {
      em.merge(seqNumStore);
    } else {
      em.persist(seqNumStore);
    }

    em.flush();
    return String.format("%06d", lastNumber);
  }

  synchronized public String getNextWorksheetBatchNumber() {
    return getNextNumber("worksheet", "worksheetBatchNumber", "W");
  }

  synchronized public String getNextBatchNumber() {
    return getNextNumber("donationBatch", "batchNumber", "B");
  }

  synchronized public String getNextTestBatchNumber() {
    return getNextNumber("testbatch", "batchNumber", "TB");
  }
}
