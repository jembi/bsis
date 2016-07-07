package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.mockito.Mockito.when;

import java.util.Arrays;

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
    Component component = aComponent().withStatus(ComponentStatus.QUARANTINED).withParentComponent(aComponent().build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithAvailableComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.AVAILABLE).withParentComponent(aComponent().build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithUnsafeComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.UNSAFE).withParentComponent(aComponent().build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithExpiredComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.EXPIRED).withParentComponent(aComponent().build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithIssuedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.ISSUED).withParentComponent(aComponent().build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithUsedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.USED).withParentComponent(aComponent().build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithDiscardedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.DISCARDED).withParentComponent(aComponent().build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithProcessedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.PROCESSED).withParentComponent(aComponent().build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithSplitComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.SPLIT).withParentComponent(aComponent().build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build()).build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithNoWeightForInitialComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withParentComponent(null)
        .withWeight(null)
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }

  @Test
  public void testCanProcessWithValidWeightForInitialComponent_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withParentComponent(null)
        .withWeight(450)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanProcessWithInvalidWeightForInitialComponent_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withParentComponent(null)
        .withWeight(350)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }
  
  @Test
  public void testCanProcessWithNoCombinations_shouldReturnFalse() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withParentComponent(null)
        .withWeight(450)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .withComponentType(aComponentType().build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(false));
  }
  
  @Test
  public void testCanProcessWithCombinations_shouldReturnTrue() {
    Component component = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withParentComponent(null)
        .withWeight(450)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .withComponentType(aComponentType().withProducedComponentTypeCombination(aComponentTypeCombination().build()).build())
        .build();
    boolean canProcess = componentConstraintChecker.canProcess(component);
    assertThat(canProcess, is(true));
  }

  @Test
  public void testCanRecordWeightWithQuarantinedComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.QUARANTINED).build();
    boolean canRecordWeight = componentConstraintChecker.canRecordWeight(component);
    assertThat(canRecordWeight, is(true));
  }

  @Test
  public void testCanRecordWeightWithAvailableComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.AVAILABLE).build();
    boolean canRecordWeight = componentConstraintChecker.canRecordWeight(component);
    assertThat(canRecordWeight, is(true));
  }

  @Test
  public void testCanRecordWeightWithUnsafeComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.UNSAFE).build();
    boolean canRecordWeight = componentConstraintChecker.canRecordWeight(component);
    assertThat(canRecordWeight, is(true));
  }

  @Test
  public void testCanRecordWeightWithExpiredComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.EXPIRED).build();
    boolean canRecordWeight = componentConstraintChecker.canRecordWeight(component);
    assertThat(canRecordWeight, is(true));
  }

  @Test
  public void testCanRecordWeightWithIssuedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.ISSUED).build();
    boolean canRecordWeight = componentConstraintChecker.canRecordWeight(component);
    assertThat(canRecordWeight, is(false));
  }

  @Test
  public void testCanRecordWeightWithUsedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.USED).build();
    boolean canRecordWeight = componentConstraintChecker.canRecordWeight(component);
    assertThat(canRecordWeight, is(false));
  }

  @Test
  public void testCanRecordWeightWithRecordWeightedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.DISCARDED).build();
    boolean canRecordWeight = componentConstraintChecker.canRecordWeight(component);
    assertThat(canRecordWeight, is(false));
  }

  @Test
  public void testCanRecordWeightWithProcessedComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.PROCESSED).build();
    boolean canRecordWeight = componentConstraintChecker.canRecordWeight(component);
    assertThat(canRecordWeight, is(false));
  }

  @Test
  public void testCanRecordWeightWithSplitComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.SPLIT).build();
    boolean canRecordWeight = componentConstraintChecker.canRecordWeight(component);
    assertThat(canRecordWeight, is(false));
  }

  @Test
  public void testCanRecordWeigthWithInitialComponent_shouldReturnTrue() {
    Component component = aComponent().withStatus(ComponentStatus.AVAILABLE).withParentComponent(null).build();
    boolean canRecordWeight = componentConstraintChecker.canRecordWeight(component);
    assertThat(canRecordWeight, is(true));
  }

  @Test
  public void testCanRecordWeigthWithNotAnInitialComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.AVAILABLE).withParentComponent(aComponent().build()).build();
    boolean canRecordWeight = componentConstraintChecker.canRecordWeight(component);
    assertThat(canRecordWeight, is(false));
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
    Component component = aComponent().withStatus(ComponentStatus.USED).build();
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
  public void testCanUnprocessWithSplitComponent_shouldReturnFalse() {
    Component component = aComponent().withStatus(ComponentStatus.SPLIT).build();
    boolean canUnprocess = componentConstraintChecker.canUnprocess(component);
    assertThat(canUnprocess, is(false));
  }

  @Test
  public void testCanUnprocessWithQuarantinedChildComponent_shouldReturnTrue() {  
    Component parentComponent = aComponent().withId(1L).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(2L).withStatus(ComponentStatus.QUARANTINED).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findComponentsByDonationIdentificationNumber(null)).thenReturn(Arrays.asList(parentComponent, child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(true));
  }

  @Test
  public void testCanUnprocessWithAvailableChildComponent_shouldReturnTrue() {
    Component parentComponent = aComponent().withId(1L).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(2L).withStatus(ComponentStatus.AVAILABLE).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findComponentsByDonationIdentificationNumber(null)).thenReturn(Arrays.asList(parentComponent, child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(true));
  }

  @Test
  public void testCanUnprocessWithUnsafeChildComponent_shouldReturnTrue() {
    Component parentComponent = aComponent().withId(1L).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(2L).withStatus(ComponentStatus.UNSAFE).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findComponentsByDonationIdentificationNumber(null)).thenReturn(Arrays.asList(parentComponent, child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(true));
  }

  @Test
  public void testCanUnprocessWithExpiredChildComponent_shouldReturnTrue() {
    Component parentComponent = aComponent().withId(1L).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(2L).withStatus(ComponentStatus.EXPIRED).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findComponentsByDonationIdentificationNumber(null)).thenReturn(Arrays.asList(parentComponent, child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(true));
  }

  @Test
  public void testCanUnprocessWithIssuedChildComponent_shouldReturnFalse() {
    Component parentComponent = aComponent().withId(1L).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(2L).withStatus(ComponentStatus.ISSUED).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findComponentsByDonationIdentificationNumber(null)).thenReturn(Arrays.asList(parentComponent, child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(false));
  }

  @Test
  public void testCanUnprocessWithUsedChildComponent_shouldReturnFalse() {
    Component parentComponent = aComponent().withId(1L).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(2L).withStatus(ComponentStatus.USED).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findComponentsByDonationIdentificationNumber(null)).thenReturn(Arrays.asList(parentComponent, child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(false));
  }

  @Test
  public void testCanUnprocessWithDiscardedChildComponent_shouldReturnFalse() {
    Component parentComponent = aComponent().withId(1L).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(2L).withStatus(ComponentStatus.DISCARDED).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findComponentsByDonationIdentificationNumber(null)).thenReturn(Arrays.asList(parentComponent, child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(false));
  }

  @Test
  public void testCanUnprocessWithSplitChildComponent_shouldReturnFalse() {
    Component parentComponent = aComponent().withId(1L).withStatus(ComponentStatus.PROCESSED).build();
    Component child = aComponent().withId(2L).withStatus(ComponentStatus.SPLIT).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findComponentsByDonationIdentificationNumber(null)).thenReturn(Arrays.asList(parentComponent, child));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(false));
  }
  
  @Test
  public void testCanUnprocessWithWrongLabelledChildComponent_shouldReturnFalse() {
    Component parentComponent = aComponent().withId(1L).withStatus(ComponentStatus.PROCESSED).build();
    Component child1 = aComponent().withId(2L).withStatus(ComponentStatus.AVAILABLE).withInventoryStatus(InventoryStatus.IN_STOCK).build();
    Component child2 = aComponent().withId(3L).withStatus(ComponentStatus.AVAILABLE).withInventoryStatus(InventoryStatus.NOT_IN_STOCK).build();

    when(componentRepository.findComponentsByDonationIdentificationNumber(null)).thenReturn(Arrays.asList(parentComponent, child1, child2));

    boolean canUnprocess = componentConstraintChecker.canUnprocess(parentComponent);
    assertThat(canUnprocess, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithDiscardedStatusThatWasInStock_shouldReturnTrue() {
    Component component = aComponent()
        .withId(1L)
        .withStatus(ComponentStatus.DISCARDED)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(true));
  }
  
  @Test
  public void testCanUndiscardComponentWithDiscardedStatusThatWasNotInStock_shouldReturnTrue() {
    Component component = aComponent()
        .withId(1L)
        .withStatus(ComponentStatus.DISCARDED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(true));
  }
  
  @Test
  public void testCanUndiscardComponentWithQuarantinedStatus_shouldReturnFalse() {
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.QUARANTINED).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithAvailableStatus_shouldReturnFalse() {
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.AVAILABLE).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithExpiredStatus_shouldReturnFalse() {
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.EXPIRED).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithIssuedStatus_shouldReturnFalse() {
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.ISSUED).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithSplitStatus_shouldReturnFalse() {
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.SPLIT).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithUsedStatus_shouldReturnFalse() {
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.USED).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithUnsafeStatus_shouldReturnFalse() {
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.UNSAFE).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }
  
  @Test
  public void testCanUndiscardComponentWithProcessedStatus_shouldReturnFalse() {
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.PROCESSED).build();
    
    boolean canUndiscard = componentConstraintChecker.canUndiscard(component);
    
    assertThat(canUndiscard, is(false));
  }

}
