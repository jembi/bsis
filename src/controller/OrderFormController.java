package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import model.location.Location;
import model.order.OrderForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import repository.ComponentTypeRepository;
import repository.LocationRepository;
import repository.OrderFormRepository;
import service.OrderFormCRUDService;
import utils.PermissionConstants;
import backingform.OrderFormBackingForm;
import backingform.OrderFormItemBackingForm;
import backingform.validator.OrderFormBackingFormValidator;
import factory.ComponentTypeFactory;
import factory.LocationViewModelFactory;
import factory.OrderFormFactory;

@RestController
@RequestMapping("orderForms")
public class OrderFormController {

  @Autowired
  private OrderFormFactory orderFormFactory;

  @Autowired
  private OrderFormCRUDService orderFormCRUDService;

  @Autowired
  private OrderFormBackingFormValidator validator;
  
  @Autowired
  private LocationRepository locationRepository;
  
  @Autowired
  private ComponentTypeRepository componentTypeRepository;
  
  @Autowired
  private LocationViewModelFactory locationViewModelFactory;
  
  @Autowired
  private OrderFormRepository orderFormRepository;
  
  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/form")
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> getOrderFormForm() {
    List<Location> usageSites = locationRepository.getUsageSites();
    List<Location> distributionSites = locationRepository.getDistributionSites();

    Map<String, Object> map = new HashMap<>();
    map.put("orderForm", new OrderFormBackingForm());
    map.put("usageSites", locationViewModelFactory.createLocationViewModels(usageSites));
    map.put("distributionSites", locationViewModelFactory.createLocationViewModels(distributionSites));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/items/form")
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> getOrderFormItemForm() {
    Map<String, Object> map = new HashMap<>();
    map.put("orderFormItem", new OrderFormItemBackingForm());
    map.put("componentTypes", componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypes()));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> addOrderForm(@Valid @RequestBody OrderFormBackingForm backingForm) {
    Map<String, Object> map = new HashMap<>();
    OrderForm orderForm = orderFormCRUDService.createOrderForm(backingForm);
    map.put("orderForm", orderFormFactory.createViewModel(orderForm));
    return new ResponseEntity<>(map, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> updateOrderForm(@PathVariable("id") Long orderFormId,
      @Valid @RequestBody OrderFormBackingForm backingForm) {
    
    // Use the id parameter from the path
    backingForm.setId(orderFormId);

    Map<String, Object> map = new HashMap<>();
    map.put("orderForm", orderFormCRUDService.updateOrderForm(backingForm));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> getOrderForm(@PathVariable Long id) {
    OrderForm orderForm = orderFormRepository.findById(id);
    
    Map<String, Object> map = new HashMap<>();
    map.put("orderForm", orderFormFactory.createViewModel(orderForm));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

}
