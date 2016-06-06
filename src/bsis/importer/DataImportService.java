package bsis.importer;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import backingform.DeferralBackingForm;
import backingform.DeferralReasonBackingForm;
import backingform.DonationBackingForm;
import backingform.DonorBackingForm;
import backingform.LocationBackingForm;
import backingform.TestResultsBackingForm;
import backingform.validator.DeferralBackingFormValidator;
import backingform.validator.DonationBackingFormValidator;
import backingform.validator.DonorBackingFormValidator;
import backingform.validator.LocationBackingFormValidator;
import model.address.AddressType;
import model.address.ContactMethodType;
import model.adverseevent.AdverseEventType;
import model.bloodtesting.BloodTest;
import model.bloodtesting.rules.BloodTestingRule;
import model.bloodtesting.rules.DonationField;
import model.donation.Donation;
import model.donation.HaemoglobinLevel;
import model.donationbatch.DonationBatch;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;
import model.idtype.IdType;
import model.location.Location;
import model.packtype.PackType;
import model.preferredlanguage.PreferredLanguage;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import model.util.Gender;
import repository.AdverseEventTypeRepository;
import repository.ContactMethodTypeRepository;
import repository.DeferralReasonRepository;
import repository.DonationBatchRepository;
import repository.DonationRepository;
import repository.DonationTypeRepository;
import repository.DonorRepository;
import repository.LocationRepository;
import repository.PackTypeRepository;
import repository.SequenceNumberRepository;
import repository.TestBatchRepository;
import repository.bloodtesting.BloodTestingRepository;
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
  private DeferralReasonRepository deferralReasonRepository;
  @Autowired
  private DeferralBackingFormValidator deferralBackingFormValidator;
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
  @Autowired
  private TestBatchRepository testBatchRepository;
  @Autowired
  private BloodTestingRepository bloodTestingRepository;
  @PersistenceContext
  private EntityManager entityManager;

  private Map<String, Donor> externalDonorIdToBsisDonor = new HashMap<>();
  private Map<String, Long> donationIdentificationNumberToDonationId = new HashMap<>();

  private boolean validationOnly;
  private String action;


  public void importData(Workbook workbook, boolean validationOnly) {
    this.validationOnly = validationOnly;
    action = validationOnly ? "Validated" : "Imported";

    System.out.println("Started import at " + new Date());

    importLocationData(workbook.getSheet("Locations"));
    importDonorData(workbook.getSheet("Donors"));
    importDonationsData(workbook.getSheet("Donations"));
    importDeferralData(workbook.getSheet("Deferrals"));
    importOutcomeData(workbook.getSheet("Outcomes"));
    
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

    // Flush remaining data
    entityManager.flush();
    entityManager.clear();
  }

  private void importDonorData(Sheet sheet) {
    Map<String, Location> locationCache = buildLocationCache();
    Map<String, PreferredLanguage> preferredLanguageCache = buildPreferredLanguageCache();
    Map<String, IdType> idTypeCache = buildIdTypeCache();
    Map<String, ContactMethodType> contactMethodTypeCache = buildContactMethodTypeCache();
    Map<String, AddressType> addressTypeCache = buildAddressTypeCache();
    List<BloodTestingRule> bloodTestingRuleCache = bloodTestingRepository.getActiveBloodTestingRules();

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
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String bloodABO = cell.getStringCellValue();
            
            if (!bloodABO.isEmpty()) {
              if (isValidBloodTyping(bloodABO, DonationField.BLOODABO, bloodTestingRuleCache)) {
                donorBackingForm.setBloodAbo(bloodABO);
              } else {
                errors.rejectValue("donor.bloodAbo", "bloodAbo.invalid", "Invalid blood ABO value");
              }
            }
            break;

          case "bloodRh":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String bloodRh = cell.getStringCellValue();
            
            if (!bloodRh.isEmpty()) {
              if (isValidBloodTyping(bloodRh, DonationField.BLOODRH, bloodTestingRuleCache)) {
                donorBackingForm.setBloodRh(bloodRh);
              } else {
                errors.rejectValue("donor.bloodRh", "bloodRh.invalid", "Invalid blood Rh value");
              }
            }
            break;

          case "notes":
            donorBackingForm.setNotes(cell.getStringCellValue());
            break;

          case "venue":
            Location venue = locationCache.get(cell.getStringCellValue());
            donorBackingForm.setVenue(new LocationBackingForm(venue));
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
      Donor smallerDonor = new Donor();
      smallerDonor.setId(donor.getId());
      smallerDonor.setDonorNumber(donor.getDonorNumber());
      externalDonorIdToBsisDonor.put(externalDonorId, smallerDonor);

      // Periodically flush data
      if (donorCount % 250 == 0) {
        entityManager.flush();
        entityManager.clear();
      }
    }
    System.out.println(); // clear logging

    // Flush remaining data
    entityManager.flush();
    entityManager.clear();
  }

  private void importDonationsData(Sheet sheet) {
    Map<String, Location> locationCache = buildLocationCache();
    Map<String, DonationType> donationTypeCache = buildDonationTypeCache();
    Map<String, PackType> packTypeCache = buildPackTypeCache();
    Map<String, AdverseEventType> adverseEventTypeCache = buildAdverseEventTypeCache();
    Map<String, DonationBatch> donationBatches = new HashMap<>();
    Map<String, TestBatch> testBatches = new HashMap<>();
    List<BloodTestingRule> bloodTestingRuleCache = bloodTestingRepository.getActiveBloodTestingRules();

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
            donationBackingForm.setVenue(new LocationBackingForm(venue));
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
            } catch (Exception e) {
              errors.rejectValue("donation.donationDate", "donationDate.invalid", "Invalid donationDate");
              // setting new Date() as donationDate to be able to create the donation batch and
              // test batch and continue the validation
              donationBackingForm.setDonationDate(new Date());
            }
            break;

          case "bleedStartTime":
            try {
              donationBackingForm.setBleedStartTime(cell.getDateCellValue());
            } catch (Exception e) {
              errors.rejectValue("donation.bleedStartTime", "bleedStartTime.invalid", "Invalid bleedStartTime");
            }
            break;

          case "bleedEndTime":
            try {
              donationBackingForm.setBleedEndTime(cell.getDateCellValue());
            } catch (Exception e) {
              errors.rejectValue("donation.bleedEndTime", "bleedEndTime.invalid", "Invalid bleedEndTime");
            }
            break;

          case "donorWeight":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (!cell.getStringCellValue().isEmpty()) {
              try {
                donationBackingForm.setDonorWeight(BigDecimal.valueOf(Double.valueOf(cell.getStringCellValue())));
              } catch (Exception e) {
                errors.rejectValue("donation.donorWeight", "donorWeight.invalid", "Invalid donorWeight");
              }
            }
            break;

          case "bloodPressureSystolic":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (!cell.getStringCellValue().isEmpty()) {
              try {
                donationBackingForm.setBloodPressureSystolic(Integer.valueOf(cell.getStringCellValue()));
              } catch (Exception e) {
                errors.rejectValue("donation.bloodPressureSystolic", "bloodPressureSystolic.invalid",
                    "Invalid bloodPressureSystolic");
              }
            }
            break;

          case "bloodPressureDiastolic":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (!cell.getStringCellValue().isEmpty()) {
              try {
                donationBackingForm.setBloodPressureDiastolic(Integer.valueOf(cell.getStringCellValue()));
              } catch (Exception e) {
                errors.rejectValue("donation.bloodPressureDiastolic", "bloodPressureDiastolic.invalid",
                    "Invalid bloodPressureDiastolic");
              }
            }
            break;

          case "donorPulse":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (!cell.getStringCellValue().isEmpty()) {
              try {
                donationBackingForm.setDonorPulse(Integer.valueOf(cell.getStringCellValue()));
              } catch (Exception e) {
                errors.rejectValue("donation.donorPulse", "donorPulse.invalid", "Invalid donorPulse");
              }
            }
            break;

          case "haemoglobinCount":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (!cell.getStringCellValue().isEmpty()) {
              try {
                donationBackingForm.setHaemoglobinCount(BigDecimal.valueOf(Double.valueOf(cell.getStringCellValue())));
              } catch (Exception e) {
                errors.rejectValue("donation.haemoglobinCount", "haemoglobinCount.invalid", "Invalid haemoglobinCount");
              }
            }
            break;

          case "haemoglobinLevel":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (!cell.getStringCellValue().isEmpty()) {
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
            }
            break;

          case "adverseEventType":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (!cell.getStringCellValue().isEmpty()) {
              AdverseEventType adverseEventType = adverseEventTypeCache.get(cell.getStringCellValue());
              if (adverseEventType == null) {
                errors.rejectValue("donation.adverseEvent.type", "type.invalid", "Invalid adverseEventType");
                break;
              }
              adverseEventTypeBackingForm = new AdverseEventTypeBackingForm();
              adverseEventTypeBackingForm.setId(adverseEventType.getId());
            }
            break;

          case "adverseEventComment":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (!cell.getStringCellValue().isEmpty()) {
              adverseEventComment = cell.getStringCellValue();
            }
            break;
            
          case "bloodAbo":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String bloodABO = cell.getStringCellValue();
            if (!cell.getStringCellValue().isEmpty()) {
              if (isValidBloodTyping(bloodABO, DonationField.BLOODABO, bloodTestingRuleCache)) {
                donationBackingForm.setBloodAbo(bloodABO);
              } else {
                errors.rejectValue("donor.bloodAbo", "bloodAbo.invalid", "Invalid blood ABO value");
              }
            }
            break;

          case "bloodRh":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String bloodRh = cell.getStringCellValue();
            if (!cell.getStringCellValue().isEmpty()) {
              if (isValidBloodTyping(bloodRh, DonationField.BLOODRH, bloodTestingRuleCache)) {
                donationBackingForm.setBloodRh(bloodRh);
              } else {
                errors.rejectValue("donor.bloodRh", "bloodRh.invalid", "Invalid blood Rh value");
              }
            }
            break;

          case "notes":
            cell.setCellType(Cell.CELL_TYPE_STRING);
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
      Donor currentDonor = externalDonorIdToBsisDonor.get(externalDonorId);
      if (currentDonor != null) {
        donationBackingForm.setDonorNumber(currentDonor.getDonorNumber());
        donationBackingForm.setDonor(currentDonor);
      } else {
        throw new IllegalArgumentException(
            "Trying to create Donation but Donor doesn't exist for externalDonorId:" + externalDonorId);
      }

      // Get donation batch and validate
      DonationBatch donationBatch = getDonationBatch(donationBatches, testBatches, donationBackingForm.getDonationDate(),
          venue);
      donationBackingForm.setDonationBatch(donationBatch);
      donationBackingFormValidator.validate(donationBackingForm, errors);

      if (errors.hasErrors()) {
        System.out.println("Invalid donation on row " + (row.getRowNum() + 1) + ". " + getErrorsString(errors));
        throw new IllegalArgumentException("Invalid donation");
      }

      // Save donation
      Donation donation = donationCRUDService.createDonation(donationBackingForm);
      // Populate the cache for use later when importing outcomes
      donationIdentificationNumberToDonationId.put(donation.getDonationIdentificationNumber(), donation.getId());

      // Set bloodTypingStatus COMPLETE if bloodAbo and bloodRh are not empty
      if (StringUtils.isNotEmpty(donation.getBloodAbo()) && StringUtils.isNotEmpty(donation.getBloodRh())) {
        donation.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
        donationRepository.saveDonation(donation);
      }

      // Periodically flush data
      if (donationCount % 50 == 0) {
        entityManager.flush();
        entityManager.clear();
      }
    }
    System.out.println(); // clear logging

    // Flush remaining data
    entityManager.flush();
    entityManager.clear();
  }

  public void importDeferralData(Sheet sheet) {
    Map<String, DeferralReason> deferralReasonCache = buildDeferralReasonCache();
    Map<String, Location> locationCache = buildLocationCache();

    // Keep a reference to the row containing the headers
    Row headers = null;

    int deferralCount = 0;

    for (Row row : sheet) {

      Donor donor = null;
      String externalDonorId = null;

      if (headers == null) {
        headers = row;
        continue;
      }

      deferralCount += 1;

      DeferralBackingForm deferralBackingForm = new DeferralBackingForm();
      BindException errors = new BindException(deferralBackingForm, "DeferralBackingForm");

      for (Cell cell : row) {
        Cell header = headers.getCell(cell.getColumnIndex());

        switch (header.getStringCellValue()) {
          case "externalDonorId":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            externalDonorId = cell.getStringCellValue();
            donor = externalDonorIdToBsisDonor.get(externalDonorId);
            break;

          case "venue":
            deferralBackingForm.setVenue(new LocationBackingForm(locationCache.get(cell.getStringCellValue())));
            break;

          case "deferralReason":
            DeferralReasonBackingForm deferralReasonBackingForm = new DeferralReasonBackingForm();
            deferralReasonBackingForm.setDeferralReason(deferralReasonCache.get(cell.getStringCellValue()));
            deferralBackingForm.setDeferralReason(deferralReasonBackingForm);
            break;

          case "deferralReasonText":
            deferralBackingForm.setDeferralReasonText(cell.getStringCellValue());
            break;

          case "createdDate":
            try {
              deferralBackingForm.setDeferralDate(cell.getDateCellValue());
            } catch (IllegalStateException e) {
              errors.rejectValue("donorDeferral.createdDate", "createdDate.invalid", "Invalid createdDate");
            }
            break;

          case "deferredUntil":
            try {
              deferralBackingForm.setDeferredUntil(cell.getDateCellValue());
            } catch (IllegalStateException e) {
              errors.rejectValue("donorDeferral.deferredUntil", "deferredUntil.invalid", "Invalid deferredUntil");
            }
            break;

          default:
            System.out.println("Unknown deferral column: " + header.getStringCellValue());
            break;
        }
      }

      displayProgressMessage(action + " " + deferralCount + " out of " + sheet.getLastRowNum() + " deferral(s)");

      if (donor != null) {
        deferralBackingForm.setDeferredDonor(new DonorBackingForm(donor));
      } else {
        errors.rejectValue("donorDeferral.deferredDonor", "deferredDonor.invalid", "Invalid deferredDonor ");
      }

      deferralBackingFormValidator.validateForm(deferralBackingForm, errors);

      if (errors.hasErrors()) {
        System.out.println("Invalid deferral on row " + (row.getRowNum() + 1) + ". " + getErrorsString(errors));
        throw new IllegalArgumentException("Invalid deferral");
      }

      // Save deferral
      DonorDeferral donorDeferral = new DonorDeferral();
      donorDeferral.setDeferralDate(deferralBackingForm.getDeferralDate());
      donorDeferral.setDeferralReason(deferralBackingForm.getDeferralReason().getDeferralReason());
      donorDeferral.setDeferredDonor(deferralBackingForm.getDeferredDonor().getDonor());
      donorDeferral.setVenue(deferralBackingForm.getVenue().getLocation());
      donorDeferral.setDeferredUntil(deferralBackingForm.getDeferredUntil());
      donorDeferral.setDeferralReasonText(deferralBackingForm.getDeferralReasonText());
      donorRepository.deferDonor(donorDeferral);
    }

    System.out.println(); // clear logging

    // Flush remaining data
    entityManager.flush();
    entityManager.clear();
  }

  public void importOutcomeData(Sheet sheet) {
    Map<String, BloodTest> bloodTestCache = buildBloodTestCache();
    
    // Keep a reference to the row containing the headers
    Row headers = null;

    int testResultsCount = 0;

    for (Row row : sheet) {

      if (headers == null) {
        headers = row;
        continue;
      }

      testResultsCount += 1;

      // There's no existing TestResultBackingFormValidator, and the existing form is a bit
      // different from what the import requires.
      // We'll just use this form for presenting error messages.
      TestResultsBackingForm testOutcomeBackingForm = new TestResultsBackingForm();
      BindException errors = new BindException(testOutcomeBackingForm, "TestResultBackingForm");

      Date testedOn = null;
      Long donationId = null;
      BloodTest bloodTest = null;
      String outcome = null;
      Map<Long, String> testResults = new HashMap<>();

      for (Cell cell : row) {
        Cell header = headers.getCell(cell.getColumnIndex());

        switch (header.getStringCellValue()) {
          case "donationIdentificationNumber":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            donationId = donationIdentificationNumberToDonationId.get(cell.getStringCellValue());
            if (donationId == null) {
              errors.rejectValue("donationIdentificationNumber", "donationIdentificationNumber.invalid",
                  "Invalid donationIdentificationNumber");
            }
            break;

          case "testedOn":
            try {
              testedOn = cell.getDateCellValue();
            } catch (Exception e) {
              errors.rejectValue("testResults", "testResults.invalid", "Invalid date testedOn");
            }
            break;

          case "bloodTestName":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            bloodTest = bloodTestCache.get(cell.getStringCellValue());
            break;
            
          case "outcome":
            cell.setCellType(Cell.CELL_TYPE_STRING);
            outcome = cell.getStringCellValue();
            if (bloodTest != null) {
              testResults.put(bloodTest.getId(), cell.getStringCellValue());
              if (!bloodTest.getValidResultsList().contains(cell.getStringCellValue())) {
                errors.rejectValue("testResults", "testResults.invalid", "Invalid outcome");
              }
            }
            testOutcomeBackingForm.setTestResults(testResults);
            break;

          default:
            System.out.println("Unknown deferral column: " + header.getStringCellValue());
            break;
        }
      }

      // Do validation specific for test outcomes import (the system accepts saving empty test
      // outcomes, but the import doesn't)
      if (bloodTest == null) {
        errors.rejectValue("testResults", "testResults.required", "bloodTestName is required");
      }
      if (outcome == null) {
        errors.rejectValue("testResults", "testResults.required", "outcome is required");
      }
      if (testedOn == null) {
        errors.rejectValue("testResults", "testResults.required", "Date testedOn is required");
      }

      displayProgressMessage(
          action + " " + testResultsCount + " out of " + sheet.getLastRowNum() + " test outcome(s)");
      
      if (errors.hasErrors()) {
        System.out.println("Invalid outcome on row " + (row.getRowNum() + 1) + ". " + getErrorsString(errors));
        throw new IllegalArgumentException("Invalid test outcome");
      }

      Donation donation = new Donation();
      donation.setId(donationId);
      bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, testedOn, null, true);

      // Periodically flush data
      if (testResultsCount % 1000 == 0) {
        entityManager.flush();
        entityManager.clear();
      }
    }
    
    System.out.println(); // clear logging
    
    // Flush remaining data
    entityManager.flush();
    entityManager.clear();
  }
  
  private boolean isValidBloodTyping(String value, DonationField donationField, List<BloodTestingRule> bloodTestingRules) {
    for (BloodTestingRule bloodTestingRule : bloodTestingRules) {
      if (bloodTestingRule.getDonationFieldChanged().equals(donationField)) {
        if (value.equals(bloodTestingRule.getNewInformation())) {
          return true; // match found
        }
      }
    }
    return false; // never found a match for the value
  }

  private DonationBatch getDonationBatch(Map<String, DonationBatch> donationBatches, Map<String, TestBatch> testBatches,
      Date donationDate, Location venue) {

    String donationDateString = new SimpleDateFormat("yyyy-MM-dd").format(donationDate);

    // Get testBatch and save it if it hasn't been created yet for that date
    TestBatch testBatch = testBatches.get(donationDateString);

    if (testBatch == null) {
      testBatch = new TestBatch();
      testBatch.setBatchNumber(sequenceNumberRepository.getNextTestBatchNumber());
      testBatch.setStatus(TestBatchStatus.CLOSED);
      testBatch.setCreatedDate(donationDate);
      testBatchRepository.save(testBatch);
      testBatches.put(donationDateString, testBatch);
    }

    String key = donationDateString + "_" + venue;

    // Get donationBatch and save it if it hasn't been created yet for that date and venue
    DonationBatch donationBatch = donationBatches.get(key);

    if (donationBatch == null) {
      donationBatch = new DonationBatch();
      donationBatch.setBatchNumber(sequenceNumberRepository.getNextBatchNumber());
      donationBatch.setVenue(venue);
      donationBatch.setIsClosed(true);
      donationBatch.setBackEntry(true);
      donationBatch.setTestBatch(testBatch);
      donationBatch.setCreatedDate(donationDate);
      donationBatchRepository.addDonationBatch(donationBatch);
      donationBatches.put(key, donationBatch);
    }

    return donationBatch;
  }

  private Map<String, BloodTest> buildBloodTestCache() {
    Map <String, BloodTest> bloodTestCache = new HashMap<>();
    List<BloodTest> bloodTests = bloodTestingRepository.getAllBloodTestsIncludeInactive();
    for (BloodTest bloodTest : bloodTests) {
      bloodTestCache.put(bloodTest.getTestName(), bloodTest);
    }
    return bloodTestCache;
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
    Map<String, ContactMethodType>  contactMethodTypeMap = new HashMap<>();
    List<ContactMethodType> contactMethodTypes = contactMethodTypeRepository.getAllContactMethodTypes();
    for (ContactMethodType contactMethodType : contactMethodTypes) {
      contactMethodTypeMap.put(contactMethodType.getContactMethodType(), contactMethodType);
    }
    return contactMethodTypeMap;
  }

  private Map<String, AddressType> buildAddressTypeCache() {
    Map<String, AddressType>  addressTypeMap = new HashMap<>();
    List<AddressType> addressTypes = donorRepository.getAllAddressTypes();
    for (AddressType addressType : addressTypes) {
      addressTypeMap.put(addressType.getPreferredAddressType(), addressType);
    }
    return addressTypeMap;
  }

  private Map<String, DeferralReason> buildDeferralReasonCache() {
    Map<String, DeferralReason>  deferralReasonMap = new HashMap<>();
    List<DeferralReason> deferralReasons = deferralReasonRepository.getAllDeferralReasons();
    for (DeferralReason deferralReason : deferralReasons) {
      deferralReasonMap.put(deferralReason.getReason(), deferralReason);
    }
    return deferralReasonMap;
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
