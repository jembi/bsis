package org.jembi.bsis.template.labelling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.DiscardLabelTemplateObjectBuilder.aDiscardLabelTemplateObject;
import static org.jembi.bsis.helpers.matchers.DiscardLabelTemplateObjectMatcher.hasSameStateAsDiscardLabelTemplateObject;
import static org.mockito.Mockito.when;

import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class TemplateObjectFactoryTests extends UnitTestSuite {

  @Spy
  @InjectMocks
  private TemplateObjectFactory templateObjectFactory;

  @Mock
  private GeneralConfigAccessorService generalConfigAccessorService;

  @Test
  public void testCreateDiscardLabelTemplateObject_shouldReturnADiscardLabelTemplateObject() {
    // Set up fixture
    String componentCode = "1001";
    String serviceInfoLine1 = "service info line 1";
    String serviceInfoLine2 = "service info line 2";

    Component component = aComponent()
        .withComponentCode(componentCode)
        .build();

    DiscardLabelTemplateObject expectedResult = aDiscardLabelTemplateObject()
        .withComponentCode(componentCode)
        .withServiceInfoLine1(serviceInfoLine1)
        .withServiceInfoLine2(serviceInfoLine2)
        .build();

    when(generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_1))
      .thenReturn(serviceInfoLine1);

    when(generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_2))
      .thenReturn(serviceInfoLine2);

    // Exercise SUT
    DiscardLabelTemplateObject actualResult = templateObjectFactory.createDiscardLabelTemplateObject(component);

    // Verify
    assertThat(actualResult, hasSameStateAsDiscardLabelTemplateObject(expectedResult));
  }
}
