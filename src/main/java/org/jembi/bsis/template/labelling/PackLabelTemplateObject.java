package org.jembi.bsis.template.labelling;

public class PackLabelTemplateObject {

  public DINPositioningTemplateObject DINPositioning = new DINPositioningTemplateObject();
  public ConfigTemplateObject config = new ConfigTemplateObject();
  public DonationTemplateObject donation = new DonationTemplateObject();
  public ComponentTemplateObject component = new ComponentTemplateObject();
  public ComponentTypeTemplateObject componentType = new ComponentTypeTemplateObject();

  public class ComponentTemplateObject {
    public String componentCode, expiresOn, expiresOnISO;
    public Integer volume;
  }

  public class ConfigTemplateObject {
    public String serviceInfoLine1, serviceInfoLine2;
  }

  public class DINPositioningTemplateObject {
    public int flagCharPos, boxPos, checkCharPos;
  }

  public class DonationTemplateObject {
    public String DIN, flagCharacters, checkCharacter, bloodABO, bloodRh, donationDate, donationDateISO;
    public boolean isBloodRhPositive, isBloodRhNegative, isBloodHighTitre;
  }

  public class ComponentTypeTemplateObject {
    public String componentTypeName, preparationInfo, storageInfo, transportInfo;
  }
}
