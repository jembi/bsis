package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hamcrest.core.IsNull;
import org.jembi.bsis.helpers.builders.ComponentBatchBuilder;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.scalastuff.scalabeans.sig.Mirror.NullValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
@Transactional
public class DonationBatchRepositoryTests {

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
    ComponentBatch newComponentBatch = ComponentBatchBuilder.aComponentBatch()
        .build();
  
    List<DonationBatch> donationBatches = Arrays.asList(
        aDonationBatch()
        .withComponentBatch(newComponentBatch)
        .buildAndPersist(entityManager),
        
        // Excluded by deletion
        aDonationBatch()
        .withComponentBatch(newComponentBatch)
        .thatIsDeleted()
        .buildAndPersist(entityManager),
        
        // Excluded by closure
        aDonationBatch()
        .withComponentBatch(newComponentBatch)
        .thatIsClosed()
        .buildAndPersist(entityManager) 
    );  
    newComponentBatch.setDonationBatch(donationBatches.get(0));
    newComponentBatch.setDonationBatch(donationBatches.get(1));
    newComponentBatch.setDonationBatch(donationBatches.get(2));
    
    // Test
    ComponentBatch returnedComponentBatch = donationBatchRepository.findComponentBatchByDonationbatchId(
        donationBatches.get(0).getId());
    
    assertThat(newComponentBatch,is(returnedComponentBatch));
  }
  
  @Test
  public void testFindComponentBatchByDonationBatchIdWithNoResults_returnsNullComponentBatch() {  
    DonationBatch donationBatch =  aDonationBatch().buildAndPersist(entityManager);
       
    // Test
    ComponentBatch returnedComponentBatch = donationBatchRepository.findComponentBatchByDonationbatchId(
        donationBatch.getId());
    
    assertThat(returnedComponentBatch,is(nullValue()));
  }

}
