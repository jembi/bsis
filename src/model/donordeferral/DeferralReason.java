package model.donordeferral;

import org.hibernate.envers.Audited;
import repository.DeferralReasonNamedQueryConstants;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = DeferralReasonNamedQueryConstants.NAME_FIND_DEFERRAL_REASON_BY_TYPE,
                query = DeferralReasonNamedQueryConstants.QUERY_FIND_DEFERRAL_REASON_BY_TYPE)
})
@Entity
@Audited
public class DeferralReason {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "SMALLINT")
  private Integer id;

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

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

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
    this.id = deferralReason.getId();
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
