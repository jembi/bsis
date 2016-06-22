package org.jembi.bsis.viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Set;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.worksheet.WorksheetType;

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

  public Long getId() {
    return rawBloodTest.getId();
  }

  public String getTestNameShort() {
    return rawBloodTest.getTestNameShort();
  }

  public String getTestName() {
    return rawBloodTest.getTestName();
  }

  /*
  public ArrayList<String> getValidResults() {
      String validResults = rawBloodTest.getValidResults();
      ArrayList<String> formatValidResults = new ArrayList<String>();
      String validResultStr = "";
      for(int i=0; i< validResults.length() ; i++){
          char c = validResults.charAt(i);
          if(c != ','){
        	  validResultStr += c;
          }
          else {
        	  formatValidResults.add(validResultStr);
        	  validResultStr = "";
          }
      }
      formatValidResults.add(validResultStr);
      return  formatValidResults;
  }
  */

  public List<String> getValidResults() {
    return rawBloodTest.getValidResultsList();
  }

  public String getBloodTestCategory() {
    String category = "";
    if (rawBloodTest.getCategory() == null)
      return "";
    switch (rawBloodTest.getCategory()) {
      case BLOODTYPING:
        category = "BLOODTYPING";
        break;
      case TTI:
        category = "TTI";
        break;
    }
    return category;
  }

  public String getNegativeResults() {
    return rawBloodTest.getNegativeResults();
  }

  public String getPositiveResults() {
    return rawBloodTest.getPositiveResults();
  }

  public Boolean getIsActive() {
    return rawBloodTest.getIsActive();
  }

  public Integer getRankInCategory() {
    return rawBloodTest.getRankInCategory();
  }

  @JsonIgnore
  public Set<WorksheetType> getWorksheetTypes() {
    return rawBloodTest.getWorksheetTypes();
  }
}
