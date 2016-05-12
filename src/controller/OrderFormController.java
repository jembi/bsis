package controller;

import java.util.HashMap;
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
import factory.OrderFormFactory;
import model.order.OrderForm;
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

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
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
