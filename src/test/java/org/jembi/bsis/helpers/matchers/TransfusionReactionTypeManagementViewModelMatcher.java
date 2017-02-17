package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.TransfusionReactionTypeManagementViewModel;

public class TransfusionReactionTypeManagementViewModelMatcher extends TypeSafeMatcher<TransfusionReactionTypeManagementViewModel> {

  private TransfusionReactionTypeManagementViewModel expected;

  public TransfusionReactionTypeManagementViewModelMatcher(TransfusionReactionTypeManagementViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A TransfusionReactionTypeManagementViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nIsDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nDescription: ").appendValue(expected.getDescription());
  }

  @Override
  protected boolean matchesSafely(TransfusionReactionTypeManagementViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted())
        && Objects.equals(actual.getDescription(), expected.getDescription());
  }
  
  public static TransfusionReactionTypeManagementViewModelMatcher hasSameStateAsTransfusionReactionTypeManagementViewModel(TransfusionReactionTypeManagementViewModel expected) {
    return new TransfusionReactionTypeManagementViewModelMatcher(expected);
  }

}
