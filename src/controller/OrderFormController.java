package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import backingform.OrderFormBackingForm;
import backingform.validator.OrderFormBackingFormValidator;
import factory.LocationViewModelFactory;
import factory.OrderFormFactory;
import model.location.Location;
import model.order.OrderForm;
import repository.LocationRepository;
import service.OrderFormCRUDService;
import utils.PermissionConstants;

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
  private LocationViewModelFactory locationViewModelFactory;

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

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> addOrderForm(@Valid @RequestBody OrderFormBackingForm backingForm) {
    Map<String, Object> map = new HashMap<String, Object>();
    OrderForm orderForm = orderFormCRUDService.createOrderForm(backingForm);
    map.put("orderForm", orderFormFactory.createViewModel(orderForm));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
  }

}
