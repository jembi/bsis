package org.jembi.bsis.controllerservice;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.viewmodel.ComponentTypeFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeSearchViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ComponentTypeControllerService {

  @Autowired
  private ComponentTypeRepository componentTypeRepository;
  
  @Autowired
  private ComponentTypeFactory componentTypeFactory;
  
  public List<ComponentTypeSearchViewModel> getComponentTypes() {
    List<ComponentType> componentTypes = componentTypeRepository.getAllComponentTypesIncludeDeleted();
    return componentTypeFactory.createSearchViewModels(componentTypes);
  }
  
  public ComponentTypeFullViewModel getComponentType(UUID id) {
    return componentTypeFactory.createFullViewModel(componentTypeRepository.getComponentTypeById(id));
  }
  
  public ComponentTypeFullViewModel addComponentType(ComponentTypeBackingForm form) {
    ComponentType componentType = componentTypeRepository.saveComponentType(componentTypeFactory.createEntity(form));
    return componentTypeFactory.createFullViewModel(componentType);
  }
  
  public ComponentTypeFullViewModel updateComponentType(ComponentTypeBackingForm form) {
    ComponentType componentType = componentTypeFactory.createEntity(form);
    
    componentType = componentTypeRepository.updateComponentType(componentType);
    return componentTypeFactory.createFullViewModel(componentType);
  }
}
