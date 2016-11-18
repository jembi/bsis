package org.jembi.bsis.model.util;

public enum BloodRh {
  POSITIVE("+"), NEGATIVE("-");

  private String value;

  private BloodRh(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}