package org.jembi.bsis.service;

import org.springframework.stereotype.Service;

@Service
/**
 * Defines the ISBT 128 check character calculations which will be used on the Donation
 * Identification Number (DIN).
 *
 * @see http://www.transfusionguidelines.org/red-book/annex-2-isbt-128-check-character-calculation
 */
public class CheckCharacterService {

  private static char[] iso7064ValueToCharTable = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ*".toCharArray();

  /**
   * Calculates check characters given a string using the ISO7064 modulus 37,2 algorithm.
   *
   * @param inputString String for which the flag characters will be calculated (e.g. DIN)
   * @return String containing ISBT128 flag characters of two digits
   */
  public String calculateFlagCharacters(String inputString) {
    int flagCharacters = calculateISO7064Mod37Comma2CheckDigits(inputString);
    return String.format("%02d", flagCharacters);
  }

  /**
   * Calculates an eye readable check character given flag characters calculated using the ISO 7064 Mod37,2 algorithm. 
   *
   * @param flagCharacters String flag characters, must be a number between 0 and 36
   * @return String containing the ISBT128 check character
   * @throws NumberFormatException if the flagCharacters are not digits or are not in the correct range
   */
  public String calculateCheckCharacter(String flagCharacters) throws NumberFormatException {
    int flagDigits = Integer.valueOf(flagCharacters);
    if (flagDigits < 0 || flagDigits > (iso7064ValueToCharTable.length-1)) {
      throw new NumberFormatException("The flagCharacters '" + flagDigits + "' are not in the correct range of 0.."
          + (iso7064ValueToCharTable.length-1));
    }
    char checkCharacter =  iso7064ValueToCharTable[flagDigits];
    return String.valueOf(checkCharacter);
  }

  private int calculateISO7064Mod37Comma2CheckDigits(String inputString) {
    // Note: code based on code from a document named ST-001-ISBT-128-Standard-Technical-Specification-v5.5.0.pdf

    // Read the characters from left to right.
    int sum = 0;
    for (char ch : inputString.toCharArray()) {
      // Ignore invalid characters as per ISO 7064.
      boolean isDigit = ((ch >= '0') && (ch <= '9'));
      boolean isUpperAlpha = ((ch >= 'A') && (ch <= 'Z'));
      if (isDigit || isUpperAlpha)
      {
        // Convert the character to its ISO 7064 value.
        int iso7064Value = 0;
        if (isDigit) {
          iso7064Value = ch - '0';
        } else {
          iso7064Value = ch - 'A' + 10;
        }
        // Add the character value to the accumulating sum,
        // multiply by two, and do an intermediate modulus to
        // prevent integer overflow.
        sum = ((sum + iso7064Value) * 2) % 37;
      }
    }
    // Find the value, that when added to the result of the above
    // calculation, would result in a number who’s modulus 37
    // result is equal to 1.
    int charValue = (38 - sum) % 37;
    return charValue;
  }
}
