package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.DonorsAdverseEventsDTO;
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

  public List<DonorsAdverseEventsDTO> countAdverseEvents(Long venueId, Date startDate, Date endDate) {
    return entityManager.createNamedQuery(AdverseEventNamedQueryConstants.NAME_COUNT_ADVERSE_EVENTS, DonorsAdverseEventsDTO.class)
        .setParameter("donationDeleted", false)
        .setParameter("adverseEventTypeDeleted", false)
        .setParameter("venueDeleted", false)
        .setParameter("venueId", venueId)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .getResultList();
  }

}
