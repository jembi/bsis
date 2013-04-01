package model.worksheet;

import java.util.List;

public class FindWorksheetBackingForm {

  private String worksheetNumber;

  private List<String> worksheetTypes;

  public FindWorksheetBackingForm() {
  }

  public String getWorksheetNumber() {
    return worksheetNumber;
  }

  public void setWorksheetNumber(String worksheetNumber) {
    this.worksheetNumber = worksheetNumber;
  }

  public List<String> getWorksheetTypes() {
    return worksheetTypes;
  }

  public void setWorksheetTypes(List<String> worksheetTypes) {
    this.worksheetTypes = worksheetTypes;
  }
}
