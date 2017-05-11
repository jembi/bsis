package org.jembi.bsis.model.bloodtesting.rules;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.repository.BloodTestingRuleNamedQueryConstants;

@Entity
@Audited
@NamedQueries({
  @NamedQuery(
      name = BloodTestingRuleNamedQueryConstants.NAME_GET_BLOOD_TESTING_RULES,
      query = BloodTestingRuleNamedQueryConstants.QUERY_GET_BLOOD_TESTING_RULES),
  @NamedQuery(
      name = BloodTestingRuleNamedQueryConstants.NAME_FIND_BLOOD_TESTING_RULE_BY_ID,
      query = BloodTestingRuleNamedQueryConstants.QUERY_FIND_BLOOD_TESTING_RULE_BY_ID)
})
public class BloodTestingRule extends BaseModificationTrackerUUIDEntity {

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

  @ManyToMany(fetch = FetchType.EAGER)
  private List<BloodTest> pendingBloodTests;

  @Column(nullable = false)
  private boolean isDeleted = false;

  public BloodTest getBloodTest() {
    return bloodTest;
  }

  public String getPattern() {
    return pattern;
  }

  public DonationField getDonationFieldChanged() {
    return donationFieldChanged;
  }
  
  public boolean getIsDeleted() {
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
  
  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getNewInformation() {
    return newInformation;
  }

  public void setNewInformation(String newInformation) {
    this.newInformation = newInformation;
  }

  /**
   * Get the pending test IDs as a set. The Set cannot be modified since changes can only be made by
   * updating the {@link #pendingBloodTests} array.
   *
   * @return An immutable set of pending test IDs.
   */
  public Set<UUID> getPendingTestsIdsSet() {
    if (pendingBloodTests == null || pendingBloodTests.isEmpty()) {
      return Collections.emptySet();
    }
    Set<UUID> ids = new HashSet<>();
    for (BloodTest pendingBloodTest : pendingBloodTests) {
      ids.add(pendingBloodTest.getId());
    }
    return Collections.unmodifiableSet(ids);
  }

  public List<BloodTest> getPendingBloodTests() {
    return pendingBloodTests;
  }

  public void setPendingBloodTests(List<BloodTest> pendingBloodTests) {
    this.pendingBloodTests = pendingBloodTests;
  }
  
  public void setDonationFieldChanged(DonationField donationFieldChanged) {
    this.donationFieldChanged = donationFieldChanged;
  }
}
