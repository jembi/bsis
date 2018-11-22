package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jembi.bsis.dto.BloodTestResultDTO;
import org.jembi.bsis.dto.BloodTestResultExportDTO;
import org.jembi.bsis.dto.BloodTestTotalDTO;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.springframework.stereotype.Repository;

@Repository
public class BloodTestResultRepository extends AbstractRepository<BloodTestResult> {

  @PersistenceContext
  private EntityManager entityManager;

  public int countBloodTestResultsForDonation(UUID donationId) {
    return entityManager.createNamedQuery(
        BloodTestResultNamedQueryConstants.NAME_COUNT_BLOOD_TEST_RESULTS_FOR_DONATION, 
        Number.class)
        .setParameter("donationId", donationId)
        .setParameter("testOutcomeDeleted", false)
        .getSingleResult()
        .intValue();
  }

  public List<BloodTestResult> getTestOutcomes(Donation donation) {
    return entityManager.createNamedQuery(
        BloodTestResultNamedQueryConstants.NAME_GET_TEST_OUTCOMES_FOR_DONATION, 
        BloodTestResult.class)
        .setParameter("donation", donation)
        .setParameter("testOutcomeDeleted", false)
        .getResultList();
  }

  public List<BloodTestResultExportDTO> findBloodTestResultsForExport() {
    return entityManager.createNamedQuery(
        BloodTestResultNamedQueryConstants.NAME_FIND_BLOOD_TEST_RESULTS_FOR_EXPORT,
        BloodTestResultExportDTO.class)
        .setParameter("deleted", false)
        .getResultList();
  }

  public List<BloodTestResultDTO> findTTIPrevalenceReportIndicators(Date startDate, Date endDate) {
    return entityManager.createNamedQuery(
        BloodTestResultNamedQueryConstants.NAME_FIND_BLOOD_TEST_RESULT_VALUE_OBJECTS_FOR_DATE_RANGE,
        BloodTestResultDTO.class)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("donationDeleted", false)
        .setParameter("testOutcomeDeleted", false)
        .setParameter("bloodTestDeleted", false)
        .setParameter("released", true)
        .setParameter("bloodTestType", BloodTestType.BASIC_TTI)
        .setParameter("countAsDonation", true)
        .getResultList();
  }

  public List<BloodTestTotalDTO> findTTIPrevalenceReportTotalUnitsTested(Date startDate, Date endDate) {
    return entityManager.createNamedQuery(
        BloodTestResultNamedQueryConstants.NAME_FIND_TOTAL_UNITS_TESTED_FOR_DATE_RANGE,
        BloodTestTotalDTO.class)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("donationDeleted", false)
        .setParameter("testOutcomeDeleted", false)
        .setParameter("released", true)
        .setParameter("bloodTestType", BloodTestType.BASIC_TTI)
        .setParameter("countAsDonation", true)
        .getResultList();
  }

  public List<BloodTestTotalDTO> findTTIPrevalenceReportTotalUnsafeUnitsTested(Date startDate, Date endDate) {
    return entityManager.createNamedQuery(
        BloodTestResultNamedQueryConstants.NAME_FIND_TOTAL_TTI_UNSAFE_UNITS_TESTED_FOR_DATE_RANGE,
        BloodTestTotalDTO.class)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("donationDeleted", false)
        .setParameter("testOutcomeDeleted", false)
        .setParameter("released", true)
        .setParameter("bloodTestType", BloodTestType.BASIC_TTI)
        .setParameter("ttiStatus", TTIStatus.UNSAFE)
        .setParameter("countAsDonation", true)
        .getResultList();
  }
}
