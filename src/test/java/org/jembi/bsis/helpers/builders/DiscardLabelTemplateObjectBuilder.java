package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.template.labelling.DiscardLabelTemplateObject;

public class DiscardLabelTemplateObjectBuilder extends AbstractBuilder<DiscardLabelTemplateObject> {
  private String componentCode;
  private String componentTypeCode;
  private String DIN;
  private String serviceInfoLine1;
  private String serviceInfoLine2;

  public DiscardLabelTemplateObjectBuilder withComponentCode(String componentCode) {
    this.componentCode = componentCode;
    return this;
  }

  public DiscardLabelTemplateObjectBuilder withComponentTypeCode(String componentTypeCode) {
    this.componentTypeCode = componentTypeCode;
    return this;
  }

  public DiscardLabelTemplateObjectBuilder withDIN(String DIN) {
    this.DIN = DIN;
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
    template.component.componentCode = componentCode;
    template.componentType.componentTypeCode = componentTypeCode;
    template.donation.DIN = DIN;
    template.config.serviceInfoLine1 = serviceInfoLine1;
    template.config.serviceInfoLine2 = serviceInfoLine2;
    return template;
  }

  public static DiscardLabelTemplateObjectBuilder aDiscardLabelTemplateObject() {
    return new DiscardLabelTemplateObjectBuilder();
  }
}
