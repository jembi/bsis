package org.jembi.bsis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.repository.TransfusionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TransfusionCRUDService {

  @Autowired
  private TransfusionRepository transfusionRepository;

  @Autowired
  private ComponentCRUDService componentCRUDService;
  
  /**
   * Create Transfusion data that links a specific Component with a Patient who received a blood
   * transfusion. The Component status will be set to TRANSFUSED
   * 
   * @param transfusion Transfusion entity to be saved
   * @param transfusedComponentTypeId Long if of the component type that was transfused
   * @return Transfusion persisted record
   */
  public Transfusion createTransfusion(Transfusion transfusion, Long transfusedComponentTypeId) {
    // Transfusion data must be associated with a Component
    Component transfusedComponent = transfusion.getComponent();
    if (transfusedComponent == null) {
      // in this case the user didn't enter a component code - they selected the ComponentType
      // we need to link the Component and the Transfusion data
      List<Component> components = componentCRUDService.findComponentsByDINAndType(transfusion.getDonationIdentificationNumber(), transfusedComponentTypeId);
      if (components.size() != 1) {
        throw new IllegalStateException("Unable to create Transfusion data. "
            + "Error: more than one matching Component is found for DIN '" + transfusion.getDonationIdentificationNumber() +"'");
      }
      transfusedComponent = components.get(0);
      transfusion.setComponent(transfusedComponent);
    }
    // Update status of transfused Component
    componentCRUDService.transfuseComponent(transfusedComponent);

    transfusionRepository.save(transfusion);

    return transfusion;
  }

  public List<Transfusion> findTransfusions(String din, String componentCode, Long componentTypeId,
      Long receivedFromId, TransfusionOutcome transfusionOutcome, Date startDate, Date endDate) {
    List<Transfusion> transfusions = new ArrayList<>();
    // Check if din is present
    if (StringUtils.isNotEmpty(din)) {
      transfusions = transfusionRepository.findTransfusionsByDINAndComponentCode(din, componentCode);
    } else {
      transfusions = transfusionRepository.findTransfusionByComponentTypeAndSiteAndOutcome(componentTypeId, receivedFromId, transfusionOutcome, startDate, endDate);
    }
    return transfusions;
  }
}
