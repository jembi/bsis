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
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * UUID From SQL Generator that calls the GENERATEBINARYUUID() function to obtain a UUID.
 */
public class BSISUUIDGenerator implements IdentifierGenerator {

  public BSISUUIDGenerator() {
  }

  public Serializable generate(SessionImplementor session, Object obj) throws HibernateException {
    Dialect dialect = session.getFactory().getDialect();
    if (dialect instanceof MySQLDialect) {
      return getVersion1UUIDFromDatabase(session);
    } else if (dialect instanceof HSQLDialect) {
      // HSQL does not support the prepared statement sql. The below is a work-around so that
      // the JUnit suite, which uses HSQL, will work.
      return getUUIDForHSQLTestingOnly();
    } else {
      throw new IllegalArgumentException("UUID generation is not yet implemented for dialect " + dialect);
    }
  }

  protected Serializable getVersion1UUIDFromDatabase(SessionImplementor session) {
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


  /**
   * This method for generator a UUID is only to be used for testing purposes.
   * 
   * @return
   */
  private Serializable getUUIDForHSQLTestingOnly() {
    UUID hsqlUUID = UUID.randomUUID();
    Long uuidLSBits = hsqlUUID.getLeastSignificantBits();
    Long uuidMSBits = hsqlUUID.getMostSignificantBits();
    // This sets the MSB to the current time so that any ordering based on the UUID will be
    // chronologically sequential.
    uuidMSBits = System.currentTimeMillis();
    // UUIDs are compared by it's long value. This means that the MSB
    // leading bit is a sign bit. This will affect the ordering, and so is removed. This should
    // suffice for purposes of testing i.e. the UUID will still be unique.
    uuidMSBits = uuidMSBits & 0x7FFFFFFFFFFFFFFFL;

    // cater for different UUIDs generated on the same millisecond. Add a sequential counter
    // to the Most Significant part of the Least Signficant bits. This will ensure sequential
    // ordering based on UUIDs
    uuidLSBits = uuidLSBits & 0x007FFFFFFFFFFFFFL;
    synchronized (BSISUUIDGenerator.class) {
      long bitmask = (testUUIDCounter << 55L);
      uuidLSBits = uuidLSBits | (bitmask);
      // allow for 0-254 increments of the counter before wrapping around.
      testUUIDCounter = ++testUUIDCounter % 0xFEL;
    }

    return new UUID(uuidMSBits, uuidLSBits);
  }

  // counter to ensure unique Test UUIDs on the same millisecond.
  public static long testUUIDCounter = 0L;
}