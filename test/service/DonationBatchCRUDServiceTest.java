package service;

import helpers.builders.DonationBatchBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.donationbatch.DonationBatch;

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
}
