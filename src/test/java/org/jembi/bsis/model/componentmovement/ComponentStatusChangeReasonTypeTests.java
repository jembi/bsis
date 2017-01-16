package org.jembi.bsis.model.componentmovement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class ComponentStatusChangeReasonTypeTests {

  @Test
  public void testCanBeRolledBackWithInvalidWeight_returnTrue() throws Exception {
    ComponentStatusChangeReasonType status = ComponentStatusChangeReasonType.INVALID_WEIGHT;
    
    boolean result = ComponentStatusChangeReasonType.canBeRolledBack(status);
    
    assertThat(result, is(true));    
  }
  
  @Test
  public void testCanBeRolledBackWithTestResults_returnFalse() throws Exception {
    ComponentStatusChangeReasonType status = ComponentStatusChangeReasonType.TEST_RESULTS;
    
    boolean result = ComponentStatusChangeReasonType.canBeRolledBack(status);
    
    assertThat(result, is(false));    
  }
  
  @Test
  public void testCanBeRolledBackWithTestResultsContainsPlasma_returnFalse() throws Exception {
    ComponentStatusChangeReasonType status = ComponentStatusChangeReasonType.TEST_RESULTS_CONTAINS_PLASMA;
    
    boolean result = ComponentStatusChangeReasonType.canBeRolledBack(status);
    
    assertThat(result, is(false));    
  }
  
  @Test
  public void testCanBeRolledBackWithUnsafeParent_returnFalse() throws Exception {
    ComponentStatusChangeReasonType status = ComponentStatusChangeReasonType.UNSAFE_PARENT;
    
    boolean result = ComponentStatusChangeReasonType.canBeRolledBack(status);
    
    assertThat(result, is(false));    
  }
  
  @Test
  public void testCanBeRolledBackWithLowWeight_returnTrue() throws Exception {
    ComponentStatusChangeReasonType status = ComponentStatusChangeReasonType.LOW_WEIGHT;
    
    boolean result = ComponentStatusChangeReasonType.canBeRolledBack(status);
    
    assertThat(result, is(true));    
  }
  
  @Test
  public void testCanBeRolledBackWithNull_returnTrue() throws Exception {
    ComponentStatusChangeReasonType status = null;
    
    boolean result = ComponentStatusChangeReasonType.canBeRolledBack(status);
    
    assertThat(result, is(true));    
  }
  
}