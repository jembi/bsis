package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import repository.LocationRepository;
import utils.PermissionConstants;
import viewmodel.LocationViewModel;

@RestController
@RequestMapping("locations")
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

    @RequestMapping(value = "/configure", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
    public 
    Map<String, Object> configureLocationsFormGenerator(
            HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        addAllLocationsToModel(map);
        map.put("refreshUrl", getUrl(request));
        return map;
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
    public 
    Map<String, Object> configureLocations(
            HttpServletRequest request, HttpServletResponse response,
            @RequestBody Map<String, Object> params) {
        List<Location> locations = new ArrayList<Location>();
        try {
            for (String id : params.keySet()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> paramValue = (Map<String, Object>) params.get(id);
                Location location = new Location();

                if (id.startsWith("newLocation")) {
                    location.setId(null);
                } else {
                    location.setId(Long.parseLong(id));
                }

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

    Map<String, Object> map = new HashMap<String, Object>();
    addAllLocationsToModel(map);
    map.put("refreshUrl", "configureLocationsFormGenerator.html");
    return map;
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
