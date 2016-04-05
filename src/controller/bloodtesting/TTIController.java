package controller.bloodtesting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestType;
import model.donation.Donation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import repository.DonationRepository;
import repository.GenericConfigRepository;
import repository.WellTypeRepository;
import repository.bloodtesting.BloodTestingRepository;
import utils.PermissionConstants;
import viewmodel.BloodTestViewModel;
import viewmodel.BloodTestingRuleResult;
import viewmodel.DonationViewModel;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("ttitests")
public class TTIController {

  private static final Logger LOGGER = Logger.getLogger(TTIController.class);

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  @Autowired
  private WellTypeRepository wellTypeRepository;

  public TTIController() {
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_TTI_OUTCOME + "')")
  public Map<String, Object> getTTIForm(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();

    List<BloodTestViewModel> basicTTITests = getBasicTTITests();
    map.put("basicTTITests", basicTTITests);

    List<BloodTestViewModel> pendingTTITests = getConfirmatoryTTITests();
    map.put("pendingTTITests", pendingTTITests);

    return map;
  }

  public List<BloodTestViewModel> getBasicTTITests() {
    List<BloodTestViewModel> tests = new ArrayList<BloodTestViewModel>();
    for (BloodTest rawBloodTest : bloodTestingRepository
        .getBloodTestsOfType(BloodTestType.BASIC_TTI)) {
      tests.add(new BloodTestViewModel(rawBloodTest));
    }
    return tests;
  }

  public List<BloodTestViewModel> getConfirmatoryTTITests() {
    List<BloodTestViewModel> tests = new ArrayList<BloodTestViewModel>();
    for (BloodTest rawBloodTest : bloodTestingRepository
        .getBloodTestsOfType(BloodTestType.CONFIRMATORY_TTI)) {
      tests.add(new BloodTestViewModel(rawBloodTest));
    }
    return tests;
  }

  @RequestMapping(value = "/results/{donationId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TTI_OUTCOME + "')")
  public Map<String, Object> showTTIResultsForDonation(HttpServletRequest request,
                                                       @PathVariable String donationId) {
    Map<String, Object> map = new HashMap<String, Object>();
    donationId = donationId.trim();
    Long donationIdLong = Long.parseLong(donationId);
    Donation donation = donationRepository
        .findDonationById(donationIdLong);
    // using test status to find existing test results and determine pending
    // tests
    BloodTestingRuleResult ruleResult = bloodTestingRepository
        .getAllTestsStatusForDonation(donationIdLong);
    map.put("donation", new DonationViewModel(donation));
    map.put("overview", ruleResult);

    return map;
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "results/onplate", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_TTI_OUTCOME + "')")
  public Map<String, Object> saveTTIResultsOnPlate(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @RequestParam(value = "ttiTestId") Long ttiTestId,
                                                   @RequestParam(value = "ttiResults") String ttiResults) {

    Map<String, Object> map = new HashMap<String, Object>();

    ObjectMapper mapper = new ObjectMapper();
    boolean success = false;
    try {
      Map<String, Map<String, Object>> ttiResultsMap = mapper.readValue(
          ttiResults, HashMap.class);
      Map<String, Object> results = bloodTestingRepository
          .saveTTIResultsOnPlate(ttiResultsMap, ttiTestId);
      if (results.get("errorsFound").equals(false))
        success = true;

      map.put("errorsByWellNumber",
          results.get("errorsByWellNumber"));
      map.put("errorsByWellNumberAsJSON", mapper
          .writeValueAsString(results.get("errorsByWellNumber")));
      map.put("errorsByWellNumber",
          results.get("errorsByWellNumber"));
      map.put("errorsByWellNumberAsJSON", mapper
          .writeValueAsString(results.get("errorsByWellNumber")));
      map.put("donations", results.get("donations"));
      map.put("bloodTestingResults",
          results.get("bloodTestingResults"));
    } catch (JsonParseException e) {
      LOGGER.error(e.getMessage() + e.getStackTrace());
    } catch (JsonMappingException e) {
      LOGGER.error(e.getMessage() + e.getStackTrace());
    } catch (IOException e) {
      LOGGER.error(e.getMessage() + e.getStackTrace());
    }

    map.put("success", success);
    if (!success) {
      map.put("errorMessage",
          "Please correct the errors on the highlighted wells before proceeding.");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    map.put("plate", bloodTestingRepository.getPlate("tti"));
    map.put("ttiTestId", ttiTestId);
    map.put("ttiTestResults", ttiResults);
    map.put("ttiTest",
        bloodTestingRepository.findBloodTestById(ttiTestId));
    map.put("ttiConfig",
        genericConfigRepository.getConfigProperties("ttiWells"));
    map.put("allWellTypes", wellTypeRepository.getAllWellTypes());

    return map;
  }
}
