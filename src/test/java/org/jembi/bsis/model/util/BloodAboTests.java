package org.jembi.bsis.model.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;

public class BloodAboTests extends UnitTestSuite {

  @Test
  public void testGetValue_shouldReturnCorrectValue() throws Exception {
    assertThat(BloodAbo.A.getValue(), is("A"));
    assertThat(BloodAbo.B.getValue(), is("B"));
    assertThat(BloodAbo.AB.getValue(), is("AB"));
    assertThat(BloodAbo.O.getValue(), is("O"));
  }
}
