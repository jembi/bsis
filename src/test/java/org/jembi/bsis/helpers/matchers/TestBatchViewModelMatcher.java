package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.TestBatchViewModel;

public class TestBatchViewModelMatcher extends TypeSafeMatcher<TestBatchViewModel> {

  private TestBatchViewModel expected;

  public TestBatchViewModelMatcher(TestBatchViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A test batch view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nStatus: ").appendValue(expected.getStatus())
        .appendText("\nBatch number: ").appendValue(expected.getBatchNumber())
        .appendText("\nCreated date: ").appendValue(expected.getCreatedDate())
        .appendText("\nLast updated date: ").appendValue(expected.getLastUpdated())
.appendText("\nNotes: ").appendValue(expected.getNotes());
  }

  @Override
  protected void describeMismatchSafely(TestBatchViewModel actual, Description description) {

    description.appendText("The following fields did not match:");

    if (!Objects.equals(actual.getId(), expected.getId())) {
      description.appendText("\nId: expected = ").appendValue(expected.getId())
          .appendText(", actual = ").appendValue(actual.getId());
    }

    if (!Objects.equals(actual.getStatus(), expected.getStatus())) {
      description.appendText("\nStatus: expected = ").appendValue(expected.getStatus())
          .appendText(", actual = ").appendValue(actual.getStatus());
    }

    if (!Objects.equals(actual.getBatchNumber(), expected.getBatchNumber())) {
      description.appendText("\nBatch number: expected = ").appendValue(expected.getBatchNumber())
          .appendText(", actual = ").appendValue(actual.getBatchNumber());
    }

    if (!Objects.equals(actual.getCreatedDate(), expected.getCreatedDate())) {
      description.appendText("\nCreated date: expected = ").appendValue(expected.getCreatedDate())
          .appendText(", actual = ").appendValue(actual.getCreatedDate());
    }

    if (!Objects.equals(actual.getLastUpdated(), expected.getLastUpdated())) {
      description.appendText("\nLast updated: expected = ").appendValue(expected.getLastUpdated())
          .appendText(", actual = ").appendValue(actual.getLastUpdated());
    }

    if (!Objects.equals(actual.getNotes(), expected.getNotes())) {
      description.appendText("\nNotes: expected = ").appendValue(expected.getNotes())
          .appendText(", actual = ").appendValue(actual.getNotes());
    }
  }

  @Override
  public boolean matchesSafely(TestBatchViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getBatchNumber(), expected.getBatchNumber()) &&
        Objects.equals(actual.getCreatedDate(), expected.getCreatedDate()) &&
        Objects.equals(actual.getLastUpdated(), expected.getLastUpdated()) &&
        Objects.equals(actual.getNotes(), expected.getNotes());
  }

  public static TestBatchViewModelMatcher hasSameStateAsTestBatchViewModel(TestBatchViewModel expected) {
    return new TestBatchViewModelMatcher(expected);
  }

}
