package repository;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.ReturnFormBuilder.aReturnForm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import helpers.builders.ComponentBuilder;
import helpers.builders.LocationBuilder;
import helpers.builders.ReturnFormBuilder;
import model.component.Component;
import model.location.Location;
import model.returnform.ReturnForm;
import model.returnform.ReturnStatus;
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
  
}
