package org.jembi.bsis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.ComponentTypeCombinationBackingForm;
import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("componenttypes")
public class ComponentTypeController {

  @Autowired
  private ComponentTypeRepository componentTypeRepository;
  
  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  public ComponentTypeController() {
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity<Map<String, Object>> getComponentTypes(
      @RequestParam(required = false, defaultValue = "true") boolean includeDeleted) {

    List<ComponentType> componentTypes;
    if (includeDeleted) {
      componentTypes = componentTypeRepository.getAllComponentTypesIncludeDeleted();
    } else {
      componentTypes = componentTypeRepository.getAllComponentTypes();
    }

    Map<String, Object> map = new HashMap<>();
    map.put("componentTypes", componentTypeFactory.createViewModels(componentTypes));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity<Map<String, Object>> getComponentTypeById(@PathVariable Long id) {

    Map<String, Object> map = new HashMap<String, Object>();
    ComponentType componentType = componentTypeRepository.getComponentTypeById(id);
    map.put("componentType", componentTypeFactory.createFullViewModel(componentType));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity saveComponentType(@Valid @RequestBody ComponentTypeBackingForm form) {

    ComponentType componentType = componentTypeRepository.saveComponentType(form.getComponentType());
    return new ResponseEntity(componentTypeFactory.createFullViewModel(componentType), HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity updateComponentType(@Valid @RequestBody ComponentTypeBackingForm form,
                                            @PathVariable Long id) {

    ComponentType componentType = form.getComponentType();
    componentType.setId(id);
    componentType = componentTypeRepository.updateComponentType(componentType);
    return new ResponseEntity(componentTypeFactory.createFullViewModel(componentType), HttpStatus.OK);
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

    Map<String, Object> map = new HashMap<String, Object>();
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

  private List<ComponentTypeCombinationViewModel>
  getComponentTypeCombinationViewModels(List<ComponentTypeCombination> componentTypeCombinations) {

    List<ComponentTypeCombinationViewModel> componentTypeCombinationViewModels
        = new ArrayList<ComponentTypeCombinationViewModel>();
    for (ComponentTypeCombination componentTypeCombination : componentTypeCombinations)
      componentTypeCombinationViewModels.add(new ComponentTypeCombinationViewModel(componentTypeCombination));

    return componentTypeCombinationViewModels;

  }

}
