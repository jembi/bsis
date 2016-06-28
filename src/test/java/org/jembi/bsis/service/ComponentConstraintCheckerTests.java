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

  @Test
  public void testCanProcessWithQuarantinedComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.QUARANTINED).withParentComponent(aComponent().build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithAvailableComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.AVAILABLE).withParentComponent(aComponent().build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithUnsafeComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.UNSAFE).withParentComponent(aComponent().build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithExpiredComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.EXPIRED).withParentComponent(aComponent().build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithIssuedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.ISSUED).withParentComponent(aComponent().build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithUsedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.USED).withParentComponent(aComponent().build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithDiscardedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.DISCARDED).withParentComponent(aComponent().build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithProcessedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.PROCESSED).withParentComponent(aComponent().build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithSplitComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.SPLIT).withParentComponent(aComponent().build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithNoWeightForInitialComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withParentComponent(null)
        .withWeight(null)
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithWeightForInitialComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withParentComponent(null)
        .withWeight(450)
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanRecordWeigthWithInitialComponent_shouldReturnTrue() {
    Component component = aComponent().withParentComponent(null).build();
    boolean canRecordWeight = componentConstraintChecker.canRecordWeight(component);
    assertThat(canRecordWeight, is(true));
  }

  @Test
  public void testCanRecordWeigthWithNotAnInitialComponent_shouldReturnFalse() {
    Component parentComponent = aComponent().build();
    Component component = aComponent().withParentComponent(parentComponent).build();
    boolean canRecordWeight = componentConstraintChecker.canRecordWeight(component);
    assertThat(canRecordWeight, is(false));
  }

}
