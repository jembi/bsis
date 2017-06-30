package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.model.testbatch.TestBatch;

public class TestBatchMatcher extends AbstractTypeSafeMatcher<TestBatch> {

  public TestBatchMatcher(TestBatch expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, TestBatch testBatch) {
    description.appendText("A test batch with the following state:")
        .appendText("\nId: ").appendValue(testBatch.getId())
        .appendText("\nStatus: ").appendValue(testBatch.getStatus())
        .appendText("\nTest batch date: ").appendValue(testBatch.getTestBatchDate())
        .appendText("\nDonation batches: ").appendValue(testBatch.getDonationBatches())
        .appendText("\nLocation: ").appendValue(testBatch.getLocation());
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
