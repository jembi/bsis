package org.jembi.bsis.model.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;

public class BloodRhTests extends UnitTestSuite {

  @Test
  public void testGetValue_shouldReturnCorrectValue() throws Exception {
    assertThat(BloodRh.POSITIVE.getValue(), is("+"));
    assertThat(BloodRh.NEGATIVE.getValue(), is("-"));
  }
}
