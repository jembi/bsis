package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.location.Location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.LocationRepository;
import viewmodel.LocationViewModel;

@Controller
public class LocationsController {

	@Autowired
	private LocationRepository locationRepository;

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
		model.put("location", new LocationViewModel(location));
		model.put("hasLocation", true);
		addAllLocationsToModel(model);
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
		List<LocationViewModel> locations = new ArrayList<LocationViewModel>();
		for (Location allLocation : allLocations) {
			locations.add(new LocationViewModel(allLocation));
		}
		model.put("allLocations", locations);
	}

	private Long getParam(Map<String, String> params, String paramName) {
		String paramValue = params.get(paramName);
		return paramValue == null || paramValue.isEmpty() ? null : Long
				.parseLong(paramValue);
	}

}
