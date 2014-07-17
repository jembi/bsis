package backingform;

import model.util.Gender;

public class FindDonorBackingForm {

  private String donorNumber;
  private String firstName;
  private String lastName;
  private Gender gender;
  private String birthDate;
  private boolean createDonorSummaryView;
  private boolean usePhraseMatch;
  private String donationIdentificationNumber;

    public String getDonationIdentificationNumber() {
        return donationIdentificationNumber;
    }

    public void setDonationIdentificationNumber(String donationIdentificationNumber) {
        this.donationIdentificationNumber = donationIdentificationNumber;
    }

  


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
       System.out.println("in form" + firstName);
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
       return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;

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