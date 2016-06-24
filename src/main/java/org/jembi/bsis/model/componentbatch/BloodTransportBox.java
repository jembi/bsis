package org.jembi.bsis.model.componentbatch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseEntity;

@Entity
@Audited
public class BloodTransportBox extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Column
  private double temperature;
  
  @ManyToOne(optional = true)
  private ComponentBatch componentBatch;
  
  @Column
  private boolean isDeleted = false;
  
  public BloodTransportBox() {
    super();
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public ComponentBatch getComponentBatch() {
    return componentBatch;
  }

  public void setComponentBatch(ComponentBatch componentBatch) {
    this.componentBatch = componentBatch;
  }
  
  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
