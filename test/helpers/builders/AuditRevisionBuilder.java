package helpers.builders;

import java.util.Date;
import java.util.Set;

import model.audit.AuditRevision;

public class AuditRevisionBuilder {
    
    private int id;
    private long timestamp;
    private String username;
    private Set<String> modifiedEntityNames;

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
    
    public AuditRevisionBuilder withModifiedEntityNames(Set<String> modifiedEntityNames) {
        this.modifiedEntityNames = modifiedEntityNames;
        return this;
    }
    
    public AuditRevision build() {
        AuditRevision auditRevision = new AuditRevision();
        auditRevision.setId(id);
        auditRevision.setTimestamp(timestamp);
        auditRevision.setUsername(username);
        auditRevision.setModifiedEntityNames(modifiedEntityNames);
        return auditRevision;
    }
    
    public static AuditRevisionBuilder anAuditRevision() {
        return new AuditRevisionBuilder();
    }

}
