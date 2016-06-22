package org.jembi.bsis.controllerservice;

import java.util.List;

import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabellingControllerService {
  
  @Autowired
  ComponentTypeRepository componentTypeRepository;
  
  @Autowired
  ComponentTypeFactory componentTypeFactory;
  

  public List<ComponentTypeViewModel> getComponentTypes() {
    return componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypes());
  }
}
