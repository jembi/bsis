package model;

import static helpers.builders.DonorBuilder.aDonor;
import helpers.builders.UserBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.donor.Donor;
import model.donor.DonorStatus;
import model.modificationtracker.RowModificationTracker;
import model.user.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.transaction.AfterTransaction;

import repository.DonorRepository;
import security.BsisUserDetails;
import suites.ContextDependentTestSuite;

public class BaseModificationTrackerEntityTest extends ContextDependentTestSuite {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private DonorRepository donorRepository;

  @Test
  public void testPersistDonor() throws Exception {
    Donor donor =
        aDonor().withFirstName("Sample").withLastName("Donor").buildAndPersist(entityManager);

    assertDateEquals("Created date has been set", new Date(), donor.getCreatedDate());
    Assert.assertNotNull("Created by has been set", donor.getCreatedBy());
    Assert.assertEquals("Created by has been set", ADMIN_USERNAME, donor.getCreatedBy()
        .getUsername());
    Assert.assertEquals("Updated same as created", donor.getCreatedDate(), donor.getLastUpdated());
    Assert.assertEquals("Updated same as created", donor.getCreatedBy(), donor.getLastUpdatedBy());
  }

  @Test
  public void testMergeDonor() throws Exception {
    Donor donor =
        aDonor().withFirstName("Sample").withLastName("Donor").thatIsNotDeleted()
            .withDonorStatus(DonorStatus.NORMAL).buildAndPersist(entityManager);
    Date newCreatedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-10-20 09:17");
    donor.setCreatedDate(newCreatedDate);
    donor.setTitle("Ms");

    Donor donor1 = donorRepository.updateDonor(donor);
    Donor updatedDonor = donorRepository.findDonorById(donor1.getId());

    Assert.assertEquals("Created date is the same", newCreatedDate, updatedDonor.getCreatedDate());
    assertDateEquals("Updated date has been set", new Date(), donor.getLastUpdated());
    Assert.assertNotNull("Updated by has been set", donor.getLastUpdatedBy());
    Assert.assertEquals("Updated by has been set", ADMIN_USERNAME, donor.getLastUpdatedBy()
        .getUsername());
  }

  @Test
  public void testSetModificationTracker() throws Exception {
    Donor donor =
        aDonor().withFirstName("Sample").withLastName("Donor").buildAndPersist(entityManager);
    RowModificationTracker tracker = new RowModificationTracker();
    Date newCreatedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-10-20 09:17");
    tracker.setCreatedDate(newCreatedDate);
    tracker.setCreatedBy(adminUser);
    donor.setModificationTracker(tracker);

    Donor updatedDonor = donorRepository.updateDonor(donor);

    RowModificationTracker updatedTracker = updatedDonor.getModificationTracker();
    Assert
        .assertEquals("Created date is the same", newCreatedDate, updatedTracker.getCreatedDate());
  }

  private void assertDateEquals(String message, Date expected, Date actual) {
    Assert.assertNotNull(message, actual);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Assert.assertEquals(message, sdf.format(expected), sdf.format(actual));
  }
}
