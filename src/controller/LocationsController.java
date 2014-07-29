package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.location.Location;
;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.LocationRepository;
import utils.PermissionConstants;
import viewmodel.LocationViewModel;

@Controller
public class LocationsController {

  @Autowired
  private LocationRepository locationRepository;

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value="/configureLocationsFormGenerator", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DONATION_SITES+"')")
  public ModelAndView configureLocationsFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    ModelAndView mv = new ModelAndView("admin/configureLocations");
    Map<String, Object> m = model.asMap();
    addAllLocationsToModel(m);
    m.put("refreshUrl", getUrl(request));
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping("/configureLocations")
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DONATION_SITES+"')")
  public ModelAndView configureLocations(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(value="params") String paramsAsJson, Model model) {
    ModelAndView mv = new ModelAndView("admin/configureLocations");
    System.out.println(paramsAsJson);
    List<Location> locations = new ArrayList<Location>();
    try {
      @SuppressWarnings("unchecked")
      Map<String, Object> params = new ObjectMapper().readValue(paramsAsJson, HashMap.class);
      for (String id : params.keySet()) {
        @SuppressWarnings("unchecked")
        Map<String, Object> paramValue = (Map<String, Object>) params.get(id);
        Location location = new Location();

        if (id.startsWith("newLocation"))
          location.setId(null);
        else
          location.setId(Long.parseLong(id));

        location.setName((String) paramValue.get("name"));
        location.setIsCollectionCenter((Boolean) paramValue.get("isCenter"));
        location.setIsCollectionSite((Boolean) paramValue.get("isCollectionSite"));
        location.setIsUsageSite((Boolean) paramValue.get("isUsageSite"));

        locations.add(location);
      }
      locationRepository.saveAllLocations(locations);
      System.out.println(params);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    Map<String, Object> m = model.asMap();
    addAllLocationsToModel(m);
    m.put("refreshUrl", "configureLocationsFormGenerator.html");
    mv.addObject("model", model);
    return mv;
  }
  
  private void addAllLocationsToModel(Map<String, Object> model) {
    List<Location> allLocations = locationRepository.getAllLocations();
    List<LocationViewModel> locations = new ArrayList<LocationViewModel>();
    for (Location allLocation : allLocations) {
      locations.add(new LocationViewModel(allLocation));
    }
    model.put("allLocations", locations);
  }

}
