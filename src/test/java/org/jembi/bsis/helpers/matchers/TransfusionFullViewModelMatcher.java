package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.TransfusionFullViewModel;

public class TransfusionFullViewModelMatcher extends AbstractTypeSafeMatcher<TransfusionFullViewModel> {

  public TransfusionFullViewModelMatcher (TransfusionFullViewModel expected) {
    super(expected);
  }
  
  @Override
  public void appendDescription(Description description, TransfusionFullViewModel transfusionFullViewModel) {
    description.appendText("A TransfusionFullViewModel with the following state:")
        .appendText("\nId: ").appendValue(transfusionFullViewModel.getId())
        .appendText("\nNotes: ").appendValue(transfusionFullViewModel.getNotes())
        .appendText("\nDIN: ").appendValue(transfusionFullViewModel.getDonationIdentificationNumber())
        .appendText("\nPatient: ").appendValue(transfusionFullViewModel.getPatient())
        .appendText("\nReceived From: ").appendValue(transfusionFullViewModel.getReceivedFrom())
        .appendText("\nTransfusion Reaction Type: ").appendValue(transfusionFullViewModel.getTransfusionReactionType())
        .appendText("\nTransfusion Outcome: ").appendValue(transfusionFullViewModel.getTransfusionOutcome())
        .appendText("\nDate Transfused: ").appendValue(transfusionFullViewModel.getDateTransfused())
        .appendText("\nComponent: ").appendValue(transfusionFullViewModel.getComponent());
  }
  
  @Override
  public boolean matchesSafely(TransfusionFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
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
