package repository;

import org.springframework.stereotype.Repository;

import repository.constant.AdverseEventNamedQueryConstants;
import model.adverseevent.AdverseEvent;
import model.donor.Donor;

@Repository
public class AdverseEventRepository extends AbstractRepository<AdverseEvent> {
    
    public int countAdverseEventsForDonor(Donor donor) {
        return entityManager.createNamedQuery(
                AdverseEventNamedQueryConstants.NAME_COUNT_ADVERSE_EVENTS_FOR_DONOR,
                Number.class)
                .setParameter("deleted", false)
                .setParameter("donor", donor)
                .getSingleResult()
                .intValue();
    }

}
