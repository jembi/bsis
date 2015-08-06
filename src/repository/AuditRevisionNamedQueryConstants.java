package repository;

public class AuditRevisionNamedQueryConstants {
    
    public static final String NAME_FIND_RECENT_AUDIT_REVISIONS =
            "AuditRevision.findRecentAuditRevisions";
    public static final String QUERY_FIND_RECENT_AUDIT_REVISIONS =
            "SELECT ar " +
            "FROM AuditRevision ar " +
            "ORDER BY ar.timestamp DESC ";

}
