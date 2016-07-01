package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ComponentManagementViewModel extends BaseViewModel {

  private ComponentTypeViewModel componentType;
  private Date createdOn;
  private ComponentStatus status;
  private String expiryStatus;
  private String componentCode;
  private Integer weight;
  private Map<String, Boolean> permissions = new HashMap<>();
  private PackTypeFullViewModel packType;

  public ComponentTypeViewModel getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public ComponentStatus getStatus() {
    return status;
  }

  public void setStatus(ComponentStatus status) {
    this.status = status;
  }
  public String getExpiryStatus() {
    return expiryStatus;
  }

  public void setExpiryStatus(String expiryStatus) {
    this.expiryStatus = expiryStatus;
  }

  public String getComponentCode() {
    return componentCode;
  }

  public void setComponentCode(String componentCode) {
    this.componentCode = componentCode;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }

  public PackTypeFullViewModel getPackType() {
    return packType;
  }

  public void setPackType(PackTypeFullViewModel packType) {
    this.packType = packType;
  }

}
