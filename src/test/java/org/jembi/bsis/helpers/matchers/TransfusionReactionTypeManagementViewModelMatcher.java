package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.TransfusionReactionTypeManagementViewModel;

public class TransfusionReactionTypeManagementViewModelMatcher extends AbstractTypeSafeMatcher<TransfusionReactionTypeManagementViewModel> {

  public TransfusionReactionTypeManagementViewModelMatcher(TransfusionReactionTypeManagementViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, TransfusionReactionTypeManagementViewModel transfusionReactionTypeManagementViewModel) {
    description.appendText("A TransfusionReactionTypeManagementViewModel with the following state:")
        .appendText("\nId: ").appendValue(transfusionReactionTypeManagementViewModel.getId())
        .appendText("\nName: ").appendValue(transfusionReactionTypeManagementViewModel.getName())
        .appendText("\nIsDeleted: ").appendValue(transfusionReactionTypeManagementViewModel.getIsDeleted())
        .appendText("\nDescription: ").appendValue(transfusionReactionTypeManagementViewModel.getDescription());
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
