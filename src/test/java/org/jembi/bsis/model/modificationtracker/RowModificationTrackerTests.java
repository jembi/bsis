package org.jembi.bsis.model.modificationtracker;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Date;

import org.junit.Test;

public class RowModificationTrackerTests {

  @Test
  public void testSerialization() throws Exception {
    // set up the data
    String userName = "test";
    Date createdDate = new Date();
    RowModificationTracker row = new RowModificationTracker();
    row.setCreatedBy(aUser().withUsername(userName).build());
    row.setCreatedDate(createdDate);

    RowModificationTracker deserializedObject = null;
    try (PipedOutputStream pos = new PipedOutputStream()) {
      try (PipedInputStream pis = new PipedInputStream()) {
        pis.connect(pos);
        // serialize the object
        try (ObjectOutputStream so = new ObjectOutputStream(pos)) {
          so.writeObject(row);
          so.flush();
        }
        // deserialize the object
        try (ObjectInputStream si = new ObjectInputStream(pis)) {
          deserializedObject = (RowModificationTracker)si.readObject();
        }
      }
    }

    // check it worked
    assertThat(deserializedObject, notNullValue());
    assertThat(deserializedObject.getCreatedDate(), is(createdDate));
    assertThat(deserializedObject.getCreatedBy(), notNullValue());
    assertThat(deserializedObject.getCreatedBy().getUsername(), is(userName));
  }
}
