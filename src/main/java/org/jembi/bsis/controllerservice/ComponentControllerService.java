package org.jembi.bsis.controllerservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.DiscardComponentsBackingForm;
import org.jembi.bsis.backingform.RecordComponentBackingForm;
import org.jembi.bsis.factory.ComponentFactory;
import org.jembi.bsis.factory.ComponentStatusChangeReasonFactory;
import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.ComponentStatusChangeReasonRepository;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.service.ComponentCRUDService;
import org.jembi.bsis.viewmodel.ComponentManagementViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.jembi.bsis.viewmodel.DiscardReasonViewModel;
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

  public ComponentViewModel findComponentById(Long id) {
    Component component = componentRepository.findComponentById(id);
    ComponentViewModel componentViewModel = componentFactory.createComponentViewModel(component);
    return componentViewModel;
  }
  
  public ComponentViewModel findComponentByCodeAndDIN(String componentCode, String donationIdentificationNumber) {
    Component component = componentRepository.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber);
    return componentFactory.createComponentViewModel(component);
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

  public List<ComponentViewModel> findAnyComponent(List<Long> componentTypeIds,
      List<ComponentStatus> statusStringToComponentStatus, Date dateFrom, Date dateTo) {
    List<Component> results = componentRepository.findAnyComponent(componentTypeIds,
        statusStringToComponentStatus, dateFrom, dateTo);
    List<ComponentViewModel> components = componentFactory.createComponentViewModels(results);
    return components;
  }
  
  public List<ComponentManagementViewModel> processComponent(RecordComponentBackingForm recordComponentForm) {
    Component parentComponent = componentCRUDService.processComponent(recordComponentForm.getParentComponentId(), 
        recordComponentForm.getComponentTypeCombination());
    List<Component> results = componentRepository.findComponentsByDonationIdentificationNumber(
        parentComponent.getDonationIdentificationNumber());
    List<ComponentManagementViewModel> componentViewModels = componentFactory.createManagementViewModels(results);
    return componentViewModels;
  }
  
  public ComponentManagementViewModel updateComponent(ComponentBackingForm componentBackingForm) {
    Component component = componentFactory.createEntity(componentBackingForm);
    component = componentCRUDService.updateComponent(component);
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

  public ComponentManagementViewModel unprocessComponent(Long componentId) {
    Component component = componentRepository.findComponentById(componentId);
    component = componentCRUDService.unprocessComponent(component);
    return componentFactory.createManagementViewModel(component);
  }
  
  public List<ComponentManagementViewModel> undiscardComponents(List<Long> componentIds) {
    List<ComponentManagementViewModel> componentViewModels = new ArrayList<>();
    for (Long componentId : componentIds) {
      Component undiscardedComponent = componentCRUDService.undiscardComponent(componentId);
      componentViewModels.add(componentFactory.createManagementViewModel(undiscardedComponent));
    }
    return componentViewModels;
  }
  
}
