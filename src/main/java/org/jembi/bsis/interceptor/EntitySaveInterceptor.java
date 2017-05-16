package org.jembi.bsis.interceptor;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.jembi.bsis.model.modificationtracker.ModificationTracker;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.security.BsisUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class EntitySaveInterceptor extends EmptyInterceptor {

  private static final long serialVersionUID = 2457309373796363013L;

  @Override
  public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
      String[] propertyNames, Type[] types) {
    setModificationTrackerProperties(entity);
    return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
  }

  @Override
  public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    setModificationTrackerProperties(entity);
    return super.onSave(entity, id, state, propertyNames, types);
  }

  private void setModificationTrackerProperties(Object object) {
    if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (principal != null && principal instanceof BsisUserDetails) {
        User user = ((BsisUserDetails) principal).getUser();
        if (object instanceof ModificationTracker && user != null) {
          ModificationTracker entity = (ModificationTracker) object;
          Date currentDateTime = new Date();
          if (entity.getCreatedDate() == null) {
            entity.setCreatedDate(currentDateTime);
          }
          if (entity.getCreatedBy() == null) {
            entity.setCreatedBy(user);
          }
          entity.setLastUpdated(currentDateTime);
          entity.setLastUpdatedBy(user);
        }
      }
    }
  }
}
