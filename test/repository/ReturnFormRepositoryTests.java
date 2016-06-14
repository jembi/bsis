package repository;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.ReturnFormBuilder.aReturnForm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import helpers.builders.ComponentBuilder;
import helpers.builders.LocationBuilder;
import helpers.builders.ReturnFormBuilder;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.component.Component;
import model.location.Location;
import model.returnform.ReturnForm;
import model.returnform.ReturnStatus;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.SecurityContextDependentTestSuite;

public class ReturnFormRepositoryTests extends SecurityContextDependentTestSuite {

  @Autowired
  private ReturnFormRepository returnFormRepository;

  @Test
  public void testSaveReturnForm_shouldPersistCorrectly() throws Exception {
    // Set up data
    Location returnedFrom = LocationBuilder.aUsageSite().buildAndPersist(entityManager);
    Location returnedTo = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    Component component = ComponentBuilder.aComponent().build();
    ReturnForm returnForm = ReturnFormBuilder.aReturnForm()
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
    Location returnedFrom = LocationBuilder.aUsageSite().buildAndPersist(entityManager);
    Location returnedTo = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    Component component = ComponentBuilder.aComponent().build();
    ReturnForm returnForm = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(component)
        .buildAndPersist(entityManager);

    // Run test
    ReturnForm persistedReturnForm = returnFormRepository.findById(returnForm.getId());
    
    // Verify
    Assert.assertEquals("Right ReturnForm was returned", returnForm, persistedReturnForm);
  }
  
  @Test(expected = javax.persistence.NoResultException.class)
  public void testFindById_shouldThrowException() throws Exception {
    returnFormRepository.findById(123L);
  }
  
  @Test
  public void testFindCreatedReturnForms_shouldReturnMatchingReturnForms() {
    // Set up fixture
    List<ReturnForm> expectedReturnForms = Arrays.asList(
        aReturnForm()
            .withReturnStatus(ReturnStatus.CREATED)
            .buildAndPersist(entityManager),
        aReturnForm()
            .withReturnStatus(ReturnStatus.CREATED)
            .withComponent(aComponent().buildAndPersist(entityManager))
            .buildAndPersist(entityManager)
    );
    
    // Excluded by return status
    aReturnForm().withReturnStatus(ReturnStatus.RETURNED).buildAndPersist(entityManager);
    
    // Excluded by deleted
    aReturnForm().withReturnStatus(ReturnStatus.CREATED).withIsDeleted(true).buildAndPersist(entityManager);
    
    // Exercise SUT
    List<ReturnForm> returnedReturnForms = returnFormRepository.findCreatedReturnForms();
    
    // Verify
    assertThat(returnedReturnForms, is(expectedReturnForms));
  }
  
  public void testFindReturnForms_shouldReturnReturnFormsInDateRange() throws Exception {
    // Set up data
    Location returnedFrom = LocationBuilder.aUsageSite().buildAndPersist(entityManager);
    Location returnedTo = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    
    Calendar calendar = Calendar.getInstance();
    
    calendar.add(Calendar.DAY_OF_MONTH, -20);
    Date returnDate1 = calendar.getTime();
    ReturnForm returnForm1 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(returnDate1)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);
    
    calendar.add(Calendar.DAY_OF_MONTH, 10);
    Date returnDate2 = calendar.getTime();
    ReturnForm returnForm2 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(returnDate2)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);
    
    calendar.add(Calendar.DAY_OF_MONTH, 5);
    Date returnDate3 = calendar.getTime();
    ReturnForm returnForm3 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(returnDate3)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(returnDate2, returnDate3, null, null);
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 2, returnForms.size());
    Assert.assertFalse("Correct ReturnForm returned", returnForms.contains(returnForm1));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm2));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm3));
  }
  
  @Test
  public void testFindReturnForms_shouldReturnReturnFormsBeforeDate() throws Exception {
    // Set up data
    Location returnedFrom = LocationBuilder.aUsageSite().buildAndPersist(entityManager);
    Location returnedTo = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    
    Calendar calendar = Calendar.getInstance();
    
    calendar.add(Calendar.DAY_OF_MONTH, -20);
    Date returnDate1 = calendar.getTime();
    ReturnForm returnForm1 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(returnDate1)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);
    
    calendar.add(Calendar.DAY_OF_MONTH, 10);
    Date returnDate2 = calendar.getTime();
    ReturnForm returnForm2 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(returnDate2)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);
    
    calendar.add(Calendar.DAY_OF_MONTH, 5);
    Date returnDate3 = calendar.getTime();
    ReturnForm returnForm3 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(returnDate3)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(null, returnDate2, null, null);
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 2, returnForms.size());
    Assert.assertFalse("Correct ReturnForm returned", returnForms.contains(returnForm3));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm1));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm2));
  }
  
  @Test
  public void testFindReturnForms_shouldReturnReturnFormsAfterDate() throws Exception {
    // Set up data
    Location returnedFrom = LocationBuilder.aUsageSite().buildAndPersist(entityManager);
    Location returnedTo = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    
    Calendar calendar = Calendar.getInstance();
    
    calendar.add(Calendar.DAY_OF_MONTH, -20);
    Date returnDate1 = calendar.getTime();
    ReturnForm returnForm1 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(returnDate1)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);
    
    calendar.add(Calendar.DAY_OF_MONTH, 10);
    Date returnDate2 = calendar.getTime();
    ReturnForm returnForm2 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(returnDate2)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);
    
    calendar.add(Calendar.DAY_OF_MONTH, 5);
    Date returnDate3 = calendar.getTime();
    ReturnForm returnForm3 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(returnDate3)
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(returnDate2, null, null, null);
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 2, returnForms.size());
    Assert.assertFalse("Correct ReturnForm returned", returnForms.contains(returnForm1));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm2));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm3));
  }
  
  @Test
  public void testFindReturnForms_shouldReturnReturnFormsReturnedFromLocation() throws Exception {
    // Set up data
    Location returnedFrom1 = LocationBuilder.aUsageSite().buildAndPersist(entityManager);
    Location returnedFrom2 = LocationBuilder.aUsageSite().buildAndPersist(entityManager);
    Location returnedTo = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    ReturnForm returnForm1 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(new Date())
        .withReturnedFrom(returnedFrom1)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);
    ReturnForm returnForm2 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(new Date())
        .withReturnedFrom(returnedFrom2)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(null, null, returnedFrom1.getId(), null);
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 1, returnForms.size());
    Assert.assertFalse("Correct ReturnForm returned", returnForms.contains(returnForm2));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm1));
  }
  
  @Test
  public void testFindReturnForms_shouldReturnReturnFormsReturnedToLocation() throws Exception {
    // Set up data
    Location returnedFrom = LocationBuilder.aUsageSite().buildAndPersist(entityManager);
    Location returnedTo1 = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    Location returnedTo2 = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    ReturnForm returnForm1 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(new Date())
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo1)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);
    ReturnForm returnForm2 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(new Date())
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo2)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(null, null, null, returnedTo2.getId());
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 1, returnForms.size());
    Assert.assertFalse("Correct ReturnForm returned", returnForms.contains(returnForm1));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm2));
  }
  
  @Test
  public void testFindReturnForms_shouldReturnAllNotDeletedReturnForms() throws Exception {
    // Set up data
    Location returnedFrom = LocationBuilder.aUsageSite().buildAndPersist(entityManager);
    Location returnedTo = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    ReturnForm returnForm1 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(new Date())
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);
    ReturnForm returnForm2 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(new Date())
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);
    ReturnForm returnForm3 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(new Date())
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .withIsDeleted(true)
        .buildAndPersist(entityManager);

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(null, null, null, null);
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 2, returnForms.size());
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm1));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm2));
    Assert.assertFalse("Correct ReturnForm returned", returnForms.contains(returnForm3));
  }
  
  @Test
  public void testFindReturnForms_shouldReturnMatchingReturnForms() throws Exception {
    // Set up data
    Location returnedFrom = LocationBuilder.aUsageSite().buildAndPersist(entityManager);
    Location returnedTo = LocationBuilder.aDistributionSite().buildAndPersist(entityManager);
    ReturnForm returnForm1 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(new Date())
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);
    ReturnForm returnForm2 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(new Date())
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);
    ReturnForm returnForm3 = ReturnFormBuilder.aReturnForm()
        .withId(null)
        .withReturnDate(new Date())
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withComponent(ComponentBuilder.aComponent().build())
        .buildAndPersist(entityManager);
        
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -5);
    Date returnedDateFrom = calendar.getTime();
    calendar.add(Calendar.DAY_OF_MONTH, 10);
    Date returnedDateTo = calendar.getTime();

    // Run test
    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(returnedDateFrom, returnedDateTo, 
      returnedFrom.getId(), returnedTo.getId());
    
    // Verify
    Assert.assertEquals("Correct number of ReturnForms returned", 3, returnForms.size());
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm1));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm2));
    Assert.assertTrue("Correct ReturnForm returned", returnForms.contains(returnForm3));
  }
}
