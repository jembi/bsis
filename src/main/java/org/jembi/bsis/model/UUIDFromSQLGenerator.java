package org.jembi.bsis.model;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * UUID From SQL Generator that calls the GENERATEBINARYUUID() function to obtain a UUID.
 */
public class UUIDFromSQLGenerator implements IdentifierGenerator {

  public UUIDFromSQLGenerator() {
  }

  public Serializable generate(SessionImplementor session, Object obj) throws HibernateException {
    Dialect dialect = session.getFactory().getDialect();
    // HSQL does not support the prepared statement sql. The below is a work-around so that
    // the JUnit suite, which uses HSQL, will work.
    if (dialect instanceof HSQLDialect) {
      return UUID.randomUUID();
    } else if (dialect instanceof MySQL5Dialect) {
      final String sql = "select GENERATEBINARYUUID() as uuid";
      try {
        PreparedStatement st =
            session.getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().prepareStatement(sql);
        try {
          ResultSet rs = session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().extract(st);
          final UUID result;
          try {
            if (!rs.next()) {
              throw new HibernateException("The database returned no UUID identity value");
            }
            result = convertBytesToUUID(rs.getBytes("uuid"));
          } finally {
            session.getTransactionCoordinator().getJdbcCoordinator().release(rs, st);
          }
          return result;
        } finally {
          session.getTransactionCoordinator().getJdbcCoordinator().release(st);
        }
      }
      catch (SQLException sqle) {
        throw session.getFactory().getSQLExceptionHelper().convert(sqle, "could not retrieve UUID", sql);
      }
    } else {
      throw new IllegalArgumentException("UUID generation is not yet implemented for dialect " + dialect);
    }
  }

  /**
   * This method takes a UUID represented as a 16 byte array and converts it to the java.util.UUID
   * type.
   * 
   * @param bytes
   * @return
   */
  protected UUID convertBytesToUUID(byte[] bytes) {
    if (bytes.length != 16) {
      throw new IllegalArgumentException("Excepted 16 bytes only");
    }
    ByteBuffer bb = ByteBuffer.wrap(bytes);
    long firstLong = bb.getLong();
    long secondLong = bb.getLong();
    return new UUID(firstLong, secondLong);
  }
}