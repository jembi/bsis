package org.jembi.bsis.service;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.repository.BloodTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BloodTestCRUDService {

  @Autowired
  private BloodTestRepository bloodTestRepository;;

  public BloodTest createBloodTest(BloodTest bloodTest) {
    bloodTestRepository.save(bloodTest);
    return bloodTest;
  }

  public BloodTest updateBloodTest(BloodTest bloodTest) {
    BloodTest existingBloodTest = bloodTestRepository.findBloodTestById(bloodTest.getId());
    existingBloodTest.setTestName(bloodTest.getTestName());
    existingBloodTest.setTestNameShort(bloodTest.getTestNameShort());
    existingBloodTest.setCategory(bloodTest.getCategory());
    existingBloodTest.setBloodTestType(bloodTest.getBloodTestType());
    existingBloodTest.setIsActive(bloodTest.getIsActive());
    existingBloodTest.setIsDeleted(bloodTest.getIsDeleted());
    existingBloodTest.setValidResults(bloodTest.getValidResults());
    existingBloodTest.setPositiveResults(bloodTest.getPositiveResults());
    existingBloodTest.setNegativeResults(bloodTest.getNegativeResults());
    existingBloodTest.setRankInCategory(bloodTest.getRankInCategory());
    existingBloodTest.setFlagComponentsForDiscard(bloodTest.isFlagComponentsForDiscard());
    existingBloodTest.setFlagComponentsContainingPlasmaForDiscard(bloodTest.getFlagComponentsContainingPlasmaForDiscard());
    return bloodTestRepository.update(existingBloodTest);
  }
}
