package bsis.importer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.IllegalStateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import backingform.DonorBackingForm;
import backingform.LocationBackingForm;
import backingform.validator.DonorBackingFormValidator;
import backingform.validator.LocationBackingFormValidator;
import model.address.AddressType;
import model.address.ContactMethodType;
import model.idtype.IdType;
import model.location.Location;
import model.preferredlanguage.PreferredLanguage;
import repository.ContactMethodTypeRepository;
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
  @Autowired
  private ContactMethodTypeRepository contactMethodTypeRepository;



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
    Map<String, PreferredLanguage> preferredLanguageCache = buildPreferredLanguageCache();
    Map<String, IdType> idTypeCache = buildIdTypeCache();
    Map<String, ContactMethodType> contactMethodTypeCache = buildContactMethodTypeCache();
    Map<String, AddressType> addressTypeCache = buildAddressTypeCache();

    // Keep a reference to the row containing the headers
    Row headers = null;

    int donorCount = 0;

    for (Row row : sheet) {

      String externalDonorId = null;

      if (headers == null) {
        headers = row;
        continue;
      } else {
        donorCount += 1;
      }

      DonorBackingForm donorBackingForm = new DonorBackingForm();
      BindException errors = new BindException(donorBackingForm, "DonorBackingForm");

      for (Cell cell : row) {
        Cell header = headers.getCell(cell.getColumnIndex());

        switch (header.getStringCellValue()) {

          case "externalDonorId":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            externalDonorId = cell.getStringCellValue();
            break;

          case "title":
            donorBackingForm.setTitle(cell.getStringCellValue());
            break;

          case "firstName":
            donorBackingForm.setFirstName(cell.getStringCellValue());
            break;

          case "middleName":
            donorBackingForm.setMiddleName(cell.getStringCellValue());
            break;

          case "lastName":
            donorBackingForm.setLastName(cell.getStringCellValue());
            break;

          case "callingName":
            donorBackingForm.setCallingName(cell.getStringCellValue());
            break;

          case "gender":
            donorBackingForm.setGender(cell.getStringCellValue());
            break;

          case "preferredLanguage":
            donorBackingForm.setPreferredLanguage(preferredLanguageCache.get(cell.getStringCellValue()));
            break;

          case "birthDate":
            try {
              donorBackingForm.setBirthDate(cell.getDateCellValue());
            } catch (IllegalStateException e) {
                errors.rejectValue("donor.birthDate", "birthDate.invalid", "Invalid birthDate");
            }


            break;

          case "age":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            donorBackingForm.setAge(cell.getStringCellValue());
            break;

          case "bloodAbo":
            donorBackingForm.setBloodAbo(cell.getStringCellValue());
            break;

          case "bloodRh":
            donorBackingForm.setBloodRh(cell.getStringCellValue());
            break;

          case "notes":
            donorBackingForm.setNotes(cell.getStringCellValue());
            break;

          case "venue":
            donorBackingForm.setVenue(locationCache.get(cell.getStringCellValue()));
            break;

          case "idType":
            if (StringUtils.isNotEmpty(cell.getStringCellValue())) {
              IdType idType = idTypeCache.get(cell.getStringCellValue());
              if (idType == null) {
                errors.rejectValue("donor.idType", "idType.invalid", "Invalid idType");
              }
              donorBackingForm.setIdType(idType);
            }
            break;

          case "idNumber":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            donorBackingForm.setIdNumber(cell.getStringCellValue());
            break;

          case "preferredContactType":
            if (StringUtils.isNotEmpty(cell.getStringCellValue())) {
              ContactMethodType contactMethodType = contactMethodTypeCache.get(cell.getStringCellValue());
              if (contactMethodType == null) {
                errors.rejectValue("donor.contactMethodType", "contactMethodType.invalid",
                    "Invalid preferredContactType");
              }
              donorBackingForm.setContactMethodType(contactMethodType);
            }
            break;

          case "mobileNumber":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            donorBackingForm.setMobileNumber(cell.getStringCellValue());
            break;

          case "homeNumber":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            donorBackingForm.setHomeNumber(cell.getStringCellValue());
            break;

          case "workNumber":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            donorBackingForm.setWorkNumber(cell.getStringCellValue());
            break;

          case "email":
            donorBackingForm.setEmail(cell.getStringCellValue());
            break;

          case "preferredAddressType":
            if (StringUtils.isNotEmpty(cell.getStringCellValue())) {
              AddressType preferredAddressType = addressTypeCache.get(cell.getStringCellValue());
              if (preferredAddressType == null) {
                errors.rejectValue("donor.addressType", "addressType.invalid",
                    "Invalid preferredAddressType");
              }
              donorBackingForm.setPreferredAddressType(preferredAddressType);
            }
            break;

          case "homeAddressLine1":
            donorBackingForm.setHomeAddressLine1(cell.getStringCellValue());
            break;

          case "homeAddressLine2":
            donorBackingForm.setHomeAddressLine2(cell.getStringCellValue());
            break;

          case "homeAddressCity":
            donorBackingForm.setHomeAddressCity(cell.getStringCellValue());
            break;

          case "homeAddressProvince":
            donorBackingForm.setHomeAddressProvince(cell.getStringCellValue());
            break;

          case "homeAddressDistrict":
            donorBackingForm.setHomeAddressDistrict(cell.getStringCellValue());
            break;

          case "homeAddressState":
            donorBackingForm.setHomeAddressState(cell.getStringCellValue());
            break;

          case "homeAddressCountry":
            donorBackingForm.setHomeAddressCountry(cell.getStringCellValue());
            break;

          case "homeAddressZipcode":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            donorBackingForm.setHomeAddressZipcode(cell.getStringCellValue());
            break;

          case "workAddressLine1":
            donorBackingForm.setWorkAddressLine1(cell.getStringCellValue());
            break;

          case "workAddressLine2":
            donorBackingForm.setWorkAddressLine2(cell.getStringCellValue());
            break;

          case "workAddressCity":
            donorBackingForm.setWorkAddressCity(cell.getStringCellValue());
            break;

          case "workAddressProvince":
            donorBackingForm.setWorkAddressProvince(cell.getStringCellValue());
            break;

          case "workAddressDistrict":
            donorBackingForm.setWorkAddressDistrict(cell.getStringCellValue());
            break;

          case "workAddressState":
            donorBackingForm.setWorkAddressState(cell.getStringCellValue());
            break;

          case "workAddressCountry":
            donorBackingForm.setWorkAddressCountry(cell.getStringCellValue());
            break;

          case "workAddressZipcode":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            donorBackingForm.setworkAddressZipcode(cell.getStringCellValue());
            break;

          case "postalAddressLine1":
            donorBackingForm.setPostalAddressLine1(cell.getStringCellValue());
            break;

          case "postalAddressLine2":
            donorBackingForm.setPostalAddressLine2(cell.getStringCellValue());
            break;

          case "postalAddressCity":
            donorBackingForm.setPostalAddressCity(cell.getStringCellValue());
            break;

          case "postalAddressProvince":
            donorBackingForm.setPostalAddressProvince(cell.getStringCellValue());
            break;

          case "postalAddressDistrict":
            donorBackingForm.setPostalAddressDistrict(cell.getStringCellValue());
            break;

          case "postalAddressState":
            donorBackingForm.setPostalAddressState(cell.getStringCellValue());
            break;

          case "postalAddressCountry":
            donorBackingForm.setPostalAddressCountry(cell.getStringCellValue());
            break;

          case "postalAddressZipcode":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            donorBackingForm.setPostalAddressZipcode(cell.getStringCellValue());
            break;

          default:
            System.out.println("Unknown donor column: " + header.getStringCellValue());
            break;
        }
      }

      donorBackingFormValidator.validate(donorBackingForm, errors);

      if (errors.hasErrors()) {
        System.out.println("Invalid donor on row " + (row.getRowNum() + 1) + ". " + getErrorsString(errors));
        throw new IllegalArgumentException("Invalid donor");
      }

    }

    if (validationOnly) {
      System.out.println("Validated " + donorCount + " location(s)");
    } else {
      System.out.println("Imported " + donorCount + " location(s)");
    }

  }

  private Map<String, Location> buildLocationCache () {
    Map<String, Location>  locationCache = new HashMap<>();
    List<Location> locations = locationRepository.getAllLocations();
    for (Location location : locations) {
      locationCache.put(location.getName(), location);
    }
    return locationCache;
  }

  private Map<String, PreferredLanguage> buildPreferredLanguageCache () {
    Map<String, PreferredLanguage>  preferredLanguageCache = new HashMap<>();
    List<PreferredLanguage> languages = donorRepository.getAllLanguages();
    for (PreferredLanguage language : languages) {
      preferredLanguageCache.put(language.getPreferredLanguage(), language);
    }
    return preferredLanguageCache;
  }

  private Map<String, IdType> buildIdTypeCache() {
    Map<String, IdType>  idTypeMap = new HashMap<>();
    List<IdType> idTypes = donorRepository.getAllIdTypes();
    for (IdType idType : idTypes) {
      idTypeMap.put(idType.getIdType(), idType);
    }
    return idTypeMap;
  }

  private Map<String, ContactMethodType> buildContactMethodTypeCache() {
    Map<java.lang.String, ContactMethodType>  contactMethodTypeMap = new HashMap<>();
    List<ContactMethodType> contactMethodTypes = contactMethodTypeRepository.getAllContactMethodTypes();
    for (ContactMethodType contactMethodType : contactMethodTypes) {
      contactMethodTypeMap.put(contactMethodType.getContactMethodType(), contactMethodType);
    }
    return contactMethodTypeMap;
  }

  private Map<String, AddressType> buildAddressTypeCache() {
    Map<java.lang.String, AddressType>  addressTypeMap = new HashMap<>();
    List<AddressType> addressTypes = donorRepository.getAllAddressTypes();
    for (AddressType addressType : addressTypes) {
      addressTypeMap.put(addressType.getPreferredAddressType(), addressType);
    }
    return addressTypeMap;
  }

  private String getErrorsString(BindException errors) {
    String errorsStr = errors.getAllErrors().size() + " errors:";
    for (ObjectError error : errors.getAllErrors()) {
      String fieldError = "";
      if (error instanceof FieldError) {
        fieldError = "Error in field " + ((FieldError) error).getField() + ": ";
      }
      errorsStr = errorsStr + "\n\t" + fieldError + error.getDefaultMessage();
    }
    return errorsStr;
  }

}
