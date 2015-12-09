package repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.bloodtesting.BloodTest;
import model.donation.Donation;
import model.worksheet.Worksheet;
import model.worksheet.WorksheetType;

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

  public void addDonationsToWorksheet(Long worksheetId, Set<String> donationIdentificationNumbers) {

    if (donationIdentificationNumbers.isEmpty())
      return;
    String queryStr = "SELECT w from Worksheet w LEFT JOIN FETCH w.donations " +
        "WHERE w.id=:worksheetId AND w.isDeleted=:isDeleted";
    TypedQuery<Worksheet> query = em.createQuery(queryStr, Worksheet.class);
    query.setParameter("worksheetId", worksheetId);
    query.setParameter("isDeleted", false);

    String donationQueryStr = "SELECT c from Donation c " +
        "LEFT JOIN FETCH c.worksheets WHERE " +
        "c.donationIdentificationNumber IN :donationIdentificationNumbers";
    TypedQuery<Donation> donationQuery = em.createQuery(donationQueryStr, Donation.class);
    donationQuery.setParameter("donationIdentificationNumbers", donationIdentificationNumbers);
    List<Donation> newDonations = donationQuery.getResultList();

    Worksheet worksheet = query.getSingleResult();
    Set<String> existingDonationIdentificationNumbers = new HashSet<>();
    for (Donation c : worksheet.getDonations()) {
      existingDonationIdentificationNumbers.add(c.getDonationIdentificationNumber());
    }

    List<Donation> donations = new ArrayList<>();
    for (Donation c : newDonations) {
      if (existingDonationIdentificationNumbers.contains(c.getDonationIdentificationNumber()))
        continue;
      donations.add(c);
//      c.getWorksheets().add(worksheet);
//      em.merge(c);
    }

    worksheet.getDonations().addAll(donations);
    em.merge(worksheet);
    em.flush();
  }

  @SuppressWarnings("unused")
  public List<Worksheet> findWorksheets(String worksheetNumber, List<String> worksheetTypes) {

    if (worksheetTypes == null || worksheetTypes.size() == 0)
      return Arrays.asList(new Worksheet[0]);

    List<Integer> worksheetTypeIds = new ArrayList<>();
    for (String worksheetTypeIdStr : worksheetTypes) {
      worksheetTypeIds.add(Integer.parseInt(worksheetTypeIdStr));
    }

    worksheetTypeIds.add(-1);
    TypedQuery<Worksheet> query = null;
    if (StringUtils.isBlank(worksheetNumber)) {
      String queryStr = "SELECT DISTINCT w FROM Worksheet w LEFT JOIN FETCH w.donations WHERE " +
          "w.worksheetType.id IN (:worksheetTypeIds) AND w.isDeleted=:isDeleted";
      query = em.createQuery(queryStr, Worksheet.class);
      query.setParameter("worksheetTypeIds", worksheetTypeIds);
      query.setParameter("isDeleted", false);
    } else {
      String queryStr = "SELECT DISTINCT w FROM Worksheet w LEFT JOIN FETCH w.donations WHERE " +
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

  public Worksheet findWorksheetFullInformation(Long worksheetId) {
    String queryStr = "SELECT w from Worksheet w LEFT JOIN FETCH w.donations WHERE " +
        "w.id=:worksheetId AND w.isDeleted=:isDeleted";
    TypedQuery<Worksheet> query = em.createQuery(queryStr, Worksheet.class);
    query.setParameter("worksheetId", worksheetId);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }

  public Worksheet findWorksheetFullInformation(String worksheetNumber) {
    String queryStr = "SELECT w from Worksheet w LEFT JOIN FETCH w.donations WHERE " +
        "w.worksheetNumber=:worksheetNumber AND w.isDeleted=:isDeleted";
    TypedQuery<Worksheet> query = em.createQuery(queryStr, Worksheet.class);
    query.setParameter("worksheetNumber", worksheetNumber);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }

  public Worksheet findWorksheetByWorksheetNumber(String worksheetNumber) {
    String queryStr = "SELECT w from Worksheet w WHERE " +
        "w.worksheetNumber=:worksheetNumber AND w.isDeleted=:isDeleted";
    TypedQuery<Worksheet> query = em.createQuery(queryStr, Worksheet.class);
    query.setParameter("worksheetNumber", worksheetNumber);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }

  public Worksheet findWorksheetByWorksheetNumberIncludeDeleted(String worksheetNumber) {
    String queryStr = "SELECT w from Worksheet w WHERE " +
        "w.worksheetNumber=:worksheetNumber";
    TypedQuery<Worksheet> query = em.createQuery(queryStr, Worksheet.class);
    query.setParameter("worksheetNumber", worksheetNumber);
    Worksheet worksheet = null;
    try {
      worksheet = query.getSingleResult();
    } catch (NoResultException ex) {
      ex.printStackTrace();
    }
    return worksheet;
  }

  public List<BloodTest> getBloodTestsInWorksheet(Worksheet worksheet) {
    TypedQuery<WorksheetType> query;
    query = em.createQuery("SELECT wt from WorksheetType wt LEFT JOIN FETCH wt.bloodTests " +
            "WHERE wt.id=:id AND wt.isDeleted=:isDeleted", WorksheetType.class);
    query.setParameter("isDeleted", false);
    query.setParameter("id", worksheet.getWorksheetType().getId());
    if (query.getResultList().size() == 0)
      return null;
    WorksheetType worksheetType = query.getSingleResult();
    List<BloodTest> bloodTests = new ArrayList<>();
    bloodTests.addAll(worksheetType.getBloodTests());
    Collections.sort(bloodTests);
    return bloodTests;
  }
}
