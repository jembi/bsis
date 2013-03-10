package repository.events;


public class CollectionAddedEvent extends AbstractApplicationEvent {

  /**
   * 
   */
  private static final long serialVersionUID = 7320839380000196568L;

  public CollectionAddedEvent(String eventId, Object eventContext) {
    super(eventId, eventContext);
    // TODO Auto-generated constructor stub
  }

}
