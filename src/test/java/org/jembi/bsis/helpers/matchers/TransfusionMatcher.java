package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.transfusion.Transfusion;

public class TransfusionMatcher extends TypeSafeMatcher<Transfusion> {

  private Transfusion expected;

  public TransfusionMatcher(Transfusion expected) {
    this.expected = expected;
  }
  
  @Override
  public void describeTo(Description description) {
    description.appendText("A transfusion with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nCreated By: ").appendValue(expected.getCreatedBy())
        .appendText("\nCreated Date: ").appendValue(expected.getCreatedDate())
        .appendText("\nLast Updated By: ").appendValue(expected.getLastUpdatedBy())
        .appendText("\nLast Updated Date: ").appendValue(expected.getLastUpdated())
        .appendText("\nDIN: ").appendValue(expected.getDonationIdentificationNumber())
        .appendText("\nPatient: ").appendValue(expected.getPatient())
        .appendText("\nComponent: ").appendValue(expected.getComponent())
        .appendText("\nNotes: ").appendValue(expected.getNotes())
        .appendText("\nReceived From: ").appendValue(expected.getReceivedFrom())
        .appendText("\ntTransfusion Reaction Type: ").appendValue(expected.getTransfusionReactionType())
        .appendText("\nTransfusion Outcome: ").appendValue(expected.getTransfusionOutcome())
        .appendText("\nDate Transfused: ").appendValue(expected.getDateTransfused())
        .appendText("\nReaction Type: ").appendValue(expected.getTransfusionReactionType());
  }
  
  @Override
  public boolean matchesSafely(Transfusion actual) {

    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getCreatedBy(), expected.getCreatedBy()) &&
        Objects.equals(actual.getCreatedDate(), expected.getCreatedDate()) &&
        Objects.equals(actual.getLastUpdatedBy(), expected.getLastUpdatedBy()) &&
        Objects.equals(actual.getLastUpdated(), expected.getLastUpdated()) &&
        Objects.equals(actual.getDonationIdentificationNumber(), expected.getDonationIdentificationNumber()) &&
        Objects.equals(actual.getPatient(), expected.getPatient()) &&
        Objects.equals(actual.getNotes(), expected.getNotes()) &&
        Objects.equals(actual.getComponent(), expected.getComponent()) &&
        Objects.equals(actual.getReceivedFrom(), expected.getReceivedFrom()) &&
        Objects.equals(actual.getTransfusionReactionType(), expected.getTransfusionReactionType()) &&
        Objects.equals(actual.getTransfusionOutcome(), expected.getTransfusionOutcome()) &&
        Objects.equals(actual.getDateTransfused(), expected.getDateTransfused()) &&
        Objects.equals(actual.getTransfusionReactionType(), expected.getTransfusionReactionType());
  }

  public static TransfusionMatcher hasSameStateAsTransfusion(Transfusion expected) {
    return new TransfusionMatcher(expected);
  }

}
