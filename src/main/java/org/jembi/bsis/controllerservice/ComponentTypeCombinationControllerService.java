package org.jembi.bsis.controllerservice;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.jembi.bsis.backingform.ComponentTypeCombinationBackingForm;
import org.jembi.bsis.factory.ComponentTypeCombinationFactory;
import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.repository.ComponentTypeCombinationRepository;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.service.ComponentTypeCombinationCRUDService;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ComponentTypeCombinationControllerService {

  @Autowired
  private ComponentTypeCombinationRepository componentTypeCombinationRepository;

  @Autowired
  private ComponentTypeCombinationFactory componentTypeCombinationFactory;

  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  @Autowired
  private ComponentTypeCombinationCRUDService componentTypeCombinationCRUDService;

  public List<ComponentTypeCombinationViewModel> getComponentTypeCombinations(boolean includeDeleted) {
    return componentTypeCombinationFactory.createViewModels(componentTypeCombinationRepository.getAllComponentTypeCombinations(includeDeleted));
  }

  public ComponentTypeCombinationFullViewModel findComponentTypeCombinationById(UUID id){
    return componentTypeCombinationFactory.createFullViewModel
        (componentTypeCombinationRepository.findComponentTypeCombinationById(id));
  }

  public List<ComponentTypeViewModel> getAllComponentTypes() {
    return componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypes());
  }
  
  public ComponentTypeCombinationFullViewModel createComponentTypeCombination(ComponentTypeCombinationBackingForm backingForm) {
    ComponentTypeCombination componentTypeCombination = componentTypeCombinationFactory.createEntity(backingForm);
    componentTypeCombination = componentTypeCombinationCRUDService.createComponentTypeCombination(componentTypeCombination);
    return componentTypeCombinationFactory.createFullViewModel(componentTypeCombination);
  }

  public ComponentTypeCombinationFullViewModel updateComponentTypeCombination(ComponentTypeCombinationBackingForm backingForm) {
    ComponentTypeCombination componentTypeCombination = componentTypeCombinationFactory.createEntity(backingForm);
    ComponentTypeCombination updatedComponentTypeCombination = componentTypeCombinationCRUDService.updateComponentTypeCombinations(componentTypeCombination);
    return componentTypeCombinationFactory.createFullViewModel(updatedComponentTypeCombination);
  }
}
