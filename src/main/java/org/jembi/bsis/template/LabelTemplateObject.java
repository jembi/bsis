package org.jembi.bsis.template;

public class LabelTemplateObject {

  public Component component = new Component();
  public Config config = new Config();

  public void setComponentCode(String componentCode) {
    this.component.componentCode = componentCode;
  }

  public void setServiceInfoLine1(String serviceInfoLine1) {
    this.config.serviceInfoLine1 = serviceInfoLine1;
  }

  public void setServiceInfoLine2(String serviceInfoLine2) {
    this.config.serviceInfoLine2 = serviceInfoLine2;
  }

  public String getComponentCode() {
    return this.component.componentCode;
  }

  public String getServiceInfoLine1() {
    return this.config.serviceInfoLine1;
  }

  public String getServiceInfoLine2() {
    return this.config.serviceInfoLine2;
  }

  static class Component {
    String componentCode;
  }

  static class Config {
    String serviceInfoLine1, serviceInfoLine2;
  }

}
