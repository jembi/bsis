package org.jembi.bsis.model.util;

public enum BloodAbo {
  A("A"), B("B"), AB("AB"), O("O");

  private String value;

  private BloodAbo(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}