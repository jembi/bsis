package controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backingform.RecordComponentBackingForm;
import controllerservice.ComponentControllerService;
import factory.ComponentTypeFactory;
import factory.ComponentViewModelFactory;
import model.component.Component;
import model.component.ComponentStatus;
import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeReasonCategory;
import repository.ComponentRepository;
import repository.ComponentStatusChangeReasonRepository;
import repository.ComponentTypeRepository;
import service.ComponentCRUDService;
import utils.PermissionConstants;
import viewmodel.ComponentViewModel;

@RestController
@RequestMapping("components")
public class ComponentController {

  @Autowired
  private ComponentRepository componentRepository;

  @Autowired
  private ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;

  @Autowired
  private ComponentTypeRepository componentTypeRepository;
  
  @Autowired
  private ComponentCRUDService componentCRUDService;

  @Autowired
  private ComponentViewModelFactory componentViewModelFactory;
  
  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  @Autowired
  private ComponentControllerService componentControllerService;

  @RequestMapping(value = "/discard/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.DISCARD_COMPONENT + "')")
  public Map<String, Object> findComponentFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("discardReasons", componentControllerService.getDiscardReasons());
    return map;
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> findComponent(@RequestParam(required = true) String componentCode,
      @RequestParam(required = true) String donationIdentificationNumber) {
    Map<String, Object> map = new HashMap<>();
    map.put("component", componentCRUDService.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber));
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> componentSummaryGenerator(HttpServletRequest request,
                                                       @PathVariable Long id) {

    Map<String, Object> map = new HashMap<String, Object>();
    Component component = componentRepository.findComponentById(id);

    ComponentViewModel componentViewModel = componentViewModelFactory.createComponentViewModel(component);
    map.put("componentTypes", componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypes()));
    map.put("component", componentViewModel);
    map.put("componentStatusChangeReasons",
        componentStatusChangeReasonRepository.getAllComponentStatusChangeReasonsAsMap());
    return map;
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT_INFORMATION + "')")
  public Map<String, Object> findComponentFormGenerator(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("componentTypes", componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypes()));
    List<ComponentStatusChangeReason> statusChangeReasons =
        componentStatusChangeReasonRepository.getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.RETURNED);
    map.put("returnReasons", statusChangeReasons);
    statusChangeReasons =
        componentStatusChangeReasonRepository.getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.DISCARDED);
    map.put("discardReasons", statusChangeReasons);
    map.put("findComponentByPackNumberForm", new RecordComponentBackingForm());
    return map;
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> findComponentPagination(HttpServletRequest request,
      @RequestParam(value = "componentNumber", required = false, defaultValue = "") String componentNumber,
      @RequestParam(value = "donationIdentificationNumber", required = false, defaultValue = "") String donationIdentificationNumber,
      @RequestParam(value = "componentTypes", required = false, defaultValue = "") List<Long> componentTypeIds,
      @RequestParam(value = "status", required = false) List<ComponentStatus> statuses,
      @RequestParam(value = "donationDateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date donationDateFrom,
      @RequestParam(value = "donationDateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date donationDateTo) {

    Map<String, Object> map = new HashMap<String, Object>();
    List<Component> results = componentRepository.findAnyComponent(donationIdentificationNumber, componentTypeIds, statuses,
        donationDateFrom, donationDateTo);
    List<ComponentViewModel> components = componentViewModelFactory.createComponentViewModels(results);
    map.put("components", components);
    return map;
  }

  @RequestMapping(value = "{id}/discard", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.DISCARD_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> discardComponent(
      @PathVariable Long id,
      @RequestParam(value = "discardReasonId") Long discardReasonId,
      @RequestParam(value = "discardReasonText", required = false) String discardReasonText) {
    
    Component discardedComponent = componentCRUDService.discardComponent(id, discardReasonId, discardReasonText);

    List<Component> results = componentRepository.findComponentsByDonationIdentificationNumber(
        discardedComponent.getDonation().getDonationIdentificationNumber());

    List<ComponentViewModel> components = componentViewModelFactory.createComponentViewModels(results);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("components", components);
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/donations/{donationNumber}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> findComponentByDonationIdentificationNumber(HttpServletRequest request, @PathVariable String donationNumber) {

    List<Component> results = componentRepository.findComponentsByDonationIdentificationNumber(donationNumber);

    List<ComponentViewModel> componentViewModels = componentViewModelFactory.createComponentViewModels(results);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("components", componentViewModels);
    return map;
  }

  @RequestMapping(value = "/recordcombinations", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> recordNewComponentCombinations(
      @RequestBody RecordComponentBackingForm recordComponentForm) throws ParseException {

    Component parentComponent = componentCRUDService.processComponent(recordComponentForm);
    List<Component> results = componentRepository.findComponentsByDonationIdentificationNumber(
        parentComponent.getDonationIdentificationNumber());
    List<ComponentViewModel> componentViewModels = componentViewModelFactory.createComponentViewModels(results);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("components", componentViewModels);

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
  }

}
