package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.AdverseEventPersister;
import org.jembi.bsis.model.adverseevent.AdverseEvent;
import org.jembi.bsis.model.adverseevent.AdverseEventType;

public class AdverseEventBuilder extends AbstractEntityBuilder<AdverseEvent> {

  private Long id;
  private AdverseEventType type = anAdverseEventType().build();
  private String comment;

  public AdverseEventBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public AdverseEventBuilder withType(AdverseEventType type) {
    this.type = type;
    return this;
  }

  public AdverseEventBuilder withComment(String comment) {
    this.comment = comment;
    return this;
  }

  @Override
  public AdverseEvent build() {
    AdverseEvent adverseEvent = new AdverseEvent();
    adverseEvent.setId(id);
    adverseEvent.setType(type);
    adverseEvent.setComment(comment);
    return adverseEvent;
  }

  @Override
  public AbstractEntityPersister<AdverseEvent> getPersister() {
    return new AdverseEventPersister();
  }

  public static AdverseEventBuilder anAdverseEvent() {
    return new AdverseEventBuilder();
  }

}
