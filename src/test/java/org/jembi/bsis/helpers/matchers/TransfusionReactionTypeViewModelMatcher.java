package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.TransfusionReactionTypeViewModel;

public class TransfusionReactionTypeViewModelMatcher extends TypeSafeMatcher<TransfusionReactionTypeViewModel> {

  private TransfusionReactionTypeViewModel expected;

  public TransfusionReactionTypeViewModelMatcher(TransfusionReactionTypeViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A TransfusionReactionTypeViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nIsDeleted: ").appendValue(expected.getIsDeleted());
  }

  @Override
  protected boolean matchesSafely(TransfusionReactionTypeViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted());
  }
  
  public static TransfusionReactionTypeViewModelMatcher hasSameStateAsTransfusionReactionTypeViewModel(TransfusionReactionTypeViewModel expected) {
    return new TransfusionReactionTypeViewModelMatcher(expected);
  }

}
