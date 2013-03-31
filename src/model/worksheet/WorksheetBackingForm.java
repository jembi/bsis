package model.worksheet;

import model.collectedsample.FindCollectedSampleBackingForm;

import org.apache.commons.lang3.StringUtils;

public class WorksheetBackingForm extends FindCollectedSampleBackingForm {

  private Worksheet worksheet;

  public WorksheetBackingForm() {
    setWorksheet(new Worksheet());
  }

  public String getWorksheetNumber() {
    return getWorksheet().getWorksheetNumber();
  }

  public void setWorksheetNumber(String worksheetNumber) {
    getWorksheet().setWorksheetNumber(worksheetNumber);
  }

  public String getWorksheetType() {
    WorksheetType worksheetType = getWorksheet().getWorksheetType();
    if (worksheetType == null)
      return "";
    else
      return worksheetType.getId().toString();
  }

  public void setWorksheetType(String worksheetTypeId) {
    if (StringUtils.isBlank(worksheetTypeId)) {
      getWorksheet().setWorksheetType(null);
    }
    else {
      WorksheetType wt = new WorksheetType();
      try {
        wt.setId(Integer.parseInt(worksheetTypeId));
        getWorksheet().setWorksheetType(wt);
      } catch (Exception ex) {
        ex.printStackTrace();
        getWorksheet().setWorksheetType(null);
      }
    }
  }

  public String getNotes() {
    return worksheet.getNotes();
  }

  public void setNotes(String notes) {
    worksheet.setNotes(notes);
  }

  public Worksheet getWorksheet() {
    return worksheet;
  }

  public void setWorksheet(Worksheet worksheet) {
    this.worksheet = worksheet;
  }

  public Boolean getIsDeleted() {
    return worksheet.getIsDeleted();
  }

  public void setIsDeleted(Boolean isDeleted) {
    worksheet.setIsDeleted(isDeleted);
  }
  
}
