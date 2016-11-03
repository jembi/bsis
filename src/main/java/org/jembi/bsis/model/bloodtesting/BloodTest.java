package org.jembi.bsis.model.bloodtesting;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseModificationTrackerEntity;
import org.jembi.bsis.model.worksheet.WorksheetType;
import org.jembi.bsis.repository.constant.BloodTestNamedQueryConstants;

@Entity
@Audited
@NamedQueries({
  @NamedQuery(
      name = BloodTestNamedQueryConstants.NAME_GET_BLOOD_TESTS_BY_CATEGORY,
      query = BloodTestNamedQueryConstants.QUERY_GET_BLOOD_TESTS_BY_CATEGORY),
  @NamedQuery(
      name = BloodTestNamedQueryConstants.NAME_GET_BLOOD_TESTS_BY_TYPE,
      query = BloodTestNamedQueryConstants.QUERY_GET_BLOOD_TESTS_BY_TYPE),
  @NamedQuery(
      name = BloodTestNamedQueryConstants.NAME_GET_BLOOD_TESTS,
      query = BloodTestNamedQueryConstants.QUERY_GET_BLOOD_TESTS),
  @NamedQuery(
      name = BloodTestNamedQueryConstants.NAME_FIND_BLOOD_TEST_BY_ID,
      query = BloodTestNamedQueryConstants.QUERY_FIND_BLOOD_TEST_BY_ID),
  @NamedQuery(
      name = BloodTestNamedQueryConstants.NAME_FIND_COUNT_BY_TEST_NAME_AND_ID,
      query = BloodTestNamedQueryConstants.QUERY_FIND_COUNT_BY_TEST_NAME_AND_ID)
})
public class BloodTest extends BaseModificationTrackerEntity implements Comparable<BloodTest> {

  private static final long serialVersionUID = 1L;

  @Column(length = 25, nullable = false)
  private String testNameShort;

  @Column(length = 40, unique = true, nullable = false)
  private String testName;

  /**
   * Comma separated list of valid values. Typically '+,-'
   */
  private String validResults;

  /**
   * TODO: not used now but will be useful for mapping numeric validResults to '-'.
   */
  private String negativeResults;

  /**
   * A comma separated list of results which count as positive.
   */
  private String positiveResults;

  private Integer rankInCategory;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private BloodTestType bloodTestType;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private BloodTestCategory category;

  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private BloodTestContext context;

  /**
   * List cannot be used here.
   */
  @ManyToMany
  private Set<WorksheetType> worksheetTypes;

  @Column(nullable = false)
  private Boolean isActive = Boolean.TRUE;

  @Column(nullable = false)
  private Boolean isDeleted = Boolean.FALSE;
  
  @Column(nullable = false)
  private boolean flagComponentsContainingPlasmaForDiscard = false;

  /**
   * Whether or not to flag associated components for discard when a test has a positive outcome.
   */
  @Column(nullable = false)
  private boolean flagComponentsForDiscard = false;

  public String getTestNameShort() {
    return testNameShort;
  }

  public String getTestName() {
    return testName;
  }

  public String getValidResults() {
    return validResults;
  }

  /**
   * Get the valid results for this test as a list. The list cannot be modified since changes to the
   * valid results must be done by updating the {@link #validResults} string.
   *
   * @return An immutable list of valid results.
   */
  public Set<String> getValidResultsList() {
    if (validResults == null || validResults.isEmpty()) {
      return Collections.emptySet();
    }
    return Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(validResults.split(","))));
  }

  public String getNegativeResults() {
    return negativeResults;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public boolean getFlagComponentsContainingPlasmaForDiscard() {
    return flagComponentsContainingPlasmaForDiscard;
  }

  public void setFlagComponentsContainingPlasmaForDiscard(boolean flagComponentsContainingPlasmaForDiscard) {
    this.flagComponentsContainingPlasmaForDiscard = flagComponentsContainingPlasmaForDiscard;
  }

  public void setTestNameShort(String testNameShort) {
    this.testNameShort = testNameShort;
  }

  public void setTestName(String testName) {
    this.testName = testName;
  }

  public void setValidResults(String validResults) {
    this.validResults = validResults;
  }

  public void setNegativeResults(String negativeResults) {
    this.negativeResults = negativeResults;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Integer getRankInCategory() {
    return rankInCategory;
  }

  public void setRankInCategory(Integer rankInCategory) {
    this.rankInCategory = rankInCategory;
  }

  public String getPositiveResults() {
    return positiveResults;
  }

  public void setPositiveResults(String positiveResults) {
    this.positiveResults = positiveResults;
  }

  public BloodTestType getBloodTestType() {
    return bloodTestType;
  }

  public void setBloodTestType(BloodTestType bloodTypingTestType) {
    this.bloodTestType = bloodTypingTestType;
  }

  public BloodTestCategory getCategory() {
    return category;
  }

  public void setCategory(BloodTestCategory category) {
    this.category = category;
  }

  public Set<WorksheetType> getWorksheetTypes() {
    return worksheetTypes;
  }

  public void setWorksheetTypes(Set<WorksheetType> worksheetTypes) {
    this.worksheetTypes = worksheetTypes;
  }

  public BloodTestContext getContext() {
    return context;
  }

  public void setContext(BloodTestContext context) {
    this.context = context;
  }


  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  @Override
  public int compareTo(BloodTest o) {
    return this.getId().compareTo(o.getId());
  }

  public boolean isFlagComponentsForDiscard() {
    return flagComponentsForDiscard;
  }

  public void setFlagComponentsForDiscard(boolean flagComponentsForDiscard) {
    this.flagComponentsForDiscard = flagComponentsForDiscard;
  }

  /**
   * @return An immutable list of negative results from the comma separated list.
   */
  public Set<String> getNegativeResultsList() {
    if (negativeResults == null || negativeResults.isEmpty()) {
      return Collections.emptySet();
    }
    return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(negativeResults.split(","))));
  }

  /**
   * @return An immutable list of positive results from the comma separated list.
   */
  public Set<String> getPositiveResultsList() {
    if (positiveResults == null || positiveResults.isEmpty()) {
      return Collections.emptySet();
    }
    return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(positiveResults.split(","))));
  }
}
