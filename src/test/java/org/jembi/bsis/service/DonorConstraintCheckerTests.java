package org.jembi.bsis.service;

import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.service.DonorDeferralStatusCalculator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DonorConstraintCheckerTests {

  @InjectMocks
  private DonorConstraintChecker donorConstraintChecker;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private DonorDeferralRepository donorReferralRepository;
  @Mock
  private DonorDeferralStatusCalculator donorDeferralStatusCalculator;

  @Test
  public void testCanDeleteDonorWithDonorWithNotes_shouldReturnFalse() {

    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donorWithNotes = aDonor().withNotes("irrelevant.notes").build();

    when(donorRepository.findDonorById(irrelevantDonorId)).thenReturn(donorWithNotes);

    boolean canDelete = donorConstraintChecker.canDeleteDonor(irrelevantDonorId);

    assertThat(canDelete, is(false));
  }

  @Test
  public void testCanDeleteDonorWithDonorWithDonations_shouldReturnFalse() {

    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donorWithDonations = aDonor().build();

    when(donorRepository.findDonorById(irrelevantDonorId)).thenReturn(donorWithDonations);
    when(donationRepository.countDonationsForDonor(donorWithDonations)).thenReturn(1);

    boolean canDelete = donorConstraintChecker.canDeleteDonor(irrelevantDonorId);

    assertThat(canDelete, is(false));
  }

  @Test
  public void testCanDeleteDonorWithDonorWithDeferrals_shouldReturnFalse() {

    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donorWithDeferrals = aDonor().build();

    when(donorRepository.findDonorById(irrelevantDonorId)).thenReturn(donorWithDeferrals);
    when(donationRepository.countDonationsForDonor(donorWithDeferrals)).thenReturn(0);
    when(donorReferralRepository.countDonorDeferralsForDonor(donorWithDeferrals)).thenReturn(1);

    boolean canDelete = donorConstraintChecker.canDeleteDonor(irrelevantDonorId);

    assertThat(canDelete, is(false));
  }

  @Test
  public void testCanDeleteDonorWithNoConstraints_shouldReturnTrue() {

    UUID irrelevantDonorId = UUID.randomUUID();
    Donor existingDonor = aDonor().withNotes("").build();

    when(donorRepository.findDonorById(irrelevantDonorId)).thenReturn(existingDonor);
    when(donationRepository.countDonationsForDonor(existingDonor)).thenReturn(0);
    when(donorReferralRepository.countDonorDeferralsForDonor(existingDonor)).thenReturn(0);

    boolean canDelete = donorConstraintChecker.canDeleteDonor(irrelevantDonorId);

    assertThat(canDelete, is(true));
  }

  @Test
  public void testIsDonorEligibleToDonateWithNonDeferredDonorWithoutDonations_shouldReturnTrue() {

    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();

    when(donorRepository.findDonorById(irrelevantDonorId)).thenReturn(donor);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor.getId())).thenReturn(false);

    boolean isEligible = donorConstraintChecker.isDonorEligibleToDonate(irrelevantDonorId);

    assertThat(isEligible, is(true));
  }

  @Test
  public void testIsDonorEligibleToDonateWithDeferredDonorWithoutDonations_shouldReturnFalse() {

    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor().withId(irrelevantDonorId).build();

    when(donorRepository.findDonorById(irrelevantDonorId)).thenReturn(donor);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(irrelevantDonorId)).thenReturn(true);

    boolean isEligible = donorConstraintChecker.isDonorEligibleToDonate(irrelevantDonorId);

    assertThat(isEligible, is(false));
  }

  @Test
  public void testIsDonorEligibleToDonateWithNonDeferredDonorWithDonationNotCountedAsDonation_shouldReturnTrue() {

    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor()
        .withId(irrelevantDonorId)
        .withDonation(aDonation()
            .withPackType(aPackType().withCountAsDonation(false).build())
            .build())
        .build();

    when(donorRepository.findDonorById(irrelevantDonorId)).thenReturn(donor);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor.getId())).thenReturn(false);

    boolean isEligible = donorConstraintChecker.isDonorEligibleToDonate(irrelevantDonorId);

    assertThat(isEligible, is(true));
  }

  @Test
  public void testIsDonorEligibleToDonateWithNonDeferredDonorWithDonationNotOverlappingPeriod_shouldReturnTrue() {

    UUID irrelevantDonorId = UUID.randomUUID();
    Date previousDonationDate = new DateTime().minusDays(3).toDate();

    Donor donor = aDonor()
        .withId(irrelevantDonorId)
        .withDonation(aDonation()
            .withDonationDate(previousDonationDate)
            .withPackType(aPackType()
                .withCountAsDonation(true)
                .withPeriodBetweenDonations(3)
                .build())
            .build())
        .build();

    when(donorRepository.findDonorById(irrelevantDonorId)).thenReturn(donor);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor.getId())).thenReturn(false);

    boolean isEligible = donorConstraintChecker.isDonorEligibleToDonate(irrelevantDonorId);

    assertThat(isEligible, is(true));
  }

  @Test
  public void testIsDonorEligibleToDonateWithNonDeferredDonorWithDonationOverlappingPeriod_shouldReturnFalse() {

    UUID irrelevantDonorId = UUID.randomUUID();
    Date previousDonationDate = new DateTime().minusDays(3).toDate();

    Donor donor = aDonor()
        .withId(irrelevantDonorId)
        .withDonation(aDonation()
            .withDonationDate(previousDonationDate)
            .withPackType(aPackType()
                .withCountAsDonation(true)
                .withPeriodBetweenDonations(5)
                .build())
            .build())
        .build();

    when(donorRepository.findDonorById(irrelevantDonorId)).thenReturn(donor);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor.getId())).thenReturn(false);

    boolean isEligible = donorConstraintChecker.isDonorEligibleToDonate(irrelevantDonorId);

    assertThat(isEligible, is(false));
  }

  @Test
  public void testIsDonorDeferred_shouldReturnTrue() {
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor()
        .withId(irrelevantDonorId)
        .build();

    when(donorRepository.findDonorById(irrelevantDonorId)).thenReturn(donor);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor.getId())).thenReturn(true);

    boolean isDonorDeferred = donorConstraintChecker.isDonorDeferred(irrelevantDonorId);
    assertThat(isDonorDeferred, is(true));
  }

  @Test
  public void testIsDonorDeferred_shouldReturnFalse() {
    UUID irrelevantDonorId = UUID.randomUUID();
    Donor donor = aDonor()
        .withId(irrelevantDonorId)
        .build();

    when(donorRepository.findDonorById(irrelevantDonorId)).thenReturn(donor);
    when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor.getId())).thenReturn(false);

    boolean isDonorDeferred = donorConstraintChecker.isDonorDeferred(irrelevantDonorId);
    assertThat(isDonorDeferred, is(false));
  }

  @Test
  public void testIsDonorEligibleToDonateTodayWithNonDeferredDonorWithRecentDonation_shouldReturnFalse() {

    UUID irrelevantDonorId = UUID.randomUUID();
    Date mobileClinicDate = new Date();

    int periodBetweenDonations = 5;
    Date previousDonationDate = new DateTime().minusDays(3).toDate();
    Date nextDonationDate = new DateTime(previousDonationDate).plusDays(periodBetweenDonations).toDate();

    Donor donor = aDonor()
        .withId(irrelevantDonorId)
        .withDonation(aDonation()
            .withDonationDate(previousDonationDate)
            .withPackType(aPackType()
                .withCountAsDonation(true)
                .withPeriodBetweenDonations(periodBetweenDonations)
                .build())
            .build())
        .build();

    when(donationRepository.findLatestDueToDonateDateForDonor(irrelevantDonorId)).thenReturn(nextDonationDate);
    when(donorDeferralStatusCalculator.isDonorDeferredOnDate(donor.getId(), mobileClinicDate)).thenReturn(false);

    boolean isEligible = donorConstraintChecker.isDonorEligibleToDonateOnDate(irrelevantDonorId, mobileClinicDate);

    assertThat(isEligible, is(false));
  }

  @Test
  public void testIsDonorEligibleToDonateNextWeekWithNonDeferredDonorWithRecentDonation_shouldReturnTrue() {

    Date futureMobileClinicDate = new DateTime().plusDays(7).toDate();
    UUID irrelevantDonorId = UUID.randomUUID();

    int periodBetweenDonations = 5;
    Date previousDonationDate = new DateTime().minusDays(3).toDate();
    Date nextDonationDate = new DateTime(previousDonationDate).plusDays(periodBetweenDonations).toDate();

    Donor donor = aDonor()
        .withId(irrelevantDonorId)
        .withDonation(aDonation()
            .withDonationDate(previousDonationDate)
            .withPackType(aPackType()
                .withCountAsDonation(true)
                .withPeriodBetweenDonations(periodBetweenDonations)
                .build())
            .build())
        .build();

    when(donationRepository.findLatestDueToDonateDateForDonor(irrelevantDonorId)).thenReturn(nextDonationDate);
    when(donorDeferralStatusCalculator.isDonorDeferredOnDate(donor.getId(), futureMobileClinicDate)).thenReturn(false);

    boolean isEligible = donorConstraintChecker.isDonorEligibleToDonateOnDate(irrelevantDonorId, futureMobileClinicDate);

    assertThat(isEligible, is(true));
  }

  @Test
  public void testIsDonorEligibleToDonateNextWeekWithDeferredDonorWithRecentDonation_shouldReturnFalse() {

    Date futureMobileClinicDate = new DateTime().plusDays(7).toDate();
    UUID irrelevantDonorId = UUID.randomUUID();

    int periodBetweenDonations = 5;
    Date previousDonationDate = new DateTime().minusDays(3).toDate();
    Date nextDonationDate = new DateTime(previousDonationDate).plusDays(periodBetweenDonations).toDate();

    Donor donor = aDonor()
        .withId(irrelevantDonorId)
        .withDonation(aDonation()
            .withDonationDate(previousDonationDate)
            .withPackType(aPackType()
                .withCountAsDonation(true)
                .withPeriodBetweenDonations(periodBetweenDonations)
                .build())
            .build())
        .build();

    when(donationRepository.findLatestDueToDonateDateForDonor(irrelevantDonorId)).thenReturn(nextDonationDate);
    when(donorDeferralStatusCalculator.isDonorDeferredOnDate(donor.getId(), futureMobileClinicDate)).thenReturn(true);

    boolean isEligible = donorConstraintChecker.isDonorEligibleToDonateOnDate(irrelevantDonorId, futureMobileClinicDate);

    assertThat(isEligible, is(false));
  }

  @Test
  public void testIsDonorEligibleToDonateNextWeekWithNonDeferredDonorWithNoDonation_shouldReturnTrue() {

    Date futureMobileClinicDate = new DateTime().plusDays(7).toDate();
    UUID irrelevantDonorId = UUID.randomUUID();

    Donor donor = aDonor()
        .withId(irrelevantDonorId)
        .build();

    when(donationRepository.findLatestDueToDonateDateForDonor(irrelevantDonorId)).thenReturn(null);
    when(donorDeferralStatusCalculator.isDonorDeferredOnDate(donor.getId(), futureMobileClinicDate)).thenReturn(false);

    boolean isEligible = donorConstraintChecker.isDonorEligibleToDonateOnDate(irrelevantDonorId, futureMobileClinicDate);

    assertThat(isEligible, is(true));
  }

  @Test
  public void testIsDonorEligibleToDonateNextWeekWithNonDeferredDonorWithDonationNotCountedAsDonation_shouldReturnTrue() {

    Date futureMobileClinicDate = new DateTime().plusDays(7).toDate();
    UUID irrelevantDonorId = UUID.randomUUID();
    
    int periodBetweenDonations = 0;
    Date previousDonationDate = new DateTime().minusDays(3).toDate();
    Date nextDonationDate = new DateTime(previousDonationDate).plusDays(periodBetweenDonations).toDate();

    Donor donor = aDonor()
        .withId(irrelevantDonorId)
        .withDonation(aDonation()
            .withDonationDate(previousDonationDate)
            .withPackType(aPackType().withCountAsDonation(false).withPeriodBetweenDonations(periodBetweenDonations).build())
            .build())
        .build();

    when(donationRepository.findLatestDueToDonateDateForDonor(irrelevantDonorId)).thenReturn(nextDonationDate);
    when(donorDeferralStatusCalculator.isDonorDeferredOnDate(donor.getId(), futureMobileClinicDate)).thenReturn(false);

    boolean isEligible = donorConstraintChecker.isDonorEligibleToDonateOnDate(irrelevantDonorId, futureMobileClinicDate);

    assertThat(isEligible, is(true));
  }
}
