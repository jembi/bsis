package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.TransfusionViewModel;

public class TransfusionViewModelMatcher extends TypeSafeMatcher<TransfusionViewModel> {

  private TransfusionViewModel expected;

  public TransfusionViewModelMatcher(TransfusionViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A transfusion view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nDIN: ").appendValue(expected.getDonationIdentificationNumber())
        .appendText("\nReceived From: ").appendValue(expected.getReceivedFrom())
        .appendText("\nTransfusion Outcome: ").appendValue(expected.getTransfusionOutcome())
        .appendText("\nDate Transfused: ").appendValue(expected.getDateTransfused())
        .appendText("\nComponentCode: ").appendValue(expected.getComponentCode())
        .appendText("\nComponentType: ").appendValue(expected.getComponentType());
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
