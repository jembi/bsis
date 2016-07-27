package org.jembi.bsis.interceptor;

import java.util.Date;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.jembi.bsis.model.modificationtracker.ModificationTracker;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.security.BsisUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class EntitySaveListener implements PersistEventListener, MergeEventListener, PreInsertEventListener {

  private static final long serialVersionUID = 1L;

  public EntitySaveListener() {
  }

  public void onPersist(PersistEvent event) throws HibernateException {
    setModificationTrackerProperties(event);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void onPersist(PersistEvent event, Map arg1) throws HibernateException {
    setModificationTrackerProperties(event);
  }

  @Override
  public void onMerge(MergeEvent event) throws HibernateException {
    setModificationTrackerProperties(event);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void onMerge(MergeEvent event, Map arg1) throws HibernateException {
    setModificationTrackerProperties(event);
  }

  @Override
  public boolean onPreInsert(PreInsertEvent arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  private void setModificationTrackerProperties(PersistEvent event) {
    if (SecurityContextHolder.getContext() != null
        && SecurityContextHolder.getContext().getAuthentication() != null) {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (principal != null && principal instanceof BsisUserDetails) {
        User user = ((BsisUserDetails) principal).getUser();
        if (event.getObject() instanceof ModificationTracker && user != null) {
          ModificationTracker entity = (ModificationTracker) event.getObject();
          if (entity.getCreatedDate() == null) {
            entity.setCreatedDate(new Date());
          }
          if (entity.getCreatedBy() == null) {
            entity.setCreatedBy(user);
          }
          entity.setLastUpdated(new Date());
          entity.setLastUpdatedBy(user);
        }
      }
    }
  }
  
  private void setModificationTrackerProperties(MergeEvent event) {
    if (SecurityContextHolder.getContext() != null 
        && SecurityContextHolder.getContext().getAuthentication() != null) {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (principal != null && principal instanceof BsisUserDetails) {
        User user = ((BsisUserDetails) principal).getUser();
        if (event.getEntity() instanceof ModificationTracker && user != null) {
          ModificationTracker entity = (ModificationTracker) event.getEntity();
          entity.setLastUpdated(new Date());
          entity.setLastUpdatedBy(user);
        }
      }
    }
  }
}