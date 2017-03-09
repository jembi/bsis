package org.jembi.bsis.controllerservice;

import javax.transaction.Transactional;

import org.jembi.bsis.factory.InventoryFactory;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.viewmodel.InventoryFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class InventoryControllerService {
  
  @Autowired
  private InventoryFactory inventoryFactory;
  @Autowired
  private ComponentRepository componentRepository;
  
  public InventoryFullViewModel findComponentByCodeAndDIN(String componentCode, String donationIdentificationNumber) {
    Component component = componentRepository.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber);
    return inventoryFactory.createFullViewModel(component);
  }
}