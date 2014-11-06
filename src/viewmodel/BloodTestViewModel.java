package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Set;
import model.bloodtesting.BloodTest;
import model.worksheet.WorksheetType;
import scala.Char;

public class BloodTestViewModel {

  @JsonIgnore
  private BloodTest rawBloodTest;

  public BloodTestViewModel(BloodTest rawBloodTest) {
    this.rawBloodTest = rawBloodTest;
  }

  public BloodTest getRawBloodTest() {
    return rawBloodTest;
  }

  public void setRawBloodTest(BloodTest rawBloodTest) {
    this.rawBloodTest = rawBloodTest;
  }

  public Integer getId() {
    return rawBloodTest.getId();
  }

  public String getTestNameShort() {
    return rawBloodTest.getTestNameShort();
  }

  public String getTestName() {
    return rawBloodTest.getTestName();
  }

  public ArrayList<String> getValidResults() {
      String validResults = rawBloodTest.getValidResults();
      ArrayList<String> formatValidResults = new ArrayList<String>();
      for(int i=0; i< validResults.length() ; i++){
          char c = validResults.charAt(i);
          if(c != ',')
              formatValidResults.add(String.valueOf(c));
      }
      return  formatValidResults;
  }

  public String getCategory() {
    String category = "";
    if (rawBloodTest.getCategory() == null)
      return "";
    switch (rawBloodTest.getCategory()) {
    case BLOODTYPING: category = "Blood typing";
                      break;
    case TTI: category = "TTI";
              break;
    }
    return category;
  }

  public String getNegativeResults() {
    return rawBloodTest.getNegativeResults();
  }

  public Boolean getIsActive() {
    return rawBloodTest.getIsActive();
  }

  public Integer getRankInCategory() {
    return rawBloodTest.getRankInCategory();
  }

  public Set<WorksheetType> getWorksheetTypes() {
    return rawBloodTest.getWorksheetTypes();
  }
}
