package org.jembi.bsis.helpers.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.TransfusionViewModel;

import java.util.Objects;

public class TransfusionViewModelMatcher extends TypeSafeMatcher<TransfusionViewModel> {

  private TransfusionViewModel expected;

  public TransfusionViewModelMatcher (TransfusionViewModel expected) {
    this.expected = expected;
  }
  
  @Override
  public void describeTo(Description description) {
    description.appendText("A transfusion with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nNotes: ").appendValue(expected.getNotes())
        .appendText("\nDIN: ").appendValue(expected.getDonationIdentificationNumber())
        .appendText("\nPatient: ").appendValue(expected.getPatient())
        .appendText("\nUsage Site: ").appendValue(expected.getUsageSite())
        .appendText("\ntTransfusion Reaction Type: ").appendValue(expected.getTransfusionReactionType())
        .appendText("\nTransfusion Outcome: ").appendValue(expected.getTransfusionOutcome())
        .appendText("\nDate Transfused: ").appendValue(expected.getDateTransfused())
        .appendText("\nComponent Type: ").appendValue(expected.getComponentType());
  }
  
  @Override
  public boolean matchesSafely(TransfusionViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getNotes(), expected.getNotes()) &&
        Objects.equals(actual.getDonationIdentificationNumber(), expected.getDonationIdentificationNumber()) &&
        Objects.equals(actual.getPatient(), expected.getPatient()) &&
        Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getUsageSite(), expected.getUsageSite()) &&
        Objects.equals(actual.getTransfusionReactionType(), expected.getTransfusionReactionType()) &&
        Objects.equals(actual.getTransfusionOutcome(), expected.getTransfusionOutcome()) &&
        Objects.equals(actual.getDateTransfused(), expected.getDateTransfused()) &&
        Objects.equals(actual.getTransfusionReactionType(), expected.getTransfusionReactionType());
  }

  public static TransfusionViewModelMatcher hasSameStateAsTransfusionViewModel(TransfusionViewModel expected) {
    return new TransfusionViewModelMatcher(expected);
  }

}
