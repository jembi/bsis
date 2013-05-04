package viewmodel;

import java.util.Date;
import java.util.Set;

import utils.CustomDateFormatter;

import model.collectedsample.CollectedSample;
import model.user.User;
import model.worksheet.Worksheet;
import model.worksheet.WorksheetType;

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

  public Set<CollectedSample> getCollectedSamples() {
    return worksheet.getCollectedSamples();
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
