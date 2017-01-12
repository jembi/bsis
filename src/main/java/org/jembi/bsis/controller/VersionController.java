package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jembi.bsis.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/version")
public class VersionController {

  @Autowired
  VersionService versionService;

  @RequestMapping(method = RequestMethod.GET)
  public Map<String, Object> getVersion(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("version", versionService.getVersion());
    return map;
  }
}
