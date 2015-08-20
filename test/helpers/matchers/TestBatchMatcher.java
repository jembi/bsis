package helpers.matchers;

import java.util.Objects;

import model.testbatch.TestBatch;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class TestBatchMatcher extends TypeSafeMatcher<TestBatch> {
    
    private TestBatch expected;

    public TestBatchMatcher(TestBatch expected) {
        this.expected = expected;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("A test batch with the following state:")
                .appendText("\nId: ").appendValue(expected.getId())
                .appendText("\nStatus: ").appendValue(expected.getStatus());
    }

    @Override
    public boolean matchesSafely(TestBatch actual) {
        return Objects.equals(actual.getId(), expected.getId()) &&
                actual.getStatus() == expected.getStatus();
    }
    
    public static TestBatchMatcher hasSameStateAsTestBatch(TestBatch expected) {
        return new TestBatchMatcher(expected);
    }

}
