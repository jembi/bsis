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
  public void testCalculateFlagCharacters_shouldReturn17() throws Exception {
    String flagDigits = checkCharacterService.calculateFlagCharacters("G123 498 654 321");
    assertThat(flagDigits, is("17"));
  }

  @Test
  public void testCalculateFlagCharacters_shouldReturn11() throws Exception {
    String flagDigits = checkCharacterService.calculateFlagCharacters("3000505");
    assertThat(flagDigits, is("11"));
  }

  @Test
  public void testCalculateFlagCharacters_shouldReturnH() throws Exception {
    String flagDigits = checkCharacterService.calculateCheckCharacter("G123 498 654 321");
    assertThat(flagDigits, is("H"));
  }

  @Test
  public void testCalculateCheckCharacterFromFlagCharacters17_shouldReturnH() throws Exception {
    String checkCharacter = checkCharacterService.calculateCheckCharacterFromFlagCharacters("17");
    assertThat(checkCharacter, is("H"));
  }

  @Test
  public void testCalculateCheckCharacter3000509_shouldReturn3() throws Exception {
    String checkCharacter = checkCharacterService.calculateCheckCharacter("3000509");
    assertThat(checkCharacter, is("3"));
  }

  @Test
  public void testCalculateCheckCharacter2210579_shouldReturnH() throws Exception {
    String checkCharacter = checkCharacterService.calculateCheckCharacter("2210579");
    assertThat(checkCharacter, is("H"));
  }

  @Test
  public void testCalculateCheckCharacter8332514_shouldReturnB() throws Exception {
    String checkCharacter = checkCharacterService.calculateCheckCharacter("8332514");
    assertThat(checkCharacter, is("B"));
  }

  @Test
  public void testCalculateCheckCharacterG123489654321_shouldReturnY() throws Exception {
    String checkSum = checkCharacterService.calculateCheckCharacter("G123489654321");
    assertThat(checkSum, is("Y"));
  }

  @Test
  public void testCalculateCheckCharacter7432269_shouldReturnW() throws Exception {
    String checkDigits = checkCharacterService.calculateFlagCharacters("7432269");
    assertThat(checkDigits, is("32"));
    String checkSum = checkCharacterService.calculateCheckCharacterFromFlagCharacters(checkDigits);
    assertThat(checkSum, is("W"));
  }

  @Test
  public void testCalculateCheckCharacter7432269_shouldReturnF() throws Exception {
    String checkDigits = checkCharacterService.calculateFlagCharacters("3946294");
    assertThat(checkDigits, is("15"));
    String checkSum = checkCharacterService.calculateCheckCharacterFromFlagCharacters(checkDigits);
    assertThat(checkSum, is("F"));
  }
}
