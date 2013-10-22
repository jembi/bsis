package model.util;

/**
 * ISO/IEC 5218
 * 0 = Mr, 1 = Ms, 2 = Mrs, 3 = Dr, 4 = Blank 
 * NOTE: Try to use valueOf() method to convert enum constants
 * to strings wherever possible. Do not store numbers as this makes the database less readable.
 * Also it is generally preferrable to use the string value as the value
 * that goes in the database as it is text and more readable than numbers.
 * See javax.persistence.EnumType.
 */

public enum Title {

	Mr(0), Ms(1), Mrs(2), Dr(3), Blank(4);

	  @SuppressWarnings("unused")
	  private int value;
	  
	  private Title(int value) {
	    this.value = value;
	  }
}
