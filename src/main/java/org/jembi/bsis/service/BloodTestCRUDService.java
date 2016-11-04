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
}
