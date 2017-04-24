package org.jembi.bsis.factory;

import static org.jembi.bsis.helpers.builders.ComponentBatchBuilder.aComponentBatch;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.service.LabellingConstraintChecker;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.LabellingViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class LabellingFactoryTests extends UnitTestSuite {

  @InjectMocks
  private LabellingFactory labellingFactory;
  @Mock
  private LabellingConstraintChecker labellingConstraintChecker;

  @Test
  public void testCreateLabellingViewModel_shouldReturnViewModelWithTheCorrectState() {

    // Setup
    UUID componentTypeId = UUID.randomUUID();
    Component component = aComponent()
        .withId(UUID.randomUUID())
        .withComponentBatch(aComponentBatch()
            .withId(UUID.randomUUID())
            .build())
        .withComponentType(aComponentType()
            .withComponentTypeName("name")
            .withId(componentTypeId)
            .withComponentTypeCode("code")
            .build())
        .build(); 
    
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canPrintDiscardLabel", true);
    permissions.put("canPrintPackLabel", true);

    // Mock
    when(labellingConstraintChecker.canPrintPackLabel(component)).thenReturn(true);
    when(labellingConstraintChecker.canPrintDiscardLabel(component)).thenReturn(true);

    // Run test
    LabellingViewModel viewModel = labellingFactory.createViewModel(component);

    // Verify
    Assert.assertNotNull("view model was created", viewModel);
    Assert.assertEquals("id is correct", component.getId(), viewModel.getId());
    Assert.assertEquals("componentName is correct", component.getComponentType().getComponentTypeName(),
        viewModel.getComponentName());
    Assert.assertTrue("component is assigned to a batch", viewModel.getHasComponentBatch());
    Assert.assertEquals("componentCode is correct", component.getComponentCode(), viewModel.getComponentCode());
    Assert.assertEquals("permissions are correct", permissions, viewModel.getPermissions());
  }
}
