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
        .appendText("\nDonations: ").appendValue(testBatch.getDonations())
        .appendText("\nLocation: ").appendValue(testBatch.getLocation());
  }

  @Override
  public boolean matchesSafely(TestBatch actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        actual.getStatus() == expected.getStatus() &&
        Objects.equals(actual.getTestBatchDate(), expected.getTestBatchDate()) &&
        Objects.equals(actual.getDonations(), expected.getDonations()) &&
        Objects.equals(actual.getLocation(), expected.getLocation());
  }

  public static TestBatchMatcher hasSameStateAsTestBatch(TestBatch expected) {
    return new TestBatchMatcher(expected);
  }
}