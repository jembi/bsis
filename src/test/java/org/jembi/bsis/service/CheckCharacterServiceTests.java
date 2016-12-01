package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CheckCharacterServiceTests extends ContextDependentTestSuite {
  
  @Autowired
  CheckCharacterService checkCharacterService;

  @Test
  public void testCalculateCheckCharacter3000505_shouldReturnB() throws Exception {
    String flagDigits = checkCharacterService.calculateFlagCharacters("3000505");
    assertThat(flagDigits, is("11"));
    String checkCharacter = checkCharacterService.calculateCheckCharacter(flagDigits);
    assertThat(checkCharacter, is("B"));
  }

  @Test
  public void testCalculateCheckCharacterG123498654321_shouldReturnH() throws Exception {
    String flagDigits = checkCharacterService.calculateFlagCharacters("G123 498 654 321");
    assertThat(flagDigits, is("17"));
    String checkCharacter = checkCharacterService.calculateCheckCharacter(flagDigits);
    assertThat(checkCharacter, is("H"));
  }

  @Test
  public void testCalculateCheckCharacter3000509_shouldReturn3() throws Exception {
    String flagDigits = checkCharacterService.calculateFlagCharacters("3000509");
    assertThat(flagDigits, is("03"));
    String checkCharacter = checkCharacterService.calculateCheckCharacter(flagDigits);
    assertThat(checkCharacter, is("3"));
  }

  @Test
  public void testCalculateCheckCharacter2210579_shouldReturnH() throws Exception {
    String flagDigits = checkCharacterService.calculateFlagCharacters("2210579");
    assertThat(flagDigits, is("17"));
    String checkCharacter = checkCharacterService.calculateCheckCharacter(flagDigits);
    assertThat(checkCharacter, is("H"));
  }

  @Test
  public void testCalculateCheckCharacter8332514_shouldReturnB() throws Exception {
    String flagDigits = checkCharacterService.calculateFlagCharacters("8332514");
    assertThat(flagDigits, is("11"));
    String checkCharacter = checkCharacterService.calculateCheckCharacter(flagDigits);
    assertThat(checkCharacter, is("B"));
  }

  @Test
  public void testCalculateCheckCharacterG123489654321_shouldReturnY() throws Exception {
    String flagDigits = checkCharacterService.calculateFlagCharacters("G123489654321");
    assertThat(flagDigits, is("34"));
    String checkCharacter = checkCharacterService.calculateCheckCharacter(flagDigits);
    assertThat(checkCharacter, is("Y"));
  }

  @Test
  public void testCalculateCheckCharacter7432269_shouldReturnW() throws Exception {
    String checkDigits = checkCharacterService.calculateFlagCharacters("7432269");
    assertThat(checkDigits, is("32"));
    String checkSum = checkCharacterService.calculateCheckCharacter(checkDigits);
    assertThat(checkSum, is("W"));
  }

  @Test
  public void testCalculateCheckCharacter3946294_shouldReturnF() throws Exception {
    String checkDigits = checkCharacterService.calculateFlagCharacters("3946294");
    assertThat(checkDigits, is("15"));
    String checkSum = checkCharacterService.calculateCheckCharacter(checkDigits);
    assertThat(checkSum, is("F"));
  }

  @Test
  public void testCalculateCheckCharacter394abc6294_shouldReturnF() throws Exception {
    String checkDigits = checkCharacterService.calculateFlagCharacters("394 <abc> [6] {294}");
    assertThat(checkDigits, is("15"));
    String checkSum = checkCharacterService.calculateCheckCharacter(checkDigits);
    assertThat(checkSum, is("F"));
  }

  public void testcalculateCheckCharacter0_shouldReturn0() throws Exception {
    String checkSum = checkCharacterService.calculateCheckCharacter("00");
    assertThat(checkSum, is("0"));
  }

  @Test(expected = java.lang.NumberFormatException.class)
  public void testcalculateCheckCharacterInvalid_shouldThrow() throws Exception {
    checkCharacterService.calculateCheckCharacter("hello!");
  }

  @Test(expected = java.lang.NumberFormatException.class)
  public void testcalculateCheckCharacterSmallNumber_shouldThrow() throws Exception {
    checkCharacterService.calculateCheckCharacter("-23");
  }

  @Test(expected = java.lang.NumberFormatException.class)
  public void testcalculateCheckCharacterLargeNumber_shouldThrow() throws Exception {
    checkCharacterService.calculateCheckCharacter("123");
  }

  @Test(expected = java.lang.NumberFormatException.class)
  public void testcalculateCheckCharacterLengthNumber_shouldThrow() throws Exception {
    checkCharacterService.calculateCheckCharacter("37");
  }
}
