package model.bloodtest;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class BloodTest{

  @Id
  @Column(nullable=false, updatable=false, insertable=false, length=30)
  private String name;

  @NotBlank
  @Column(length=30, nullable=false)
  private String correctResult;

  @Lob
  private String notes;

  private Boolean isRequired;

  private Boolean isDeleted;

  @OneToMany(mappedBy="bloodTest", fetch=FetchType.EAGER)
  private List<BloodTestResult> allowedResults;

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

  public List<BloodTestResult> getAllowedResults() {
    return allowedResults;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCorrectResult(String correctResult) {
    this.correctResult = correctResult;
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
      List<BloodTestResult> allowedResults) {
    this.allowedResults = allowedResults;
  }
}
