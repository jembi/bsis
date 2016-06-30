package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;

import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;

import org.jembi.bsis.helpers.builders.ComponentBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ComponentRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private ComponentRepository componentRepository;

  @Test
  public void testUpdateComponentStatusForDonor_shouldOnlyUpdateMatchingComponents() {
    ComponentStatus firstOldStatus = ComponentStatus.AVAILABLE;
    ComponentStatus secondOldStatus = ComponentStatus.QUARANTINED;
    ComponentStatus newStatus = ComponentStatus.UNSAFE;
    Donor donor = aDonor().build();

    Component firstComponentToUpdate = aComponent()
        .withStatus(firstOldStatus)
        .withDonation(aDonation().withDonor(donor).build())
        .buildAndPersist(entityManager);

    Component secondComponentToUpdate = aComponent()
        .withStatus(secondOldStatus)
        .withDonation(aDonation().withDonor(donor).build())
        .buildAndPersist(entityManager);

    Component componentExcludedByStatus = aComponent()
        .withStatus(ComponentStatus.USED)
        .withDonation(aDonation().withDonor(donor).build())
        .buildAndPersist(entityManager);

    Component componentExcludedByDonor = aComponent()
        .withStatus(firstOldStatus)
        .withDonation(aDonation()
            .withDonor(aDonor().build())
            .build())
        .buildAndPersist(entityManager);

    componentRepository.updateComponentStatusesForDonor(Arrays.asList(firstOldStatus, secondOldStatus), newStatus,
        donor);

    entityManager.refresh(firstComponentToUpdate);
    entityManager.refresh(secondComponentToUpdate);
    entityManager.refresh(componentExcludedByStatus);
    entityManager.refresh(componentExcludedByDonor);

    assertThat(firstComponentToUpdate.getStatus(), is(newStatus));
    assertThat(secondComponentToUpdate.getStatus(), is(newStatus));
    assertThat(componentExcludedByStatus.getStatus(), is(ComponentStatus.USED));
    assertThat(componentExcludedByDonor.getStatus(), is(firstOldStatus));
  }

  @Test
  public void testUpdateComponentStatusForDonation_shouldOnlyUpdateMatchingComponents() {
    ComponentStatus firstOldStatus = ComponentStatus.AVAILABLE;
    ComponentStatus secondOldStatus = ComponentStatus.QUARANTINED;
    ComponentStatus newStatus = ComponentStatus.UNSAFE;

    Donation donation = aDonation().build();

    Component firstComponentToUpdate = aComponent()
        .withStatus(firstOldStatus)
        .withDonation(donation)
        .buildAndPersist(entityManager);

    Component secondComponentToUpdate = aComponent()
        .withStatus(secondOldStatus)
        .withDonation(donation)
        .buildAndPersist(entityManager);

    Component componentExcludedByStatus = aComponent()
        .withStatus(ComponentStatus.USED)
        .withDonation(donation)
        .buildAndPersist(entityManager);

    componentRepository.updateComponentStatusForDonation(Arrays.asList(firstOldStatus, secondOldStatus), newStatus,
        donation);

    entityManager.refresh(firstComponentToUpdate);
    entityManager.refresh(secondComponentToUpdate);
    entityManager.refresh(componentExcludedByStatus);

    assertThat(firstComponentToUpdate.getStatus(), is(newStatus));
    assertThat(secondComponentToUpdate.getStatus(), is(newStatus));
    assertThat(componentExcludedByStatus.getStatus(), is(ComponentStatus.USED));
  }
  
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

}
