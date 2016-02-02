package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import model.componenttype.ComponentType;
import model.componenttype.ComponentTypeCombination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import repository.ComponentTypeRepository;
import utils.PermissionConstants;
import viewmodel.ComponentTypeCombinationViewModel;
import viewmodel.ComponentTypeViewModel;
import backingform.ComponentTypeBackingForm;
import backingform.ComponentTypeCombinationBackingForm;

@RestController
@RequestMapping("componenttypes")
public class ComponentTypeController {

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  public ComponentTypeController() {
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
      reqUrl += "?" + queryString;
    }
    return reqUrl;
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity<Map<String, Object>> getComponentTypes() {
    Map<String, Object> map = new HashMap<>();
    List<ComponentType> componentTypes = componentTypeRepository.getAllComponentTypesIncludeDeleted();
    map.put("componentTypes", getComponentTypeViewModels(componentTypes));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity<Map<String, Object>> getComponentTypeById(@PathVariable Long id) {

    Map<String, Object> map = new HashMap<>();
    ComponentType componentType = componentTypeRepository.getComponentTypeById(id);
    map.put("componentType", new ComponentTypeViewModel(componentType));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity saveComponentType(@Valid @RequestBody ComponentTypeBackingForm form) {

    ComponentType componentType = componentTypeRepository.saveComponentType(form.getComponentType());
    return new ResponseEntity(new ComponentTypeViewModel(componentType), HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity updateComponentType(@Valid @RequestBody ComponentTypeBackingForm form,
                                            @PathVariable Long id) {

    ComponentType componentType = form.getComponentType();
    componentType.setId(id);
    componentType = componentTypeRepository.updateComponentType(componentType);
    return new ResponseEntity(new ComponentTypeViewModel(componentType), HttpStatus.OK);
  }

  @RequestMapping(value = "{id}/deactivate", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity deactivateComponentType(@PathVariable Long id) {

    componentTypeRepository.deactivateComponentType(id);
    return new ResponseEntity(HttpStatus.OK);
  }

  @RequestMapping(value = "{id}/activate", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity activateComponentType(HttpServletRequest request,
                                              @PathVariable Long id) {

    componentTypeRepository.activateComponentType(id);
    return new ResponseEntity(HttpStatus.OK);
  }

  @RequestMapping(value = "/combinations", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
  public List<ComponentTypeCombinationViewModel> getComponentTypeCombinations() {
    List<ComponentTypeCombination> allComponentTypeCombinationsIncludeDeleted = componentTypeRepository.getAllComponentTypeCombinationsIncludeDeleted();
    return getComponentTypeCombinationViewModels(allComponentTypeCombinationsIncludeDeleted);
  }

  @RequestMapping(value = "/combinations/{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
  public Map<String, Object> getComponentTypeCombination(HttpServletRequest request,
                                                         @PathVariable Long id) {

    Map<String, Object> map = new HashMap<>();
    ComponentTypeCombination componentTypeCombination = componentTypeRepository.getComponentTypeCombinationById(id);
    map.put("componentTypeCombination", new ComponentTypeCombinationViewModel(componentTypeCombination));
    return map;
  }

  @RequestMapping(value = "/combinations", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
  public ResponseEntity saveComponentTypeCombination(@RequestBody ComponentTypeCombinationBackingForm componentTypeCombinationBackingForm) {
    ComponentTypeCombination componentTypeCombination
        = componentTypeCombinationBackingForm.getComponentTypeCombination();

    componentTypeRepository.saveComponentTypeCombination(componentTypeCombination);
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @RequestMapping(value = "/combinations/{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
  public ResponseEntity updateComponentTypeCombination(HttpServletResponse response,
                                                       @RequestBody ComponentTypeCombinationBackingForm componentTypeCombinationBackingForm
      , @PathVariable Long id) {
    ComponentTypeCombination componentTypeCombination =
        componentTypeCombinationBackingForm.getComponentTypeCombination();
    componentTypeCombination.setId(id);
    componentTypeRepository.updateComponentTypeCombination(componentTypeCombination);
    return new ResponseEntity(HttpStatus.OK);
  }

  @RequestMapping(value = "/combinations/{id}/deactivate", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
  public ResponseEntity deactivateComponentTypeCombination(
      @PathVariable Long id) {

    componentTypeRepository.deactivateComponentTypeCombination(id);
    return new ResponseEntity(HttpStatus.OK);
  }

  @RequestMapping(value = "/combinations/{id}/activate", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
  public ResponseEntity activateComponentTypeCombination(
      @PathVariable Long id) {

    componentTypeRepository.activateComponentTypeCombination(id);
    return new ResponseEntity(HttpStatus.OK);
  }

  private List<ComponentTypeViewModel> getComponentTypeViewModels(List<ComponentType> componentTypes) {

    List<ComponentTypeViewModel> componentTypeViewModels = new ArrayList<>();
    for (ComponentType componentType : componentTypes)
      componentTypeViewModels.add(new ComponentTypeViewModel(componentType));

    return componentTypeViewModels;

  }

  private List<ComponentTypeCombinationViewModel>
  getComponentTypeCombinationViewModels(List<ComponentTypeCombination> componentTypeCombinations) {

    List<ComponentTypeCombinationViewModel> componentTypeCombinationViewModels
        = new ArrayList<>();
    for (ComponentTypeCombination componentTypeCombination : componentTypeCombinations)
      componentTypeCombinationViewModels.add(new ComponentTypeCombinationViewModel(componentTypeCombination));

    return componentTypeCombinationViewModels;

  }

}
