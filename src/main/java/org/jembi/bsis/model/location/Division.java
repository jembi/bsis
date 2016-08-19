package org.jembi.bsis.model.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;
import org.jembi.bsis.model.BaseModificationTrackerEntity;

/**
 * Entity representing a Division to which a Location belongs.
 */
@Entity
@Audited
public class Division extends BaseModificationTrackerEntity {

  private static final long serialVersionUID = 1L;

  @NotBlank
  @Column(length = 255, nullable = false)
  private String name;

  @Column
  private int level;

  @ManyToOne(optional = true)
  private Division parent = null;

  public String getName() {
    return name;
  }

  public int getLevel() {
    return level;
  }

  public Division getParent() {
    return parent;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public void setParent(Division parent) {
    this.parent = parent;
  }

}
