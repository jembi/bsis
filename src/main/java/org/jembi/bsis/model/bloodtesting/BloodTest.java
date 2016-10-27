package org.jembi.bsis.model.bloodtesting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseEntity;
import org.jembi.bsis.model.worksheet.WorksheetType;

@Entity
@Audited
public class BloodTest extends BaseEntity implements Comparable<BloodTest> {

  private static final long serialVersionUID = 1L;

  @Column(length = 25)
  private String testNameShort;

  @Column(length = 40)
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

  /**
   * TODO: not sure if this is useful.
   */
  private Boolean isEmptyAllowed;

  private Boolean isActive;

  private Boolean isDeleted;
  
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
  public List<String> getValidResultsList() {
    if (validResults == null || validResults.isEmpty()) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(Arrays.asList(validResults.split(",")));
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

  public Boolean getIsEmptyAllowed() {
    return isEmptyAllowed;
  }

  public void setIsEmptyAllowed(Boolean isEmptyAllowed) {
    this.isEmptyAllowed = isEmptyAllowed;
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
  public List<String> getNegativeResultsList() {
    if (negativeResults == null || negativeResults.isEmpty()) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(Arrays.asList(negativeResults.split(",")));
  }

  /**
   * @return An immutable list of positive results from the comma separated list.
   */
  public List<String> getPositiveResultsList() {
    if (positiveResults == null || positiveResults.isEmpty()) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(Arrays.asList(positiveResults.split(",")));
  }
}
