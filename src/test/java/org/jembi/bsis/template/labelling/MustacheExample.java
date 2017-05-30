package org.jembi.bsis.template.labelling;

public class MustacheExample {
  public boolean showIdNumber;
  public boolean doNotShowGender;
  public DonorTemplateObject donor = new DonorTemplateObject();

  public void setDonorName(String name) {
    donor.name = name;
  }

  public void setDonorIdNumber(String idNumber) {
    donor.idNumber = idNumber;
  }

  public void setDonorGender(String gender) {
    donor.gender = gender;
  }

  static class DonorTemplateObject {
    String name, idNumber, gender;
  }
}