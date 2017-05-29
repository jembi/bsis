package org.jembi.bsis.template;

public class PackLabelTemplateObject {

  public DINPositioningTemplateObject DINPositioning = new DINPositioningTemplateObject();
  public ConfigTemplateObject config = new ConfigTemplateObject();
  public DonationTemplateObject donation = new DonationTemplateObject();
  public ComponentTemplateObject component = new ComponentTemplateObject();
  public ComponentTypeTemplateObject componentType = new ComponentTypeTemplateObject();

  public class ComponentTemplateObject {
    String componentCode, expiresOn, expiresOnISO;
    Integer volume;
  }

  public class ConfigTemplateObject {
    String serviceInfoLine1, serviceInfoLine2;
  }

  public class DINPositioningTemplateObject {
    int flagCharPos, boxPos, checkCharPos;
  }

  public class DonationTemplateObject {
    String DIN, flagCharacters, checkCharacter, bloodABO, bloodRh, donationDate, donationDateISO;
    boolean bloodPositive, bloodHighTitre;
  }

  public class ComponentTypeTemplateObject {
    String componentTypeName, preparationInfo, storageInfo, transportInfo;
  }
}
