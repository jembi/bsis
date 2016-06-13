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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import backingform.ReturnFormBackingForm;
import backingform.validator.ReturnFormBackingFormValidator;
import controllerservice.ReturnFormControllerService;
import factory.LocationViewModelFactory;
import model.location.Location;
import repository.LocationRepository;
import utils.PermissionConstants;

@RestController
@RequestMapping("returnforms")
public class ReturnFormController {

  @Autowired
  private ReturnFormControllerService returnFormControllerService;
  
  @Autowired
  private ReturnFormBackingFormValidator validator;
  
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
    map.put("returnForm", new ReturnFormBackingForm());
    map.put("usageSites", locationViewModelFactory.createLocationViewModels(usageSites));
    map.put("distributionSites", locationViewModelFactory.createLocationViewModels(distributionSites));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> addReturnForm(@Valid @RequestBody ReturnFormBackingForm backingForm) {
    Map<String, Object> map = new HashMap<>();
    map.put("returnForm", returnFormControllerService.createReturnForm(backingForm));
    return new ResponseEntity<>(map, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> getReturnForm(@PathVariable Long id) {
    Map<String, Object> map = new HashMap<>();
    map.put("returnForm", returnFormControllerService.findById(id));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> updateReturnForm(@PathVariable("id") Long id,
      @Valid @RequestBody ReturnFormBackingForm backingForm) {

    // Use the id parameter from the path
    backingForm.setId(id);

    Map<String, Object> map = new HashMap<>();
    map.put("returnForm", returnFormControllerService.updateReturnForm(backingForm));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

}
