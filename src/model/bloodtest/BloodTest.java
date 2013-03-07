package model.bloodtest;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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

  private String correctResult;

  private Boolean isRequired;

  private Boolean isConfidential;

  private Boolean isFinalOutcome;

  @ManyToMany
  private List<BloodTest> bloodTestsIfCorrect;

  @ManyToMany
  private List<BloodTest> bloodTestsIfIncorrect;

  @Lob
  private String notes;

  private Boolean isDeleted;

  public String getName() {
    return name;
  }

  public String getCorrectResult() {
    return correctResult;
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

  public List<BloodTest> getBloodTestsIfCorrect() {
    return bloodTestsIfCorrect;
  }

  public void setBloodTestsIfCorrect(List<BloodTest> bloodTestsIfCorrect) {
    this.bloodTestsIfCorrect = bloodTestsIfCorrect;
  }

  public List<BloodTest> getBloodTestsIfIncorrect() {
    return bloodTestsIfIncorrect;
  }

  public void setBloodTestsIfIncorrect(List<BloodTest> bloodTestsIfIncorrect) {
    this.bloodTestsIfIncorrect = bloodTestsIfIncorrect;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
}
