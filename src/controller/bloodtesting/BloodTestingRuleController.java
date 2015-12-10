package controller.bloodtesting;

import backingform.BloodTestingRuleBackingForm;
import model.bloodtesting.rules.BloodTestingRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import repository.bloodtesting.BloodTestingRepository;
import utils.PermissionConstants;
import viewmodel.BloodTestingRuleViewModel;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bloodtestrules")
public class BloodTestingRuleController {

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TYPING_RULES + "')")
  public ResponseEntity configureBloodTypingTests() {
    Map<String, Object> map = new HashMap<>();
    map.put("bloodTypingTests", bloodTestingRepository.getBloodTypingTests());
    List<BloodTestingRuleViewModel> rules = new ArrayList<>();
    for (BloodTestingRule rule : bloodTestingRepository.getBloodTypingRules(true)) {
      rules.add(new BloodTestingRuleViewModel(rule));
    }
    map.put("bloodTypingRules", rules);
    return new ResponseEntity(map, HttpStatus.OK);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_BLOOD_TYPING_OUTCOME + "')")
  public ResponseEntity getBloodTypingRuleSummary(@PathVariable Integer id) {

    Map<String, Object> map = new HashMap<>();
    BloodTestingRuleViewModel bloodTestingRule;
    bloodTestingRule = new BloodTestingRuleViewModel(bloodTestingRepository.getBloodTestingRuleById(id));
    map.put("bloodTypingRule", bloodTestingRule);
    return new ResponseEntity(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTS + "')")
  public ResponseEntity saveBloodTypingRule(
          @RequestBody BloodTestingRuleBackingForm form) {

    Map<String, Object> map = new HashMap<>();
    BloodTestingRule typingRule = bloodTestingRepository.saveBloodTypingRule(form.getTypingRule());
    map.put("bloodtest", new BloodTestingRuleViewModel(typingRule));
    return new ResponseEntity(map, HttpStatus.CREATED);


  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTS + "')")
  public ResponseEntity updateNewBloodTypingRule(
          @RequestBody BloodTestingRuleBackingForm form, @PathVariable Integer id) {

    Map<String, Object> map = new HashMap<>();
    form.setId(id);
    BloodTestingRule typingRule = form.getTypingRule();
    bloodTestingRepository.updateBloodTypingRule(typingRule);
    map.put("bloodtest", new BloodTestingRuleViewModel(bloodTestingRepository.getBloodTestingRuleById(id)));
    return new ResponseEntity(map, HttpStatus.OK);

  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTS + "')")
  public ResponseEntity deleteBloodTypingRule(HttpServletRequest request,
                                              @PathVariable Integer id) {

    bloodTestingRepository.deleteBloodTestingRule(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

}
