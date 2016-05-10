package controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import backingform.ComponentBatchBackingForm;
import backingform.validator.ComponentBatchBackingFormValidator;
import factory.ComponentBatchViewModelFactory;
import factory.DonationBatchViewModelFactory;
import factory.LocationViewModelFactory;
import model.componentbatch.ComponentBatch;
import model.donationbatch.DonationBatch;
import model.location.Location;
import model.location.LocationType;
import repository.DonationBatchRepository;
import repository.LocationRepository;
import service.ComponentBatchCRUDService;
import service.FormFieldAccessorService;
import utils.PermissionConstants;
import viewmodel.ComponentBatchViewModel;

@RestController
@RequestMapping("/componentbatches")
public class ComponentBatchController {
  
  @Autowired
  private FormFieldAccessorService formFieldAccessorService;
  
  @Autowired
  ComponentBatchBackingFormValidator componentBatchBackingFormValidator;
  
  @Autowired
  private ComponentBatchCRUDService componentBatchCRUDService;
  
  @Autowired
  private ComponentBatchViewModelFactory componentBatchViewModelFactory;
  
  @Autowired
  private DonationBatchRepository donationBatchRepository;
  
  @Autowired
  private LocationRepository locationRepository;
  
  @Autowired
  private DonationBatchViewModelFactory donationBatchViewModelFactory;
  
  @Autowired
  private LocationViewModelFactory locationViewModelFactory;

  public ComponentBatchController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(componentBatchBackingFormValidator);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT_INFORMATION + "')")
  public Map<String, Object> addComponentBatchFormGenerator(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    
    List<DonationBatch> donationBatches = donationBatchRepository.findUnassignedDonationBatchesForComponentBatch();
    List<Location> locations = locationRepository.getLocationsByType(LocationType.PROCESSING_SITE);
    
    map.put("addComponentBatchForm", new ComponentBatchBackingForm());
    map.put("donationBatches", donationBatchViewModelFactory.createDonationBatchBasicViewModels(donationBatches));
    map.put("componentBatchFields", formFieldAccessorService.getFormFieldsForForm("ComponentBatch"));
    map.put("bloodTransportBoxFields", formFieldAccessorService.getFormFieldsForForm("BloodTransportBox"));
    map.put("processingSites", locationViewModelFactory.createLocationViewModels(locations));

    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_COMPONENT_BATCH + "')")
  public ResponseEntity<ComponentBatchViewModel> addComponentBatch(@RequestBody @Valid ComponentBatchBackingForm form) {
    ComponentBatch componentBatch = form.getComponentBatch();
    componentBatch = componentBatchCRUDService.createComponentBatch(componentBatch);
    return new ResponseEntity<>(
        componentBatchViewModelFactory.createComponentBatchFullViewModel(componentBatch), HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_COMPONENT_BATCH + "')")
  public ResponseEntity<ComponentBatchViewModel> updateComponentBatch(@PathVariable Long id, @RequestBody @Valid ComponentBatchBackingForm form) {
    ComponentBatch componentBatch = form.getComponentBatch();
    componentBatch = componentBatchCRUDService.updateComponentBatch(componentBatch);
    return new ResponseEntity<>(
        componentBatchViewModelFactory.createComponentBatchFullViewModel(componentBatch), HttpStatus.OK);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_COMPONENT_BATCH + "')")
  public void deleteComponentBatch(@PathVariable Long id) {
    componentBatchCRUDService.deleteComponentBatch(id);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT_BATCH + "')")
  public ResponseEntity<ComponentBatchViewModel> getComponentBatch(@PathVariable Long id) {
    ComponentBatch componentBatch = componentBatchCRUDService.getComponentBatchById(id);
    return new ResponseEntity<>(
        componentBatchViewModelFactory.createComponentBatchFullViewModel(componentBatch), HttpStatus.OK);
  }
  
  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT_BATCH + "')")
  public ResponseEntity<Map<String, Object>> findComponentBatches(
      @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startCollectionDate,
      @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endCollectionDate) {
    Map<String, Object> map = new HashMap<String, Object>();
    List<ComponentBatch> componentBatches = componentBatchCRUDService.findComponentBatches(startCollectionDate, endCollectionDate);
    map.put("componentBatches", componentBatchViewModelFactory.createComponentBatchBasicViewModels(componentBatches));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

  }

}