package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestFullViewModelBuilder.aBloodTestFullViewModel;
import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestResultFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestResultViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class BloodTestResultFactoryTests extends UnitTestSuite {

  @InjectMocks
  private BloodTestResultFactory bloodTestResultFactory;

  @Mock
  private BloodTestFactory bloodTestFactory;

  @Test
  public void testCreateViewModel_shouldReturnViewModelWithTheCorrectState() {
    Date testedOn = new Date();
    UUID id = UUID.randomUUID();
    BloodTest bloodTest = aBloodTest().withTestNameShort("testName").withCategory(BloodTestCategory.TTI).build();
    BloodTestResult bloodTestResult = aBloodTestResult()
        .withId(id)
        .withBloodTest(bloodTest)
        .withResult("result")
        .withTestedOn(testedOn)
        .build();
    
    BloodTestResultViewModel expected = BloodTestResultViewModel.builder()
        .id(id)
        .testName("testName")
        .testCategory(BloodTestCategory.TTI)
        .result("result")
        .testedOn(testedOn)
        .build();

    BloodTestResultViewModel actual =
        bloodTestResultFactory.createViewModel(bloodTestResult);

    assertThat(actual, is(equalTo(expected)));
  }
  
  @Test
  public void testCreateFullViewModel_shouldReturnViewModelWithTheCorrectState() {
    Date testedOn = new Date();
    UUID id = UUID.randomUUID();
    BloodTest bloodTest = aBloodTest().withTestName("testName").build();
    BloodTestFullViewModel bloodTestFullViewModel =
        aBloodTestFullViewModel().withId(id).withTestName("testName").build();
    BloodTestResult bloodTestResult = aBloodTestResult()
        .withId(id)
        .withBloodTest(bloodTest)
        .withResult("result")
        .withTestedOn(testedOn)
        .withReEntryRequired(true)
        .build();
    
    BloodTestResultFullViewModel expected = BloodTestResultFullViewModel.builder()
        .id(id)
        .result("result")
        .testedOn(testedOn)
        .bloodTest(bloodTestFullViewModel)
        .reEntryRequired(true)
        .build();

    when(bloodTestFactory.createFullViewModel(bloodTest)).thenReturn(bloodTestFullViewModel);

    BloodTestResultFullViewModel actual =
        bloodTestResultFactory.createFullViewModel(bloodTestResult);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void testCreateFullViewModels_shouldReturnCorrectViewModels() {
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult().withId(id1).withBloodTest(aBloodTest().build())
            .build(),
        aBloodTestResult().withId(id2).withBloodTest(aBloodTest().build())
            .build());

    List<BloodTestResultFullViewModel> expected = Arrays.asList(
        BloodTestResultFullViewModel.builder().id(id1).build(), BloodTestResultFullViewModel.builder().id(id2).build());

    List<BloodTestResultFullViewModel> actual =
        bloodTestResultFactory.createFullViewModels(bloodTestResults);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void testCreateViewModels_shouldReturnCorrectViewModels() {
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    List<BloodTestResult> bloodTestResults =
        Arrays.asList(
            aBloodTestResult().withId(id1)
                .withBloodTest(aBloodTest().withTestNameShort("testName1").build()).build(),
            aBloodTestResult().withId(id2)
                .withBloodTest(aBloodTest().withTestNameShort("testName2").build()).build());

    List<BloodTestResultViewModel> expected =
        Arrays.asList(BloodTestResultViewModel.builder().id(id1).testName("testName1").build(),
            BloodTestResultViewModel.builder().id(id2).testName("testName2").build());

    List<BloodTestResultViewModel> actual = bloodTestResultFactory.createViewModels(bloodTestResults);

    assertThat(actual, is(equalTo(expected)));
  }

}
