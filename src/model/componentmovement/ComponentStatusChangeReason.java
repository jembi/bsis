package model.componentmovement;

import org.hibernate.envers.Audited;

import javax.persistence.*;


@Entity
@Audited
public class ComponentStatusChangeReason {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "SMALLINT")
  private Integer id;

  @Column(length = 100)
  private String statusChangeReason;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private ComponentStatusChangeReasonCategory category;

  private Boolean isDeleted;

  public ComponentStatusChangeReason() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getStatusChangeReason() {
    return statusChangeReason;
  }

  public void setStatusChangeReason(String statusChangeReason) {
    this.statusChangeReason = statusChangeReason;
  }

  public ComponentStatusChangeReasonCategory getCategory() {
    return category;
  }

  public void setCategory(ComponentStatusChangeReasonCategory category) {
    this.category = category;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void copy(ComponentStatusChangeReason componentStatusChangeReason) {
    this.setId(componentStatusChangeReason.getId());
    this.setCategory(componentStatusChangeReason.getCategory());
    this.setStatusChangeReason(componentStatusChangeReason.getStatusChangeReason());
    this.setIsDeleted(componentStatusChangeReason.getIsDeleted());
  }
}
