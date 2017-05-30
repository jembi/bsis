package org.jembi.bsis.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.Titre;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.util.BloodAbo;
import org.jembi.bsis.model.util.BloodGroup;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.GeneralConfigRepository;
import org.jembi.bsis.template.TemplateEngine;
import org.jembi.bsis.template.labelling.DiscardLabelTemplateObject;
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
  private GeneralConfigAccessorService generalConfigAccessorService;
  @Autowired 
  private CheckCharacterService checkCharacterService;
  @Autowired
  private ComponentVolumeService componentVolumeService;
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
      components = componentRepository.findSafeComponents(componentTypeId, locationId, bloodGroupsList, startDate, endDate,
          inventoryStatus, false);
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
  
  public String printPackLabel(UUID componentId) {
    Component component = componentCRUDService.findComponentById(componentId);
    Donation donation = component.getDonation();
    ComponentType componentType = component.getComponentType();

    boolean canPrintPackLabel = labellingConstraintChecker.canPrintPackLabelWithConsistencyChecks(component);

    // Check to make sure label can be printed
    if (!canPrintPackLabel) {
      throw new IllegalArgumentException("Pack Label can't be printed");
    }

    // If current status is IN_STOCK, update inventory status to REMOVED for this component
    // The component will be put in stock upon successful verification of packLabel
    if (component.getInventoryStatus().equals(InventoryStatus.IN_STOCK)) {
      componentCRUDService.removeComponentFromStock(component);
    }

    // Set up date formats
    String dateFormatString = generalConfigAccessorService.getGeneralConfigValueByName("dateFormat");
    String dateTimeFormatString = generalConfigAccessorService.getGeneralConfigValueByName("dateTimeFormat");
    
    DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
    DateFormat dateTimeFormat = new SimpleDateFormat(dateTimeFormatString);
    DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Update donation without flag characters
    if (donation.getFlagCharacters() == null || donation.getFlagCharacters().isEmpty() ) {
      donation.setFlagCharacters(checkCharacterService.calculateFlagCharacters(donation.getDonationIdentificationNumber()));
    }

    // Generate DIN related elements
    String din = donation.getDonationIdentificationNumber();
    String flagChars = donation.getFlagCharacters();
    String checkCharacter = checkCharacterService.calculateCheckCharacter(flagChars);

    final int dinCharWidth = 10 + 2; // 10 for font width, and 2 for ICG (inter character gap).
    final int startFlagCharsPos = 75;

    int flagCharPos = startFlagCharsPos + ((din.length() - 1) * dinCharWidth);
    int boxPos = flagCharPos + 30;
    int checkCharPos = boxPos + 9;

    String dinZPL = 
      "^FT59,52^A0N,17,38^FH^FDDIN^FS" +
      // Bar code with flag characters, Note "A" - automatic mode used to determine best packing
      // mode
      "^BY2,3,82^FT61,144^BCN,,N,N,N,A" +
      "^FD" + din + flagChars + "^FS" + 
      // manual interpretation line below
      "^FT61,180^ADN,30,10^FH^FD" + din + "^FS" + // DIN
      "^FT" + flagCharPos +",155^ADR,30,10^FH^FD" + flagChars + "^FS"+  // flag character
      "^FO" + boxPos + ",148^GB30,38,3^FS" +  // box around check character
      "^FT" + checkCharPos + ",180^ADN,27,14^FH^FD" + checkCharacter + "^FS"; // check character

    // Generate element for blood Rh
    String bloodRh = "";
    if (donation.getBloodRh().contains("+")) {
      bloodRh = "^FT487,365^A0N,40,38^FB221,1,0,C^FH^FDRhD POSITIVE^FS";
    } else if (donation.getBloodRh().contains("-")) {
      bloodRh = "^FO482,326^GB233,50,50^FS^FT482,363^A0N,40,38^FB233,1,0,C^FR^FH^FDRhD NEGATIVE^FS";
    }
    
    // Generate element for high titre
    String highTitre = "";
    if (shouldLabelIncludeHighTitre(component)) {
      highTitre = "^FT505,409^A0N,36,36,C^FR^FDHIGH TITRE^FS";
    }

    // Generate element for component volume
    String volumeText = "";
    Integer volume = componentVolumeService.calculateVolume(component);
    if (volume != null) {
      volumeText = "Volume: " + volume + "ml";
    }

    // Get configured service info values
    String serviceInfoLine1 = generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_1);
    String serviceInfoLine2 = generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_2);
    
    String labelZPL = 
        "CT~~CD,~CC^~CT~" +
        "^CI28" +
        "^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR2,2~SD15^JUS^LRN^CI28^XZ" +
        "^XA" +
        "^MMT" +
        "^PW799" +
        "^LL0799" +
        "^LS0" +
        dinZPL +
        "^BY3,3,80^FT445,147^BCN,,Y,N^FD" + donation.getBloodAbo() + donation.getBloodRh() + "^FS" +
        "^FT64,208^A0N,17,38^FDCollected On^FS" +
        "^FT64,331^A0N,23,36^FD" + dateFormat.format(donation.getDonationDate()) + "^FS" +
        "^FT505,304^A0N,152,148^FB166,1,0,C^FD" + donation.getBloodAbo() + "^FS" +
        bloodRh +
        "^FT64,414^A0N,20,14^FD" + serviceInfoLine2 + "^FS" +
        "^FT64,387^A0N,20,14^FD" + serviceInfoLine1 + "^FS" +
        "^BY3,3,77^FT64,535^BCN,,Y,N^FD" + component.getComponentCode() + "^FS" +
        "^FT64,616^A0N,43,16^FD" + componentType.getComponentTypeName() + "^FS" +
        "^BY2,3,84^FT64,305^BCN,,N,N^FD" + isoDateFormat.format(donation.getDonationDate()) + "^FS" +
        highTitre +
        "^FT445,439^A0N,17,38^FDExpires On^FS" +
        "^BY2,3,82^FT445,535^BCN,,N,N^FD" + isoDateFormat.format(component.getExpiresOn()) + "^FS" +
        "^FT445,565^A0N,23,31^FD" + dateTimeFormat.format(component.getExpiresOn()) + "^FS" +
        "^FT64,655^A0N,23,14^FD" + volumeText + "^FS" +
        "^FT64,681^A0N,23,14^FD" + componentType.getPreparationInfo() + "^FS" +
        "^FT64,707^A0N,23,14^FD" + componentType.getStorageInfo() + "^FS" +
        "^FT64,733^A0N,23,14^FD" + componentType.getTransportInfo() + "^FS" +
        "^PQ1,0,1,Y^XZ";

    return labelZPL;
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
    String discardLabelZPL = templateEngine.execute(discardLabelTemplate.getName(),discardLabelTemplate.getValue(), (Object)discardLabelTemplateObject);
    return DATA_LINK_ESCAPE + discardLabelZPL;
  }
  
  /**
   * The label should include "HIGH TITRE" if:
   * 
   * 1- The donation's titre is high
   * 2- The donation's blood ABO is "O"
   * 3- The component's type contains plasma
   *
   * @param component the component
   * @return true, if successful
   */
  protected boolean shouldLabelIncludeHighTitre(Component component) {
    Donation donation = component.getDonation();
    if (donation.getTitre() != null && donation.getTitre().equals(Titre.HIGH) && 
        donation.getBloodAbo().equals(BloodAbo.O.name()) && 
        component.getComponentType().getContainsPlasma()) {
      return true;
    }
    return false;
  }

}
