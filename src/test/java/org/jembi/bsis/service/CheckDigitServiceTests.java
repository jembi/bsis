package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CheckDigitServiceTests extends ContextDependentTestSuite {
  
  @Autowired
  CheckDigitService checkDigitService;

  @Test
  public void testCalculateISO7064Mod37Comma2CheckDigits_shouldReturn17() throws Exception {
    int checkSum = checkDigitService.calculateISO7064Mod37Comma2CheckDigits("G123 498 654 321");
    assertThat(checkSum, is(17));
  }

  @Test
  public void testCalculateISO7064Mod37Comma2CheckDigits_shouldReturn11() throws Exception {
    int checkSum = checkDigitService.calculateISO7064Mod37Comma2CheckDigits("3000505");
    assertThat(checkSum, is(11));
  }

  @Test
  public void testCalculateISBT128CheckG123498654321_shouldReturnH() throws Exception {
    char checkSum = checkDigitService.calculateISBT128Check("G123 498 654 321");
    assertThat(checkSum, is("H".charAt(0)));
  }

  @Test
  public void testCalculateISBT128CheckGivenCheckDigits17_shouldReturnH() throws Exception {
    char checkSum = checkDigitService.calculateISBT128Check(17);
    assertThat(checkSum, is("H".charAt(0)));
  }

  @Test
  public void testCalculateISBT128Check3000509_shouldReturn3() throws Exception {
    char checkSum = checkDigitService.calculateISBT128Check("3000509");
    assertThat(checkSum, is("3".charAt(0)));
  }

  @Test
  public void testCalculateISBT128Check2210579_shouldReturnH() throws Exception {
    char checkSum = checkDigitService.calculateISBT128Check("2210579");
    assertThat(checkSum, is("H".charAt(0)));
  }

  @Test
  public void testCalculateISBT128Check8332514_shouldReturnB() throws Exception {
    char checkSum = checkDigitService.calculateISBT128Check("8332514");
    assertThat(checkSum, is("B".charAt(0)));
  }

  @Test
  public void testCalculateISBT128CheckG123489654321_shouldReturnY() throws Exception {
    char checkSum = checkDigitService.calculateISBT128Check("G123489654321");
    assertThat(checkSum, is("Y".charAt(0)));
  }

  @Test
  public void testCalculateISBT128Check7432269_shouldReturnW() throws Exception {
    int checkDigits = checkDigitService.calculateISO7064Mod37Comma2CheckDigits("7432269");
    assertThat(checkDigits, is(32));
    char checkSum = checkDigitService.calculateISBT128Check(checkDigits);
    assertThat(checkSum, is("W".charAt(0)));
  }

  @Test
  public void testCalculateISBT128Check7432269_shouldReturnF() throws Exception {
    int checkDigits = checkDigitService.calculateISO7064Mod37Comma2CheckDigits("3946294");
    assertThat(checkDigits, is(15));
    char checkSum = checkDigitService.calculateISBT128Check(checkDigits);
    assertThat(checkSum, is("F".charAt(0)));
  }
}
