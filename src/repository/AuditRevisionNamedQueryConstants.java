package repository;

public class AuditRevisionNamedQueryConstants {
    
    public static final String NAME_FIND_RECENT_AUDIT_REVISIONS =
            "AuditRevision.findRecentAuditRevisions";
    public static final String QUERY_FIND_RECENT_AUDIT_REVISIONS =
            "SELECT ar " +
            "FROM AuditRevision ar " +
            "ORDER BY ar.timestamp DESC ";
    
    public static final String NAME_FIND_AUDIT_REVISIONS_BY_USER =
            "AuditRevision.findAuditRevisionsByUser";
    public static final String QUERY_FIND_AUDIT_REVISIONS_BY_USER =
            "SELECT ar " +
            "FROM AuditRevision ar, User u " +
            "WHERE ar.username = u.username " +
            "AND (LOWER(u.username) LIKE :search " +
            " OR LOWER(u.firstName) LIKE :search " +
            " OR LOWER(u.lastName) LIKE :search)" +
            "ORDER BY ar.timestamp DESC ";

}
