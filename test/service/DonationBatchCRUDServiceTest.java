package service;

import helpers.builders.DonationBatchBuilder;
import helpers.builders.DonationBuilder;
import helpers.builders.DonorBuilder;
import helpers.builders.LocationBuilder;
import helpers.builders.PackTypeBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.donor.Donor;
import model.location.Location;
import model.packtype.PackType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import repository.DonationBatchRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class DonationBatchCRUDServiceTest {

  @Autowired
  DonationBatchCRUDService donationBatchCRUDService;

  @Autowired
  DonationBatchRepository donationBatchRepository;

  @PersistenceContext
  EntityManager entityManager;

  @Test
  public void testDeleteDonationBatch() throws Exception {
    new DonationBatchBuilder().withBatchNumber("TEST123").buildAndPersist(entityManager);
    DonationBatch db1 = donationBatchRepository.findDonationBatchByBatchNumber("TEST123");
    Assert.assertNotNull("Entity was saved", db1);
    donationBatchCRUDService.deleteDonationBatch(db1.getId());
    try {
      donationBatchRepository.findDonationBatchByBatchNumber("TEST123");
      Assert.fail("Entity was deleted");
    } catch (javax.persistence.NoResultException e) {
      // this will be thrown if the entity was deleted
    }
  }

  @Test
  public void testUpdateDonationBatch() throws Exception {
    Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2015-10-28");
    Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2015-10-20");

    Location venue1 = new LocationBuilder().withName("Cape Town").buildAndPersist(entityManager);
    PackType packType = new PackTypeBuilder().withPeriodBetweenDonations(10).buildAndPersist(entityManager);

    List<Donation> donations = new ArrayList<Donation>();
    Donor donor1 = new DonorBuilder().withDonorNumber("111").withVenue(venue1).thatIsNotDeleted().buildAndPersist(entityManager);
    donations.add(new DonationBuilder().withDonationDate(date1).withVenue(venue1).withDonor(donor1).withPackType(packType).thatIsNotDeleted().buildAndPersist(entityManager));
    Donor donor2 = new DonorBuilder().withDonorNumber("112").withVenue(venue1).thatIsNotDeleted().buildAndPersist(entityManager);
    donations.add(new DonationBuilder().withDonationDate(date1).withVenue(venue1).withDonor(donor2).withPackType(packType).thatIsNotDeleted().buildAndPersist(entityManager));

    DonationBatch donationBatch = new DonationBatchBuilder().withBatchNumber("TEST123").withDonations(donations).withVenue(venue1).buildAndPersist(entityManager);

    Location venue2 = new LocationBuilder().withName("Durban").buildAndPersist(entityManager);

    donationBatch.setVenue(venue2);
    donationBatch.setCreatedDate(date2);

    donationBatchCRUDService.updateDonationBatch(donationBatch);

    DonationBatch updatedDonationBatch = donationBatchRepository.findDonationBatchByBatchNumber("TEST123");
    Assert.assertNotNull("DonationBatch is found", updatedDonationBatch);
    Assert.assertEquals("DonationBatch is updated", date2, updatedDonationBatch.getCreatedDate());
    Assert.assertEquals("DonationBatch is updated", venue2.getId(), updatedDonationBatch.getVenue().getId());
    List<Donation> savedDonations = updatedDonationBatch.getDonations();
    Assert.assertNotNull("DonationBatch is updated", savedDonations);
    Assert.assertEquals("DonationBatch donations are updated", 2, savedDonations.size());
    Donation savedDonation1 = savedDonations.get(0);
    Assert.assertNotNull("DonationBatch donations are updated", savedDonation1);
    Assert.assertEquals("DonationBatch donations are updated", venue2.getId(), savedDonation1.getVenue().getId());
    Assert.assertEquals("DonationBatch donations are updated", date2, savedDonation1.getDonationDate());
    Donation savedDonation2 = savedDonations.get(1);
    Assert.assertNotNull("DonationBatch donations are updated", savedDonation2);
    Assert.assertEquals("DonationBatch donations are updated", venue2.getId(), savedDonation2.getVenue().getId());
    Assert.assertEquals("DonationBatch donations are updated", date2, savedDonation2.getDonationDate());
  }
}
