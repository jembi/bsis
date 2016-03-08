package bsis.importer;

import static helpers.builders.LocationBuilder.aLocation;
import static helpers.matchers.LocationMatcher.hasSameStateAsLocation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import helpers.builders.FormFieldBuilder;

import java.io.FileInputStream;
import java.io.IOException;

import model.address.AddressType;
import model.address.ContactMethodType;
import model.admin.FormField;
import model.admin.GeneralConfig;
import model.donor.Donor;
import model.idtype.IdType;
import model.location.Location;
import model.preferredlanguage.PreferredLanguage;
import model.util.Gender;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.ContextDependentTestSuite;
import utils.CustomDateFormatter;

public class DataImportServiceTests extends ContextDependentTestSuite {
  
  @Autowired
  private DataImportService dataImportService;
  
  @Before
  public void setUpTest() throws EncryptedDocumentException, InvalidFormatException, IOException {
    // Set up fixture
    FileInputStream fileInputStream = new FileInputStream("test/fixtures/BSIS-import.xlsx");
    Workbook workbook = WorkbookFactory.create(fileInputStream);
    
    // Set up test date (Donor)
    IdType nationalId = new IdType();
    nationalId.setIdType("National Id");
    entityManager.persist(nationalId);
    ContactMethodType email = new ContactMethodType();
    email.setContactMethodType("Email");
    entityManager.persist(email);
    AddressType home = new AddressType();
    home.setPreferredAddressType("Home");
    entityManager.persist(home);
    PreferredLanguage english = new PreferredLanguage();
    english.setPreferredLanguage("English");
    entityManager.persist(english);
    PreferredLanguage afrikaans = new PreferredLanguage();
    afrikaans.setPreferredLanguage("Afrikaans");
    entityManager.persist(afrikaans);
    GeneralConfig donorNumberGeneralConfig = new GeneralConfig();
    donorNumberGeneralConfig.setName("donor.donorNumberFormat");
    donorNumberGeneralConfig.setValue("%06d");
    entityManager.persist(donorNumberGeneralConfig);
    FormFieldBuilder.aFormField().withForm("donor").withField("donorNumber")
        .withAutoGenerate(true).withMaxLength(15)
        .buildAndPersist(entityManager);
    
    // Exercise SUT
    dataImportService.importData(workbook, false);
  }

  @Test
  public void testImportLocationData_shouldCreateLocationsFromSpreadsheet() {
    // Verify
    Location firstLocation = findLocationByName("First");
    Location secondLocation = findLocationByName("Second");
    Location thirdLocation = findLocationByName("Third");
    Location expectedFirstLocation = aLocation()
        .withId(firstLocation.getId())
        .withName("First")
        .withNotes("First Location")
        .thatIsVenue()
        .build();
    Location expectedSecondLocation = aLocation()
        .withId(secondLocation.getId())
        .withName("Second")
        .thatIsMobileSite()
        .thatIsDeleted()
        .build();
    Location expectedThirdLocation = aLocation()
        .withId(thirdLocation.getId())
        .withName("Third")
        .thatIsUsageSite()
        .build();
    
    assertThat(firstLocation, hasSameStateAsLocation(expectedFirstLocation));
    assertThat(secondLocation, hasSameStateAsLocation(expectedSecondLocation));
    assertThat(thirdLocation, hasSameStateAsLocation(expectedThirdLocation));
  }
  
  private Location findLocationByName(String name) {
    return entityManager.createQuery("SELECT l FROM Location l WHERE l.name = :name", Location.class)
        .setParameter("name", name)
        .getSingleResult();
  }

  @Test
  public void testImportDonorData_shouldCreateDonorsFromSpreadsheet()
      throws EncryptedDocumentException, InvalidFormatException, IOException {
   
    // Verify
    Donor firstDonor = findDonorByName("David", "Smith");
    Donor secondDonor = findDonorByName("Jane", "Doe");
    
    // first Donor
    assertThat("Middle name matches", firstDonor.getMiddleName(), equalTo("John"));
    assertThat("Title matches", firstDonor.getTitle(), equalTo("Mr"));
    assertThat("Calling name matches", firstDonor.getCallingName(), equalTo("Dave"));
    assertThat("Gender matches", firstDonor.getGender(), equalTo(Gender.male));
    assertThat("Preferred Language matches", firstDonor.getPreferredLanguage().getPreferredLanguage(), equalTo("English"));
    assertThat("BirthDate matches", CustomDateFormatter.format(firstDonor.getBirthDate()), equalTo("1982-12-03"));
    assertThat("BloodAbo matches", firstDonor.getBloodAbo(), equalTo("A"));
    assertThat("BloodRh matches", firstDonor.getBloodRh(), equalTo("+"));
    assertThat("Notes matches", firstDonor.getNotes(), equalTo("Imported Donor"));
    assertThat("Venue matches", firstDonor.getVenue().getName(), equalTo("First"));
    assertThat("Id Type matches", firstDonor.getIdType().getIdType(), equalTo("National Id"));
    assertThat("Id number matches", firstDonor.getIdNumber(), equalTo("8212030162083"));
    assertThat("Preferred contact type matches", firstDonor.getContactMethodType().getContactMethodType(), equalTo("Email"));
    assertThat("Mobile number matches", firstDonor.getContact().getMobileNumber(), equalTo("0768198075"));
    assertThat("Home number matches", firstDonor.getContact().getHomeNumber(), equalTo("0214615177"));
    assertThat("Work number matches", firstDonor.getContact().getWorkNumber(), equalTo("0217010939"));
    assertThat("Email matches", firstDonor.getContact().getEmail(), equalTo("dave@email.com"));
    assertThat("Preferred Address Type matches", firstDonor.getAddressType().getPreferredAddressType(), equalTo("Home"));
    assertThat("Home address line 1 matches", firstDonor.getAddress().getHomeAddressLine1(), equalTo("1 Appartment House"));
    assertThat("Home address line 2 matches", firstDonor.getAddress().getHomeAddressLine2(), equalTo("123 Street"));
    assertThat("Home address city matches", firstDonor.getAddress().getHomeAddressCity(), equalTo("Cape Town"));
    assertThat("Home address province matches", firstDonor.getAddress().getHomeAddressProvince(), equalTo("Western Cape"));
    assertThat("Home address district matches", firstDonor.getAddress().getHomeAddressDistrict(), equalTo("Gardens"));
    assertThat("Home address state matches", firstDonor.getAddress().getHomeAddressState(), equalTo("Western Cape"));
    assertThat("Home address country matches", firstDonor.getAddress().getHomeAddressCountry(), equalTo("South Africa"));
    assertThat("Home address zipcode matches", firstDonor.getAddress().getHomeAddressZipcode(), equalTo("8001"));
    assertThat("Work address line 1 matches", firstDonor.getAddress().getWorkAddressLine1(), equalTo("Unit D11 Westlake Square"));
    assertThat("Work address line 2 matches", firstDonor.getAddress().getWorkAddressLine2(), equalTo("Westlake Drive"));
    assertThat("Work address city matches", firstDonor.getAddress().getWorkAddressCity(), equalTo("Westlake"));
    assertThat("Work address province matches", firstDonor.getAddress().getWorkAddressProvince(), equalTo("Western Cape"));
    assertThat("Work address district matches", firstDonor.getAddress().getWorkAddressDistrict(), equalTo("Gardens"));
    assertThat("Work address state matches", firstDonor.getAddress().getWorkAddressState(), equalTo("Western Cape"));
    assertThat("Work address country matches", firstDonor.getAddress().getWorkAddressCountry(), equalTo("South Africa"));
    assertThat("Work address zipcode matches", firstDonor.getAddress().getWorkAddressZipcode(), equalTo("8001"));
    assertThat("Postal address line 1 matches", firstDonor.getAddress().getPostalAddressLine1(), equalTo("P.O. Box 12345"));
    assertThat("Postal address line 2 matches", firstDonor.getAddress().getPostalAddressLine2(), equalTo("The Post Office"));
    assertThat("Postal address city matches", firstDonor.getAddress().getPostalAddressCity(), equalTo("Vlaeberg"));
    assertThat("Postal address province matches", firstDonor.getAddress().getPostalAddressProvince(), equalTo("Western Cape"));
    assertThat("Postal address district matches", firstDonor.getAddress().getPostalAddressDistrict(), equalTo("Gardens"));
    assertThat("Postal address state matches", firstDonor.getAddress().getPostalAddressState(), equalTo("Western Cape"));
    assertThat("Postal address country matches", firstDonor.getAddress().getPostalAddressCountry(), equalTo("South Africa"));
    assertThat("Postal address zipcode matches", firstDonor.getAddress().getPostalAddressZipcode(), equalTo("8018"));
    
    // Second donor
    assertThat("Middle name matches", secondDonor.getMiddleName(), equalTo("Mary"));
    assertThat("Title matches", secondDonor.getTitle(), equalTo("Mrs"));
    assertThat("Calling name matches", secondDonor.getCallingName(), equalTo("Janey"));
    assertThat("Gender matches", secondDonor.getGender(), equalTo(Gender.female));
    assertThat("Preferred Language matches", secondDonor.getPreferredLanguage().getPreferredLanguage(), equalTo("Afrikaans"));
    assertThat("BirthDate matches", CustomDateFormatter.format(secondDonor.getBirthDate()), equalTo("1972-10-03"));
    assertThat("BloodAbo matches", secondDonor.getBloodAbo(), equalTo("B"));
    assertThat("BloodRh matches", secondDonor.getBloodRh(), equalTo("+"));
    assertThat("Venue matches", secondDonor.getVenue().getName(), equalTo("Second"));
    assertThat("Id number matches", secondDonor.getIdNumber(), equalTo("7210030162086"));
  }
  
  private Donor findDonorByName(String firstName, String lastName) {
    return entityManager.createQuery("SELECT d FROM Donor d WHERE d.firstName = :firstName and d.lastName = :lastName", Donor.class)
        .setParameter("firstName", firstName)
        .setParameter("lastName", lastName)
        .getSingleResult();
  }
}
