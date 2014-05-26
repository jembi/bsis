package repository;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import labsetup.BloodTypingMechanism;
import labsetup.CrossmatchProcedure;
import labsetup.TTIMechanism;
import model.bloodtesting.BloodTestContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import repository.bloodtesting.BloodTestingRepository;

@Repository
@Transactional
public class LabSetupRepository {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;
  
  @Autowired
  private GenericConfigRepository genericConfigRepository;
  
  public void updateLabSetup(Map<String, String> params) {

    Map<String, String> configProperties = new HashMap<String, String>();

    BloodTypingMechanism bloodTypingMechanism = BloodTypingMechanism.valueOf(params.get("bloodTypingMechanism"));

    switch (bloodTypingMechanism) {
    case BLOODTYPING_TEST_RESULTS_ELISA:
      configProperties.put("bloodTypingContext", BloodTestContext.RECORD_BLOOD_TYPING_TESTS.toString());
      bloodTestingRepository.activateTests(BloodTestContext.RECORD_BLOOD_TYPING_TESTS);
      bloodTestingRepository.deactivateTests(BloodTestContext.RECORD_BLOOD_TYPING_OUTCOMES);
      configProperties.put("bloodTypingElisa", "true");
      configProperties.put("bloodTypingWorksheets", "false");
      break;
    case BLOODTYPING_TEST_RESULTS_WORKSHEETS:
      configProperties.put("bloodTypingContext", BloodTestContext.RECORD_BLOOD_TYPING_TESTS.toString());
      bloodTestingRepository.activateTests(BloodTestContext.RECORD_BLOOD_TYPING_TESTS);
      bloodTestingRepository.deactivateTests(BloodTestContext.RECORD_BLOOD_TYPING_OUTCOMES);
      configProperties.put("bloodTypingElisa", "false");
      configProperties.put("bloodTypingWorksheets", "true");
      break;
    case BLOODTYPING_OUTCOMES_WORKSHEETS:
      configProperties.put("bloodTypingContext", BloodTestContext.RECORD_BLOOD_TYPING_OUTCOMES.toString());
      bloodTestingRepository.activateTests(BloodTestContext.RECORD_BLOOD_TYPING_OUTCOMES);
      bloodTestingRepository.deactivateTests(BloodTestContext.RECORD_BLOOD_TYPING_TESTS);
      configProperties.put("bloodTypingElisa", "false");
      configProperties.put("bloodTypingWorksheets", "true");
      break;
    }

    configProperties.put("bloodTypingMechanism", bloodTypingMechanism.toString());

    TTIMechanism ttiMechanism = TTIMechanism.valueOf(params.get("ttiMechanism"));
    switch (ttiMechanism) {
    case TTI_ELISA:
      configProperties.put("ttiElisa", "true");
      configProperties.put("ttiWorksheets", "false");
      configProperties.put("ttiUploadResult", "false");
      configProperties.put("recordMachineReadingsForTTI", "true");
      break;
    case TTI_WORKSHEETS:
      configProperties.put("ttiElisa", "false");
      configProperties.put("ttiWorksheets", "true");
      configProperties.put("ttiUploadResult", "false");
      configProperties.put("recordMachineReadingsForTTI", "false");
      break;
    case TTI_RESULT:
    	configProperties.put("ttiElisa", "false");
      configProperties.put("ttiWorksheets", "false");
      configProperties.put("ttiUploadResult", "true");
      configProperties.put("recordMachineReadingsForTTI", "false");
      break;
    }

    if (configProperties.get("ttiWorksheets").equals("false") &&
        configProperties.get("bloodTypingWorksheets").equals("false")) {
      configProperties.put("useWorksheets", "false");
    } else {
      configProperties.put("useWorksheets", "true");
    }

    configProperties.put("ttiMechanism", ttiMechanism.toString());

    configProperties.put("recordUsage", params.get("recordUsage"));
    if (params.get("recordUsage").equals("false"))
      configProperties.put("usageTabEnabled", "false");
    else
      configProperties.put("usageTabEnabled", "true");

    CrossmatchProcedure crossmatchProcedure = CrossmatchProcedure.valueOf(params.get("crossmatchProcedure"));

    switch (crossmatchProcedure) {
    case CROSSMATCH_NOT_DONE:
      configProperties.put("showCrossmatchConfirmation", "false");
      configProperties.put("allowSkipCrossmatch", "true");
      break;
    case CROSSMATCH_DONE_CAN_SKIP:
      configProperties.put("showCrossmatchConfirmation", "true");
      configProperties.put("allowSkipCrossmatch", "true");
      break;
    case CROSSMATCH_DONE_CANNOT_SKIP:
      configProperties.put("showCrossmatchConfirmation", "true");
      configProperties.put("allowSkipCrossmatch", "false");
      break;
    }

    configProperties.put("crossmatchProcedure", crossmatchProcedure.toString());

    genericConfigRepository.updateConfigProperties("labsetup", configProperties);
  }
}
