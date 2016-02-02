package repository;

import model.adverseevent.AdverseEvent;
import model.donor.Donor;
import org.springframework.stereotype.Repository;
import repository.constant.AdverseEventNamedQueryConstants;

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
