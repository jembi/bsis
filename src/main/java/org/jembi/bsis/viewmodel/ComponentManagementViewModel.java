package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ComponentManagementViewModel extends BaseViewModel<UUID> {

  private ComponentTypeViewModel componentType;
  private Date createdOn;
  private Date expiresOn;
  private ComponentStatus status;
  private int daysToExpire;
  private String componentCode;
  private Integer weight;
  private Map<String, Boolean> permissions = new HashMap<>();
  private PackTypeFullViewModel packType;
  @JsonProperty("hasComponentBatch")
  private boolean batched;
  private InventoryStatus inventoryStatus;
  private Date donationDateTime;
  private Date bleedStartTime;
  private Date bleedEndTime;
  private UUID parentComponentId;
  private String discardReason;
  private String discardReasonComment;

  @Builder
  @SuppressWarnings("unused")
  public ComponentManagementViewModel(
      UUID id, ComponentTypeViewModel componentType, Date createdOn, Date expiresOn, ComponentStatus status,
      int daysToExpire, String componentCode, Integer weight, Map<String, Boolean> permissions,
      PackTypeFullViewModel packType, boolean batched, InventoryStatus inventoryStatus, Date donationDateTime,
      Date bleedStartTime, Date bleedEndTime, UUID parentComponentId, String discardReason, String discardReasonComment) {
    setId(id);
    this.componentType = componentType;
    this.createdOn = createdOn;
    this.expiresOn = expiresOn;
    this.status = status;
    this.daysToExpire = daysToExpire;
    this.componentCode = componentCode;
    this.weight = weight;
    this.permissions = permissions;
    this.packType = packType;
    this.batched = batched;
    this.inventoryStatus = inventoryStatus;
    this.donationDateTime = donationDateTime;
    this.bleedStartTime = bleedStartTime;
    this.bleedEndTime = bleedEndTime;
    this.parentComponentId = parentComponentId;
    this.discardReason = discardReason;
    this.discardReasonComment = discardReasonComment;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getBleedStartTime() {
    return bleedStartTime;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getBleedEndTime() {
    return bleedEndTime;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getCreatedOn() {
    return createdOn;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getExpiresOn() {
    return expiresOn;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDonationDateTime() {
    return donationDateTime;
  }
}
