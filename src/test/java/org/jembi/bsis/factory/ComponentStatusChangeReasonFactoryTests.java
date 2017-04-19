package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aComponentStatusChangeReason;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aDiscardReason;
import static org.jembi.bsis.helpers.builders.DiscardReasonBackingFormBuilder.aDiscardReasonBackingForm;
import static org.jembi.bsis.helpers.matchers.ComponentStatusChangeReasonMatcher.hasSameStateAsComponentStatusChangeReason;

import java.util.UUID;

import org.jembi.bsis.backingform.DiscardReasonBackingForm;

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
        .withId(UUID.randomUUID())
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

  @Test
  public void testCreateDiscardReasonEntity_returnEntity() {
    UUID discardReason = UUID.randomUUID();
    DiscardReasonBackingForm form = aDiscardReasonBackingForm()
        .withId(discardReason)
        .withReason("REASON")
        .thatIsNotDeleted()
        .build();
    ComponentStatusChangeReason expectedEntity = aDiscardReason()
        .withId(discardReason)
        .withStatusChangeReason("REASON")
        .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.DISCARDED)
        .thatIsNotDeleted()
        .build();
    ComponentStatusChangeReason entity = componentStatusChangeReasonFactory.createDiscardReasonEntity(form);
    assertThat(entity, hasSameStateAsComponentStatusChangeReason(expectedEntity));
  }
}
