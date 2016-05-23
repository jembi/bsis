package repository;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.OrderFormBuilder.anOrderForm;
import static helpers.builders.PackTypeBuilder.aPackType;
import helpers.builders.ComponentTypeBuilder;
import helpers.builders.LocationBuilder;
import helpers.builders.OrderFormBuilder;
import helpers.builders.OrderFormComponentBuilder;
import helpers.builders.OrderFormItemBuilder;

import java.util.Arrays;

import javax.persistence.NoResultException;

import model.component.Component;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.location.Location;
import model.order.OrderForm;
import model.order.OrderFormComponent;
import model.order.OrderFormItem;
import model.packtype.PackType;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
  
  @Test
  public void testSaveOrderFormWithItemsAndComponents() throws Exception {
    // set up data
    Location loc = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    ComponentType componentType = ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager);
    OrderFormItem item = OrderFormItemBuilder.anOrderItemForm().withComponentType(componentType).buildAndPersist(entityManager);
    PackType packType = aPackType().withPackType("packType").withComponentType(componentType).buildAndPersist(entityManager);
    Donation donation = aDonation().withDonationIdentificationNumber("DIN123").withPackType(packType).buildAndPersist(entityManager);
    Component component = aComponent().withComponentType(componentType).withDonation(donation).buildAndPersist(entityManager);
    OrderFormComponent orderFormComponent = OrderFormComponentBuilder.anOrderFormComponent().withComponent(component).build();
    donation.setComponents(Arrays.asList(component));
    OrderForm orderForm = OrderFormBuilder.anOrderForm()
        .withDispatchedFrom(loc).withDispatchedTo(loc)
        .withOrderFormItem(item)
        .withComponent(orderFormComponent)
        .build();
    orderFormComponent.setOrderForm(orderForm);
    item.setOrderForm(orderForm);

    // run test
    orderFormRepository.save(orderForm);
    
    OrderForm savedOrderForm = entityManager.find(OrderForm.class, orderForm.getId());
    
    // do checks
    Assert.assertNotNull("Component was persisted", savedOrderForm.getComponents());
    Assert.assertFalse("Component was persisted", savedOrderForm.getComponents().isEmpty());
    Assert.assertNotNull("Component was persisted", savedOrderForm.getComponents().get(0).getId());
  }
  
  @Test
  public void testFindOrderFormWithExistingOrderForm() {
    // Set up
    OrderForm orderForm = anOrderForm().buildAndPersist(entityManager);

    // Test
    OrderForm returnedOrderForm = orderFormRepository.findById(orderForm.getId());
    
    // Verify
    Assert.assertEquals("Order form was found", orderForm, returnedOrderForm);
  }
  
  @Test(expected = NoResultException.class)
  public void testFindOrderFormWithNoExistingOrderForm() {
    // Test
    orderFormRepository.findById(1L);
  }
  
}
