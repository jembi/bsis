package org.jembi.bsis.model.bloodtesting.rules;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Assert;
import org.junit.Test;


public class DonationFieldTests extends UnitTestSuite {

  @Test
  public void testGetDonationFieldsForCategoryTTI_returnsCorrectEnums() throws Exception {
    List<DonationField> expectedFields = Arrays.asList(
        DonationField.TTISTATUS);
    
    List<DonationField> fields = DonationField.getDonationFieldsForCategory(BloodTestCategory.TTI);
    
    assertThat(fields, is(expectedFields));
  }

  @Test
  public void testGetDonationFieldsForCategoryBloodTyping_returnsCorrectEnums() throws Exception {
    List<DonationField> expectedFields = Arrays.asList(
        DonationField.BLOODABO, 
        DonationField.BLOODRH,
        DonationField.TITRE);

    List<DonationField> fields = DonationField.getDonationFieldsForCategory(BloodTestCategory.BLOODTYPING);
    
    assertThat(fields, is(expectedFields));
  }

  @Test
  public void testGetNewInformationForDonationFieldForBloodAbo_returnsCorrectStrings() throws Exception {
    List<String> expectedNewInformation = Arrays.asList("A","B","AB","O"); 
    List<String> newInformation = DonationField.getNewInformationForDonationField(DonationField.BLOODABO);
    assertThat(newInformation, is(expectedNewInformation));
  }

  @Test
  public void testGetNewInformationForDonationFieldForBloodRh_returnsCorrectStrings() throws Exception {
    List<String> expectedNewInformation = Arrays.asList("+","-"); 
    List<String> newInformation = DonationField.getNewInformationForDonationField(DonationField.BLOODRH);    
    assertThat(newInformation, is(expectedNewInformation));
  }

  @Test
  public void testGetNewInformationForDonationFieldForTTIStatus_returnsCorrectStrings() throws Exception {
    List<String> expectedNewInformation = Arrays.asList("NOT_DONE","SAFE", "UNSAFE", "INDETERMINATE"); 
    List<String> newInformation = DonationField.getNewInformationForDonationField(DonationField.TTISTATUS);    
    assertThat(newInformation, is(expectedNewInformation));
  }

  @Test
  public void testGetNewInformationForDonationFieldForTitre_returnsCorrectStrings() throws Exception {
    List<String> expectedNewInformation = Arrays.asList("HIGH", "LOW");
    List<String> newInformation = DonationField.getNewInformationForDonationField(DonationField.TITRE);    
    assertThat(newInformation, is(expectedNewInformation));
  }

  @Test
  public void testGetNewInformationForNullDonationField_returnsEmptyNewInformation() throws Exception {
    List<String> newInformation = DonationField.getNewInformationForDonationField(null);
    Assert.assertTrue(newInformation.isEmpty());
  }
}
