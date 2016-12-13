package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.AuditRevisionViewModel;

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

  public static AuditRevisionViewModelMatcher hasSameStateAsAuditRevisionViewModel(AuditRevisionViewModel expected) {
    return new AuditRevisionViewModelMatcher(expected);
  }

}
