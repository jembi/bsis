package bsis.importer;

import static helpers.builders.LocationBuilder.aLocation;
import static helpers.matchers.LocationMatcher.hasSameStateAsLocation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import helpers.builders.AdverseEventTypeBuilder;
import helpers.builders.DonationTypeBuilder;
import helpers.builders.FormFieldBuilder;
import helpers.builders.PackTypeBuilder;
import helpers.builders.UserBuilder;
import model.address.AddressType;
import model.address.ContactMethodType;
import model.admin.DataType;
import model.admin.GeneralConfig;
import model.componenttype.ComponentType;
import model.componenttype.ComponentTypeTimeUnits;
import model.donation.Donation;
import model.donation.HaemoglobinLevel;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.idtype.IdType;
import model.location.Location;
import model.preferredlanguage.PreferredLanguage;
import model.util.Gender;
import suites.ContextDependentTestSuite;

public class DataImportServiceTests extends ContextDependentTestSuite {
  
  @Autowired
  private DataImportService dataImportService;
  
  @Test
  public void testDataImport() throws EncryptedDocumentException, InvalidFormatException, IOException {
    // Set up fixture and data
    FileInputStream fileInputStream = new FileInputStream("test/fixtures/BSIS-import.xlsx");
    Workbook workbook = WorkbookFactory.create(fileInputStream);
    createSupportingTestData();

    // Exercise SUT
    dataImportService.importData(workbook, "superuser", false);
    
    // Ensure stale entities are cleared
    entityManager.clear();
    
    // Assert data is correct
    assertImportLocationData_shouldCreateLocationsFromSpreadsheet();
    assertImportDonorData_shouldCreateDonorsFromSpreadsheet();
    assertImportDonationData_shouldCreateDonationsFromSpreadsheet();
  }
  
  private void createSupportingTestData() {
    // Setup user
    UserBuilder.aUser().withUsername("superuser").thatIsNotDeleted().buildAndPersist(entityManager);

    // Setup test data for Donor
    IdType nationalId = new IdType();
    nationalId.setIdType("National Id");
    entityManager.persist(nationalId);
    ContactMethodType email = new ContactMethodType();
    email.setContactMethodType("Email");
    entityManager.persist(email);
    AddressType home = new AddressType();
    home.setPreferredAddressType("Home Address");
    entityManager.persist(home);
    PreferredLanguage english = new PreferredLanguage();
    english.setPreferredLanguage("English");
    entityManager.persist(english);
    PreferredLanguage afrikaans = new PreferredLanguage();
    afrikaans.setPreferredLanguage("Afrikaans");
    entityManager.persist(afrikaans);

    // Setup Donor and Donation related configuration
    GeneralConfig donorNumberGeneralConfig = new GeneralConfig();
    donorNumberGeneralConfig.setName("donor.donorNumberFormat");
    donorNumberGeneralConfig.setValue("%06d");
    entityManager.persist(donorNumberGeneralConfig);

    GeneralConfig bpSystolicMinConfig = new GeneralConfig();
    bpSystolicMinConfig.setName("donation.donor.bpSystolicMin");
    bpSystolicMinConfig.setValue("10");
    entityManager.persist(bpSystolicMinConfig);

    GeneralConfig bpSystolicMaxConfig = new GeneralConfig();
    bpSystolicMaxConfig.setName("donation.donor.bpSystolicMax");
    bpSystolicMaxConfig.setValue("200");
    entityManager.persist(bpSystolicMaxConfig);

    GeneralConfig bpDiastolicMinConfig = new GeneralConfig();
    bpDiastolicMinConfig.setName("donation.donor.bpDiastolicMin");
    bpDiastolicMinConfig.setValue("10");
    entityManager.persist(bpDiastolicMinConfig);

    GeneralConfig bpDiastolicMaxConfig = new GeneralConfig();
    bpDiastolicMaxConfig.setName("donation.donor.bpDiastolicMax");
    bpDiastolicMaxConfig.setValue("90");
    entityManager.persist(bpDiastolicMaxConfig);

    GeneralConfig hbMinConfig = new GeneralConfig();
    hbMinConfig.setName("donation.donor.hbMin");
    hbMinConfig.setValue("1");
    entityManager.persist(hbMinConfig);

    GeneralConfig hbMaxConfig = new GeneralConfig();
    hbMaxConfig.setName("donation.donor.hbMax");
    hbMaxConfig.setValue("90");
    entityManager.persist(hbMaxConfig);

    GeneralConfig weightMinConfig = new GeneralConfig();
    weightMinConfig.setName("donation.donor.weightMin");
    weightMinConfig.setValue("10");
    entityManager.persist(weightMinConfig);

    GeneralConfig weightMaxConfig = new GeneralConfig();
    weightMaxConfig.setName("donation.donor.weightMax");
    weightMaxConfig.setValue("90");
    entityManager.persist(weightMaxConfig);

    GeneralConfig pulseMinConfig = new GeneralConfig();
    pulseMinConfig.setName("donation.donor.pulseMin");
    pulseMinConfig.setValue("10");
    entityManager.persist(pulseMinConfig);

    GeneralConfig pulseMaxConfig = new GeneralConfig();
    pulseMaxConfig.setName("donation.donor.pulseMax");
    pulseMaxConfig.setValue("90");
    entityManager.persist(pulseMaxConfig);
    
    DataType booleanDataType = new DataType();
    booleanDataType.setDatatype("boolean");
    entityManager.persist(booleanDataType);
    GeneralConfig createInitialComponentsConfig = new GeneralConfig();
    createInitialComponentsConfig.setName("components.createInitialComponents");
    createInitialComponentsConfig.setValue("true");
    createInitialComponentsConfig.setDataType(booleanDataType);
    entityManager.persist(createInitialComponentsConfig);

    FormFieldBuilder.aFormField().withForm("donor").withField("donorNumber")
        .withAutoGenerate(true).withMaxLength(15)
        .buildAndPersist(entityManager);
    entityManager.flush();

    // set up test data (Donation)
    ComponentType componentType = new ComponentType();
    componentType.setComponentTypeNameShort("0011");
    componentType.setExpiresAfter(35);
    componentType.setExpiresAfterUnits(ComponentTypeTimeUnits.DAYS);
    entityManager.persist(componentType);
    PackTypeBuilder.aPackType().withPackType("Single").withCountAsDonation(true).withTestSampleProduced(true)
        .withPeriodBetweenDonations(90).withComponentType(componentType)
        .thatIsNotDeleted().buildAndPersist(entityManager);
    DonationTypeBuilder.aDonationType().withName("Voluntary").thatIsNotDeleted().buildAndPersist(entityManager);
    AdverseEventTypeBuilder.anAdverseEventType().withName("Haematoma")
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);

    // Synchronize entities to the database before running the test
    entityManager.flush();
  }

  private void assertImportLocationData_shouldCreateLocationsFromSpreadsheet() {
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

  private void assertImportDonorData_shouldCreateDonorsFromSpreadsheet()
      throws EncryptedDocumentException, InvalidFormatException, IOException {
   
    // Verify
    Donor firstDonor = findDonorByName("David", "Smith");
    Donor secondDonor = findDonorByName("Jane", "Doe");
    
    // first Donor
    assertThat("Donor status is correct", firstDonor.getDonorStatus(), equalTo(DonorStatus.NORMAL));
    assertThat("Donor is not deleted", firstDonor.getIsDeleted(), equalTo(Boolean.FALSE));
    assertThat("Middle name matches", firstDonor.getMiddleName(), equalTo("John"));
    assertThat("Title matches", firstDonor.getTitle(), equalTo("Mr"));
    assertThat("Calling name matches", firstDonor.getCallingName(), equalTo("Dave"));
    assertThat("Gender matches", firstDonor.getGender(), equalTo(Gender.male));    
    assertThat("Preferred Language matches", firstDonor.getPreferredLanguage().getPreferredLanguage(), equalTo("English"));
    assertThat("BirthDate matches", new SimpleDateFormat("yyyy-MM-dd").format(firstDonor.getBirthDate()), equalTo("1982-12-03"));
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
    assertThat("Preferred Address Type matches", firstDonor.getAddressType().getPreferredAddressType(), equalTo("Home Address"));
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
    assertThat("Work address district matches", firstDonor.getAddress().getWorkAddressDistrict(), equalTo("Cape Town"));
    assertThat("Work address state matches", firstDonor.getAddress().getWorkAddressState(), equalTo("Western Cape"));
    assertThat("Work address country matches", firstDonor.getAddress().getWorkAddressCountry(), equalTo("South Africa"));
    assertThat("Work address zipcode matches", firstDonor.getAddress().getWorkAddressZipcode(), equalTo("8001"));
    assertThat("Postal address line 1 matches", firstDonor.getAddress().getPostalAddressLine1(), equalTo("P.O. Box 12345"));
    assertThat("Postal address line 2 matches", firstDonor.getAddress().getPostalAddressLine2(), equalTo("The Post Office"));
    assertThat("Postal address city matches", firstDonor.getAddress().getPostalAddressCity(), equalTo("Vlaeberg"));
    assertThat("Postal address province matches", firstDonor.getAddress().getPostalAddressProvince(), equalTo("Western Cape"));
    assertThat("Postal address district matches", firstDonor.getAddress().getPostalAddressDistrict(), equalTo("Cape Town"));
    assertThat("Postal address state matches", firstDonor.getAddress().getPostalAddressState(), equalTo("Western Cape"));
    assertThat("Postal address country matches", firstDonor.getAddress().getPostalAddressCountry(), equalTo("South Africa"));
    assertThat("Postal address zipcode matches", firstDonor.getAddress().getPostalAddressZipcode(), equalTo("8018"));
    
    // Second donor
    assertThat("Middle name matches", secondDonor.getMiddleName(), equalTo("Mary"));
    assertThat("Title matches", secondDonor.getTitle(), equalTo("Mrs"));
    assertThat("Calling name matches", secondDonor.getCallingName(), equalTo("Janey"));
    assertThat("Gender matches", secondDonor.getGender(), equalTo(Gender.female));
    assertThat("Preferred Language matches", secondDonor.getPreferredLanguage().getPreferredLanguage(), equalTo("Afrikaans"));
    assertThat("BirthDate matches", new SimpleDateFormat("yyyy-MM-dd").format(secondDonor.getBirthDate()), equalTo("1972-10-03"));
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

  private void assertImportDonationData_shouldCreateDonationsFromSpreadsheet()
      throws EncryptedDocumentException, InvalidFormatException, IOException {
    Donation firstDonation = findDonationByDonationIdentificationNumber("32434");

    assertThat("venue is set", firstDonation.getVenue().getName(), equalTo("First"));
    assertThat("donationType is set", firstDonation.getDonationType().getDonationType(), equalTo("Voluntary"));
    assertThat("packType is set", firstDonation.getPackType().getPackType(), equalTo("Single"));
    SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
    assertThat("donationDate is set", dateSdf.format(firstDonation.getDonationDate()), equalTo("2016-03-03"));
    SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm");
    assertThat("bleedStartTime is set", timeSdf.format(firstDonation.getBleedStartTime()), equalTo("09:00"));
    assertThat("bleedEndTime is set", timeSdf.format(firstDonation.getBleedEndTime()), equalTo("09:09"));
    assertThat("donorWeight is set", firstDonation.getDonorWeight().toString(), equalTo("89.00"));
    assertThat("bloodPressureSystolic is set", firstDonation.getBloodPressureSystolic(), equalTo(Integer.valueOf(113)));
    assertThat("bloodPressureDiastolic is set", firstDonation.getBloodPressureDiastolic(), equalTo(Integer.valueOf(56)));
    assertThat("donorPulse is set", firstDonation.getDonorPulse(), equalTo(Integer.valueOf(30)));
    assertThat("haemoglobinCount is set", firstDonation.getHaemoglobinCount().toString(), equalTo("23.00"));
    assertThat("haemoglobinLevel is set", firstDonation.getHaemoglobinLevel(), equalTo(HaemoglobinLevel.PASS));
    assertThat("adverseEventType is set", firstDonation.getAdverseEvent().getType().getName(), equalTo("Haematoma"));
    assertThat("adverseEventComment is set", firstDonation.getAdverseEvent().getComment(), equalTo("bla"));
    assertThat("bloodAbo is set", firstDonation.getBloodAbo(), equalTo("O"));
    assertThat("bloodRh is set", firstDonation.getBloodRh(), equalTo("+"));
    assertThat("notes is set", firstDonation.getNotes(), equalTo("Notes"));
  }
  
  private Donation findDonationByDonationIdentificationNumber(String din) {
    return entityManager.createQuery("SELECT d FROM Donation d WHERE d.donationIdentificationNumber = :din", Donation.class)
        .setParameter("din", din)
        .getSingleResult();
  }
}
