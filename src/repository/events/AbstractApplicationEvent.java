package repository.events;

import org.springframework.context.ApplicationEvent;

import java.util.Map;


abstract class AbstractApplicationEvent extends ApplicationEvent {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  protected String eventId;
  protected Object eventContext;

  public AbstractApplicationEvent(String eventId, Object eventContext) {
    super(eventId);
    this.eventId = eventId;
    this.eventContext = eventContext;
  }

  public String getEventId() {
    return eventId;
  }

  public Object getEventContext() {
    return eventContext;
  }

  public Object getContextParams(String paramName) {
    if (eventContext == null || !(eventContext instanceof Map)) {
      return null;
    }
    return ((Map) eventContext).get(paramName);
  }
}
