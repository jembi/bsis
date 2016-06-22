package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;

import org.jembi.bsis.controller.ComponentConstraintChecker;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;

public class ComponentConstraintCheckerTests extends UnitTestSuite {
  
  @InjectMocks
  private ComponentConstraintChecker componentConstraintChecker;
  
  @Test
  public void testCanDiscardWithQuarantinedComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.QUARANTINED).build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(true));
  }
  
  @Test
  public void testCanDiscardWithAvailableComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.AVAILABLE).build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(true));
  }
  
  @Test
  public void testCanDiscardWithUnsafeComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.UNSAFE).build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(true));
  }
  
  @Test
  public void testCanDiscardWithExpiredComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.EXPIRED).build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(true));
  }
  
  @Test
  public void testCanDiscardWithIssuedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.ISSUED).build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(false));
  }
  
  @Test
  public void testCanDiscardWithUsedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.USED).build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(false));
  }
  
  @Test
  public void testCanDiscardWithDiscardedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.DISCARDED).build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(false));
  }
  
  @Test
  public void testCanDiscardWithProcessedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.PROCESSED).build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(false));
  }
  
  @Test
  public void testCanDiscardWithSplitComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.SPLIT).build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(false));
  }

}
