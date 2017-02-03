package org.jembi.bsis.controllerservice;

import java.util.Date;
import java.util.List;

import org.jembi.bsis.factory.ComponentFactory;
import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.factory.LabellingFactory;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.service.ComponentCRUDService;
import org.jembi.bsis.service.LabellingService;
import org.jembi.bsis.viewmodel.ComponentFullViewModel;
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
  private LabellingService labellingService;

  @Autowired
  private ComponentFactory componentFactory;

  public List<ComponentTypeViewModel> getComponentTypes() {
    return componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypes());
  }
  
  public List<LabellingViewModel> getComponentsForLabelling(String donationIdentificationNumber, long componentTypeId) {
    List<Component> components = componentCRUDService.findComponentsByDINAndType(donationIdentificationNumber, componentTypeId);
    return labellingFactory.createViewModels(components);
  }

  public String printPackLabel(long componentId) {
    return labellingService.printPackLabel(componentId);
  }

  public String printDiscardLabel(long componentId) {
    return labellingService.printDiscardLabel(componentId);
  }
  
  public boolean verifyPackLabel(long componentId, String prePrintedDIN, String packLabelDIN) {
    return labellingService.verifyPackLabel(componentId, prePrintedDIN, packLabelDIN);
  }

  public List<ComponentFullViewModel> findSafeComponents(String din, String componentCode, Long componentTypeId,
      Long locationId, List<String> bloodGroups, Date startDate, Date endDate, InventoryStatus inventoryStatus) {
    List<Component> components = labellingService.findSafeComponents(din, componentCode, componentTypeId, locationId,
        bloodGroups, startDate, endDate, inventoryStatus);
    return componentFactory.createComponentFullViewModels(components);
  }
}
