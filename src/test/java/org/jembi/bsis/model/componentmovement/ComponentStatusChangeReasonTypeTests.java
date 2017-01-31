package org.jembi.bsis.model.componentmovement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class ComponentStatusChangeReasonTypeTests {

  @Test
  public void testCanBeRolledBackWithInvalidWeight_shouldReturnTrue() throws Exception {
    ComponentStatusChangeReasonType status = ComponentStatusChangeReasonType.INVALID_WEIGHT;
    
    boolean result = ComponentStatusChangeReasonType.canBeRolledBack(status);
    
    assertThat(result, is(true));    
  }
  
  @Test
  public void testCanBeRolledBackWithTestResults_shouldReturnFalse() throws Exception {
    ComponentStatusChangeReasonType status = ComponentStatusChangeReasonType.TEST_RESULTS;
    
    boolean result = ComponentStatusChangeReasonType.canBeRolledBack(status);
    
    assertThat(result, is(false));    
  }
  
  @Test
  public void testCanBeRolledBackWithTestResultsContainsPlasma_shouldReturnFalse() throws Exception {
    ComponentStatusChangeReasonType status = ComponentStatusChangeReasonType.TEST_RESULTS_CONTAINS_PLASMA;
    
    boolean result = ComponentStatusChangeReasonType.canBeRolledBack(status);
    
    assertThat(result, is(false));    
  }
  
  @Test
  public void testCanBeRolledBackWithUnsafeParent_shouldReturnFalse() throws Exception {
    ComponentStatusChangeReasonType status = ComponentStatusChangeReasonType.UNSAFE_PARENT;
    
    boolean result = ComponentStatusChangeReasonType.canBeRolledBack(status);
    
    assertThat(result, is(false));    
  }
  
  @Test
  public void testCanBeRolledBackWithLowWeight_shouldReturnTrue() throws Exception {
    ComponentStatusChangeReasonType status = ComponentStatusChangeReasonType.LOW_WEIGHT;
    
    boolean result = ComponentStatusChangeReasonType.canBeRolledBack(status);
    
    assertThat(result, is(true));    
  }
  
  @Test
  public void testCanBeRolledBackWithNull_shouldReturnTrue() throws Exception {
    ComponentStatusChangeReasonType status = null;
    
    boolean result = ComponentStatusChangeReasonType.canBeRolledBack(status);
    
    assertThat(result, is(true));    
  }
  
}