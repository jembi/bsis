package helpers.matchers;

import org.hamcrest.Description;

import org.hamcrest.TypeSafeMatcher;
import viewmodel.AuditRevisionViewModel;

import java.util.Objects;

public class AuditRevisionViewModelMatcher extends TypeSafeMatcher<AuditRevisionViewModel> {

  private AuditRevisionViewModel expected;

  private AuditRevisionViewModelMatcher(AuditRevisionViewModel expected) {
    this.expected = expected;
  }

  public static AuditRevisionViewModelMatcher hasSameStateAsAuditRevisionViewModel(AuditRevisionViewModel expected) {
    return new AuditRevisionViewModelMatcher(expected);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An AuditRevisionViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nRevision date: ").appendValue(expected.getRevisionDate())
        .appendText("\nUser: ").appendValue(expected.getUser())
        .appendText("\nEntity revisions: ").appendValue(expected.getEntityModifications());
  }

  @Override
  public boolean matchesSafely(AuditRevisionViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getRevisionDate(), expected.getRevisionDate()) &&
        Objects.equals(actual.getUser(), expected.getUser()) &&
        Objects.equals(actual.getEntityModifications(), expected.getEntityModifications());
  }

}
