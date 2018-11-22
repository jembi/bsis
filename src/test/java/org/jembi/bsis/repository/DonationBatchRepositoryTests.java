package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jembi.bsis.helpers.builders.ComponentBatchBuilder;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DonationBatchRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private DonationBatchRepository donationBatchRepository;
  @PersistenceContext
  private EntityManager entityManager;

  @Test
  public void testCountOpenDonationBatchesWithNoDonationBatches_shouldReturnZero() {
    int returnedCount = donationBatchRepository.countOpenDonationBatches();
    assertThat(returnedCount, is(0));
  }

  @Test
  public void testCountOpenDonationBatches_shouldCountOpenAndNotDeletedBatches() {
    // Expected donation batches
    aDonationBatch().buildAndPersist(entityManager);
    aDonationBatch().buildAndPersist(entityManager);

    // Excluded by closed flag
    aDonationBatch().thatIsClosed().buildAndPersist(entityManager);

    // Excluded by deleted flag
    aDonationBatch().thatIsDeleted().buildAndPersist(entityManager);

    int returnedCount = donationBatchRepository.countOpenDonationBatches();

    assertThat(returnedCount, is(2));
  }
  
  @Test
  public void testFindComponentBatchByDonationBatchId_returnsComponentBatch() {
    ComponentBatch newComponentBatch = ComponentBatchBuilder.aComponentBatch().build();
   
    List<DonationBatch> donationBatches = Arrays.asList(
        // Donation batches with different Id's
        aDonationBatch()
        .withComponentBatch(newComponentBatch)
        .buildAndPersist(entityManager),
      
        aDonationBatch()
        .withComponentBatch(newComponentBatch)
        .buildAndPersist(entityManager),
        
        aDonationBatch()
        .withComponentBatch(newComponentBatch)
        .buildAndPersist(entityManager) 
    );  
    newComponentBatch.setDonationBatch(donationBatches.get(0));
    newComponentBatch.setDonationBatch(donationBatches.get(1));
    newComponentBatch.setDonationBatch(donationBatches.get(2));
    
    // Test
    ComponentBatch returnedComponentBatch = donationBatchRepository.findComponentBatchByDonationbatchId(
        donationBatches.get(0).getId());
    
    assertThat(newComponentBatch, is(returnedComponentBatch));
  }
  
  @Test
  public void testFindComponentBatchByDonationBatchIdWithNoResults_returnsNullComponentBatch() {  
    DonationBatch donationBatch =  aDonationBatch().buildAndPersist(entityManager);
       
    // Test
    ComponentBatch returnedComponentBatch = donationBatchRepository.findComponentBatchByDonationbatchId(
        donationBatch.getId());
    
    assertThat(returnedComponentBatch, is(nullValue()));
  }
  
  @Test
  public void testFindDonationBatchesWithDonationBatchesWithinRange_shouldReturnDonationBatches() throws Exception {
    Date dateRangeStart = new DateTime().minusDays(5).toDate();
    Date dateRangeEnd = new DateTime().toDate();

    DonationBatch donationBatchDateInRange = aDonationBatch()
        .withDonationBatchDate(new DateTime().minusDays(3).toDate())
        .buildAndPersist(entityManager);

    DonationBatch anotherDonationBatchDateInRange = aDonationBatch()
        .withDonationBatchDate(new DateTime().minusDays(2).toDate())
        .buildAndPersist(entityManager);

    // donation Batch Excluded by date range (before range)
    aDonationBatch()
        .withDonationBatchDate(new DateTime().minusDays(7).toDate())
        .buildAndPersist(entityManager);

    // donation Batch Excluded by date range (after range)
    aDonationBatch()
        .withDonationBatchDate(new DateTime().plusDays(7).toDate())
        .buildAndPersist(entityManager);

    List<DonationBatch> expectedDonationBatches =
        Arrays.asList(donationBatchDateInRange, anotherDonationBatchDateInRange);

    List<DonationBatch> returnedDonationBatches = donationBatchRepository.findDonationBatches(null, Collections.<UUID>emptyList(), dateRangeStart, dateRangeEnd);

    assertThat(returnedDonationBatches, is(expectedDonationBatches));
  }

}
