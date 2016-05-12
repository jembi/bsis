package factory;

import static helpers.builders.LocationBuilder.aLocation;
import static helpers.builders.OrderFormBackingFormBuilder.anOrderFormBackingForm;
import static helpers.builders.OrderFormBuilder.anOrderForm;
import static helpers.builders.OrderFormViewModelBuilder.anOrderFormViewModel;
import static helpers.matchers.OrderFormViewModelMatcher.hasSameStateAsOrderFormViewModel;
import static helpers.matchers.OrderFormMatcher.hasSameStateAsOrderForm;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import backingform.OrderFormBackingForm;
import helpers.builders.LocationBuilder;
import model.location.Location;
import model.order.OrderForm;
import viewmodel.OrderFormViewModel;

@RunWith(MockitoJUnitRunner.class)
public class OrderFormFactoryTests {

  @InjectMocks
  private OrderFormFactory orderFormFactory;

  @Test
  public void testConvertOrderFormBackingFormToOrderFormEntity_shouldReturnExpectedEntity() {
    Location dispatchedFrom = aLocation().withName("LocFrom").build();
    Location dispatchedTo = aLocation().withName("LocTo").build();
    Date orderDate = new Date();

    OrderForm expectedEntity = anOrderForm().withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).build();
    
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo).withOrderDate(orderDate).build();

    OrderForm convertedEntity = orderFormFactory.createEntity(backingForm);
   
    assertThat(convertedEntity, hasSameStateAsOrderForm(expectedEntity));
  }

  @Test
  public void testConvertEntityToOrderFormViewModel_shouldReturnExpectedViewModel() {
    Location dispatchedFrom = LocationBuilder.aLocation().withName("LocFrom").build();
    Location dispatchedTo = LocationBuilder.aLocation().withName("LocTo").build();
    Date orderDate = new Date();

    OrderFormViewModel expectedViewModel = anOrderFormViewModel().withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo).withOrderDate(orderDate).withId(1L).build();

    OrderForm entity = anOrderForm().withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).withId(1L).build();

    OrderFormViewModel convertedViewModel = orderFormFactory.createViewModel(entity);

    assertThat(convertedViewModel, hasSameStateAsOrderFormViewModel(expectedViewModel));
  }


}
