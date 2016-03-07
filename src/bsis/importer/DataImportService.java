package bsis.importer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backingform.LocationBackingForm;
import backingform.validator.DonorBackingFormValidator;
import backingform.validator.LocationBackingFormValidator;
import model.location.Location;
import repository.DonorRepository;
import repository.LocationRepository;

@Transactional
@Service
@Scope("prototype")
public class DataImportService {
  
  @Autowired
  private LocationBackingFormValidator locationBackingFormValidator;
  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private DonorBackingFormValidator donorBackingFormValidator;
  @Autowired
  private DonorRepository donorRepository;


  private boolean validationOnly;


  public void importData(Workbook workbook, boolean validationOnly) {

    this.validationOnly = validationOnly;
    String action = validationOnly ? "Validation" : "Import";

    System.out.println(action + " of Locations started");
    importLocationData(workbook.getSheet("Locations"));
    System.out.println(action + " of Locations completed");

    System.out.println(action + " of Donors started");
    importDonorData(workbook.getSheet("Donors"));
    System.out.println(action + " of Donors completed");
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
        System.out.println("Invalid location on row " + (row.getRowNum() + 1) + ". " + getErrorsString(errors));
        throw new IllegalArgumentException("Invalid location");
      }

      // Only save if validationOnly is false
      if (!validationOnly) {
        locationRepository.saveLocation(locationBackingForm.getLocation());
      }
    }


    if (validationOnly) {
      System.out.println("Validated " + locationCount + " location(s)");
    } else {
      System.out.println("Imported " + locationCount + " location(s)");
    }

  }

  private void importDonorData(Sheet sheet) {
    Map<String, Location> locationCache = buildLocationCache();
  }

  private Map<String, Location> buildLocationCache () {
    Map<String, Location>  locationCache = new HashMap<>();
    List<Location> locations = locationRepository.getAllLocations();
    for (Location location : locations) {
      locationCache.put(location.getName(), location);
    }
    return locationCache;
  }

  private String getErrorsString(BindException errors) {
    String errorsStr = "Errors:";
    for (ObjectError error : errors.getAllErrors()) {
      errorsStr = errorsStr + "\n\t" + error.getDefaultMessage();
    }
    return errorsStr;
  }

}
