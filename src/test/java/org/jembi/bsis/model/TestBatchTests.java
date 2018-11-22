package org.jembi.bsis.model;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.jembi.bsis.model.testbatch.TestBatchStatus.CLOSED;
import static org.jembi.bsis.model.testbatch.TestBatchStatus.OPEN;

public class TestBatchTests extends UnitTestSuite {

  @Test(expected = IllegalStateException.class)
  public void testAddDonationWithNonOpenBatch_shouldThrow() {
    Donation donation = aDonation().build();

    TestBatch testBatch = aTestBatch().withStatus(CLOSED).build();

    testBatch.addDonation(donation);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddDonationWithDonationincludedToAnotherBatch_shouldThrow() {
    Donation donation = aDonation().withTestBatch(aTestBatch().withStatus(OPEN).build()).build();

    TestBatch testBatch = aTestBatch().withStatus(OPEN).build();

    testBatch.addDonation(donation);
  }

  @Test
  public void testAddDonationWithDonationWithMatchingTestBatch_shouldNoNothing() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch().withStatus(OPEN).withDonation(donation).build();
    donation.setTestBatch(testBatch);

    testBatch.addDonation(donation);

    assertThat(testBatch.getDonations(), hasItem(donation));
    assertThat(donation.getTestBatch(), is(equalTo(testBatch)));
  }

  @Test
  public void testAddDonationWithDonationWithNullTestBatch_shouldAdd() {
    Donation donation = aDonation().withTestBatch(null).build();

    TestBatch testBatch = aTestBatch().withStatus(OPEN).withDonations(new HashSet<>()).build();

    testBatch.addDonation(donation);

    assertThat(testBatch.getDonations(), hasItem(donation));
  }

  @Test(expected = IllegalStateException.class)
  public void testRemoveDonationWithNonOpenBatch_shouldThrow() {
    Donation donation = aDonation().build();

    TestBatch testBatch = aTestBatch().withStatus(CLOSED).build();

    testBatch.removeDonation(donation);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveDonationWithDonationIncludedInAnotherBatch_shouldThrow() {
    Donation donation = aDonation().withTestBatch(aTestBatch().withStatus(OPEN).build()).build();

    TestBatch testBatch = aTestBatch().withStatus(OPEN).build();

    testBatch.removeDonation(donation);
  }

  @Test
  public void testRemoveDonationWithDonationWithNullTestBatch_shouldDoNoting() {
    Donation donation = aDonation().withTestBatch(null).build();

    TestBatch testBatch = aTestBatch().withStatus(OPEN).withDonations(new HashSet<>()).build();

    testBatch.removeDonation(donation);

    assertThat(testBatch.getDonations(), not(hasItem(donation)));
    assertThat(donation.getTestBatch(), is(nullValue(TestBatch.class)));
  }

  @Test
  public void testRemoveDonationWithDonationWithMatchingTestBatch_shouldRemove() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch().withStatus(OPEN).withDonation(donation).build();
    donation.setTestBatch(testBatch);

    testBatch.removeDonation(donation);

    assertThat(testBatch.getDonations(), not(hasItem(donation)));
    assertThat(donation.getTestBatch(), is(nullValue(TestBatch.class)));
  }
}
