package model.util;

/**
 * ISO/IEC 5218
 * 0 = not known, 1 = male, 2 = female, 9 = not applicable
 * NOTE: Try to use valueOf() method to convert enum constants
 * to strings wherever possible.
 */
public enum Gender {

  not_known(0), male(1), female(2), not_applicable(9);

  @SuppressWarnings("unused")
  private int value;
  
  private Gender(int value) {
    this.value = value;
  }
}