package helpers.builders;

import java.util.Date;

import model.audit.AuditRevision;

public class AuditRevisionBuilder {
    
    private long timestamp;
    
    public AuditRevisionBuilder withRevisionDate(Date revisionDate) {
        timestamp = revisionDate.getTime();
        return this;
    }
    
    public AuditRevision build() {
        AuditRevision auditRevision = new AuditRevision();
        auditRevision.setTimestamp(timestamp);
        return auditRevision;
    }
    
    public static AuditRevisionBuilder anAuditRevision() {
        return new AuditRevisionBuilder();
    }

}
