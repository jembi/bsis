package org.jembi.bsis.service.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jembi.bsis.dto.DeferralExportDTO;
import org.jembi.bsis.dto.DonationExportDTO;
import org.jembi.bsis.dto.DonorExportDTO;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataExportService {
  
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private static final DateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm aaa");
  
  @Autowired
  private DonorRepository donorRepository;
  @Autowired
  private DonationRepository donationRepository;
  @Autowired
  private DonorDeferralRepository deferralRepository;
  
  public void exportData(OutputStream outputStream) throws IOException {
    ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
    OutputStreamWriter writer = new OutputStreamWriter(zipOutputStream);
    
    // Export donor data
    zipOutputStream.putNextEntry(new ZipEntry("Donors.csv"));
    exportDonorData(writer);
    zipOutputStream.closeEntry();

    // Export donation data
    zipOutputStream.putNextEntry(new ZipEntry("Donations.csv"));
    exportDonationData(writer);
    zipOutputStream.closeEntry();

    // Export post donation counselling data
    zipOutputStream.putNextEntry(new ZipEntry("Post-donation Counselling.csv"));
    exportPostDonationCounsellingData(writer);
    zipOutputStream.closeEntry();
    
    // Export deferral data
    zipOutputStream.putNextEntry(new ZipEntry("Deferrals.csv"));
    exportDeferralData(writer);
    zipOutputStream.closeEntry();
    
    // Export blood test result data
    zipOutputStream.putNextEntry(new ZipEntry("Test Outcomes.csv"));
    exportBloodTestResultData(writer);
    zipOutputStream.closeEntry();
    
    // Export component data
    zipOutputStream.putNextEntry(new ZipEntry("Components.csv"));
    exportComponentData(writer);
    zipOutputStream.closeEntry();
    
    writer.close();
  }
  
  @SuppressWarnings("resource")
  private void exportDonorData(OutputStreamWriter writer) throws IOException {
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
      donorRecord.add(donor.getWorkAddressState());
      donorRecord.add(donor.getWorkAddressCountry());
      donorRecord.add(donor.getWorkAddressZipcode());
      donorRecord.add(donor.getPostalAddressLine1());
      donorRecord.add(donor.getPostalAddressLine2());
      donorRecord.add(donor.getPostalAddressCity());
      donorRecord.add(donor.getPostalAddressProvince());
      donorRecord.add(donor.getPostalAddressDistrict());
      donorRecord.add(donor.getPostalAddressState());
      donorRecord.add(donor.getPostalAddressCountry());
      donorRecord.add(donor.getPostalAddressZipcode());
      printer.printRecord(donorRecord);
    }
    
    printer.flush();
  }
  
  @SuppressWarnings("resource")
  private void exportDonationData(OutputStreamWriter writer) throws IOException {
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
  
  private void exportPostDonationCounsellingData(OutputStreamWriter writer) throws IOException {
  }
  
  @SuppressWarnings("resource")
  private void exportDeferralData(OutputStreamWriter writer) throws IOException {
    CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

    // Write headers
    printer.printRecord(Arrays.asList("donorNumber", "createdDate", "createdBy", "lastUpdated", "lastUpdatedBy",
        "deferralReasonText", "deferralDate", "deferredUntil"));
    
    // Write rows
    for (DeferralExportDTO deferral : deferralRepository.findDeferralsForExport()) {
      List<String> deferralRecord = new ArrayList<>();
      deferralRecord.add(deferral.getDonorNumber());
      deferralRecord.add(formatDate(deferral.getCreatedDate()));
      deferralRecord.add(deferral.getCreatedBy());
      deferralRecord.add(formatDate(deferral.getLastUpdated()));
      deferralRecord.add(deferral.getLastUpdatedBy());
      deferralRecord.add(deferral.getDeferralReasonText());
      deferralRecord.add(formatDate(deferral.getDeferralDate()));
      deferralRecord.add(formatDate(deferral.getDeferredUntil()));
      printer.printRecord(deferralRecord);
    }

    printer.flush();
  }
  
  private void exportBloodTestResultData(OutputStreamWriter writer) throws IOException {
  }
  
  private void exportComponentData(OutputStreamWriter writer) throws IOException {
  }
  
  private String formatDate(Date date) {
    if (date == null) {
      return "";
    }
    return DATE_FORMAT.format(date);
  }
  
  private String formatTime(Date date) {
    if (date == null) {
      return "";
    }
    return TIME_FORMAT.format(date);
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
