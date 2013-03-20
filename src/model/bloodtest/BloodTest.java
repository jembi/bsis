package model.bloodtest;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import model.bloodtyping.BloodTypingTest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;

@Entity
@Audited
public class BloodTest {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, updatable=false, insertable=false)
  private Long id;

  @Column(length=30)
  private String name;

  @Column(length=30)
  private String displayName;

  private String validResults;

  @Column(length=10)
  private String correctResult;

  private Boolean isRequired;

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
    if (validResults == null)
      return null;
    return Arrays.asList(validResults.split(","));
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
    this.validResults = StringUtils.join(allowedResults, ",");
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCorrectResult() {
    return correctResult;
  }

  public void setCorrectResult(String correctResult) {
    this.correctResult = correctResult;
  }
}
