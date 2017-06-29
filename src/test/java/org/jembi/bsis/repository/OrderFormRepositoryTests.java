package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.jembi.bsis.helpers.builders.BloodUnitsOrderDTOBuilder.aBloodUnitsOrderDTO;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.OrderFormBuilder.anOrderForm;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.matchers.BloodUnitsOrderDTOMatcher.hasSameStateAsBloodUnitsOrderDTO;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    orderFormRepository.findById(UUID.randomUUID());
  }

  @Test
  public void testFindOrderFormByComponent_shouldReturnCorrectOrderForm() {
    Component component = aComponent().build();
    OrderForm orderForm = anOrderForm()
        .withComponent(component)
        .buildAndPersist(entityManager);

    //Test
    List<OrderForm> returnedOrderForms = orderFormRepository.findByComponent(component.getId());

    //Verify
    Assert.assertNotNull("Order for list was returned", returnedOrderForms);
    Assert.assertEquals("Order form was found", 1, returnedOrderForms.size());
    Assert.assertEquals("Order form was found", orderForm, returnedOrderForms.get(0));
  }

  @Test
  public void testFindOrderFormByComponentNoneExisting_shouldReturnNull() {
    //Test
    List<OrderForm> returnedOrderForms = orderFormRepository.findByComponent(UUID.randomUUID());
    Assert.assertNotNull("Order for list was returned", returnedOrderForms);
    Assert.assertEquals("Order form was not found", 0, returnedOrderForms.size());
  }
  
  public void testFindOrderFormByComponentAssociatedToAnotherForm_shouldReturnNull() {
    Component component = aComponent().build();
    OrderForm orderForm1 = anOrderForm().withComponent(component).withOrderStatus(OrderStatus.DISPATCHED).buildAndPersist(entityManager);
    OrderForm orderForm2 = anOrderForm().withComponent(component).buildAndPersist(entityManager);
    List<OrderForm> returnedOrderForms = orderFormRepository.findByComponent(component.getId());
    Assert.assertNotNull("Order for list was returned", returnedOrderForms);
    Assert.assertEquals("Order forms were found", 2, returnedOrderForms.size());
    Assert.assertEquals("Order form was found", orderForm1, returnedOrderForms.get(0));
    Assert.assertEquals("Order form was found", orderForm2, returnedOrderForms.get(1));
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
    Date today = now.toDate();
    Date aDayAgo = now.minusDays(1).toDate();
    Date twoDaysAgo = now.minusDays(2).toDate();
    Date threeDaysAgo = now.minusDays(3).toDate();
    Location dispatchedFrom = LocationBuilder.aDistributionSite().withName("site1").build();
    Location dispatchedTo = LocationBuilder.aDistributionSite().withName("site2").build();
    OrderForm orderForm = anOrderForm()
        .withOrderDate(twoDaysAgo)
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .buildAndPersist(entityManager);
    anOrderForm().withOrderDate(today).withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo)
        .buildAndPersist(entityManager);
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
  public void testFindBloodUnitsOrderedWithSameOrderTypeAndDifferentLocationsAndDifferentComponentType_shouldReturnRightDtos() {
    // Set up
    Location dispatchedFrom1 = aDistributionSite().withName("Distribution Site #1").buildAndPersist(entityManager);
    Location dispatchedFrom2 = aDistributionSite().withName("Distribution Site #1").buildAndPersist(entityManager);
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().minusDays(2).toDate();
    ComponentType componentType1 = ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager);
    ComponentType componentType2 = ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager);
    
    OrderForm order1 = OrderFormBuilder.anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withDispatchedFrom(dispatchedFrom1)
        .buildAndPersist(entityManager);

    OrderForm order2 = OrderFormBuilder.anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withDispatchedFrom(dispatchedFrom2)
        .buildAndPersist(entityManager);

    OrderForm order3 = OrderFormBuilder.anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withDispatchedFrom(dispatchedFrom1)
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

    OrderFormItemBuilder.anOrderItemForm()
        .withComponentType(componentType1)
        .withNumberOfUnits(2)
        .withOrderForm(order3)
        .buildAndPersist(entityManager);

    // expected data
    BloodUnitsOrderDTO dto1 = aBloodUnitsOrderDTO()
        .withComponentType(componentType1)
        .withDistributionSite(dispatchedFrom1)
        .withOrderType(OrderType.ISSUE)
        .withCount(8)
        .build();
    BloodUnitsOrderDTO dto2 = aBloodUnitsOrderDTO()
        .withComponentType(componentType1)
        .withDistributionSite(dispatchedFrom2)
        .withOrderType(OrderType.ISSUE)
        .withCount(2)
        .build();
    BloodUnitsOrderDTO dto3 = aBloodUnitsOrderDTO()
        .withComponentType(componentType2)
        .withDistributionSite(dispatchedFrom2)
        .withOrderType(OrderType.ISSUE)
        .withCount(7)
        .build();

    // Run test
    List<BloodUnitsOrderDTO> dtos = orderFormRepository.findBloodUnitsOrdered(startDate, endDate);

    // Verify
    assertThat(dtos.size(), is(3));
    assertThat(dtos, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto1)));
    assertThat(dtos, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto2)));
    assertThat(dtos, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto3)));
  }

  @Test
  public void testFindBloodUnitsOrderedWithDifferentOrderTypeAndSameLocationsAndDifferentComponentType_shouldReturnRightDtos() {
    // Set up
    Location dispatchedFrom1 = aDistributionSite().withName("Distribution Site #1").buildAndPersist(entityManager);
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().minusDays(2).toDate();
    ComponentType componentType1 = ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager);
    ComponentType componentType2 = ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager);

    OrderForm order1 = OrderFormBuilder.anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withDispatchedFrom(dispatchedFrom1)
        .buildAndPersist(entityManager);

    OrderForm order2 = OrderFormBuilder.anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.PATIENT_REQUEST)
        .withDispatchedFrom(dispatchedFrom1)
        .buildAndPersist(entityManager);

    OrderForm order3 = OrderFormBuilder.anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withDispatchedFrom(dispatchedFrom1)
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

    OrderFormItemBuilder.anOrderItemForm()
        .withComponentType(componentType1)
        .withNumberOfUnits(2)
        .withOrderForm(order3)
        .buildAndPersist(entityManager);

    // expected data
    BloodUnitsOrderDTO dto1 = aBloodUnitsOrderDTO()
        .withComponentType(componentType1)
        .withDistributionSite(dispatchedFrom1)
        .withOrderType(OrderType.ISSUE)
        .withCount(8)
        .build();
    BloodUnitsOrderDTO dto2 = aBloodUnitsOrderDTO()
        .withComponentType(componentType1)
        .withDistributionSite(dispatchedFrom1)
        .withOrderType(OrderType.PATIENT_REQUEST)
        .withCount(2)
        .build();
    BloodUnitsOrderDTO dto3 = aBloodUnitsOrderDTO()
        .withComponentType(componentType2)
        .withDistributionSite(dispatchedFrom1)
        .withOrderType(OrderType.PATIENT_REQUEST)
        .withCount(7)
        .build();

    // Run test
    List<BloodUnitsOrderDTO> dtos = orderFormRepository.findBloodUnitsOrdered(startDate, endDate);

    // Verify
    assertThat(dtos.size(), is(3));
    assertThat(dtos, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto1)));
    assertThat(dtos, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto2)));
    assertThat(dtos, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto3)));
  }

  @Test
  public void testFindBloodUnitsOrderedWithExcludingFields_shouldntReturnDtos() {
    // Set up
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().minusDays(2).toDate();
    ComponentType componentType = ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager);
    
    // Exclude by order type 'CREATED'
    OrderForm orderExcludedByType = OrderFormBuilder.anOrderForm()
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
        .withOrderDate(new DateTime().minusDays(30).toDate())
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .buildAndPersist(entityManager);   
    OrderFormItemBuilder.anOrderItemForm()
        .withComponentType(componentType)
        .withNumberOfUnits(1)
        .withOrderForm(orderExcludedByDate)
        .buildAndPersist(entityManager);
    
    // Exclude by deleted order
    OrderForm orderExcludedByDeletedOrder = OrderFormBuilder.anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withIsDeleted(true)
        .buildAndPersist(entityManager);   
    OrderFormItemBuilder.anOrderItemForm()
        .withComponentType(componentType)
        .withNumberOfUnits(1)
        .withOrderForm(orderExcludedByDeletedOrder)
        .buildAndPersist(entityManager);

    // Run test
    List<BloodUnitsOrderDTO> dtos = orderFormRepository.findBloodUnitsOrdered(startDate, endDate);

    // Verify
    Assert.assertEquals("Found 0 dtos", 0, dtos.size());
  }

  @Test
  public void testFindBloodUnitsIssuedWithDifferentLocationAndSameOrderTypesAndDifferentComponentTypes_shouldReturnCorrectDTOs() {
    // Set up fixture
    Date startDate = new DateTime().minusDays(10).toDate();
    Date endDate = new DateTime().minusDays(1).toDate();

    ComponentType firstComponentType = aComponentType().buildAndPersist(entityManager);
    ComponentType secondComponentType = aComponentType().buildAndPersist(entityManager);

    Location dispatchedFrom1 = aDistributionSite().withName("Distribution Site #1").buildAndPersist(entityManager);
    Location dispatchedFrom2 = aDistributionSite().withName("Distribution Site #1").buildAndPersist(entityManager);

    // Expected, 2 first components, 1 second component, ISSUE
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom1)
        .buildAndPersist(entityManager);

    // Expected, 1 first component, 1 second component, PATIENT_REQUEST
    anOrderForm()
        .withOrderDate(endDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom2)
        .buildAndPersist(entityManager);

    // Excluded by date
    anOrderForm()
        .withOrderDate(new DateTime().minusDays(90).toDate())
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom1)
        .buildAndPersist(entityManager);

    // Excluded by order status
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.CREATED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom2)
        .buildAndPersist(entityManager);

    // Excluded by order type
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.TRANSFER)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom1)
        .buildAndPersist(entityManager);
    
    // Excluded by deleted order
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom2)
        .withIsDeleted(true)
        .buildAndPersist(entityManager);
    
    // expected data
    BloodUnitsOrderDTO dto1 = aBloodUnitsOrderDTO()
        .withComponentType(firstComponentType)
        .withDistributionSite(dispatchedFrom1)
        .withOrderType(OrderType.ISSUE)
        .withCount(2)
        .build();
    BloodUnitsOrderDTO dto2 = aBloodUnitsOrderDTO()
        .withComponentType(secondComponentType)
        .withDistributionSite(dispatchedFrom1)
        .withOrderType(OrderType.ISSUE)
        .withCount(1)
        .build();
    BloodUnitsOrderDTO dto3 = aBloodUnitsOrderDTO()
        .withComponentType(firstComponentType)
        .withDistributionSite(dispatchedFrom2)
        .withOrderType(OrderType.ISSUE)
        .withCount(1)
        .build();
    BloodUnitsOrderDTO dto4 = aBloodUnitsOrderDTO()
        .withComponentType(secondComponentType)
        .withDistributionSite(dispatchedFrom2)
        .withOrderType(OrderType.ISSUE)
        .withCount(1)
        .build();

    // Exercise SUT
    List<BloodUnitsOrderDTO> returnedDTOs = orderFormRepository.findBloodUnitsIssued(startDate, endDate);

    // Verify
    assertThat(returnedDTOs.size(), is(4));
    assertThat(returnedDTOs, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto1)));
    assertThat(returnedDTOs, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto2)));
    assertThat(returnedDTOs, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto3)));
    assertThat(returnedDTOs, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto4)));
  }

  @Test
  public void testFindBloodUnitsIssuedWithSameLocationDifferentOrderTypesAndDifferentComponentTypes_shouldReturnCorrectDTOs() {
    // Set up fixture
    Date startDate = new DateTime().minusDays(10).toDate();
    Date endDate = new DateTime().minusDays(1).toDate();
    
    ComponentType firstComponentType = aComponentType().buildAndPersist(entityManager);
    ComponentType secondComponentType = aComponentType().buildAndPersist(entityManager);

    Location dispatchedFrom1 = aDistributionSite().withName("Distribution Site #1").buildAndPersist(entityManager);

    // Expected, 2 first components, 1 second component, ISSUE
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom1)
        .buildAndPersist(entityManager);

    // Expected, 1 first component, 1 second component, PATIENT_REQUEST
    anOrderForm()
        .withOrderDate(endDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.PATIENT_REQUEST)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom1)
        .buildAndPersist(entityManager);

    // Excluded by date
    anOrderForm()
        .withOrderDate(new DateTime().minusDays(90).toDate())
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom1)
        .buildAndPersist(entityManager);

    // Excluded by order status
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.CREATED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .buildAndPersist(entityManager);

    // Excluded by order type
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.TRANSFER)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom1)
        .buildAndPersist(entityManager);

    // Excluded by deleted order
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom1)
        .withIsDeleted(true)
        .buildAndPersist(entityManager);

    // Exercise SUT
    List<BloodUnitsOrderDTO> returnedDTOs = orderFormRepository.findBloodUnitsIssued(startDate, endDate);

    // expected data
    BloodUnitsOrderDTO dto1 = aBloodUnitsOrderDTO()
        .withComponentType(firstComponentType)
        .withOrderType(OrderType.ISSUE)
        .withDistributionSite(dispatchedFrom1)
        .withCount(2)
        .build();
    BloodUnitsOrderDTO dto2 = aBloodUnitsOrderDTO()
        .withComponentType(firstComponentType)
        .withOrderType(OrderType.PATIENT_REQUEST)
        .withDistributionSite(dispatchedFrom1)
        .withCount(1)
        .build();
    BloodUnitsOrderDTO dto3 = aBloodUnitsOrderDTO()
        .withComponentType(secondComponentType)
        .withOrderType(OrderType.ISSUE)
        .withDistributionSite(dispatchedFrom1)
        .withCount(1)
        .build();
    BloodUnitsOrderDTO dto4 = aBloodUnitsOrderDTO()
        .withComponentType(secondComponentType)
        .withOrderType(OrderType.PATIENT_REQUEST)
        .withDistributionSite(dispatchedFrom1)
        .withCount(1)
        .build();

    // Verify
    assertThat(returnedDTOs.size(), is(4));
    assertThat(returnedDTOs, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto1)));
    assertThat(returnedDTOs, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto2)));
    assertThat(returnedDTOs, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto3)));
    assertThat(returnedDTOs, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto4)));
  }
  

  @Test
  public void testFindBloodUnitsIssuedWithDifferentLocationAndDifferentOrderTypesAndDifferentComponentTypes_shouldReturnCorrectDTOs() {
    // Set up fixture
    Date startDate = new DateTime().minusDays(10).toDate();
    Date endDate = new DateTime().minusDays(1).toDate();

    ComponentType firstComponentType = aComponentType().buildAndPersist(entityManager);
    ComponentType secondComponentType = aComponentType().buildAndPersist(entityManager);

    Location dispatchedFrom1 = aDistributionSite().withName("Distribution Site #1").buildAndPersist(entityManager);
    Location dispatchedFrom2 = aDistributionSite().withName("Distribution Site #1").buildAndPersist(entityManager);

    // Expected, 2 first components, 1 second component, ISSUE
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom1)
        .buildAndPersist(entityManager);

    // Expected, 1 first component, 1 second component, PATIENT_REQUEST
    anOrderForm()
        .withOrderDate(endDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.PATIENT_REQUEST)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom2)
        .buildAndPersist(entityManager);

    // Excluded by date
    anOrderForm()
        .withOrderDate(new DateTime().minusDays(90).toDate())
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.PATIENT_REQUEST)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom1)
        .buildAndPersist(entityManager);

    // Excluded by order status
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.CREATED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom2)
        .buildAndPersist(entityManager);

    // Excluded by order type
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.TRANSFER)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom1)
        .buildAndPersist(entityManager);
    
    // Excluded by deleted order
    anOrderForm()
        .withOrderDate(startDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withComponent(aComponent().withComponentType(firstComponentType).build())
        .withComponent(aComponent().withComponentType(secondComponentType).build())
        .withDispatchedFrom(dispatchedFrom2)
        .withIsDeleted(true)
        .buildAndPersist(entityManager);
    
    // expected data
    BloodUnitsOrderDTO dto1 = aBloodUnitsOrderDTO()
        .withComponentType(firstComponentType)
        .withDistributionSite(dispatchedFrom1)
        .withOrderType(OrderType.ISSUE)
        .withCount(2)
        .build();
    BloodUnitsOrderDTO dto2 = aBloodUnitsOrderDTO()
        .withComponentType(secondComponentType)
        .withDistributionSite(dispatchedFrom1)
        .withOrderType(OrderType.ISSUE)
        .withCount(1)
        .build();
    BloodUnitsOrderDTO dto3 = aBloodUnitsOrderDTO()
        .withComponentType(firstComponentType)
        .withDistributionSite(dispatchedFrom2)
        .withOrderType(OrderType.PATIENT_REQUEST)
        .withCount(1)
        .build();
    BloodUnitsOrderDTO dto4 = aBloodUnitsOrderDTO()
        .withComponentType(secondComponentType)
        .withDistributionSite(dispatchedFrom2)
        .withOrderType(OrderType.PATIENT_REQUEST)
        .withCount(1)
        .build();

    // Exercise SUT
    List<BloodUnitsOrderDTO> returnedDTOs = orderFormRepository.findBloodUnitsIssued(startDate, endDate);

    // Verify
    assertThat(returnedDTOs.size(), is(4));
    assertThat(returnedDTOs, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto1)));
    assertThat(returnedDTOs, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto2)));
    assertThat(returnedDTOs, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto3)));
    assertThat(returnedDTOs, hasItem(hasSameStateAsBloodUnitsOrderDTO(dto4)));
  }

  @Test
  public void testIsComponentInAnotherOrderFormForExistingForm_returnsFalse() {
    // set up
    Component component = aComponent().build();
    OrderForm orderForm = anOrderForm()
        .withComponent(component)
        .withOrderStatus(OrderStatus.CREATED)
        .buildAndPersist(entityManager);
    
    // run test
    boolean verify = orderFormRepository.isComponentInAnotherOrderForm(orderForm.getId(), component.getId());
    
    // assert
    assertThat(verify, is(false));
  }

  @Test
  public void testIsComponentInAnotherOrderFormForExistingForm_returnsTrue() {
    // set up
    Component component = aComponent().build();
    OrderForm orderForm1 = anOrderForm()
        .withOrderStatus(OrderStatus.CREATED)
        .buildAndPersist(entityManager);
    anOrderForm() // other order form
        .withComponent(component)
        .withOrderStatus(OrderStatus.CREATED)
        .buildAndPersist(entityManager);
    
    // run test
    boolean verify = orderFormRepository.isComponentInAnotherOrderForm(orderForm1.getId(), component.getId());
    
    // assert
    assertThat(verify, is(true));
  }

  @Test
  public void testIsComponentInAnotherOrderFormForExistingDispatchedForm_returnsFalse() {
    // set up
    Component component = aComponent().build();
    OrderForm orderForm1 = anOrderForm()
        .withOrderStatus(OrderStatus.CREATED)
        .buildAndPersist(entityManager);
    anOrderForm() // other order form, but is dispatched
        .withComponent(component)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .buildAndPersist(entityManager);
    
    // run test
    boolean verify = orderFormRepository.isComponentInAnotherOrderForm(orderForm1.getId(), component.getId());
    
    // assert
    assertThat(verify, is(false));
  }

  @Test
  public void testIsComponentInAnotherOrderFormForNewForm_returnsFalse() {
    // set up
    Component component = aComponent().build();
    
    // run test
    boolean verify = orderFormRepository.isComponentInAnotherOrderForm(null, component.getId());
    
    // assert
    assertThat(verify, is(false));
  }

  @Test
  public void testIsComponentInAnotherOrderFormForNewForm_returnsTrue() {
    // set up
    Component component = aComponent().build();
    anOrderForm()  // other order form
      .withComponent(component)
      .withOrderStatus(OrderStatus.CREATED)
      .buildAndPersist(entityManager);
    
    // run test
    boolean verify = orderFormRepository.isComponentInAnotherOrderForm(null, component.getId());
    
    // assert
    assertThat(verify, is(true));
  }
}
