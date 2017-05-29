package org.jembi.bsis.template;

public class DiscardLabelTemplateObject {

  public ComponentTemplateObject component = new ComponentTemplateObject();
  public ConfigTemplateObject config = new ConfigTemplateObject();

  public class ComponentTemplateObject {
    String componentCode;

    public String getComponentCode() {
      return componentCode;
    }

    public void setComponentCode(String componentCode) {
      this.componentCode = componentCode;
    }
  }

  public class ConfigTemplateObject {
    String serviceInfoLine1, serviceInfoLine2;

    public String getServiceInfoLine1() {
      return serviceInfoLine1;
    }

    public void setServiceInfoLine1(String serviceInfoLine1) {
      this.serviceInfoLine1 = serviceInfoLine1;
    }

    public String getServiceInfoLine2() {
      return serviceInfoLine2;
    }

    public void setServiceInfoLine2(String serviceInfoLine2) {
      this.serviceInfoLine2 = serviceInfoLine2;
    }
  }

}
