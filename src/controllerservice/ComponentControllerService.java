package controllerservice;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backingform.RecordComponentBackingForm;
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
import viewmodel.ComponentTypeViewModel;
import viewmodel.ComponentViewModel;

@Service
@Transactional
public class ComponentControllerService {

  @Autowired
  private ComponentRepository componentRepository;
  
  @Autowired
  private ComponentCRUDService componentCRUDService;

  @Autowired
  private ComponentViewModelFactory componentViewModelFactory;
  
  @Autowired
  private ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;

  @Autowired
  private ComponentTypeRepository componentTypeRepository;
  
  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  public ComponentViewModel findComponentById(Long id) {
    Component component = componentRepository.findComponentById(id);
    ComponentViewModel componentViewModel = componentViewModelFactory.createComponentViewModel(component);
    return componentViewModel;
  }
  
  public ComponentViewModel findComponentByCodeAndDIN(String componentCode, String donationIdentificationNumber) {
    Component component = componentRepository.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber);
    return componentViewModelFactory.createComponentViewModel(component);
  }
  
  public List<ComponentViewModel> findComponentsByDonationIdentificationNumber(String donationNumber) {
    List<Component> results = componentRepository.findComponentsByDonationIdentificationNumber(donationNumber);
    List<ComponentViewModel> componentViewModels = componentViewModelFactory.createComponentViewModels(results);
    return componentViewModels;
  }

  public List<ComponentViewModel> findAnyComponent(String donationIdentificationNumber, List<Long> componentTypeIds,
      List<ComponentStatus> statusStringToComponentStatus, Date dateFrom, Date dateTo) {
    List<Component> results = componentRepository.findAnyComponent(donationIdentificationNumber, componentTypeIds,
        statusStringToComponentStatus, dateFrom, dateTo);
    List<ComponentViewModel> components = componentViewModelFactory.createComponentViewModels(results);
    return components;
  }

  public List<ComponentViewModel> discardComponent(Long id, Long discardReasonId, String discardReasonText) {
    Component discardedComponent = componentCRUDService.discardComponent(id, discardReasonId, discardReasonText);

    List<Component> results = componentRepository.findComponentsByDonationIdentificationNumber(
        discardedComponent.getDonation().getDonationIdentificationNumber());

    List<ComponentViewModel> components = componentViewModelFactory.createComponentViewModels(results);
    
    return components;
  }
  
  public List<ComponentViewModel> processComponent(RecordComponentBackingForm recordComponentForm) {
    Component parentComponent = componentCRUDService.processComponent(recordComponentForm.getParentComponentId(), 
        recordComponentForm.getComponentTypeCombination());
    List<Component> results = componentRepository.findComponentsByDonationIdentificationNumber(
        parentComponent.getDonationIdentificationNumber());
    List<ComponentViewModel> componentViewModels = componentViewModelFactory.createComponentViewModels(results);
    return componentViewModels;
  }
  
  public List<ComponentStatusChangeReason> getReturnReasons() {
    return componentStatusChangeReasonRepository.getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.RETURNED);
  }
  
  public List<ComponentStatusChangeReason> getDiscardReasons() {
    return componentStatusChangeReasonRepository.getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.DISCARDED);
  }
  
  public List<ComponentTypeViewModel> getComponentTypes() {
    return componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypes());
  }
  
}
