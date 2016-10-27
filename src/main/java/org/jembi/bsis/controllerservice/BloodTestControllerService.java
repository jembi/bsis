package org.jembi.bsis.controllerservice;

import java.util.List;

import javax.transaction.Transactional;

import org.jembi.bsis.factory.BloodTestFactory;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BloodTestControllerService {

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  @Autowired
  private BloodTestFactory bloodTestFactory;

  public List<BloodTestFullViewModel> getAllBloodTests() {
    List<BloodTest> bloodTests = bloodTestingRepository.getAllBloodTestsIncludeInactive();
    return bloodTestFactory.createViewModels(bloodTests);
  }

}
