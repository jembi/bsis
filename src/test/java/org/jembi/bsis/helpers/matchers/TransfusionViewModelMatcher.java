package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.TransfusionViewModel;

public class TransfusionViewModelMatcher extends AbstractTypeSafeMatcher<TransfusionViewModel> {

  public TransfusionViewModelMatcher(TransfusionViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, TransfusionViewModel transfusionViewModel) {
    description.appendText("A TransfusionViewModel with the following state:")
        .appendText("\nId: ").appendValue(transfusionViewModel.getId())
        .appendText("\nDIN: ").appendValue(transfusionViewModel.getDonationIdentificationNumber())
        .appendText("\nReceived From: ").appendValue(transfusionViewModel.getReceivedFrom())
        .appendText("\nTransfusion Outcome: ").appendValue(transfusionViewModel.getTransfusionOutcome())
        .appendText("\nDate Transfused: ").appendValue(transfusionViewModel.getDateTransfused())
        .appendText("\nComponentCode: ").appendValue(transfusionViewModel.getComponentCode())
        .appendText("\nComponentType: ").appendValue(transfusionViewModel.getComponentType());
  }

  @Override
  public boolean matchesSafely(TransfusionViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getDonationIdentificationNumber(), expected.getDonationIdentificationNumber()) &&
        Objects.equals(actual.getComponentCode(), expected.getComponentCode()) &&
        Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getReceivedFrom(), expected.getReceivedFrom()) &&
        Objects.equals(actual.getTransfusionOutcome(), expected.getTransfusionOutcome()) &&
        Objects.equals(actual.getDateTransfused(), expected.getDateTransfused());
  }

  public static TransfusionViewModelMatcher hasSameStateAsTransfusionViewModel(TransfusionViewModel expected) {
    return new TransfusionViewModelMatcher(expected);
  }

}
