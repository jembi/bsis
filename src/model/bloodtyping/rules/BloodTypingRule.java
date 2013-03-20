package model.bloodtyping.rules;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import model.bloodtyping.BloodGroupPart;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class BloodTypingRule {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="TINYINT")
  private Integer id;

  @Column(length=200)
  private String bloodTypingTestIds;

  @Column(length=50)
  private String pattern;

  @Enumerated(EnumType.STRING)
  @Column(length=12)
  private BloodGroupPart partOfBloodGroupChanged;

  @Column(length=30)
  private String newInformation;

  @Column(length=30)
  private String extraInformation;

  @Column(length=30)
  private String extraTestsIds;

  private Boolean markSampleAsUnsafe;

  private Boolean isActive;

  public Integer getId() {
    return id;
  }

  public String getBloodTypingTestIds() {
    return bloodTypingTestIds;
  }

  public String getPattern() {
    return pattern;
  }

  public BloodGroupPart getPart() {
    return partOfBloodGroupChanged;
  }

  public String getExtraTestsIds() {
    return extraTestsIds;
  }

  public Boolean getMarkSampleAsUnsafe() {
    return markSampleAsUnsafe;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setBloodTypingTestIds(String bloodTypingTestIds) {
    this.bloodTypingTestIds = bloodTypingTestIds;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public void setPart(BloodGroupPart part) {
    this.partOfBloodGroupChanged = part;
  }

  public void setExtraTestsIds(String extraTestsIds) {
    this.extraTestsIds = extraTestsIds;
  }

  public void setMarkSampleAsUnsafe(Boolean markSampleAsUnsafe) {
    this.markSampleAsUnsafe = markSampleAsUnsafe;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public String getNewInformation() {
    return newInformation;
  }

  public void setNewInformation(String newInformation) {
    this.newInformation = newInformation;
  }

  public String getExtraInformation() {
    return extraInformation;
  }

  public void setExtraInformation(String extraInformation) {
    this.extraInformation = extraInformation;
  }
}
