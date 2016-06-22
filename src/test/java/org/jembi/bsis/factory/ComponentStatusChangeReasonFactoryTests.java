package org.jembi.bsis.factory;

import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aComponentStatusChangeReason;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aDiscardReason;

import org.jembi.bsis.factory.ComponentStatusChangeReasonFactory;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.viewmodel.DiscardReasonViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComponentStatusChangeReasonFactoryTests {

  @InjectMocks
  private ComponentStatusChangeReasonFactory componentStatusChangeReasonFactory;

  @Test
  public void testCreateDiscardReasonViewModel_shouldReturnExpectedViewModel() {
    
    ComponentStatusChangeReason entity = aDiscardReason()
        .withId(1L)
        .withStatusChangeReason("reason").build();
    
    DiscardReasonViewModel viewModel = componentStatusChangeReasonFactory.createDiscardReasonViewModel(entity);
    
    Assert.assertNotNull("View Model was created", viewModel);
    Assert.assertEquals("View Model correct", entity.getId(), viewModel.getId());
    Assert.assertEquals("View Model correct", entity.getStatusChangeReason(), viewModel.getReason());
    Assert.assertEquals("View Model correct", entity.getIsDeleted(), viewModel.getIsDeleted());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateDiscardReasonViewModelWithWrongCategory_shouldThrow() {
    ComponentStatusChangeReason entity = aComponentStatusChangeReason()
        .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.ISSUED).build();

    componentStatusChangeReasonFactory.createDiscardReasonViewModel(entity);

  }
}
