package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.template.labelling.PackLabelTemplateObject;

public class PackLabelTemplateObjectBuilder extends AbstractBuilder<PackLabelTemplateObject> {
  private int flagCharPos;
  private int boxPos;
  private int checkCharPos;

  private String serviceInfoLine1;
  private String serviceInfoLine2;

  private String DIN;
  private String flagCharacters;
  private String checkCharacter;
  private String bloodABO;
  private String bloodRh;
  private boolean isBloodRhPositive;
  private boolean isBloodRhNegative;
  private boolean isBloodHighTitre;
  private String donationDate;
  private String donationDateISO;

  private String componentCode;
  private String expiresOn;
  private String expiresOnISO;
  private Integer volume;

  private String componentTypeName;
  private String preparationInfo;
  private String storageInfo;
  private String transportInfo;

  public PackLabelTemplateObjectBuilder withServiceInfoLine1(String serviceInfoLine1) {
    this.serviceInfoLine1 = serviceInfoLine1;
    return this;
  }

  public PackLabelTemplateObjectBuilder withServiceInfoLine2(String serviceInfoLine2) {
    this.serviceInfoLine2 = serviceInfoLine2;
    return this;
  }

  public PackLabelTemplateObjectBuilder withFlagCharPos(int flagCharPos) {
    this.flagCharPos = flagCharPos;
    return this;
  }

  public PackLabelTemplateObjectBuilder withBoxPos(int boxPos) {
    this.boxPos = boxPos;
    return this;
  }

  public PackLabelTemplateObjectBuilder withCheckCharPos(int checkCharPos) {
    this.checkCharPos = checkCharPos;
    return this;
  }

  public PackLabelTemplateObjectBuilder withDIN(String DIN) {
    this.DIN = DIN;
    return this;
  }

  public PackLabelTemplateObjectBuilder withFlagCharacters(String flagCharacters) {
    this.flagCharacters = flagCharacters;
    return this;
  }

  public PackLabelTemplateObjectBuilder withCheckCharacter(String checkCharacter) {
    this.checkCharacter = checkCharacter;
    return this;
  }

  public PackLabelTemplateObjectBuilder withBloodABO(String bloodABO) {
    this.bloodABO = bloodABO;
    return this;
  }

  public PackLabelTemplateObjectBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public PackLabelTemplateObjectBuilder thatIsBloodRhPositive() {
    this.isBloodRhPositive = true;
    return this;
  }

  public PackLabelTemplateObjectBuilder thatIsNotBloodRhPositive() {
    this.isBloodRhPositive = false;
    return this;
  }

  public PackLabelTemplateObjectBuilder thatIsBloodRhNegative() {
    this.isBloodRhNegative = true;
    return this;
  }

  public PackLabelTemplateObjectBuilder thatIsNotBloodRhNegative() {
    this.isBloodRhNegative = false;
    return this;
  }

  public PackLabelTemplateObjectBuilder thatIsBloodHighTitre() {
    this.isBloodHighTitre = true;
    return this;
  }

  public PackLabelTemplateObjectBuilder thatIsNotBloodHighTitre() {
    this.isBloodHighTitre = false;
    return this;
  }

  public PackLabelTemplateObjectBuilder withDonationDate(String donationDate) {
    this.donationDate = donationDate;
    return this;
  }

  public PackLabelTemplateObjectBuilder withDonationDateISO(String donationDateISO) {
    this.donationDateISO = donationDateISO;
    return this;
  }

  public PackLabelTemplateObjectBuilder withComponentCode(String componentCode) {
    this.componentCode = componentCode;
    return this;
  }

  public PackLabelTemplateObjectBuilder withExpiresOn(String expiresOn) {
    this.expiresOn = expiresOn;
    return this;
  }

  public PackLabelTemplateObjectBuilder withExpiresOnISO(String expiresOnISO) {
    this.expiresOnISO = expiresOnISO;
    return this;
  }

  public PackLabelTemplateObjectBuilder withVolume(Integer volume) {
    this.volume = volume;
    return this;
  }

  public PackLabelTemplateObjectBuilder withComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
    return this;
  }

  public PackLabelTemplateObjectBuilder withPreparationInfo(String preparationInfo) {
    this.preparationInfo = preparationInfo;
    return this;
  }

  public PackLabelTemplateObjectBuilder withStorageInfo(String storageInfo) {
    this.storageInfo = storageInfo;
    return this;
  }

  public PackLabelTemplateObjectBuilder withTransportInfo(String transportInfo) {
    this.transportInfo = transportInfo;
    return this;
  }

  @Override
  public PackLabelTemplateObject build() {
    PackLabelTemplateObject template = new PackLabelTemplateObject();

    template.DINPositioning.flagCharPos = flagCharPos;
    template.DINPositioning.boxPos = boxPos;
    template.DINPositioning.checkCharPos = checkCharPos;

    template.config.serviceInfoLine1 = serviceInfoLine1;
    template.config.serviceInfoLine2 = serviceInfoLine2;

    template.donation.DIN = DIN;
    template.donation.flagCharacters = flagCharacters;
    template.donation.checkCharacter = checkCharacter;
    template.donation.bloodABO = bloodABO;
    template.donation.bloodRh = bloodRh;
    template.donation.isBloodRhPositive = isBloodRhPositive;
    template.donation.isBloodRhNegative = isBloodRhNegative;
    template.donation.isBloodHighTitre = isBloodHighTitre;
    template.donation.donationDate = donationDate;
    template.donation.donationDateISO = donationDateISO;

    template.component.componentCode = componentCode;
    template.component.expiresOn = expiresOn;
    template.component.expiresOnISO = expiresOnISO;
    template.component.volume = volume;

    template.componentType.componentTypeName = componentTypeName;
    template.componentType.preparationInfo = preparationInfo;
    template.componentType.storageInfo = storageInfo;
    template.componentType.transportInfo = transportInfo;
    return template;
  }

  public static PackLabelTemplateObjectBuilder aPackLabelTemplateObject() {
    return new PackLabelTemplateObjectBuilder();
  }
}
