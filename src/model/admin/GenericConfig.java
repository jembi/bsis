package model.admin;

import javax.persistence.Column;
import javax.persistence.Entity;

import model.BaseEntity;

@Entity
public class GenericConfig extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 80)
  private String propertyName;

  @Column(length = 80)
  private String propertyValue;

  @Column(length = 30)
  private String propertyOwner;

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String name) {
    this.propertyName = name;
  }

  public String getPropertyValue() {
    return propertyValue;
  }

  public void setPropertyValue(String value) {
    this.propertyValue = value;
  }

  public String getPropertyOwner() {
    return propertyOwner;
  }

  public void setPropertyOwner(String owner) {
    this.propertyOwner = owner;
  }
}
