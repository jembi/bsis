package model.bloodtesting;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.ModificationTrackerBaseEntity;
import model.donation.Donation;
import model.microtiterplate.MachineReading;
import repository.BloodTestResultNamedQueryConstants;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Stores the result of one blood typing test for one donation.
 * No need to audit this table. NEVER update the entities of this table.
 * Always insert a new row. Use testedOn to find the latest test result.
 */
@NamedQueries({
    @NamedQuery(name = BloodTestResultNamedQueryConstants.NAME_COUNT_BLOOD_TEST_RESULTS_FOR_DONATION,
            query = BloodTestResultNamedQueryConstants.QUERY_COUNT_BLOOD_TEST_RESULTS_FOR_DONATION)
})
@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class BloodTestResult extends ModificationTrackerBaseEntity {

  private static final long serialVersionUID = 1L;

  @ManyToOne(cascade = {CascadeType.MERGE})
  private Donation donation;

  @ManyToOne(cascade = {CascadeType.MERGE})
  private BloodTest bloodTest;

  @Column(length=10)
  private String result;

  @Temporal(TemporalType.TIMESTAMP)
  private Date testedOn;

  @OneToOne(cascade = {CascadeType.MERGE})
  private MachineReading machineReading;

  @Lob
  private String notes;
    
  @Column(length=20)
  private String reagentLotNumber;

  public BloodTestResult() {
    super();
  }

  public BloodTest getBloodTest() {
    return bloodTest;
  }

  public String getResult() {
    return result;
  }

  public String getNotes() {
    return notes;
  }

  public void setBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Donation getDonation() {
    return donation;
  }

  public void setDonation(Donation donation) {
    this.donation = donation;
  }

  public Date getTestedOn() {
    return testedOn;
  }

  public void setTestedOn(Date testedOn) {
    this.testedOn = testedOn;
  }

  public MachineReading getMachineReading() {
    return machineReading;
  }

  public void setMachineReading(MachineReading machineReading) {
    this.machineReading = machineReading;
  }

	public String getReagentLotNumber() {
		return reagentLotNumber;
	}

	public void setReagentLotNumber(String reagentLotNumber) {
		this.reagentLotNumber = reagentLotNumber;
	}
}
