package org.jembi.bsis.service.export;

import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DataExporter {
  
  public static void main(String[] args) throws IOException {
    // Load the application context
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/bsis-servlet.xml");

    // Get services
    DataExportService dataExportService = applicationContext.getBean(DataExportService.class);
    
    FileOutputStream outputStream = new FileOutputStream("/tmp/export.zip");
    dataExportService.exportData(outputStream);
    
    applicationContext.close();
  }

}
