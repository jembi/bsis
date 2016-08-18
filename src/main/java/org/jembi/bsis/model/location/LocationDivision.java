package org.jembi.bsis.model.location;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;
import org.jembi.bsis.model.BaseEntity;
import org.jembi.bsis.repository.LocationNamedQueryConstants;

/**
 * Entity representing a Division to which a Location belongs.
 */
@Entity
@Audited
public class LocationDivision extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @NotBlank
  private String name;

  private int level;

  private LocationDivision parentDivision = null;

  private Boolean isDeleted = Boolean.FALSE;

  @Lob
  private String notes;

  public LocationDivision() {
  }

  public void copy(LocationDivision location) {
    this.name = location.name;
    this.level = location.level;
    this.parentDivision = location.parentDivision;
    this.isDeleted = location.isDeleted;
  }

  public String getName() {
    return name;
  }

  public int getLevel() {
    return level;
  }

  public LocationDivision getParentDivision() {
    return parentDivision;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public void setParentDivision(LocationDivision parentDivision) {
    this.parentDivision = parentDivision;
  }
}
