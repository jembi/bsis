package factory;

import static helpers.builders.OrderFormBuilder.anOrderForm;
import static helpers.builders.OrderFormItemBackingFormBuilder.anOrderFormItemBackingForm;
import static helpers.builders.OrderFormItemBuilder.anOrderItemForm;
import helpers.builders.ComponentTypeBuilder;
import model.componenttype.ComponentType;
import model.order.OrderForm;
import model.order.OrderFormItem;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import viewmodel.OrderFormItemViewModel;
import backingform.OrderFormItemBackingForm;

@RunWith(MockitoJUnitRunner.class)
public class OrderFormItemFactoryTests {

  @InjectMocks
  private OrderFormItemFactory orderFormItemFactory;
  
  @Mock
  private ComponentTypeFactory componentTypeFactory;

  @Test
  public void testConvertOrderFormItemBackingFormToOrderFormItemEntity_shouldReturnExpectedEntity() {
    ComponentType componentType = ComponentTypeBuilder.aComponentType().build();
    OrderFormItemBackingForm backingForm = anOrderFormItemBackingForm().withId(1L)
        .withBloodGroup("A+").withNumberOfUnits(2)
        .withComponentType(componentType).build();
    
    OrderForm orderForm = anOrderForm().build();
    OrderFormItem convertedEntity = orderFormItemFactory.createEntity(orderForm, backingForm);
   
    Assert.assertNotNull("Entity was created", convertedEntity);
    Assert.assertEquals("Entity is correct", orderForm, convertedEntity.getOrderForm());
    Assert.assertEquals("Entity is correct", "A", convertedEntity.getBloodAbo());
    Assert.assertEquals("Entity is correct", "+", convertedEntity.getBloodRh());
    Assert.assertEquals("Entity is correct", 2, convertedEntity.getNumberOfUnits());
    Assert.assertEquals("Entity is correct", false, convertedEntity.getIsDeleted());
    Assert.assertEquals("Entity is correct", componentType, convertedEntity.getComponentType());
  }

  @Test
  public void testConvertEntityToOrderFormItemViewModel_shouldReturnExpectedViewModel() {
    ComponentType componentType = ComponentTypeBuilder.aComponentType().build();
    OrderFormItem entity = anOrderItemForm().withBloodAbo("A").withBloodRh("+")
        .withNumberOfUnits(2).withComponentType(componentType).withId(1L).build();

    OrderFormItemViewModel convertedViewModel = orderFormItemFactory.createViewModel(entity);

    Assert.assertNotNull("ViewModel was created", convertedViewModel);
    Assert.assertEquals("ViewModel is correct", Long.valueOf(1), convertedViewModel.getId());
    Assert.assertEquals("ViewModel is correct", "A+", convertedViewModel.getBloodGroup());
    Assert.assertEquals("ViewModel is correct", 2, convertedViewModel.getNumberOfUnits());
  }
}