package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.location.LocationType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.LocationTypeRepository;

@Controller
public class LocationTypesController {

	@Autowired
	private LocationTypeRepository locationTypeRepository;

	@RequestMapping("/admin-locationTypesLandingPage")
	public ModelAndView locationTypesLandingPage(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("locationTypesLandingPage");
		Map<String, Object> model = new HashMap<String, Object>();
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-locationTypes")
	public ModelAndView locations(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("locationTypes");
		Map<String, Object> model = new HashMap<String, Object>();
		addAllLocationTypesToModel(model);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-addLocationType")
	public ModelAndView addLocation(@RequestParam Map<String, String> params,
			HttpServletRequest request) {

		LocationType locationType = new LocationType(params.get("name"),
				Boolean.FALSE, params.get("comments"));
		locationTypeRepository.saveLocationType(locationType);
		ModelAndView modelAndView = new ModelAndView("locationTypes");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("locationTypeAdded", true);
		model.put("locationType", locationType);
		model.put("hasLocationType", true);
		addAllLocationTypesToModel(model);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-updateLocationType")
	public ModelAndView updateLocation(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		LocationType locationType = new LocationType(params.get("name"),
				Boolean.FALSE, params.get("comments"));
		LocationType updatedLocationType = locationTypeRepository
				.updateLocationType(getParam(params, "locationTypeId"),
						locationType);
		ModelAndView modelAndView = new ModelAndView("locationTypes");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("locationTypeUpdated", true);
		model.put("locationType", updatedLocationType);
		model.put("hasLocationType", true);
		addAllLocationTypesToModel(model);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-deleteLocationType")
	public ModelAndView deleteLocation(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		locationTypeRepository.deleteLocationType(getParam(params,
				"locationTypeId"));
		ModelAndView modelAndView = new ModelAndView("locationTypes");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("locationTypeDeleted", true);
		model.put("locationTypeNameDeleted", params.get("name"));
		addAllLocationTypesToModel(model);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-selectLocationType")
	public ModelAndView selectDonor(@RequestParam Map<String, String> params,
			HttpServletRequest request) {

		LocationType locationType = locationTypeRepository
				.getLocationType(getParam(params, "selectedLocationTypeId"));
		ModelAndView modelAndView = new ModelAndView("locationTypes");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("locationType", locationType);
		model.put("hasLocationType", true);
		addAllLocationTypesToModel(model);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	private void addAllLocationTypesToModel(Map<String, Object> model) {
		List<LocationType> allLocationTypes = locationTypeRepository
				.getAllLocationTypes();
		model.put("allLocationTypes", allLocationTypes);
	}

	private Long getParam(Map<String, String> params, String paramName) {
		String paramValue = params.get(paramName);
		return paramValue == null || paramValue.isEmpty() ? null : Long
				.parseLong(paramValue);
	}
}
