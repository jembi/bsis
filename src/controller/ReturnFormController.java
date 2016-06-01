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

import utils.PermissionConstants;
import backingform.ReturnFormBackingForm;
import backingform.validator.ReturnFormBackingFormValidator;
import controllerservice.ReturnFormControllerService;

@RestController
@RequestMapping("returnforms")
public class ReturnFormController {

  @Autowired
  private ReturnFormControllerService returnFormControllerService;
  
  @Autowired
  private ReturnFormBackingFormValidator validator;
  
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> addReturnForm(@Valid @RequestBody ReturnFormBackingForm backingForm) {
    Map<String, Object> map = new HashMap<>();
    map.put("returnForm", returnFormControllerService.createReturnForm(backingForm));
    return new ResponseEntity<>(map, HttpStatus.CREATED);
  }

}
