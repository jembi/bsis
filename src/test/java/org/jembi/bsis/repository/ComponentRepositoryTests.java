package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;

import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;

import org.jembi.bsis.helpers.builders.ComponentBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ComponentRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private ComponentRepository componentRepository;
  
  @Test
  public void testFindComponentByCodeAndDIN_shouldReturnMatchingComponent() {
    
    String componentCode = "0011-01";
    String donationIdentificationNumber = "0000002";
    
    Donation donationWithExpectedDonationIdentificationNumber = aDonation()
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .buildAndPersist(entityManager);
    
    // Excluded by component code
    aComponent()
        .withComponentCode("0011-02")
        .withDonation(donationWithExpectedDonationIdentificationNumber)
        .buildAndPersist(entityManager);
    
    // Excluded by donation identification number
    aComponent()
        .withComponentCode(componentCode)
        .withDonation(aDonation().withDonationIdentificationNumber("1000007").build())
        .buildAndPersist(entityManager);
    
    // Expected
    Component expectedComponent = aComponent()
        .withComponentCode(componentCode)
        .withDonation(donationWithExpectedDonationIdentificationNumber)
        .buildAndPersist(entityManager);
    
    // Test
    Component returnedComponent = componentRepository.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber);;
    
    // Verify
    assertThat(returnedComponent, is(expectedComponent));
  }
  
  @Test(expected = NoResultException.class)
  public void testFindNonExistentComponentByCodeAndDIN_shoulThrow() {
    // Test
    componentRepository.findComponentByCodeAndDIN("", "");
  }


  @Test
  public void testComponentExists() throws Exception {
    Component component = ComponentBuilder.aComponent().buildAndPersist(entityManager);
    Assert.assertTrue("Component exists", componentRepository.verifyComponentExists(component.getId()));
  }

  @Test
  public void testEntityDoesNotExist() throws Exception {
    Assert.assertFalse("Component does not exist", componentRepository.verifyComponentExists(1L));
  }

  @Test
  public void testfindChildComponents() throws Exception {
    Component parentComponent = aComponent().buildAndPersist(entityManager);
    Component child1 = aComponent().withParentComponent(parentComponent).buildAndPersist(entityManager);
    Component child2 = aComponent().withParentComponent(parentComponent).buildAndPersist(entityManager);

    List<Component> children = componentRepository.findChildComponents(parentComponent);

    Assert.assertTrue("contains child1", children.contains(child1));
    Assert.assertTrue("contains child2", children.contains(child2));

  }
  
  @Test
  public void testFindComponentsByDonationIdentificationNumberAndStatus_shouldReturnCorrectComponents() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Donation donation = aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );
    // Excluded by isDeleted
    aComponent()
        .withIsDeleted(true)
        .withStatus(ComponentStatus.DISCARDED)
        .withDonation(donation)
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);
    // Excluded by status
    aComponent()
        .withStatus(ComponentStatus.EXPIRED)
        .withDonation(donation)
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);
    // Excluded by donation
    aComponent()
        .withStatus(ComponentStatus.DISCARDED)
        .withDonation(aDonation().build())
        .buildAndPersist(entityManager);
    
    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findComponentsByDonationIdentificationNumberAndStatus(
        donationIdentificationNumber, ComponentStatus.DISCARDED);
    
    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

}
