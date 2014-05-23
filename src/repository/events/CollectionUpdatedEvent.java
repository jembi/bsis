package repository.events;


public class CollectionUpdatedEvent extends AbstractApplicationEvent {

  private static final long serialVersionUID = 1L;

  public CollectionUpdatedEvent(String eventId, Object eventContext) {
    super(eventId, eventContext);
    // TODO Auto-generated constructor stub
  }

}
