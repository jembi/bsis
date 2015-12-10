package model.bloodtesting.rules;

import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestContext;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Audited
public class BloodTestingRule {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "SMALLINT")
  private Integer id;

  /**
   * Comma Separated list of ids of tests which correspond to the pattern.
   */
  @Column(length = 200)
  private String bloodTestsIds;

  @Column(length = 50)
  private String pattern;

  @Enumerated(EnumType.STRING)
  @Column(length = 12)
  private DonationField donationFieldChanged;

  @Column(length = 30)
  private String newInformation;

  @Column(length = 30)
  private String extraInformation;

  @Column(length = 60)
  private String pendingTestsIds;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private BloodTestCategory category;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private BloodTestSubCategory subCategory;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private BloodTestContext context;

  /**
   * TODO: Not used right now.
   */
  private Boolean markSampleAsUnsafe;

  private Boolean isActive;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getBloodTestsIds() {
    return bloodTestsIds;
  }

  public void setBloodTestsIds(String bloodTestsIds) {
    this.bloodTestsIds = bloodTestsIds;
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public DonationField getDonationFieldChanged() {
    return donationFieldChanged;
  }

  public void setDonationFieldChanged(DonationField donationFieldChanged) {
    this.donationFieldChanged = donationFieldChanged;
  }

  public Boolean getMarkSampleAsUnsafe() {
    return markSampleAsUnsafe;
  }

  public void setMarkSampleAsUnsafe(Boolean markSampleAsUnsafe) {
    this.markSampleAsUnsafe = markSampleAsUnsafe;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public void setPart(DonationField part) {
    this.setDonationFieldChanged(part);
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
}
