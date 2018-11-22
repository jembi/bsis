package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.model.returnform.ReturnForm;

public class ReturnFormMatcher extends AbstractTypeSafeMatcher<ReturnForm> {

  public ReturnFormMatcher(ReturnForm expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, ReturnForm returnForm) {
    description.appendText("An ReturnForm with the following state:")
        .appendText("\nId: ").appendValue(returnForm.getId())
        .appendText("\nCreated Date: ").appendValue(returnForm.getCreatedDate())
        .appendText("\nStatus: ").appendValue(returnForm.getStatus())
        .appendText("\nReturn Date: ").appendValue(returnForm.getReturnDate())
        .appendText("\nReturned From: ").appendValue(returnForm.getReturnedFrom())
        .appendText("\nReturned To: ").appendValue(returnForm.getReturnedTo())
        .appendText("\nComponents: ").appendValue(returnForm.getComponents())
        .appendText("\nDeleted: ").appendValue(returnForm.getIsDeleted());
  }
  
  @Override
  public boolean matchesSafely(ReturnForm actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getCreatedDate(), expected.getCreatedDate()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getReturnDate(), expected.getReturnDate()) &&
        Objects.equals(actual.getReturnedFrom(), expected.getReturnedFrom()) &&
        Objects.equals(actual.getReturnedTo(), expected.getReturnedTo()) &&
        Objects.equals(actual.getComponents(), expected.getComponents()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted());
  }

  public static ReturnFormMatcher hasSameStateAsReturnForm(ReturnForm expected) {
    return new ReturnFormMatcher(expected);
  }

}
