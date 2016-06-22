package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.Set;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.model.worksheet.Worksheet;
import org.jembi.bsis.model.worksheet.WorksheetType;
import org.jembi.bsis.utils.CustomDateFormatter;

public class WorksheetViewModel {

  private Worksheet worksheet;

  public WorksheetViewModel(Worksheet worksheet) {
    this.worksheet = worksheet;
  }

  public Long getId() {
    return worksheet.getId();
  }

  public String getWorksheetNumber() {
    return worksheet.getWorksheetNumber();
  }

  public Date getLastUpdated() {
    return worksheet.getLastUpdated();
  }

  public User getCreatedBy() {
    return worksheet.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return worksheet.getLastUpdatedBy();
  }

  public Set<Donation> getDonations() {
    return worksheet.getDonations();
  }

  public WorksheetType getWorksheetType() {
    return worksheet.getWorksheetType();
  }

  public String getNotes() {
    return worksheet.getNotes();
  }

  public Boolean getIsDeleted() {
    return worksheet.getIsDeleted();
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateTimeString(worksheet.getCreatedDate());
  }
}
