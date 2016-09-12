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
    
    // Export deferral data
    zipOutputStream.putNextEntry(new ZipEntry("deferrals.csv"));
    exportDeferralData(writer);
    zipOutputStream.closeEntry();
    
    writer.close();
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

}
