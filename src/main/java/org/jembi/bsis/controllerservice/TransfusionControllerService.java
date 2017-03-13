package org.jembi.bsis.controllerservice;

import java.util.Date;
import java.util.List;

import org.jembi.bsis.backingform.TransfusionBackingForm;
import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.factory.TransfusionFactory;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.TransfusionRepository;
import org.jembi.bsis.service.TransfusionCRUDService;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.TransfusionFullViewModel;
import org.jembi.bsis.viewmodel.TransfusionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TransfusionControllerService {

  @Autowired
  private TransfusionCRUDService transfusionCRUDService;
  @Autowired
  private TransfusionFactory transfusionFactory;
  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private LocationFactory locationFactory;
  @Autowired
  private ComponentTypeRepository componentTypeRepository;
  @Autowired
  private ComponentTypeFactory componentTypeFactory;
  @Autowired
  private TransfusionRepository transfusionRepository;

  public List<LocationViewModel> getUsageSites() {
    List<Location> usageSites = locationRepository.getUsageSites();
    return locationFactory.createViewModels(usageSites);
  }

  public List<ComponentTypeViewModel> getComponentTypes() {
    List<ComponentType> componentTypes = componentTypeRepository.getAllComponentTypesThatCanBeIssued();
    return componentTypeFactory.createViewModels(componentTypes);
  }

  public TransfusionFullViewModel createTransfusion(TransfusionBackingForm backingForm) {
    Transfusion entity = transfusionFactory.createEntity(backingForm);
    entity = transfusionCRUDService.createTransfusion(entity, backingForm.getDonationIdentificationNumber(),
        backingForm.getComponentCode(), backingForm.getComponentType().getId());
    return transfusionFactory.createFullViewModel(entity);
  }

  public TransfusionFullViewModel updateTransfusion(TransfusionBackingForm backingForm) {
    Transfusion entity = transfusionFactory.createEntity(backingForm);
    Long componentTypeId = null;
    if (backingForm.getComponentType() != null) {
      componentTypeId = backingForm.getComponentType().getId();
    }
    entity = transfusionCRUDService.updateTransfusion(entity, backingForm.getDonationIdentificationNumber(),
        backingForm.getComponentCode(), componentTypeId);
    return transfusionFactory.createFullViewModel(entity);
  }

  public List<TransfusionViewModel> findTransfusions(String din, String componentCode, Long componentTypeId,
      Long receivedFromId, TransfusionOutcome transfusionOutcome, Date startDate, Date endDate) {
    List<Transfusion> transfusions = transfusionCRUDService.findTransfusions(din, componentCode, componentTypeId,
        receivedFromId, transfusionOutcome, startDate, endDate);
    return transfusionFactory.createViewModels(transfusions);
  }
  
  public TransfusionFullViewModel getTransfusion(Long id) {
    Transfusion transfusion = transfusionRepository.findTransfusionById(id);
    return transfusionFactory.createFullViewModel(transfusion);
  }
}