package model.bloodtesting.rules;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestContext;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class BloodTestingRule {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="SMALLINT")
  private Integer id;

  /**
   * Comma Separated list of ids of tests which correspond to the pattern.
   */
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
  private String pendingTestsIds;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  private BloodTestCategory category;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  private BloodTestSubCategory subCategory;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  private BloodTestContext context;

  /**
   * TODO: Not used right now.
   */
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
    this.setCollectionFieldChanged(part);
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

  public BloodTestContext getContext() {
    return context;
  }

  public void setContext(BloodTestContext context) {
    this.context = context;
  }

  public BloodTestCategory getCategory() {
    return category;
  }

  public void setCategory(BloodTestCategory category) {
    this.category = category;
  }

  public String getPendingTestsIds() {
    return pendingTestsIds;
  }

  public void setPendingTestsIds(String pendingTestsIds) {
    this.pendingTestsIds = pendingTestsIds;
  }

  public BloodTestSubCategory getSubCategory() {
    return subCategory;
  }

  public void setSubCategory(BloodTestSubCategory subCategory) {
    this.subCategory = subCategory;
  }

  public void setCollectionFieldChanged(CollectionField collectionFieldChanged) {
    this.collectionFieldChanged = collectionFieldChanged;
  }
}
