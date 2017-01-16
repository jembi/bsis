package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
public class ComponentVolumeServiceTests extends UnitTestSuite {
  
  @InjectMocks
  private ComponentVolumeService componentVolumeService;
  
  @Test
  public void testCalculateVolumeWithGavityAndWeightSet_shouldReturnCorrectResult() {
    ComponentType componentType = aComponentType()
        .withGravity(1.001)
        .build();

    Component component = aComponent()
        .withComponentType(componentType)
        .withWeight(95)
        .build();

    // Verify
    Integer volume = componentVolumeService.calculateVolume(component); 
    assertThat(volume, is(95));
  }
  
  @Test
  public void testCalculateVolumeWithNULLWieghtAndNULLGravity_shouldReturnNULL() {
    ComponentType componentType = aComponentType()
        .withGravity(null)
        .build();

    Component component = aComponent()
        .withComponentType(componentType)
        .withWeight(null)
        .build();

    // Verify
    Integer volume = componentVolumeService.calculateVolume(component);
    assertThat(volume, is(nullValue()));
  }
  
  @Test
  public void testCalculateVolumeWithNULLWieght_shouldReturnNULL() {
    ComponentType componentType = aComponentType()
        .withGravity(1.001)
        .build();

    Component component = aComponent()
        .withComponentType(componentType)
        .withWeight(null)
        .build();

    // Verify
    Integer volume = componentVolumeService.calculateVolume(component);
    assertThat(volume, is(nullValue()));
  }
  
  @Test
  public void testCalculateVolumeWithNULLGravity_shouldReturnNULL() {
    ComponentType componentType = aComponentType()
        .withGravity(null)
        .build();

    Component component = aComponent()
        .withComponentType(componentType)
        .withWeight(95)
        .build();

    // Verify
    Integer volume = componentVolumeService.calculateVolume(component);
    assertThat(volume, is(nullValue()));
  } 
}