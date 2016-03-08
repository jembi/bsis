package bsis.importer;

import static helpers.builders.LocationBuilder.aLocation;
import static helpers.matchers.LocationMatcher.hasSameStateAsLocation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import model.address.AddressType;
import model.address.ContactMethodType;
import model.donor.Donor;
import model.idtype.IdType;
import model.location.Location;
import model.preferredlanguage.PreferredLanguage;
import model.util.Gender;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.joda.time.LocalDate;
import org.joda.time.Years;
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
    assertThat("Middle name matches", "John", equalTo(firstDonor.getMiddleName()));
    assertThat("Title matches", "Mr", equalTo(firstDonor.getTitle()));
    assertThat("Calling name matches", "Dave", equalTo(firstDonor.getCallingName()));
    assertThat("Gender matches", Gender.male, equalTo(firstDonor.getGender()));
    assertThat("Preferred Language matches", "English", equalTo(firstDonor.getPreferredLanguage().getPreferredLanguage()));
    assertThat("BirthDate matches", "1982-12-03", equalTo(CustomDateFormatter.format(firstDonor.getBirthDate())));
    LocalDate now = LocalDate.now();
    LocalDate birthdate1 = new LocalDate(firstDonor.getBirthDate());
    Years age1 = Years.yearsBetween(birthdate1, now);
    assertThat("BirthDate matches", age1.getYears(), equalTo(firstDonor.getAge().intValue()));
    assertThat("BloodAbo matches", "A", equalTo(firstDonor.getBloodAbo()));
    assertThat("BloodRh matches", "+", equalTo(firstDonor.getBloodRh()));
    assertThat("Notes matches", "Imported Donor", equalTo(firstDonor.getNotes()));
    assertThat("Venue matches", "First", equalTo(firstDonor.getVenue().getName()));
    assertThat("Id Type matches", "National Id", equalTo(firstDonor.getIdType().getIdType()));
    assertThat("Id number matches", "8212030162083", equalTo(firstDonor.getIdNumber()));
    assertThat("Preferred contact type matches", "Email", equalTo(firstDonor.getContactMethodType().getContactMethodType()));
    assertThat("Mobile number matches", "0768198075", equalTo(firstDonor.getContact().getMobileNumber()));
    assertThat("Home number matches", "0214615177", equalTo(firstDonor.getContact().getHomeNumber()));
    assertThat("Work number matches", "0217010939", equalTo(firstDonor.getContact().getWorkNumber()));
    assertThat("Email matches", "dave@email.com", equalTo(firstDonor.getContact().getEmail()));
    assertThat("Preferred Address Type matches", "Home", equalTo(firstDonor.getAddressType().getPreferredAddressType()));
    assertThat("Home address line 1 matches", "1 Appartment House", equalTo(firstDonor.getAddress().getHomeAddressLine1()));
    assertThat("Home address line 2 matches", "123 Street", equalTo(firstDonor.getAddress().getHomeAddressLine2()));
    assertThat("Home address city matches", "Cape Town", equalTo(firstDonor.getAddress().getHomeAddressCity()));
    assertThat("Home address province matches", "Western Cape", equalTo(firstDonor.getAddress().getHomeAddressProvince()));
    assertThat("Home address district matches", "Gardens", equalTo(firstDonor.getAddress().getHomeAddressDistrict()));
    assertThat("Home address state matches", "Western Cape", equalTo(firstDonor.getAddress().getHomeAddressState()));
    assertThat("Home address country matches", "South Africa", equalTo(firstDonor.getAddress().getHomeAddressCountry()));
    assertThat("Home address zipcode matches", "8001", equalTo(firstDonor.getAddress().getHomeAddressZipcode()));
    assertThat("Work address line 1 matches", "Unit D11 Westlake Square", equalTo(firstDonor.getAddress().getWorkAddressLine1()));
    assertThat("Work address line 2 matches", "Westlake Drive", equalTo(firstDonor.getAddress().getWorkAddressLine2()));
    assertThat("Work address city matches", "Westlake", equalTo(firstDonor.getAddress().getWorkAddressCity()));
    assertThat("Work address province matches", "Western Cape", equalTo(firstDonor.getAddress().getWorkAddressProvince()));
    assertThat("Work address district matches", "Gardens", equalTo(firstDonor.getAddress().getWorkAddressDistrict()));
    assertThat("Work address state matches", "Western Cape", equalTo(firstDonor.getAddress().getWorkAddressState()));
    assertThat("Work address country matches", "South Africa", equalTo(firstDonor.getAddress().getWorkAddressCountry()));
    assertThat("Work address zipcode matches", "8001", equalTo(firstDonor.getAddress().getWorkAddressZipcode()));
    assertThat("Postal address line 1 matches", "P.O. Box 12345", equalTo(firstDonor.getAddress().getPostalAddressLine1()));
    assertThat("Postal address line 2 matches", "The Post Office", equalTo(firstDonor.getAddress().getPostalAddressLine2()));
    assertThat("Postal address city matches", "Vlaeberg", equalTo(firstDonor.getAddress().getPostalAddressCity()));
    assertThat("Postal address province matches", "Western Cape", equalTo(firstDonor.getAddress().getPostalAddressProvince()));
    assertThat("Postal address district matches", "Gardens", equalTo(firstDonor.getAddress().getPostalAddressDistrict()));
    assertThat("Postal address state matches", "Western Cape", equalTo(firstDonor.getAddress().getPostalAddressState()));
    assertThat("Postal address country matches", "South Africa", equalTo(firstDonor.getAddress().getPostalAddressCountry()));
    assertThat("Postal address zipcode matches", "8018", equalTo(firstDonor.getAddress().getPostalAddressZipcode()));
    
    // Second donor
    assertThat("Middle name matches", "Mary", equalTo(secondDonor.getMiddleName()));
    assertThat("Title matches", "Mrs", equalTo(secondDonor.getTitle()));
    assertThat("Calling name matches", "Janey", equalTo(secondDonor.getCallingName()));
    assertThat("Gender matches", Gender.female, equalTo(secondDonor.getGender()));
    assertThat("Preferred Language matches", "English", equalTo(secondDonor.getPreferredLanguage().getPreferredLanguage()));
    assertThat("BirthDate matches", "1972-10-03", equalTo(CustomDateFormatter.format(secondDonor.getBirthDate())));
    LocalDate birthdate2 = new LocalDate(firstDonor.getBirthDate());
    Years age2 = Years.yearsBetween(birthdate2, now);
    assertThat("BirthDate matches", age2.getYears(), equalTo(secondDonor.getAge().intValue()));
    assertThat("BloodAbo matches", "B", equalTo(secondDonor.getBloodAbo()));
    assertThat("BloodRh matches", "+", equalTo(secondDonor.getBloodRh()));
    assertThat("Venue matches", "Second", equalTo(secondDonor.getVenue().getName()));
    assertThat("Id number matches", "7210030162086", equalTo(secondDonor.getIdNumber()));
  }
  
  private Donor findDonorByName(String firstName, String lastName) {
    return entityManager.createQuery("SELECT d FROM Donor d WHERE d.firstName = :firstName and d.lastName = :lastName", Donor.class)
        .setParameter("firstName", firstName)
        .setParameter("lastName", lastName)
        .getSingleResult();
  }
}
