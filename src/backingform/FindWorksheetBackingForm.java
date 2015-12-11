package backingform;

import java.util.List;

class FindWorksheetBackingForm {

  private String worksheetNumber;

  private List<String> worksheetTypes;

  private String worksheetResultClickUrl;

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

  public String getWorksheetResultClickUrl() {
    return worksheetResultClickUrl;
  }

  public void setWorksheetResultClickUrl(String worksheetResultClickUrl) {
    this.worksheetResultClickUrl = worksheetResultClickUrl;
  }
}
