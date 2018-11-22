package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.model.transfusion.Transfusion;

public class TransfusionMatcher extends AbstractTypeSafeMatcher<Transfusion> {

  public TransfusionMatcher(Transfusion expected) {
    super(expected);
  }
  
  @Override
  public void appendDescription(Description description, Transfusion transfusion) {
    description.appendText("A Transfusion with the following state:")
        .appendText("\nId: ").appendValue(transfusion.getId())
        .appendText("\nDeleted: ").appendValue(transfusion.getIsDeleted())
        .appendText("\nCreated By: ").appendValue(transfusion.getCreatedBy())
        .appendText("\nCreated Date: ").appendValue(transfusion.getCreatedDate())
        .appendText("\nLast Updated By: ").appendValue(transfusion.getLastUpdatedBy())
        .appendText("\nLast Updated Date: ").appendValue(transfusion.getLastUpdated())
        .appendText("\nPatient: ").appendValue(transfusion.getPatient())
        .appendText("\nComponent: ").appendValue(transfusion.getComponent())
        .appendText("\nNotes: ").appendValue(transfusion.getNotes())
        .appendText("\nReceived From: ").appendValue(transfusion.getReceivedFrom())
        .appendText("\ntTransfusion Reaction Type: ").appendValue(transfusion.getTransfusionReactionType())
        .appendText("\nTransfusion Outcome: ").appendValue(transfusion.getTransfusionOutcome())
        .appendText("\nDate Transfused: ").appendValue(transfusion.getDateTransfused())
        .appendText("\nReaction Type: ").appendValue(transfusion.getTransfusionReactionType());
  }
  
  @Override
  public boolean matchesSafely(Transfusion actual) {

    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getCreatedBy(), expected.getCreatedBy()) &&
        Objects.equals(actual.getCreatedDate(), expected.getCreatedDate()) &&
        Objects.equals(actual.getLastUpdatedBy(), expected.getLastUpdatedBy()) &&
        Objects.equals(actual.getLastUpdated(), expected.getLastUpdated()) &&
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
