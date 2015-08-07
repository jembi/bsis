package repository.events;


public class DonationUpdatedEvent extends AbstractApplicationEvent {

  private static final long serialVersionUID = 1L;

  public DonationUpdatedEvent(String eventId, Object eventContext) {
    super(eventId, eventContext);
    System.out.println("Event created");
    System.out.println("eventId: " + eventId);
    System.out.println(eventContext);
    // TODO Auto-generated constructor stub
  }

}
