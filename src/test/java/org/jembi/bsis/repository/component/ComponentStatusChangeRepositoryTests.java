package org.jembi.bsis.repository.component;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeBuilder.aComponentStatusChange;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aComponentStatusChangeReason;
import static org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory.DISCARDED;
import static org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory.UNSAFE;

public class ComponentStatusChangeRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private ComponentStatusChangeRepository repository;

  @Test
  public void testFindLatestDiscardReasonForComponentWithNoDiscardReason_shouldReturnEmptyOptional() {
    ComponentStatusChangeReason reason = aComponentStatusChangeReason()
        .withComponentStatusChangeReasonCategory(UNSAFE).build();

    Component component = aComponent().buildAndPersist(entityManager);

    aComponentStatusChange().withComponent(component).withNewStatus(ComponentStatus.UNSAFE)
        .withStatusChangeReason(reason).buildAndPersist(entityManager);

    Optional<ComponentStatusChange> optionalReason = repository.findLatestDiscardReasonForComponent(component);

    assertThat(optionalReason, is(emptyOptional()));
  }

  @Test
  public void testFindLatestDiscardReasonForComponentWithExactlyOneDiscardReason_shouldReturnDiscardReason() {
    ComponentStatusChangeReason available = aComponentStatusChangeReason()
        .withComponentStatusChangeReasonCategory(UNSAFE).build();

    ComponentStatusChangeReason discarded = aComponentStatusChangeReason()
        .withComponentStatusChangeReasonCategory(DISCARDED).build();

    Component component = aComponent().buildAndPersist(entityManager);

    aComponentStatusChange().withComponent(component).withNewStatus(ComponentStatus.UNSAFE)
        .withStatusChangeReason(available).buildAndPersist(entityManager);

    ComponentStatusChange discardedStatusChange = aComponentStatusChange().withComponent(component)
        .withNewStatus(ComponentStatus.DISCARDED).withStatusChangeReason(discarded).buildAndPersist(entityManager);

    Optional<ComponentStatusChange> actual = repository.findLatestDiscardReasonForComponent(component);

    assertThat(actual, is(optionalWithValue(equalTo(discardedStatusChange))));
  }

  @Test
  public void testFindLatestDiscardReasonForComponentWithMoreThanOneDiscardReason_shouldReturnLatestDiscardReason() {
    Date yesterday = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
    Date today = new Date();

    ComponentStatusChangeReason discarded = aComponentStatusChangeReason()
        .withComponentStatusChangeReasonCategory(DISCARDED).build();

    Component component = aComponent().buildAndPersist(entityManager);

    aComponentStatusChange().withComponent(component).withNewStatus(ComponentStatus.DISCARDED)
        .withStatusChangeReason(discarded).withStatusChangedOn(yesterday).buildAndPersist(entityManager);

    aComponentStatusChange().withComponent(component).withNewStatus(ComponentStatus.QUARANTINED)
        .withStatusChangeReason(discarded).withStatusChangedOn(today).buildAndPersist(entityManager);

    ComponentStatusChange latestDiscardedStatusChange = aComponentStatusChange().withComponent(component)
        .withNewStatus(ComponentStatus.DISCARDED).withStatusChangeReason(discarded).withStatusChangedOn(today)
        .buildAndPersist(entityManager);

    Optional<ComponentStatusChange> actual = repository.findLatestDiscardReasonForComponent(component);

    assertThat(actual, is(optionalWithValue(equalTo(latestDiscardedStatusChange))));
  }
}
