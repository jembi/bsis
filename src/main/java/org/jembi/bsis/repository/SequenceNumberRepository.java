package org.jembi.bsis.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.jembi.bsis.model.sequencenumber.SequenceNumberStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SequenceNumberRepository {
  
  private static final Logger LOGGER = Logger.getLogger(SequenceNumberRepository.class);

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
      LOGGER.debug("Creating SequenceNumberStore entry for Donation donationIdentificationNumber");
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
      LOGGER.debug("Creating SequenceNumberStore entry for Request requestNumber");
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
    boolean valuePresentInTable = true;
    try {
      seqNumStore = query.getSingleResult();
      lastNumber = seqNumStore.getLastNumber();
      //prefix = seqNumStore.getPrefix();
    } catch (NoResultException ex) {
      LOGGER.debug("Creating SequenceNumberStore entry for Donor donorNumber");
      valuePresentInTable = false;
      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable("Donor");
      seqNumStore.setColumnName("donorNumber");
    }


    if (lastNumber == 0) {
      lastNumber++;
    }
    String donorNumberFormat = generalConfigRepository.getGeneralConfigByName("donor.donorNumberFormat").getValue();
    String lastNumberStr = String.format(donorNumberFormat, lastNumber);
    String requestNumber = lastNumberStr;
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
      LOGGER.debug("Creating SequenceNumberStore entry for Donor donorNumber");

      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable("Donor");
      seqNumStore.setColumnName("donorNumber");


    }


    if (lastNumber == 0) {
      lastNumber++;
    }
    String lastNumberStr = String.format("%06d", lastNumber);

    String requestNumber = lastNumberStr;


    em.flush();
    return requestNumber;
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
      LOGGER.debug("Creating SequenceNumberStore entry for Donation donationIdentificationNumber");
      valuePresentInTable = false;
      prefix = "C";
      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable("Donation");
      seqNumStore.setColumnName("donationIdentificationNumber");
      seqNumStore.setPrefix(prefix);
    }

    List<String> donationIdentificationNumbers = new ArrayList<String>();
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
      LOGGER.debug("Creating SequenceNumberStore entry for " + targetTable + " " + columnName + " and prefix " + numberPrefix);
      valuePresentInTable = false;
      prefix = numberPrefix;
      seqNumStore = new SequenceNumberStore();
      seqNumStore.setTargetTable(targetTable);
      seqNumStore.setColumnName(columnName);
      seqNumStore.setPrefix(prefix);
    }

    String lastNumberStr = String.format("%06d", lastNumber);
    // may need a prefix for center where the number is generated
    String nextNumber = lastNumberStr;
    lastNumber = lastNumber + 1;
    seqNumStore.setLastNumber(lastNumber);
    if (valuePresentInTable) {
      em.merge(seqNumStore);
    } else {
      em.persist(seqNumStore);
    }

    em.flush();
    return nextNumber;
  }

  synchronized public String getNextBatchNumber() {
    return getNextNumber("donationBatch", "batchNumber", "B");
  }

  synchronized public String getNextTestBatchNumber() {
    return getNextNumber("testbatch", "batchNumber", "TB");
  }
}
