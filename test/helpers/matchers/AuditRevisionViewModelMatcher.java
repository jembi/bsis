package helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

import viewmodel.AuditRevisionViewModel;

public class AuditRevisionViewModelMatcher extends TypeSafeMatcher<AuditRevisionViewModel> {
    
    private AuditRevisionViewModel expected;
    
    public AuditRevisionViewModelMatcher(AuditRevisionViewModel expected) {
        this.expected = expected;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("An AuditRevisionViewModel with the following state:")
                .appendText("\nId: ").appendValue(expected.getId())
                .appendText("\nRevision date: ").appendValue(expected.getRevisionDate())
                .appendText("\nUser: ").appendValue(expected.getUser());
    }

    @Override
    public boolean matchesSafely(AuditRevisionViewModel actual) {
        return Objects.equals(actual.getId(), expected.getId()) &&
                Objects.equals(actual.getRevisionDate(), expected.getRevisionDate()) &&
                Objects.equals(actual.getUser(), expected.getUser());
    }
    
    public static AuditRevisionViewModelMatcher hasSameStateAsAuditRevisionViewModel(AuditRevisionViewModel expected) {
        return new AuditRevisionViewModelMatcher(expected);
    }

}
