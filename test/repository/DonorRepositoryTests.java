package repository;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dto.DuplicateDonorDTO;
import helpers.builders.DonorBuilder;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.util.Gender;
import suites.ContextDependentTestSuite;

public class DonorRepositoryTests extends ContextDependentTestSuite {
  
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

  @Test(expected = NoResultException.class)
  public void testFindDonorByDonationIdentificationNumberWithNoExistingDonor_shouldThrowNoResultException() {
    donorRepository.findDonorByDonationIdentificationNumber("0000001");
  }
}
