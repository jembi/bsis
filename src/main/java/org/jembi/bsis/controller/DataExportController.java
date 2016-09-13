package org.jembi.bsis.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jembi.bsis.service.export.DataExportService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dataexport")
public class DataExportController {

  @Autowired
  private DataExportService dataExportService;
  
  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.DATA_EXPORT + "')")
  public ResponseEntity<?> downloadDataExport(HttpServletResponse response) throws IOException {
    String fileName = "dataexport " + new SimpleDateFormat("yyyy-MM-dd HHmm").format(new Date()) + ".zip";
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      dataExportService.exportData(outputStream);
      response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
      response.setContentType("application/zip");
      return ResponseEntity.ok(outputStream.toByteArray());
    } finally {
      try {
        outputStream.close();
      } catch (IOException e) { }
    }
  }
}
