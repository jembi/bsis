package org.jembi.bsis.factory;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.helpers.builders.ComponentBuilder;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
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
    Component component = ComponentBuilder.aComponent()
        .withId(1L)
        .withComponentType(ComponentTypeBuilder.aComponentType()
            .withComponentTypeName("name")
            .withId(1L)
            .withComponentTypeCode("code")
            .build())
        .build(); 
    
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canPrintDiscardLabel", false);
    permissions.put("canPrintPackLabel", true);
    
    // Mock
    when(labellingConstraintChecker.canPrintPackLabel(component)).thenReturn(true);

    // Run test
    LabellingViewModel viewModel = labellingFactory.createViewModel(component);

    // Verify
    Assert.assertNotNull("view model was created", viewModel);
    Assert.assertEquals("id is correct", component.getId(), viewModel.getId());
    Assert.assertEquals("componentName is correct", component.getComponentType().getComponentTypeName(),
        viewModel.getComponentName());
    Assert.assertEquals("componentCode is correct", component.getComponentCode(), viewModel.getComponentCode());
    Assert.assertEquals("permissions are correct", permissions, viewModel.getPermissions());

  }

}
