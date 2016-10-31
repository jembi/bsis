package org.jembi.bsis.repository;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import org.jembi.bsis.dto.BloodTestResultDTO;
import org.jembi.bsis.dto.BloodTestResultExportDTO;
import org.jembi.bsis.dto.BloodTestTotalDTO;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.service.BloodTestingRuleEngine;
import org.jembi.bsis.viewmodel.BloodTestResultViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BloodTestResultRepository extends AbstractRepository<BloodTestResult> {

    private static final Logger LOGGER = Logger.getLogger(BloodTestResultRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonationBatchRepository donationBatchRepository;

    @Autowired
    private BloodTestingRuleEngine ruleEngine;

    // TODO: Test
    public int countBloodTestResultsForDonation(long donationId) {
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
                BloodTestResultNamedQueryConstants.NAME_GET_TEST_OUTCOMES_FOR_DONATION, BloodTestResult.class)
                .setParameter("donation", donation)
                .setParameter("testOutcomeDeleted", false)
                .getResultList();
    }

    public List<BloodTestResultExportDTO> findBloodTestResultsForExport() {
        return entityManager.createNamedQuery(BloodTestResultNamedQueryConstants.NAME_FIND_BLOOD_TEST_RESULTS_FOR_EXPORT,
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
                .setParameter("released", true)
                .setParameter("bloodTestType", BloodTestType.BASIC_TTI)
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
                .setParameter("ttiStatus", TTIStatus.TTI_UNSAFE)
                .getResultList();
    }

    public List<BloodTestingRuleResult> getAllTestsStatusForDonationBatches(
            List<Long> donationBatchIds) {

        List<BloodTestingRuleResult> bloodTestingRuleResults = new ArrayList<BloodTestingRuleResult>();

        for (Long donationBatchId : donationBatchIds) {
            List<Donation> donations = donationBatchRepository.findDonationsInBatch(donationBatchId);

            for (Donation donation : donations) {

                if (!donation.getPackType().getTestSampleProduced()) {
                    // This donation did not produce a test sample so skip it
                    continue;
                }

                BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(
                        donation, new HashMap<Long, String>());
                bloodTestingRuleResults.add(ruleResult);
            }
        }

        return bloodTestingRuleResults;
    }

    public List<BloodTestingRuleResult> getAllTestsStatusForDonationBatchesByBloodTestType(List<Long> donationBatchIds,
                                                                                           BloodTestType bloodTestType) {

        List<BloodTestingRuleResult> bloodTestingRuleResults = getAllTestsStatusForDonationBatches(donationBatchIds);
        List<BloodTestingRuleResult> filteredRuleResults = new ArrayList<BloodTestingRuleResult>();
        for (BloodTestingRuleResult result : bloodTestingRuleResults) {
            Map<String, BloodTestResultViewModel> modelMap = result.getRecentTestResults();
            Map<String, BloodTestResultViewModel> filteredModelMap = new HashMap<String, BloodTestResultViewModel>();
            for (String key : modelMap.keySet()) {
                BloodTestResultViewModel model = modelMap.get(key);
                if (model.getBloodTest().getBloodTestType().equals(bloodTestType)) {
                    filteredModelMap.put(key, model);
                }
            }
            result.setRecentTestResults(filteredModelMap);
            filteredRuleResults.add(result);
        }
        bloodTestingRuleResults = filteredRuleResults;

        return bloodTestingRuleResults;
    }

    public BloodTestingRuleResult getAllTestsStatusForDonation(
            Long donationId) {
        Donation donation = donationRepository
                .findDonationById(donationId);
        return ruleEngine.applyBloodTests(donation,
                new HashMap<Long, String>());
    }
}
