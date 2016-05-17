package factory;

import static helpers.builders.LocationBuilder.aDistributionSite;
import static helpers.builders.OrderFormBackingFormBuilder.anOrderFormBackingForm;
import static helpers.builders.OrderFormBuilder.anOrderForm;
import static helpers.builders.OrderFormViewModelBuilder.anOrderFormViewModel;
import static helpers.matchers.OrderFormMatcher.hasSameStateAsOrderForm;
import static helpers.matchers.OrderFormViewModelMatcher.hasSameStateAsOrderFormViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import backingform.LocationBackingForm;
import backingform.OrderFormBackingForm;
import model.location.Location;
import model.order.OrderForm;
import repository.LocationRepository;
import viewmodel.LocationViewModel;
import viewmodel.OrderFormViewModel;

@RunWith(MockitoJUnitRunner.class)
public class OrderFormFactoryTests {

  @InjectMocks
  private OrderFormFactory orderFormFactory;

  @Mock
  private LocationRepository locationRepository;

  @Test
  public void testConvertOrderFormBackingFormToOrderFormEntity_shouldReturnExpectedEntity() {
    Location dispatchedFrom = aDistributionSite().withName("LocFrom").withId(1l).build();
    Location dispatchedTo = aDistributionSite().withName("LocTo").withId(2l).build();
    Date orderDate = new Date();

    OrderForm expectedEntity = anOrderForm().withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).build();
    
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withDispatchedFrom(new LocationBackingForm(dispatchedFrom))
        .withDispatchedTo(new LocationBackingForm(dispatchedTo)).withOrderDate(orderDate).build();

    // Setup mock
    when(locationRepository.getLocation(1l)).thenReturn(dispatchedFrom);
    when(locationRepository.getLocation(2l)).thenReturn(dispatchedTo);

    OrderForm convertedEntity = orderFormFactory.createEntity(backingForm);
   
    assertThat(convertedEntity, hasSameStateAsOrderForm(expectedEntity));
  }

  @Test
  public void testConvertEntityToOrderFormViewModel_shouldReturnExpectedViewModel() {
    Location dispatchedFrom = aDistributionSite().withName("LocFrom").build();
    Location dispatchedTo = aDistributionSite().withName("LocTo").build();
    Date orderDate = new Date();

    OrderFormViewModel expectedViewModel = anOrderFormViewModel().withDispatchedFrom(new LocationViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationViewModel(dispatchedTo)).withOrderDate(orderDate).withId(1L).build();

    OrderForm entity = anOrderForm().withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).withId(1L).build();

    OrderFormViewModel convertedViewModel = orderFormFactory.createViewModel(entity);

    assertThat(convertedViewModel, hasSameStateAsOrderFormViewModel(expectedViewModel));
  }


}
