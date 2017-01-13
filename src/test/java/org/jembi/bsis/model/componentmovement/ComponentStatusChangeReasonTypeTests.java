package org.jembi.bsis.model.componentmovement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;

public class ComponentStatusChangeReasonTypeTests extends UnitTestSuite {

  @Test
  public void testComponentStatusChangeReasonTypeThatCanBeRolledBack() throws Exception {
    ComponentStatusChangeReasonType nullComponentStatusChangeReasonType = null;
    assertThat(ComponentStatusChangeReasonType.canBeRolledBack(ComponentStatusChangeReasonType.INVALID_WEIGHT), is(true));
    assertThat(ComponentStatusChangeReasonType.canBeRolledBack(ComponentStatusChangeReasonType.TEST_RESULTS), is(false));
    assertThat(ComponentStatusChangeReasonType.canBeRolledBack(ComponentStatusChangeReasonType.TEST_RESULTS_CONTAINS_PLASMA), is(false));
    assertThat(ComponentStatusChangeReasonType.canBeRolledBack(ComponentStatusChangeReasonType.UNSAFE_PARENT), is(false));
    assertThat(ComponentStatusChangeReasonType.canBeRolledBack(ComponentStatusChangeReasonType.INVALID_WEIGHT_CONTAINS_PLASMA), is(true));
    assertThat(ComponentStatusChangeReasonType.canBeRolledBack(nullComponentStatusChangeReasonType), is(true));
    
  }
  
}