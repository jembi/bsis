package controllerservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.ComponentTypeRepository;
import viewmodel.ComponentTypeViewModel;
import factory.ComponentTypeFactory;

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
