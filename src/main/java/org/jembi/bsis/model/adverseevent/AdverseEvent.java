package org.jembi.bsis.model.adverseevent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseUUIDEntity;
import org.jembi.bsis.repository.constant.AdverseEventNamedQueryConstants;

/**
 * Entity to store any negative reaction (or adverse event) a Donor might have while making a
 * Donation.
 */
@NamedQueries({
    @NamedQuery(name = AdverseEventNamedQueryConstants.NAME_COUNT_ADVERSE_EVENTS_FOR_DONOR,
        query = AdverseEventNamedQueryConstants.QUERY_COUNT_ADVERSE_EVENTS_FOR_DONOR),
    @NamedQuery(name = AdverseEventNamedQueryConstants.NAME_COUNT_ADVERSE_EVENTS,
        query = AdverseEventNamedQueryConstants.QUERY_COUNT_ADVERSE_EVENTS)
})
@Entity
@Audited
public class AdverseEvent extends BaseUUIDEntity {

  private static final long serialVersionUID = 1L;

  @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
  private AdverseEventType type;

  @Lob
  @Column
  private String comment;

  public AdverseEventType getType() {
    return type;
  }

  public void setType(AdverseEventType type) {
    this.type = type;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
