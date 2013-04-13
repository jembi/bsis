package model.bloodtesting.rules;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import model.bloodtesting.CollectionField;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class BloodTestRule {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="MEDIUMINT")
  private Integer id;

  @Column(length=200)
  private String bloodTestsIds;

  @Column(length=50)
  private String pattern;

  @Enumerated(EnumType.STRING)
  @Column(length=12)
  private CollectionField collectionFieldChanged;

  @Column(length=30)
  private String newInformation;

  @Column(length=30)
  private String extraInformation;

  @Column(length=60)
  private String extraAboTestsIds;

  @Column(length=60)
  private String extraRhTestsIds;

  @Column(length=60)
  private String extraTtiTestsIds;

  private Boolean markSampleAsUnsafe;

  private Boolean isActive;

  public Integer getId() {
    return id;
  }

  public String getBloodTestsIds() {
    return bloodTestsIds;
  }

  public String getPattern() {
    return pattern;
  }

  public CollectionField getCollectionFieldChanged() {
    return collectionFieldChanged;
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

  public void setBloodTestsIds(String bloodTestsIds) {
    this.bloodTestsIds = bloodTestsIds;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public void setPart(CollectionField part) {
    this.collectionFieldChanged = part;
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

  public String getExtraAboTestsIds() {
    return extraAboTestsIds;
  }

  public void setExtraTestsIds(String extraAboTestsIds) {
    this.extraAboTestsIds = extraAboTestsIds;
  }

  public String getExtraRhTestsIds() {
    return extraRhTestsIds;
  }

  public void setExtraRhTestsIds(String extraRhTestsIds) {
    this.extraRhTestsIds = extraRhTestsIds;
  }

  public String getExtraTtiTestsIds() {
    return extraTtiTestsIds;
  }

  public void setExtraTtiTestsIds(String extraTtiTestsIds) {
    this.extraTtiTestsIds = extraTtiTestsIds;
  }
}
