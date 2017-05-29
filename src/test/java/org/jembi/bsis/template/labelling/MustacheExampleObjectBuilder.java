package org.jembi.bsis.template.labelling;

public class MustacheExampleObjectBuilder {
  private boolean showIdNumber;
  private boolean doNotShowGender;
  public String donorName;
  public String idNumber;
  public String donorGender;

  public MustacheExampleObjectBuilder withShowIdNumber(boolean showIdNumber) {
    this.showIdNumber = showIdNumber;
    return this;
  }

  public MustacheExampleObjectBuilder withDoNotShowGender(boolean doNotShowGender) {
    this.doNotShowGender = doNotShowGender;
    return this;
  }

  public MustacheExampleObjectBuilder withDonorName(String donorName) {
    this.donorName=donorName;
    return this;
  }

  public MustacheExampleObjectBuilder withDonorIdNumber(String idNumber) {
    this.idNumber=idNumber;
    return this;
  }

  public MustacheExampleObjectBuilder withDonorGender(String donorGender) {
    this.donorGender=donorGender;
    return this;
  }

  public MustacheExample build() {
    MustacheExample mustacheExample = new MustacheExample();
    mustacheExample.showIdNumber = showIdNumber;
    mustacheExample.doNotShowGender = doNotShowGender;
    mustacheExample.setDonorGender(donorGender);
    mustacheExample.setDonorIdNumber(idNumber);
    mustacheExample.setDonorName(donorName);
    return mustacheExample;
  }

  public static MustacheExampleObjectBuilder aMustacheExampleObjectBuilder() {
    return new MustacheExampleObjectBuilder();
  }
}
