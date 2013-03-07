package model.bloodtest;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.StringUtils;

@Entity
public class BloodTest {

  @Id
  @Column(nullable=false, updatable=false, insertable=false, length=30)
  private String name;

  private String displayName;

  private String allowedResults;

  private String resultCalculated;

  private String positiveResults;

  private String negativeResults;

  private Boolean negativeRequiredForUse;
  
  private Boolean isRequired;

  private Boolean isConfidential;

  private Boolean isFinalOutcome;

  @ManyToMany
  @JoinTable(name="BloodTest_TestsRequiredIfPositive")
  private List<BloodTest> testsRequiredIfPositive;

  @ManyToMany
  @JoinTable(name="BloodTest_TestsRequiredIfNegative")
  private List<BloodTest> testsRequiredIfNegative;

  @Lob
  private String notes;

  private Boolean isDeleted;

  public String getName() {
    return name;
  }

  public String getNotes() {
    return notes;
  }

  public Boolean getIsRequired() {
    return isRequired;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public List<String> getAllowedResults() {
    if (allowedResults == null)
      return null;
    return Arrays.asList(allowedResults.split(","));
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsRequired(Boolean isRequired) {
    this.isRequired = isRequired;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void setAllowedResults(
      List<String> allowedResults) {
    this.allowedResults = StringUtils.join(allowedResults, ",");
  }

  public Boolean getIsConfidential() {
    return isConfidential;
  }

  public void setIsConfidential(Boolean isConfidential) {
    this.isConfidential = isConfidential;
  }

  public Boolean getIsFinalOutcome() {
    return isFinalOutcome;
  }

  public void setIsFinalOutcome(Boolean isFinalOutcome) {
    this.isFinalOutcome = isFinalOutcome;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getPositiveResults() {
    return positiveResults;
  }

  public void setPositiveResults(String positiveResults) {
    this.positiveResults = positiveResults;
  }

  public String getNegativeResults() {
    return negativeResults;
  }

  public void setNegativeResults(String negativeResults) {
    this.negativeResults = negativeResults;
  }

  public Boolean getNegativeRequiredForUse() {
    return negativeRequiredForUse;
  }

  public void setNegativeRequiredForUse(Boolean negativeRequiredForUse) {
    this.negativeRequiredForUse = negativeRequiredForUse;
  }

  public String getResultCalculated() {
    return resultCalculated;
  }

  public void setResultCalculated(String resultCalculated) {
    this.resultCalculated = resultCalculated;
  }

  public List<BloodTest> getTestsRequiredIfNegative() {
    return testsRequiredIfNegative;
  }

  public void setTestsRequiredIfNegative(List<BloodTest> testsRequiredIfNegative) {
    this.testsRequiredIfNegative = testsRequiredIfNegative;
  }

  public List<BloodTest> getTestsRequiredIfPositive() {
    return testsRequiredIfPositive;
  }

  public void setTestsRequiredIfPositive(List<BloodTest> testsRequiredIfPositive) {
    this.testsRequiredIfPositive = testsRequiredIfPositive;
  }
}
