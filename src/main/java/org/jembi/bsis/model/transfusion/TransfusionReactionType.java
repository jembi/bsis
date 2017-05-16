package org.jembi.bsis.model.transfusion;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.repository.constant.TransfusionReactionTypeNamedQueryConstants;

/**
 * Entity representing a Transfusion Reaction Type.
 */
@NamedQueries({
  @NamedQuery(name = TransfusionReactionTypeNamedQueryConstants.NAME_GET_ALL_TRANSFUSION_REACTION_TYPES,
        query = TransfusionReactionTypeNamedQueryConstants.QUERY_GET_ALL_TRANSFUSION_REACTION_TYPES),
  @NamedQuery(name = TransfusionReactionTypeNamedQueryConstants.NAME_FIND_BY_ID,
        query = TransfusionReactionTypeNamedQueryConstants.QUERY_FIND_BY_ID),
  @NamedQuery(name = TransfusionReactionTypeNamedQueryConstants.NAME_VERIFY_UNIQUE_TRANSFUSION_REACTION_TYPE_NAME,
  query = TransfusionReactionTypeNamedQueryConstants.QUERY_VERIFY_UNIQUE_TRANSFUSION_REACTION_TYPE_NAME)
})
@Entity
@Audited
public class TransfusionReactionType extends BaseModificationTrackerUUIDEntity {

  private static final long serialVersionUID = 1L;

  @NotBlank
  @Column(length = 255, unique = true, nullable = false)
  private String name;

  @Column
  private String description;

  private boolean isDeleted;

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
