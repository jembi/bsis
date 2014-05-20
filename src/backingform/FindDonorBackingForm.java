package backingform;

import java.util.ArrayList;
import java.util.List;

import model.util.BloodGroup;

public class FindDonorBackingForm {

  private String donorNumber;
  private String firstName;
  private String lastName;
  private boolean createDonorSummaryView;
  private boolean usePhraseMatch;

  public String getDonorNumber() {
    return donorNumber;
  }

  public void setDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }


  public boolean getCreateDonorSummaryView() {
    return createDonorSummaryView;
  }

  public void setCreateDonorSummaryView(boolean createDonorSummaryView) {
    this.createDonorSummaryView = createDonorSummaryView;
  }

	
   public boolean isUsePhraseMatch() {
		return usePhraseMatch;
	}

  public void setUsePhraseMatch(boolean usePhraseMatch) {
		this.usePhraseMatch = usePhraseMatch;
	}



}