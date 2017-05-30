package org.jembi.bsis.template.labelling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.jembi.bsis.template.labelling.ExampleTemplateObjectBuilder.anExampleTemplateObjectBuilder;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.jembi.bsis.template.TemplateEngine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TemplateEngineTests extends ContextDependentTestSuite {
  
  @Autowired
  TemplateEngine templateEngine;

  private String template = "Donor Information\n"
      + "-----------------\n"
      + "Name: {{donor.name}}\n"
      + "{{#showIdNumber}}\n"
      + "ID: {{donor.idNumber}}\n"
      + "{{/showIdNumber}}\n"
      + "{{^doNotShowGender}}\n"
      + "Gender: {{donor.gender}}\n"
      + "{{/doNotShowGender}}";

  @Test
  public void testMustacheExecuteWithGenderAndIDNumberDisplayed_shouldReturnAllInforamtion() throws Exception {
    String donorName = "Donor 1";
    String idNumber = "123";
    String donorGender = "male";

    String output = templateEngine.execute(template,
        anExampleTemplateObjectBuilder()
            .withDoNotShowGender(false)
            .withShowIdNumber(true)
            .withDonorName(donorName)
            .withDonorGender(donorGender)
            .withDonorIdNumber(idNumber)
            .build());

    assertThat(output, containsString("Name: " + donorName));
    assertThat(output, containsString("ID: " + idNumber));
    assertThat(output, containsString("Gender: " + donorGender));
  }

  @Test
  public void testMustacheExecuteWithGenderButNoIdNumberDisplayed_shouldReturnNameAndGender() throws Exception {
    String donorName = "Donor 1";
    String idNumber = "123";
    String donorGender = "male";

    String output = templateEngine.execute(template, 
        anExampleTemplateObjectBuilder()
            .withDoNotShowGender(false)
            .withShowIdNumber(false)
            .withDonorName(donorName)
            .withDonorGender(donorGender)
            .withDonorIdNumber(idNumber)
            .build());

    assertThat(output, containsString("Name: " + donorName));
    assertThat(output, not(containsString("ID: " + idNumber)));
    assertThat(output, containsString("Gender: " + donorGender));
  }

  @Test
  public void testMustacheExecuteWithIdNumberButNoGenderDisplayed_shouldReturnNameAndIdNumber() throws Exception {
    String donorName = "Donor 1";
    String idNumber = "123";
    String donorGender = "male";

    String output = templateEngine.execute(template, 
        anExampleTemplateObjectBuilder()
            .withDoNotShowGender(true)
            .withShowIdNumber(true)
            .withDonorName(donorName)
            .withDonorGender(donorGender)
            .withDonorIdNumber(idNumber)
            .build());

    assertThat(output, containsString("Name: " + donorName));
    assertThat(output, containsString("ID: " + idNumber));
    assertThat(output, not(containsString("Gender: " + donorGender)));
  }

  @Test
  public void testMustacheExecuteWithNoIdNumberAndNoGenderDisplayed_shouldReturnNameOnly() throws Exception {
    String donorName = "Donor 1";
    String idNumber = "123";
    String donorGender = "male";

    String output = templateEngine.execute(template, 
        anExampleTemplateObjectBuilder()
            .withDoNotShowGender(true)
            .withShowIdNumber(false)
            .withDonorName(donorName)
            .withDonorGender(donorGender)
            .withDonorIdNumber(idNumber)
            .build());

    assertThat(output, containsString("Name: " + donorName));
    assertThat(output, not(containsString("ID: " + idNumber)));
    assertThat(output, not(containsString("Gender: " + donorGender)));
  }  
}
