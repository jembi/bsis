package org.jembi.bsis.model.util;

/**
 * ISO/IEC 5218 0 = not known, 1 = male, 2 = female, 9 = not applicable NOTE: Try to use valueOf()
 * method to convert enum constants to strings wherever possible. Do not store numbers as this makes
 * the database less readable. Also it is generally preferrable to use the string value as the value
 * that goes in the database as it is text and more readable than numbers. See
 * javax.persistence.EnumType.
 */
public enum Gender {

  not_known(0), male(1), female(2), not_applicable(9);

  @SuppressWarnings("unused")
  private int value;

  private Gender(int value) {
    this.value = value;
  }
}