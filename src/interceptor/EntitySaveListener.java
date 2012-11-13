package interceptor;

import java.util.Date;
import java.util.Map;

import model.modificationtracker.ModificationTracker;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntitySaveListener implements PersistEventListener, MergeEventListener {

  private static final long serialVersionUID = 1L;

  static final Logger logger = LoggerFactory.getLogger(EntitySaveListener.class);

  public void onPersist(PersistEvent event) throws HibernateException {
    System.out.println("onPersist");

    if (event.getObject() instanceof ModificationTracker) {
      ModificationTracker entity = (ModificationTracker) event.getObject();
      entity.setCreatedDate(new Date());
      entity.setLastUpdated(new Date());
    }
  }

  @Override
  public void onPersist(PersistEvent event, Map arg1) throws HibernateException {
    // TODO Auto-generated method stub    
  }

  @Override
  public void onMerge(MergeEvent event) throws HibernateException {
    System.out.println("onMerge");
    if (event.getEntity() instanceof ModificationTracker) {
      ModificationTracker entity = (ModificationTracker) event.getEntity();
      entity.setLastUpdated(new Date());
    }
  }

  @Override
  public void onMerge(MergeEvent arg0, Map arg1) throws HibernateException {
    // TODO Auto-generated method stub
  }

}
