package repository;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;

import model.component.Component;
import model.component.ComponentStatus;
import model.donation.Donation;
import model.donor.Donor;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.ContextDependentTestSuite;

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

}
