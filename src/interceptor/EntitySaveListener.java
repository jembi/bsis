package interceptor;

import java.util.Date;
import java.util.Map;

import model.modificationtracker.ModificationTracker;
import model.user.User;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.Security;

import security.V2VUserDetails;

@Component
public class EntitySaveListener implements PersistEventListener, MergeEventListener, PreInsertEventListener {

  private static final long serialVersionUID = 1L;

  static final Logger logger = LoggerFactory.getLogger(EntitySaveListener.class);

  @Autowired
  private LocalEntityManagerFactoryBean entityManagerFactory;

  public EntitySaveListener() {
    System.out.println("listener created here");
    System.out.println(entityManagerFactory);
  }

  public void onPersist(PersistEvent event) throws HibernateException {
    System.out.println("onPersist");

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal != null && principal instanceof V2VUserDetails) {
      User user = ((V2VUserDetails) principal).getUser();
      if (event.getObject() instanceof ModificationTracker && user != null) {
        ModificationTracker entity = (ModificationTracker) event.getObject();
        entity.setCreatedDate(new Date());
        entity.setCreatedBy(user);
        entity.setLastUpdated(new Date());
        entity.setLastUpdatedBy(user);
      }
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void onPersist(PersistEvent event, Map arg1) throws HibernateException {
    // TODO Auto-generated method stub    
  }

  @Override
  public void onMerge(MergeEvent event) throws HibernateException {
    System.out.println("onMerge");
    if (SecurityContextHolder.getContext() != null &&
        SecurityContextHolder.getContext().getAuthentication() != null) {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (principal != null && principal instanceof V2VUserDetails) {
        User user = ((V2VUserDetails) principal).getUser();
        if (event.getEntity() instanceof ModificationTracker && user != null) {
          ModificationTracker entity = (ModificationTracker) event.getEntity();
          entity.setLastUpdated(new Date());
          entity.setLastUpdatedBy(user);
        }
      }
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void onMerge(MergeEvent arg0, Map arg1) throws HibernateException {
    // TODO Auto-generated method stub
  }

  @Override
  public boolean onPreInsert(PreInsertEvent arg0) {
    // TODO Auto-generated method stub
    return false;
  }

}
