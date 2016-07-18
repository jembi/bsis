package org.jembi.bsis.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.validator.LocationBackingFormValidator;
import org.jembi.bsis.controllerservice.LocationControllerService;
import org.jembi.bsis.model.location.LocationType;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("locations")
public class LocationsController {

  @Autowired
  private LocationControllerService locationControllerService;

  @Autowired
  private LocationBackingFormValidator locationBackingFormValidator;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(locationBackingFormValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
  public Map<String, Object> getAllLocations(
      HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("allLocations", locationControllerService.getAllLocations());
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
  public ResponseEntity<LocationFullViewModel> addLocation(
      @RequestBody @Valid LocationBackingForm form) {
    return new ResponseEntity<>(locationControllerService.addLocation(form), HttpStatus.CREATED);

  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
  public ResponseEntity<Map<String, Object>> updateLocation(@PathVariable long id,
      @RequestBody @Valid LocationBackingForm form) {
    Map<String, Object> map = new HashMap<String, Object>();
    form.setId(id);
    map.put("location", locationControllerService.updateLocation(form));
    return new ResponseEntity<>(map, HttpStatus.OK);

  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
  public ResponseEntity<Map<String, Object>> getLocationById(@PathVariable long id) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("location", locationControllerService.getLocationById(id));
    return new ResponseEntity<>(map, HttpStatus.OK);

  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteLocation(@PathVariable long id) {
    locationControllerService.deleteLocation(id);
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/form")
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
  public ResponseEntity<Map<String, Object>> getSearchForm() {
    Map<String, Object> map = new HashMap<>();
    map.put("locationType", Arrays.asList(LocationType.values()));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
  public ResponseEntity<Map<String, Object>> search (
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "includeSimilarResults", required = false) boolean includeSimilarResults,
      @RequestParam(value = "locationType", required = false) LocationType locationType) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("locations", locationControllerService.findLocations(name, includeSimilarResults, locationType));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }
}
