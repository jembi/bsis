package bsis.importer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

import backingform.LocationBackingForm;
import backingform.validator.LocationBackingFormValidator;
import repository.LocationRepository;

@Transactional
@Service
public class DataImportService {
  
  @Autowired
  private LocationBackingFormValidator locationBackingFormValidator;
  @Autowired
  private LocationRepository locationRepository;

  public void importData(Workbook workbook) {
    System.out.println("Import started");
    importLocationData(workbook.getSheet("Locations"));
    System.out.println("Import completed");
  }
  
  private void importLocationData(Sheet sheet) {
    
    // Keep a reference to the row containing the headers
    Row headers = null;
    
    int locationCount = 0;

    for (Row row : sheet) {

      if (headers == null) {
        headers = row;
        continue;
      }
      
      locationCount += 1;
        
      LocationBackingForm locationBackingForm = new LocationBackingForm();
      
      for (Cell cell : row) {
        
        Cell header = headers.getCell(cell.getColumnIndex());
        
        switch (header.getStringCellValue()) {
          
          case "name":
            locationBackingForm.setName(cell.getStringCellValue());
            break;
            
          case "isUsageSite":
            locationBackingForm.setIsUsageSite(cell.getBooleanCellValue());
            break;
            
          case "isMobileSite":
            locationBackingForm.setIsMobileSite(cell.getBooleanCellValue());
            break;
            
          case "isVenue":
            locationBackingForm.setIsVenue(cell.getBooleanCellValue());
            break;
            
          case "isDeleted":
            locationBackingForm.setIsDeleted(cell.getBooleanCellValue());
            break;
            
          case "notes":
            locationBackingForm.setNotes(cell.getStringCellValue());
            break;
          
          default:
            System.out.println("Unknown location column: " + header.getStringCellValue());
            break;
        }
      }
      
      BindException errors = new BindException(locationBackingForm, "LocationBackingForm");
      locationBackingFormValidator.validate(locationBackingForm, errors);
      
      if (errors.hasErrors()) {
        throw new IllegalArgumentException("Invalid location: " + locationBackingForm.getName());
      }
      
      locationRepository.saveLocation(locationBackingForm.getLocation());
    }
    
    System.out.println("Imported " + locationCount + " location(s)");
  }

}
