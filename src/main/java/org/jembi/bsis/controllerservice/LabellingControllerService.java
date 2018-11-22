package org.jembi.bsis.controllerservice;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.factory.ComponentFactory;
import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.factory.LabellingFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.ComponentCRUDService;
import org.jembi.bsis.service.LabellingService;
import org.jembi.bsis.viewmodel.ComponentFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.LabellingViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
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

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private LocationFactory locationFactory;

  public List<ComponentTypeViewModel> getComponentTypes() {
    return componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypes());
  }
  
  public List<LabellingViewModel> getComponentsForLabelling(String donationIdentificationNumber, UUID componentTypeId) {
    List<Component> components = componentCRUDService.findComponentsByDINAndType(donationIdentificationNumber, componentTypeId);
    return labellingFactory.createViewModels(components);
  }

  public String printPackLabel(UUID componentId) throws IOException {
    return labellingService.printPackLabel(componentId);
  }

  public String printDiscardLabel(UUID componentId) throws IOException {
    return labellingService.printDiscardLabel(componentId);
  }
  
  public boolean verifyPackLabel(UUID componentId, String prePrintedDIN, String packLabelDIN) {
    return labellingService.verifyPackLabel(componentId, prePrintedDIN, packLabelDIN);
  }

  public List<ComponentFullViewModel> findSafeComponentsToLabel(String din, String componentCode, UUID componentTypeId,
      UUID locationId, List<String> bloodGroups, Date startDate, Date endDate, InventoryStatus inventoryStatus) {
    List<Component> components = labellingService.findSafeComponentsToLabel(din, componentCode, componentTypeId, locationId,
        bloodGroups, startDate, endDate, inventoryStatus);
    return componentFactory.createComponentFullViewModels(components);
  }

  public List<LocationViewModel> getLocations() {
    return locationFactory.createViewModels(locationRepository.getAllLocations(false));
  }

}
