package model.bloodtyping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class BloodTypingTest {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="TINYINT")
  private Integer id;

  @Column(length=15)
  private String testNameShort;

  @Column(length=30)
  private String testName;

  @Enumerated(EnumType.STRING)
  @Column(length=10)
  private BloodTestDataType dataType;

  private String validResults;

  private String negativeResults;

  private String positiveResults;

  private Integer rankInCategory;

  @Enumerated(EnumType.STRING)
  @Column(length=12)
  private BloodTypingTestType bloodTypingTestType;

  private Boolean isEmptyAllowed;

  private Boolean isActive;

  public Integer getId() {
    return id;
  }

  public String getTestNameShort() {
    return testNameShort;
  }

  public String getTestName() {
    return testName;
  }

  public BloodTestDataType getDataType() {
    return dataType;
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

  public void setDataType(BloodTestDataType dataType) {
    this.dataType = dataType;
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

  public BloodTypingTestType getBloodTypingTestType() {
    return bloodTypingTestType;
  }

  public void setBloodTypingTestType(BloodTypingTestType bloodTypingTestType) {
    this.bloodTypingTestType = bloodTypingTestType;
  }

  public Boolean getIsEmptyAllowed() {
    return isEmptyAllowed;
  }

  public void setIsEmptyAllowed(Boolean isEmptyAllowed) {
    this.isEmptyAllowed = isEmptyAllowed;
  }
}
