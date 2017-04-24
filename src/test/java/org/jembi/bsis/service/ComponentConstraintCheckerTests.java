package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBatchBuilder.aComponentBatch;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.UUID;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ComponentConstraintCheckerTests extends UnitTestSuite {
  
  @InjectMocks
  private ComponentConstraintChecker componentConstraintChecker;
  
  @Mock
  private ComponentRepository componentRepository;
  
  private static final UUID COMPONENT_ID_1 = UUID.randomUUID();
  private static final UUID COMPONENT_ID_2 = UUID.randomUUID();
  private static final UUID COMPONENT_ID_3 = UUID.randomUUID();

  @Test
  public void testCanDiscardWithQuarantinedComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.QUARANTINED)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(true));
  }
  
  @Test
  public void testCanDiscardWithAvailableComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(true));
  }
  
  @Test
  public void testCanDiscardWithUnsafeComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.UNSAFE)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(true));
  }
  
  @Test
  public void testCanDiscardWithExpiredComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.EXPIRED)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(true));
  }
  
  @Test
  public void testCanDiscardWithNoComponentBatch_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.ISSUED)
        .build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(false));
  }
  
  @Test
  public void testCanDiscardWithIssuedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.ISSUED)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(false));
  }
  
  @Test
  public void testCanDiscardWithUsedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.TRANSFUSED)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(false));
  }
  
  @Test
  public void testCanDiscardWithDiscardedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.DISCARDED)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(false));
  }
  
  @Test
  public void testCanDiscardWithProcessedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.PROCESSED)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canDiscard = componentConstraintChecker.canDiscard(component);
    assertThat(canDiscard, is(false));
  }
  
  @Test
  public void testCanProcessWithQuarantinedComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.QUARANTINED)
        .withComponentBatch(aComponentBatch().build())
        .withParentComponent(aComponent().build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithAvailableComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch().build())
        .withParentComponent(aComponent().build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithUnsafeComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.UNSAFE)
        .withParentComponent(aComponent().build())
        .withComponentBatch(aComponentBatch().build())
        .withComponentType(aComponentType()
            .withProducedComponentTypeCombination(aComponentTypeCombination()
                .build())
            .build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithExpiredComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.EXPIRED)
        .withComponentBatch(aComponentBatch().build())
        .withParentComponent(aComponent().build())
        .withComponentType(aComponentType()
            .withProducedComponentTypeCombination(aComponentTypeCombination()
                .build())
            .build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }
  
  @Test
  public void testCanProcessWithNoComponentBatch_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withParentComponent(aComponent().build())
        .withComponentType(aComponentType()
            .withProducedComponentTypeCombination(aComponentTypeCombination()
                .build())
            .build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithIssuedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.ISSUED)
        .withComponentBatch(aComponentBatch().build())
        .withParentComponent(aComponent().build())
        .withComponentType(aComponentType()
            .withProducedComponentTypeCombination(aComponentTypeCombination()
                .build())
            .build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithUsedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.TRANSFUSED)
        .withParentComponent(aComponent().build())
        .withComponentBatch(aComponentBatch().build())
        .withComponentType(aComponentType()
            .withProducedComponentTypeCombination(aComponentTypeCombination()
                .build())
            .build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithDiscardedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.DISCARDED)
        .withParentComponent(aComponent().build())
        .withComponentBatch(aComponentBatch().build())
        .withComponentType(aComponentType()
            .withProducedComponentTypeCombination(aComponentTypeCombination()
                .build())
            .build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithProcessedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.PROCESSED)
        .withComponentBatch(aComponentBatch().build())
        .withParentComponent(aComponent().build())
        .withComponentType(aComponentType()
            .withProducedComponentTypeCombination(aComponentTypeCombination()
                .build())
            .build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithNoWeightForInitialComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch().build())
        .withParentComponent(null)
        .withWeight(null)
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithValidWeightForInitialComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch().build())
        .withParentComponent(null)
        .withWeight(450)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithInvalidWeightForInitialComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch().build())
        .withParentComponent(null)
        .withWeight(350)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithNoCombinations_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch().build())
        .withParentComponent(null)
        .withWeight(450)
        .withDonation(aDonation().withPackType(aPackType().withLowVolumeWeight(316).withMinWeight(400).withMaxWeight(500).build()).build())
        .withComponentType(aComponentType().build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }
  
  @Test
  public void testCanProcessWithCombinations_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch().build())
        .withParentComponent(null)
        .withWeight(450)
        .withDonation(aDonation().withPackType(aPackType().withLowVolumeWeight(316).withMinWeight(400).withMaxWeight(500).build()).build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanPreProcessComponentWithQuarantinedComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.QUARANTINED)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canPreProcess = componentConstraintChecker.canPreProcess(component);
    assertThat(canPreProcess, is(true));
  }

  @Test
  public void testCanPreProcessComponentWithAvailableComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canPreProcess = componentConstraintChecker.canPreProcess(component);
    assertThat(canPreProcess, is(true));
  }

  @Test
  public void testCanPreProcessComponentWithUnsafeComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withComponentBatch(aComponentBatch().build())
        .withStatus(ComponentStatus.UNSAFE)
        .build();
    boolean canPreProcess = componentConstraintChecker.canPreProcess(component);
    assertThat(canPreProcess, is(true));
  }

  @Test
  public void testCanPreProcessComponentWithExpiredComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.EXPIRED)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canPreProcess = componentConstraintChecker.canPreProcess(component);
    assertThat(canPreProcess, is(true));
  }
  
  @Test
  public void testCanPreProcessComponentWithNoComponentBatch_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.QUARANTINED)
        .build();
    boolean canPreProcess = componentConstraintChecker.canPreProcess(component);
    assertThat(canPreProcess, is(false));
  }
  
  @Test
  public void testCanPreProcessComponentWithIssuedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.ISSUED)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canPreProcess = componentConstraintChecker.canPreProcess(component);
    assertThat(canPreProcess, is(false));
  }

  @Test
  public void testCanPreProcessComponentWithUsedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.TRANSFUSED)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canPreProcess = componentConstraintChecker.canPreProcess(component);
    assertThat(canPreProcess, is(false));
  }

  @Test
  public void testCanPreProcessComponentWithcanPreProcessComponentedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.DISCARDED)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canPreProcess = componentConstraintChecker.canPreProcess(component);
    assertThat(canPreProcess, is(false));
  }

  @Test
  public void testCanPreProcessComponentWithProcessedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.PROCESSED)
        .withComponentBatch(aComponentBatch().build())
        .build();
    boolean canPreProcess = componentConstraintChecker.canPreProcess(component);
    assertThat(canPreProcess, is(false));
  }

  @Test
  public void testCanRecordWeigthWithInitialComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch().build())
        .withParentComponent(null)
        .build();
    boolean canPreProcess = componentConstraintChecker.canPreProcess(component);
    assertThat(canPreProcess, is(true));
  }

  @Test
  public void testCanRecordWeigthWithNotAnInitialComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentBatch(aComponentBatch().build())
        .withParentComponent(aComponent().build())
        .build();
    boolean canPreProcess = componentConstraintChecker.canPreProcess(component);
    assertThat(canPreProcess, is(false));
  }

  @Test
  public void testCanUnprocessWithQuarantinedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.QUARANTINED).build();
    boolean canUnprocess = componentConstraintChecker.canUnprocess(component);
    assertThat(canUnprocess, is(false));
  }

  @Test
  public void testCanUnprocessWithAvailableComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.AVAILABLE).build();
    boolean canUnprocess = componentConstraintChecker.canUnprocess(component);
    assertThat(canUnprocess, is(false));
  }

  @Test
  public void testCanUnprocessWithUnsafeComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.UNSAFE).build();
    boolean canUnprocess = componentConstraintChecker.canUnprocess(component);
    assertThat(canUnprocess, is(false));
  }

  @Test
  public void testCanUnprocessWithExpiredComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.EXPIRED).build();
    boolean canUnprocess = componentConstraintChecker.canUnprocess(component);
    assertThat(canUnprocess, is(false));
  }

  @Test
  public void testCanUnprocessWithIssuedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.ISSUED).build();
    boolean canUnprocess = componentConstraintChecker.canUnprocess(component);
    assertThat(canUnprocess, is(false));
  }

  @Test
  public void testCanUnprocessWithUsedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.TRANSFUSED).build();
    boolean canUnprocess = componentConstraintChecker.canUnprocess(component);
    assertThat(canUnprocess, is(false));
  }

  @Test
  public void testCanUnprocessWithDiscardedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.DISCARDED).build();
    boolean canUnprocess = componentConstraintChecker.canUnprocess(component);
    assertThat(canUnprocess, is(false));
  }

  @Test
  public void testCanUnprocessWithQuarantinedChildComponent_shouldReturnTrue() {  
    Component parentComponent = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(COMPONENT_ID_2).withStatus(ComponentStatus.QUARANTINED).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findChildComponents(parentComponent)).thenReturn(Arrays.asList(child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(true));
  }

  @Test
  public void testCanUnprocessWithAvailableChildComponent_shouldReturnTrue() {
    Component parentComponent = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(COMPONENT_ID_2).withStatus(ComponentStatus.AVAILABLE).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findChildComponents(parentComponent)).thenReturn(Arrays.asList(child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(true));
  }

  @Test
  public void testCanUnprocessWithUnsafeChildComponent_shouldReturnTrue() {
    Component parentComponent = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(COMPONENT_ID_2).withStatus(ComponentStatus.UNSAFE).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findChildComponents(parentComponent)).thenReturn(Arrays.asList(child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(true));
  }

  @Test
  public void testCanUnprocessWithExpiredChildComponent_shouldReturnTrue() {
    Component parentComponent = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(COMPONENT_ID_2).withStatus(ComponentStatus.EXPIRED).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findChildComponents(parentComponent)).thenReturn(Arrays.asList(child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(true));
  }

  @Test
  public void testCanUnprocessWithIssuedChildComponent_shouldReturnFalse() {
    Component parentComponent = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(COMPONENT_ID_2).withStatus(ComponentStatus.ISSUED).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findChildComponents(parentComponent)).thenReturn(Arrays.asList(child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(false));
  }

  @Test
  public void testCanUnprocessWithUsedChildComponent_shouldReturnFalse() {
    Component parentComponent = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(COMPONENT_ID_2).withStatus(ComponentStatus.TRANSFUSED).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findChildComponents(parentComponent)).thenReturn(Arrays.asList(child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(false));
  }

  @Test
  public void testCanUnprocessWithDiscardedChildComponent_shouldReturnFalse() {
    Component parentComponent = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(COMPONENT_ID_2).withStatus(ComponentStatus.DISCARDED).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findChildComponents(parentComponent)).thenReturn(Arrays.asList(child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(false));
  }
  
  @Test
  public void testCanUnprocessWithWrongLabelledChildComponent_shouldReturnFalse() {
    Component parentComponent = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.PROCESSED).build();
    Component child1 = aComponent().withId(COMPONENT_ID_2).withStatus(ComponentStatus.AVAILABLE).withInventoryStatus(InventoryStatus.IN_STOCK).build();
    Component child2 = aComponent().withId(COMPONENT_ID_3).withStatus(ComponentStatus.AVAILABLE).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findChildComponents(parentComponent)).thenReturn(Arrays.asList(child1, child2));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithDiscardedStatusThatWasInStock_shouldReturnTrue() {
    Component component = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.DISCARDED)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(true));
  }
  
  @Test
  public void testCanUndiscardComponentWithDiscardedStatusThatWasNotInStock_shouldReturnTrue() {
    Component component = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.DISCARDED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(true));
  }
  
  @Test
  public void testCanUndiscardComponentWithQuarantinedStatus_shouldReturnFalse() {
    Component component = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.QUARANTINED).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithAvailableStatus_shouldReturnFalse() {
    Component component = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.AVAILABLE).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithExpiredStatus_shouldReturnFalse() {
    Component component = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.EXPIRED).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithIssuedStatus_shouldReturnFalse() {
    Component component = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.ISSUED).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
 
  @Test
  public void testCanUndiscardComponentWithUsedStatus_shouldReturnFalse() {
    Component component = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.TRANSFUSED).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithUnsafeStatus_shouldReturnFalse() {
    Component component = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.UNSAFE).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithProcessedStatus_shouldReturnFalse() {
    Component component = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.PROCESSED).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
  
  @Test
  public void testCanRecordChildComponentWeight_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withParentComponent(aComponent().withWeight(450).withStatus(ComponentStatus.PROCESSED).build())
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .build();
    boolean canRecordChildComponentWeight = componentConstraintChecker.canRecordChildComponentWeight(component);
    assertThat(canRecordChildComponentWeight, is(true));
  }
  
  @Test
  public void testCanRecordChildComponentWeightThatIsNotAChild_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withParentComponent(null)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .build();
    boolean canRecordChildComponentWeight = componentConstraintChecker.canRecordChildComponentWeight(component);
    assertThat(canRecordChildComponentWeight, is(false));
  }
  
  @Test
  public void testCanRecordChildComponentWeightWithParentWithNoWeight_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withParentComponent(aComponent().withWeight(null).withStatus(ComponentStatus.PROCESSED).build())
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .build();
    boolean canRecordChildComponentWeight = componentConstraintChecker.canRecordChildComponentWeight(component);
    assertThat(canRecordChildComponentWeight, is(false));
  }
  
  @Test
  public void testCanRecordChildComponentWeightWithNonProcessedParent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withParentComponent(aComponent().withWeight(null).withStatus(ComponentStatus.AVAILABLE).build())
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .build();
    boolean canRecordChildComponentWeight = componentConstraintChecker.canRecordChildComponentWeight(component);
    assertThat(canRecordChildComponentWeight, is(false));
  }
  
  @Test
  public void testCanRecordChildComponentWeightThatIsInStock_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withParentComponent(aComponent().withWeight(450).withStatus(ComponentStatus.PROCESSED).build())
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .build();
    boolean canRecordChildComponentWeight = componentConstraintChecker.canRecordChildComponentWeight(component);
    assertThat(canRecordChildComponentWeight, is(false));
  }

  @Test
  public void testCanRecordChildComponentThatIsDiscarded_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.DISCARDED)
        .withParentComponent(aComponent().withWeight(450).withStatus(ComponentStatus.PROCESSED).build())
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .build();
    boolean canRecordChildComponentWeight = componentConstraintChecker.canRecordChildComponentWeight(component);
    assertThat(canRecordChildComponentWeight, is(false));
  }

  @Test
  public void testCanRecordChildComponentThatIsProcessed_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.PROCESSED)
        .withParentComponent(aComponent().withWeight(450).withStatus(ComponentStatus.PROCESSED).build())
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .build();
    boolean canRecordChildComponentWeight = componentConstraintChecker.canRecordChildComponentWeight(component);
    assertThat(canRecordChildComponentWeight, is(false));
  }

  @Test
  public void testCanTransfuseWithIssuedComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.ISSUED)
        .build();
    boolean canTransfuse = componentConstraintChecker.canTransfuse(component);
    assertThat(canTransfuse, is(true));
  }

  @Test
  public void testCanTransfuseWithUnsafeComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.UNSAFE)
        .build();
    boolean canTransfuse = componentConstraintChecker.canTransfuse(component);
    assertThat(canTransfuse, is(false));
  }

  @Test
  public void testCanTransfuseWithAvailableComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .build();
    boolean canTransfuse = componentConstraintChecker.canTransfuse(component);
    assertThat(canTransfuse, is(false));
  }

  @Test
  public void testCanTransfuseWithDiscardedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.DISCARDED)
        .build();
    boolean canTransfuse = componentConstraintChecker.canTransfuse(component);
    assertThat(canTransfuse, is(false));
  }

  @Test
  public void testCanTransfuseWithExpiredComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.EXPIRED)
        .build();
    boolean canTransfuse = componentConstraintChecker.canTransfuse(component);
    assertThat(canTransfuse, is(false));
  }

  @Test
  public void testCanTransfuseWithProcessedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.PROCESSED)
        .build();
    boolean canTransfuse = componentConstraintChecker.canTransfuse(component);
    assertThat(canTransfuse, is(false));
  }

  @Test
  public void testCanTransfuseWithQuarantinedComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.QUARANTINED)
        .build();
    boolean canTransfuse = componentConstraintChecker.canTransfuse(component);
    assertThat(canTransfuse, is(false));
  }
}
