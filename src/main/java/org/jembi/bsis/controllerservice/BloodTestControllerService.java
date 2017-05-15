package org.jembi.bsis.controllerservice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.factory.BloodTestFactory;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.service.BloodTestCRUDService;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BloodTestControllerService {

  @Autowired
  private BloodTestRepository bloodTestRepository;

  @Autowired
  private BloodTestFactory bloodTestFactory;

  @Autowired
  private BloodTestCRUDService bloodTestCRUDService;

  public List<BloodTestViewModel> getAllBloodTests() {
    List<BloodTest> bloodTests = bloodTestRepository.getBloodTests(true, true);
    return bloodTestFactory.createViewModels(bloodTests);
  }

  public List<BloodTestCategory> getCategories() {
    return Arrays.asList(BloodTestCategory.values());
  }

  public Map<BloodTestCategory, List<BloodTestType>> getTypes() {
    Map<BloodTestCategory, List<BloodTestType>> types = new HashMap<>();
    for (BloodTestCategory category : BloodTestCategory.values()) {
      types.put(category, BloodTestType.getBloodTestTypeForCategory(category));
    }
    return types;
  }

  public BloodTestFullViewModel createBloodTest(BloodTestBackingForm bloodTestBackingForm) {
    BloodTest bloodTest = bloodTestFactory.createEntity(bloodTestBackingForm);
    return bloodTestFactory.createFullViewModel(bloodTestCRUDService.createBloodTest(bloodTest));
  }

  public BloodTestFullViewModel updateBloodTest(BloodTestBackingForm bloodTestBackingForm) {
    BloodTest bloodTest = bloodTestFactory.createEntity(bloodTestBackingForm);
    return bloodTestFactory.createFullViewModel(bloodTestCRUDService.updateBloodTest(bloodTest));
  }

  public BloodTestFullViewModel getBloodTestById(UUID id) {
    BloodTest bloodTest = bloodTestRepository.findBloodTestById(id);
    return bloodTestFactory.createFullViewModel(bloodTest);
  }
}
