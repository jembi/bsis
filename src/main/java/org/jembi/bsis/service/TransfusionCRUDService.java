package org.jembi.bsis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.TransfusionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TransfusionCRUDService {
  
  private static final Logger LOGGER = Logger.getLogger(TransfusionCRUDService.class);

  @Autowired
  private TransfusionRepository transfusionRepository;

  @Autowired
  private ComponentCRUDService componentCRUDService;

  @Autowired
  private ComponentRepository componentRepository;
  
  /**
   * Create Transfusion data that links a specific Component with a Patient who received a blood
   * transfusion. The Component status will be set to TRANSFUSED
   * 
   * @param transfusion Transfusion entity to be saved
   * @return Transfusion persisted record
   */
  public Transfusion createTransfusion(
      Transfusion transfusion, String donationIdentificatioNumber, String transfusedComponentCode, UUID transfusedComponentTypeId) {

    // Transfusion data must be associated with a Component
    if (transfusion.getComponent() == null) {
      // we need to link the Component and the Transfusion data
      transfusion.setComponent(
          getTransfusedComponent(donationIdentificatioNumber, transfusedComponentCode, transfusedComponentTypeId));
    }

    // Update status of transfused Component
    componentCRUDService.transfuseComponent(transfusion.getComponent());

    transfusionRepository.save(transfusion);

    return transfusion;
  }

  public List<Transfusion> findTransfusions(String din, String componentCode, UUID componentTypeId,
      UUID receivedFromId, TransfusionOutcome transfusionOutcome, Date startDate, Date endDate) {
    List<Transfusion> transfusions = new ArrayList<>();

    if (StringUtils.isNotEmpty(din)) {
      // user entered a DIN and Component Code
      try {
        Transfusion transfusion = transfusionRepository.findTransfusionByDINAndComponentCode(din, componentCode);
        transfusions.add(transfusion);
      } catch (NoResultException e) {
        LOGGER.debug("No Transfusion found for din '" + din + "' and componentCode '" + componentCode + "'.");
      }
    } else {
      // user entered other search fields (not DIN and Component Code)
      transfusions = transfusionRepository.findTransfusions(componentTypeId, receivedFromId, transfusionOutcome, startDate, endDate);
    }

    return transfusions;
  }

  private Component getTransfusedComponent(
      String donationIdentificationNumber, String transfusedComponentCode, UUID transfusedComponentTypeId) {
    if (transfusedComponentCode != null) {
      // the user scanned a component code - we need to use that to get the component
      return componentRepository.findComponentByCodeAndDIN(
          transfusedComponentCode, donationIdentificationNumber);
    }

    // if the user selected a componentType - we need to use that to get the component
    List<Component> components = componentCRUDService.findComponentsByDINAndType(donationIdentificationNumber, transfusedComponentTypeId);
    if (components.size() != 1) {
      throw new IllegalStateException("Unable to create Transfusion data. "
          + "Error: Only one matching component is allowed, but zero or more than one matching Components were found for DIN: '"
          + donationIdentificationNumber + "' and component type id: '" + transfusedComponentTypeId + "'");
    }
    return components.get(0);
  }

  public Transfusion updateTransfusion(Transfusion transfusion,  String donationIdentificatioNumber, String transfusedComponentCode) {
    Transfusion existingTransfusion = transfusionRepository.findTransfusionById(transfusion.getId());

    existingTransfusion.setDateTransfused(transfusion.getDateTransfused());
    existingTransfusion.setIsDeleted(transfusion.getIsDeleted());
    existingTransfusion.setNotes(transfusion.getNotes());
    existingTransfusion.setPatient(transfusion.getPatient());
    existingTransfusion.setReceivedFrom(transfusion.getReceivedFrom());
    existingTransfusion.setTransfusionOutcome(transfusion.getTransfusionOutcome());
    existingTransfusion.setTransfusionReactionType(transfusion.getTransfusionReactionType());
    return transfusionRepository.update(existingTransfusion);
  }

  public void deleteTransfusion(UUID transfusionId) throws IllegalStateException, NoResultException {
    Transfusion transfusion = transfusionRepository.findTransfusionById(transfusionId);
    if (transfusion == null) {
      throw new IllegalStateException("Transfusion with id " + transfusionId
          + " does not exist (or has already been deleted).");
    }

    // Rollback component status from TRANSFUSED to ISSUED
    componentCRUDService.untransfuseComponent(transfusion.getComponent());

    transfusion.setIsDeleted(Boolean.TRUE);
    transfusionRepository.update(transfusion);
  }
}
