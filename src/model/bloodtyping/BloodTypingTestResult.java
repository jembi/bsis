package model.bloodtyping;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.collectedsample.CollectedSample;

/**
 * Stores the result of one blood typing test for one collection.
 * No need to audit this table. NEVER update the entities of this table.
 * Always insert a new row. Use testedOn to find the latest test result.
 * @author iamrohitbanga
 */
@Entity
public class BloodTypingTestResult {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false)
  private Long id;

  @ManyToOne
  private CollectedSample collectedSample;

  @ManyToOne
  private BloodTypingTest bloodTypingTest;

  @Column(length=10)
  private String result;

  @Temporal(TemporalType.TIMESTAMP)
  private Date testedOn;

  @Lob
  private String notes;

  public BloodTypingTestResult() {
  }

  public Long getId() {
    return id;
  }

  public BloodTypingTest getBloodTypingTest() {
    return bloodTypingTest;
  }

  public String getResult() {
    return result;
  }

  public String getNotes() {
    return notes;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setBloodTypingTest(BloodTypingTest bloodTypingTest) {
    this.bloodTypingTest = bloodTypingTest;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public CollectedSample getCollectedSample() {
    return collectedSample;
  }

  public void setCollectedSample(CollectedSample collectedSample) {
    this.collectedSample = collectedSample;
  }

  public Date getTestedOn() {
    return testedOn;
  }

  public void setTestedOn(Date testedOn) {
    this.testedOn = testedOn;
  }
}
