package org.jembi.bsis.template.labelling;

public class DiscardLabelTemplateObject {

  public DonationTemplateObject donation = new DonationTemplateObject();
  public ComponentTemplateObject component = new ComponentTemplateObject();
  public ComponentTypeTemplateObject componentType = new ComponentTypeTemplateObject();
  public ConfigTemplateObject config = new ConfigTemplateObject();

  public class ComponentTemplateObject {
    public String componentCode;
  }

  public class ComponentTypeTemplateObject {
    public String componentTypeCode;
  }

  public class ConfigTemplateObject {
    public String serviceInfoLine1, serviceInfoLine2;
  }

  public class DonationTemplateObject {
    public String DIN;
  }
}
