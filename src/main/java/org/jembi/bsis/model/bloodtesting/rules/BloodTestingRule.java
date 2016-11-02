package org.jembi.bsis.model.bloodtesting.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseEntity;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestContext;
import org.jembi.bsis.repository.BloodTestingRuleNamedQueryConstants;

@Entity
@Audited
@NamedQueries({
  @NamedQuery(
      name = BloodTestingRuleNamedQueryConstants.NAME_GET_BLOOD_TESTING_RULES,
      query = BloodTestingRuleNamedQueryConstants.QUERY_GET_BLOOD_TESTING_RULES)
})
public class BloodTestingRule extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  private BloodTest bloodTest;

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

  @Column(nullable = false)
  private Boolean isDeleted = Boolean.FALSE;

  public BloodTest getBloodTest() {
    return bloodTest;
  }

  public String getPattern() {
    return pattern;
  }

  public DonationField getDonationFieldChanged() {
    return donationFieldChanged;
  }

  public Boolean getMarkSampleAsUnsafe() {
    return markSampleAsUnsafe;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public void setPart(DonationField part) {
    this.setDonationFieldChanged(part);
  }

  public void setMarkSampleAsUnsafe(Boolean markSampleAsUnsafe) {
    this.markSampleAsUnsafe = markSampleAsUnsafe;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
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

  public List<String> getPendingTestsIds() {
    if (pendingTestsIds == null || pendingTestsIds.equals("")) {
      return new ArrayList<String>(0);
    }
    return Arrays.asList(pendingTestsIds.split(","));
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

  public void setDonationFieldChanged(DonationField donationFieldChanged) {
    this.donationFieldChanged = donationFieldChanged;
  }
}
