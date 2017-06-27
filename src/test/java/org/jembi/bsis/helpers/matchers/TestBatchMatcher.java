package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.testbatch.TestBatch;

public class TestBatchMatcher extends TypeSafeMatcher<TestBatch> {

  private TestBatch expected;

  public TestBatchMatcher(TestBatch expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A test batch with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nStatus: ").appendValue(expected.getStatus())
        .appendText("\nTest batch date: ").appendValue(expected.getTestBatchDate())
        .appendText("\nDonation batches: ").appendValue(expected.getDonationBatches())
        .appendText("\nLocation: ").appendValue(expected.getLocation());
  }

  @Override
  protected void describeMismatchSafely(TestBatch actual, Description description) {

    description.appendText("The mismatched fields are:");

    if (!Objects.equals(actual.getId(), expected.getId())) {
      description.appendText("\nId: actual = ").appendValue(actual.getId())
          .appendText(", expected = ").appendValue(expected.getId());
    }

    if (actual.getStatus() != expected.getStatus()) {
      description.appendText("\nStatus: actual = ").appendValue(actual.getStatus())
          .appendText(", expected = ").appendValue(expected.getStatus());
    }

    if (!Objects.equals(actual.getTestBatchDate(), expected.getId())) {
      description.appendText("\nTest batch date: actual = ").appendValue(actual.getId())
          .appendText(", expected = ").appendValue(expected.getTestBatchDate());
    }
  }

  @Override
  public boolean matchesSafely(TestBatch actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        actual.getStatus() == expected.getStatus() &&
        Objects.equals(actual.getTestBatchDate(), expected.getTestBatchDate()) &&
        Objects.equals(actual.getDonationBatches(), expected.getDonationBatches()) &&
        Objects.equals(actual.getLocation(), expected.getLocation());
  }

  public static TestBatchMatcher hasSameStateAsTestBatch(TestBatch expected) {
    return new TestBatchMatcher(expected);
  }

}
