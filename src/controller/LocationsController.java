package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.Location;
import model.LocationType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.LocationRepository;
import repository.LocationTypeRepository;
import viewmodel.LocationViewModel;

@Controller
public class LocationsController {

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private LocationTypeRepository locationTypeRepository;

	@RequestMapping("/admin-locationsLandingPage")
	public ModelAndView locationsLandingPage(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("locationsLandingPage");
		Map<String, Object> model = new HashMap<String, Object>();
		addAllLocationsToModel(model);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-locations")
	public ModelAndView locations(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("locations");
		Map<String, Object> model = new HashMap<String, Object>();
		addAllLocationsToModel(model);
		List<LocationType> allLocationTypes = locationTypeRepository
				.getAllLocationTypes();
		model.put("locationTypes", allLocationTypes);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-addLocation")
	public ModelAndView addLocation(@RequestParam Map<String, String> params,
			HttpServletRequest request) {

		Location location = new Location(params.get("name"), getParam(params,
				"locationType"), getBooleanParam(params, "center"),
				getBooleanParam(params, "collectionSite"), getBooleanParam(
						params, "usageSite"), getBooleanParam(params,
						"mobileSite"), Boolean.FALSE, params.get("comments"));
		locationRepository.saveLocation(location);
		ModelAndView modelAndView = new ModelAndView("locations");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("locationAdded", true);
		List<LocationType> allLocationTypes = locationTypeRepository
				.getAllLocationTypes();
		model.put("location", new LocationViewModel(location, allLocationTypes));
		model.put("hasLocation", true);
		addAllLocationsToModel(model);
		model.put("locationTypes", allLocationTypes);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-updateLocation")
	public ModelAndView updateLocation(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		Location location = new Location(params.get("name"), getParam(params,
				"locationType"), getBooleanParam(params, "center"),
				getBooleanParam(params, "collectionSite"), getBooleanParam(
						params, "usageSite"), getBooleanParam(params,
						"mobileSite"), Boolean.FALSE, params.get("comments"));
		Location updatedLocation = locationRepository.updateLocation(
				getParam(params, "locationId"), location);
		ModelAndView modelAndView = new ModelAndView("locations");
		Map<String, Object> model = new HashMap<String, Object>();
		List<LocationType> allLocationTypes = locationTypeRepository
				.getAllLocationTypes();
		model.put("location", new LocationViewModel(updatedLocation,
				allLocationTypes));
		model.put("locationUpdated", true);
		model.put("hasLocation", true);
		addAllLocationsToModel(model);
		model.put("locationTypes", allLocationTypes);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-deleteLocation")
	public ModelAndView deleteLocation(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		Long locationId = getParam(params, "locationId");
		locationRepository.deleteLocation(locationId);
		ModelAndView modelAndView = new ModelAndView("locations");
		Map<String, Object> model = new HashMap<String, Object>();
		List<LocationType> allLocationTypes = locationTypeRepository
				.getAllLocationTypes();
		model.put("locationDeleted", true);
		model.put("locationNameDeleted", params.get("name"));
		addAllLocationsToModel(model);
		model.put("locationTypes", allLocationTypes);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-selectLocation")
	public ModelAndView selectLocation(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		Location location = locationRepository.getLocation(getParam(params,
				"selectedLocationId"));
		ModelAndView modelAndView = new ModelAndView("locations");
		Map<String, Object> model = new HashMap<String, Object>();
		List<LocationType> allLocationTypes = locationTypeRepository
				.getAllLocationTypes();
		model.put("location", new LocationViewModel(location, allLocationTypes));
		model.put("hasLocation", true);
		addAllLocationsToModel(model);
		model.put("locationTypes", allLocationTypes);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	private Boolean getBooleanParam(Map<String, String> params, String paramName) {
		String paramValue = params.get(paramName);
		return StringUtils.hasText(paramValue) ? Boolean
				.parseBoolean(paramValue) : false;
	}

	private void addAllLocationsToModel(Map<String, Object> model) {
		List<Location> allLocations = locationRepository.getAllLocations();
		List<LocationType> allLocationTypes = locationTypeRepository
				.getAllLocationTypes();
		List<LocationViewModel> locations = new ArrayList<LocationViewModel>();
		for (Location allLocation : allLocations) {
			locations.add(new LocationViewModel(allLocation, allLocationTypes));
		}
		model.put("allLocations", locations);
	}

	private Long getParam(Map<String, String> params, String paramName) {
		String paramValue = params.get(paramName);
		return paramValue == null || paramValue.isEmpty() ? null : Long
				.parseLong(paramValue);
	}

}
