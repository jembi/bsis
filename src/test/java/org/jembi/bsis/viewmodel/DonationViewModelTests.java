package org.jembi.bsis.viewmodel;

import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DonationViewModelBuilder.aDonationViewModel;

public class DonationViewModelTests extends UnitTestSuite {

  @Test
  public void testComparableWithNullDins_shouldReturnZero() {
    DonationViewModel donationViewModel = aDonationViewModel().withDonationIdentificationNumber(null).build();
    DonationViewModel anotherDonationViewModel = aDonationViewModel().withDonationIdentificationNumber(null).build();

    assertThat(donationViewModel.compareTo(anotherDonationViewModel), is(equalTo(0)));
  }

  @Test
  public void testComparableWithLeftDinNull_shouldReturnPositiveOne() {
    DonationViewModel donationViewModel = aDonationViewModel().withDonationIdentificationNumber(null).build();
    DonationViewModel anotherDonationViewModel = aDonationViewModel().withDonationIdentificationNumber("").build();

    assertThat(donationViewModel.compareTo(anotherDonationViewModel), is(equalTo(-1)));
  }

  @Test
  public void testComparableWithRightDinNull_shouldReturnNegativeOne() {
    DonationViewModel donationViewModel = aDonationViewModel().withDonationIdentificationNumber("").build();
    DonationViewModel anotherDonationViewModel = aDonationViewModel().withDonationIdentificationNumber(null).build();

    assertThat(donationViewModel.compareTo(anotherDonationViewModel), is(equalTo(1)));
  }

  @Test
  public void testComparableWithLeftDinOrderedFirst_shouldReturnOne() {
    DonationViewModel donationViewModel = aDonationViewModel().withDonationIdentificationNumber("a").build();
    DonationViewModel anotherDonationViewModel = aDonationViewModel().withDonationIdentificationNumber("b").build();

    assertThat(donationViewModel.compareTo(anotherDonationViewModel), is(equalTo(-1)));
  }

  @Test
  public void testComparableWithRightDinOrderedFirst_shouldReturnOne() {
    DonationViewModel donationViewModel = aDonationViewModel().withDonationIdentificationNumber("b").build();
    DonationViewModel anotherDonationViewModel = aDonationViewModel().withDonationIdentificationNumber("a").build();

    assertThat(donationViewModel.compareTo(anotherDonationViewModel), is(equalTo(1)));
  }

  @Test
  public void testComparableWithEqualDins_shouldReturnZero() {
    DonationViewModel donationViewModel = aDonationViewModel().withDonationIdentificationNumber("a").build();
    DonationViewModel anotherDonationViewModel = aDonationViewModel().withDonationIdentificationNumber("a").build();

    assertThat(donationViewModel.compareTo(anotherDonationViewModel), is(equalTo(0)));
  }
}
