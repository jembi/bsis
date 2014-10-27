package model.bloodtesting;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import model.worksheet.WorksheetType;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class BloodTest implements Comparable<BloodTest> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="SMALLINT")
  private Integer id;

  @Column(length=25)
  private String testNameShort;

  @Column(length=40)
  private String testName;

  /**
   * Comma separated list of valid values.
   * Typically '+,-'
   */
  private String validResults;

  /**
   * TODO: not used now but will be useful for mapping numeric validResults to '-'.
   */
  private String negativeResults;

  /**
   * TODO: not used now but will be useful for mapping numeric validResults to '+'.
   */
  private String positiveResults;

  private Integer rankInCategory;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  private BloodTestType bloodTestType;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  private BloodTestCategory category;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
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

  public Integer getId() {
    return id;
  }

  public String getTestNameShort() {
    return testNameShort;
  }

  public String getTestName() {
    return testName;
  }

  public String getValidResults() {
    return validResults;
  }

  public String getNegativeResults() {
    return negativeResults;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setId(Integer id) {
    this.id = id;
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
    return this.id.compareTo(o.id);
  }
}
