package org.jembi.bsis.repository;

public class AuditRevisionNamedQueryConstants {

  public static final String NAME_FIND_AUDIT_REVISIONS =
      "AuditRevision.findAuditRevisions";
  public static final String QUERY_FIND_AUDIT_REVISIONS =
      "SELECT ar " +
          "FROM AuditRevision ar " +
          "WHERE ar.timestamp >= :startTimestamp " +
          "AND ar.timestamp <= :endTimestamp " +
          "ORDER BY ar.timestamp DESC ";

  public static final String NAME_FIND_AUDIT_REVISIONS_BY_USER =
      "AuditRevision.findAuditRevisionsByUser";
  public static final String QUERY_FIND_AUDIT_REVISIONS_BY_USER =
      "SELECT ar " +
          "FROM AuditRevision ar, User u " +
          "WHERE ar.timestamp >= :startTimestamp " +
          "AND ar.timestamp <= :endTimestamp " +
          "AND ar.username = u.username " +
          "AND (LOWER(u.username) LIKE :search " +
          " OR LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE :search) " +
          "ORDER BY ar.timestamp DESC ";

}
