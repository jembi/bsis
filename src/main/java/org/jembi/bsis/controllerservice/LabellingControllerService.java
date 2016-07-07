package org.jembi.bsis.controllerservice;

import java.util.List;

import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.factory.LabellingFactory;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.service.ComponentCRUDService;
import org.jembi.bsis.service.LabellingCRUDService;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.LabellingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabellingControllerService {
  
  @Autowired
  private ComponentTypeRepository componentTypeRepository;
  
  @Autowired
  private ComponentTypeFactory componentTypeFactory;
  
  @Autowired
  private LabellingFactory labellingFactory;
  
  @Autowired
  private ComponentCRUDService componentCRUDService;

  @Autowired
  private LabellingCRUDService labellingCRUDService;

  public List<ComponentTypeViewModel> getComponentTypes() {
    return componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypes());
  }
  
  public List<LabellingViewModel> getComponentsForLabelling(String donationIdentificationNumber, long componentTypeId) {
    List<Component> components = componentCRUDService.findComponentsByDINAndType(donationIdentificationNumber, componentTypeId);
    return labellingFactory.createViewModels(components);
  }

  public String printPackLabel(long componentId) {
    return labellingCRUDService.printPackLabel(componentId);
  }

  public String printDiscardLabel(long componentId) {
    return labellingCRUDService.printDiscardLabel(componentId);
  }
}
