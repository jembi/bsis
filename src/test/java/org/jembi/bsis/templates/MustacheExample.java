package org.jembi.bsis.templates;

public class MustacheExample {
  public boolean showIdNumber;
  public boolean doNotShowGender;
  public DonorTemplateObject donor;

  static class DonorTemplateObject {
    String name, idNumber, gender;

    DonorTemplateObject(String name, String idNumber, String gender) {
      this.name = name;
      this.idNumber = idNumber;
      this.gender = gender;
    }
  }
}