package org.jembi.bsis.template.labelling;

public class DiscardLabelTemplateObject {

  public ComponentTemplateObject component = new ComponentTemplateObject();
  public ConfigTemplateObject config = new ConfigTemplateObject();

  public class ComponentTemplateObject {
    public String componentCode;
  }

  public class ConfigTemplateObject {
    public String serviceInfoLine1, serviceInfoLine2;
  }

}
