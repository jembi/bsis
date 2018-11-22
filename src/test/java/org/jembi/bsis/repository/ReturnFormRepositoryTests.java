package org.jembi.bsis.repository;

import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.ReturnFormBuilder.aReturnForm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.returnform.ReturnForm;
import org.jembi.bsis.model.returnform.ReturnStatus;
import org.jembi.bsis.repository.ReturnFormRepository;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ReturnFormRepositoryTests extends SecurityContextDependentTestSuite {

  @Autowired
  private ReturnFormRepository returnFormRepository;

  @Test
  public void testSaveReturnForm_shouldPersistCorrectly() throws Exception {
    // Set up data
    Location returnedFrom = aUsageSite().buildAndPersist(entityManager);
    Location returnedTo = aDistributionSite().buildAndPersist(entityManager);
    Component component = aComponent().build();
    ReturnForm returnForm = aReturnForm()
        .withId(null)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(component)
        .build();

    // Run test
    returnFormRepository.save(returnForm);
    
    // Verify
    Assert.assertNotNull("ReturnForm was persisted", returnForm.getId());
    Assert.assertNotNull("createdDate has been populated", returnForm.getCreatedDate());
    Assert.assertNotNull("createdBy has been populated", returnForm.getCreatedBy());
    Assert.assertNotNull("lastUpdated has been populated", returnForm.getLastUpdated());
    Assert.assertNotNull("lastUpdatedBy has been populated", returnForm.getLastUpdatedBy());
    Assert.assertNotNull("components has been populated", returnForm.getComponents());
    Assert.assertEquals("there's 1 component", 1, returnForm.getComponents().size());
  }

  @Test
  public void testFindById_shouldReturnReturnForm() throws Exception {
    // Set up data
    ReturnForm returnForm = aReturnForm().buildAndPersist(entityManager);

    // Run test
    ReturnForm persistedReturnForm = returnFormRepository.findById(returnForm.getId());
    
    // Verify
    Assert.assertEquals("Right ReturnForm was returned", returnForm, persistedReturnForm);
  }
  
  @Test(expected = javax.persistence.NoResultException.class)
  public void testFindById_shouldThrowException() throws Exception {
    returnFormRepository.findById(UUID.randomUUID());
  }
  
  public void testFindReturnForms_shouldReturnReturnFormsInDateRange() throws Exception {
    // Set up data   
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -20);
    Date returnDate1 = calendar.getTime();
    ReturnForm returnForm1 = aReturnForm().withReturnDate(returnDate1).buildAndPersist(entityManager);
    calendar.add(Calendar.DAY_OF_MONTH, 10);
    Date returnDate2 = calendar.getTime();
    ReturnForm returnForm2 = aReturnForm().withReturnDate(returnDate2).buildAndPersist(entityManager);
    calendar.add(Calendar.DAY_OF_MONTH, 5);
    Date returnDate3 = calendar.getTime();
    ReturnForm returnForm3 = aReturnForm().withReturnDate(returnDate3).buildAndPersist(entityManager);

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(returnDate2, returnDate3, null, null, null);
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 2, returnForms.size());
    Assert.assertFalse("Correct ReturnForm returned", returnForms.contains(returnForm1));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm2));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm3));
  }
  
  @Test
  public void testFindReturnForms_shouldReturnReturnFormsBeforeDate() throws Exception {
    // Set up data   
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -20);
    Date returnDate1 = calendar.getTime();
    ReturnForm returnForm1 = aReturnForm().withReturnDate(returnDate1).buildAndPersist(entityManager);
    calendar.add(Calendar.DAY_OF_MONTH, 10);
    Date returnDate2 = calendar.getTime();
    ReturnForm returnForm2 = aReturnForm().withReturnDate(returnDate2).buildAndPersist(entityManager);
    calendar.add(Calendar.DAY_OF_MONTH, 5);
    Date returnDate3 = calendar.getTime();
    ReturnForm returnForm3 = aReturnForm().withReturnDate(returnDate3).buildAndPersist(entityManager);

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(null, returnDate2, null, null, null);
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 2, returnForms.size());
    Assert.assertFalse("Correct ReturnForm returned", returnForms.contains(returnForm3));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm1));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm2));
  }
  
  @Test
  public void testFindReturnForms_shouldReturnReturnFormsAfterDate() throws Exception {
    // Set up data    
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -20);
    Date returnDate1 = calendar.getTime();
    ReturnForm returnForm1 = aReturnForm().withReturnDate(returnDate1).buildAndPersist(entityManager);
    calendar.add(Calendar.DAY_OF_MONTH, 10);
    Date returnDate2 = calendar.getTime();
    ReturnForm returnForm2 = aReturnForm().withReturnDate(returnDate2).buildAndPersist(entityManager);
    calendar.add(Calendar.DAY_OF_MONTH, 5);
    Date returnDate3 = calendar.getTime();
    ReturnForm returnForm3 = aReturnForm().withReturnDate(returnDate3).buildAndPersist(entityManager);

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(returnDate2, null, null, null, null);
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 2, returnForms.size());
    Assert.assertFalse("Correct ReturnForm returned", returnForms.contains(returnForm1));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm2));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm3));
  }
  
  @Test
  public void testFindReturnForms_shouldReturnReturnFormsReturnedFromLocation() throws Exception {
    // Set up data
    Location returnedFrom1 = aUsageSite().buildAndPersist(entityManager);
    Location returnedFrom2 = aUsageSite().buildAndPersist(entityManager);
    ReturnForm returnForm1 = aReturnForm().withReturnedFrom(returnedFrom1).buildAndPersist(entityManager);
    ReturnForm returnForm2 = aReturnForm().withReturnedFrom(returnedFrom2).buildAndPersist(entityManager);

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(null, null, returnedFrom1.getId(), null, null);
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 1, returnForms.size());
    Assert.assertFalse("Correct ReturnForm returned", returnForms.contains(returnForm2));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm1));
  }
  
  @Test
  public void testFindReturnForms_shouldReturnReturnFormsReturnedToLocation() throws Exception {
    // Set up data
    Location returnedTo1 = aDistributionSite().buildAndPersist(entityManager);
    Location returnedTo2 = aDistributionSite().buildAndPersist(entityManager);
    ReturnForm returnForm1 = aReturnForm().withReturnedTo(returnedTo1).buildAndPersist(entityManager);
    ReturnForm returnForm2 = aReturnForm().withReturnedTo(returnedTo2).buildAndPersist(entityManager);

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(null, null, null, returnedTo2.getId(), null);
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 1, returnForms.size());
    Assert.assertFalse("Correct ReturnForm returned", returnForms.contains(returnForm1));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm2));
  }
  
  @Test
  public void testFindReturnForms_shouldReturnAllNotDeletedReturnForms() throws Exception {
    // Set up data
    ReturnForm returnForm1 = aReturnForm().buildAndPersist(entityManager);
    ReturnForm returnForm2 = aReturnForm().buildAndPersist(entityManager);
    ReturnForm returnForm3 = aReturnForm().withIsDeleted(true).buildAndPersist(entityManager);

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(null, null, null, null, null);
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 2, returnForms.size());
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm1));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm2));
    Assert.assertFalse("Correct ReturnForm returned", returnForms.contains(returnForm3));
  }
  
  @Test
  public void testFindReturnFormsByStatus_shouldReturnMatchingReturnForms() throws Exception {
    // Set up data
    ReturnForm expectedReturnForm = aReturnForm()
        .withReturnStatus(ReturnStatus.CREATED)
        .buildAndPersist(entityManager);
    ReturnForm excludedReturnForm = aReturnForm()
        .withReturnStatus(ReturnStatus.RETURNED)
        .buildAndPersist(entityManager);
        
    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(null, null, null, null, ReturnStatus.CREATED);
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 1, returnForms.size());
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(expectedReturnForm));
    Assert.assertFalse("Correct ReturnForm returned", returnForms.contains(excludedReturnForm));
  }
  
  @Test
  public void testFindReturnForms_shouldReturnMatchingReturnForms() throws Exception {
    // Set up data
    Location returnedFrom = aUsageSite().buildAndPersist(entityManager);
    Location returnedTo = aDistributionSite().buildAndPersist(entityManager);
    ReturnStatus returnStatus = ReturnStatus.RETURNED;
    ReturnForm returnForm1 = aReturnForm()
        .withReturnDate(new Date())
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnStatus(returnStatus)
        .buildAndPersist(entityManager);
    ReturnForm returnForm2 = aReturnForm()
        .withReturnDate(new Date())
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnStatus(returnStatus)
        .buildAndPersist(entityManager);
    ReturnForm returnForm3 = aReturnForm()
        .withReturnDate(new Date())
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnStatus(returnStatus)
        .buildAndPersist(entityManager);
        
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -5);
    Date returnedDateFrom = calendar.getTime();
    calendar.add(Calendar.DAY_OF_MONTH, 10);
    Date returnedDateTo = calendar.getTime();

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(returnedDateFrom, returnedDateTo, 
      returnedFrom.getId(), returnedTo.getId(), returnStatus);
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 3, returnForms.size());
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm1));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm2));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm3));
  }
}