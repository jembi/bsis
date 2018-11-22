package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;

public class TransfusionReactionTypeMatcher extends AbstractTypeSafeMatcher<TransfusionReactionType> {

  public TransfusionReactionTypeMatcher(TransfusionReactionType expected) {
    super(expected);
  }
  
  @Override
  public void appendDescription(Description description, TransfusionReactionType transfusionReactionType) {
    description.appendText("A TransfusionReactionType with the following state:")
        .appendText("\nId: ").appendValue(transfusionReactionType.getId())
        .appendText("\nName: ").appendValue(transfusionReactionType.getName())
        .appendText("\nDescription: ").appendValue(transfusionReactionType.getDescription())
        .appendText("\nDeleted: ").appendValue(transfusionReactionType.getIsDeleted())
        .appendText("\nCreated By: ").appendValue(transfusionReactionType.getCreatedBy())
        .appendText("\nCreated Date: ").appendValue(transfusionReactionType.getCreatedDate())
        .appendText("\nLast Updated By: ").appendValue(transfusionReactionType.getLastUpdatedBy())
        .appendText("\nLast Updated Date: ").appendValue(transfusionReactionType.getLastUpdated());
  }
  
  @Override
  public boolean matchesSafely(TransfusionReactionType actual) {

    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getName(), expected.getName()) &&
        Objects.equals(actual.getDescription(), expected.getDescription()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getCreatedBy(), expected.getCreatedBy()) &&
        Objects.equals(actual.getCreatedDate(), expected.getCreatedDate()) &&
        Objects.equals(actual.getLastUpdatedBy(), expected.getLastUpdatedBy()) &&
        Objects.equals(actual.getLastUpdated(), expected.getLastUpdated());
  }

  public static TransfusionReactionTypeMatcher hasSameStateAsTransfusionReactionType(TransfusionReactionType expected) {
    return new TransfusionReactionTypeMatcher(expected);
  }

}
