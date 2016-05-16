package repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import helpers.builders.ComponentTypeBuilder;
import helpers.builders.LocationBuilder;
import helpers.builders.OrderFormBuilder;
import helpers.builders.OrderFormItemBuilder;
import model.componenttype.ComponentType;
import model.location.Location;
import model.order.OrderForm;
import model.order.OrderFormItem;
import suites.SecurityContextDependentTestSuite;

public class OrderFormRepositoryTests extends SecurityContextDependentTestSuite {

  @Autowired
  private OrderFormRepository orderFormRepository;

  @Test
  public void testSaveOrderFormWithItems() throws Exception {
    // set up data
    Location loc = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    ComponentType componentType = ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager);
    OrderFormItem item = OrderFormItemBuilder.anOrderItemForm().withComponentType(componentType).build();
    OrderForm orderForm = OrderFormBuilder.anOrderForm().withDispatchedFrom(loc).withDispatchedTo(loc).withOrderFormItem(item).build();
    item.setOrderForm(orderForm);

    // run test
    orderFormRepository.save(orderForm);
    
    // do checks
    Assert.assertNotNull("Item was persisted", orderForm.getItems().get(0).getId());
  }
  
}
