package org.jembi.bsis.repository;

import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.OrderFormBuilder.anOrderForm;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.jembi.bsis.dto.BloodUnitsOrderDTO;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.builders.OrderFormBuilder;
import org.jembi.bsis.helpers.builders.OrderFormItemBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderFormItem;
import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    List<OrderForm> orders = orderFormRepository.findOrderForms(null, null, null, null, null, null);

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
    List<OrderForm> orders = orderFormRepository.findOrderForms(twoDaysAgo, aDayAgo, null, null, null, null);

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
        orderFormRepository.findOrderForms(null, null, dispatchedFrom.getId(), dispatchedTo.getId(), null, null);

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
 orderFormRepository.findOrderForms(twoDaysAgo, aDayAgo, dispatchedFrom.getId(),
        dispatchedTo.getId(), null, null);

    // Verify
    Assert.assertEquals("Found 1 order", 1, orders.size());
    Assert.assertEquals("Verify right order was returned", orderForm, orders.get(0));
  }

  @Test
  public void testFindOrderFormsByIssueType_shouldReturnRightOrder() {
    // Set up
    OrderForm createdOrderForm = anOrderForm().withOrderType(OrderType.ISSUE).buildAndPersist(entityManager);
    anOrderForm().withOrderType(OrderType.TRANSFER).buildAndPersist(entityManager);

    // Test
    List<OrderForm> orders = orderFormRepository.findOrderForms(null, null, null, null, OrderType.ISSUE, null);

    // Verify
    Assert.assertEquals("Found 1 order", 1, orders.size());
    Assert.assertEquals("Verify right order was returned", createdOrderForm, orders.get(0));
  }

  @Test
  public void testFindOrderFormsByTransferType_shouldReturnRightOrder() {
    // Set up
    OrderForm dispatchedOrderForm = anOrderForm().withOrderType(OrderType.TRANSFER).buildAndPersist(entityManager);
    anOrderForm().withOrderType(OrderType.ISSUE).buildAndPersist(entityManager);

    // Test
    List<OrderForm> orders = orderFormRepository.findOrderForms(null, null, null, null, OrderType.TRANSFER, null);

    // Verify
    Assert.assertEquals("Found 1 order", 1, orders.size());
    Assert.assertEquals("Verify right order was returned", dispatchedOrderForm, orders.get(0));
  }

  @Test
  public void testFindOrderFormsByCreatedStatus_shouldReturnRightOrder() {
    // Set up
    OrderForm createdOrderForm = anOrderForm().withOrderStatus(OrderStatus.CREATED).buildAndPersist(entityManager);
    anOrderForm().withOrderStatus(OrderStatus.DISPATCHED).buildAndPersist(entityManager);

    // Test
    List<OrderForm> orders = orderFormRepository.findOrderForms(null, null, null, null, null, OrderStatus.CREATED);

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
    List<OrderForm> orders = orderFormRepository.findOrderForms(null, null, null, null, null, OrderStatus.DISPATCHED);

    // Verify
    Assert.assertEquals("Found 1 order", 1, orders.size());
    Assert.assertEquals("Verify right order was returned", dispatchedOrderForm, orders.get(0));
  }

  @Test
  public void testFindBloodUnitsOrdered_shouldReturnRightDtos() {
    // Set up
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().minusDays(2).toDate();
    Location dispatchedFrom1 = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    Location dispatchedFrom2 = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    ComponentType componentType1 = ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager);
    ComponentType componentType2 = ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager);
    
    OrderForm order1 = OrderFormBuilder.anOrderForm()
        .withDispatchedFrom(dispatchedFrom1)
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .buildAndPersist(entityManager);
    
    OrderForm order2 = OrderFormBuilder.anOrderForm()
        .withDispatchedFrom(dispatchedFrom2)
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .buildAndPersist(entityManager);
    
    OrderFormItemBuilder.anOrderItemForm()
        .withComponentType(componentType1)
        .withNumberOfUnits(1)
        .withOrderForm(order1)
        .buildAndPersist(entityManager);
    
    OrderFormItemBuilder.anOrderItemForm()
        .withComponentType(componentType1)
        .withNumberOfUnits(5)
        .withOrderForm(order1)
        .buildAndPersist(entityManager);
    
    OrderFormItemBuilder.anOrderItemForm()
        .withComponentType(componentType1)
        .withNumberOfUnits(2)
        .withOrderForm(order2)
        .buildAndPersist(entityManager);

    OrderFormItemBuilder.anOrderItemForm()
        .withComponentType(componentType2)
        .withNumberOfUnits(7)
        .withOrderForm(order2)
        .buildAndPersist(entityManager);

    // Run test
    List<BloodUnitsOrderDTO> dtos = orderFormRepository.findBloodUnitsOrdered(startDate, endDate);

    // Verify
    Assert.assertEquals("Found 3 dtos", 3, dtos.size());
    Assert.assertEquals("Correct count", 6, dtos.get(0).getCount());
    Assert.assertEquals("Correct componentType", componentType1, dtos.get(0).getComponentType());
    Assert.assertEquals("Correct location", dispatchedFrom1, dtos.get(0).getLocation());
    Assert.assertEquals("Correct count", 2, dtos.get(1).getCount());
    Assert.assertEquals("Correct componentType", componentType1, dtos.get(1).getComponentType());
    Assert.assertEquals("Correct location", dispatchedFrom2, dtos.get(1).getLocation());
    Assert.assertEquals("Correct count", 7, dtos.get(2).getCount());
    Assert.assertEquals("Correct componentType", componentType2, dtos.get(2).getComponentType());
    Assert.assertEquals("Correct location", dispatchedFrom2, dtos.get(2).getLocation());
  }
  
  @Test
  public void testFindBloodUnitsOrderedWithExcludingFields_shouldntReturnDtos() {
    // Set up
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().minusDays(2).toDate();
    Location dispatchedFrom = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    ComponentType componentType = ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager);
    
    // Exclude by order type 'CREATED'
    OrderForm orderExcludedByType = OrderFormBuilder.anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.CREATED)
        .withOrderType(OrderType.ISSUE)
        .buildAndPersist(entityManager);   
    OrderFormItemBuilder.anOrderItemForm()
        .withComponentType(componentType)
        .withNumberOfUnits(1)
        .withOrderForm(orderExcludedByType)
        .buildAndPersist(entityManager);
    
    // Exclude by order status 'TRANSFER'
    OrderForm orderExcludedByStatus = OrderFormBuilder.anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.TRANSFER)
        .buildAndPersist(entityManager);   
    OrderFormItemBuilder.anOrderItemForm()
        .withComponentType(componentType)
        .withNumberOfUnits(1)
        .withOrderForm(orderExcludedByStatus)
        .buildAndPersist(entityManager);
    
    // Exclude by date
    OrderForm orderExcludedByDate = OrderFormBuilder.anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withOrderDate(new DateTime().minusDays(30).toDate())
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .buildAndPersist(entityManager);   
    OrderFormItemBuilder.anOrderItemForm()
        .withComponentType(componentType)
        .withNumberOfUnits(1)
        .withOrderForm(orderExcludedByDate)
        .buildAndPersist(entityManager);

    // Run test
    List<BloodUnitsOrderDTO> dtos = orderFormRepository.findBloodUnitsOrdered(startDate, endDate);

    // Verify
    Assert.assertEquals("Found 0 dtos", 0, dtos.size());
  }

  public void testFindBloodUnitsIssued_shouldReturnCorrectDTOs() {
    // Set up fixture
    Date startDate = new DateTime().minusDays(10).toDate();
    Date endDate = new DateTime().minusDays(1).toDate();
    
    ComponentType firstComponentType = aComponentType().buildAndPersist(entityManager);
    ComponentType secondComponentType = aComponentType().buildAndPersist(entityManager);
    
    Location firstLocation = aDistributionSite().buildAndPersist(entityManager);
    Location secondLocation = aDistributionSite().buildAndPersist(entityManager);
    
    // Expected, first location, 2 first components, 1 second component
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(firstLocation)
        .buildAndPersist(entityManager);

    // Expected, first location, 1 first component, 1 second component
    anOrderForm()
        .withOrderDate(endDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(firstLocation)
        .buildAndPersist(entityManager);

    // Expected, second location, 1 first component, 0 second components
    anOrderForm()
        .withOrderDate(endDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withDispatchedFrom(secondLocation)
        .buildAndPersist(entityManager);

    // Excluded by date
    anOrderForm()
        .withOrderDate(new DateTime().minusDays(90).toDate())
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(firstLocation)
        .buildAndPersist(entityManager);

    // Excluded by order status
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.CREATED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(firstLocation)
        .buildAndPersist(entityManager);

    // Excluded by order type
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.TRANSFER)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(firstLocation)
        .buildAndPersist(entityManager);
    
    // Exercise SUT
    List<BloodUnitsOrderDTO> returnedDTOs = orderFormRepository.findBloodUnitsIssued(startDate, endDate);
    
    // Verify
    Assert.assertEquals("Found 2 dtos", 3, returnedDTOs.size());
    // First location, first component type
    Assert.assertEquals("Correct count", 3, returnedDTOs.get(0).getCount());
    Assert.assertEquals("Correct componentType", firstComponentType, returnedDTOs.get(0).getComponentType());
    Assert.assertEquals("Correct location", firstLocation, returnedDTOs.get(0).getLocation());
    // First location, second component type
    Assert.assertEquals("Correct count", 2, returnedDTOs.get(1).getCount());
    Assert.assertEquals("Correct componentType", secondComponentType, returnedDTOs.get(1).getComponentType());
    Assert.assertEquals("Correct location", firstLocation, returnedDTOs.get(1).getLocation());
    // Second location, first component type
    Assert.assertEquals("Correct count", 1, returnedDTOs.get(2).getCount());
    Assert.assertEquals("Correct componentType", firstComponentType, returnedDTOs.get(2).getComponentType());
    Assert.assertEquals("Correct location", secondLocation, returnedDTOs.get(2).getLocation());
  }

}
