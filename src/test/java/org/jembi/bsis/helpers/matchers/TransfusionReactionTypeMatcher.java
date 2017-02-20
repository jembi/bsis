package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;

public class TransfusionReactionTypeMatcher extends TypeSafeMatcher<TransfusionReactionType> {

  private TransfusionReactionType expected;

  public TransfusionReactionTypeMatcher(TransfusionReactionType expected) {
    this.expected = expected;
  }
  
  @Override
  public void describeTo(Description description) {
    description.appendText("A transfusion reaction type with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nDescription: ").appendValue(expected.getDescription())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nCreated By: ").appendValue(expected.getCreatedBy())
        .appendText("\nCreated Date: ").appendValue(expected.getCreatedDate())
        .appendText("\nLast Updated By: ").appendValue(expected.getLastUpdatedBy())
        .appendText("\nLast Updated Date: ").appendValue(expected.getLastUpdated());
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
