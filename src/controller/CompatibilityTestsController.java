package controller;

import backingform.CompatibilityTestBackingForm;
import backingform.validator.CompatibilityTestBackingFormValidator;
import model.compatibility.CompatibilityTest;
import model.request.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import repository.CompatibilityTestRepository;
import repository.CrossmatchTypeRepository;
import repository.RequestRepository;
import utils.PermissionConstants;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("compatibility")
public class CompatibilityTestsController {

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private CompatibilityTestRepository compatibilityTestRepository;

  @Autowired
  private CrossmatchTypeRepository crossmatchTypeRepository;

  @Autowired
  private UtilController utilController;

  public CompatibilityTestsController() {
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
      reqUrl += "?" + queryString;
    }
    return reqUrl;
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new CompatibilityTestBackingFormValidator(binder.getValidator(), utilController));
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("crossmatchTypes", crossmatchTypeRepository.getAllCrossmatchTypes());
    utilController.addTipsToModel(m, "requests.addcompatibilityresult");
  }

  @RequestMapping(value = "{requestId}/edit/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.BLOOD_CROSS_MATCH_CHECK + "')")
  public ResponseEntity<Map<String, Object>> editCompatibilityTestsFormGenerator(
          @PathVariable String requestId) {

    Map<String, Object> m = new HashMap<>();
    addEditSelectorOptions(m);
    m.put("crossmatchForRequest", true);
    // to ensure custom field names are displayed in the form
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("CompatibilityTest");
    m.put("compatibilityTestFields", formFields);

    CompatibilityTestBackingForm form = new CompatibilityTestBackingForm();
    m.put("editCompatibilityTestForm", form);

    Request componentRequest = requestRepository.findRequestById(requestId);
    form.setForRequest(componentRequest);
    return new ResponseEntity<>(m, HttpStatus.OK);

  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.BLOOD_CROSS_MATCH_CHECK + "')")
  public ResponseEntity<Map<String, Object>>
  addCompatibilityTest(@Valid @RequestBody CompatibilityTestBackingForm form) {

    Map<String, Object> map = new HashMap<>();
    addEditSelectorOptions(map);
    CompatibilityTest crossmatchTest = form.getCompatibilityTest();
    crossmatchTest.setIsDeleted(false);
    compatibilityTestRepository.addCompatibilityTest(crossmatchTest);
    form = new CompatibilityTestBackingForm();
    map.put("editCompatibilityTestForm", form);
    map.put("existingCompatibilityTest", false);
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
}
