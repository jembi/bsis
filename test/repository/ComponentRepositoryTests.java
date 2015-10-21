package repository;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.component.Component;
import model.component.ComponentStatus;
import model.donation.Donation;
import model.donor.Donor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import suites.ContextDependentTestSuite;

public class ComponentRepositoryTests extends ContextDependentTestSuite {
    
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ComponentRepository componentRepository;
    
    @Test
    public void testUpdateComponentStatusForDonor_shouldOnlyUpdateMatchingComponents() {
        ComponentStatus oldStatus = ComponentStatus.AVAILABLE;
        ComponentStatus newStatus = ComponentStatus.UNSAFE;
        Donor donor = aDonor().build();
        
        Component firstComponentToUpdate = aComponent()
                .withStatus(oldStatus)
                .withDonation(aDonation().withDonor(donor).build())
                .buildAndPersist(entityManager);
        
        Component secondComponentToUpdate = aComponent()
                .withStatus(oldStatus)
                .withDonation(aDonation().withDonor(donor).build())
                .buildAndPersist(entityManager);

        Component componentExcludedByStatus = aComponent()
                .withStatus(ComponentStatus.USED)
                .withDonation(aDonation().withDonor(donor).build())
                .buildAndPersist(entityManager);

        Component componentExcludedByDonor = aComponent()
                .withStatus(oldStatus)
                .withDonation(aDonation()
                        .withDonor(aDonor().build())
                        .build())
                .buildAndPersist(entityManager);
        
        componentRepository.updateComponentStatusForDonor(oldStatus, newStatus, donor);
        
        entityManager.refresh(firstComponentToUpdate);
        entityManager.refresh(secondComponentToUpdate);
        entityManager.refresh(componentExcludedByStatus);
        entityManager.refresh(componentExcludedByDonor);
        
        assertThat(firstComponentToUpdate.getStatus(), is(newStatus));
        assertThat(secondComponentToUpdate.getStatus(), is(newStatus));
        assertThat(componentExcludedByStatus.getStatus(), is(ComponentStatus.USED));
        assertThat(componentExcludedByDonor.getStatus(), is(oldStatus));
    }
    
    @Test
    public void testUpdateComponentStatusForDonation_shouldOnlyUpdateMatchingComponents() {
        ComponentStatus oldStatus = ComponentStatus.AVAILABLE;
        ComponentStatus newStatus = ComponentStatus.UNSAFE;
        
        Donation donation = aDonation().build();
        
        Component firstComponentToUpdate = aComponent()
                .withStatus(oldStatus)
                .withDonation(donation)
                .buildAndPersist(entityManager);
        
        Component secondComponentToUpdate = aComponent()
                .withStatus(oldStatus)
                .withDonation(donation)
                .buildAndPersist(entityManager);
        
        Component componentExcludedByStatus = aComponent()
                .withStatus(ComponentStatus.USED)
                .withDonation(donation)
                .buildAndPersist(entityManager);

        componentRepository.updateComponentStatusForDonation(oldStatus, newStatus, donation);

        entityManager.refresh(firstComponentToUpdate);
        entityManager.refresh(secondComponentToUpdate);
        entityManager.refresh(componentExcludedByStatus);

        assertThat(firstComponentToUpdate.getStatus(), is(newStatus));
        assertThat(secondComponentToUpdate.getStatus(), is(newStatus));
        assertThat(componentExcludedByStatus.getStatus(), is(ComponentStatus.USED));
    }

}
