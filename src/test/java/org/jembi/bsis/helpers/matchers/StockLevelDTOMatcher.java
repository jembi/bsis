package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.dto.StockLevelDTO;

public class StockLevelDTOMatcher extends AbstractTypeSafeMatcher<StockLevelDTO> {

  public StockLevelDTOMatcher(StockLevelDTO expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, StockLevelDTO model) {
    description.appendText("A StockLevelDTOMatcher with the following state:")
        .appendText("\nComponentType: ").appendValue(model.getComponentType())
        .appendText("\nCount: ").appendValue(model.getCount())
        .appendText("\nBlood ABO: ").appendValue(model.getBloodAbo())
        .appendText("\nBlood Rh: ").appendValue(model.getBloodRh());
  }

  @Override
  public boolean matchesSafely(StockLevelDTO actual) {
    return Objects.equals(actual.getComponentType(), expected.getComponentType()) &&
        Objects.equals(actual.getCount(), expected.getCount()) &&
        Objects.equals(actual.getBloodAbo(), expected.getBloodAbo()) &&
        Objects.equals(actual.getBloodRh(), expected.getBloodRh());
  }

  public static StockLevelDTOMatcher hasSameStateAsStockLevelDTO(StockLevelDTO expected) {
    return new StockLevelDTOMatcher(expected);
  }
}
