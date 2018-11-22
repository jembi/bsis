package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.TransfusionReactionTypeViewModel;

public class TransfusionReactionTypeViewModelMatcher extends AbstractTypeSafeMatcher<TransfusionReactionTypeViewModel> {

  public TransfusionReactionTypeViewModelMatcher(TransfusionReactionTypeViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, TransfusionReactionTypeViewModel transfusionReactionTypeViewModel) {
    description.appendText("A TransfusionReactionTypeViewModel with the following state:")
        .appendText("\nId: ").appendValue(transfusionReactionTypeViewModel.getId())
        .appendText("\nName: ").appendValue(transfusionReactionTypeViewModel.getName())
        .appendText("\nIsDeleted: ").appendValue(transfusionReactionTypeViewModel.getIsDeleted());
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
