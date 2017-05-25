package org.jembi.bsis.templates;

import org.jembi.bsis.templates.MustacheExample.DonorTemplateObject;

public class MustacheExampleObjectBuilder {
    private boolean showIdNumber;
    private boolean doNotShowGender;
    private DonorTemplateObject donor;

    public MustacheExampleObjectBuilder withShowIdNumber(boolean showIdNumber) {
      this.showIdNumber = showIdNumber;
      return this;
    }

    public MustacheExampleObjectBuilder withDoNotShowGender(boolean doNotShowGender) {
      this.doNotShowGender = doNotShowGender;
      return this;
    }

    public MustacheExampleObjectBuilder withDonor(DonorTemplateObject donor) {
      this.donor = donor;
      return this;
    }

    public MustacheExample build() {
      MustacheExample mustacheExample = new MustacheExample();
      mustacheExample.showIdNumber = showIdNumber;
      mustacheExample.doNotShowGender = doNotShowGender;
      mustacheExample.donor = donor;
      return mustacheExample;
    }
    
    public static MustacheExampleObjectBuilder aMustacheExampleObjectBuilder() {
      return new MustacheExampleObjectBuilder();
    }
  }