package org.jembi.bsis.model.adverseevent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.repository.constant.AdverseEventTypeNamedQueryConstants;

/**
 * Entity defining the kind of AdverseEvents (negative reactions) supported in the application.
 */
@NamedQueries({
    @NamedQuery(name = AdverseEventTypeNamedQueryConstants.NAME_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS,
        query = AdverseEventTypeNamedQueryConstants.QUERY_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS),
    @NamedQuery(name = AdverseEventTypeNamedQueryConstants.NAME_FIND_ADVERSE_EVENT_TYPE_IDS_BY_NAME,
        query = AdverseEventTypeNamedQueryConstants.QUERY_FIND_ADVERSE_EVENT_TYPE_IDS_BY_NAME),
    @NamedQuery(name = AdverseEventTypeNamedQueryConstants.NAME_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS_WITH_DELETED_FLAG,
        query = AdverseEventTypeNamedQueryConstants.QUERY_FIND_ADVERSE_EVENT_TYPE_VIEW_MODELS_WITH_DELETED_FLAG)
})
@Entity
@Audited
public class AdverseEventType extends BaseModificationTrackerUUIDEntity {

  private static final long serialVersionUID = 1L;

  @Column(nullable = false, unique = true)
  private String name;

  @Lob
  @Column(nullable = true)
  private String description;

  @Column(nullable = false)
  private boolean isDeleted;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
