package bsis.importer;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import backingform.AdverseEventBackingForm;
import backingform.AdverseEventTypeBackingForm;
import backingform.DonationBackingForm;
import backingform.DonorBackingForm;
import backingform.LocationBackingForm;
import backingform.validator.DonationBackingFormValidator;
import backingform.validator.DonorBackingFormValidator;
import backingform.validator.LocationBackingFormValidator;
import model.address.AddressType;
import model.address.ContactMethodType;
import model.adverseevent.AdverseEventType;
import model.donation.Donation;
import model.donation.HaemoglobinLevel;
import model.donationbatch.DonationBatch;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.idtype.IdType;
import model.location.Location;
import model.packtype.PackType;
import model.preferredlanguage.PreferredLanguage;
import model.util.Gender;
import repository.AdverseEventTypeRepository;
import repository.ContactMethodTypeRepository;
import repository.DonationBatchRepository;
import repository.DonationRepository;
import repository.DonationTypeRepository;
import repository.DonorRepository;
import repository.LocationRepository;
import repository.PackTypeRepository;
import repository.SequenceNumberRepository;
import repository.bloodtesting.BloodTypingStatus;
import service.DonationCRUDService;

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
  private SequenceNumberRepository sequenceNumberRepository;
  @Autowired
  private ContactMethodTypeRepository contactMethodTypeRepository;
  @Autowired
  private DonationTypeRepository donationTypeRepository;
  @Autowired
  private PackTypeRepository packTypeRepository;
  @Autowired
  private AdverseEventTypeRepository adverseEventTypeRepository;
  @Autowired
  private DonationBackingFormValidator donationBackingFormValidator;
  @Autowired
  private DonationRepository donationRepository;
  @Autowired
  private DonationBatchRepository donationBatchRepository;
  @Autowired
  private DonationCRUDService donationCRUDService;

  private Map<String, Donor> externalDonorIdToBsisId = new HashMap<>();

  private boolean validationOnly;
  private String action;


  public void importData(Workbook workbook, boolean validationOnly) {
    this.validationOnly = validationOnly;
    action = validationOnly ? "Validated" : "Imported";
    
    System.out.println("Started import at " + new Date());

    importLocationData(workbook.getSheet("Locations"));
    importDonorData(workbook.getSheet("Donors"));
    importDonationsData(workbook.getSheet("Donations"));
    
    System.out.println("Finished import at " + new Date());
    
    if (this.validationOnly) {
      throw new RollbackException();
    }
  }
  
  /**
   * Exception class to handle rollbacks for validation only executions
   */
  class RollbackException extends RuntimeException { private static final long serialVersionUID = 1L; }
  
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
      
      displayProgressMessage(action + " " + locationCount + " out of " + sheet.getLastRowNum() + " locations(s)");
      
      BindException errors = new BindException(locationBackingForm, "LocationBackingForm");
      locationBackingFormValidator.validate(locationBackingForm, errors);
      
      if (errors.hasErrors()) {
        System.out.println("Invalid location on row " + (row.getRowNum() + 1) + ". " + getErrorsString(errors));
        throw new IllegalArgumentException("Invalid location");
      }

      locationRepository.saveLocation(locationBackingForm.getLocation());
    }
    System.out.println(); // clear logging
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
      }
      
      donorCount += 1;

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
            String genderStr = cell.getStringCellValue();
            Gender gender = null;
            if (StringUtils.isNotEmpty(genderStr)) {
              try {
                gender = Gender.valueOf(genderStr);
              } catch (Exception e) {
                errors.rejectValue("donor.gender", "gender.invalid", "Invalid gender");
              }
            }
            donorBackingForm.setGender(gender);
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
      
      displayProgressMessage(action + " " + donorCount + " out of " + sheet.getLastRowNum() + " donor(s)");

      donorBackingFormValidator.validate(donorBackingForm, errors);

      if (errors.hasErrors()) {
        System.out.println("Invalid donor on row " + (row.getRowNum() + 1) + ". " + getErrorsString(errors));
        throw new IllegalArgumentException("Invalid donor");
      }
      
      // Save donation
      Donor donor = donorBackingForm.getDonor();
      donor.setContact(donorBackingForm.getContact());
      donor.setAddress(donorBackingForm.getAddress());
      donor.setDonorNumber(sequenceNumberRepository.getNextDonorNumber());
      donorRepository.addDonor(donor);
      
      // Cache new donor identifier
      externalDonorIdToBsisId.put(externalDonorId, donor);

    }
    System.out.println(); // clear logging
  }

  private void importDonationsData(Sheet sheet) {
    Map<String, Location> locationCache = buildLocationCache();
    Map<String, DonationType> donationTypeCache = buildDonationTypeCache();
    Map<String, PackType> packTypeCache = buildPackTypeCache();
    Map<String, AdverseEventType> adverseEventTypeCache = buildAdverseEventTypeCache();
    Map<String, DonationBatch> donationBatches = new HashMap<String, DonationBatch>();

    // Keep a reference to the row containing the headers
    Row headers = null;

    int donationCount = 0;

    for (Row row : sheet) {

      String externalDonorId = null;

      if (headers == null) {
        headers = row;
        continue;
      }
      
      donationCount += 1;

      DonationBackingForm donationBackingForm = new DonationBackingForm();
      BindException errors = new BindException(donationBackingForm, "DonationBackingForm");

      AdverseEventTypeBackingForm adverseEventTypeBackingForm = null;
      String adverseEventComment = null;
      Location venue = null;
      String donationDate = null;

      for (Cell cell : row) {

        Cell header = headers.getCell(cell.getColumnIndex());

        switch (header.getStringCellValue()) {

          case "externalDonorId":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            externalDonorId = cell.getStringCellValue();
            break;

          case "donationIdentificationNumber":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            donationBackingForm.setDonationIdentificationNumber(cell.getStringCellValue());
            break;

          case "venue":
            venue = locationCache.get(cell.getStringCellValue());
            donationBackingForm.setVenue(venue);
            break;

          case "donationType":
            donationBackingForm.setDonationType(donationTypeCache.get(cell.getStringCellValue()));
            break;

          case "packType":
            donationBackingForm.setPackType(packTypeCache.get(cell.getStringCellValue()));
            break;

          case "donationDate":
            try {
              donationBackingForm.setDonationDate(cell.getDateCellValue());
              donationDate = new SimpleDateFormat("yyyy-mm-dd").format(donationBackingForm.getDonationDate());
            } catch (IllegalStateException e) {
              errors.rejectValue("donation.donationDate", "donationDate.invalid", "Invalid donationDate");
            }
            break;

          case "bleedStartTime":
            donationBackingForm.setBleedStartTime(cell.getDateCellValue());
            break;

          case "bleedEndTime":
            donationBackingForm.setBleedEndTime(cell.getDateCellValue());
            break;

          case "donorWeight":
            donationBackingForm.setDonorWeight(BigDecimal.valueOf(cell.getNumericCellValue()));
            break;

          case "bloodPressureSystolic":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            donationBackingForm.setBloodPressureSystolic(Integer.valueOf(cell.getStringCellValue()));
            break;

          case "bloodPressureDiastolic":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            donationBackingForm.setBloodPressureDiastolic(Integer.valueOf(cell.getStringCellValue()));
            break;

          case "donorPulse":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            donationBackingForm.setDonorPulse(Integer.valueOf(cell.getStringCellValue()));
            break;

          case "haemoglobinCount":
            donationBackingForm.setHaemoglobinCount(BigDecimal.valueOf(cell.getNumericCellValue()));
            break;

          case "haemoglobinLevel":
            String haemoglobinLevelStr = cell.getStringCellValue();
            HaemoglobinLevel haemoglobinLevel = null;
            if (StringUtils.isNotEmpty(haemoglobinLevelStr)) {
              try {
                haemoglobinLevel = HaemoglobinLevel.valueOf(haemoglobinLevelStr);
              } catch (Exception e) {
                errors.rejectValue("donation.haemoglobinLevel", "haemoglobinLevel.invalid", "Invalid haemoglobinLevel");
              }
            }
            donationBackingForm.setHaemoglobinLevel(haemoglobinLevel);
            break;

          case "adverseEventType":
            AdverseEventType adverseEventType = adverseEventTypeCache.get(cell.getStringCellValue());
            if (adverseEventType == null) {
              errors.rejectValue("donation.adverseEvent.type", "type.invalid", "Invalid adverseEventType");
              break;
            }
            adverseEventTypeBackingForm = new AdverseEventTypeBackingForm();
            adverseEventTypeBackingForm.setId(adverseEventType.getId());
            break;

          case "adverseEventComment":
            adverseEventComment = cell.getStringCellValue();
            break;

          case "bloodAbo":
            donationBackingForm.setBloodAbo(cell.getStringCellValue());
            break;

          case "bloodRh":
            donationBackingForm.setBloodRh(cell.getStringCellValue());
            break;

          case "notes":
            donationBackingForm.setNotes(cell.getStringCellValue());
            break;

          default:
            System.out.println("Unknown donation column: " + header.getStringCellValue());
            break;
        }
      }
      
      displayProgressMessage(action + " " + donationCount + " out of " + sheet.getLastRowNum() + " donations(s)");

      // Set adverse event if present
      if (adverseEventTypeBackingForm != null) {
        AdverseEventBackingForm adverseEventBackingForm = new AdverseEventBackingForm();
        adverseEventBackingForm.setType(adverseEventTypeBackingForm);
        adverseEventBackingForm.setComment(adverseEventComment);
        donationBackingForm.setAdverseEvent(adverseEventBackingForm);
      }

      // Get donor
      Donor currentDonor = externalDonorIdToBsisId.get(externalDonorId);
      if (currentDonor != null) {
        donationBackingForm.setDonorNumber(currentDonor.getDonorNumber());
        donationBackingForm.setDonor(currentDonor);
      } else {
        throw new IllegalArgumentException(
            "Trying to create Donation but Donor doesn't exist for externalDonorId:" + externalDonorId);
      }

      // Get donation batch and validate
      DonationBatch donationBatch = getDonationBatch(donationBatches, donationDate, venue);
      donationBackingForm.setDonationBatch(donationBatch);
      donationBackingFormValidator.validate(donationBackingForm, errors);

      if (errors.hasErrors()) {
        System.out.println("Invalid donation on row " + (row.getRowNum() + 1) + ". " + getErrorsString(errors));
        throw new IllegalArgumentException("Invalid donation");
      }

      // Save donation
      Donation donation = donationCRUDService.createDonation(donationBackingForm);

      // Set bloodTypingStatus COMPLETE if bloodAbo and bloodRh are not empty
      if (StringUtils.isNotEmpty(donation.getBloodAbo()) && StringUtils.isNotEmpty(donation.getBloodRh())) {
        donation.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
        donationRepository.saveDonation(donation);
      }
    }
    System.out.println(); // clear logging
  }

  private DonationBatch getDonationBatch(Map<String, DonationBatch> donationBatches, String donationDate,
      Location venue) {

    String key = donationDate + "_" + venue;

    // Get donationBatch and save it if it hasn't been created yet for that date and venue
    DonationBatch donationBatch = donationBatches.get(key);

    if (donationBatch == null) {
      donationBatch = new DonationBatch();
      donationBatch.setBatchNumber(sequenceNumberRepository.getNextBatchNumber());
      donationBatch.setVenue(venue);
      donationBatch.setIsClosed(true);
      donationBatchRepository.addDonationBatch(donationBatch);
      donationBatches.put(key, donationBatch);
    }
    return donationBatch;
  }

  private Map<String, AdverseEventType> buildAdverseEventTypeCache() {
    Map<String, AdverseEventType> adverseEventTypeCache = new HashMap<>();
    List<AdverseEventType> adverseEventTypes = adverseEventTypeRepository.getAllAdverseEventTypes();
    for (AdverseEventType adverseEventType : adverseEventTypes) {
      adverseEventTypeCache.put(adverseEventType.getName(), adverseEventType);
    }
    return adverseEventTypeCache;
  }

  private Map<String, PackType> buildPackTypeCache() {
    Map<String, PackType> packTypeCache = new HashMap<>();
    List<PackType> packTypes = packTypeRepository.getAllEnabledPackTypes();
    for (PackType packType : packTypes) {
      packTypeCache.put(packType.getPackType(), packType);
    }
    return packTypeCache;
  }

  private Map<String, DonationType> buildDonationTypeCache() {
    Map<String, DonationType> donationTypeCache = new HashMap<>();
    List<DonationType> donationTypes = donationTypeRepository.getAllDonationTypes();
    for (DonationType donationType : donationTypes) {
      donationTypeCache.put(donationType.getDonationType(), donationType);
    }
    return donationTypeCache;
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

  private void displayProgressMessage(String progressMessage) {
    try {
      System.out.write((progressMessage+"\r").getBytes());
    } catch (Exception e) {
      // just ignore
    }
  }
}
