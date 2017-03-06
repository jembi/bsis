package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.dto.TransfusionSummaryDTO;

public class TransfusionSummaryDTOMatcher extends TypeSafeMatcher<TransfusionSummaryDTO> {

  private TransfusionSummaryDTO expected;

  public TransfusionSummaryDTOMatcher(TransfusionSummaryDTO expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A transfusion summary dto with the following state:")
        .appendText("\nCount: ").appendValue(expected.getCount())
        .appendText("\nTransfusion Reaction Type: ").appendValue(expected.getTransfusionReactionType())
        .appendText("\nTransfusion Site: ").appendValue(expected.getTransfusionSite()) 
        .appendText("\nTransfusion Outcome: ").appendValue(expected.getTransfusionOutcome());
  }

  @Override
  public boolean matchesSafely(TransfusionSummaryDTO actual) {
    return Objects.equals(actual.getCount(), expected.getCount()) &&
        Objects.equals(actual.getTransfusionReactionType(), expected.getTransfusionReactionType()) &&
        Objects.equals(actual.getTransfusionSite(), expected.getTransfusionSite()) &&
        Objects.equals(actual.getTransfusionOutcome(), expected.getTransfusionOutcome());
  }

  public static TransfusionSummaryDTOMatcher hasSameStateAsTransfusionSummaryDTO(TransfusionSummaryDTO expected) {
    return new TransfusionSummaryDTOMatcher(expected);
  }
}