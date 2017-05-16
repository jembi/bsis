package org.jembi.bsis.suites;

import java.sql.SQLException;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.UserRepository;
import org.jembi.bsis.security.BsisUserDetails;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.transaction.AfterTransaction;


/**
 * Super class for DBUnit tests that handles loading of the dataset into the database and loading a user into the 
 * Spring Security Context.
 */
public abstract class DBUnitContextDependentTestSuite extends ContextDependentTestSuite {

  protected User loggedInUser;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  private DataSource dataSource;

  /**
   * Implement and specify which Dataset XML file should be loaded into the database.
   *
   * @return IDataSet
   */
  protected abstract IDataSet getDataSet() throws Exception;

  /**
   * Auto generated method comment
   */
  protected User getLoggedInUser() throws Exception {
    User user;
    try {
      user = userRepository.findUserById(UUID.fromString("9bb07a38-eb7f-4e35-b5b1-34e77ad79a81"));
    } catch (NoResultException e) {
      throw new RuntimeException("Could not find a user with id '1'. Please add a user to the DBUnit dataset XML or override `getLoggedInUser`.", e);
    }
    return user;
  }

  @Before
  public void init() throws Exception {
    IDatabaseConnection connection = getConnection();
    try {
      IDataSet dataSet = getDataSet();
      DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
      initSpringSecurityUser(); // must be done after data has been inserted successfully
    } finally {
      connection.close();
    }
  }

  @AfterTransaction
  public void after() throws Exception {
    IDatabaseConnection connection = getConnection();
    try {
      clearSpringSecurityUser(); // reverse the initiation of the Spring Security user
      IDataSet dataSet = getDataSet();
      DatabaseOperation.DELETE_ALL.execute(connection, dataSet);
    } finally {
      connection.close();
    }
  }

  private IDatabaseConnection getConnection() throws SQLException {
    IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
    DatabaseConfig config = connection.getConfig();
    config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
    config.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
    return connection;
  }

  private void initSpringSecurityUser() throws Exception {
    User user = getLoggedInUser();
    if (user != null) {
      BsisUserDetails bsisUser = new BsisUserDetails(getLoggedInUser());
      TestingAuthenticationToken auth = new TestingAuthenticationToken(bsisUser, "Credentials");
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
  }

  private void clearSpringSecurityUser() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(null);
  }
}
