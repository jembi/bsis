package helpers.matchers;

import model.testbatch.TestBatch;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Objects;

public class TestBatchMatcher extends TypeSafeMatcher<TestBatch> {

  private TestBatch expected;

  private TestBatchMatcher(TestBatch expected) {
    this.expected = expected;
  }

  public static TestBatchMatcher hasSameStateAsTestBatch(TestBatch expected) {
    return new TestBatchMatcher(expected);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A test batch with the following state:")
            .appendText("\nId: ").appendValue(expected.getId())
            .appendText("\nStatus: ").appendValue(expected.getStatus())
            .appendText("\nCreated date: ").appendValue(expected.getCreatedDate());
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

    if (!Objects.equals(actual.getCreatedDate(), expected.getId())) {
      description.appendText("\nCreated date: actual = ").appendValue(actual.getId())
              .appendText(", expected = ").appendValue(expected.getCreatedDate());
    }
  }

  @Override
  public boolean matchesSafely(TestBatch actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
            actual.getStatus() == expected.getStatus() &&
            Objects.equals(actual.getCreatedDate(), expected.getCreatedDate());
  }

}
