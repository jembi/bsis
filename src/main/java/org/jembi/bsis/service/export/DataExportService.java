package org.jembi.bsis.service.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.Logger;
import org.jembi.bsis.dto.BloodTestResultExportDTO;
import org.jembi.bsis.dto.ComponentExportDTO;
import org.jembi.bsis.dto.DeferralExportDTO;
import org.jembi.bsis.dto.DonationExportDTO;
import org.jembi.bsis.dto.DonorExportDTO;
import org.jembi.bsis.dto.PostDonationCounsellingExportDTO;
import org.jembi.bsis.repository.BloodTestResultRepository;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.service.DateGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataExportService {
  
  private static final Logger LOGGER = Logger.getLogger(DataExportService.class);
  
  @Autowired
  private DonorRepository donorRepository;
  @Autowired
  private DonationRepository donationRepository;
  @Autowired
  private PostDonationCounsellingRepository postDonationCounsellingRepository;
  @Autowired
  private DonorDeferralRepository deferralRepository;
  @Autowired
  private BloodTestResultRepository bloodTestResultRepository;
  @Autowired
  private ComponentRepository componentRepository;
  @Autowired
  private DateGeneratorService dateGeneratorService;
  
  public void exportData(OutputStream outputStream) throws IOException {
    LOGGER.info("Starting data export...");

    ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
    OutputStreamWriter writer = new OutputStreamWriter(zipOutputStream);
    
    Date currentDateTime = dateGeneratorService.generateDate(); 
    long now = currentDateTime.getTime();
    
    String dateTimeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(currentDateTime);
    
    // Export donor data
    ZipEntry donorsCSV = new ZipEntry("Donors" + dateTimeStamp + ".csv");
    donorsCSV.setTime(now);
    zipOutputStream.putNextEntry(donorsCSV);
    exportDonorData(writer);
    zipOutputStream.closeEntry();

    // Export donation data
    ZipEntry donationsCSV = new ZipEntry("Donations" + dateTimeStamp + ".csv");
    donationsCSV.setTime(now);
    zipOutputStream.putNextEntry(donationsCSV);
    exportDonationData(writer);
    zipOutputStream.closeEntry();

    // Export post donation counselling data
    ZipEntry postDonationCounsellingsCSV = new ZipEntry("Post-donation Counselling" + dateTimeStamp + ".csv");
    postDonationCounsellingsCSV.setTime(now);
    zipOutputStream.putNextEntry(postDonationCounsellingsCSV);
    exportPostDonationCounsellingData(writer);
    zipOutputStream.closeEntry();
    
    // Export deferral data
    ZipEntry deferralsCSV = new ZipEntry("Deferrals" + dateTimeStamp + ".csv");
    deferralsCSV.setTime(now);
    zipOutputStream.putNextEntry(deferralsCSV);
    exportDeferralData(writer);
    zipOutputStream.closeEntry();
    
    // Export blood test result data
    ZipEntry testOutcomesCSV = new ZipEntry("Test Outcomes" + dateTimeStamp + ".csv");
    testOutcomesCSV.setTime(now);
    zipOutputStream.putNextEntry(testOutcomesCSV);
    exportBloodTestResultData(writer);
    zipOutputStream.closeEntry();
    
    // Export component data
    ZipEntry componentsCSV = new ZipEntry("Components" + dateTimeStamp + ".csv");
    componentsCSV.setTime(now);
    zipOutputStream.putNextEntry(componentsCSV);
    exportComponentData(writer);
    zipOutputStream.closeEntry();
    
    zipOutputStream.finish();
    LOGGER.info("Data export complete.");
  }
  
  @SuppressWarnings("resource")
  private void exportDonorData(OutputStreamWriter writer) throws IOException {
    LOGGER.debug("Exporting donor data...");

    CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

    // Write headers
    printer.printRecord(Arrays.asList("donorNumber", "createdDate", "createdBy", "lastUpdated", "lastUpdatedBy",
        "title", "firstName", "middleName", "lastName", "callingName", "gender", "birthDate", "preferredLanguage",
        "venue", "bloodABO", "bloodRh", "notes", "idType", "idNumber", "dateOfFirstDonation", "dateOfLastDonation",
        "dueToDonate", "contactMethodType", "mobileNumber", "homeNumber", "workNumber", "email", "preferredAddressType",
        "homeAddressLine1", "homeAddressLine2", "homeAddressCity", "homeAddressProvince", "homeAddressDistrict",
        "homeAddressState", "homeAddressCountry", "homeAddressZipcode", "workAddressLine1", "workAddressLine2",
        "workAddressCity", "workAddressProvince", "workAddressDistrict", "workAddressCountry", "workAddressState",
        "workAddressZipcode", "postalAddressLine1", "postalAddressLine2", "postalAddressCity", "postalAddressProvince",
        "postalAddressDistrict", "postalAddressCountry", "postalAddressState", "postalAddressZipcode"));
    
    // Write rows
    for (DonorExportDTO donor : donorRepository.findDonorsForExport()) {
      List<String> donorRecord = new ArrayList<>();
      donorRecord.add(donor.getDonorNumber());
      donorRecord.add(formatDate(donor.getCreatedDate()));
      donorRecord.add(donor.getCreatedBy());
      donorRecord.add(formatDate(donor.getLastUpdated()));
      donorRecord.add(donor.getLastUpdatedBy());
      donorRecord.add(donor.getTitle());
      donorRecord.add(donor.getFirstName());
      donorRecord.add(donor.getMiddleName());
      donorRecord.add(donor.getLastName());
      donorRecord.add(donor.getCallingName());
      donorRecord.add(formatObject(donor.getGender()));
      donorRecord.add(formatDate(donor.getBirthDate()));
      donorRecord.add(donor.getPreferredLanguage());
      donorRecord.add(donor.getVenue());
      donorRecord.add(donor.getBloodABO());
      donorRecord.add(donor.getBloodRh());
      donorRecord.add(donor.getNotes());
      donorRecord.add(donor.getIdType());
      donorRecord.add(donor.getIdNumber());
      donorRecord.add(formatDate(donor.getDateOfFirstDonation()));
      donorRecord.add(formatDate(donor.getDateOfLastDonation()));
      donorRecord.add(formatDate(donor.getDueToDonate()));
      donorRecord.add(donor.getContactMethodType());
      donorRecord.add(donor.getMobileNumber());
      donorRecord.add(donor.getHomeNumber());
      donorRecord.add(donor.getWorkNumber());
      donorRecord.add(donor.getEmail());
      donorRecord.add(donor.getPreferredAddressType());
      donorRecord.add(donor.getHomeAddressLine1());
      donorRecord.add(donor.getHomeAddressLine2());
      donorRecord.add(donor.getHomeAddressCity());
      donorRecord.add(donor.getHomeAddressProvince());
      donorRecord.add(donor.getHomeAddressDistrict());
      donorRecord.add(donor.getHomeAddressState());
      donorRecord.add(donor.getHomeAddressCountry());
      donorRecord.add(donor.getHomeAddressZipcode());
      donorRecord.add(donor.getWorkAddressLine1());
      donorRecord.add(donor.getWorkAddressLine2());
      donorRecord.add(donor.getWorkAddressCity());
      donorRecord.add(donor.getWorkAddressProvince());
      donorRecord.add(donor.getWorkAddressDistrict());
      donorRecord.add(donor.getWorkAddressCountry());
      donorRecord.add(donor.getWorkAddressState());
      donorRecord.add(donor.getWorkAddressZipcode());
      donorRecord.add(donor.getPostalAddressLine1());
      donorRecord.add(donor.getPostalAddressLine2());
      donorRecord.add(donor.getPostalAddressCity());
      donorRecord.add(donor.getPostalAddressProvince());
      donorRecord.add(donor.getPostalAddressDistrict());
      donorRecord.add(donor.getPostalAddressCountry());
      donorRecord.add(donor.getPostalAddressState());
      donorRecord.add(donor.getPostalAddressZipcode());
      printer.printRecord(donorRecord);
    }
    
    printer.flush();
  }
  
  @SuppressWarnings("resource")
  private void exportDonationData(OutputStreamWriter writer) throws IOException {
    LOGGER.debug("Exporting donation data...");

    CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

    // Write headers
    printer.printRecord(Arrays.asList("donorNumber", "donationIdentificationNumber", "createdDate", "createdBy",
        "lastUpdated", "lastUpdatedBy", "packType", "donationDate", "bloodTypingStatus", "bloodTypingMatchStatus",
        "ttiStatus", "bleedStartTime", "bleedEndTime", "donorWeight", "bloodPressureSystolic", "bloodPressureDiastolic",
        "donorPulse", "haemoglobinCount", "haemoglobinLevel", "adverseEventType", "adverseEventComment", "bloodAbo",
        "bloodRh", "released", "ineligbleDonor", "notes"));
    
    // Write rows
    for (DonationExportDTO donation : donationRepository.findDonationsForExport()) {
      List<String> donationRecord = new ArrayList<>();
      donationRecord.add(donation.getDonorNumber());
      donationRecord.add(donation.getDonationIdentificationNumber());
      donationRecord.add(formatDate(donation.getCreatedDate()));
      donationRecord.add(donation.getCreatedBy());
      donationRecord.add(formatDate(donation.getLastUpdated()));
      donationRecord.add(donation.getLastUpdatedBy());
      donationRecord.add(donation.getPackType());
      donationRecord.add(formatDate(donation.getDonationDate()));
      donationRecord.add(formatObject(donation.getBloodTypingStatus()));
      donationRecord.add(formatObject(donation.getBloodTypingMatchStatus()));
      donationRecord.add(formatObject(donation.getTtiStatus()));
      donationRecord.add(formatTime(donation.getBleedStartTime()));
      donationRecord.add(formatTime(donation.getBleedEndTime()));
      donationRecord.add(formatObject(donation.getDonorWeight()));
      donationRecord.add(formatObject(donation.getBloodPressureSystolic()));
      donationRecord.add(formatObject(donation.getBloodPressureDiastolic()));
      donationRecord.add(formatObject(donation.getDonorPulse()));
      donationRecord.add(formatObject(donation.getHaemoglobinCount()));
      donationRecord.add(formatObject(donation.getHaemoglobinLevel()));
      donationRecord.add(donation.getAdverseEventType());
      donationRecord.add(donation.getAdverseEventComment());
      donationRecord.add(donation.getBloodAbo());
      donationRecord.add(donation.getBloodRh());
      donationRecord.add(formatBoolean(donation.isReleased()));
      donationRecord.add(formatBoolean(donation.isIneligbleDonor()));
      donationRecord.add(donation.getNotes());
      printer.printRecord(donationRecord);
    }
    
    printer.flush();
  }
  
  @SuppressWarnings("resource")
  private void exportPostDonationCounsellingData(OutputStreamWriter writer) throws IOException {
    LOGGER.debug("Exporting post donation counselling data...");

    CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

    // Write headers
    printer.printRecord(Arrays.asList("donationIdentificationNumber", "createdDate", "createdBy", "lastUpdated",
        "lastUpdatedBy", "counsellingDate"));
    
    // Write rows
    for (PostDonationCounsellingExportDTO counselling : postDonationCounsellingRepository.findPostDonationCounsellingsForExport()) {
      List<String> counsellingRecord = new ArrayList<>();
      counsellingRecord.add(counselling.getDonationIdentificationNumber());
      counsellingRecord.add(formatDate(counselling.getCreatedDate()));
      counsellingRecord.add(counselling.getCreatedBy());
      counsellingRecord.add(formatDate(counselling.getLastUpdated()));
      counsellingRecord.add(counselling.getLastUpdatedBy());
      counsellingRecord.add(formatDate(counselling.getCounsellingDate()));
      printer.printRecord(counsellingRecord);
    }
    
    printer.flush();
  }
  
  @SuppressWarnings("resource")
  private void exportDeferralData(OutputStreamWriter writer) throws IOException {
    LOGGER.debug("Exporting deferral data...");

    CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

    // Write headers
    printer.printRecord(Arrays.asList("donorNumber", "createdDate", "createdBy", "lastUpdated", "lastUpdatedBy",
        "deferralReason", "deferralReasonText", "deferralDate", "deferredUntil"));
    
    // Write rows
    for (DeferralExportDTO deferral : deferralRepository.findDeferralsForExport()) {
      List<String> deferralRecord = new ArrayList<>();
      deferralRecord.add(deferral.getDonorNumber());
      deferralRecord.add(formatDate(deferral.getCreatedDate()));
      deferralRecord.add(deferral.getCreatedBy());
      deferralRecord.add(formatDate(deferral.getLastUpdated()));
      deferralRecord.add(deferral.getLastUpdatedBy());
      deferralRecord.add(deferral.getDeferralReason());
      deferralRecord.add(deferral.getDeferralReasonText());
      deferralRecord.add(formatDate(deferral.getDeferralDate()));
      deferralRecord.add(formatDate(deferral.getDeferredUntil()));
      printer.printRecord(deferralRecord);
    }

    printer.flush();
  }
  
  @SuppressWarnings("resource")
  private void exportBloodTestResultData(OutputStreamWriter writer) throws IOException {
    LOGGER.debug("Exporting blood test result data...");

    CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

    // Write headers
    printer.printRecord(Arrays.asList("donationIdentificationNumber", "createdDate", "createdBy", "lastUpdated",
        "lastUpdatedBy", "testName", "outcome"));
    
    // Write rows
    for (BloodTestResultExportDTO bloodTestResult : bloodTestResultRepository.findBloodTestResultsForExport()) {
      List<String> bloodTestResultRecord = new ArrayList<>();
      bloodTestResultRecord.add(bloodTestResult.getDonationIdentificationNumber());
      bloodTestResultRecord.add(formatDate(bloodTestResult.getCreatedDate()));
      bloodTestResultRecord.add(bloodTestResult.getCreatedBy());
      bloodTestResultRecord.add(formatDate(bloodTestResult.getLastUpdated()));
      bloodTestResultRecord.add(bloodTestResult.getLastUpdatedBy());
      bloodTestResultRecord.add(bloodTestResult.getTestName());
      bloodTestResultRecord.add(bloodTestResult.getResult());
      printer.printRecord(bloodTestResultRecord);
    }
    
    printer.flush();
  }
  
  @SuppressWarnings("resource")
  private void exportComponentData(OutputStreamWriter writer) throws IOException {
    LOGGER.debug("Exporting component data...");

    CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

    // Write headers
    printer.printRecord(Arrays.asList("donationIdentificationNumber", "componentCode", "createdDate", "createdBy",
        "lastUpdated", "lastUpdatedBy", "parentComponentCode", "createdOn", "status", "location", "issuedOn",
        "inventoryStatus", "discardedOn", "discardReason", "expiresOn", "notes"));
    
    // Write rows
    for (ComponentExportDTO component : componentRepository.findComponentsForExport()) {
      List<String> componentRecord = new ArrayList<>();
      componentRecord.add(component.getDonationIdentificationNumber());
      componentRecord.add(component.getComponentCode());
      componentRecord.add(formatDate(component.getCreatedDate()));
      componentRecord.add(component.getCreatedBy());
      componentRecord.add(formatDate(component.getLastUpdated()));
      componentRecord.add(component.getLastUpdatedBy());
      componentRecord.add(component.getParentComponentCode());
      componentRecord.add(formatDate(component.getCreatedOn()));
      componentRecord.add(formatObject(component.getStatus()));
      componentRecord.add(component.getLocation());
      componentRecord.add(formatDate(component.getIssuedOn()));
      componentRecord.add(formatObject(component.getInventoryStatus()));
      componentRecord.add(formatDate(component.getDiscardedOn()));
      componentRecord.add(component.getDiscardReason());
      componentRecord.add(formatDate(component.getExpiresOn()));
      componentRecord.add(component.getNotes());
      printer.printRecord(componentRecord);
    }
    
    printer.flush();
  }
  
  private String formatDate(Date date) {
    if (date == null) {
      return "";
    }
    return new SimpleDateFormat("yyyy-MM-dd").format(date);
  }
  
  private String formatTime(Date date) {
    if (date == null) {
      return "";
    }
    return new SimpleDateFormat("hh:mm aaa").format(date);
  }
  
  private String formatBoolean(Boolean value) {
    if (value == null) {
      return "";
    }
    return value ? "Y" : "N";
  }
  
  private String formatObject(Object object) {
    if (object == null) {
      return "";
    }
    return object.toString();
  }

}
