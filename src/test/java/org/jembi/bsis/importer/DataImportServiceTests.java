package org.jembi.bsis.importer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;
import static org.jembi.bsis.helpers.builders.GeneralConfigBuilder.aGeneralConfig;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.matchers.DivisionMatcher.hasSameStateAsDivision;
import static org.jembi.bsis.helpers.matchers.LocationMatcher.hasSameStateAsLocation;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder;
import org.jembi.bsis.helpers.builders.DataTypeBuilder;
import org.jembi.bsis.helpers.builders.DonationTypeBuilder;
import org.jembi.bsis.helpers.builders.FormFieldBuilder;
import org.jembi.bsis.helpers.builders.PackTypeBuilder;
import org.jembi.bsis.helpers.builders.UserBuilder;
import org.jembi.bsis.model.address.AddressType;
import org.jembi.bsis.model.address.ContactMethodType;
import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.idtype.IdType;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.preferredlanguage.PreferredLanguage;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DataImportServiceTests extends SecurityContextDependentTestSuite {
  
  @Autowired
  private DataImportService dataImportService;

  @Test
  public void testDataImport() throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
    // Set up fixture and data
    FileInputStream fileInputStream = new FileInputStream("src/test/resources/fixtures/BSIS-import.xlsx");
    Workbook workbook = WorkbookFactory.create(fileInputStream);
    createSupportingTestData();
    DataType dataType = DataTypeBuilder
        .aDataType()
        .withDataType("Integer")
        .buildAndPersist(entityManager);
    aGeneralConfig()
        .withName(GeneralConfigConstants.DIN_LENGTH)
        .withValue("7")
        .withDataType(dataType)
        .buildAndPersist(entityManager);
    // Exercise SUT
    dataImportService.importData(workbook, false);
    
    // Ensure stale entities are cleared
    entityManager.clear();
    
    // Assert data is correct
    assertImportLocationData_shouldCreateLocationsFromSpreadsheet();
    assertImportDonorData_shouldCreateDonorsFromSpreadsheet();
    assertImportDonationData_shouldCreateDonationsFromSpreadsheet();
    assertImportDonationData_shouldCreateDeferralsFromSpreadsheet();
    assertImportDonationData_shouldCreateOutcomesFromSpreadsheet();
    assertImportDivisionData_shouldCreateDivisionsFromSpreadsheet();
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
    ComponentType componentType = aComponentType()
        .withComponentTypeCode("0011")
        .withExpiresAfter(35)
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .buildAndPersist(entityManager);
    PackTypeBuilder.aPackType().withPackType("Single").withCountAsDonation(true).withTestSampleProduced(true)
        .withPeriodBetweenDonations(90).withComponentType(componentType)
        .thatIsNotDeleted().buildAndPersist(entityManager);
    DonationTypeBuilder.aDonationType().withName("Voluntary").thatIsNotDeleted().buildAndPersist(entityManager);
    AdverseEventTypeBuilder.anAdverseEventType().withName("Haematoma")
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);

    // set up test data (Deferral)
    DeferralReason deferralReason = new DeferralReason();
    deferralReason.setReason("Other reasons");
    deferralReason.setIsDeleted(false);
    entityManager.persist(deferralReason);

    // set up test data (Outcomes)
    BloodTest aboBloodTest = aBloodTest().withValidResults("A,B,AB,O").withTestName("ABO").withTestNameShort("ABO").buildAndPersist(entityManager);
    BloodTestingRule aboBloodTestingRule1 = new BloodTestingRule();
    aboBloodTestingRule1.setBloodTest(aboBloodTest);
    aboBloodTestingRule1.setDonationFieldChanged(DonationField.BLOODABO);
    aboBloodTestingRule1.setNewInformation("A");
    aboBloodTestingRule1.setIsDeleted(false);
    entityManager.persist(aboBloodTestingRule1);
    BloodTestingRule aboBloodTestingRule2 = new BloodTestingRule();
    aboBloodTestingRule2.setBloodTest(aboBloodTest);
    aboBloodTestingRule2.setDonationFieldChanged(DonationField.BLOODABO);
    aboBloodTestingRule2.setNewInformation("B");
    aboBloodTestingRule2.setIsDeleted(false);
    entityManager.persist(aboBloodTestingRule2);
    BloodTestingRule aboBloodTestingRule3 = new BloodTestingRule();
    aboBloodTestingRule3.setBloodTest(aboBloodTest);
    aboBloodTestingRule3.setDonationFieldChanged(DonationField.BLOODABO);
    aboBloodTestingRule3.setNewInformation("O");
    aboBloodTestingRule3.setIsDeleted(false);
    entityManager.persist(aboBloodTestingRule3);
    BloodTestingRule aboBloodTestingRule4 = new BloodTestingRule();
    aboBloodTestingRule4.setBloodTest(aboBloodTest);
    aboBloodTestingRule4.setDonationFieldChanged(DonationField.BLOODABO);
    aboBloodTestingRule4.setNewInformation("AB");
    aboBloodTestingRule4.setIsDeleted(false);
    entityManager.persist(aboBloodTestingRule4);

    BloodTest rhBloodTest = aBloodTest().withValidResults("POS,NEG").withTestName("Rh").withTestNameShort("Rh").buildAndPersist(entityManager);
    BloodTestingRule rhBloodTestingRule1 = new BloodTestingRule();
    rhBloodTestingRule1.setBloodTest(rhBloodTest);
    rhBloodTestingRule1.setDonationFieldChanged(DonationField.BLOODRH);
    rhBloodTestingRule1.setNewInformation("+");
    rhBloodTestingRule1.setIsDeleted(false);
    entityManager.persist(rhBloodTestingRule1);
    BloodTestingRule rhBloodTestingRule2 = new BloodTestingRule();
    rhBloodTestingRule2.setBloodTest(rhBloodTest);
    rhBloodTestingRule2.setDonationFieldChanged(DonationField.BLOODRH);
    rhBloodTestingRule2.setNewInformation("-");
    rhBloodTestingRule2.setIsDeleted(false);
    entityManager.persist(rhBloodTestingRule2);

    aBloodTest().withValidResults("POS,NEG").withTestName("HIV").withTestNameShort("HIV").buildAndPersist(entityManager);

    FormFieldBuilder.aFormField()
        .withForm("division")
        .withField("name")
        .withMaxLength(0)
        .thatIsRequired(true)
        .thatIsHidden(false)
        .buildAndPersist(entityManager);

    FormFieldBuilder.aFormField()
        .withForm("division")
        .withField("level")
        .thatIsRequired(true)
        .thatIsHidden(false)
        .withMaxLength(0)
        .buildAndPersist(entityManager);

    // Synchronize entities to the database before running the test
    entityManager.flush();
  }

  private void assertImportLocationData_shouldCreateLocationsFromSpreadsheet() {
    // Verify
    Location firstLocation = findLocationByName("First");
    Location secondLocation = findLocationByName("Second");
    Location thirdLocation = findLocationByName("Third");
    Location fourthLocation = findLocationByName("Fourth");
    
    Division westernCapeDivision = findDivisionByName("Western Cape");
    Division cityOfCapeTownDivision = findDivisionByName("City of Cape Town");
    Division khayelitshaDivision = findDivisionByName("Khayelitsha");
    Division capeWinelandsDivision = findDivisionByName("Cape Winelands");
    Division stellenboschDivision = findDivisionByName("Stellenbosch");
    Division gautengDivision = findDivisionByName("Gauteng");
    Division tshwaneDivision = findDivisionByName("Tshwane");
    Division pretoriaDivision = findDivisionByName("Pretoria");

    Location expectedFirstLocation = aLocation()
        .withId(firstLocation.getId())
        .withName("First")
        .withNotes("First Location")
        .thatIsVenue()
        .withDivisionLevel1(gautengDivision)
        .withDivisionLevel2(tshwaneDivision)
        .withDivisionLevel3(pretoriaDivision)
        .build();
    Location expectedSecondLocation = aLocation()
        .withId(secondLocation.getId())
        .withName("Second")
        .thatIsMobileSite()
        .thatIsVenue()
        .thatIsDeleted()
        .withDivisionLevel1(westernCapeDivision)
        .withDivisionLevel2(cityOfCapeTownDivision)
        .withDivisionLevel3(khayelitshaDivision)
        .build();
    Location expectedThirdLocation = aLocation()
        .withId(thirdLocation.getId())
        .withName("Third")
        .thatIsUsageSite()
        .thatIsProcessingSite()
        .thatIsDistributionSite()
        .thatIsTestingSite()
        .thatIsReferralSite()
        .withDivisionLevel1(westernCapeDivision)
        .withDivisionLevel2(capeWinelandsDivision)
        .withDivisionLevel3(stellenboschDivision)
        .build();
    Location expectedFourthLocation = aLocation()
        .withId(fourthLocation.getId())
        .withName("Fourth")
        .thatIsVenue()
        .withNotes("A venue")
        .withDivisionLevel1(westernCapeDivision)
        .withDivisionLevel2(cityOfCapeTownDivision)
        .withDivisionLevel3(khayelitshaDivision)
        .build();
    
    assertThat(firstLocation, hasSameStateAsLocation(expectedFirstLocation));
    assertThat(secondLocation, hasSameStateAsLocation(expectedSecondLocation));
    assertThat(thirdLocation, hasSameStateAsLocation(expectedThirdLocation));
    assertThat(fourthLocation, hasSameStateAsLocation(expectedFourthLocation));
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
    Donor thirdDonor = findDonorByName("Donald", "Brown");
    
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
    
    // Minimal donor
    assertThat("BloodAbo not set", thirdDonor.getBloodAbo(), equalTo(""));
    assertThat("BloodRh not set", thirdDonor.getBloodRh(), equalTo(""));
    assertThat("Id number not set", thirdDonor.getIdNumber(), equalTo(""));
  }
  
  private Donor findDonorByName(String firstName, String lastName) {
    return entityManager.createQuery("SELECT d FROM Donor d WHERE d.firstName = :firstName and d.lastName = :lastName", Donor.class)
        .setParameter("firstName", firstName)
        .setParameter("lastName", lastName)
        .getSingleResult();
  }

  private long countTestBatches() {
    return entityManager.createQuery("SELECT COUNT(tb) FROM TestBatch tb", Number.class).getSingleResult().longValue();
  }

  private void assertImportDonationData_shouldCreateDonationsFromSpreadsheet()
      throws EncryptedDocumentException, InvalidFormatException, IOException {
    Donation firstDonation = findDonationByDonationIdentificationNumber("3243400");

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
    assertThat("dateOfLastDonation is set on donor", dateSdf.format(firstDonation.getDonor().getDateOfLastDonation()), equalTo("2016-03-03"));
    assertThat("dueToDonate is set on donor", firstDonation.getDonor().getDueToDonate(), notNullValue());
    assertThat("dateOfFirstDonation is set on donor", firstDonation.getDonor().getDateOfFirstDonation(), notNullValue());
    assertThat("donation is released", firstDonation.isReleased(), equalTo(true));

    TestBatch firstTestBatch = firstDonation.getTestBatch();
    assertThat("Donation has a test batch", firstTestBatch, notNullValue());
    assertThat("TestBatch location is a testing site", firstTestBatch.getLocation().getIsTestingSite(), equalTo(true));
    
    DonationBatch firstDonationBatch = firstDonation.getDonationBatch();
    assertThat("DonationBatch has been defined", firstDonationBatch, notNullValue());
    assertThat("DonationBatch has a batch number", firstDonationBatch.getBatchNumber(), notNullValue());
    assertThat("DonationBatch venue is set", firstDonationBatch.getVenue().getName(), equalTo("First"));
    assertThat("DonationBatch is closed", firstDonationBatch.getIsClosed(), equalTo(true));
        
    Donation secondDonation = findDonationByDonationIdentificationNumber("3243500");
    assertThat("Same DonationBatch", secondDonation.getDonationBatch().getId(), equalTo(firstDonationBatch.getId()));
    assertThat("Same TestBatch", secondDonation.getTestBatch(), equalTo(firstTestBatch));
    assertThat("donation is released", firstDonation.isReleased(), equalTo(true));
    
    Donation thirdDonation = findDonationByDonationIdentificationNumber("3243200");
    assertThat("Different DonationBatch", thirdDonation.getDonationBatch().getId(), not(equalTo(firstDonationBatch.getId())));
    assertThat("Different TestBatch", thirdDonation.getTestBatch(), not(equalTo(firstDonation.getTestBatch())));
    assertThat("TestBatch location is a testing site", thirdDonation.getTestBatch().getLocation().getIsTestingSite(), equalTo(true));
    assertThat("donation is released", firstDonation.isReleased(), equalTo(true));
    
    Donation fourthDonation = findDonationByDonationIdentificationNumber("3243100");
    DonationBatch fourthDonationBatch = fourthDonation.getDonationBatch();
    assertThat("DonationBatch venue is set", fourthDonationBatch.getVenue().getName(), equalTo("Fourth"));
    assertThat("Different DonationBatch", fourthDonationBatch.getId(), not(equalTo(thirdDonation.getDonationBatch().getId())));
    assertThat("TestBatch location is a testing site", fourthDonation.getTestBatch().getLocation().getIsTestingSite(), equalTo(true));
    assertThat("donation is released", firstDonation.isReleased(), equalTo(true));

    // The first pair of donations are in the same donation batch and test batch
    // The second pair of donations are in different donation batches but the same test batch
    assertThat("the correct number of test batches are created", countTestBatches(), equalTo(3L));
    
    Donation fifthDonation = findDonationByDonationIdentificationNumber("3243600");
    assertThat("donorWeight is not set", fifthDonation.getDonorWeight(), nullValue());
    assertThat("bloodPressureSystolic is not set", fifthDonation.getBloodPressureSystolic(), nullValue());
    assertThat("bloodPressureDiastolic is not set", fifthDonation.getBloodPressureDiastolic(), nullValue());
    assertThat("donorPulse is not set", fifthDonation.getDonorPulse(), nullValue());
    assertThat("haemoglobinCount is not set", fifthDonation.getHaemoglobinCount(), nullValue());
    assertThat("haemoglobinLevel is not set", fifthDonation.getHaemoglobinLevel(), nullValue());
    assertThat("adverseEventType is not set", fifthDonation.getAdverseEvent(), nullValue());
    assertThat("adverseEventComment is not set", fifthDonation.getAdverseEvent(), nullValue());
    assertThat("bloodAbo is not set", fifthDonation.getBloodAbo(), nullValue());
    assertThat("bloodRh is not set", fifthDonation.getBloodRh(), nullValue());
    assertThat("notes is not set", fifthDonation.getNotes(), equalTo(""));
    assertThat("TestBatch location is a testing site", fifthDonation.getTestBatch().getLocation().getIsTestingSite(), equalTo(true));
    assertThat("donation is released", firstDonation.isReleased(), equalTo(true));
   
  }

  private Donation findDonationByDonationIdentificationNumber(String din) {
    return entityManager.createQuery("SELECT d FROM Donation d WHERE d.donationIdentificationNumber = :din", Donation.class)
        .setParameter("din", din)
        .getSingleResult();
  }
  
  private void assertImportDonationData_shouldCreateDeferralsFromSpreadsheet() throws ParseException {
    DonorDeferral firstDeferral = findDeferralByDeferralReasonText("Had nausea");
    
    assertThat("venue is set", firstDeferral.getVenue().getName(), equalTo("First"));
    assertThat("deferral reason is set", firstDeferral.getDeferralReason().getReason(), equalTo("Other reasons"));
    assertThat("createdDate is set", new SimpleDateFormat("yyyy-MM-dd").format(firstDeferral.getDeferralDate()), equalTo("2016-01-03"));
    assertThat("deferredUntil is set", new SimpleDateFormat("yyyy-MM-dd").format(firstDeferral.getDeferredUntil()), equalTo("2016-06-03"));
    assertThat("deferred donor is set", firstDeferral.getDeferredDonor().getFirstName(), equalTo("David"));
    
    DonorDeferral secondDeferral = findDeferralByDonor("Donald", "Brown");
    assertThat("reason text is not set", secondDeferral.getDeferralReasonText(), equalTo(""));
  }
  
  private DonorDeferral findDeferralByDeferralReasonText(String deferralReasonText) {
    return entityManager.createQuery("SELECT d FROM DonorDeferral d WHERE d.deferralReasonText = :deferralReasonText", DonorDeferral.class)
        .setParameter("deferralReasonText", deferralReasonText)
        .getSingleResult();
  }
  
  private DonorDeferral findDeferralByDonor(String donorFirstName, String donorLastName) {
    return entityManager.createQuery("SELECT d FROM DonorDeferral d WHERE d.deferredDonor.firstName = :firstName "
        + "AND d.deferredDonor.lastName = :lastName", DonorDeferral.class)
        .setParameter("firstName", donorFirstName)
        .setParameter("lastName", donorLastName)
        .getSingleResult();
  }
  
  private void assertImportDonationData_shouldCreateOutcomesFromSpreadsheet() {
    Donation firstDonation = findDonationByDonationIdentificationNumber("3243400");
    BloodTestResult bloodTestOutcome = findBloodTestOutcomeByDonation(firstDonation);
    
    SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
    assertThat("testedOn date is set", dateSdf.format(bloodTestOutcome.getTestedOn()), equalTo("2016-03-16"));
    SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm");
    assertThat("testedOn time is set", timeSdf.format(bloodTestOutcome.getTestedOn()), equalTo("09:00"));
    assertThat("bloodTest is set", bloodTestOutcome.getBloodTest().getTestName(), equalTo("HIV"));
    assertThat("outcome is set", bloodTestOutcome.getResult(), equalTo("POS"));
    assertThat("reEntryRequired is false", !bloodTestOutcome.getReEntryRequired());
  }
  
  private BloodTestResult findBloodTestOutcomeByDonation(Donation donation) {
    return entityManager.createQuery("SELECT tr FROM BloodTestResult tr WHERE tr.donation = :donation", BloodTestResult.class)
        .setParameter("donation", donation)
        .getSingleResult();
  }

  private void assertImportDivisionData_shouldCreateDivisionsFromSpreadsheet() {
    // Verify
    String westernCape = "Western Cape";
    Division westernCapeDivision = findDivisionByName(westernCape);

    String cityOfCapeTown = "City of Cape Town";
    Division cityOfCapeTownDivision = findDivisionByName(cityOfCapeTown);

    String khayelitsha = "Khayelitsha";
    Division khayelitshaDivision = findDivisionByName(khayelitsha);

    String mitchellsPlain = "Mitchell’s Plain";
    Division mitchellsPlainDivision = findDivisionByName(mitchellsPlain);

    String capeWinelands = "Cape Winelands";
    Division capeWinelandsDivision = findDivisionByName(capeWinelands);

    String stellenbosch = "Stellenbosch";
    Division stellenboschDivision = findDivisionByName(stellenbosch);

    String gauteng = "Gauteng";
    Division gautengDivision = findDivisionByName(gauteng);

    String tshwane = "Tshwane";
    Division tshwaneDivision = findDivisionByName(tshwane);

    String pretoria = "Pretoria";
    Division pretoriaDivision = findDivisionByName(pretoria);

    Division expectedWesternCapeDivision = aDivision()
        .withId(westernCapeDivision.getId())
        .withName(westernCape)
        .withLevel(1)
        .build();

    Division expectedCityOfCapeTownDivision = aDivision()
        .withId(cityOfCapeTownDivision.getId())
        .withName(cityOfCapeTown)
        .withLevel(2)
        .withParent(westernCapeDivision)
        .build();

    Division expectedKhayelitshaDivision = aDivision()
        .withId(khayelitshaDivision.getId())
        .withName(khayelitsha)
        .withLevel(3)
        .withParent(cityOfCapeTownDivision)
        .build();

    Division expectedMitchellsPlainDivision = aDivision()
        .withId(mitchellsPlainDivision.getId())
        .withName(mitchellsPlain)
        .withLevel(3)
        .withParent(cityOfCapeTownDivision)
        .build();

    Division expectedCapeWinelandsDivision = aDivision()
        .withId(capeWinelandsDivision.getId())
        .withName(capeWinelands)
        .withLevel(2)
        .withParent(westernCapeDivision)
        .build();

    Division expectedStellenboschDivision = aDivision()
        .withId(stellenboschDivision.getId())
        .withName(stellenbosch)
        .withLevel(3)
        .withParent(capeWinelandsDivision)
        .build();

    Division expectedGautengDivision =
        aDivision()
        .withId(gautengDivision.getId())
        .withName(gauteng)
        .withLevel(1)
        .build();

    Division expectedTshwaneDivision = aDivision()
        .withId(tshwaneDivision.getId())
        .withName(tshwane)
        .withLevel(2)
        .withParent(gautengDivision)
        .build();

    Division expectedPretoriaDivision = aDivision()
        .withId(pretoriaDivision.getId())
        .withName(pretoria)
        .withLevel(3)
        .withParent(tshwaneDivision)
        .build();

    assertThat(westernCapeDivision, hasSameStateAsDivision(expectedWesternCapeDivision));
    assertThat(cityOfCapeTownDivision, hasSameStateAsDivision(expectedCityOfCapeTownDivision));
    assertThat(khayelitshaDivision, hasSameStateAsDivision(expectedKhayelitshaDivision));
    assertThat(mitchellsPlainDivision, hasSameStateAsDivision(expectedMitchellsPlainDivision));
    assertThat(capeWinelandsDivision, hasSameStateAsDivision(expectedCapeWinelandsDivision));
    assertThat(stellenboschDivision, hasSameStateAsDivision(expectedStellenboschDivision));
    assertThat(gautengDivision, hasSameStateAsDivision(expectedGautengDivision));
    assertThat(tshwaneDivision, hasSameStateAsDivision(expectedTshwaneDivision));
    assertThat(pretoriaDivision, hasSameStateAsDivision(expectedPretoriaDivision));
  }

  private Division findDivisionByName(String name) {
    TypedQuery<Division> query =
        entityManager.createQuery("SELECT d FROM Division d WHERE d.name = :name ", Division.class);
    query.setParameter("name", name);
    List<Division> divisions = query.getResultList();

    if (divisions.isEmpty()) {
      return null;
    }

    // there should only ever be 0 or 1 division with a given name, so if there is > 0 we can
    // safely take the first division
    return divisions.get(0);
  }

}
