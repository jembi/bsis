package model.worksheet;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import model.ModificationTrackerBaseEntity;
import model.donation.Donation;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Audited
public class Worksheet extends ModificationTrackerBaseEntity {

  private static final long serialVersionUID = 1L;

  @Column(length=20, unique=true)
  private String worksheetNumber;

  @ManyToOne
  private WorksheetType worksheetType;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @ManyToMany
  private Set<Donation> donations;

  private Boolean isDeleted;

  @Lob
  private String notes;

  public Worksheet() {
    super();
    donations = new HashSet<Donation>();
  }

  public String getWorksheetNumber() {
    return worksheetNumber;
  }

  public void setWorksheetNumber(String worksheetNumber) {
    this.worksheetNumber = worksheetNumber;
  }

  public Set<Donation> getDonations() {
    return donations;
  }

  public void setDonations(Set<Donation> donations) {
    this.donations = donations;
  }

  public WorksheetType getWorksheetType() {
    return worksheetType;
  }

  public void setWorksheetType(WorksheetType worksheetType) {
    this.worksheetType = worksheetType;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}