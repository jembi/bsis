package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.jembi.bsis.service.DonorDeferralStatusCalculator;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DonorDeferralStatusCalculatorTests {

  @InjectMocks
  private DonorDeferralStatusCalculator donorDeferralStatusCalculator;
  @Mock
  private GeneralConfigAccessorService generalConfigAccessorService;
  @Mock
  private DonorDeferralRepository donorDeferralRepository;

  @Test
  public void testShouldDonorBeDeferredWithNonConfirmatoryResult_shouldReturnFalse() {
    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withResult("POS")
            .withBloodTest(aBloodTest()
                .withBloodTestType(BloodTestType.BASIC_TTI)
                .withPositiveResults("POS,+")
                .build())
            .build()
    );

    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.DEFER_DONORS_WITH_NEG_REPEAT_OUTCOMES))
        .thenReturn(false);

    boolean returnedValue = donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults);

    assertThat(returnedValue, is(false));
  }

  @Test
  public void testShouldDonorBeDeferredWithNegativeConfirmatoryResult_shouldReturnFalse() {
    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withResult("NEG")
            .withBloodTest(aBloodTest()
                .withBloodTestType(BloodTestType.CONFIRMATORY_TTI)
                .withPositiveResults("POS,+")
                .build())
            .build()
    );

    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.DEFER_DONORS_WITH_NEG_REPEAT_OUTCOMES))
        .thenReturn(false);

    boolean returnedValue = donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults);

    assertThat(returnedValue, is(false));
  }

  @Test
  public void testShouldDonorBeDeferredWithPositiveConfirmatoryResult_shouldReturnTrue() {
    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withResult("POS")
            .withBloodTest(aBloodTest()
                .withBloodTestType(BloodTestType.CONFIRMATORY_TTI)
                .withPositiveResults("POS,+")
                .build())
            .build()
    );

    boolean returnedValue = donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults);

    assertThat(returnedValue, is(true));
  }

  @Test
  public void testIsDonorCurrentlyDeferredWithCurrentDeferrals_shouldReturnTrue() {

    Donor donor = aDonor().build();

    when(donorDeferralRepository.countCurrentDonorDeferralsForDonor(donor)).thenReturn(1);

    boolean returnedValue = donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor);

    assertThat(returnedValue, is(true));
  }

  @Test
  public void testIsDonorCurrentlyDeferredWithNoCurrentDeferrals_shouldReturnFalse() {

    Donor donor = aDonor().build();

    when(donorDeferralRepository.countCurrentDonorDeferralsForDonor(donor)).thenReturn(0);

    boolean returnedValue = donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor);

    assertThat(returnedValue, is(false));
  }

  @Test
  public void testIsDonorDeferredWithNoCurrentDeferralsOnDate_shouldReturnFalse() {

    Date futureMobileClinicDate = new DateTime().plusDays(7).toDate();
    Donor donor = aDonor().build();

    when(donorDeferralRepository.countDonorDeferralsForDonorOnDate(donor, futureMobileClinicDate)).thenReturn(0);

    boolean returnedValue = donorDeferralStatusCalculator.isDonorDeferredOnDate(donor, futureMobileClinicDate);

    assertThat(returnedValue, is(false));
  }
}