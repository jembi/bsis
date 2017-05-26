package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.template.DiscardLabelTemplateObject;

public class DiscardLabelTemplateObjectBuilder extends AbstractBuilder<DiscardLabelTemplateObject> {
  private String componentCode;
  private String serviceInfoLine1;
  private String serviceInfoLine2;

  public DiscardLabelTemplateObjectBuilder withComponentCode(String componentCode) {
    this.componentCode = componentCode;
    return this;
  }

  public DiscardLabelTemplateObjectBuilder withServiceInfoLine1(String serviceInfoLine1) {
    this.serviceInfoLine1 = serviceInfoLine1;
    return this;
  }

  public DiscardLabelTemplateObjectBuilder withServiceInfoLine2(String serviceInfoLine2) {
    this.serviceInfoLine2 = serviceInfoLine2;
    return this;
  }

  @Override
  public DiscardLabelTemplateObject build() {
    DiscardLabelTemplateObject template = new DiscardLabelTemplateObject();
    template.setComponentCode(componentCode);
    template.setServiceInfoLine1(serviceInfoLine1);
    template.setServiceInfoLine2(serviceInfoLine2);
    return template;
  }

  public static DiscardLabelTemplateObjectBuilder aDiscardLabelTemplateObject() {
    return new DiscardLabelTemplateObjectBuilder();
  }
}
