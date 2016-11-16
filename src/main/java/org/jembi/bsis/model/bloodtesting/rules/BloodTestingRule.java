package org.jembi.bsis.model.bloodtesting.rules;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseModificationTrackerEntity;
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
public class BloodTestingRule extends BaseModificationTrackerEntity {

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

  @Column(length = 60)
  private String pendingTestsIds;

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
   * updating the {@link #pendingTestsIds} string.
   *
   * @return An immutable set of pending test IDs.
   */
  public Set<Long> getPendingTestsIdsSet() {
    if (pendingTestsIds == null || pendingTestsIds.isEmpty()) {
      return Collections.emptySet();
    }
    Set<Long> ids = new HashSet<>();
    StringTokenizer st = new StringTokenizer(pendingTestsIds, ",");
    while (st.hasMoreTokens()) {
      ids.add(Long.valueOf(st.nextToken().trim()));
    }
    return Collections.unmodifiableSet(ids);
  }

  public String getPendingTestingIds() {
    return pendingTestsIds;
  }

  public void setPendingTestsIds(String pendingTestsIds) {
    this.pendingTestsIds = pendingTestsIds;
  }
  
  public void setDonationFieldChanged(DonationField donationFieldChanged) {
    this.donationFieldChanged = donationFieldChanged;
  }
}
