package org.jembi.bsis.template.labelling;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.Titre;
import org.jembi.bsis.model.util.BloodAbo;
import org.jembi.bsis.service.CheckCharacterService;
import org.jembi.bsis.service.ComponentVolumeService;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateObjectFactory {

  @Autowired
  private GeneralConfigAccessorService generalConfigAccessorService;

  @Autowired
  private ComponentVolumeService componentVolumeService;

  @Autowired
  private CheckCharacterService checkCharacterService;

  public PackLabelTemplateObject createPackLabelTemplateObject(Component component) {
    PackLabelTemplateObject template = new PackLabelTemplateObject();

    String dateFormatString = generalConfigAccessorService.getGeneralConfigValueByName("dateFormat");
    String dateTimeFormatString = generalConfigAccessorService.getGeneralConfigValueByName("dateTimeFormat");

    DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
    DateFormat dateTimeFormat = new SimpleDateFormat(dateTimeFormatString);
    DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    updatePackLabelTemplateObjectWithConfigInfo(template);
    updatePackLabelTemplateObjectWithComponentInfo(template, component, dateTimeFormat, isoDateFormat);
    updatePackLabelTemplateObjectWithDonationInfo(template, component, dateFormat, isoDateFormat);
    updatePackLabelTemplateObjectWithComponentTypeInfo(template, component.getComponentType());
    updatePackLabelTemplateObjectWithDINPositioningInfo(template,
        component.getDonation().getDonationIdentificationNumber().length());
    return template;
  }

  private void updatePackLabelTemplateObjectWithConfigInfo(PackLabelTemplateObject template) {
    String serviceInfoLine1 =
        generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.SERVICE_INFO_LINE_1);
    String serviceInfoLine2 =
        generalConfigAccessorService.getGeneralConfigValueByName(GeneralConfigConstants.SERVICE_INFO_LINE_2);

    template.config.serviceInfoLine1 = serviceInfoLine1;
    template.config.serviceInfoLine2 = serviceInfoLine2;
  }

  private void updatePackLabelTemplateObjectWithDINPositioningInfo(PackLabelTemplateObject template, int DINLength) {
    final int dinCharWidth = 12; // // = 10+2 : 10 for font width, and 2 for ICG (inter character gap).
    final int startFlagCharsPos = 75;
    int flagCharPos = startFlagCharsPos + ((DINLength - 1) * dinCharWidth);
    int boxPos = flagCharPos + 30;

    template.DINPositioning.flagCharPos = flagCharPos;
    template.DINPositioning.boxPos = boxPos;
    template.DINPositioning.checkCharPos = boxPos + 9;
  }

  private void updatePackLabelTemplateObjectWithComponentInfo(PackLabelTemplateObject template, Component component,
      DateFormat dateTimeFormat, DateFormat isoDateFormat) {
    template.component.componentCode = component.getComponentCode();
    template.component.expiresOn = dateTimeFormat.format(component.getExpiresOn());
    template.component.expiresOnISO = isoDateFormat.format(component.getExpiresOn());
    template.component.volume = componentVolumeService.calculateVolume(component);
  }

  private void updatePackLabelTemplateObjectWithDonationInfo(PackLabelTemplateObject template, Component component,
      DateFormat dateFormat, DateFormat isoDateFormat) {
    Donation donation = component.getDonation();

    template.donation.DIN = donation.getDonationIdentificationNumber();
    template.donation.flagCharacters = donation.getFlagCharacters();
    template.donation.checkCharacter = checkCharacterService.calculateCheckCharacter(donation.getFlagCharacters());
    template.donation.bloodABO = donation.getBloodAbo();
    template.donation.bloodRh = donation.getBloodRh();
    template.donation.isBloodRhPositive = donation.getBloodRh().contains("+");
    template.donation.isBloodRhNegative = donation.getBloodRh().contains("-");
    template.donation.isBloodHighTitre = shouldLabelIncludeHighTitre(component);
    template.donation.donationDate = dateFormat.format(donation.getDonationDate());
    template.donation.donationDateISO = isoDateFormat.format(donation.getDonationDate());
  }

  /**
   * The label should include "HIGH TITRE" if:
   * 
   * 1- The donation's titre is high 2- The donation's blood ABO is "O" 3- The component's type
   * contains plasma
   *
   * @param component the component
   * @return true, if successful
   */
  protected boolean shouldLabelIncludeHighTitre(Component component) {
    Donation donation = component.getDonation();
    if (donation.getTitre() != null && donation.getTitre().equals(Titre.HIGH)
        && donation.getBloodAbo().equals(BloodAbo.O.name()) && component.getComponentType().getContainsPlasma()) {
      return true;
    }
    return false;
  }

  private void updatePackLabelTemplateObjectWithComponentTypeInfo(PackLabelTemplateObject template,
      ComponentType componentType) {
    template.componentType.componentTypeName = componentType.getComponentTypeName();
    template.componentType.preparationInfo = componentType.getPreparationInfo();
    template.componentType.storageInfo = componentType.getStorageInfo();
    template.componentType.transportInfo = componentType.getTransportInfo();
  }

  public DiscardLabelTemplateObject createDiscardLabelTemplateObject(Component component) {
    DiscardLabelTemplateObject template = new DiscardLabelTemplateObject();

    template.component.componentCode = component.getComponentCode();
    template.componentType.componentTypeCode = component.getComponentType().getComponentTypeCode();
    template.donation.DIN = component.getDonationIdentificationNumber();

    updateDiscardLabelTemplateObjectWithConfigInfo(template);

    return template;
  }

  private void updateDiscardLabelTemplateObjectWithConfigInfo(DiscardLabelTemplateObject template) {
    String serviceInfoLine1 = generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_1);
    String serviceInfoLine2 = generalConfigAccessorService.getGeneralConfigValueByName(
        GeneralConfigConstants.SERVICE_INFO_LINE_2);
    template.config.serviceInfoLine1 = serviceInfoLine1;
    template.config.serviceInfoLine2 = serviceInfoLine2;
  }
}
