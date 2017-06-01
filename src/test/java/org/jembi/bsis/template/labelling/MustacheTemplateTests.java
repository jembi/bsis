package org.jembi.bsis.template.labelling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.jembi.bsis.template.labelling.MustacheExampleObjectBuilder.aMustacheExampleObjectBuilder;

import java.io.StringWriter;

import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class MustacheTemplateTests extends UnitTestSuite {

  MustacheFactory mf = new DefaultMustacheFactory();
  Mustache mustache = mf.compile("src/test/resources/templates/template.mustache");

  @Test
  public void testMustacheExecuteWithGenderAndIDNumberDisplayed_shouldReturnAllInforamtion() throws Exception {
    String donorName = "Donor 1";
    String idNumber = "123";
    String donorGender = "male";

    StringWriter writer = new StringWriter();
    mustache.execute(writer,
        aMustacheExampleObjectBuilder()
            .withDoNotShowGender(false)
            .withShowIdNumber(true)
            .withDonorName(donorName)
            .withDonorGender(donorGender)
            .withDonorIdNumber(idNumber)
            .build())
        .flush();

    String mustacheOutput = writer.toString();
    assertThat(mustacheOutput, containsString("Name: " + donorName));
    assertThat(mustacheOutput, containsString("ID: " + idNumber));
    assertThat(mustacheOutput, containsString("Gender: " + donorGender));
  }

  @Test
  public void testMustacheExecuteWithGenderButNoIdNumberDisplayed_shouldReturnNameAndGender() throws Exception {
    String donorName = "Donor 1";
    String idNumber = "123";
    String donorGender = "male";

    StringWriter writer = new StringWriter();
    mustache.execute(writer, 
        aMustacheExampleObjectBuilder()
            .withDoNotShowGender(false)
            .withShowIdNumber(false)
            .withDonorName(donorName)
            .withDonorGender(donorGender)
            .withDonorIdNumber(idNumber)
            .build())
        .flush();

    String mustacheOutput = writer.toString();
    assertThat(mustacheOutput, containsString("Name: " + donorName));
    assertThat(mustacheOutput, not(containsString("ID: " + idNumber)));
    assertThat(mustacheOutput, containsString("Gender: " + donorGender));
  }

  @Test
  public void testMustacheExecuteWithIdNumberButNoGenderDisplayed_shouldReturnNameAndIdNumber() throws Exception {
    String donorName = "Donor 1";
    String idNumber = "123";
    String donorGender = "male";

    StringWriter writer = new StringWriter();
    mustache.execute(writer, 
        aMustacheExampleObjectBuilder()
            .withDoNotShowGender(true)
            .withShowIdNumber(true)
            .withDonorName(donorName)
            .withDonorGender(donorGender)
            .withDonorIdNumber(idNumber)
            .build())
        .flush();

    String mustacheOutput = writer.toString();
    assertThat(mustacheOutput, containsString("Name: " + donorName));
    assertThat(mustacheOutput, containsString("ID: " + idNumber));
    assertThat(mustacheOutput, not(containsString("Gender: " + donorGender)));
  }

  @Test
  public void testMustacheExecuteWithNoIdNumberAndNoGenderDisplayed_shouldReturnNameOnly() throws Exception {
    String donorName = "Donor 1";
    String idNumber = "123";
    String donorGender = "male";

    StringWriter writer = new StringWriter();
    mustache.execute(writer, 
        aMustacheExampleObjectBuilder()
            .withDoNotShowGender(true)
            .withShowIdNumber(false)
            .withDonorName(donorName)
            .withDonorGender(donorGender)
            .withDonorIdNumber(idNumber)
            .build())
        .flush();

    String mustacheOutput = writer.toString();
    assertThat(mustacheOutput, containsString("Name: " + donorName));
    assertThat(mustacheOutput, not(containsString("ID: " + idNumber)));
    assertThat(mustacheOutput, not(containsString("Gender: " + donorGender)));
  }  
}
