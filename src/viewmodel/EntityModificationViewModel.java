package viewmodel;

import org.hibernate.envers.RevisionType;

public class EntityModificationViewModel {

  private Long id;
  private RevisionType revisionType;
  private String entityName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RevisionType getRevisionType() {
    return revisionType;
  }

  public void setRevisionType(RevisionType revisionType) {
    this.revisionType = revisionType;
  }

  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }

    if (!(other instanceof EntityModificationViewModel)) {
      return false;
    }

    EntityModificationViewModel entityModificationViewModel = (EntityModificationViewModel) other;

    return entityModificationViewModel.id == id &&
        entityModificationViewModel.revisionType == revisionType &&
        entityName == null
        ? entityModificationViewModel.entityName == null
        : entityName.equals(entityModificationViewModel.entityName);
  }

}
