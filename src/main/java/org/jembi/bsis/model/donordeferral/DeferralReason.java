package org.jembi.bsis.model.donordeferral;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.repository.DeferralReasonNamedQueryConstants;

@NamedQueries({
    @NamedQuery(name = DeferralReasonNamedQueryConstants.NAME_FIND_DEFERRAL_REASON_BY_TYPE,
        query = DeferralReasonNamedQueryConstants.QUERY_FIND_DEFERRAL_REASON_BY_TYPE),
    @NamedQuery(name = DeferralReasonNamedQueryConstants.NAME_COUNT_DEFERRAL_REASONS_FOR_ID,
        query = DeferralReasonNamedQueryConstants.QUERY_COUNT_DEFERRAL_REASONS_FOR_ID),
    @NamedQuery(name = DeferralReasonNamedQueryConstants.NAME_FIND_ALL_DEFERRAL_REASONS,
        query = DeferralReasonNamedQueryConstants.QUERY_FIND_ALL_DEFERRAL_REASONS),
        
})
@Entity
@Audited
public class DeferralReason extends BaseModificationTrackerUUIDEntity {

  private static final long serialVersionUID = 203754154113421034L;

  @Column(length = 100)
  private String reason;

  private Boolean isDeleted;

  @Column(nullable = true)
  private Integer defaultDuration; // in days

  @Column(length = 30, nullable = false)
  @Enumerated(EnumType.STRING)
  private DeferralReasonType type = DeferralReasonType.NORMAL;

  @Column(length = 30, nullable = false)
  @Enumerated(EnumType.STRING)
  private DurationType durationType = DurationType.TEMPORARY;

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void copy(DeferralReason deferralReason) {
    setId(deferralReason.getId());
    this.reason = deferralReason.getReason();
    this.isDeleted = deferralReason.getIsDeleted();
    this.defaultDuration = deferralReason.getDefaultDuration();
    this.durationType = deferralReason.getDurationType();
  }

  public Integer getDefaultDuration() {
    return defaultDuration;
  }

  public void setDefaultDuration(Integer defaultDuration) {
    this.defaultDuration = defaultDuration;
  }

  public DeferralReasonType getType() {
    return type;
  }

  public void setType(DeferralReasonType type) {
    this.type = type;
  }

  public DurationType getDurationType() {
    return durationType;
  }

  public void setDurationType(DurationType durationType) {
    this.durationType = durationType;
  }

}
