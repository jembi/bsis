package model.predonationtests;

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
public class ConfiguredPreDonationTest {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, columnDefinition="SMALLINT")
  private Integer id;

  @Column(length=30)
  private String testName;

  @Enumerated(EnumType.STRING)
  @Column(length=15)
  private PreDonationTestType testType;

  @Column(length=30)
  private PreDonationTestResultDatatype resultDataType;

  @Column(length=10)
  private String units;

  // in case it is a range check
  @Column(length=30)
  private String lowerLimit;

  // in case it is a range check
  @Column(length=30)
  private String upperLimit;

  // acceptable results in a comma separated list
  @Column(length=500)
  private String acceptableResults;

  // acceptable results in a comma separated list
  @Column(length=500)
  private String allowedResults;

  private Boolean negateResult;

  private Boolean enabled;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTestName() {
    return testName;
  }

  public void setTestName(String testName) {
    this.testName = testName;
  }

  public PreDonationTestType getTestType() {
    return testType;
  }

  public void setTestType(PreDonationTestType testType) {
    this.testType = testType;
  }

  public PreDonationTestResultDatatype getResultDataType() {
    return resultDataType;
  }

  public String getUnits() {
    return units;
  }

  public void setUnits(String units) {
    this.units = units;
  }

  public void setResultDataType(PreDonationTestResultDatatype resultDataType) {
    this.resultDataType = resultDataType;
  }

  public String getLowerLimit() {
    return lowerLimit;
  }

  public void setLowerLimit(String lowerLimit) {
    this.lowerLimit = lowerLimit;
  }

  public String getUpperLimit() {
    return upperLimit;
  }

  public void setUpperLimit(String upperLimit) {
    this.upperLimit = upperLimit;
  }

  public String getAcceptableResults() {
    return acceptableResults;
  }

  public void setAcceptableResults(String acceptableResults) {
    this.acceptableResults = acceptableResults;
  }

  public String getAllowedResults() {
    return allowedResults;
  }

  public void setAllowedResults(String allowedResults) {
    this.allowedResults = allowedResults;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Boolean getNegateResult() {
    return negateResult;
  }

  public void setNegateResult(Boolean negateResult) {
    this.negateResult = negateResult;
  }
}
