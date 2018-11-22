package org.jembi.bsis.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.util.BloodGroup;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.GeneralConfigRepository;
import org.jembi.bsis.template.TemplateEngine;
import org.jembi.bsis.template.labelling.DiscardLabelTemplateObject;
import org.jembi.bsis.template.labelling.PackLabelTemplateObject;
import org.jembi.bsis.template.labelling.TemplateObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabellingService {

  public static final String DATA_LINK_ESCAPE = "\u0010";

  @Autowired
  private ComponentCRUDService componentCRUDService;
  @Autowired
  private LabellingConstraintChecker labellingConstraintChecker;
  @Autowired 
  private CheckCharacterService checkCharacterService;
  @Autowired
  private ComponentRepository componentRepository;
  @Autowired
  private TemplateObjectFactory templateObjectFactory;
  @Autowired
  private TemplateEngine templateEngine;
  @Autowired
  private GeneralConfigRepository generalConfigRepository;
  
  public List<Component> findSafeComponentsToLabel(String din, String componentCode, UUID componentTypeId,
      UUID locationId,
      List<String> bloodGroups, Date startDate, Date endDate, InventoryStatus inventoryStatus) {
    List<Component> components = new ArrayList<>();
    // Search for safe components excluding initial ones, as those can't be labelled
    // Check if din is present
    if (StringUtils.isNotEmpty(din)) {
      components = componentRepository.findComponentsByDINAndComponentCodeAndStatus(din, componentCode,
          ComponentStatus.AVAILABLE, false);
    } else {
      List<BloodGroup> bloodGroupsList = BloodGroup.toBloodGroups(bloodGroups);
      List<InventoryStatus> inventoryStatuses = null;
      if (inventoryStatus != null) {
        inventoryStatuses = new ArrayList<>();
        inventoryStatuses.add(inventoryStatus);
        if (inventoryStatus == InventoryStatus.NOT_IN_STOCK) {
          inventoryStatuses.add(InventoryStatus.REMOVED);
        }
      }
      components = componentRepository.findSafeComponents(componentTypeId, locationId, bloodGroupsList, startDate, endDate,
          inventoryStatuses, false);
    }
    return components;
  }

  public boolean verifyPackLabel(UUID componentId, String prePrintedDIN, String packLabelDIN) {
    Component component = componentCRUDService.findComponentById(componentId);
    if (!component.getStatus().equals(ComponentStatus.AVAILABLE)) {
      return false;
    }
    
    String recordedDin = component.getDonation().getDonationIdentificationNumber();
    String recordedFlagCharacters = component.getDonation().getFlagCharacters();
    if (!recordedDin.equals(prePrintedDIN) ||!(recordedDin+recordedFlagCharacters).equals(packLabelDIN)) {
      return false;
    } else {
      componentCRUDService.putComponentInStock(component);
      return true;
    }
  }
  
  public String printPackLabel(UUID componentId) throws IOException {
    Component component = componentCRUDService.findComponentById(componentId);

    // Check to make sure label can be printed
    boolean canPrintPackLabel = labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);
    if (!canPrintPackLabel) {
      throw new IllegalArgumentException("Pack Label can't be printed");
    }

    // If current status is IN_STOCK, update inventory status to REMOVED for this component
    // The component will be put in stock upon successful verification of packLabel
    if (component.getInventoryStatus().equals(InventoryStatus.IN_STOCK)) {
      componentCRUDService.removeComponentFromStock(component);
    }

    // Update donation without flag characters
    Donation donation = component.getDonation();
    if (donation.getFlagCharacters() == null || donation.getFlagCharacters().isEmpty() ) {
      donation.setFlagCharacters(checkCharacterService.calculateFlagCharacters(donation.getDonationIdentificationNumber()));
    }

    PackLabelTemplateObject packLabelTemplateObject = templateObjectFactory.createPackLabelTemplateObject(component);
    GeneralConfig packLabelTemplate = generalConfigRepository.getGeneralConfigByName(GeneralConfigConstants.PACK_LABEL_ZPL);
    String packLabelZPL = templateEngine.execute(packLabelTemplate.getName(), packLabelTemplate.getValue(), packLabelTemplateObject);

    return DATA_LINK_ESCAPE + packLabelZPL;
  }

  public String printDiscardLabel(UUID componentId) throws IOException {
    Component component = componentCRUDService.findComponentById(componentId);

    boolean canPrintDiscardLabel = labellingConstraintChecker.canPrintDiscardLabel(component);

    // check to make sure discard label can be printed
    if (!canPrintDiscardLabel) {
      throw new IllegalArgumentException("Discard Label can't be printed");
    }

    DiscardLabelTemplateObject discardLabelTemplateObject = templateObjectFactory.createDiscardLabelTemplateObject(component);
    GeneralConfig discardLabelTemplate = generalConfigRepository.getGeneralConfigByName(GeneralConfigConstants.DISCARD_LABEL_ZPL);
    String discardLabelZPL = templateEngine.execute(discardLabelTemplate.getName(), discardLabelTemplate.getValue(), discardLabelTemplateObject);
    return DATA_LINK_ESCAPE + discardLabelZPL;
  }
}
