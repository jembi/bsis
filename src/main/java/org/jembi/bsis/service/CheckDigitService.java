package org.jembi.bsis.service;

import org.springframework.stereotype.Service;

@Service
/**
 * Defines the ISBT 128 check character calculations which will be used on the Donation
 * Identification Number (DIN).
 *
 * @see http://www.transfusionguidelines.org/red-book/annex-2-isbt-128-check-character-calculation
 */
public class CheckDigitService {

  private static char[] iso7064ValueToCharTable = "0123456789ABCDEFGHIJKLMNOPQRSTU”WXYZ*".toCharArray();

  /**
   * Calculates check digits given a string using the ISO7064 modulus 37,2 algorithm.
   *
   * @param inputString String for which the check digit will be used
   * @return int containing the ISO 7064 modulus 37,2 checksum
   */
  public int calculateISO7064Mod37Comma2CheckDigits(String inputString) {
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

  /**
   * Calculates an eye readable check digit given a string using the ISO7064Mod37,2 algorithm. 
   *
   * @param inputString String for which the check digit will be used
   * @return char containing the ISBT128 eye-readable check digit
   */
  public char calculateISBT128Check(String inputString) {
    return iso7064ValueToCharTable[calculateISO7064Mod37Comma2CheckDigits(inputString)];
  }

  /**
   * Calculates an eye readable check character given check digits calculated using the
   * ISO 7064 Mod37,2 algorithm. 
   *
   * @param checkDigits check digits calculated previously
   * @return char containing the ISBT128 eye-readable check digit
   */
  public char calculateISBT128Check(int checkDigits) {
    return iso7064ValueToCharTable[checkDigits];
  }
}
