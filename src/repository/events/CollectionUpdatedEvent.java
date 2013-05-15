package repository.events;


public class CollectionUpdatedEvent extends AbstractApplicationEvent {

  private static final long serialVersionUID = 1L;

  public CollectionUpdatedEvent(String eventId, Object eventContext) {
    super(eventId, eventContext);
    System.out.println("Event created");
    System.out.println("eventId: " + eventId);
    System.out.println(eventContext);
    // TODO Auto-generated constructor stub
  }

}
