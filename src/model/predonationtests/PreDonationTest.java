package model.predonationtests;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class PreDonationTest {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false)
  private Long id;

  @Column(length=30)
  private String testResult;

  @ManyToOne(optional=false)
  private ConfiguredPreDonationTest configuredPreDonationTest;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTestResult() {
    return testResult;
  }

  public void setTestResult(String testResult) {
    this.testResult = testResult;
  }

  public ConfiguredPreDonationTest getConfiguredPreDonationTest() {
    return configuredPreDonationTest;
  }

  public void setConfiguredPreDonationTest(ConfiguredPreDonationTest configuredPreDonationTest) {
    this.configuredPreDonationTest = configuredPreDonationTest;
  }
}
