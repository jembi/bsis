package model.rawbloodtest;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import model.bloodtest.BloodTest;
import model.microtiterplate.MicrotiterPlate;

@Entity
public class RawBloodTest {

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
  private RawBloodTestDataType dataType;

  private String allowedResults;

  private String positiveResults;

  private String negativeResults;

  private Boolean negativeRequiredForUse;

  @ManyToOne
  private MicrotiterPlate plateUsedForTesting;

  @ManyToOne
  private RawBloodTestGroup rawBloodTestGroup;

  @ManyToMany
  @JoinTable(name="RawBloodTest_TestsRequiredIfPositive")
  private List<BloodTest> testsRequiredIfPositive;

  @ManyToMany
  @JoinTable(name="RawBloodTest_TestsRequiredIfNegative")
  private List<BloodTest> testsRequiredIfNegative;

  private Boolean isConfidential;

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

  public RawBloodTestDataType getDataType() {
    return dataType;
  }

  public String getAllowedResults() {
    return allowedResults;
  }

  public String getPositiveResults() {
    return positiveResults;
  }

  public String getNegativeResults() {
    return negativeResults;
  }

  public Boolean getNegativeRequiredForUse() {
    return negativeRequiredForUse;
  }

  public MicrotiterPlate getPlateUsedForTesting() {
    return plateUsedForTesting;
  }

  public RawBloodTestGroup getRawBloodTestGroup() {
    return rawBloodTestGroup;
  }

  public List<BloodTest> getTestsRequiredIfPositive() {
    return testsRequiredIfPositive;
  }

  public List<BloodTest> getTestsRequiredIfNegative() {
    return testsRequiredIfNegative;
  }

  public Boolean getIsConfidential() {
    return isConfidential;
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

  public void setDataType(RawBloodTestDataType dataType) {
    this.dataType = dataType;
  }

  public void setAllowedResults(String allowedResults) {
    this.allowedResults = allowedResults;
  }

  public void setPositiveResults(String positiveResults) {
    this.positiveResults = positiveResults;
  }

  public void setNegativeResults(String negativeResults) {
    this.negativeResults = negativeResults;
  }

  public void setNegativeRequiredForUse(Boolean negativeRequiredForUse) {
    this.negativeRequiredForUse = negativeRequiredForUse;
  }

  public void setPlateUsedForTesting(MicrotiterPlate plateUsedForTesting) {
    this.plateUsedForTesting = plateUsedForTesting;
  }

  public void setRawBloodTestGroup(RawBloodTestGroup rawBloodTestGroup) {
    this.rawBloodTestGroup = rawBloodTestGroup;
  }

  public void setTestsRequiredIfPositive(List<BloodTest> testsRequiredIfPositive) {
    this.testsRequiredIfPositive = testsRequiredIfPositive;
  }

  public void setTestsRequiredIfNegative(List<BloodTest> testsRequiredIfNegative) {
    this.testsRequiredIfNegative = testsRequiredIfNegative;
  }

  public void setIsConfidential(Boolean isConfidential) {
    this.isConfidential = isConfidential;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

}
