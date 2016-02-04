package repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import helpers.builders.MobileClinicDonorBuilder;
import helpers.builders.LocationBuilder;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.donor.DonorStatus;
import model.donor.MobileClinicDonor;
import model.location.Location;
import model.util.Gender;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.ContextDependentTestSuite;

public class MobileClinicRepositoryTests extends ContextDependentTestSuite {

  @PersistenceContext
  private EntityManager entityManager;
  @Autowired
  private MobileClinicRepository mobileClinicRepository;

  @Test
  public void testMobileClinicDonorsCanBeFound() throws Exception {
    
    Location venue = LocationBuilder.aLocation()
        .withName("test")
        .buildAndPersist(entityManager);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    MobileClinicDonorBuilder.aMobileClinicDonor()
        .withDonorNumber("D1")
        .withFirstName("Clara")
        .withLastName("Donor")
        .withBirthDate(sdf.parse("20/02/1975"))
        .withGender(Gender.female)
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    MobileClinicDonorBuilder.aMobileClinicDonor()
        .withDonorNumber("D2")
        .withFirstName("Bobby")
        .withLastName("ADonor")
        .withBirthDate(sdf.parse("5/12/1982"))
        .withGender(Gender.male)
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
      MobileClinicDonorBuilder.aMobileClinicDonor()
        .withDonorNumber("D3")
        .withFirstName("Abigail")
        .withLastName("Donor")
        .withBirthDate(sdf.parse("10/10/1985"))
        .withGender(Gender.female)
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    
    List<MobileClinicDonor> mobileClinicDonors = mobileClinicRepository.lookUp(venue.getId());

    assertThat("Correct number of MobileClinicDonors returned", mobileClinicDonors.size(), is(3));
    // check sorting
    MobileClinicDonor returnedDonor1 = mobileClinicDonors.get(0);
    Assert.assertEquals("MobileClinicDonor sorting is correct", "D2", returnedDonor1.getDonorNumber());
    MobileClinicDonor returnedDonor2 = mobileClinicDonors.get(1);
    Assert.assertEquals("MobileClinicDonor sorting is correct", "D3", returnedDonor2.getDonorNumber());
    MobileClinicDonor returnedDonor3 = mobileClinicDonors.get(2);
    Assert.assertEquals("MobileClinicDonor sorting is correct", "D1", returnedDonor3.getDonorNumber());
  }

  @Test
  public void testDeletedMobileClinicDonorsAreNotReturned() throws Exception {
    
    Location venue = LocationBuilder.aLocation()
        .withName("test")
        .buildAndPersist(entityManager);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    MobileClinicDonorBuilder.aMobileClinicDonor()
        .withDonorNumber("D1")
        .withFirstName("Test")
        .withLastName("DonorOne")
        .withBirthDate(sdf.parse("20/02/1975"))
        .withGender(Gender.female)
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    MobileClinicDonor donor2 = MobileClinicDonorBuilder.aMobileClinicDonor()
        .withDonorNumber("D2")
        .withFirstName("Test")
        .withLastName("DonorTwo")
        .withBirthDate(sdf.parse("5/12/1982"))
        .withGender(Gender.male)
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsDeleted()
        .buildAndPersist(entityManager);
    
    List<MobileClinicDonor> mobileClinicDonors = mobileClinicRepository.lookUp(venue.getId());

    assertThat("Correct number of MobileClinicDonors returned", mobileClinicDonors.size(), is(1));
    Assert.assertFalse("Deleted MobileClinicDonor not returned", mobileClinicDonors.contains(donor2));
  }
  
  @Test
  public void testMergedMobileClinicDonorsAreNotReturned() throws Exception {
    
    Location venue = LocationBuilder.aLocation()
        .withName("test")
        .buildAndPersist(entityManager);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    MobileClinicDonorBuilder.aMobileClinicDonor()
        .withDonorNumber("D1")
        .withFirstName("Test")
        .withLastName("DonorOne")
        .withBirthDate(sdf.parse("20/02/1975"))
        .withGender(Gender.female)
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    MobileClinicDonor donor2 = MobileClinicDonorBuilder.aMobileClinicDonor()
        .withDonorNumber("D2")
        .withFirstName("Test")
        .withLastName("DonorTwo")
        .withBirthDate(sdf.parse("5/12/1982"))
        .withGender(Gender.male)
        .withDonorStatus(DonorStatus.MERGED)
        .withVenue(venue)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    
    List<MobileClinicDonor> mobileClinicDonors = mobileClinicRepository.lookUp(venue.getId());

    assertThat("Correct number of MobileClinicDonors returned", mobileClinicDonors.size(), is(1));
    Assert.assertFalse("Deleted MobileClinicDonor not returned", mobileClinicDonors.contains(donor2));
  }
  
  @Test
  public void testMobileClinicDonorsAreInCorrectVenue() throws Exception {
    
    Location venue1 = LocationBuilder.aLocation()
        .withName("test1")
        .buildAndPersist(entityManager);
    Location venue2 = LocationBuilder.aLocation()
        .withName("test2")
        .buildAndPersist(entityManager);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    MobileClinicDonorBuilder.aMobileClinicDonor()
        .withDonorNumber("D1")
        .withFirstName("Test")
        .withLastName("DonorOne")
        .withBirthDate(sdf.parse("20/02/1975"))
        .withGender(Gender.female)
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue1)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    MobileClinicDonorBuilder.aMobileClinicDonor()
        .withDonorNumber("D2")
        .withFirstName("Test")
        .withLastName("DonorTwo")
        .withBirthDate(sdf.parse("5/12/1982"))
        .withGender(Gender.male)
        .withDonorStatus(DonorStatus.NORMAL)
        .withVenue(venue2)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    
    List<MobileClinicDonor> mobileClinicDonors = mobileClinicRepository.lookUp(venue1.getId());

    assertThat("Correct number of MobileClinicDonors returned", mobileClinicDonors.size(), is(1));
    for (MobileClinicDonor d : mobileClinicDonors) {
      Assert.assertEquals("MobileClinicDonor in correct venue", venue1, d.getVenue());
    }
  }
}
