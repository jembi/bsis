package org.jembi.bsis.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

public class BSISUUIDGeneratorTests {

  @Test
  public void testConvertBytesToUUID() {
    BSISUUIDGenerator uuidGenerator = new BSISUUIDGenerator();
    UUID testData = UUID.randomUUID();

    Long lsb = testData.getLeastSignificantBits();
    Long msb = testData.getMostSignificantBits();
    byte[] bytesLSB = ByteBuffer.allocate(8).putLong(lsb).array();
    byte[] bytesMSB = ByteBuffer.allocate(8).putLong(msb).array();
    
    byte[] UUIDByteArray = ArrayUtils.addAll(bytesMSB, bytesLSB);

    UUID uuidResult = uuidGenerator.convertBytesToUUID(UUIDByteArray);

    assertThat(uuidResult, is(testData));
  }
}