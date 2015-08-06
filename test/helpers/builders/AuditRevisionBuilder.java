package helpers.builders;

import java.util.Date;

import model.audit.AuditRevision;

public class AuditRevisionBuilder {
    
    private int id;
    private long timestamp;
    private String username;

    public AuditRevisionBuilder withId(int id) {
        this.id = id;
        return this;
    }
    
    public AuditRevisionBuilder withRevisionDate(Date revisionDate) {
        timestamp = revisionDate.getTime();
        return this;
    }
    
    public AuditRevisionBuilder withUsername(String username) {
        this.username = username;
        return this;
    }
    
    public AuditRevision build() {
        AuditRevision auditRevision = new AuditRevision();
        auditRevision.setId(id);
        auditRevision.setTimestamp(timestamp);
        auditRevision.setUsername(username);
        return auditRevision;
    }
    
    public static AuditRevisionBuilder anAuditRevision() {
        return new AuditRevisionBuilder();
    }

}
