package org.jembi.bsis.factory;

import static org.jembi.bsis.helpers.builders.BloodTransportBoxBuilder.aBloodTransportBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jembi.bsis.factory.BloodTransportBoxViewModelFactory;
import org.jembi.bsis.model.componentbatch.BloodTransportBox;
import org.jembi.bsis.viewmodel.BloodTransportBoxViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BloodTransportBoxViewModelFactoryTests {

  @InjectMocks
  BloodTransportBoxViewModelFactory factory;
  
  
  @Test
  public void testCreateBloodTransportBoxViewModel_checkReturn() throws Exception {
    // set up data
    BloodTransportBox box = aBloodTransportBox().withId(1L).withTemperature(0.1).build();
    
    // run test
    BloodTransportBoxViewModel viewModel = factory.createBloodTransportBoxViewModel(box);
    
    // do asserts
    Assert.assertNotNull("View model returned", viewModel);
    Assert.assertEquals("Correct view model returned", 0.1, viewModel.getTemperature(), 0);
    Assert.assertEquals("Correct view model returned", Long.valueOf(1), viewModel.getId());
  }
  
  @Test
  public void testCreateBloodTransportBoxViewModels_twoBoxes() throws Exception {
    // set up data
    Set<BloodTransportBox> boxes = new HashSet<>();
    boxes.add(aBloodTransportBox().withId(1L).withTemperature(0.1).build());
    boxes.add(aBloodTransportBox().withId(2L).withTemperature(0.05).build());
    
    // run test
    List<BloodTransportBoxViewModel> viewModels = factory.createBloodTransportBoxViewModels(boxes);
    
    // do asserts
    Assert.assertNotNull("View models returned", viewModels);
    Assert.assertEquals("Correct number of view models returned", 2, viewModels.size());
  }
  
  @Test
  public void testCreateBloodTransportBoxViewModels_nullSet() throws Exception {
    // run test
    List<BloodTransportBoxViewModel> viewModels = factory.createBloodTransportBoxViewModels(null);
    
    // do asserts
    Assert.assertNotNull("View models returned", viewModels);
  }
}
