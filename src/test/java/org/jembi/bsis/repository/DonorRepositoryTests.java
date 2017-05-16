package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.matchers.DonorMatcher.hasSameStateAsDonor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.dto.DonorExportDTO;
import org.jembi.bsis.dto.DuplicateDonorDTO;
import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.helpers.builders.AddressBuilder;
import org.jembi.bsis.helpers.builders.AddressTypeBuilder;
import org.jembi.bsis.helpers.builders.ContactBuilder;
import org.jembi.bsis.helpers.builders.ContactMethodTypeBuilder;
import org.jembi.bsis.helpers.builders.DonorBuilder;
import org.jembi.bsis.helpers.builders.IdTypeBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.builders.PreferredLanguageBuilder;
import org.jembi.bsis.helpers.matchers.DonorExportDTOMatcher;
import org.jembi.bsis.model.address.Address;
import org.jembi.bsis.model.address.AddressType;
import org.jembi.bsis.model.address.Contact;
import org.jembi.bsis.model.address.ContactMethodType;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.idtype.IdType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.preferredlanguage.PreferredLanguage;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DonorRepositoryTests extends SecurityContextDependentTestSuite {
  
  @Autowired
  DonorRepository donorRepository;

  @Test
  public void testFindDuplicateDonorsBasic() throws Exception {
    List<Donor> donors = new ArrayList<Donor>();
    donors.add(DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    donors.add(DonorBuilder.aDonor().withDonorNumber("2").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    donors.add(DonorBuilder.aDonor().withDonorNumber("3").withFirstName("Jo").withLastName("Smith")
        .withGender(Gender.female).withBirthDate("1978-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    donors.add(DonorBuilder.aDonor().withDonorNumber("4").withFirstName("Nancy").withLastName("Drew")
        .withGender(Gender.female).withBirthDate("1964-11-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    List<DuplicateDonorDTO> duplicateDonors = donorRepository.getDuplicateDonors();
    Assert.assertEquals("One set of matching donors", 1, duplicateDonors.size());
    Assert.assertEquals("Two matching donors", 2, duplicateDonors.get(0).getCount());
    Assert.assertEquals("David is matching Donor", "David", duplicateDonors.get(0).getFirstName());
  }

  @Test
  public void testFindDuplicateDonorsDouble() throws Exception {
    List<Donor> donors = new ArrayList<Donor>();
    donors.add(DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    donors.add(DonorBuilder.aDonor().withDonorNumber("2").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    donors.add(DonorBuilder.aDonor().withDonorNumber("3").withFirstName("Jo").withLastName("Smith")
        .withGender(Gender.female).withBirthDate("1978-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    donors.add(DonorBuilder.aDonor().withDonorNumber("4").withFirstName("Sue").withLastName("Simpson")
        .withGender(Gender.female).withBirthDate("1982-02-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    donors.add(DonorBuilder.aDonor().withDonorNumber("5").withFirstName("Nancy").withLastName("Drew")
        .withGender(Gender.female).withBirthDate("1964-11-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    donors.add(DonorBuilder.aDonor().withDonorNumber("6").withFirstName("Sue").withLastName("Simpson")
        .withGender(Gender.female).withBirthDate("1982-02-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    donors.add(DonorBuilder.aDonor().withDonorNumber("7").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    List<DuplicateDonorDTO> duplicateDonors = donorRepository.getDuplicateDonors();
    Assert.assertEquals("Two sets of matching donors", 2, duplicateDonors.size());
    Assert.assertEquals("Three matching donors", 3, duplicateDonors.get(0).getCount());
    Assert.assertEquals("David is matching Donor", "David", duplicateDonors.get(0).getFirstName());
    Assert.assertEquals("Two matching donors", 2, duplicateDonors.get(1).getCount());
    Assert.assertEquals("Sue is matching Donor", "Sue", duplicateDonors.get(1).getFirstName());
  }
  
  @Test
  public void testFindDuplicateDonorsMerged() throws Exception {
    List<Donor> donors = new ArrayList<Donor>();
    donors.add(DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    donors.add(DonorBuilder.aDonor().withDonorNumber("2").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.MERGED)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    List<DuplicateDonorDTO> duplicateDonors = donorRepository.getDuplicateDonors();
    Assert.assertEquals("No duplicate donors", 0, duplicateDonors.size());
  }
  
  @Test
  public void testFindDuplicateDonorsDeleted() throws Exception {
    List<Donor> donors = new ArrayList<Donor>();
    donors.add(DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager));
    donors.add(DonorBuilder.aDonor().withDonorNumber("3").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsDeleted().buildAndPersist(entityManager));
    List<DuplicateDonorDTO> duplicateDonors = donorRepository.getDuplicateDonors();
    Assert.assertEquals("No duplicate donors", 0, duplicateDonors.size());
  }
  
  @Test
  public void testFindDuplicateDonorsMatchingOneBasic() throws Exception {
    Donor donor = DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager);
    DonorBuilder.aDonor().withDonorNumber("2").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager);
    DonorBuilder.aDonor().withDonorNumber("3").withFirstName("Jo").withLastName("Smith")
        .withGender(Gender.female).withBirthDate("1978-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager);
    DonorBuilder.aDonor().withDonorNumber("4").withFirstName("Nancy").withLastName("Drew")
        .withGender(Gender.female).withBirthDate("1964-11-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager);

    List<Donor> duplicateDonors = donorRepository.getDuplicateDonors(donor.getFirstName(), donor.getLastName(), 
        donor.getBirthDate(), donor.getGender());
    
    Assert.assertEquals("Two matching donors", 2, duplicateDonors.size());
    Assert.assertEquals("David is matching Donor", "David", duplicateDonors.get(0).getFirstName());
    Assert.assertEquals("David is matching Donor", "David", duplicateDonors.get(1).getFirstName());
  }

  @Test
  public void testFindDuplicateMatchingOneDonorsDouble() throws Exception {
    DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager);
    DonorBuilder.aDonor().withDonorNumber("2").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager);
    DonorBuilder.aDonor().withDonorNumber("3").withFirstName("Jo").withLastName("Smith")
        .withGender(Gender.female).withBirthDate("1978-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager);
    DonorBuilder.aDonor().withDonorNumber("4").withFirstName("Sue").withLastName("Simpson")
        .withGender(Gender.female).withBirthDate("1982-02-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager);
    DonorBuilder.aDonor().withDonorNumber("5").withFirstName("Nancy").withLastName("Drew")
        .withGender(Gender.female).withBirthDate("1964-11-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager);
    Donor donor = DonorBuilder.aDonor().withDonorNumber("6").withFirstName("Sue").withLastName("Simpson")
        .withGender(Gender.female).withBirthDate("1982-02-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager);
    DonorBuilder.aDonor().withDonorNumber("7").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").withDonorStatus(DonorStatus.NORMAL)
        .thatIsNotDeleted().buildAndPersist(entityManager);
    DonorBuilder.aDonor().withDonorNumber("8").withFirstName("Sue").withLastName("Simpson")
        .withGender(Gender.female).withBirthDate("1982-02-20").withDonorStatus(DonorStatus.MERGED)
        .thatIsNotDeleted().buildAndPersist(entityManager);

    List<Donor> duplicateDonors = donorRepository.getDuplicateDonors(donor.getFirstName(), donor.getLastName(), 
        donor.getBirthDate(), donor.getGender());

    Assert.assertEquals("Two matching donors", 2, duplicateDonors.size());
    Assert.assertEquals("Sue is matching Donor", "Sue", duplicateDonors.get(0).getFirstName());
    Assert.assertEquals("Sue is matching Donor", "Sue", duplicateDonors.get(1).getFirstName());
  }
  
  @Test
  public void testMobileClinicDonorsCanBeFound() throws Exception {

    Location venue = LocationBuilder.aVenue().withName("test").thatIsMobileSite().buildAndPersist(entityManager);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    DonorBuilder.aDonor()
        .withDonorNumber("D1")
        .withFirstName("Clara")
        .withLastName("Donor")
        .withBirthDate(sdf.parse("20/02/1975"))
        .withGender(Gender.female)
        .withBloodAbo("A")
        .withBloodRh("+")
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    DonorBuilder.aDonor()
        .withDonorNumber("D2")
        .withFirstName("Bobby")
        .withLastName("ADonor")
        .withBirthDate(sdf.parse("5/12/1982"))
        .withGender(Gender.male)
        .withBloodAbo("O")
        .withBloodRh("-")
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    DonorBuilder.aDonor()
        .withDonorNumber("D3")
        .withFirstName("Abigail")
        .withLastName("Donor")
        .withBirthDate(sdf.parse("10/10/1985"))
        .withGender(Gender.female)
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);

    List<MobileClinicDonorDTO> mobileClinicDonorDTOs =
        donorRepository.findMobileClinicDonorsByVenues(new HashSet<UUID>(Arrays.asList(venue.getId())));

    assertThat("Correct number of MobileClinicDonors returned", mobileClinicDonorDTOs.size(), is(3));

    MobileClinicDonorDTO returnedDonor1 = mobileClinicDonorDTOs.get(0);
    assertThat("MobileClinicDonor sorting is correct", returnedDonor1.getDonorNumber(), is("D2"));
    assertThat("Donor firstName is correct", returnedDonor1.getFirstName(), is("Bobby"));
    assertThat("Donor lastName is correct", returnedDonor1.getLastName(), is("ADonor"));
    assertThat("Donor gender is correct", returnedDonor1.getGender(), is(Gender.male));
    assertThat("Donor birthDate is correct", sdf.format(returnedDonor1.getBirthDate()), is("05/12/1982"));
    assertThat("Donor ABO is correct", returnedDonor1.getBloodAbo(), is("O"));
    assertThat("Donor Rh is correct", returnedDonor1.getBloodRh(), is("-"));
    assertThat("Donor status is correct", returnedDonor1.getDonorStatus(), is(DonorStatus.NORMAL));
    assertThat("Donor venue is correct", returnedDonor1.getVenue(), is(venue));
    
    MobileClinicDonorDTO returnedDonor2 = mobileClinicDonorDTOs.get(1);
    assertThat("MobileClinicDonor sorting is correct", returnedDonor2.getDonorNumber(), is("D3"));
    assertThat("Donor firstName is correct", returnedDonor2.getFirstName(), is("Abigail"));
    assertThat("Donor lastName is correct", returnedDonor2.getLastName(), is("Donor"));
    assertThat("Donor gender is correct", returnedDonor2.getGender(), is(Gender.female));
    assertThat("Donor birthDate is correct", sdf.format(returnedDonor2.getBirthDate()), is("10/10/1985"));
    assertThat("Donor ABO is correct", returnedDonor2.getBloodAbo(), nullValue());
    assertThat("Donor Rh is correct", returnedDonor2.getBloodRh(), nullValue());
    assertThat("Donor status is correct", returnedDonor2.getDonorStatus(), is(DonorStatus.NORMAL));
    assertThat("Donor venue is correct", returnedDonor2.getVenue(), is(venue));
    
    MobileClinicDonorDTO returnedDonor3 = mobileClinicDonorDTOs.get(2);
    assertThat("MobileClinicDonor sorting is correct", returnedDonor3.getDonorNumber(), is("D1"));
    assertThat("Donor firstName is correct", returnedDonor3.getFirstName(), is("Clara"));
    assertThat("Donor lastName is correct", returnedDonor3.getLastName(), is("Donor"));
    assertThat("Donor gender is correct", returnedDonor3.getGender(), is(Gender.female));
    assertThat("Donor birthDate is correct", sdf.format(returnedDonor3.getBirthDate()), is("20/02/1975"));
    assertThat("Donor ABO is correct", returnedDonor3.getBloodAbo(), is("A"));
    assertThat("Donor Rh is correct", returnedDonor3.getBloodRh(), is("+"));
    assertThat("Donor status is correct", returnedDonor3.getDonorStatus(), is(DonorStatus.NORMAL));
    assertThat("Donor venue is correct", returnedDonor3.getVenue(), is(venue));
  }

  @Test
  public void testDeletedMobileClinicDonorsAreNotReturned() throws Exception {

    Location venue = LocationBuilder.aVenue().withName("test").thatIsMobileSite().buildAndPersist(entityManager);

    DonorBuilder.aDonor()
        .withDonorNumber("D1")
        .withVenue(venue)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    Donor donor2 = DonorBuilder.aDonor()
        .withDonorNumber("D2")
        .withVenue(venue)
        .thatIsDeleted()
        .buildAndPersist(entityManager);

    List<MobileClinicDonorDTO> mobileClinicDonorDTOs =
        donorRepository.findMobileClinicDonorsByVenues(new HashSet<UUID>(Arrays.asList(venue.getId())));

    assertThat("Correct number of MobileClinicDonors returned", mobileClinicDonorDTOs.size(), is(1));
    Assert.assertFalse("Deleted MobileClinicDonor not returned", mobileClinicDonorDTOs.contains(donor2));
  }

  @Test
  public void testMergedMobileClinicDonorsAreNotReturned() throws Exception {

    Location venue = LocationBuilder.aVenue().withName("test").thatIsMobileSite().buildAndPersist(entityManager);
    DonorBuilder.aDonor()
        .withDonorNumber("D1")
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    Donor donor2 = DonorBuilder.aDonor()
        .withDonorNumber("D2")
        .withDonorStatus(DonorStatus.MERGED)
        .withVenue(venue)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);

    List<MobileClinicDonorDTO> mobileClinicDonorDTOs =
        donorRepository.findMobileClinicDonorsByVenues(new HashSet<UUID>(Arrays.asList(venue.getId())));

    assertThat("Correct number of MobileClinicDonors returned", mobileClinicDonorDTOs.size(), is(1));
    Assert.assertFalse("Deleted MobileClinicDonor not returned", mobileClinicDonorDTOs.contains(donor2));
  }

  @Test
  public void testMobileClinicDonorsAreInCorrectVenue() throws Exception {

    Location venue1 = LocationBuilder.aVenue().withName("test1").thatIsMobileSite().buildAndPersist(entityManager);
    Location venue2 = LocationBuilder.aVenue().withName("test2").thatIsMobileSite().buildAndPersist(entityManager);

    DonorBuilder.aDonor()
        .withDonorNumber("D1")
        .withVenue(venue1)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    DonorBuilder.aDonor()
        .withDonorNumber("D2")
        .withVenue(venue2)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);

    List<MobileClinicDonorDTO> mobileClinicDonorDTOs =
        donorRepository.findMobileClinicDonorsByVenues(new HashSet<UUID>(Arrays.asList(venue1.getId())));

    assertThat("Correct number of MobileClinicDonors returned", mobileClinicDonorDTOs.size(), is(1));
    for (MobileClinicDonorDTO d : mobileClinicDonorDTOs) {
      Assert.assertEquals("MobileClinicDonor in correct venue", venue1, d.getVenue());
    }
  }

  @Test
  public void testFindDonorByDonorNumberWithExistingDonor_shouldReturnDonor() {
    // Set up
    String donorNumber = "000001";
    Donor expectedDonor = aDonor().withDonorNumber(donorNumber).buildAndPersist(entityManager);
    aDonor().withDonorNumber("667754").buildAndPersist(entityManager);

    // Test
    Donor returnedDonor = donorRepository.findDonorByDonorNumber(donorNumber);

    // Verify
    assertThat(returnedDonor, is(expectedDonor));
  }

  @Test(expected = NoResultException.class)
  public void testFindDonorByDonorNumberWithNoExistingDonor_shouldThrowNoResultException() {
    donorRepository.findDonorByDonorNumber("000001");
  }

  @Test(expected = NoResultException.class)
  public void testFindDonorByDonorNumberWithMergedDonor_shouldThrowNoResultException() {
    DonorBuilder.aDonor().withDonorStatus(DonorStatus.MERGED).withDonorNumber("000001").buildAndPersist(entityManager);
    donorRepository.findDonorByDonorNumber("000001");
  }

  @Test(expected = NoResultException.class)
  public void testFindDonorByDonorNumberWithDeletedDonor_shouldThrowNoResultException() {
    DonorBuilder.aDonor().thatIsDeleted().withDonorNumber("000001").buildAndPersist(entityManager);
    donorRepository.findDonorByDonorNumber("000001");
  }

  @Test
  public void testFindDonorByDonationIdentificationNumberWithExistingDonor_shouldReturnDonor() {
    // Set up
    String donationIdentificationNumber = "0000001";
    Donor expectedDonor = aDonor().buildAndPersist(entityManager);
    aDonation()
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withDonor(expectedDonor)
        .buildAndPersist(entityManager);
    aDonation()
        .withDonationIdentificationNumber("5687411")
        .withDonor(aDonor().build())
        .buildAndPersist(entityManager);

    // Test
    Donor returnedDonor = donorRepository.findDonorByDonationIdentificationNumber(donationIdentificationNumber);

    // Verify
    assertThat(returnedDonor, is(expectedDonor));
  }
  
  @Test
  public void testFindDonorByDonationIdentificationNumberWithFlagCharactersForExistingDonor_shouldReturnDonor() {
    // Set up
    String dinWithFlagCharacters = "0000001B1";
    Donor expectedDonor = aDonor().buildAndPersist(entityManager);
    aDonation()
        .withDonationIdentificationNumber("0000001")
        .withFlagCharacters("B1")
        .withDonor(expectedDonor)
        .buildAndPersist(entityManager);
    
    //Excluded donation by DIN
    aDonation()
        .withDonationIdentificationNumber("5687411")
        .withFlagCharacters("C1")
        .withDonor(aDonor().build())
        .buildAndPersist(entityManager);

    // Test
    Donor returnedDonor = donorRepository.findDonorByDonationIdentificationNumber(dinWithFlagCharacters);

    // Verify
    assertThat(returnedDonor, is(hasSameStateAsDonor(expectedDonor)));
  }

  @Test(expected = NoResultException.class)
  public void testFindDonorByDonationIdentificationNumberWithNoExistingDonor_shouldThrowNoResultException() {
    donorRepository.findDonorByDonationIdentificationNumber("0000001");

  }
  
  @Test(expected = NoResultException.class)
  public void testFindDonorByDonationIdentificationNumberWithMergedStatus_shouldThrowNoResultException() {
    String din = "0000001";
    Donor mergedDonor = aDonor().withDonorStatus(DonorStatus.MERGED).buildAndPersist(entityManager);
    aDonation()
      .withDonationIdentificationNumber(din)
      .withDonor(mergedDonor)
      .buildAndPersist(entityManager);
    donorRepository.findDonorByDonationIdentificationNumber(din);
  }
  
  @Test(expected = NoResultException.class)
  public void testFindDonorByDonationIdentificationNumberThatIsDeleted_shouldThrowNoResultException() {
    String din = "0000001";
    Donor deletedDonor = aDonor().thatIsDeleted().buildAndPersist(entityManager);
    aDonation()
      .withDonationIdentificationNumber(din)
      .withDonor(deletedDonor)
      .buildAndPersist(entityManager);
    donorRepository.findDonorByDonationIdentificationNumber(din);
  }
  
  @Test(expected = NoResultException.class)
  public void testFindDonorByDeletedDonationDonationIdentificationNumber_shouldThrowNoResultException() {
    String din = "0000001";
    Donor donor = aDonor().buildAndPersist(entityManager);
    aDonation()
      .withDonationIdentificationNumber(din)
      .withDonor(donor)
      .thatIsDeleted()
      .buildAndPersist(entityManager);
    donorRepository.findDonorByDonationIdentificationNumber(din);
  }

  @Test
  public void testVerifyDonorExists() {
    Donor donor = DonorBuilder.aDonor().buildAndPersist(entityManager);
    Assert.assertTrue("Donor exists", donorRepository.verifyDonorExists(donor.getId()));
  }

  @Test
  public void testVerifyDonorExistsWithInvalidId_shouldNotExist() {
    UUID donorId = new UUID(12345678,12345678);
    Assert.assertFalse("Donor does not exist", donorRepository.verifyDonorExists(donorId));
  }

  @Test
  public void testVerifyDonorExistsWithMergedStatus_shouldNotExist() {
    Donor mergedDonor = DonorBuilder.aDonor().withDonorStatus(DonorStatus.MERGED).buildAndPersist(entityManager);
    Assert.assertFalse("Donor does not exist", donorRepository.verifyDonorExists(mergedDonor.getId()));
  }

  @Test
  public void testVerifyDonorExistsThatIsDeleted_shouldNotExist() {
    Donor deletedDonor = DonorBuilder.aDonor().thatIsDeleted().buildAndPersist(entityManager);
    Assert.assertFalse("Donor does not exist", donorRepository.verifyDonorExists(deletedDonor.getId()));
  }

  @Test
  public void testFindDonorsForExport_shouldReturnDonorsExportDTOsThatAreNotDeleted() {
    String donorNumber = "1234567";
    String title = "Ms";
    String firstName = "A";
    String middleName = "Sample";
    String lastName = "Donor";
    String callingName = "Sue";
    Gender gender = Gender.female;
    Date birthDate = new DateTime(1977,10,20,0,0,0).toDate();
    PreferredLanguage language = PreferredLanguageBuilder.anEnglishPreferredLanguage().buildAndPersist(entityManager);
    Location venue = aVenue().withName("DonateHere").buildAndPersist(entityManager);
    String bloodAbo = "A";
    String bloodRh = "+";
    String notes = "noted";
    IdType idType = IdTypeBuilder.aNationalId().buildAndPersist(entityManager);
    String idNumber = "77102089390328";
    Date dateOfFirstDonation = new LocalDate(2000,1,1).toDate();
    Date dateOfLastDonation = new DateTime(2016,9,9,9,9,9).toDate();
    Date dueToDonate = new DateTime(2019,12,12,0,0,0).toDate();
    String homeAddressLine1 = "123 Apartment";
    String homeAddressLine2 = "4 Street Rd";
    String city = "Cape Town";
    String province = "Western Cape";
    String district = "Cape Peninsula";
    String state = "Western Cape";
    String country = "South Africa";
    String zipcode = "8001";
    String workAddressLine1 = "Office 123";
    String workAddressLine2 = "4 Road Avenue";
    String postalAddressLine1 = "P.O. Box 1234";
    String postalAddressLine2 = "Centre";
    Address address = AddressBuilder.anAddress()
          .withHomeAddress(homeAddressLine1, homeAddressLine2, city, province, district, country, state, zipcode)
          .withWorkAddress(workAddressLine1, workAddressLine2, city, province, district, country, state, zipcode)
          .withPostalAddress(postalAddressLine1, postalAddressLine2, city, province, district, country, state, zipcode)
          .buildAndPersist(entityManager);
    AddressType addressType = AddressTypeBuilder.aHomeAddressType().buildAndPersist(entityManager);
    String homeNumber = "0214561212";
    String workNumber = "0211234567";
    String mobileNumber = "0734567827";
    String email = "email@jembi.org";
    Contact contact = ContactBuilder.aContact()
          .withHomeNumber(homeNumber)
          .withWorkNumber(workNumber)
          .withMobileNumber(mobileNumber)
          .withEmail(email)
          .buildAndPersist(entityManager);
    ContactMethodType contactMethodType = ContactMethodTypeBuilder.anEmailContactMethodType().buildAndPersist(entityManager);
    
    DonorExportDTO expectedDonorDTO = new DonorExportDTO(donorNumber, new Date(), USERNAME, new Date(), USERNAME,
        title, firstName, middleName, lastName, callingName, gender,
        birthDate, language.getPreferredLanguage(), venue.getName(), bloodAbo, bloodRh, notes,
        idType.getIdType(), idNumber, dateOfFirstDonation, dateOfLastDonation, dueToDonate,
        contactMethodType.getContactMethodType(), mobileNumber, homeNumber, workNumber, email,
        addressType.getPreferredAddressType(), 
        homeAddressLine1, homeAddressLine2, city, province, district, country, state, zipcode, 
        workAddressLine1, workAddressLine2, city, province, district, country, state, zipcode, 
        postalAddressLine1, postalAddressLine2, city, province, district, country, state, zipcode);
    
    // Expected Donor
    aDonor()
      .withDonorNumber(donorNumber)
      .withTitle(title)
      .withFirstName(firstName)
      .withMiddleName(middleName)
      .withLastName(lastName)
      .withCallingName(callingName)
      .withGender(gender)
      .withBirthDate(birthDate)
      .withVenue(venue)
      .withPreferredLanguage(language)
      .withBloodAbo(bloodAbo)
      .withBloodRh(bloodRh)
      .withNotes(notes)
      .withIdType(idType)
      .withIdNumber(idNumber)
      .withDateOfFirstDonation(dateOfFirstDonation)
      .withDateOfLastDonation(dateOfLastDonation)
      .withDueToDonate(dueToDonate)
      .withContact(contact)
      .withAddress(address)
      .withAddressType(addressType)
      .withContactMethodType(contactMethodType)
      .thatIsNotDeleted()
      .buildAndPersist(entityManager);
    
    // Deleted donor (excluded from export)
    aDonor()
      .thatIsDeleted()
      .buildAndPersist(entityManager);
    
    List<DonorExportDTO> exportedDonors = donorRepository.findDonorsForExport();
    
    // Verify
    assertThat(exportedDonors.size(), is(1));
    
    // Assert state
    assertThat(exportedDonors.get(0), is(DonorExportDTOMatcher.hasSameStateAsDonorExport(expectedDonorDTO)));
  }
  
  @Test
  public void testFindDonorsForExportWithNullAddressTypeAndContactType_shouldReturnDonorsExportDTO() {
    String donorNumber = "1234567";
    String title = "Ms";
    String firstName = "A";
    String middleName = "Sample";
    String lastName = "Donor";
    String callingName = "Sue";
    Gender gender = Gender.female;
    Date birthDate = new DateTime(1977,10,20,0,0,0).toDate();
    PreferredLanguage language = PreferredLanguageBuilder.anEnglishPreferredLanguage().buildAndPersist(entityManager);
    Location venue = aVenue().withName("DonateHere").buildAndPersist(entityManager);
    String bloodAbo = "A";
    String bloodRh = "+";
    String notes = "noted";
    IdType idType = IdTypeBuilder.aNationalId().buildAndPersist(entityManager);
    String idNumber = "77102089390328";
    Date dateOfFirstDonation = new LocalDate(2000,1,1).toDate();
    Date dateOfLastDonation = new DateTime(2016,9,9,9,9,9).toDate();
    Date dueToDonate = new DateTime(2019,12,12,0,0,0).toDate();
    String homeAddressLine1 = "123 Apartment";
    String homeAddressLine2 = "4 Street Rd";
    String city = "Cape Town";
    String province = "Western Cape";
    String district = "Cape Peninsula";
    String state = "Western Cape";
    String country = "South Africa";
    String zipcode = "8001";
    String workAddressLine1 = "Office 123";
    String workAddressLine2 = "4 Road Avenue";
    String postalAddressLine1 = "P.O. Box 1234";
    String postalAddressLine2 = "Centre";
    Address address = AddressBuilder.anAddress()
          .withHomeAddress(homeAddressLine1, homeAddressLine2, city, province, district, country, state, zipcode)
          .withWorkAddress(workAddressLine1, workAddressLine2, city, province, district, country, state, zipcode)
          .withPostalAddress(postalAddressLine1, postalAddressLine2, city, province, district, country, state, zipcode)
          .buildAndPersist(entityManager);
    String homeNumber = "0214561212";
    String workNumber = "0211234567";
    String mobileNumber = "0734567827";
    String email = "email@jembi.org";
    Contact contact = ContactBuilder.aContact()
          .withHomeNumber(homeNumber)
          .withWorkNumber(workNumber)
          .withMobileNumber(mobileNumber)
          .withEmail(email)
          .buildAndPersist(entityManager);
    
    DonorExportDTO expectedDonorDTO = new DonorExportDTO(donorNumber, new Date(), USERNAME, new Date(), USERNAME,
        title, firstName, middleName, lastName, callingName, gender,
        birthDate, language.getPreferredLanguage(), venue.getName(), bloodAbo, bloodRh, notes,
        idType.getIdType(), idNumber, dateOfFirstDonation, dateOfLastDonation, dueToDonate,
        (String)null, mobileNumber, homeNumber, workNumber, email,
        (String)null, homeAddressLine1, homeAddressLine2, city, province, district, country, state, zipcode, 
        workAddressLine1, workAddressLine2, city, province, district, country, state, zipcode, 
        postalAddressLine1, postalAddressLine2, city, province, district, country, state, zipcode);
    
    // Expected Donor
    aDonor()
      .withDonorNumber(donorNumber)
      .withTitle(title)
      .withFirstName(firstName)
      .withMiddleName(middleName)
      .withLastName(lastName)
      .withCallingName(callingName)
      .withGender(gender)
      .withBirthDate(birthDate)
      .withVenue(venue)
      .withPreferredLanguage(language)
      .withBloodAbo(bloodAbo)
      .withBloodRh(bloodRh)
      .withNotes(notes)
      .withIdType(idType)
      .withIdNumber(idNumber)
      .withDateOfFirstDonation(dateOfFirstDonation)
      .withDateOfLastDonation(dateOfLastDonation)
      .withDueToDonate(dueToDonate)
      .withContact(contact)
      .withAddress(address)
      .thatIsNotDeleted()
      .buildAndPersist(entityManager);
    
    List<DonorExportDTO> exportedDonors = donorRepository.findDonorsForExport();
    
    // Verify
    assertThat(exportedDonors.size(), is(1));
    
    // Assert state
    assertThat(exportedDonors.get(0), is(DonorExportDTOMatcher.hasSameStateAsDonorExport(expectedDonorDTO)));
  }
  
  @Test
  public void testFindDonorsForExport_shouldReturnDonorsExportDTOInCorrectOrder() {
    Location venue = aVenue().withName("DonateHere").buildAndPersist(entityManager);

    String donorNumber1 = "1234567";
    Date createdDate1 = new DateTime(2016,9,9,11,44).toDate();

    String donorNumber2 = "1234568";
    Date createdDate2 = new DateTime(2016,9,1,15,30).toDate();
 
    // Expected Donor #1
    aDonor()
      .withDonorNumber(donorNumber1)
      .withFirstName("Sample")
      .withLastName("Donor")
      .withGender(Gender.female)
      .withVenue(venue)
      .withCreatedDate(createdDate1)
      .thatIsNotDeleted()
      .buildAndPersist(entityManager);
    
    // Expected Donor #2
    aDonor()
      .withDonorNumber(donorNumber2)
      .withFirstName("Sample")
      .withMiddleName("Too")
      .withLastName("Donor")
      .withGender(Gender.male)
      .withVenue(venue)
      .withCreatedDate(createdDate2)
      .thatIsNotDeleted()
      .buildAndPersist(entityManager);
    
    List<DonorExportDTO> exportedDonors = donorRepository.findDonorsForExport();
    
    // Verify
    assertThat(exportedDonors.size(), is(2));
    
    // Assert state
    assertThat(exportedDonors.get(0).getDonorNumber(), is(donorNumber2));
    assertThat(exportedDonors.get(1).getDonorNumber(), is(donorNumber1));
  }

  @Test
  public void testFindMobileClinicDonorsByVenues_shouldReturnCorrectNumberOfDonors() throws Exception {
    Location permVenue = LocationBuilder.aVenue().withName("perm venue").buildAndPersist(entityManager);
    Location mobileVenue1 = LocationBuilder.aVenue().withName("mobile venue #1").thatIsMobileSite().buildAndPersist(entityManager);
    Location mobileVenue2 = LocationBuilder.aVenue().withName("mobile venue #2").thatIsMobileSite().buildAndPersist(entityManager);
    Location location = LocationBuilder.aLocation().withName("a location").buildAndPersist(entityManager);

    // All donors should match
    Donor permDonor = DonorBuilder.aDonor()
        .withDonorNumber("D1")
        .withVenue(permVenue)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    Donor locationDonor = DonorBuilder.aDonor()
        .withDonorNumber("D2")
        .withVenue(location)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    Donor mobileClinicDonor1 = DonorBuilder.aDonor()
        .withDonorNumber("D3")
        .withVenue(mobileVenue1)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    Donor mobileClinicDonor2 = DonorBuilder.aDonor()
        .withDonorNumber("D4")
        .withVenue(mobileVenue2)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    List<MobileClinicDonorDTO> mobileClinicDonorDTOs = donorRepository.findMobileClinicDonorsByVenues(null);

    assertThat("Correct number of MobileClinicDonors returned", mobileClinicDonorDTOs.size(), is(4));
    assertThat("Donors from all venues should be returned", mobileClinicDonorDTOs.get(0).getDonorNumber(), is(permDonor.getDonorNumber()));
    assertThat("Donors from all venues should be returned", mobileClinicDonorDTOs.get(1).getDonorNumber(), is(locationDonor.getDonorNumber()));
    assertThat("Donors from all venues should be returned", mobileClinicDonorDTOs.get(2).getDonorNumber(), is(mobileClinicDonor1.getDonorNumber()));
    assertThat("Donors from all venues should be returned", mobileClinicDonorDTOs.get(3).getDonorNumber(), is(mobileClinicDonor2.getDonorNumber()));
  }

  @Test
  public void testFindMobileClinicDonorsByVenuesWithEmptySetOfVenueIds_shouldReturnCorrectNumberOfDonors() throws Exception {
    Location permVenue = LocationBuilder.aVenue().withName("perm venue").buildAndPersist(entityManager);
    Location mobileVenue1 = LocationBuilder.aVenue().withName("mobile venue #1").thatIsMobileSite().buildAndPersist(entityManager);
    Location mobileVenue2 = LocationBuilder.aVenue().withName("mobile venue #2").thatIsMobileSite().buildAndPersist(entityManager);
    Location location = LocationBuilder.aLocation().withName("a location").buildAndPersist(entityManager);

    // All donors should match
    Donor permDonor = DonorBuilder.aDonor()
                .withDonorNumber("D1")
                .withVenue(permVenue)
                .thatIsNotDeleted()
                .buildAndPersist(entityManager);
    Donor locationDonor = DonorBuilder.aDonor()
        .withDonorNumber("D2")
        .withVenue(location)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    Donor mobileClinicDonor1 = DonorBuilder.aDonor()
        .withDonorNumber("D3")
        .withVenue(mobileVenue1)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    Donor mobileClinicDonor2 = DonorBuilder.aDonor()
        .withDonorNumber("D4")
        .withVenue(mobileVenue2)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    Set<UUID> venueIds = new HashSet<>();
    List<MobileClinicDonorDTO> mobileClinicDonorDTOs = donorRepository.findMobileClinicDonorsByVenues(venueIds);

    assertThat("Correct number of MobileClinicDonors returned", mobileClinicDonorDTOs.size(), is(4));
    assertThat("Donors from all venues should be returned", mobileClinicDonorDTOs.get(0).getDonorNumber(), is(permDonor.getDonorNumber()));
    assertThat("Donors from all venues should be returned", mobileClinicDonorDTOs.get(1).getDonorNumber(), is(locationDonor.getDonorNumber()));
    assertThat("Donors from all venues should be returned", mobileClinicDonorDTOs.get(2).getDonorNumber(), is(mobileClinicDonor1.getDonorNumber()));
    assertThat("Donors from all venues should be returned", mobileClinicDonorDTOs.get(3).getDonorNumber(), is(mobileClinicDonor2.getDonorNumber()));
  }
}
