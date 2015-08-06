package interceptor;

import model.audit.AuditRevision;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevision auditRevision = (AuditRevision) revisionEntity;

        // Store the username of the user making the revision
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            auditRevision.setUsername(authentication.getName());
        }
    }

}
