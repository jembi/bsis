package model.testresults;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class BloodTest{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, updatable=false, insertable=false)
  private Long id;

  @NotBlank
  @Column(length=30, nullable=false)
  private String name;

  @NotBlank
  @Column(length=30, nullable=false)
  private String correctResult;

  @Lob
  private String notes;

  private Boolean isRequired;

  private Boolean isDeleted;

  @OneToMany(mappedBy="bloodTest")
  private List<BloodTestAllowedResults> allowedResults;

  public Long getId() {
    return id;
  }

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

  public List<BloodTestAllowedResults> getAllowedResults() {
    return allowedResults;
  }

  public void setId(Long id) {
    this.id = id;
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
      List<BloodTestAllowedResults> allowedResults) {
    this.allowedResults = allowedResults;
  }
}
