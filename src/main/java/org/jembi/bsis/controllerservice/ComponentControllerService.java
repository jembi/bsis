package org.jembi.bsis.controllerservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.ComponentPreProcessingBackingForm;
import org.jembi.bsis.backingform.DiscardComponentsBackingForm;
import org.jembi.bsis.backingform.RecordComponentBackingForm;
import org.jembi.bsis.factory.ComponentFactory;
import org.jembi.bsis.factory.ComponentStatusChangeReasonFactory;
import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.ComponentStatusChangeReasonRepository;
import org.jembi.bsis.repository.ComponentTypeCombinationRepository;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.ComponentCRUDService;
import org.jembi.bsis.viewmodel.ComponentFullViewModel;
import org.jembi.bsis.viewmodel.ComponentManagementViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.jembi.bsis.viewmodel.DiscardReasonViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ComponentControllerService {

  @Autowired
  private ComponentRepository componentRepository;
  
  @Autowired
  private ComponentCRUDService componentCRUDService;

  @Autowired
  private ComponentFactory componentFactory;
  
  @Autowired
  private ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;

  @Autowired
  private ComponentTypeRepository componentTypeRepository;
  
  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  @Autowired
  private ComponentStatusChangeReasonFactory componentStatusChangeReasonFactory;

  @Autowired
  private ComponentTypeCombinationRepository componentTypeCombinationRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private LocationFactory locationFactory;

  public ComponentFullViewModel findComponentById(UUID id) {
    Component component = componentRepository.findComponentById(id);
    ComponentFullViewModel componentFullViewModel = componentFactory.createComponentFullViewModel(component);
    return componentFullViewModel;
  }
  
  public ComponentFullViewModel findComponentByCodeAndDIN(String componentCode, String donationIdentificationNumber) {
    Component component = componentRepository.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber);
    return componentFactory.createComponentFullViewModel(component);
  }
  
  public List<ComponentManagementViewModel> findManagementComponentsByDonationIdentificationNumber(String donationNumber) {
    List<Component> results = componentRepository.findComponentsByDonationIdentificationNumber(donationNumber);
    List<ComponentManagementViewModel> componentViewModels = componentFactory.createManagementViewModels(results);
    return componentViewModels;
  }
  
  public List<ComponentViewModel> findComponentsByDonationIdentificationNumber(String donationNumber) {
    List<Component> results = componentRepository.findComponentsByDonationIdentificationNumber(donationNumber);
    return componentFactory.createComponentViewModels(results);
  }

  public List<ComponentViewModel> findComponentsByDonationIdentificationNumberAndStatus(
      String donationIdentificationNumber, ComponentStatus status) {
    List<Component> results = componentRepository.findComponentsByDonationIdentificationNumberAndStatus(
        donationIdentificationNumber, status);
    return componentFactory.createComponentViewModels(results);
  }

  public List<ComponentViewModel> findAnyComponent(List<UUID> componentTypeIds,
      ComponentStatus status, Date dateFrom, Date dateTo, UUID locationId) {
    List<Component> results = componentRepository.findAnyComponent(componentTypeIds, status, dateFrom, dateTo, locationId);
    List<ComponentViewModel> components = componentFactory.createComponentViewModels(results);
    return components;
  }
  
  public List<ComponentManagementViewModel> processComponent(RecordComponentBackingForm recordComponentForm) {
    Component parentComponent = componentCRUDService.processComponent(recordComponentForm.getParentComponentId(), 
        recordComponentForm.getComponentTypeCombination().getId(), recordComponentForm.getProcessedOn());
    List<Component> results = componentRepository.findComponentsByDonationIdentificationNumber(
        parentComponent.getDonationIdentificationNumber());
    List<ComponentManagementViewModel> componentViewModels = componentFactory.createManagementViewModels(results);
    return componentViewModels;
  }
  
  public ComponentManagementViewModel preProcessComponent(ComponentPreProcessingBackingForm componentBackingForm) {
    Component component = componentCRUDService.preProcessComponent(componentBackingForm.getId(),
        componentBackingForm.getWeight(), componentBackingForm.getBleedStartTime(), componentBackingForm.getBleedEndTime());
    return componentFactory.createManagementViewModel(component);
  }
  
  public List<ComponentStatusChangeReason> getReturnReasons() {
    return componentStatusChangeReasonRepository.getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.RETURNED);
  }
  
  public List<DiscardReasonViewModel> getDiscardReasons() {
    List<ComponentStatusChangeReason> reasons = componentStatusChangeReasonRepository.getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.DISCARDED);
    return componentStatusChangeReasonFactory.createDiscardReasonViewModels(reasons);
  }
  
  public List<ComponentTypeViewModel> getComponentTypes() {
    return componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypes());
  }

  public void discardComponents(DiscardComponentsBackingForm discardComponentsBackingForm) {
    componentCRUDService.discardComponents(discardComponentsBackingForm.getComponentIds(),
        discardComponentsBackingForm.getDiscardReason().getId(), discardComponentsBackingForm.getDiscardReasonText());
  }

  public ComponentManagementViewModel unprocessComponent(UUID componentId) {
    Component component = componentRepository.findComponentById(componentId);
    component = componentCRUDService.unprocessComponent(component);
    return componentFactory.createManagementViewModel(component);
  }
  
  public List<ComponentManagementViewModel> undiscardComponents(List<UUID> componentIds) {
    List<ComponentManagementViewModel> componentViewModels = new ArrayList<>();
    for (UUID componentId : componentIds) {
      Component undiscardedComponent = componentCRUDService.undiscardComponent(componentId);
      componentViewModels.add(componentFactory.createManagementViewModel(undiscardedComponent));
    }
    return componentViewModels;
  }

  public Map<UUID, List<ComponentTypeViewModel>> getProducedComponentTypesByCombinationId() {
    Map<UUID, List<ComponentTypeViewModel>> map = new HashMap<UUID, List<ComponentTypeViewModel>>();
    for (ComponentTypeCombination combination : componentTypeCombinationRepository
        .getAllComponentTypeCombinations(false)) {
      map.put(combination.getId(), componentTypeFactory.createViewModels(combination.getComponentTypes()));
    }
    return map;
  }
  
  public ComponentManagementViewModel recordChildComponentWeight(ComponentBackingForm componentBackingForm) {
    Component component = componentCRUDService.recordChildComponentWeight(componentBackingForm.getId(), 
        componentBackingForm.getWeight());
    return componentFactory.createManagementViewModel(component);
  }

  public List<ComponentStatus> getComponentStatuses() {
    return Arrays.asList(ComponentStatus.values());
  }

  public List<LocationViewModel> getLocations() {
    return locationFactory.createViewModels(locationRepository.getAllLocations(false));
  }
}
