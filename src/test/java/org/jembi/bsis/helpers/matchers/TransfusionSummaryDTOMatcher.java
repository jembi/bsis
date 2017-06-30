package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.dto.TransfusionSummaryDTO;

public class TransfusionSummaryDTOMatcher extends AbstractTypeSafeMatcher<TransfusionSummaryDTO> {

  public TransfusionSummaryDTOMatcher(TransfusionSummaryDTO expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, TransfusionSummaryDTO transfusionSummaryDTO) {
    description.appendText("A TransfusionSummaryDTO with the following state:")
        .appendText("\nCount: ").appendValue(transfusionSummaryDTO.getCount())
        .appendText("\nTransfusion Reaction Type: ").appendValue(transfusionSummaryDTO.getTransfusionReactionType())
        .appendText("\nTransfusion Site: ").appendValue(transfusionSummaryDTO.getTransfusionSite()) 
        .appendText("\nTransfusion Outcome: ").appendValue(transfusionSummaryDTO.getTransfusionOutcome());
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