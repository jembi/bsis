package model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Abstract parent class for all entities - model objects that are persisted to the data store. It
 * provides a Long id and default implementations of toString, equals and hashCode.
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false)
  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("hashCode", Integer.toHexString(hashCode()))
        .append("id", getId())
        .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (!(obj instanceof BaseEntity)) {
      // This entity has a different type
      return false;
    }

    if (getId() == null) {
      // This entity has not been persisted
      return super.equals(obj);
    }

    BaseEntity other = (BaseEntity) obj;

    return getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    if (getId() == null) {
      // This entity has not been persisted
      return super.hashCode();
    }
    return Objects.hashCode(getId());
  }
}
