package bsis.importer;

import static helpers.builders.LocationBuilder.aLocation;
import static helpers.matchers.LocationMatcher.hasSameStateAsLocation;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import model.location.Location;
import suites.ContextDependentTestSuite;

public class DataImportServiceTests extends ContextDependentTestSuite {
  
  @Autowired
  private DataImportService dataImportService;

  @Test
  public void testImportLocationData_shouldCreateLocationsFromSpreadsheet()
      throws EncryptedDocumentException, InvalidFormatException, IOException {
    
    // Set up fixture
    FileInputStream fileInputStream = new FileInputStream("test/fixtures/locations.xlsx");
    Workbook workbook = WorkbookFactory.create(fileInputStream);
    
    // Exercise SUT
    dataImportService.importData(workbook, false);
    
    // Verify
    Location firstLocation = findLocationByName("First");
    Location secondLocation = findLocationByName("Second");
    Location thirdLocation = findLocationByName("Third");
    Location expectedFirstLocation = aLocation()
        .withId(firstLocation.getId())
        .withName("First")
        .withNotes("First Location")
        .thatIsVenue()
        .build();
    Location expectedSecondLocation = aLocation()
        .withId(secondLocation.getId())
        .withName("Second")
        .thatIsMobileSite()
        .thatIsDeleted()
        .build();
    Location expectedThirdLocation = aLocation()
        .withId(thirdLocation.getId())
        .withName("Third")
        .thatIsUsageSite()
        .build();
    
    assertThat(firstLocation, hasSameStateAsLocation(expectedFirstLocation));
    assertThat(secondLocation, hasSameStateAsLocation(expectedSecondLocation));
    assertThat(thirdLocation, hasSameStateAsLocation(expectedThirdLocation));
  }
  
  private Location findLocationByName(String name) {
    return entityManager.createQuery("SELECT l FROM Location l WHERE l.name = :name", Location.class)
        .setParameter("name", name)
        .getSingleResult();
  }

}
