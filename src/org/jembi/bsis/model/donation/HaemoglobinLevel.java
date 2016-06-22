package org.jembi.bsis.model.donation;

public enum HaemoglobinLevel {

  PASS("Pass"),
  FAIL("Fail");

  private String label;

  HaemoglobinLevel(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

}
