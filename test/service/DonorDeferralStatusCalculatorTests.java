package service;

import constant.GeneralConfigConstants;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.BloodTestType;
import model.donor.Donor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.DonorDeferralRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static helpers.builders.BloodTestBuilder.aBloodTest;
import static helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static helpers.builders.DonorBuilder.aDonor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

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
    List<BloodTestResult> bloodTestResults = Collections.singletonList(
            aBloodTestResult()
                    .withResult("POS")
                    .withBloodTest(aBloodTest()
                            .withBloodTestType(BloodTestType.BASIC_TTI)
                            .withPositiveResults("POS,+")
                            .build())
                    .build()
    );

    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.DEFER_DONORS_WITH_NEG_CONFIRMATORY_OUTCOMES))
            .thenReturn(false);

    boolean returnedValue = donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults);

    assertThat(returnedValue, is(false));
  }

  @Test
  public void testShouldDonorBeDeferredWithNegativeConfirmatoryResult_shouldReturnFalse() {
    List<BloodTestResult> bloodTestResults = Collections.singletonList(
            aBloodTestResult()
                    .withResult("NEG")
                    .withBloodTest(aBloodTest()
                            .withBloodTestType(BloodTestType.CONFIRMATORY_TTI)
                            .withPositiveResults("POS,+")
                            .build())
                    .build()
    );

    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.DEFER_DONORS_WITH_NEG_CONFIRMATORY_OUTCOMES))
            .thenReturn(false);

    boolean returnedValue = donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults);

    assertThat(returnedValue, is(false));
  }

  @Test
  public void testShouldDonorBeDeferredWithPositiveConfirmatoryResult_shouldReturnTrue() {
    List<BloodTestResult> bloodTestResults = Collections.singletonList(
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

}
