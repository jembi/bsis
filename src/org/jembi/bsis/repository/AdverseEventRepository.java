package org.jembi.bsis.repository;

import org.jembi.bsis.model.adverseevent.AdverseEvent;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.repository.constant.AdverseEventNamedQueryConstants;
import org.springframework.stereotype.Repository;

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
