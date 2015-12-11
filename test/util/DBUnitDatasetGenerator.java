package util;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Utility class to automagically generate DBUnit XML datasets using the data from your database. To
 * use this class, edit the constants at the top to include the tables you wish to export and
 * provide the access details for the database that will be mined.
 */
class DBUnitDatasetGenerator {

  /* names of the tables that should be exported */
  private static final String[] TABLE_NAMES = {"BloodTestingRule", "BloodTest"};

  /* the name of the output dataset XML file */
  private static final String OUTPUT_FILENAME = "test/dataset/dataset.xml";

  /* your database access details */
  private static final String DATABASE_USER = "root";

  private static final String DATABASE_PWD = "root";

  private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/bsis";

  private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";

  public static void main(String[] args) throws Exception {
    // database connection
    Class driverClass = Class.forName(DATABASE_DRIVER);
    Connection jdbcConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PWD);
    IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

    // partial database export
    QueryDataSet partialDataSet = new QueryDataSet(connection);
    for (String tableName : TABLE_NAMES) {
      partialDataSet.addTable(tableName);
    }
    FlatXmlDataSet.write(partialDataSet, new FileOutputStream(OUTPUT_FILENAME));
  }
}
