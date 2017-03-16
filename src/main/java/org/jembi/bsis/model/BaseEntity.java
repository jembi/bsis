package org.jembi.bsis.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract parent class for all entities that use a Long type identifier. Entities are model
 * objects that are persisted to the data store. This class provides a Long id and inherits the
 * default implementations of toString, equals and hashCode from the BSISEntity.
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class BaseEntity extends BSISEntity<Long> {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
