package org.jembi.bsis.model.bloodtesting;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseModificationTrackerEntity;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.repository.BloodTestResultNamedQueryConstants;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Stores the result of one blood typing test for one donation. No need to audit this table. NEVER
 * update the entities of this table. Always insert a new row. Use testedOn to find the latest test
 * result.
 */
@NamedQueries({
    @NamedQuery(name = BloodTestResultNamedQueryConstants.NAME_COUNT_BLOOD_TEST_RESULTS_FOR_DONATION,
        query = BloodTestResultNamedQueryConstants.QUERY_COUNT_BLOOD_TEST_RESULTS_FOR_DONATION),
    @NamedQuery(name = BloodTestResultNamedQueryConstants.NAME_FIND_BLOOD_TEST_RESULT_VALUE_OBJECTS_FOR_DATE_RANGE,
        query = BloodTestResultNamedQueryConstants.QUERY_FIND_BLOOD_TEST_RESULT_VALUE_OBJECTS_FOR_DATE_RANGE),
    @NamedQuery(name = BloodTestResultNamedQueryConstants.NAME_FIND_TOTAL_UNITS_TESTED_FOR_DATE_RANGE,
        query = BloodTestResultNamedQueryConstants.QUERY_FIND_TOTAL_UNITS_TESTED_FOR_DATE_RANGE),
    @NamedQuery(name = BloodTestResultNamedQueryConstants.NAME_FIND_TOTAL_TTI_UNSAFE_UNITS_TESTED_FOR_DATE_RANGE,
        query = BloodTestResultNamedQueryConstants.QUERY_FIND_TOTAL_TTI_UNSAFE_UNITS_TESTED_FOR_DATE_RANGE),
    @NamedQuery(name = BloodTestResultNamedQueryConstants.NAME_GET_TEST_OUTCOMES_FOR_DONATION,
        query = BloodTestResultNamedQueryConstants.QUERY_GET_TEST_OUTCOMES_FOR_DONATION),
    @NamedQuery(name = BloodTestResultNamedQueryConstants.NAME_FIND_BLOOD_TEST_RESULTS_FOR_EXPORT,
        query = BloodTestResultNamedQueryConstants.QUERY_FIND_BLOOD_TEST_RESULTS_FOR_EXPORT)

})
@Entity
@Audited
public class BloodTestResult extends BaseModificationTrackerUUIDEntity {

  private static final long serialVersionUID = 1L;

  @ManyToOne(cascade = {CascadeType.MERGE})
  private Donation donation;

  @ManyToOne(cascade = {CascadeType.MERGE})
  private BloodTest bloodTest;

  @Column(length = 10)
  private String result;

  @Temporal(TemporalType.TIMESTAMP)
  private Date testedOn;

  @Lob
  private String notes;

  @Column(length = 20)
  private String reagentLotNumber;

  private Boolean reEntryRequired;

  private boolean isDeleted = false;

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

  public String getReagentLotNumber() {
    return reagentLotNumber;
  }

  public void setReagentLotNumber(String reagentLotNumber) {
    this.reagentLotNumber = reagentLotNumber;
  }

  public Boolean getReEntryRequired() {
    return reEntryRequired;
  }

  public void setReEntryRequired(Boolean reEntryRequired) {
    this.reEntryRequired = reEntryRequired;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}
