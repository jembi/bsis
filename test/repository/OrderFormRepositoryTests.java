package repository;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.OrderFormBuilder.anOrderForm;
import static helpers.builders.PackTypeBuilder.aPackType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import helpers.builders.ComponentTypeBuilder;
import helpers.builders.LocationBuilder;
import helpers.builders.OrderFormBuilder;
import helpers.builders.OrderFormItemBuilder;
import model.component.Component;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.location.Location;
import model.order.OrderForm;
import model.order.OrderFormItem;
import model.order.OrderStatus;
import model.packtype.PackType;
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
    donation.setComponents(Arrays.asList(component));
    OrderForm orderForm = OrderFormBuilder.anOrderForm()
        .withDispatchedFrom(loc).withDispatchedTo(loc)
        .withOrderFormItem(item)
        .withComponent(component)
        .build();
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
  
  @Test
  public void testFindOrderFormsNoQueryParams_shouldReturnAllOrders() {
    // Set up
    DateTime now = new DateTime();
    Date aDayAgo = now.minusDays(1).toDate();
    Date twoDaysAgo = now.minusDays(2).toDate();
    Date threeDaysAgo = now.minusDays(3).toDate();
    anOrderForm().withOrderDate(aDayAgo).buildAndPersist(entityManager);
    anOrderForm().withOrderDate(twoDaysAgo).buildAndPersist(entityManager);
    anOrderForm().withOrderDate(threeDaysAgo).buildAndPersist(entityManager);

    // Test
    List<OrderForm> orders = orderFormRepository.findOrderForms(null, null, null, null, null);

    // Verify
    Assert.assertEquals("Found 3 orders", 3, orders.size());
  }

  @Test
  public void testFindOrderFormsByPeriod_shouldReturnOrdersInPeriod() {
    // Set up
    DateTime now = new DateTime();
    Date aDayAgo = now.minusDays(1).toDate();
    Date twoDaysAgo = now.minusDays(2).toDate();
    Date threeDaysAgo = now.minusDays(3).toDate();
    OrderForm orderForm1 = anOrderForm().withOrderDate(aDayAgo).buildAndPersist(entityManager);
    OrderForm orderForm2 = anOrderForm().withOrderDate(twoDaysAgo).buildAndPersist(entityManager);
    anOrderForm().withOrderDate(threeDaysAgo).buildAndPersist(entityManager);

    // Test
    List<OrderForm> orders = orderFormRepository.findOrderForms(twoDaysAgo, aDayAgo, null, null, null);

    // Verify
    Assert.assertEquals("Found 2 orders", 2, orders.size());
    Assert.assertEquals("Verify right order was returned", orderForm1, orders.get(0));
    Assert.assertEquals("Verify right order was returned", orderForm2, orders.get(1));
  }

  @Test
  public void testFindOrderFormsByLocations_shouldReturnRightOrders() {
    // Set up
    Location dispatchedFrom = LocationBuilder.aDistributionSite().withName("site1").build();
    Location dispatchedTo = LocationBuilder.aDistributionSite().withName("site2").build();
    OrderForm orderForm = anOrderForm().withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo).buildAndPersist(entityManager);
    anOrderForm().withDispatchedFrom(dispatchedFrom).buildAndPersist(entityManager);
    anOrderForm().buildAndPersist(entityManager);

    // Test
    List<OrderForm> orders =
        orderFormRepository.findOrderForms(null, null, dispatchedFrom.getId(), dispatchedTo.getId(), null);

    // Verify
    Assert.assertEquals("Found 1 order", 1, orders.size());
    Assert.assertEquals("Verify right order was returned", orderForm, orders.get(0));
  }

  @Test
  public void testFindOrderFormsByPeriodAndLocation_shouldReturnRightOrders() {
    // Set up
    DateTime now = new DateTime();
    Date aDayAgo = now.minusDays(1).toDate();
    Date twoDaysAgo = now.minusDays(2).toDate();
    Date threeDaysAgo = now.minusDays(3).toDate();
    Location dispatchedFrom = LocationBuilder.aDistributionSite().withName("site1").build();
    Location dispatchedTo = LocationBuilder.aDistributionSite().withName("site2").build();
    OrderForm orderForm = anOrderForm()
        .withOrderDate(aDayAgo).withOrderDate(twoDaysAgo)
        .withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo).buildAndPersist(entityManager);
    anOrderForm().withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo).buildAndPersist(entityManager);
    anOrderForm().withOrderDate(threeDaysAgo).buildAndPersist(entityManager);

    // Test
    List<OrderForm> orders =
        orderFormRepository.findOrderForms(twoDaysAgo, aDayAgo, dispatchedFrom.getId(), dispatchedTo.getId(), null);

    // Verify
    Assert.assertEquals("Found 1 order", 1, orders.size());
    Assert.assertEquals("Verify right order was returned", orderForm, orders.get(0));
  }

  @Test
  public void testFindOrderFormsByCreatedStatus_shouldReturnRightOrder() {
    // Set up
    OrderForm createdOrderForm = anOrderForm().withOrderStatus(OrderStatus.CREATED).buildAndPersist(entityManager);
    anOrderForm().withOrderStatus(OrderStatus.DISPATCHED).buildAndPersist(entityManager);

    // Test
    List<OrderForm> orders = orderFormRepository.findOrderForms(null, null, null, null, OrderStatus.CREATED);

    // Verify
    Assert.assertEquals("Found 1 order", 1, orders.size());
    Assert.assertEquals("Verify right order was returned", createdOrderForm, orders.get(0));
  }
  
  @Test
  public void testFindOrderFormsByDispatchedStatus_shouldReturnRightOrder() {
    // Set up
    OrderForm dispatchedOrderForm = anOrderForm().withOrderStatus(OrderStatus.DISPATCHED).buildAndPersist(entityManager);
    anOrderForm().withOrderStatus(OrderStatus.CREATED).buildAndPersist(entityManager);

    // Test
    List<OrderForm> orders = orderFormRepository.findOrderForms(null, null, null, null, OrderStatus.DISPATCHED);

    // Verify
    Assert.assertEquals("Found 1 order", 1, orders.size());
    Assert.assertEquals("Verify right order was returned", dispatchedOrderForm, orders.get(0));
  }

}
