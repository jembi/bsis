package repository.events;


public class DonationUpdatedEvent extends AbstractApplicationEvent {

  private static final long serialVersionUID = 1L;

  public DonationUpdatedEvent(String eventId, Object eventContext) {
    super(eventId, eventContext);
  }

}
