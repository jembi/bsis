package backingform;

import java.util.ArrayList;
import java.util.List;

import model.util.BloodGroup;

public class FindDonorBackingForm {

  private String donorNumber;
  private String firstName;
  private String lastName;
  private List<BloodGroup> bloodGroups;
  private boolean createDonorSummaryView;
  private boolean dueToDonate;
  private String anyBloodGroup;
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

  public List<BloodGroup> getBloodGroups() {
    return bloodGroups;
  }

  public void setBloodGroups(List<String> bloodGroups) {
    this.bloodGroups = new ArrayList<BloodGroup>();
    for (String bg : bloodGroups) {
      this.bloodGroups.add(new BloodGroup(bg));
    }
  }

  public void setAnyBloodGroup(String anyBloodGroup) {
    this.anyBloodGroup = anyBloodGroup;
  }

  public String getAnyBloodGroup() {
    return anyBloodGroup;
  }

  public boolean getCreateDonorSummaryView() {
    return createDonorSummaryView;
  }

  public void setCreateDonorSummaryView(boolean createDonorSummaryView) {
    this.createDonorSummaryView = createDonorSummaryView;
  }

	public boolean getDueToDonate() {
		return dueToDonate;
	}

	public void setDueToDonate(boolean dueToDonate) {
		this.dueToDonate = dueToDonate;
	}

	public boolean isUsePhraseMatch() {
		return usePhraseMatch;
	}

	public void setUsePhraseMatch(boolean usePhraseMatch) {
		this.usePhraseMatch = usePhraseMatch;
	}



}