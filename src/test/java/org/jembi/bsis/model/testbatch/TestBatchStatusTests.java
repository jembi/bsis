package org.jembi.bsis.model.testbatch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

public class TestBatchStatusTests {

  @Test
  public void testHasBeenReleasedForOpenTestBatch_shouldReturnFalse() {
    assertThat(TestBatchStatus.hasBeenReleased(TestBatchStatus.OPEN), is(false));
  }

  @Test
  public void testHasBeenReleasedForClosedTestBatch_shouldReturnTrue() {
    assertThat(TestBatchStatus.hasBeenReleased(TestBatchStatus.CLOSED), is(true));
  }

  @Test
  public void testHasBeenReleasedForReleasedTestBatch_shouldReturnTrue() {
    assertThat(TestBatchStatus.hasBeenReleased(TestBatchStatus.RELEASED), is(true));
  }
}
