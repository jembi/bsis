package org.jembi.bsis.backingform;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentPreProcessingBackingForm {

  private UUID id;
  private Integer weight;
  private Date bleedStartTime;
  private Date bleedEndTime;

  public Date getBleedStartTime () {
    return bleedStartTime;
  }

  public void setBleedStartTime (Date bleedStartTime) {
    this.bleedStartTime = bleedStartTime;
  }

  public Date getBleedEndTime () {
    return bleedEndTime;
  }

  public void setBleedEndTime (Date bleedEndTime) {
    this.bleedEndTime = bleedEndTime;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }
}