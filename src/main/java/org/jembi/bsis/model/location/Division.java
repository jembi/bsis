package org.jembi.bsis.model.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.repository.constant.DivisionNamedQueryConstants;

/**
 * Entity representing a Division to which a Location belongs.
 */
@NamedQueries({
    @NamedQuery(name = DivisionNamedQueryConstants.NAME_FIND_DIVISION_BY_ID,
        query = DivisionNamedQueryConstants.QUERY_FIND_DIVISION_BY_ID),
    @NamedQuery(name = DivisionNamedQueryConstants.NAME_FIND_DIVISION_BY_NAME,
        query = DivisionNamedQueryConstants.QUERY_FIND_DIVISION_BY_NAME),
    @NamedQuery(name = DivisionNamedQueryConstants.NAME_COUNT_DIVISIONS_WITH_PARENT,
        query = DivisionNamedQueryConstants.QUERY_COUNT_DIVISIONS_WITH_PARENT),
    @NamedQuery(name = DivisionNamedQueryConstants.NAME_GET_ALL_DIVISIONS,
        query = DivisionNamedQueryConstants.QUERY_GET_ALL_DIVISIONS)
})
@Entity
@Audited
public class Division extends BaseModificationTrackerUUIDEntity {

  private static final long serialVersionUID = 1L;

  @NotBlank
  @Column(length = 255, nullable = false, unique = true)
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
