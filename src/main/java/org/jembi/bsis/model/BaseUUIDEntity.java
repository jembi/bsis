package org.jembi.bsis.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract parent class for all entities that use a UUID type identifier. Entities are model
 * objects that are persisted to the data store. This class provides a UUID id and inherits the
 * default implementations of toString, equals and hashCode from the BSISEntity.
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class BaseUUIDEntity extends BSISEntity<UUID> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO) // FIXME, this needs to be updated to our custom generator. 
  @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, insertable = false, updatable = false)
  private UUID id;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }
}
