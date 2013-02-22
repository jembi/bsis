package model.bloodtest;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.apache.commons.lang3.StringUtils;

@Entity
public class BloodTest {

  @Id
  @Column(nullable=false, updatable=false, insertable=false, length=30)
  private String name;

  private String allowedResults;

  private String correctResult;
  
  @Lob
  private String notes;

  private Boolean isRequired;

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
}
