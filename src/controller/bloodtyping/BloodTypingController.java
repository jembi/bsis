package controller.bloodtyping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.GenericConfigRepository;
import repository.rawbloodtests.RawBloodTestRepository;
import controller.UtilController;

@Controller
public class BloodTypingController {

  @Autowired
  private UtilController utilController;

  @Autowired
  private GenericConfigRepository genericConfigRepository;

  @Autowired
  private RawBloodTestRepository rawBloodTestRepository;

  public BloodTypingController() {
  }

  @RequestMapping(value="/bloodTypingWorksheetGenerator", method=RequestMethod.GET)
  public ModelAndView getBloodTypingWorksheet(HttpServletRequest request) {
    ModelAndView mv = new ModelAndView("bloodtyping/bloodTypingWorksheetForm");

    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "bloodtyping.plate.step1");
    mv.addObject("tips", tips);
    mv.addObject("plate", rawBloodTestRepository.getPlate("bloodtyping"));
    mv.addObject("bloodTestsOnPlate", rawBloodTestRepository.getRawBloodTestsForPlate("bloodtyping"));

    return mv;
  }

  @RequestMapping(value="/addCollectionsToBloodTypingPlate", method=RequestMethod.POST)
  public ModelAndView addCollectionsToBloodTypingPlate(HttpServletRequest request,
          @RequestParam(value="collectionNumbers[]") List<String> collectionNumbers) {
    ModelAndView mv = new ModelAndView("bloodtyping/bloodTypingWells");

    System.out.println(collectionNumbers);
    
    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "bloodtyping.plate.step1");
    mv.addObject("tips", tips);
    mv.addObject("plate", rawBloodTestRepository.getPlate("bloodtyping"));
    mv.addObject("bloodTestsOnPlate", rawBloodTestRepository.getRawBloodTestsForPlate("bloodtyping"));

    mv.addObject("bloodTypingConfig", genericConfigRepository.getConfigProperties("bloodTyping"));

    return mv;
  }
}
