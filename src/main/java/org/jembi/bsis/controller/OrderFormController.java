package org.jembi.bsis.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.backingform.OrderFormBackingForm;
import org.jembi.bsis.backingform.OrderFormItemBackingForm;
import org.jembi.bsis.backingform.validator.OrderFormBackingFormValidator;
import org.jembi.bsis.controllerservice.OrderFormControllerService;
import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orderforms")
public class OrderFormController {

  @Autowired
  private OrderFormBackingFormValidator validator;
  
  @Autowired
  private OrderFormControllerService orderFormControllerService;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/form")
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> getOrderFormForm() {
    Map<String, Object> map = new HashMap<>();
    map.put("orderForm", new OrderFormBackingForm());
    map.put("usageSites", orderFormControllerService.getUsageSites());
    map.put("distributionSites", orderFormControllerService.getDistributionSites());
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/items/form")
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> getOrderFormItemForm() {
    Map<String, Object> map = new HashMap<>();
    map.put("orderFormItem", new OrderFormItemBackingForm());
    map.put("componentTypes", orderFormControllerService.getAllComponentTypes());
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> addOrderForm(@Valid @RequestBody OrderFormBackingForm backingForm) {
    Map<String, Object> map = new HashMap<>();
    map.put("orderForm", orderFormControllerService.createOrderForm(backingForm));
    return new ResponseEntity<>(map, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> updateOrderForm(@PathVariable("id") UUID orderFormId,
      @Valid @RequestBody OrderFormBackingForm backingForm) {
    
    // Use the id parameter from the path
    backingForm.setId(orderFormId);

    Map<String, Object> map = new HashMap<>();
    map.put("orderForm", orderFormControllerService.updateOrderForm(backingForm));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> getOrderForm(@PathVariable UUID id) {
    Map<String, Object> map = new HashMap<>();
    map.put("orderForm", orderFormControllerService.findOrderForm(id));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> findComponentBatches(
      @RequestParam(value = "orderDateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date orderDateFrom,
      @RequestParam(value = "orderDateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date orderDateTo,
      @RequestParam(value = "dispatchedFromId", required = false) UUID dispatchedFromId,
      @RequestParam(value = "dispatchedToId", required = false) UUID dispatchedToId,
      @RequestParam(value = "type", required = false) OrderType type,
      @RequestParam(value = "status", required = false) OrderStatus status) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("orderForms", orderFormControllerService.findOrderForms(orderDateFrom, orderDateTo, dispatchedFromId, dispatchedToId, type, status));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }
  
  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_ORDER_FORM + "')")
  public void deleteOrderForm(@PathVariable UUID id) {
    orderFormControllerService.deleteOrderForm(id);
  }
}
