package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.TransfusionFullViewModel;

public class TransfusionFullViewModelMatcher extends TypeSafeMatcher<TransfusionFullViewModel> {

  private TransfusionFullViewModel expected;

  public TransfusionFullViewModelMatcher (TransfusionFullViewModel expected) {
    this.expected = expected;
  }
  
  @Override
  public void describeTo(Description description) {
    description.appendText("A transfusion view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nNotes: ").appendValue(expected.getNotes())
        .appendText("\nDIN: ").appendValue(expected.getDonationIdentificationNumber())
        .appendText("\nPatient: ").appendValue(expected.getPatient())
        .appendText("\nReceived From: ").appendValue(expected.getReceivedFrom())
        .appendText("\nTransfusion Reaction Type: ").appendValue(expected.getTransfusionReactionType())
        .appendText("\nTransfusion Outcome: ").appendValue(expected.getTransfusionOutcome())
        .appendText("\nDate Transfused: ").appendValue(expected.getDateTransfused())
        .appendText("\nComponent: ").appendValue(expected.getComponent());
  }
  
  @Override
  public boolean matchesSafely(TransfusionFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getNotes(), expected.getNotes()) &&
        Objects.equals(actual.getDonationIdentificationNumber(), expected.getDonationIdentificationNumber()) &&
        Objects.equals(actual.getPatient(), expected.getPatient()) &&
        Objects.equals(actual.getComponent(), expected.getComponent()) &&
        Objects.equals(actual.getReceivedFrom(), expected.getReceivedFrom()) &&
        Objects.equals(actual.getTransfusionReactionType(), expected.getTransfusionReactionType()) &&
        Objects.equals(actual.getTransfusionOutcome(), expected.getTransfusionOutcome()) &&
        Objects.equals(actual.getDateTransfused(), expected.getDateTransfused()) &&
        Objects.equals(actual.getTransfusionReactionType(), expected.getTransfusionReactionType());
  }

  public static TransfusionFullViewModelMatcher hasSameStateAsTransfusionFullViewModel(
      TransfusionFullViewModel expected) {
    return new TransfusionFullViewModelMatcher(expected);
  }

}
