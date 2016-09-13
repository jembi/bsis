package org.jembi.bsis.service.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jembi.bsis.dto.DeferralExportDTO;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataExportService {
  
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  
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
  
  private void exportDonorData(OutputStreamWriter writer) throws IOException {
  }
  
  private void exportDonationData(OutputStreamWriter writer) throws IOException {
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
      deferralRecord.add(DATE_FORMAT.format(deferral.getCreatedDate()));
      deferralRecord.add(deferral.getCreatedBy());
      deferralRecord.add(DATE_FORMAT.format(deferral.getLastUpdated()));
      deferralRecord.add(deferral.getLastUpdatedBy());
      deferralRecord.add(deferral.getDeferralReasonText());
      deferralRecord.add(DATE_FORMAT.format(deferral.getDeferralDate()));
      deferralRecord.add(DATE_FORMAT.format(deferral.getDeferredUntil()));
      printer.printRecord(deferralRecord);
    }

    printer.flush();
  }
  
  private void exportBloodTestResultData(OutputStreamWriter writer) throws IOException {
  }
  
  private void exportComponentData(OutputStreamWriter writer) throws IOException {
  }

}
