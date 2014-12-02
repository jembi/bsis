package controller;

import backingform.LocationBackingForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import model.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
    public 
    Map<String, Object> configureLocationsFormGenerator(
            HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        addAllLocationsToModel(map);
        return map;
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
    public 
    ResponseEntity addLocation(
            @RequestBody @Valid LocationBackingForm formData) {
        Location location = formData.getLocation();
        locationRepository.saveLocation(location);
        return new ResponseEntity(new LocationViewModel(location), HttpStatus.CREATED);
        
  }
   
    @RequestMapping(value = "{id}" , method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
    public 
    ResponseEntity updateLocation(@PathVariable Long id,
            @RequestBody @Valid LocationBackingForm formData) {
        Map<String, Object> map = new HashMap<String, Object>();
        Location location = formData.getLocation();
        Location updatedLocation = locationRepository.updateLocation(id, location);
        map.put("location", new LocationViewModel(updatedLocation));
        return new ResponseEntity(map, HttpStatus.OK);
        
  }
   
   @RequestMapping(value = "{id}" , method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
    public 
    ResponseEntity getLocationById(@PathVariable Long id) {
        
        Map<String, Object> map = new HashMap<String, Object>();
        Location location = locationRepository.getLocation(id);
        map.put("location", new LocationViewModel(location));
        return new ResponseEntity(map, HttpStatus.OK);
        
  }
    
    @RequestMapping(value = "{id}" , method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_SITES + "')")
    public 
    ResponseEntity deleteLocation(@PathVariable Long id) {
        
        locationRepository.deleteLocation(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
        
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
