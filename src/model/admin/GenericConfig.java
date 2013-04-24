package model.admin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GenericConfig {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false)
  private Long id;

  @Column(length=80)
  private String propertyName;

  @Column(length=80)
  private String propertyValue;

  @Column(length=30)
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
}
