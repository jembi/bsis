package bsis.importer;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DataImporter {
  
  public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {
    
    if (args.length != 1) {
      throw new IllegalArgumentException("A single argument must be provided for the path to the spreadsheet");
    }
    
    // Read in the spreadsheet
    FileInputStream fileInputStream = new FileInputStream(args[0]);
    Workbook workbook = WorkbookFactory.create(fileInputStream);
    
    // Load the application context
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/bsis-servlet.xml");
    
    // Import the data
    DataImportService importService = applicationContext.getBean(DataImportService.class);
    
    try {
      importService.importData(workbook);
    } catch (Exception e) {
      System.err.println("Import failed");
      e.printStackTrace();
      System.exit(1);
    }
    
    // Close the application context
    applicationContext.close();
  }

}
