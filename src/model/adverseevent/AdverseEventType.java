package model.adverseevent;

import org.hibernate.envers.Audited;
import repository.constant.AdverseEventTypeNamedQueryConstants;

import javax.persistence.*;

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
public class AdverseEventType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, updatable = false, insertable = false)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Lob
  @Column(nullable = true)
  private String description;

  @Column(nullable = false)
  private boolean isDeleted;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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
