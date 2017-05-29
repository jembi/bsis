package org.jembi.bsis.template.labelling;

public class ExampleTemplateObjectBuilder {
  private boolean showIdNumber;
  private boolean doNotShowGender;
  public String donorName;
  public String idNumber;
  public String donorGender;

  public ExampleTemplateObjectBuilder withShowIdNumber(boolean showIdNumber) {
    this.showIdNumber = showIdNumber;
    return this;
  }

  public ExampleTemplateObjectBuilder withDoNotShowGender(boolean doNotShowGender) {
    this.doNotShowGender = doNotShowGender;
    return this;
  }

  public ExampleTemplateObjectBuilder withDonorName(String donorName) {
    this.donorName=donorName;
    return this;
  }

  public ExampleTemplateObjectBuilder withDonorIdNumber(String idNumber) {
    this.idNumber=idNumber;
    return this;
  }

  public ExampleTemplateObjectBuilder withDonorGender(String donorGender) {
    this.donorGender=donorGender;
    return this;
  }

  public ExampleTemplateObject build() {
    ExampleTemplateObject exampleTemplateObject = new ExampleTemplateObject();
    exampleTemplateObject.showIdNumber = showIdNumber;
    exampleTemplateObject.doNotShowGender = doNotShowGender;
    exampleTemplateObject.setDonorGender(donorGender);
    exampleTemplateObject.setDonorIdNumber(idNumber);
    exampleTemplateObject.setDonorName(donorName);
    return exampleTemplateObject;
  }

  public static ExampleTemplateObjectBuilder anExampleTemplateObjectBuilder() {
    return new ExampleTemplateObjectBuilder();
  }
}
