package repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import helpers.builders.ComponentBuilder;
import helpers.builders.LocationBuilder;
import helpers.builders.ReturnFormBuilder;
import model.component.Component;
import model.location.Location;
import model.returnform.ReturnForm;
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
  
}
