package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.PackTypeViewFullModelBuilder.aPackTypeViewFullModel;
import static org.jembi.bsis.helpers.matchers.PackTypeFullViewModelMatcher.hasSameStateAsPackTypeViewFullModel;
import static org.mockito.Mockito.when;

import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.helpers.builders.PackTypeBuilder;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.PackTypeViewFullModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PackTypeFactoryTests {

  @InjectMocks
  private PackTypeFactory packTypeFactory;
  @Mock
  private ComponentTypeFactory componentTypeFactory;

  @Test
  public void testConvertEntityToPackTypeFullViewModel_shouldReturnExpectedViewModel() {
    ComponentType componentType = ComponentTypeBuilder.aComponentType().withId(1L).build();
    ComponentTypeViewModel componentTypeViewModel = new ComponentTypeViewModel(componentType);
    PackTypeViewFullModel expectedViewModel = aPackTypeViewFullModel()
        .withComponentType(componentTypeViewModel)
        .withPackType("Single")
        .withPeriodBetweenDonations(90)
        .withTestSampleProduced(true)
        .withCountAsDonation(true)
        .withMinWeight(222)
        .withMaxWeight(999)
        .withLowVolumeWeight(555)
        .withId(1L).build();

    PackType packType = PackTypeBuilder.aPackType()
        .withComponentType(componentType)
        .withPackType("Single")
        .withPeriodBetweenDonations(90)
        .withTestSampleProduced(true)
        .withCountAsDonation(true)
        .withMinWeight(222)
        .withMaxWeight(999)
        .withLowVolumeWeight(555)
        .withId(1L).build();

    // Setup mocks
    when(componentTypeFactory.createViewModel(componentType)).thenReturn(componentTypeViewModel);

    PackTypeViewFullModel convertedViewModel = packTypeFactory.createFullViewModel(packType);

    assertThat(convertedViewModel, hasSameStateAsPackTypeViewFullModel(expectedViewModel));
  }

}
