package org.jembi.bsis.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;

import java.util.Date;

import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.model.modificationtracker.RowModificationTracker;
import org.jembi.bsis.repository.DivisionRepository;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseModificationTrackerUUIDEntityTests extends SecurityContextDependentTestSuite {

  @Autowired
  private DivisionRepository divisionRepository;

  @Test
  public void testPersistDivision() {
    Division division = aDivision().withName("Div").buildAndPersist(entityManager);

    assertThat("Created date has been set to now", dateTimeSetToNow(division.getCreatedDate()), is(true));
    assertThat("Created by has been set correctly", division.getCreatedBy().getUsername(), is(USERNAME));
    assertThat("Last Updated date is same as created", division.getCreatedDate(), is(division.getLastUpdated()));
    assertThat("Last Updated by is same as created", division.getCreatedBy(), is(division.getLastUpdatedBy()));
  }

  @Test
  public void testUpdateDivision() {
    Division division = aDivision().withName("Div").buildAndPersist(entityManager);
    Date createdDateInThePast = DateTime.now().minusDays(2).toDate();
    division.setCreatedDate(createdDateInThePast);
    division.setName("Div2");

    Division updatedDivision = divisionRepository.update(division);

    assertThat("Created date stays the same", createdDateInThePast, is(updatedDivision.getCreatedDate()));
    assertThat("Last Updated date is set to now", dateTimeSetToNow(updatedDivision.getLastUpdated()), is(true));
    assertThat("Last Updated by is set", updatedDivision.getLastUpdatedBy().getUsername(), is(USERNAME));
    assertThat("Name is correct", updatedDivision.getName(), is("Div2"));
  }

  @Test
  public void testSetModificationTracker() {
    Division division = aDivision().withName("Div").buildAndPersist(entityManager);
    RowModificationTracker tracker = new RowModificationTracker();
    Date createdDateInThePast = DateTime.now().minusDays(2).toDate();
    tracker.setCreatedDate(createdDateInThePast);
    tracker.setCreatedBy(loggedInUser);
    division.setModificationTracker(tracker);

    Division updatedDivision = divisionRepository.update(division);
    RowModificationTracker updatedTracker = updatedDivision.getModificationTracker();

    assertThat("Created date is the same", updatedTracker.getCreatedDate(), is(createdDateInThePast));
  }

  private boolean dateTimeSetToNow(Date dateTime) {
    Date now = new Date();
    long gap = now.getTime() - dateTime.getTime();
    // Consider that dateTime is set to now if the gap is less than 5000
    return gap < 5000;
  }
}
